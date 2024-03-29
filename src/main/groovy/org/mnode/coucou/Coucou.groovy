/**
 * This file is part of Coucou.
 *
 * Copyright (c) 2011, Ben Fortuna [fortuna@micronode.com]
 *
 * Coucou is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Coucou is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Coucou.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.mnode.coucou

import static org.jdesktop.swingx.JXStatusBar.Constraint.ResizeBehavior.*

import groovy.transform.Field;

import java.awt.BorderLayout
import java.awt.Color
import java.awt.Cursor
import java.awt.Desktop
import java.awt.FlowLayout
import java.awt.Font
import java.awt.GraphicsEnvironment
import java.awt.SystemTray
import java.awt.TrayIcon
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import java.awt.event.MouseEvent
import java.beans.PropertyChangeListener

import javax.jcr.Node
import javax.jcr.PropertyType
import javax.jcr.SimpleCredentials
import javax.jcr.observation.EventListener
import javax.mail.internet.InternetAddress
import javax.mail.internet.MailDateFormat
import javax.naming.InitialContext
import javax.swing.JFileChooser
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.JScrollPane
import javax.swing.JSplitPane
import javax.swing.SwingUtilities
import javax.swing.UIManager
import javax.swing.UIManager.LookAndFeelInfo
import javax.swing.event.DocumentListener
import javax.swing.text.DefaultStyledDocument
import javax.swing.text.StyleConstants
import javax.swing.text.StyleContext
import javax.swing.text.StyledDocument
import javax.swing.text.html.StyleSheet

import org.apache.jackrabbit.core.jndi.RegistryHelper
import org.apache.jackrabbit.util.Text
import org.jdesktop.swingx.JXErrorPane
import org.jdesktop.swingx.JXStatusBar
import org.jdesktop.swingx.error.ErrorInfo
import org.jdesktop.swingx.prompt.BuddySupport
import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smackx.ChatState
import org.jivesoftware.smackx.ChatStateListener
import org.jivesoftware.smackx.ChatStateManager
import org.mnode.base.log.LogAdapter
import org.mnode.base.log.adapter.Slf4jAdapter
import org.mnode.coucou.activity.DateExpansionModel
import org.mnode.coucou.breadcrumb.PathResultCallback
import org.mnode.coucou.chat.PeerListCellRenderer
import org.mnode.coucou.contacts.AbstractXmppPathResult;
import org.mnode.coucou.contacts.ContactsManager
import org.mnode.coucou.contacts.ContactsModule
import org.mnode.coucou.contacts.ContactsNodePathResult;
import org.mnode.coucou.contacts.RosterEntryResultLoader
import org.mnode.coucou.contacts.RosterGroupPathResult;
import org.mnode.coucou.contacts.RosterPathResult
import org.mnode.coucou.feed.Aggregator
import org.mnode.coucou.feed.FeedNodePathResult
import org.mnode.coucou.feed.FeedNodeResultLoader
import org.mnode.coucou.feed.FeedsModule
import org.mnode.coucou.feed.FeedsNodePathResult
import org.mnode.ousia.layer.StatusLayerUI
import org.mnode.coucou.mail.DialogAuthenticator
import org.mnode.coucou.mail.FolderNodePathResult;
import org.mnode.coucou.mail.FolderPathResult
import org.mnode.coucou.mail.FolderResultLoader
import org.mnode.coucou.mail.MailModule
import org.mnode.coucou.mail.Mailbox
import org.mnode.coucou.mail.MailboxNodePathResult;
import org.mnode.coucou.mail.StorePathResult
import org.mnode.coucou.mail.StoreResultLoader
import org.mnode.coucou.planner.CalendarCollectionPathResult;
import org.mnode.coucou.planner.CalendarStorePathResult;
import org.mnode.coucou.planner.Planner
import org.mnode.coucou.planner.PlannerModule;
import org.mnode.coucou.planner.PlannerNodePathResult;
import org.mnode.coucou.search.SearchPathResult
import org.mnode.coucou.search.SearchPathResultLoader;
import org.mnode.coucou.util.HtmlCodes
import org.mnode.juicer.query.QueryBuilder
import org.mnode.ousia.HTMLEditorKitExt
import org.mnode.ousia.HyperlinkBrowser
import org.mnode.ousia.OusiaBuilder
import org.mnode.ousia.HyperlinkBrowser.HyperlinkFeedback
import org.mnode.ousia.layer.ProgressLayerUI;
import org.pushingpixels.flamingo.api.bcb.BreadcrumbItem
import org.pushingpixels.flamingo.api.bcb.BreadcrumbPathListener
import org.pushingpixels.flamingo.api.common.JCommandButton.CommandButtonKind
import org.pushingpixels.flamingo.api.ribbon.RibbonElementPriority
import org.pushingpixels.substance.api.SubstanceConstants
import org.pushingpixels.substance.api.SubstanceLookAndFeel
import org.slf4j.LoggerFactory

import ca.odell.glazedlists.BasicEventList
import ca.odell.glazedlists.Filterator
import ca.odell.glazedlists.TextFilterator
import ca.odell.glazedlists.TreeList
import ca.odell.glazedlists.TreeList.Format
import ca.odell.glazedlists.event.ListEventListener
import ca.odell.glazedlists.gui.TableFormat
import ca.odell.glazedlists.matchers.CompositeMatcherEditor
import ca.odell.glazedlists.matchers.MatcherEditor
import ca.odell.glazedlists.swing.EventTableModel
import ca.odell.glazedlists.swing.TextComponentMatcherEditor
import ca.odell.glazedlists.swing.TreeTableSupport

LogAdapter log = new Slf4jAdapter(LoggerFactory.getLogger(Coucou))

try {
	new Socket('localhost', 1337)
	println 'Already running'
	System.exit(0)
}
catch (Exception e) {
}

UIManager.put(SubstanceLookAndFeel.TABBED_PANE_CONTENT_BORDER_KIND, SubstanceConstants.TabContentPaneBorderKind.SINGLE_FULL)
UIManager.installLookAndFeel(new LookAndFeelInfo('Nebula', 'substance-nebula'))
UIManager.installLookAndFeel(new LookAndFeelInfo('Office Blue 2007', 'substance-office-blue-2007'))
UIManager.installLookAndFeel(new LookAndFeelInfo('Office Silver 2007', 'substance-office-silver-2007'))
UIManager.installLookAndFeel(new LookAndFeelInfo('Mariner', 'substance-mariner'))
UIManager.installLookAndFeel(new LookAndFeelInfo('Business Black Steel', 'substance-business-black-steel'))
UIManager.installLookAndFeel(new LookAndFeelInfo('Business Blue Steel', 'substance-business-blue-steel'))
UIManager.installLookAndFeel(new LookAndFeelInfo('Raven', 'substance-raven'))

def currentLookAndFeelInfo = {
	for (laf in UIManager.installedLookAndFeels) {
		if (UIManager.lookAndFeel.name == laf.name) {
			return laf
		}
//		else {
//			println "${UIManager.lookAndFeel.name} != ${it.name}"
//		}
	}
	return new LookAndFeelInfo(UIManager.lookAndFeel.name, UIManager.lookAndFeel.ID)
}

//def repoConfig = RepositoryConfig.create(Coucou.getResource("/config.xml").toURI(), new File(System.getProperty("user.home"), ".coucou/data").absolutePath)
//def repository = new TransientRepository(repoConfig)

new File(System.getProperty("user.home"), ".coucou/logs").mkdirs()
def configFile = new File(System.getProperty("user.home"), ".coucou/config.xml")
configFile.text = Coucou.getResourceAsStream("/config.xml").text


def ousia = new OusiaBuilder()

Thread.start {
	ServerSocket server = [1337]
	while(true) {
		try {
			server.accept {}
		}
		finally {
			ousia.doLater {
				frame.visible = true
			}
		}
	}
}

File repositoryLocation = [System.getProperty("user.home"), ".coucou/data"]

//@Field
//MailModule mailModule
//@Field
//FeedsModule feedsModule
//@Field
//ContactsModule contactsModule
//@Field
//PlannerModule plannerModule

ousia.edt {
	
	//	lookAndFeel('substance-nebula')
	lookAndFeel(prefs(Coucou).get('lookAndFeel', 'system'))
	
	fileChooser(id: 'dirChooser', fileSelectionMode: JFileChooser.FILES_AND_DIRECTORIES)
	
	dialog(title: rs('Repository Location'), size: [400, 150], show: true, modal: true, locationRelativeTo: null, id: 'repositoryLocationDialog') {
		panel() {
			textField(columns: 35, text: repositoryLocation.absolutePath, id: 'repositoryLocationField')
			repositoryLocationField.actionPerformed = {
				repositoryLocationOkButton.doClick()
			}
			
			button(text: rs('Browse'), actionPerformed: {
				if (dirChooser.showOpenDialog() == JFileChooser.APPROVE_OPTION) {
					repositoryLocationField.text = dirChooser.selectedFile.absolutePath
				}
			})
		}
		panel(constraints: BorderLayout.SOUTH) {
			flowLayout(new FlowLayout(FlowLayout.TRAILING))
			button(text: rs('Ok'), id: 'repositoryLocationOkButton', actionPerformed: {
				repositoryLocation = [repositoryLocationField.text]
				repositoryLocationDialog.dispose()
			})
			button(text: rs('Cancel'), actionPerformed: { System.exit(0)})
		}
		
		repositoryLocationDialog.windowClosing = {
			System.exit(0)
		}
	}
}

def context = new InitialContext()
RegistryHelper.registerRepository(context, 'coucou', configFile.absolutePath,
	 repositoryLocation.absolutePath, false)
def repository = context.lookup('coucou')

def session = repository.login(new SimpleCredentials('readonly', ''.toCharArray()))
Runtime.getRuntime().addShutdownHook({
//	session.logout()
    RegistryHelper.unregisterRepository(context, 'coucou')
})

Mailbox mailbox = [repository, 'Mail']

// initialise feeds..
Aggregator aggregator = [repository, 'Feeds']

ContactsManager contactsManager = [repository, 'Contacts']

def newContact = { events ->
	for (event in events) {
		log.log LogEntries.NODE_ADDED, event.path
		def message = session.getNode(event.path)
		if (message.hasNode('headers')) {
			def headers = message.getNode('headers')
			InternetAddress to = [headers.getProperty('To').string]
			def contactNode = contactsManager.add(to)
//			println "Added contact: ${contactNode.path}"
		}
	}
} as EventListener

session.workspace.observationManager.addEventListener(newContact, 1, '/Mail/folders/Sent', true, null, null, false)

Planner planner = [repository, 'Planner']

//def searchManager = new SearchManager(session)

def actionContext = [:] as ObservableMap

def dateGroup = { date ->
	def today = Calendar.instance
	today.clearTime()
	def yesterday = Calendar.instance
	yesterday.add Calendar.DAY_OF_YEAR, -1
	yesterday.clearTime()
	if (date < yesterday.time) {
		return 'Older Items'
	}
	else if (date < today.time) {
		return 'Yesterday'
	}
	else {
		return 'Today'
	}
}

def dateGroupComparator = {a, b ->
	def groups = ['Today', 'Yesterday', 'Older Items']
	groups.indexOf(a) - groups.indexOf(b)
} as Comparator

def groupComparators = [:]
groupComparators['Date'] = {a, b -> dateGroupComparator.compare(dateGroup(a.date), dateGroup(b.date))} as Comparator
groupComparators['Source'] = {a, b -> a.source <=> b.source} as Comparator

def selectedGroup = 'Date'
def selectedSort = ousia.rs('Date')

def sortComparators = [:]
sortComparators[ousia.rs('Date')] = {a, b ->
	int groupSort = groupComparators[selectedGroup].compare(a, b)
	(groupSort != 0) ? groupSort : b.date <=> a.date
} as Comparator
sortComparators[ousia.rs('Title')] = {a, b ->
	int groupSort = groupComparators[selectedGroup].compare(a, b)
	groupSort = (groupSort != 0) ? groupSort : a.title <=> b.title
	(groupSort != 0) ? groupSort : b.date <=> a.date
} as Comparator
sortComparators[ousia.rs('Source')] = {a, b ->
	int groupSort = groupComparators[selectedGroup].compare(a, b)
	groupSort = (groupSort != 0) ? groupSort : b.source <=> a.source
	(groupSort != 0) ? groupSort : b.date <=> a.date
} as Comparator

def resultLoaders = [:]
resultLoaders[SearchPathResult] = new SearchPathResultLoader()

/*
def reloadResults = {
	ousia.edt {
		
		if (breadcrumb.model.items[0].data.element.path == '/Mail') {
			frame.ribbon.setVisible frame.ribbon.getContextualTaskGroup(0), true
			if (breadcrumb.model.items[-1].data.name == 'Mail') {
				frame.ribbon.selectedTask = mailRibbonTask
			}
		}
		else {
			frame.ribbon.setVisible frame.ribbon.getContextualTaskGroup(0), false
		}
		
		if (breadcrumb.model.items[0].data.element.path == '/Feeds') {
			frame.ribbon.setVisible frame.ribbon.getContextualTaskGroup(1), true
			if (breadcrumb.model.items[-1].data.name == 'Feeds') {
				frame.ribbon.selectedTask = feedRibbonTask
			}
		}
		else {
			frame.ribbon.setVisible frame.ribbon.getContextualTaskGroup(1), false
		}
		
		if (breadcrumb.model.items[0].data.element.path == '/Contacts') {
			frame.ribbon.setVisible frame.ribbon.getContextualTaskGroup(2), true
			if (breadcrumb.model.items[-1].data.name == 'Contacts') {
				frame.ribbon.selectedTask = contactsRibbonTask
			}
		}
		else {
			frame.ribbon.setVisible frame.ribbon.getContextualTaskGroup(2), false
		}

		actionContext.addFolder = { folderName ->
			breadcrumb.model.items[-1].data.element.addNode(Text.escapeIllegalJcrChars(folderName))
			breadcrumb.model.items[-1].data.element.save()
		}
	}

	ousia.doOutside {
//						def items = breadcrumb.callback.getLeafs(breadcrumb.model.items)
		def items = breadcrumb.model.items[-1].data.results

		doLater {
			// install new renderer..
//							def defaultRenderer = new DefaultNodeTableCellRenderer(breadcrumb.model.items[-1].data)
			DefaultNodeTableCellRenderer defaultRenderer = [activityTree, ['Today', 'Yesterday', 'Older Items']]
			defaultRenderer.background = Color.WHITE
			
//							def dateRenderer = new DateCellRenderer(breadcrumb.model.items[-1].data)
			DateCellRenderer dateRenderer = [defaultRenderer]
			dateRenderer.background = Color.WHITE
			
			ttsupport.delegateRenderer = defaultRenderer
			activityTable.columnModel.getColumn(1).cellRenderer = defaultRenderer
			activityTable.columnModel.getColumn(2).cellRenderer = dateRenderer
			
			activities.withWriteLock {
				clear()
			}
		}

		 items.reverseEach {
			 def item = [:]
			 // feeds / items..
			 if (it.hasProperty('title')) {
				 item['title'] = HtmlCodes.unescape(it.getProperty('title').string)
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
			 // calendars..
			 else if (it.hasProperty('summary')) {
				 item['title'] = it.getProperty('summary').string
			 }
			 else {
				 item['title'] = it.name
			 }
			 
			 if (it.hasProperty('source')) {
				 if (it.getProperty('source').type == PropertyType.REFERENCE) {
					 item['source'] = HtmlCodes.unescape(it.getProperty('source').getNode().getProperty('title').string).intern()
				 }
				 else {
					 item['source'] = HtmlCodes.unescape(it.getProperty('source').string).intern()
				 }
			 }
			 // mail messages..
			 else if (it.hasNode('headers')) {
				 def headers = it.getNode('headers')
				 if (it.parent.parent.name == 'Sent' && headers.hasProperty('To')) {
					 item['source'] = headers.getProperty('To').string.intern()
				 }
				 else if (headers.hasProperty('From')) {
					 item['source'] = headers.getProperty('From').string.intern()
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
					 item['source'] = headers.getProperty('To').string.intern()
				 }
				 else if (headers.hasProperty('From')) {
					 item['source'] = headers.getProperty('From').string.intern()
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
			 item.seen = { node ->
				 node.seen?.boolean == true
			 }.curry(it)
			 
			 item.flagged = { node ->
				 node.flagged?.boolean == true
			 }.curry(it)
			 
			 doLater {
				 activities.withWriteLock {
					 add(item)
				 }
			 }
		}
		
		 doLater {
//							 if (activities.size() == 0) {
//								 statusMessage.text = rs('Nothing to see here')
//							 }
			 frame.contentPane.cursor = Cursor.defaultCursor
		 }
/*
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
*
	}

	ousia.doLater {
		activityTable.scrollRectToVisible(activityTable.getCellRect(0, 0, true))
	}
}
*/

def buildActivityTableModel = {
	ousia.build {
		new EventTableModel<?>(activityTree,
			[
				getColumnCount: {3},
				getColumnName: {column -> switch(column) {
						case 0: return 'Source'
						case 1: return 'Title'
						case 2: return 'When'
						default: return null
					}
				},
				getColumnValue: {object, column -> switch(column) {
					case 0: if (object instanceof String) {
						return object
					} else {
						return object['source']
					}
					case 1: if (!(object instanceof String)) {
//						return object['title'].replaceAll('&amp;', '&').replaceAll('<(/*)[^>]+>', '')
						return object['title']
					}
					case 2: if (!(object instanceof String)) {
	//									return new PrettyTime().format(object['date'])
						return object['date']
					}
				}}
			] as TableFormat)
	}
}

ousia.edt {
	
	// icons..
	resizableIcon('/logo.svg', size: [20, 20], id: 'logoIcon')
	resizableIcon('/add.svg', size: [16, 16], id: 'newIcon')
	resizableIcon('/task.svg', size: [16, 16], id: 'taskIcon')
	resizableIcon('/exit.svg', size: [16, 16], id: 'exitIcon')
	resizableIcon('/help.svg', size: [16, 16], id: 'helpIcon')
	resizableIcon('/ok.svg', size: [16, 16], id: 'okIcon')
	resizableIcon('/ok_all.svg', size: [16, 16], id: 'okAllIcon')
	resizableIcon('/cancel.svg', size: [16, 16], id: 'cancelIcon')
	resizableIcon('/cancel.svg', size: [12, 12], id: 'clearIcon')
	resizableIcon('/cancel.svg', size: [12, 12], id: 'deleteIcon')
	resizableIcon('/cancel.svg', size: [12, 12], id: 'deleteMailIcon')
	resizableIcon('/search.svg', size: [12, 12], id: 'searchIcon')
	resizableIcon('/im.svg', size: [16, 16], id: 'chatIcon')
	resizableIcon('/im.svg', size: [12, 12], id: 'chatIconSmall')
	resizableIcon('/reply.svg', size: [16, 16], id: 'replyIcon')
	resizableIcon('/replyAll.svg', size: [16, 16], id: 'replyAllIcon')
	resizableIcon('/forward.svg', size: [16, 16], id: 'forwardIcon')
	resizableIcon('/copy.svg', size: [16, 16], id: 'copyIcon')
	resizableIcon('/document.svg', size: [16, 16], id: 'documentIcon')
	resizableIcon('/document.svg', size: [16, 16], id: 'archiveIcon')
	resizableIcon('/import.svg', size: [16, 16], id: 'importIcon')
	resizableIcon('/export.svg', size: [16, 16], id: 'exportIcon')
	resizableIcon('/previous.svg', size: [16, 16], id: 'previousIcon')
	resizableIcon('/next.svg', size: [16, 16], id: 'nextIcon')
	resizableIcon('/reload.svg', size: [16, 16], id: 'refreshIcon')
    resizableIcon('/cancel.svg', size: [16, 16], id: 'cancelLoadIcon')
	resizableIcon('/forward.svg', size: [12, 12], id: 'folderIcon')
	resizableIcon('/event.svg', size: [16, 16], id: 'eventIcon')
	
	def frameIconImages = [imageIcon('/globe64.png').image, imageIcon('/globe48.png').image, imageIcon('/globe32.png').image, imageIcon('/globe16.png').image]
	
	actions {
        action id: 'exitAction', name: rs('Exit'), accelerator: shortcut('Q'), closure: {
            System.exit(0)
        }

		action id: 'openExplorerView', name: rs('Repository Explorer'), closure: {
			frame(title: rs('Repository Explorer'), size: [320, 240], show: true, defaultCloseOperation: JFrame.DISPOSE_ON_CLOSE, trackingEnabled: true) {
				widget(new ExplorerView(session.rootNode, frame, actionContext))
			}
		}
		
		action id: 'openLogView', name: rs('Log'), closure: {
			frame(title: rs('Log'), size: [320, 240], show: true, defaultCloseOperation: JFrame.DISPOSE_ON_CLOSE) {
				widget new LogView(new File("${System.getProperty('user.home')}/.coucou/logs/coucou.log"))
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
		
		action id: 'preferencesAction', name: rs('Preferences'), closure: {
			dialog(title: rs('Preferences'), size: [350, 250], show: true, owner: frame, modal: true, locationRelativeTo: frame, id: 'preferencesDialog') {
				borderLayout()
				panel(constraints: BorderLayout.CENTER, border: emptyBorder(10)) {
					label(text: rs('Look and Feel'))
//					comboBox(items: ['system', 'substance-nebula', 'substance-office-blue-2007'] as Object[], editable: false, itemStateChanged: { e->
					comboBox(items: UIManager.installedLookAndFeels, editable: false, renderer: new LookAndFeelInfoRenderer(), selectedItem: currentLookAndFeelInfo(),
						itemStateChanged: { e->
							doLater {
								lookAndFeel(e.source.selectedItem.className, 'system')
								SwingUtilities.updateComponentTreeUI(frame)
								SwingUtilities.updateComponentTreeUI(preferencesDialog)
								e.source.selectedItem = currentLookAndFeelInfo()
								prefs(Coucou).put('lookAndFeel', e.source.selectedItem.className)
							}
						})
				}
			}
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
			if (quickSearchField.text) {
				def searchQuery = new QueryBuilder(session.workspace.queryManager, session.valueFactory).with {
					query(
						source: selector(nodeType: 'nt:unstructured', name: 'items'),
						constraint: and(
							constraint1: descendantNode(selectorName: 'items', path: breadcrumb.model.items[-1].data.element.path),
							constraint2: fullTextSearch(selectorName: 'items', propertyName: 'description', searchTerms: quickSearchField.text)
						)
					)
				}
				SearchPathResult pr = [searchQuery, quickSearchField.text]
				def searchResult = new BreadcrumbItem<PathResult<?, javax.jcr.Node>>(pr.name, pr)
				searchResult.icon = searchIcon
				breadcrumb.model.addLast(searchResult)
			}
		}
		
		action id: 'markAsReadAction', name: rs('Mark As Read'), closure: {
			actionContext.markAsRead()
		}
		
		action id: 'markAllReadAction', name: rs('Mark All Read'), closure: {
			actionContext.markAllRead()
		}

		action id: 'deleteAction', name: rs('Delete'), SmallIcon: deleteIcon, closure: {
			actionContext.delete()
		}
		
		action id: 'archiveAction', name: rs('Archive'), SmallIcon: archiveIcon, closure: {
			actionContext.archive()
		}

		action id: 'newFolderAction', name: rs('New Folder'), closure: {
			folderName = JOptionPane.showInputDialog(frame, rs('Folder Name'))
			if (folderName && actionContext.addFolder) {
				actionContext.addFolder(folderName)
			}
		}
		
		action id: 'refreshAction', name: rs('Refresh'), closure: {
			reloadResults()
		}
	}

	fileChooser(id: 'chooser')
	fileChooser(id: 'imageChooser', fileFilter: new ImageFileFilter())
	
	ribbonFrame(title: rs('Coucou'), size: [640, 480], show: true, locationRelativeTo: null,
		defaultCloseOperation: JFrame.EXIT_ON_CLOSE, id: 'frame', iconImages: frameIconImages,
		applicationIcon: logoIcon, trackingEnabled: true) {
		
		mailModule = new MailModule(mailbox: mailbox)
		mailModule.initUI(ousia)
		resultLoaders[MailboxNodePathResult] = mailModule
		resultLoaders[StorePathResult] = mailModule
		resultLoaders[FolderPathResult] = mailModule
		resultLoaders[FolderNodePathResult] = mailModule

		feedsModule = new FeedsModule(aggregator: aggregator)
		feedsModule.initUI(ousia)
		resultLoaders[FeedsNodePathResult] = feedsModule
		resultLoaders[FeedNodePathResult] = feedsModule

		contactsModule = new ContactsModule(contactsManager: contactsManager)
		contactsModule.initUI(ousia)
		resultLoaders[ContactsNodePathResult] = contactsModule
		resultLoaders[RosterPathResult] = contactsModule
		resultLoaders[RosterGroupPathResult] = contactsModule
		
		plannerModule = new PlannerModule(planner: planner)
		plannerModule.initUI(ousia)
		resultLoaders[PlannerNodePathResult] = plannerModule
		resultLoaders[CalendarStorePathResult] = plannerModule
		resultLoaders[CalendarCollectionPathResult] = plannerModule
		
		
		ribbonApplicationMenu(id: 'appMenu') {
			ribbonApplicationMenuEntryPrimary(id: 'newMenu', icon: newIcon, text: rs('New'), kind: CommandButtonKind.POPUP_ONLY)
//				['groupTitle': 'New Items', 'entries': [
//			ribbonApplicationMenuEntrySecondary(id: 'newFeed', icon: feedIcon, text: rs('Feed'), kind: CommandButtonKind.ACTION_ONLY, actionPerformed: addFeedAction)
//			}
			newMenu.addSecondaryMenuGroup 'Create a new item', newCalendar
			
			appMenu.addMenuSeparator()
			ribbonApplicationMenuEntryPrimary(id: 'saveAsMenu', icon: forwardIcon, text: rs('Save As'), kind: CommandButtonKind.ACTION_AND_POPUP_MAIN_ACTION)
			ribbonApplicationMenuEntrySecondary(id: 'saveAsFile', icon: documentIcon, text: rs('File'), kind: CommandButtonKind.ACTION_ONLY)
			ribbonApplicationMenuEntrySecondary(id: 'saveAsTemplate', icon: documentIcon, text: rs('Template'), kind: CommandButtonKind.ACTION_ONLY)
			saveAsMenu.addSecondaryMenuGroup 'Save Item', saveAsFile, saveAsTemplate

			appMenu.addMenuSeparator()
			ribbonApplicationMenuEntryPrimary(id: 'importMenu', icon: importIcon, text: rs('Import'), kind: CommandButtonKind.POPUP_ONLY)
			importMenu.addSecondaryMenuGroup 'Import external data', importMail, importFeeds

			ribbonApplicationMenuEntryPrimary(id: 'exportMenu', icon: exportIcon, text: rs('Export'), kind: CommandButtonKind.POPUP_ONLY)
			exportMenu.addSecondaryMenuGroup 'Export data', exportFeeds
			appMenu.addMenuSeparator()
			
			ribbonApplicationMenuEntryPrimary(icon: exitIcon, text: rs('Exit'), kind: CommandButtonKind.ACTION_ONLY, actionPerformed: exitAction)
			ribbonApplicationMenuEntryFooter(text: rs('Preferences'), actionPerformed: preferencesAction)
		}
		frame.ribbon.applicationMenu = appMenu
		frame.ribbon.configureHelp helpIcon, aboutAction
/*
		ribbonBand(rs('Avatar'), id: 'avatarBand', resizePolicies: ['mirror']) {
			ribbonComponent(
				component: commandButton(forwardIcon),
				priority: RibbonElementPriority.TOP
			)
		}
		
		ribbonBand(rs('Advanced'), id: 'advancedSearchBand', resizePolicies: ['mirror']) {
			ribbonComponent([
				component: commandButton(rs('New Search')),
				priority: RibbonElementPriority.TOP
			])
			ribbonComponent([
				component: commandButton(rs('Saved Searches')),
				priority: RibbonElementPriority.MEDIUM
			])
		}
*/
		frame.ribbon.addTask(ribbonTask(rs('Home'), bands: [
		
			ribbonBand(rs('Navigate'), icon: taskIcon, id: 'navigationBand', resizePolicies: ['mirror']) {
	            /*
				ribbonComponent([
					component: commandButtonStrip(displayState: CommandButtonDisplayState.BIG) {
						commandButton(previousIcon, text: rs('Previous'), id: 'previousButton', actionPerformed: {actionContext.previousItem()} as ActionListener) {
							bind(source: actionContext, sourceProperty: 'previousItem', target: previousButton, targetProperty: 'enabled', converter: {it != null})
						}
						commandButton(nextIcon, text: rs('Next'), id: 'nextButton', actionPerformed: {actionContext.nextItem()} as ActionListener) {
							bind(source: actionContext, sourceProperty: 'nextItem', target: nextButton, targetProperty: 'enabled', converter: {it != null})
						}
					},
					rowSpan: 3
				])
				*/
	            ribbonComponent(
	                component: commandButton(previousIcon, text: rs('Previous'), id: 'previousButton', actionPerformed: {actionContext.previousItem()} as ActionListener) {
	                        bind(source: actionContext, sourceProperty: 'previousItem', target: previousButton, targetProperty: 'enabled', converter: {it != null})
	                    },
	                priority: RibbonElementPriority.TOP
	            )
	            ribbonComponent(
	                component: commandButton(nextIcon, text: rs('Next'), id: 'nextButton', actionPerformed: {actionContext.nextItem()} as ActionListener) {
	                        bind(source: actionContext, sourceProperty: 'nextItem', target: nextButton, targetProperty: 'enabled', converter: {it != null})
	                    },
	                priority: RibbonElementPriority.TOP
	            )
			},
        
	        ribbonBand(rs('Load'), icon: taskIcon, id: 'loadBand', resizePolicies: ['mirror']) {
	            ribbonComponent(
	                component: commandButton(refreshIcon, action: refreshAction),
	                priority: RibbonElementPriority.TOP
	            )
	            ribbonComponent(
	                component: commandButton(cancelLoadIcon, text: rs('Cancel')),
	                priority: RibbonElementPriority.TOP
	            )
	        },
		
			ribbonBand(rs('Quick Search'), icon: forwardIcon, id: 'quickSearchBand', resizePolicies: ['mirror']) {
				ribbonComponent([
					component: textField(id: 'quickSearchField', columns: 14, enabled: false, prompt: quickSearchAction.getValue('Name'), promptFontStyle: Font.ITALIC, promptForeground: Color.LIGHT_GRAY,
						keyPressed: {e-> if (e.keyCode == KeyEvent.VK_ESCAPE) e.source.text = null}) {
						
						quickSearchField.addActionListener quickSearchAction
						quickSearchField.addBuddy commandButton(searchIcon, enabled: false, actionPerformed: quickSearchAction, id: 'quickSearchButton'), BuddySupport.Position.RIGHT
					},
					rowSpan: 1
				])
	
				ribbonComponent([
					component: checkBox(text: rs('Include Archived')),
					rowSpan: 1
				])
				ribbonComponent([
					component: checkBox(text: rs('Include Deleted')),
					rowSpan: 1
				])
			},
		]))
		
		frame.ribbon.addTask(ribbonTask(rs('View'), bands: [
		
			ribbonBand(rs('Group By'), icon: forwardIcon, id: 'groupByBand', resizePolicies: ['mirror']) {
				ribbonComponent([
					component: comboBox(items: [rs('Date'), rs('Source')] as Object[], editable: false, itemStateChanged: { e->
						doLater {
							selectedGroup = e.source.selectedItem
							sortedActivities.comparator = sortComparators[selectedSort]
							
							if (e.source.selectedItem == rs('Source')) {
								treeList(sortedActivities,
									 expansionModel: TreeList.NODES_START_COLLAPSED, format: [
								        allowsChildren: {element -> true},
								        getComparator: {depth -> },
								        getPath: {path, element ->
											path << element.source
											path << element
										 }
								    ] as Format<?>, id: 'activityTree')
								
								activityTable.model = buildActivityTableModel()
							}
							else {
								treeList(sortedActivities,
									 expansionModel: new DateExpansionModel(), format: [
								        allowsChildren: {element -> true},
								        getComparator: {depth -> },
								        getPath: {path, element ->
											path << dateGroup(element.date)
											path << element
										 }
								    ] as Format<?>, id: 'activityTree')
								
								activityTable.model = buildActivityTableModel()
							}
							
							ttsupport.uninstall()
							ttsupport = TreeTableSupport.install(activityTable, activityTree, 0)
							ttsupport.arrowKeyExpansionEnabled = true
							ttsupport.delegateRenderer.background = Color.WHITE
							
							DefaultNodeTableCellRenderer defaultRenderer = [activityTree, []]
							defaultRenderer.background = Color.WHITE
							
							DateCellRenderer dateRenderer = [activityTree]
							dateRenderer.background = Color.WHITE
							
							ttsupport.delegateRenderer = defaultRenderer
							activityTable.columnModel.getColumn(1).cellRenderer = defaultRenderer
							activityTable.columnModel.getColumn(2).cellRenderer = dateRenderer
							
						}
					}),
					rowSpan: 1
				])
			},
		
			ribbonBand(rs('Sort By'), icon: forwardIcon, id: 'sortBand', resizePolicies: ['mirror']) {
				ribbonComponent([
					component: comboBox(items: sortComparators.keySet() as Object[], editable: false, itemStateChanged: { e->
						doLater {
							selectedSort = e.source.selectedItem
							sortedActivities.comparator = sortComparators[selectedSort]
						}
					}),
					rowSpan: 1
				])
	//			commandButton(rs('Sort Order'), commandButtonKind: CommandButtonKind.POPUP_ONLY, popupOrientationKind: CommandButtonPopupOrientationKind.SIDEWARD, popupCallback: {
	//				commandPopupMenu() {
	//					commandToggleMenuButton(rs('Ascending'))
	//					commandToggleMenuButton(rs('Descending'))
	//				}
	//			} as PopupPanelCallback)
			},

			ribbonBand(rs('Filter'), icon: forwardIcon, id: 'filterBand', resizePolicies: ['mirror']) {
				ribbonComponent(
					component: textField(columns: 14, prompt: rs('Type To Filter..'), promptFontStyle: Font.ITALIC, promptForeground: Color.LIGHT_GRAY, id: 'filterTextField', keyPressed: {e-> if (e.keyCode == KeyEvent.VK_ESCAPE) e.source.text = null}),
					rowSpan: 1
				) {
					filterTextField.addBuddy commandButton(clearIcon, actionPerformed: {filterTextField.text = null} as ActionListener), BuddySupport.Position.RIGHT
				}
				ribbonComponent(
					component: checkBox(text: rs('Unread Items'), id: 'unreadFilterCheckbox'),
					rowSpan: 1
				)
				ribbonComponent(
					component: checkBox(text: rs('Important Items'), id: 'importantFilterCheckbox'),
					rowSpan: 1
				)
			},

			ribbonBand(rs('Show/Hide'), icon: forwardIcon, id: 'showHideBand', resizePolicies: ['mirror']) {
				ribbonComponent(
					component: commandToggleButton(id: 'toggleTableHeader', rs('Table Header'),
						 actionPerformed: { e->
							 if (e.source.actionModel.selected) {
								 activityTable.tableHeader.visible = true
								 activityTable.tableHeader.preferredSize = null
					 		 }
							 else {
								 activityTable.tableHeader.visible = false
								 activityTable.tableHeader.preferredSize = [-1, 0]
							 }
						 } as ActionListener),
					priority: RibbonElementPriority.TOP
				)
				
				ribbonComponent(
					component: commandToggleButton(id: 'toggleStatusBar', rs('Status Bar'), actionPerformed: {e-> statusBar.visible = e.source.actionModel.selected} as ActionListener),
					priority: RibbonElementPriority.TOP
				)
			},
		
			ribbonBand(rs('Mode'), icon: forwardIcon, id: 'viewModeBand', resizePolicies: ['mirror']) {
				ribbonComponent([
					component: commandToggleButton(rs('Fullscreen'), actionPerformed: fullScreenAction),
					priority: RibbonElementPriority.TOP
				])
			},
		]))
		
		frame.ribbon.addTask(ribbonTask(rs('Folder'), bands: [
 
			ribbonBand(rs('New Folder'), id: 'newFolderBand', resizePolicies: ['mirror']) {
				ribbonComponent(
					component: commandButton(newIcon, action: newFolderAction),
					priority: RibbonElementPriority.TOP
				)
			},
		]))
		
//		frame.ribbon.addTask new RibbonTask(rs('Search'), advancedSearchBand, quickSearchBand)
//		frame.ribbon.addTask(new RibbonTask(rs('Action'), updateBand, organiseBand, actionExtrasBand))
//		frame.ribbon.addTask(new RibbonTask(rs('Presence'), avatarBand))
		
		frame.ribbon.addTask(ribbonTask(rs('Tools'), bands: [

			ribbonBand(rs('Tools'), icon: taskIcon, id: 'toolsBand', resizePolicies: ['mirror']) {
				ribbonComponent(
					component: commandButton(taskIcon, action: openExplorerView),
					priority: RibbonElementPriority.TOP
				)
				ribbonComponent(
					component: commandButton(taskIcon, action: openLogView),
					priority: RibbonElementPriority.MEDIUM
				)
			},
		]))

		panel {
			borderLayout()
//			breadcrumbFileSelector(path: new File(System.getProperty('user.home')), constraints: BorderLayout.NORTH)
			
//			breadcrumbBar(new NodeCallback(session.rootNode), constraints: BorderLayout.NORTH, id: 'breadcrumb')
			def pathIcons = [:]
			pathIcons[SearchPathResult] = searchIcon
			pathIcons[FeedNodePathResult] = feedIconSmall
//			pathIcons[FolderNodePathResult] = mailIconSmall
//			pathIcons[StorePathResult] = mailIconSmall
//			pathIcons[RosterPathResult] = chatIconSmall
			
			pathResultContext = new PathResultContext()
			breadcrumbBar(new PathResultCallback(root: new RootNodePathResult(session.rootNode, pathResultContext), pathIcons: pathIcons), throwsExceptions: false, constraints: BorderLayout.NORTH, id: 'breadcrumb')

			activities = new BasicEventList<?>()
			
			// XXX: when look and feel changes the breadcrumb is reset..
			UIManager.addPropertyChangeListener({ e ->
				if (e.propertyName == 'lookAndFeel') {
					activities.withWriteLock {
						clear()
					}
				}
			} as PropertyChangeListener)

			splitPane(orientation: JSplitPane.VERTICAL_SPLIT, dividerLocation: 200, continuousLayout: true, oneTouchExpandable: true, dividerSize: 10) {
				
				scrollPane(constraints: 'left', horizontalScrollBarPolicy: JScrollPane.HORIZONTAL_SCROLLBAR_NEVER, border: null, viewportBorder: null, id: 'sp') {
	//				sp.viewport.background = Color.RED
					table(gridColor: Color.LIGHT_GRAY, showHorizontalLines: false, opaque: false, border: null, id: 'activityTable') {
						
						def filters = new BasicEventList<MatcherEditor<?>>()
						filters << new TextComponentMatcherEditor(filterTextField, { baseList, e ->
							baseList << e['title']
						} as TextFilterator, true)
						filters << new JCheckboxMatcherEditor(unreadFilterCheckbox, { baseList, e ->
							/*
							if (e['node'].hasProperty('seen')) {
								baseList << !e['node'].getProperty('seen').boolean
							}
							else if (e['node'].hasProperty('flags') && e['node'].flags.values.collect { it.string}.contains('seen')) {
								baseList << false
							}
							else {
								baseList << true
							}
							*/
							baseList << !e.seen()
						} as Filterator)
						filters << new JCheckboxMatcherEditor(importantFilterCheckbox, { baseList, e ->
//							baseList << (e['node'].flagged && e['node'].flagged.boolean)
							baseList << e.flagged()
						} as Filterator)

						CompositeMatcherEditor filterMatcherEditor = [filters]
						filterMatcherEditor.mode = CompositeMatcherEditor.AND
						filterList(activities, id: 'filteredActivities', matcherEditor: filterMatcherEditor)
						
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

						treeList(sortedList(filteredActivities, comparator: sortComparators[selectedSort], id: 'sortedActivities'),
							 expansionModel: new DateExpansionModel(), format: [
						        allowsChildren: {element -> true},
						        getComparator: {depth -> },
						        getPath: {path, element ->
									path << dateGroup(element.date)
									path << element
								 }
						    ] as Format<?>, id: 'activityTree')
						
						activityTable.model = buildActivityTableModel()
						
						activityTable.tableHeader.visible = false
						activityTable.tableHeader.preferredSize = [-1, 0]
						
						ttsupport = TreeTableSupport.install(activityTable, activityTree, 0)
						ttsupport.arrowKeyExpansionEnabled = true
	//					ttsupport.delegateRenderer = new DefaultTableCellRenderer(opaque: false)
	//					ttsupport.delegateRenderer = new DefaultNodeTableCellRenderer(session.rootNode)
						ttsupport.delegateRenderer.background = Color.WHITE
						
						activityTable.columnModel.getColumn(0).maxWidth = 250
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
									
									if (entryIndex > 0) {
										actionContext.previousItem = {
											int previousIndex = activityTable.convertRowIndexToView(entryIndex - 1)
											activityTable.setRowSelectionInterval(previousIndex, previousIndex)
											activityTable.scrollRectToVisible(activityTable.getCellRect(previousIndex, 0, true))
										}
									}
									else {
										actionContext.previousItem = null
									}
									
									if (entryIndex < activityTree.size() - 1) {
										actionContext.nextItem = {
											int nextIndex = activityTable.convertRowIndexToView(entryIndex + 1)
											activityTable.setRowSelectionInterval(nextIndex, nextIndex)
											activityTable.scrollRectToVisible(activityTable.getCellRect(nextIndex, 0, true))
										}
									}
									else {
										actionContext.nextItem = null
									}

									def entry = activityTree[entryIndex]
									if (entry && entry instanceof Map) {
										// update action context..
										if (entry['node'].hasProperty('link')) {
											actionContext.markAsRead = {
												aggregator.markNodeRead entry['node']
			                                    if (entryIndex < activityTree.size() - 1) {
													int nextIndex = activityTable.convertRowIndexToView(entryIndex + 1)
			                                        activityTable.setRowSelectionInterval(nextIndex, nextIndex)
													activityTable.scrollRectToVisible(activityTable.getCellRect(nextIndex, 0, true))
			                                    }
											}
											actionContext.delete = {
												aggregator.delete entry['node']
												activityTree.remove entryIndex
											}
/*
											actionContext.toggleBookmark = {
												entry['node'].flagged = !(entry['node'].flagged && entry['node'].flagged.boolean)
												aggregator.save entry['node']
												activityTable.model.fireTableRowsUpdated activityTable.selectedRow, activityTable.selectedRow
											}
*/
											bookmarkFeedButton.enabled = true
											bookmarkFeedButton.actionModel.selected = entry['node'].flagged && entry['node'].flagged.boolean
										}
										else if (entry['node'].parent.path == '/Feeds') {
											actionContext.markAsRead = null
											actionContext.delete = {
												aggregator.delete entry['node']
												activityTree.remove entryIndex
											}
											bookmarkFeedButton.enabled = false
											bookmarkFeedButton.actionModel.selected = false
										}
										else if (entry['node'].hasNode('headers')) {
											actionContext.markAsRead = null
											actionContext.delete = {
												mailbox.delete entry['node']
												activityTree.remove entryIndex
											}
											actionContext.archive = {
												mailbox.archive entry['node']
												activityTree.remove entryIndex
											}
											bookmarkFeedButton.enabled = false
											bookmarkFeedButton.actionModel.selected = false
										}
										else {
											actionContext.markAsRead = null
											actionContext.delete = null
											actionContext.archive = null
											bookmarkFeedButton.enabled = false
											bookmarkFeedButton.actionModel.selected = false
										}
										
										def resultLoader = resultLoaders[breadcrumb.model.items[-1].data.class]
										if (resultLoader) {
											resultLoader.loadPreview ousia, entry
										}
										else {
											edt {
												contentTitle.text = "<html><strong>${entry['title']}</strong><br/>${entry['source']} <em>${entry['date']}</em></html>"
												
												contentView.editorKit = defaultEditorKit
												
												if (entry['node'].hasProperty('description')) {
				//                                        println "Entry selected: ${entryList.model[entryList.selectedRow]}"
													def content = entry['node'].getProperty('description').string.replaceAll(/(?<=img src\=\")http:\/\/.+:.*(?=")/, 'http://coucou.im/favicon.gif') //.replaceAll(/(http:\/\/)?([a-zA-Z0-9\-.]+\.[a-zA-Z0-9\-]{2,}([\/]([a-zA-Z0-9_\/\-.?&%=+])*)*)(\s+|$)/, '<a href="http://$2">$2</a> ')
													contentView.text = content
													contentView.caretPosition = 0
												}
												else if (entry['node'].hasNode('body')) {
													def content = entry['node'].getNode('body').getNode('part').getNode('jcr:content')
													if (content.getProperty('jcr:mimeType').string =~ /(?i)^text\/plain.*$/) {
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
												else if (entry['node'].hasNode('description')) {
													def content = entry['node'].getNode('description').getNode('text').getNode('jcr:content').getProperty('jcr:data').string
													contentView.text = content
													contentView.caretPosition = 0
												}
												else {
													contentView.text = null
												}
												previewPane.layout.show(previewPane, 'defaultView')
											}
										}
									}
									else {
										edt {
											contentTitle.text = ''
											contentView.text = null
											previewPane.layout.show(previewPane, 'defaultView')
										}
										actionContext.markAsRead = null
										bookmarkFeedButton.enabled = false
										bookmarkFeedButton.actionModel.selected = false
									}
								}
								else {
									edt {
										contentTitle.text = ''
										contentView.text = null
										previewPane.layout.show(previewPane, 'defaultView')
									}
									actionContext.markAsRead = null
									actionContext.previousItem = null
									actionContext.nextItem = null
									bookmarkFeedButton.enabled = false
									bookmarkFeedButton.actionModel.selected = false
								}
							}
						}
		
		                activityTable.mouseClicked = { e ->
		                    if (e.button == MouseEvent.BUTTON1 && e.clickCount >= 2 && activityTable.selectedRow >= 0) {
		                        def selectedItem = activityTree[activityTable.convertRowIndexToModel(activityTable.selectedRow)]
								// feed item..
		                        if (selectedItem.node.hasProperty('link')) {
									doOutside {
			                            Desktop.desktop.browse(URI.create(selectedItem.node.getProperty('link').value.string))
										aggregator.markNodeRead selectedItem.node
										activityTable.model.fireTableRowsUpdated activityTable.selectedRow, activityTable.selectedRow
									}
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
									doOutside {
										File file = [System.getProperty('java.io.tmpdir'), selectedItem.node.name]
										file.bytes = selectedItem.node.getNode('jcr:content').getProperty('jcr:data').binary.stream.bytes
										Desktop.desktop.open(file)
									}
								}
								else {
									doOutside {
										final PathResult pr = breadcrumb.model.items[-1].data.getChild(selectedItem.node)
										def breadcrumbItem = new BreadcrumbItem<PathResult>(pr.name, pr)
										breadcrumbItem.icon = pathIcons[pr.class]
										
										doLater {
											breadcrumb.model.addLast(breadcrumbItem)
										}
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
				
				panel(constraints: 'right') {
					borderLayout()
					
					label(constraints: BorderLayout.NORTH, border: emptyBorder(5), id: 'contentTitle') {
//						contentTitle.font = contentTitle.font.deriveFont(Font.BOLD, 14f)
					}
					
					panel(id: 'previewPane') {
						cardLayout()
						
						panel(id: 'defaultView', constraints: 'defaultView') {
							borderLayout()
							scrollPane {
								def styleSheet = new StyleSheet()
								styleSheet.addRule("body {background-color:#ffffff; color:#444b56; font-family:verdana,sans-serif; margin:8px; }")
								defaultEditorKit = new HTMLEditorKitExt(styleSheet: styleSheet)
								editorPane(id: 'contentView', editorKit: defaultEditorKit, editable: false, contentType: 'text/html', opaque: true, border: null)
							}
						}
						panel(feedItemView, constraints: 'feedItemView')
					}
/*
					def statusLayer = new StatusLayerUI()
					layer(statusLayer) {
						scrollPane {
							def styleSheet = new StyleSheet()
							styleSheet.addRule("body {background-color:#ffffff; color:#444b56; font-family:verdana,sans-serif; margin:8px; }")
					//        styleSheet.addRule("a {text-decoration:underline; color:blue; }")
					//                            styleSheet.addRule("a:hover {text-decoration:underline; }")
					//        styleSheet.addRule("img {border-width:0; }")
							
							defaultEditorKit = new HTMLEditorKitExt(styleSheet: styleSheet)
					
							editorPane(id: 'contentView', editorKit: defaultEditorKit, editable: false, contentType: 'text/html', opaque: true, border: null)
							contentView.addHyperlinkListener(new HyperlinkBrowser(feedback: [
									show: { uri -> statusLayer.showStatusMessage uri.toString() },
									hide: { statusLayer.hideStatusMessage() }
								] as HyperlinkFeedback))
						}
					}
*/
				}
			}

			breadcrumb.model.addPathListener({
				edt {
					
					filterTextField.text = null
					unreadFilterCheckbox.selected = false
					importantFilterCheckbox.selected = false
					
					frame.title = breadcrumb.model.items.collect({ it.data.name }).join(' | ') << ' - Coucou'
					
					// enable/disable ribbon tasks..
					quickSearchField.text = null
					quickSearchField.enabled = !breadcrumb.model.items[-1].data.leaf
					quickSearchButton.enabled = !breadcrumb.model.items[-1].data.leaf

					doOutside {
						try {
							doLater {
								frame.contentPane.cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
								breadcrumb.enabled = false
							}
							
							def resultLoader = resultLoaders[breadcrumb.model.items[-1].data.class]
							if (resultLoader) {
								resultLoader.loadResults(ousia, activities, ttsupport, breadcrumb.model.items[-1].data)
							}
							else {
								reloadResults()
							}
						}
						finally {
							doLater {
								activityTable.scrollRectToVisible(activityTable.getCellRect(0, 0, true))
								frame.contentPane.cursor = Cursor.defaultCursor
								breadcrumb.enabled = true
							}
						}
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

		def progressLayer = new ProgressLayerUI()
		aggregator.progressMonitor = progressLayer
		layer(progressLayer, constraints: BorderLayout.SOUTH, id: 'statusBar', trackingEnabled: true) {
			statusBar() {
				label(id: 'statusMessage', text: rs('Ready'), constraints: new JXStatusBar.Constraint(FILL))
	//			bind(source: viewStatusBar, sourceProperty: 'selected', target: statusBar, targetProperty:'visible')
			}
		}
		toggleStatusBar.actionModel.selected = statusBar.visible
	}
		
		
	Thread.defaultUncaughtExceptionHandler = new DefaultExceptionHandler(dialogOwner: frame)
	Authenticator.default = new org.mnode.ousia.DialogAuthenticator(frame)
	
	aggregator.start()
	
	mailbox.passwordPrompt = new DialogAuthenticator(frame)
	mailbox.start()
	
	contactsManager.passwordPrompt = { message ->
		passwordField(id: 'passwordField', columns: 20)
		if (JOptionPane.showConfirmDialog(frame, passwordField,
				message, JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
			return passwordField.text
		}
	}
	
	doOutside {
		contactsManager.connect()
		pathResultContext.putSharedValue('xmppConnections', contactsManager.xmppConnections)
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
	if (!contactsManager.xmppConnections.isEmpty()) {
	frame(id: 'chatFrame', size: [300, 200], locationRelativeTo: null, defaultCloseOperation: JFrame.HIDE_ON_CLOSE,
		iconImages: frameIconImages, trackingEnabled: true, title: 'Conversations - Coucou') {
		
		panel() {
			borderLayout()
			comboBox(id: 'peers', constraints: BorderLayout.NORTH, items: contactsManager.xmppConnections[0].roster.entries as Object[], editable: false) {
				peers.renderer = new PeerListCellRenderer(contactsManager.xmppConnections[0])
			}
			scrollPane() {
//				list(id: 'messages', model: new DefaultListModel()) {
//					messages.cellRenderer = new MessageListCellRenderer(contactsManager.xmpp)
//				}
				StyledDocument messagesDoc = new DefaultStyledDocument()
				participantStyle = messagesDoc.addStyle 'participant', StyleContext.defaultStyleContext.getStyle(StyleContext.DEFAULT_STYLE)
				StyleConstants.setForeground(participantStyle, Color.LIGHT_GRAY)
				messagesDoc.addStyle 'message', StyleContext.defaultStyleContext.getStyle(StyleContext.DEFAULT_STYLE)
				
				textPane(document: messagesDoc, editable: false, id: 'messages')
			}
			panel(constraints: BorderLayout.SOUTH) {
				borderLayout()
				
				textField(id: 'chatStatus', editable: false, foreground: Color.LIGHT_GRAY, border: emptyBorder(2), constraints: BorderLayout.NORTH)
				
				scrollPane(horizontalScrollBarPolicy: JScrollPane.HORIZONTAL_SCROLLBAR_NEVER) {
					
					textArea(id: 'entry', rows: 4, wrapStyleWord: true) {
						entry.keyPressed = { e ->
							if (e.keyCode == KeyEvent.VK_ENTER && entry.text) {
		                        Message message = new Message()
		                        message.body = entry.text
		                        entry.text = null
								def chat = contactsManager.activeChats[peers.selectedItem]
								if (!chat) {
									contactsManager.activeChats[peers.selectedItem] = contactsManager.xmppConnections[0].chatManager.createChat(peers.selectedItem.user,
										[ 
											processMessage: { achat, msg ->
												if (msg.body) {
//							                        messages.model.addElement msg
//							                        messages.ensureIndexIsVisible(messages.model.size - 1);
													messages.document.append "${contactsManager.getXmppName(msg, contactsManager.xmppConnections[0])}: ", 'participant'
													messages.document.append "${msg.body}\n", 'message'
													messages.caretPosition = messages.document.length
												}
											},
										
											stateChanged: { achat, state ->
												if (state == ChatState.composing) {
													chatStatus.text = "$achat.participant is typing.."
												}
												else if (state == ChatState.gone) {
													chatStatus.text = "$achat.participant has left the conversation."
												}
												else {
													chatStatus.text = null
												}
											}
										] as ChatStateListener)
									chat = contactsManager.activeChats[peers.selectedItem]
								}
		                        chat.sendMessage(message);
//		                        messages.model.addElement message
//		                        messages.ensureIndexIsVisible(messages.model.size - 1);
								messages.document.append "${contactsManager.getXmppName(message, contactsManager.xmppConnections[0])}: ", 'participant'
								messages.document.append "${message.body}\n", 'message'
								messages.caretPosition = messages.document.length
								e.consume()
							}
						}
					}
				}
				
				entry.document.addDocumentListener({
					def chat = contactsManager.activeChats[peers.selectedItem]
					if (chat) {
						if (entry.text.isEmpty()) {
	                        ChatStateManager.getInstance(contactsManager.xmppConnections[0]).setCurrentState(ChatState.inactive, chat);
						}
						else {
							ChatStateManager.getInstance(contactsManager.xmppConnections[0]).setCurrentState(ChatState.composing, chat)
						}
					}
				} as DocumentListener)
			}
		}
	}
/*
	popup = new PopupMenu()
	MenuItem open = [rs('Open')]
	open.font = new Font('Helvetica', Font.BOLD, 12)
	open.actionPerformed = {openDialog()}
	popup.add open
	popup.addSeparator()
	MenuItem exit = [rs('Exit')]
	exit.actionPerformed = {System.exit(0)}
	popup.add exit
*/
	trayIcon = new TrayIcon(ousia.imageIcon('/icons/liquidicity/red-chat.png').image, rs('Coucou')) //, popup)
	trayIcon.imageAutoSize = true
	trayIcon.mouseReleased = { e ->
		if (e.button == MouseEvent.BUTTON1) {
			if (System.getProperty('os.name') =~ /(?i)windows/ && e.clickCount < 2) {
				return
			}
			chatFrame.visible = !chatFrame.active
		}
	}
	
	SystemTray tray = SystemTray.systemTray
	tray.add trayIcon
	}
}
