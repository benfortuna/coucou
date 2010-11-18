package org.mnode.coucou

import groovyx.gpars.Asynchronizer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import static org.jdesktop.swingx.JXStatusBar.Constraint.ResizeBehavior.*

import javax.jcr.PropertyType;
import javax.jcr.SimpleCredentials;
import javax.jcr.observation.EventListener;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.MailDateFormat;
import javax.naming.InitialContext;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.html.StyleSheet;
import javax.swing.JSplitPane;


import org.apache.jackrabbit.core.TransientRepository;
import org.apache.jackrabbit.core.config.RepositoryConfig;
import org.apache.jackrabbit.core.jndi.RegistryHelper;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXStatusBar;
import org.jdesktop.swingx.error.ErrorInfo;
import org.mnode.coucou.activity.DateExpansionModel;
import org.mnode.coucou.breadcrumb.NodeCallback;
import org.mnode.coucou.feed.Aggregator;
import org.mnode.coucou.mail.Mailbox;
import org.mnode.juicer.query.QueryBuilder;
import org.mnode.ousia.HTMLEditorKitExt;
import org.mnode.ousia.HyperlinkBrowser;
import org.mnode.ousia.OusiaBuilder;
import org.noos.xing.mydoggy.ToolWindowAnchor;
import org.pushingpixels.flamingo.api.bcb.BreadcrumbPathListener;
import org.pushingpixels.flamingo.api.common.JCommandButton.CommandButtonKind;
import org.pushingpixels.flamingo.api.ribbon.JRibbonBand;
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenuEntryFooter;
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenuEntryPrimary;
import org.pushingpixels.flamingo.api.ribbon.RibbonElementPriority;
import org.pushingpixels.flamingo.api.ribbon.RibbonTask;
import org.pushingpixels.flamingo.api.ribbon.resize.CoreRibbonResizePolicies;
import org.pushingpixels.substance.api.SubstanceConstants;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.slf4j.LoggerFactory;
import javax.jcr.Node;

import org.pushingpixels.flamingo.api.bcb.BreadcrumbItem;


import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.TreeList;
import ca.odell.glazedlists.TreeList.Format;
import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.glazedlists.swing.EventTableModel;
import ca.odell.glazedlists.swing.TreeTableSupport;

import com.ocpsoft.pretty.time.PrettyTime;
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

def session = repository.login(new SimpleCredentials('admin', ''.toCharArray()))
Runtime.getRuntime().addShutdownHook({
//	session.logout()
    RegistryHelper.unregisterRepository(context, 'coucou')
})

// initialise feeds..
def aggregator = new Aggregator(session, 'Feeds')
aggregator.start()

def mailbox = new Mailbox(session, 'Mail')
mailbox.start()

def mailDateFormat = new MailDateFormat()

def editContext = [:] as ObservableMap

def ousia = new OusiaBuilder()


def addFeed = { url ->
	ousia.doOutside {
		 def feedNode = null
		 def feedUrl
		 try {
			 feedUrl = new URL(url)
		 }
		 catch (MalformedURLException e) {
			 try {
				 feedUrl = new URL("http://${url}")
			 }
			 catch (MalformedURLException e1) {
				 doLater {
					 JOptionPane.showMessageDialog(frame, "Invalid URL: ${url}")
				 }
			 }
		 }
		 if (feedUrl) {
			 try {
				 feedNode = aggregator.updateFeed(url)
			 }
			 catch (Exception e) {
				  try {
					  html = new XmlSlurper(new org.ccil.cowan.tagsoup.Parser()).parse(feedUrl.content)
					  def feeds = html.head.link.findAll { it.@type == 'application/rss+xml' || it.@type == 'application/atom+xml' }
					  println "Found ${feeds.size()} feeds: ${feeds.collect { it.@href.text() } }"
					  if (!feeds.isEmpty()) {
						  feedNode = aggregator.updateFeed(new URL(feedUrl, feeds[0].@href.text()).toString())
					  }
					  else {
						  doLater {
							  JOptionPane.showMessageDialog(frame, "No feeds found for site: ${url}")
						  }
					  }
				  }
				  catch (Exception e2) {
					  doLater {
						  JXErrorPane.showDialog(e2);
					  }
				  }
			 }
		 }
	}
}

ousia.edt {
//	lookAndFeel('substance-nebula')
	lookAndFeel('system')

	actions {
        action id: 'exitAction', name: rs('Exit'), accelerator: shortcut('Q'), closure: {
            System.exit(0)
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
					for (feed in feeds) {
						try {
							Asynchronizer.doParallel {
								def future = aggregator.updateFeed.callAsync(feed)
//	                            future.get()
//	                            doLater {
//	                                feedList.model.fireTableDataChanged()
//	                            }
							}
						}
						catch (Exception ex) {
							log.log unexpected_error, ex
							errorMap.put(feed, ex)
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
				widget(new ExplorerView(session.rootNode, frame, editContext))
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
								propertyColumn(header: rs('Property'), propertyName:'property')
								propertyColumn(header: rs('Value'), propertyName:'value')
							}
						}
					}
				}
			}
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
	resizableIcon('/forward.svg', size: [16, 16], id: 'forwardIcon')
	resizableIcon('/task.svg', size: [16, 16], id: 'taskIcon')
	resizableIcon('/exit.svg', size: [16, 16], id: 'exitIcon')
	
	ribbonFrame(title: rs('Coucou'), size: [640, 480], show: true, locationRelativeTo: null,
		defaultCloseOperation: JFrame.EXIT_ON_CLOSE, id: 'frame', iconImage: imageIcon('/globe.png').image,
		applicationIcon: logoIcon) {
/*		
		menuBar {
			menu(text: rs('File'), mnemonic: 'F') {
				menuItem(exitAction)
			}
			menu(text: rs('Edit'), mnemonic: 'E') {
			}
			menu(text: rs('View'), mnemonic: 'V') {
				menu(rs('Sidebar')) {
					checkBoxMenuItem(text: rs("Navigator"), id: 'viewNavigator')
					checkBoxMenuItem(text: rs("Aggregator"), id: 'viewAggregator')
					checkBoxMenuItem(text: rs("Accounts"), id: 'viewAccounts')
				}
				checkBoxMenuItem(text: rs("Status Bar"), id: 'viewStatusBar')
				checkBoxMenuItem(fullScreenAction)
			}
			menu(text: rs("Tools"), mnemonic: 'T') {
			}
			menu(text: rs("Help"), mnemonic: 'H') {
				menuItem(onlineHelpAction)
	//                menuItem(showTipsAction)
				separator()
				menuItem(aboutAction)
			}
		}
*/	
		ribbonApplicationMenu(id: 'appMenu') {
			ribbonApplicationMenuEntryPrimary(id: 'newMenu', icon: newIcon, text: rs('New'), kind: CommandButtonKind.POPUP_ONLY)
//				['groupTitle': 'New Items', 'entries': [
			ribbonApplicationMenuEntrySecondary(id: 'newFeed', icon: feedIcon, text: rs('Feed'), kind: CommandButtonKind.ACTION_ONLY, actionPerformed: {
				url = JOptionPane.showInputDialog(frame, rs('URL'))
				if (url) {
					addFeed(url)
				}
			} as ActionListener) //]]
//			}
			newMenu.addSecondaryMenuGroup 'Create a new item', newFeed
			
			appMenu.addMenuSeparator()
			ribbonApplicationMenuEntryPrimary(id: 'importMenu', icon: forwardIcon, text: rs('Import'), kind: CommandButtonKind.POPUP_ONLY)
			ribbonApplicationMenuEntrySecondary(id: 'importMail', icon: mailIcon, text: rs('Email'), kind: CommandButtonKind.ACTION_ONLY, actionPerformed: importMailAction)
			ribbonApplicationMenuEntrySecondary(id: 'importFeeds', icon: feedIcon, text: rs('Feeds'), kind: CommandButtonKind.ACTION_ONLY, actionPerformed: importFeedsAction)
			importMenu.addSecondaryMenuGroup 'Import external data', importMail, importFeeds

			ribbonApplicationMenuEntryPrimary(id: 'exportMenu', icon: forwardIcon, text: rs('Export'), kind: CommandButtonKind.POPUP_ONLY)
			appMenu.addMenuSeparator()
			
			ribbonApplicationMenuEntryPrimary(icon: exitIcon, text: rs('Exit'), kind: CommandButtonKind.ACTION_ONLY, actionPerformed: exitAction)
			ribbonApplicationMenuEntryFooter(text: rs('Preferences'))
		}
		frame.ribbon.applicationMenu = appMenu
 
		avatarBand = new JRibbonBand(rs('Avatar'), null, null)
//		avatarBand.resizePolicies = CoreRibbonResizePolicies.getCorePoliciesNone(avatarBand)
		avatarBand.resizePolicies = [new CoreRibbonResizePolicies.Mirror(avatarBand.controlPanel)]
		avatarBand.addCommandButton(commandButton(forwardIcon), RibbonElementPriority.MEDIUM)
		
		itemsBand = new JRibbonBand(rs('Items'), forwardIcon, null)
		itemsBand.resizePolicies = [new CoreRibbonResizePolicies.Mirror(itemsBand.controlPanel)]
		
		contactsBand = new JRibbonBand(rs('Contacts'), forwardIcon, null)
		contactsBand.resizePolicies = [new CoreRibbonResizePolicies.Mirror(contactsBand.controlPanel)]
		
		replyBand = new JRibbonBand(rs('Reply'), forwardIcon, null)
		replyBand.resizePolicies = [new CoreRibbonResizePolicies.Mirror(replyBand.controlPanel)]
		
		toolsBand = new JRibbonBand(rs('Tools'), taskIcon, null)
		toolsBand.resizePolicies = [new CoreRibbonResizePolicies.Mirror(toolsBand.controlPanel)]
		toolsBand.addCommandButton(commandButton(taskIcon, actionPerformed: openExplorerView), RibbonElementPriority.MEDIUM)
		
		frame.ribbon.addTask(new RibbonTask(rs('Presence'), avatarBand))
		frame.ribbon.addTask(new RibbonTask(rs('Filter'), itemsBand))
		frame.ribbon.addTask(new RibbonTask(rs('Search'), contactsBand))
		frame.ribbon.addTask(new RibbonTask(rs('Action'), replyBand))
		frame.ribbon.addTask(new RibbonTask(rs('Tools'), toolsBand))
		
		panel {
			borderLayout()
//			breadcrumbFileSelector(path: new File(System.getProperty('user.home')), constraints: BorderLayout.NORTH)
			breadcrumbBar(new NodeCallback(session.rootNode), constraints: BorderLayout.NORTH, id: 'breadcrumb')

			// Treetable renderering..
			def ttsupport

			splitPane(orientation: JSplitPane.VERTICAL_SPLIT, dividerLocation: 200, continuousLayout: true, oneTouchExpandable: true, dividerSize: 10) {
				
				scrollPane(constraints: 'left', horizontalScrollBarPolicy: JScrollPane.HORIZONTAL_SCROLLBAR_NEVER, border: null, viewportBorder: null, id: 'sp') {
	//				sp.viewport.background = Color.RED
					table(gridColor: Color.LIGHT_GRAY, showHorizontalLines: false, opaque: false, border: null, id: 'activityTable') {
						activities = new BasicEventList<?>()
						
						treeList(sortedList(activities, comparator: {a, b -> b.date <=> a.date} as Comparator, id: 'sortedActivities'),
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
									def entry = activityTree[activityTable.convertRowIndexToModel(activityTable.selectedRow)]
									if (entry && entry instanceof Map) {
										edt {
											if (entry['node'].hasProperty('description')) {
			//                                        println "Entry selected: ${entryList.model[entryList.selectedRow]}"
												def content = entry['node'].getProperty('description').string.replaceAll(/http:\/\/.+:.*(?=")/, '') //.replaceAll(/(http:\/\/)?([a-zA-Z0-9\-.]+\.[a-zA-Z0-9\-]{2,}([\/]([a-zA-Z0-9_\/\-.?&%=+])*)*)(\s+|$)/, '<a href="http://$2">$2</a> ')
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
									}
								}
								else {
									edt {
										contentView.text = null
									}
								}
							}
						}
		
		                activityTable.mouseClicked = { e ->
		                    if (e.button == MouseEvent.BUTTON1 && e.clickCount >= 2 && activityTable.selectedRow >= 0) {
		                        def selectedItem = activityTree[activityTable.convertRowIndexToModel(activityTable.selectedRow)]
		                        if (selectedItem.node.hasProperty('link')) {
		                            Desktop.desktop.browse(URI.create(selectedItem.node.getProperty('link').value.string))
		                            selectedItem.node.setProperty('seen', true)
		                            selectedItem.node.save()
		                        }
								else if (selectedItem.node.hasProperty('title')) {
									breadcrumb.model.addLast(new BreadcrumbItem<Node>(selectedItem.node.getProperty('title').string, selectedItem.node))
								}
								else if (selectedItem.node.hasNode('jcr:content')) {
									def file = new File(System.getProperty('java.io.tmpdir'), selectedItem.node.name)
									file.bytes = selectedItem.node.getNode('jcr:content').getProperty('jcr:data').binary.stream.bytes
									Desktop.desktop.open(file)
								}
		                    }
		                }
						
						activityTable.focusLost = { e ->
							if (e.oppositeComponent != contentView) {
								activityTable.clearSelection()
							}
						}
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
						frame.contentPane.cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
					}
				
					doOutside {
						def items = breadcrumb.callback.getLeafs(breadcrumb.model.items)

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
							 if (it.value.hasProperty('title')) {
								 item['title'] = it.value.getProperty('title').string
							 }
							 else if (it.value.hasNode('headers')) {
								 def headers = it.value.getNode('headers')
								 if (headers.hasProperty('Subject')) {
									 item['title'] = headers.getProperty('Subject').string
								 }
								 else {
									 item['title'] = '<No Subject>'
								 }
							 }
							 else {
								 item['title'] = it.value.name
							 }
							 
							 if (it.value.hasProperty('source')) {
								 if (it.value.getProperty('source').type == PropertyType.REFERENCE) {
									 item['source'] = it.value.getProperty('source').getNode().getProperty('title').string
								 }
								 else {
									 item['source'] = it.value.getProperty('source').string
								 }
							 }
							 else if (it.value.hasNode('headers')) {
								 def headers = it.value.getNode('headers')
								 if (headers.hasProperty('From')) {
									 item['source'] = headers.getProperty('From').string
								 }
								 else {
									 item['source'] = '<Unknown Sender>'
								 }
							 }
							 else if (it.value.parent.name == 'attachments') {
								 def headers = it.value.parent.parent.getNode('headers')
								 if (headers.hasProperty('From')) {
									 item['source'] = headers.getProperty('From').string
								 }
								 else {
									 item['source'] = '<Unknown Sender>'
								 }
							 }
							 else {
								 item['source'] = it.value.parent.name
							 }

							 if (it.value.hasProperty('date')) {
								 item['date'] = it.value.getProperty('date').date.time
							 }
							 else if (it.value.hasNode('headers')) {
								 def headers = it.value.getNode('headers')
								 if (headers.hasProperty('Date')) {
									 try {
										 item['date'] = mailDateFormat.parse(headers.getProperty('Date').string)
									 }
									 catch (Exception e) {
									 }
								 }
							 }
							 else if (it.value.hasProperty('received')) {
								 item['date'] = it.value.getProperty('received').date.time
							 }
							 else if (it.value.parent.name == 'attachments') {
								 def headers = it.value.parent.parent.getNode('headers')
								 if (headers.hasProperty('Date')) {
									 try {
										 item['date'] = mailDateFormat.parse(headers.getProperty('Date').string)
									 }
									 catch (Exception e) {
									 }
								 }
							 }

							 item['node'] = it.value

							 doLater {							 
								 try {
									 // lock for list modification..
									 activities.readWriteLock.writeLock().lock()
									 activities.add(item)
									 statusMessage.text = "${activities.size()} items"
								 }
								 finally {
									 // unlock post-list modification..
									 activities.readWriteLock.writeLock().unlock()
								 }
							 }
						}
						
						 doLater {
							 frame.contentPane.cursor = Cursor.defaultCursor
						 }
						 
						// listen for data changes..
						def tableUpdater = { events ->
							println '*** Refresh table'
//							activityTable.model.fireTableDataChanged()
						} as EventListener
					
						def path = breadcrumb.model.items[-1].data.path

						session.workspace.observationManager.removeEventListener(tableUpdater)
						session.workspace.observationManager.addEventListener(tableUpdater, 1|2|4|8|16|32, path, true, null, null, false)
						println "Listening for changes: ${path}"
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
//			bind(source: viewStatusBar, sourceProperty:'selected', target:statusBar, targetProperty:'visible')
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
