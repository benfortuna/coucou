package org.mnode.coucou

import groovy.xml.MarkupBuilder;
import groovyx.gpars.GParsExecutorsPool;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static org.jdesktop.swingx.JXStatusBar.Constraint.ResizeBehavior.*

import javax.jcr.PropertyType;
import javax.jcr.SimpleCredentials;
import javax.jcr.observation.EventListener;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MailDateFormat;
import javax.naming.InitialContext;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.text.html.StyleSheet;
import javax.swing.JSplitPane;


import org.apache.jackrabbit.core.jndi.RegistryHelper;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXStatusBar;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.prompt.BuddySupport;
import org.jdesktop.swingx.prompt.PromptSupport.FocusBehavior;
import org.mnode.coucou.activity.DateExpansionModel;
import org.mnode.coucou.breadcrumb.PathResultCallback;
import org.mnode.coucou.contacts.ContactsManager;
import org.mnode.coucou.feed.Aggregator;
import org.mnode.coucou.mail.Mailbox;
import org.mnode.coucou.planner.Planner;
import org.mnode.coucou.search.SearchPathResult;
import org.mnode.juicer.query.QueryBuilder;
import org.mnode.ousia.HTMLEditorKitExt;
import org.mnode.ousia.HyperlinkBrowser;
import org.mnode.ousia.OusiaBuilder;
import org.pushingpixels.flamingo.api.bcb.BreadcrumbPathListener;
import org.pushingpixels.flamingo.api.common.JCommandButton.CommandButtonKind;
import org.pushingpixels.flamingo.api.common.JCommandButton.CommandButtonPopupOrientationKind;
import org.pushingpixels.flamingo.api.common.popup.PopupPanelCallback;
import org.pushingpixels.flamingo.api.ribbon.JRibbonBand;
import org.pushingpixels.flamingo.api.ribbon.RibbonElementPriority;
import org.pushingpixels.flamingo.api.ribbon.RibbonTask;
import org.pushingpixels.flamingo.api.ribbon.resize.CoreRibbonResizePolicies;
import org.pushingpixels.substance.api.SubstanceConstants;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.slf4j.LoggerFactory;
import javax.jcr.Node;

import org.pushingpixels.flamingo.api.bcb.BreadcrumbItem;


import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.Filterator;
import ca.odell.glazedlists.TextFilterator;
import ca.odell.glazedlists.TreeList;
import ca.odell.glazedlists.TreeList.Format;
import ca.odell.glazedlists.event.ListEventListener;
import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.glazedlists.matchers.CompositeMatcherEditor;
import ca.odell.glazedlists.matchers.MatcherEditor;
import ca.odell.glazedlists.swing.EventTableModel;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;
import ca.odell.glazedlists.swing.TreeTableSupport;

import org.mnode.base.log.FormattedLogEntry;
import org.mnode.base.log.LogEntry.Level;
import org.mnode.base.log.LogEntry;
import org.mnode.base.log.LogAdapter;
import org.mnode.base.log.adapter.Slf4jAdapter;

LogAdapter log = new Slf4jAdapter(LoggerFactory.getLogger(Coucou))
LogEntry unexpected_error = new FormattedLogEntry(Level.Error, 'An unexpected error has occurred')

UIManager.put(SubstanceLookAndFeel.TABBED_PANE_CONTENT_BORDER_KIND, SubstanceConstants.TabContentPaneBorderKind.SINGLE_FULL)

//def repoConfig = RepositoryConfig.create(Coucou.getResource("/config.xml").toURI(), new File(System.getProperty("user.home"), ".coucou/data").absolutePath)
//def repository = new TransientRepository(repoConfig)

def context = new InitialContext()
RegistryHelper.registerRepository(context, 'coucou', Coucou.getResource("/config.xml").file,
	 new File(System.getProperty("user.home"), ".coucou/data").absolutePath, false)
def repository = context.lookup('coucou')

def session = repository.login(new SimpleCredentials('readonly', ''.toCharArray()))
Runtime.getRuntime().addShutdownHook({
//	session.logout()
    RegistryHelper.unregisterRepository(context, 'coucou')
})

def mailbox = new Mailbox(repository, 'Mail')
mailbox.start()

def mailDateFormat = new MailDateFormat()

// initialise feeds..
def aggregator = new Aggregator(repository, 'Feeds')
aggregator.start()

def contactsManager = new ContactsManager(repository, 'Contacts')

def newContact = { events ->
	for (event in events) {
		println "Node added: ${event.path}"
		def message = session.getNode(event.path)
		if (message.hasNode('headers')) {
			def headers = message.getNode('headers')
			def to = new InternetAddress(headers.getProperty('To').string)
			def contactNode = contactsManager.add(to)
			println "Added contact: ${contactNode.path}"
		}
	}
} as EventListener

session.workspace.observationManager.addEventListener(newContact, 1, '/Mail/folders/Sent', true, null, null, false)

def planner = new Planner(repository, 'Planner')

//def searchManager = new SearchManager(session)

def actionContext = [:] as ObservableMap

def ousia = new OusiaBuilder()

ousia.edt {
//	lookAndFeel('substance-nebula')
	lookAndFeel('system')

	actions {
        action id: 'exitAction', name: rs('Exit'), accelerator: shortcut('Q'), closure: {
            System.exit(0)
        }
		
		action id: 'addFeedAction', name: rs('Feed'), closure: {
			url = JOptionPane.showInputDialog(frame, rs('URL'))
			if (url) {
				doOutside {
					try {
						aggregator.addFeed(url)
					}
					catch (MalformedURLException e) {
						doLater {
							JOptionPane.showMessageDialog(frame, "Invalid URL: ${url}")
						}
					}
					catch (Exception e2) {
						doLater {
							def error = new ErrorInfo('Error', "${e2.message}",
								"<html><body>Error adding feed: ${e2}</body></html>", null, null, null, null)
							JXErrorPane.showDialog(frame, error);
						}
					}
				}
			}
		}
		
		action id: 'importFeedsAction', name: rs('Feeds'), closure: {
			if (chooser.showOpenDialog() == JFileChooser.APPROVE_OPTION) {
				doOutside {
					def opml = new XmlSlurper().parse(chooser.selectedFile)
					def feeds = opml.body.outline.outline.collect { it.@xmlUrl.text() }
					if (feeds.isEmpty()) {
						feeds = opml.body.outline.collect { it.@xmlUrl.text() }
					}
					println "Feeds: ${feeds}"
					def errorMap = [:]
					GParsExecutorsPool.withPool(10) {
						for (feed in feeds) {
							try {
								def future = aggregator.updateFeed.callAsync(feed)
//	                            future.get()
//	                            doLater {
//	                                feedList.model.fireTableDataChanged()
//	                            }
							}
							catch (Exception ex) {
								log.log unexpected_error, ex
								errorMap.put(feed, ex)
							}
						}
					}
					if (!errorMap.isEmpty()) {
						doLater {
							def error = new ErrorInfo('Import Error', 'An error occurred importing feeds - see log for details',
								"<html><body>Error importing feeds: ${errorMap}</body></html>", null, null, null, null)
							JXErrorPane.showDialog(frame, error);
						}
					}
				}
			}
		}
		
		action id: 'exportFeedsAction', name: rs('Feeds'), closure: {
			if (chooser.showSaveDialog() == JFileChooser.APPROVE_OPTION) {
				doOutside {
			        def writer = new FileWriter(chooser.selectedFile)
			        def opmlBuilder = new MarkupBuilder(writer)
			        opmlBuilder.opml(version: '1.0') {
			            body {
			                for (feedNode in session.rootNode.getNode('Feeds').nodes) {
								if (!feedNode.hasNode('query')) {
				                    outline(title: "${feedNode.getProperty('title').string}",
				                        xmlUrl: "${feedNode.getProperty('url').string}")
								}
			                }
			            }
			        }
				}
			}
		}

		// import email..
		action id: 'importMailAction', name: rs('Email'), closure: {
			if (dirChooser.showOpenDialog() == JFileChooser.APPROVE_OPTION) {
				doOutside {
					// load email..
					Session importSession = Session.getInstance(new Properties())
					Store importStore = importSession.getStore(new URLName("mstor:${dirChooser.selectedFile.absolutePath}"))
					importStore.connect()
			
					mailbox.importMail importStore
				}
			}
		}

		action id: 'openExplorerView', name: rs('Repository Explorer'), closure: {
			frame(title: rs('Repository Explorer'), size: [320, 240], show: true, defaultCloseOperation: JFrame.DISPOSE_ON_CLOSE) {
				widget(new ExplorerView(session.rootNode, frame, actionContext))
			}
		}
		
		def screenEnv = GraphicsEnvironment.localGraphicsEnvironment.defaultScreenDevice
		action id: 'fullScreenAction', name: rs('Fullscreen'), accelerator: 'F11', enabled: screenEnv.fullScreenSupported, closure: {
			// toggle..
			if (screenEnv.fullScreenWindow) {
				frame.dispose()
				frame.undecorated = false
				frame.resizable = true
				screenEnv.fullScreenWindow = null
				frame.visible = true
			}
			else {
				frame.dispose()
				frame.undecorated = true
				frame.resizable = false
				screenEnv.fullScreenWindow = frame
			}
		}
		
		action id: 'onlineHelpAction', name: rs('Online Help'), accelerator: 'F1', closure: {
			Desktop.desktop.browse(URI.create('http://basetools.org/coucou'))
		}
		
		action id: 'aboutAction', name: rs('About'), closure: {
			dialog(title: rs('About Coucou'), size: [350, 250], show: true, owner: frame, modal: true, locationRelativeTo: frame) {
				borderLayout()
				label(text: "${rs('Coucou')} 1.0", constraints: BorderLayout.NORTH, border: emptyBorder(10))
				panel(constraints: BorderLayout.CENTER, border: emptyBorder(10)) {
					borderLayout()
					scrollPane(horizontalScrollBarPolicy: JScrollPane.HORIZONTAL_SCROLLBAR_NEVER, border: null) {
						table(gridColor: Color.LIGHT_GRAY) {
							def systemProps = []
							for (propName in System.properties.keySet()) {
								systemProps.add([property: propName, value: System.properties.getProperty(propName)])
							}
							tableModel(list: systemProps) {
								propertyColumn(header: rs('Property'), propertyName: 'property', editable: false)
								propertyColumn(header: rs('Value'), propertyName: 'value', editable: false)
							}
						}
					}
				}
			}
		}
		
		action id: 'quickSearchAction', name: rs('Search Items'), closure: {
			def searchQuery = new QueryBuilder(session.workspace.queryManager, session.valueFactory).with {
				query(
					source: selector(nodeType: 'nt:unstructured', name: 'items'),
					constraint: and(
						constraint1: descendantNode(selectorName: 'items', path: breadcrumb.model.items[-1].data.element.path),
						constraint2: fullTextSearch(selectorName: 'items', propertyName: 'description', searchTerms: quickSearchField.text)
					)
				)
			}
			def pr = new SearchPathResult(searchQuery, quickSearchField.text)
			def searchResult = new BreadcrumbItem<PathResult<?, javax.jcr.Node>>(pr.name, pr)
			searchResult.icon = searchIcon
			breadcrumb.model.addLast(searchResult)
		}
		
		action id: 'markAsReadAction', name: rs('Mark As Read'), closure: {
			actionContext.markAsRead()
		}
		
		action id: 'markAllReadAction', name: rs('Mark All Read'), closure: {
			actionContext.markAllRead()
		}

		action id: 'deleteAction', name: rs('Delete'), closure: {
			actionContext.delete()
		}
	}

	fileChooser(id: 'chooser')
	fileChooser(id: 'imageChooser', fileFilter: new ImageFileFilter())
	fileChooser(id: 'dirChooser', fileSelectionMode: JFileChooser.FILES_AND_DIRECTORIES)

	// icons..
	resizableIcon('/logo.svg', size: [20, 20], id: 'logoIcon')
	resizableIcon('/add.svg', size: [16, 16], id: 'newIcon')
	resizableIcon('/feed.svg', size: [16, 16], id: 'feedIcon')
	resizableIcon('/mail.svg', size: [16, 16], id: 'mailIcon')
	resizableIcon('/task.svg', size: [16, 16], id: 'taskIcon')
	resizableIcon('/exit.svg', size: [16, 16], id: 'exitIcon')
	resizableIcon('/help.svg', size: [16, 16], id: 'helpIcon')
	resizableIcon('/ok.svg', size: [16, 16], id: 'okIcon')
	resizableIcon('/ok_all.svg', size: [16, 16], id: 'okAllIcon')
	resizableIcon('/cancel.svg', size: [16, 16], id: 'cancelIcon')
	resizableIcon('/search.svg', size: [12, 12], id: 'searchIcon')
	resizableIcon('/im.svg', size: [16, 16], id: 'chatIcon')
	resizableIcon('/event.svg', size: [16, 16], id: 'eventIcon')
	resizableIcon('/reply.svg', size: [16, 16], id: 'replyIcon')
	resizableIcon('/replyAll.svg', size: [16, 16], id: 'replyAllIcon')
	resizableIcon('/forward.svg', size: [16, 16], id: 'forwardIcon')
	resizableIcon('/copy.svg', size: [16, 16], id: 'copyIcon')
	resizableIcon('/document.svg', size: [16, 16], id: 'documentIcon')
	resizableIcon('/import.svg', size: [16, 16], id: 'importIcon')
	resizableIcon('/export.svg', size: [16, 16], id: 'exportIcon')
	
	ribbonFrame(title: rs('Coucou'), size: [640, 480], show: true, locationRelativeTo: null,
		defaultCloseOperation: JFrame.EXIT_ON_CLOSE, id: 'frame', iconImage: imageIcon('/globe.png').image,
		applicationIcon: logoIcon) {

		ribbonApplicationMenu(id: 'appMenu') {
			ribbonApplicationMenuEntryPrimary(id: 'newMenu', icon: newIcon, text: rs('New'), kind: CommandButtonKind.POPUP_ONLY)
//				['groupTitle': 'New Items', 'entries': [
			ribbonApplicationMenuEntrySecondary(id: 'newFeed', icon: feedIcon, text: rs('Feed'), kind: CommandButtonKind.ACTION_ONLY, actionPerformed: addFeedAction) //]]
//			}
			newMenu.addSecondaryMenuGroup 'Create a new item', newFeed
			
			appMenu.addMenuSeparator()
			ribbonApplicationMenuEntryPrimary(id: 'saveAsMenu', icon: forwardIcon, text: rs('Save As'), kind: CommandButtonKind.ACTION_AND_POPUP_MAIN_ACTION)
			ribbonApplicationMenuEntrySecondary(id: 'saveAsFile', icon: documentIcon, text: rs('File'), kind: CommandButtonKind.ACTION_ONLY)
			ribbonApplicationMenuEntrySecondary(id: 'saveAsTemplate', icon: documentIcon, text: rs('Template'), kind: CommandButtonKind.ACTION_ONLY)
			saveAsMenu.addSecondaryMenuGroup 'Save Item', saveAsFile, saveAsTemplate

			appMenu.addMenuSeparator()
			ribbonApplicationMenuEntryPrimary(id: 'importMenu', icon: importIcon, text: rs('Import'), kind: CommandButtonKind.POPUP_ONLY)
			ribbonApplicationMenuEntrySecondary(id: 'importMail', icon: mailIcon, text: rs('Email'), kind: CommandButtonKind.ACTION_ONLY, actionPerformed: importMailAction)
			ribbonApplicationMenuEntrySecondary(id: 'importFeeds', icon: feedIcon, text: rs('Feeds'), kind: CommandButtonKind.ACTION_ONLY, actionPerformed: importFeedsAction)
			importMenu.addSecondaryMenuGroup 'Import external data', importMail, importFeeds

			ribbonApplicationMenuEntryPrimary(id: 'exportMenu', icon: exportIcon, text: rs('Export'), kind: CommandButtonKind.POPUP_ONLY)
			ribbonApplicationMenuEntrySecondary(id: 'exportFeeds', icon: feedIcon, text: rs('Feeds'), kind: CommandButtonKind.ACTION_ONLY, actionPerformed: exportFeedsAction)
			exportMenu.addSecondaryMenuGroup 'Export data', exportFeeds
			appMenu.addMenuSeparator()
			
			ribbonApplicationMenuEntryPrimary(icon: exitIcon, text: rs('Exit'), kind: CommandButtonKind.ACTION_ONLY, actionPerformed: exitAction)
			ribbonApplicationMenuEntryFooter(text: rs('Preferences'))
		}
		frame.ribbon.applicationMenu = appMenu
		frame.ribbon.configureHelp helpIcon, aboutAction
 
		avatarBand = new JRibbonBand(rs('Avatar'), null, null)
//		avatarBand.resizePolicies = CoreRibbonResizePolicies.getCorePoliciesNone(avatarBand)
		avatarBand.resizePolicies = [new CoreRibbonResizePolicies.Mirror(avatarBand.controlPanel)]
		avatarBand.addCommandButton(commandButton(forwardIcon), RibbonElementPriority.MEDIUM)
		
		filterBand = new JRibbonBand(rs('Filter'), forwardIcon, null)
		filterBand.resizePolicies = [new CoreRibbonResizePolicies.Mirror(filterBand.controlPanel)]
		filterBand.addRibbonComponent ribbonComponent(textField(columns: 14, prompt: rs('Type To Filter..'), promptFontStyle: Font.ITALIC, promptForeground: Color.LIGHT_GRAY, id: 'filterTextField', keyPressed: {e-> if (e.keyCode == KeyEvent.VK_ESCAPE) e.source.text = null}))
		filterBand.addRibbonComponent ribbonComponent(checkBox(text: rs('Unread Items'), id: 'unreadFilterCheckbox'))
		filterBand.addRibbonComponent ribbonComponent(checkBox(text: rs('Important Items')))
		
		groupByBand = new JRibbonBand(rs('Group By'), forwardIcon, null)
		groupByBand.resizePolicies = [new CoreRibbonResizePolicies.Mirror(groupByBand.controlPanel)]
		groupByBand.addRibbonComponent ribbonComponent(comboBox(items: [rs('Date'), rs('Source')] as Object[], editable: false))
		
		sortBand = new JRibbonBand(rs('Sort By'), forwardIcon, null)
		sortBand.resizePolicies = [new CoreRibbonResizePolicies.Mirror(sortBand.controlPanel)]
		sortBand.addRibbonComponent ribbonComponent(comboBox(items: [rs('Date'), rs('Title'), rs('Source')] as Object[], editable: false))
		sortBand.addRibbonComponent ribbonComponent(commandButton(rs('Sort Order'), commandButtonKind: CommandButtonKind.POPUP_ONLY, popupOrientationKind: CommandButtonPopupOrientationKind.SIDEWARD, popupCallback: { 
				commandPopupMenu() {
					commandToggleMenuButton(rs('Ascending'))
					commandToggleMenuButton(rs('Descending'))
				}
			} as PopupPanelCallback))

		showHideBand = new JRibbonBand(rs('Show/Hide'), forwardIcon, null)
		showHideBand.resizePolicies = [new CoreRibbonResizePolicies.Mirror(showHideBand.controlPanel)]
		showHideBand.addCommandButton(commandToggleButton(rs('Status Bar'), selected: true, actionPerformed: {e-> statusBar.visible = e.source.actionModel.selected} as ActionListener), RibbonElementPriority.MEDIUM)
		
		viewModeBand = new JRibbonBand(rs('Mode'), forwardIcon, null)
		viewModeBand.resizePolicies = [new CoreRibbonResizePolicies.Mirror(viewModeBand.controlPanel)]
		viewModeBand.addCommandButton(commandToggleButton(rs('Fullscreen'), actionPerformed: fullScreenAction), RibbonElementPriority.MEDIUM)
		
		quickSearchBand = new JRibbonBand(rs('Quick Search'), forwardIcon, null)
		quickSearchBand.resizePolicies = [new CoreRibbonResizePolicies.Mirror(quickSearchBand.controlPanel)]
		quickSearchBand.addRibbonComponent ribbonComponent(textField(id: 'quickSearchField', columns: 14, enabled: false, prompt: quickSearchAction.getValue('Name'), promptFontStyle: Font.ITALIC, promptForeground: Color.LIGHT_GRAY,
			 keyPressed: {e-> if (e.keyCode == KeyEvent.VK_ESCAPE) e.source.text = null}))
		quickSearchField.addActionListener quickSearchAction
		quickSearchField.addBuddy commandButton(searchIcon, flat: true, actionPerformed: quickSearchAction, id: 'quickSearchButton'), BuddySupport.Position.RIGHT
		quickSearchBand.addRibbonComponent ribbonComponent(checkBox(text: rs('Unread Items')))
		quickSearchBand.addRibbonComponent ribbonComponent(checkBox(text: rs('Important Items')))
		
		advancedSearchBand = new JRibbonBand(rs('Advanced'), forwardIcon, null)
		advancedSearchBand.resizePolicies = [new CoreRibbonResizePolicies.Mirror(advancedSearchBand.controlPanel)]
		advancedSearchBand.addCommandButton(commandButton(rs('New Search')), RibbonElementPriority.MEDIUM)
		advancedSearchBand.addCommandButton(commandButton(rs('Saved Searches')), RibbonElementPriority.MEDIUM)
		
		respondBand = new JRibbonBand(rs('Respond'), forwardIcon, null)
		respondBand.resizePolicies = [new CoreRibbonResizePolicies.Mirror(respondBand.controlPanel)]
		respondBand.addCommandButton(commandButton(replyIcon, text: rs('Reply')), RibbonElementPriority.MEDIUM)
		respondBand.addCommandButton(commandButton(replyAllIcon, text: rs('Reply To All')), RibbonElementPriority.MEDIUM)
		respondBand.addCommandButton(commandButton(forwardIcon, text: rs('Forward')), RibbonElementPriority.MEDIUM)
		respondBand.addCommandButton(commandButton(chatIcon, text: rs('Chat')), RibbonElementPriority.MEDIUM)
		
		updateBand = new JRibbonBand(rs('Update'), forwardIcon, null)
		updateBand.resizePolicies = [new CoreRibbonResizePolicies.Mirror(updateBand.controlPanel)]
		updateBand.addCommandButton(commandButton(okIcon, action: markAsReadAction), RibbonElementPriority.MEDIUM)
		updateBand.addCommandButton(commandButton(okAllIcon, action: markAllReadAction), RibbonElementPriority.MEDIUM)
		updateBand.addCommandButton(commandButton(cancelIcon, action: deleteAction), RibbonElementPriority.MEDIUM)
		
		organiseBand = new JRibbonBand(rs('Organise'), forwardIcon, null)
		organiseBand.resizePolicies = [new CoreRibbonResizePolicies.Mirror(organiseBand.controlPanel)]
		organiseBand.addCommandButton(commandButton(rs('Flag')), RibbonElementPriority.MEDIUM)
		organiseBand.addCommandButton(commandButton(rs('Tag')), RibbonElementPriority.MEDIUM)
		organiseBand.addCommandButton(commandButton(rs('Move To')), RibbonElementPriority.MEDIUM)
		organiseBand.addCommandButton(commandButton(rs('Archive')), RibbonElementPriority.MEDIUM)

//		flagTagBand = new JRibbonBand(rs('Flag/Tag'), forwardIcon, null)
//		flagTagBand.resizePolicies = [new CoreRibbonResizePolicies.Mirror(flagTagBand.controlPanel)]
		
		actionExtrasBand = new JRibbonBand(rs('Extras'), forwardIcon, null)
		actionExtrasBand.resizePolicies = [new CoreRibbonResizePolicies.Mirror(actionExtrasBand.controlPanel)]
		actionExtrasBand.addCommandButton(commandButton(copyIcon, text: rs('Copy')), RibbonElementPriority.MEDIUM)
		actionExtrasBand.addCommandButton(commandButton(eventIcon, text: rs('Add To Planner')), RibbonElementPriority.MEDIUM)

		toolsBand = new JRibbonBand(rs('Tools'), taskIcon, null)
		toolsBand.resizePolicies = [new CoreRibbonResizePolicies.Mirror(toolsBand.controlPanel)]
		toolsBand.addCommandButton(commandButton(taskIcon, actionPerformed: openExplorerView), RibbonElementPriority.MEDIUM)
		
		frame.ribbon.addTask(new RibbonTask(rs('View'), filterBand, groupByBand, sortBand, showHideBand, viewModeBand))
		frame.ribbon.addTask(new RibbonTask(rs('Search'), quickSearchBand, advancedSearchBand))
		frame.ribbon.addTask(new RibbonTask(rs('Action'), respondBand, updateBand, organiseBand, actionExtrasBand))
		frame.ribbon.addTask(new RibbonTask(rs('Presence'), avatarBand))
		frame.ribbon.addTask(new RibbonTask(rs('Tools'), toolsBand))
		
		panel {
			borderLayout()
//			breadcrumbFileSelector(path: new File(System.getProperty('user.home')), constraints: BorderLayout.NORTH)
			
//			breadcrumbBar(new NodeCallback(session.rootNode), constraints: BorderLayout.NORTH, id: 'breadcrumb')
			breadcrumbBar(new PathResultCallback(root: new RootNodePathResult(session.rootNode), searchPathIcon: searchIcon), throwsExceptions: false, constraints: BorderLayout.NORTH, id: 'breadcrumb')

			// Treetable renderering..
			def ttsupport

			splitPane(orientation: JSplitPane.VERTICAL_SPLIT, dividerLocation: 200, continuousLayout: true, oneTouchExpandable: true, dividerSize: 10) {
				
				scrollPane(constraints: 'left', horizontalScrollBarPolicy: JScrollPane.HORIZONTAL_SCROLLBAR_NEVER, border: null, viewportBorder: null, id: 'sp') {
	//				sp.viewport.background = Color.RED
					table(gridColor: Color.LIGHT_GRAY, showHorizontalLines: false, opaque: false, border: null, id: 'activityTable') {
						activities = new BasicEventList<?>()
						
						def filters = new BasicEventList<MatcherEditor<?>>()
						filters << new TextComponentMatcherEditor(filterTextField, { baseList, e ->
							baseList << e['title']
						} as TextFilterator, true)
						filters << new JCheckboxMatcherEditor(unreadFilterCheckbox, { baseList, e ->
							if (e['node'].hasProperty('seen')) {
								baseList << !e['node'].getProperty('seen').boolean
							}
						} as Filterator)

						filterList(activities, id: 'filteredActivities', matcherEditor: new CompositeMatcherEditor<?>(filters))
						
						filteredActivities.addListEventListener({
							doLater {
								activityTable.clearSelection()
								if (filteredActivities.size() > 0) {
									statusMessage.text = "${filteredActivities.size()} ${rs('items')}"
								}
								else {
									statusMessage.text = rs('Nothing to see here')
								}
							}
						} as ListEventListener)

						treeList(sortedList(filteredActivities, comparator: {a, b -> b.date <=> a.date} as Comparator, id: 'sortedActivities'),
							 expansionModel: new DateExpansionModel(), format: [
						        allowsChildren: {element -> true},
						        getComparator: {depth -> },
						        getPath: {path, element ->
									today = Calendar.instance
									today.clearTime()
									if (element.date < today.time) {
										path << 'Older Items'
									}
									else {
										path << 'Today'
									}
									path << element
								 }
						    ] as Format<?>, id: 'activityTree')
						
						activityTable.model = new EventTableModel<?>(activityTree,
							[
								getColumnCount: {3},
								getColumnName: {column -> },
								getColumnValue: {object, column -> switch(column) {
									case 0: if (object instanceof String) {
										return object
									} else {
										return object['source']
									}
									case 1: if (!(object instanceof String)) {
										return object['title']
									}
									case 2: if (!(object instanceof String)) {
	//									return new PrettyTime().format(object['date'])
										return object['date']
									}
								}}
							] as TableFormat)
						
						activityTable.tableHeader = null
	
						ttsupport = TreeTableSupport.install(activityTable, activityTree, 0)
						ttsupport.arrowKeyExpansionEnabled = true
	//					ttsupport.delegateRenderer = new DefaultTableCellRenderer(opaque: false)
	//					ttsupport.delegateRenderer = new DefaultNodeTableCellRenderer(session.rootNode)
						ttsupport.delegateRenderer.background = Color.WHITE
						
						activityTable.columnModel.getColumn(0).maxWidth = 200
						activityTable.columnModel.getColumn(0).preferredWidth = 200
						activityTable.columnModel.getColumn(2).maxWidth = 150
						activityTable.columnModel.getColumn(2).preferredWidth = 150
						/*
						activities << ['title': 'Welcome to Coucou', 'source': 'Coucou', 'date': new Date()]
						
						aggregator = new Aggregator()
						aggregator.urls << 'http://ausdroid.net/feed/'
						aggregator.urls << 'http://www.abc.net.au/news/indexes/justin/rss.xml'
						
						aggregator.update(activities)
						*/
						activityTable.selectionModel.valueChanged = { e ->
							if (!e.valueIsAdjusting) {
								if (activityTable.selectedRow >= 0) {
									int entryIndex = activityTable.convertRowIndexToModel(activityTable.selectedRow)
									def entry = activityTree[entryIndex]
									if (entry && entry instanceof Map) {
										// update action context..
										if (entry['node'].hasProperty('link')) {
											actionContext.markAsRead = {
												aggregator.markNodeRead entry['node']
			                                    if (entryIndex < activityTree.size() - 1) {
													int nextIndex = activityTable.convertRowIndexToView(entryIndex + 1)
			                                        activityTable.setRowSelectionInterval(nextIndex, nextIndex)
			                                    }
											}
											actionContext.delete = {
												aggregator.delete entry['node']
												activityTree.remove entryIndex
											}
										}
										else if (entry['node'].parent.path == '/Feeds') {
											actionContext.markAsRead = null
											actionContext.delete = {
												aggregator.delete entry['node']
												activityTree.remove entryIndex
											}
										}
										else {
											actionContext.markAsRead = null
											actionContext.delete = null
										}
										
										edt {
											if (entry['node'].hasProperty('description')) {
			//                                        println "Entry selected: ${entryList.model[entryList.selectedRow]}"
												def content = entry['node'].getProperty('description').string.replaceAll(/(?<=img src\=\")http:\/\/.+:.*(?=")/, '') //.replaceAll(/(http:\/\/)?([a-zA-Z0-9\-.]+\.[a-zA-Z0-9\-]{2,}([\/]([a-zA-Z0-9_\/\-.?&%=+])*)*)(\s+|$)/, '<a href="http://$2">$2</a> ')
												contentView.text = content
												contentView.caretPosition = 0
											}
											else if (entry['node'].hasNode('body')) {
												def content = entry['node'].getNode('body').getNode('part').getNode('jcr:content')
												if (content.getProperty('jcr:mimeType').string.startsWith('text/plain')) {
													contentView.text = "<html><pre>${content.getProperty('jcr:data').string}"
												}
												else {
													contentView.text = content.getProperty('jcr:data').string
												}
												contentView.caretPosition = 0
											}
											else if (entry['node'].hasNode('content')) {
												def content = entry['node'].getNode('content').getNode('data').getNode('jcr:content').getProperty('jcr:data').string
												contentView.text = content
												contentView.caretPosition = 0
											}
											else {
												contentView.text = null
											}
										}
									}
									else {
										contentView.text = null
										actionContext.markAsRead = null
									}
								}
								else {
									edt {
										contentView.text = null
									}
									actionContext.markAsRead = null
								}
							}
						}
		
		                activityTable.mouseClicked = { e ->
		                    if (e.button == MouseEvent.BUTTON1 && e.clickCount >= 2 && activityTable.selectedRow >= 0) {
		                        def selectedItem = activityTree[activityTable.convertRowIndexToModel(activityTable.selectedRow)]
								// feed item..
		                        if (selectedItem.node.hasProperty('link')) {
		                            Desktop.desktop.browse(URI.create(selectedItem.node.getProperty('link').value.string))
									aggregator.markNodeRead selectedItem.node
		                        }
								/*
								// feed subscription..
								else if (selectedItem.node.hasProperty('title')) {
									breadcrumb.model.addLast(new BreadcrumbItem<Node>(selectedItem.node.getProperty('title').string, selectedItem.node))
								}
								// mail folder..
								else if (selectedItem.node.hasProperty('folderName')) {
									breadcrumb.model.addLast(new BreadcrumbItem<Node>(selectedItem.node.getProperty('folderName').string, selectedItem.node))
								}
								// mail message..
								else if (selectedItem.node.parent.name == 'messages') {
									breadcrumb.model.addLast(new BreadcrumbItem<Node>(selectedItem.node.getNode('headers').getProperty('Subject').string, selectedItem.node))
								}
								*/
								// mail attachment..
								else if (selectedItem.node.hasNode('jcr:content')) {
									def file = new File(System.getProperty('java.io.tmpdir'), selectedItem.node.name)
									file.bytes = selectedItem.node.getNode('jcr:content').getProperty('jcr:data').binary.stream.bytes
									Desktop.desktop.open(file)
								}
								else {
									doLater {
										final PathResult<?, javax.jcr.Node> pr = breadcrumb.model.items[-1].data.getChild(selectedItem.node)
										breadcrumb.model.addLast(new BreadcrumbItem<PathResult<?, javax.jcr.Node>>(pr.name, pr))
									}
								}
		                    }
		                }
						
//						activityTable.focusLost = { e ->
//							if (e.oppositeComponent != contentView) {
//								activityTable.clearSelection()
//							}
//						}
					}
				}
				
				scrollPane(constraints: 'right') {
					def styleSheet = new StyleSheet()
					styleSheet.addRule("body {background-color:#ffffff; color:#444b56; font-family:verdana,sans-serif; margin:8px; }")
			//        styleSheet.addRule("a {text-decoration:underline; color:blue; }")
			//                            styleSheet.addRule("a:hover {text-decoration:underline; }")
			//        styleSheet.addRule("img {border-width:0; }")
					
					def defaultEditorKit = new HTMLEditorKitExt(styleSheet: styleSheet)
			
					editorPane(id: 'contentView', editorKit: defaultEditorKit, editable: false, contentType: 'text/html', opaque: true, border: null)
					contentView.addHyperlinkListener(new HyperlinkBrowser())
				}
			}

			breadcrumb.model.addPathListener({
					edt {
						filterTextField.text = null
						frame.title = "${breadcrumb.model.items[-1].data.name} - ${rs('Coucou')}"
						frame.contentPane.cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
						
						// enable/disable ribbon tasks..
						quickSearchField.text = null
						quickSearchField.enabled = !breadcrumb.model.items[-1].data.leaf
						quickSearchButton.enabled = !breadcrumb.model.items[-1].data.leaf
					}
				
					doOutside {
//						def items = breadcrumb.callback.getLeafs(breadcrumb.model.items)
						def items = breadcrumb.model.items[-1].data.results

						doLater {
							// install new renderer..
//							def defaultRenderer = new DefaultNodeTableCellRenderer(breadcrumb.model.items[-1].data)
							def defaultRenderer = new DefaultNodeTableCellRenderer(activityTree)
							defaultRenderer.background = Color.WHITE
							
//							def dateRenderer = new DateCellRenderer(breadcrumb.model.items[-1].data)
							def dateRenderer = new DateCellRenderer(activityTree)
							dateRenderer.background = Color.WHITE
							
							ttsupport.delegateRenderer = defaultRenderer
							activityTable.columnModel.getColumn(1).cellRenderer = defaultRenderer
							activityTable.columnModel.getColumn(2).cellRenderer = dateRenderer
							
							 try {
								 // lock for list modification..
								 activities.readWriteLock.writeLock().lock()
								 
								 activities.clear()
							 }
							 finally {
								 // unlock post-list modification..
								 activities.readWriteLock.writeLock().unlock()
							 }
						}

						 items.each {
							 def item = [:]
							 // feeds / items..
							 if (it.hasProperty('title')) {
								 item['title'] = it.getProperty('title').string
							 }
							 // mail messages..
							 else if (it.hasNode('headers')) {
								 def headers = it.getNode('headers')
								 if (headers.hasProperty('Subject')) {
									 item['title'] = headers.getProperty('Subject').string
								 }
								 else {
									 item['title'] = '<No Subject>'
								 }
							 }
							 // contacts..
							 else if (it.hasProperty('personal')) {
								 item['title'] = it.getProperty('personal').string
							 }
							 else {
								 item['title'] = it.name
							 }
							 
							 if (it.hasProperty('source')) {
								 if (it.getProperty('source').type == PropertyType.REFERENCE) {
									 item['source'] = it.getProperty('source').getNode().getProperty('title').string
								 }
								 else {
									 item['source'] = it.getProperty('source').string
								 }
							 }
							 // mail messages..
							 else if (it.hasNode('headers')) {
								 def headers = it.getNode('headers')
								 if (it.parent.parent.name == 'Sent' && headers.hasProperty('To')) {
									 item['source'] = headers.getProperty('To').string
								 }
								 else if (headers.hasProperty('From')) {
									 item['source'] = headers.getProperty('From').string
								 }
								 else {
									 item['source'] = '<Unknown Sender>'
								 }
							 }
							 // attachments..
							 else if (it.parent.name == 'attachments') {
								 def message = it.parent.parent
								 def headers = message.getNode('headers')
								 if (message.parent.parent.name == 'Sent' && headers.hasProperty('To')) {
									 item['source'] = headers.getProperty('To').string
								 }
								 else if (headers.hasProperty('From')) {
									 item['source'] = headers.getProperty('From').string
								 }
								 else {
									 item['source'] = '<Unknown Sender>'
								 }
							 }
							 // contacts..
							 else if (it.hasProperty('email')) {
								 item['source'] = it.getProperty('email').string
							 }
							 else {
								 item['source'] = it.parent.name
							 }

							 if (it.hasProperty('date')) {
								 item['date'] = it.getProperty('date').date.time
							 }
							 // mail messages..
							 else if (it.hasNode('headers')) {
								 def headers = it.getNode('headers')
								 if (headers.hasProperty('Date')) {
									 try {
										 item['date'] = mailDateFormat.parse(headers.getProperty('Date').string)
									 }
									 catch (Exception e) {
									 }
								 }
							 }
							 else if (it.hasProperty('received')) {
								 item['date'] = it.getProperty('received').date.time
							 }
							 // attachments..
							 else if (it.parent.name == 'attachments') {
								 def headers = it.parent.parent.getNode('headers')
								 if (headers.hasProperty('Date')) {
									 try {
										 item['date'] = mailDateFormat.parse(headers.getProperty('Date').string)
									 }
									 catch (Exception e) {
									 }
								 }
							 }

							 item['node'] = it

							 doLater {							 
								 try {
									 // lock for list modification..
									 activities.readWriteLock.writeLock().lock()
									 activities.add(item)
//									 statusMessage.text = "${activities.size()} ${rs('items')}"
								 }
								 finally {
									 // unlock post-list modification..
									 activities.readWriteLock.writeLock().unlock()
								 }
							 }
						}
						
						 doLater {
//							 if (activities.size() == 0) {
//								 statusMessage.text = rs('Nothing to see here')
//							 }
							 frame.contentPane.cursor = Cursor.defaultCursor
						 }
						 
						// listen for data changes..
						def tableUpdater = { events ->
							println '*** Refresh table'
//							activityTable.model.fireTableDataChanged()
						} as EventListener
					
						if (breadcrumb.model.items[-1].data.element instanceof javax.jcr.Node) {
							def path = breadcrumb.model.items[-1].data.element.path
	
							session.workspace.observationManager.removeEventListener(tableUpdater)
							session.workspace.observationManager.addEventListener(tableUpdater, 1|2|4|8|16|32, path, true, null, null, false)
							println "Listening for changes: ${path}"
						}
					}
					
				} as BreadcrumbPathListener)
		}
		
//		toolWindowManager(id: 'windowManager')
		
//		def navigator = windowManager.registerToolWindow(rs("Navigator"), rs("Repository"), null, new RepositoryNavigator(), ToolWindowAnchor.LEFT)
//		bind(source: viewNavigator, sourceProperty:'selected', target: navigator, targetProperty:'available')
		
//		def aggregator = windowManager.registerToolWindow(rs("Aggregator"), rs("Feeds"), null, new JXPanel(), ToolWindowAnchor.BOTTOM)
//		bind(source: viewAggregator, sourceProperty:'selected', target: aggregator, targetProperty:'available')
		
//		def accounts = windowManager.registerToolWindow(rs("Accounts"), rs("Accounts"), null, new JXPanel(), ToolWindowAnchor.RIGHT)
//		bind(source: viewAccounts, sourceProperty:'selected', target: accounts, targetProperty:'available')

		statusBar(constraints: BorderLayout.SOUTH, id: 'statusBar') {
			label(id: 'statusMessage', text: rs('Ready'), constraints: new JXStatusBar.Constraint(FILL))
//			bind(source: viewStatusBar, sourceProperty: 'selected', target: statusBar, targetProperty:'visible')
		}
	}
/*
	def content = windowManager.contentManager.addContent('tabs', 'Tabs', null, tabbedPane(tabLayoutPolicy: JTabbedPane.SCROLL_TAB_LAYOUT, id: 'tabs') {
		panel(name: rs('Home'), id: 'homeTab') {
			borderLayout()
			
			tabbedPane(tabPlacement: JTabbedPane.BOTTOM, id: 'navTabs') {
				panel(name: rs('Inbox')) {
					
				}
				panel(name: rs('Contacts')) {
					
				}
				panel(name: rs('Planner')) {
					
				}
				panel(name: rs('Journal')) {
					
				}
				panel(name: rs('Notes')) {
					
				}
				panel(name: rs('Files')) {
					
				}
			}
		}
	}, null)
	content.selected = true
	tabs.setIconAt(tabs.indexOfComponent(homeTab), paddedIcon(imageIcon('/logo-12.png'), size: [width: 14, height: 20]))
*/	
}
