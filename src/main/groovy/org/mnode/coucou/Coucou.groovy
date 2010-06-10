/**

* This file is part of Base Modules.
 *
 * Copyright (c) 2010, Ben Fortuna [fortuna@micronode.com]
 *
 * Base Modules is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Base Modules is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Base Modules.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.mnode.coucou

import groovy.beans.Bindable
import groovy.swing.SwingXBuilder
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Component
import java.awt.Cursor
import java.awt.Desktop
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.Font
import java.awt.Graphics
import java.awt.Image
import java.awt.Insets
import java.awt.Rectangle
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import java.awt.event.MouseEvent
import javax.swing.AbstractAction
import javax.swing.UIManager
import groovy.swing.LookAndFeelHelper
import java.awt.TrayIcon
import java.awt.PopupMenu
import java.awt.SystemTray
import java.awt.MenuItem
import java.util.regex.Pattern
import javax.swing.table.AbstractTableModel
import javax.swing.DefaultComboBoxModel
import javax.swing.JFrame
import javax.swing.JTextField
import javax.swing.JFileChooser
import javax.swing.JScrollPane
import javax.swing.JTabbedPane
import javax.swing.JSplitPane
import javax.swing.SwingConstants
import javax.swing.event.DocumentListener
import javax.swing.event.DocumentEvent
import javax.swing.tree.DefaultMutableTreeNode as TreeNode
import javax.swing.text.html.HTMLEditorKit
import org.jvnet.substance.SubstanceLookAndFeel
import org.jvnet.substance.api.SubstanceConstants
import org.jvnet.substance.api.SubstanceConstants.TabCloseKind
import org.jvnet.substance.api.tabbed.TabCloseCallback
import org.jvnet.substance.api.tabbed.VetoableMultipleTabCloseListener
import org.jvnet.substance.api.tabbed.VetoableTabCloseListener
import org.jvnet.lafwidget.LafWidget
import org.jvnet.lafwidget.tabbed.DefaultTabPreviewPainter
import org.jvnet.lafwidget.utils.LafConstants.TabOverviewKind
import org.jvnet.flamingo.svg.SvgBatikResizableIcon
import org.jvnet.flamingo.common.JCommandButton
import org.jvnet.flamingo.common.JCommandMenuButton
import org.jvnet.flamingo.common.JCommandButtonStrip
import org.jvnet.flamingo.common.CommandButtonDisplayState
import org.jvnet.flamingo.common.popup.PopupPanelCallback
import org.jvnet.flamingo.common.popup.JCommandPopupMenu
import org.jdesktop.swingx.JXHyperlink
import org.jdesktop.swingx.JXStatusBar
import org.jdesktop.swingx.JXStatusBar.Constraint
import org.jdesktop.swingx.decorator.PatternFilter
import org.jdesktop.swingx.decorator.HighlighterFactory
import org.jdesktop.swingx.treetable.AbstractTreeTableModel
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.XMPPException
import javax.swing.filechooser.FileFilter
import java.io.File
import javax.swing.DefaultListCellRenderer
import javax.swing.JList
import javax.swing.DefaultListModel
import org.netbeans.spi.wizard.WizardPage
import org.netbeans.spi.wizard.Wizard
import org.netbeans.spi.wizard.WizardBranchController
import org.netbeans.spi.wizard.WizardException
import org.netbeans.spi.wizard.WizardPage.WizardResultProducer
import javax.jcr.ItemNotFoundException
import javax.jcr.PropertyType
import javax.jcr.SimpleCredentials
import javax.jcr.observation.*
import javax.jcr.query.Query
import org.apache.jackrabbit.core.TransientRepository
import org.apache.jackrabbit.util.Text
import net.miginfocom.swing.MigLayout
import org.mnode.base.desktop.AbstractTreeModel
import javax.swing.tree.TreePath
import org.apache.jackrabbit.core.config.RepositoryConfig
import javax.swing.tree.DefaultTreeCellRenderer
import javax.swing.Icon
import javax.swing.JTree
import javax.swing.JOptionPane
import javax.swing.ListModel
import javax.swing.ComboBoxModel
import javax.swing.DefaultListCellRenderer
import javax.swing.event.ListDataListener
import org.apache.log4j.Logger
import com.ocpsoft.pretty.time.PrettyTime
import javax.swing.table.DefaultTableModel
import javax.swing.ListSelectionModel
import org.mnode.base.desktop.PaddedIcon
import org.jivesoftware.smackx.packet.VCard
import javax.swing.ImageIcon
import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.SASLAuthentication
import groovy.util.XmlSlurper
import org.jdesktop.swingx.JXErrorPane
import org.jdesktop.swingx.error.ErrorInfo
import javax.swing.RowFilter
import javax.swing.SortOrder
import org.jvnet.flamingo.common.icon.EmptyResizableIcon
import javax.swing.text.html.StyleSheet
import java.awt.event.ActionListener
import org.mnode.base.desktop.JTextFieldExt
import org.mnode.base.desktop.HyperlinkListenerImpl
import java.awt.event.InputEvent
import javax.swing.KeyStroke
import org.eclipse.mylyn.wikitext.core.WikiText
import org.eclipse.mylyn.wikitext.core.parser.MarkupParser
import org.eclipse.mylyn.wikitext.mediawiki.core.MediaWikiLanguage
import org.eclipse.mylyn.wikitext.confluence.core.ConfluenceLanguage
import org.eclipse.mylyn.wikitext.textile.core.TextileLanguage
import org.fife.ui.rsyntaxtextarea.SyntaxConstants
import org.jdesktop.swingx.treetable.DefaultTreeTableModel
import javax.swing.Action
import java.net.URI
import org.jdesktop.swingx.table.ColumnFactory
import javax.mail.Session
import java.util.Properties
import javax.mail.Store
import javax.mail.URLName
import javax.naming.InitialContext
import org.apache.jackrabbit.core.jndi.RegistryHelper
import javax.mail.Authenticator
import javax.mail.PasswordAuthentication
//import org.jvnet.flamingo.ribbon.JRibbonFrame
//import griffon.builder.flamingo.FlamingoBuilder
import org.jvnet.flamingo.common.JCommandButton
import org.jvnet.flamingo.common.JCommandButtonPanel
import com.sun.syndication.io.SyndFeedInput
import com.sun.syndication.io.XmlReader
//import ch.bluepenguin.jcr.groovy.builder.JcrBuilder
import org.mnode.base.log.LogEntry
import org.mnode.base.log.LogEntry.Level
import org.mnode.base.log.FormattedLogEntry
import org.mnode.base.log.LogAdapter
import org.mnode.base.log.adapter.Slf4jAdapter
import org.slf4j.LoggerFactory
import org.mnode.base.substance.TabCloseCallbackImpl
import org.mnode.base.substance.VetoableMultipleTabCloseListenerImpl
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.swing.event.HyperlinkListener
import javax.swing.event.HyperlinkEvent
import groovyx.gpars.Asynchronizer
import javax.swing.JTable
import javax.swing.table.DefaultTableCellRenderer
import javax.imageio.ImageIO
import ca.odell.glazedlists.swing.EventListModel
import ca.odell.glazedlists.swing.EventTableModel
import ca.odell.glazedlists.BasicEventList
import ca.odell.glazedlists.SortedList
import ca.odell.glazedlists.RangeList
import ca.odell.glazedlists.GlazedLists
import ca.odell.glazedlists.gui.AdvancedTableFormat
import ca.odell.glazedlists.swing.EventListJXTableSorting
import org.fife.ui.rtextarea.RTextScrollPane
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import javax.jcr.RepositoryException
import javax.jcr.query.qom.QueryObjectModelConstants
import org.mnode.base.log.adapter.JclAdapter
import org.apache.commons.logging.LogFactory
import org.mnode.coucou.qom.QueryObjectModelBuilder
import javax.mail.Folder
import javax.mail.MessagingException

/**
 * @author fortuna
 *
 */
 /*
@Grapes([
//      @Grab(group='net.java.dev.glazedlists', module='glazedlists_java15', version='1.8.0'),
      @Grab(group='org.codehaus.gpars', module='gpars', version='0.9'),
//    @Grab(group='com.sun.phobos', module='tagsoup', version='1.2'),
//    @Grab(group='ch.bluepenguin.groovy', module='ocmgroovy', version='0.1-SNAPSHOT'),
//    @Grab(group='rome', module='rome', version='1.0'),
//    @Grab(group='rome', module='rome-fetcher', version='1.0'),
//    @Grab(group='jdom', module='jdom', version='1.0'),
    @Grab(group='org.mnode.coucou', module='coucou', version='0.0.1-SNAPSHOT', transitive=false),
//    @Grab(group='org.codehaus.griffon.swingxbuilder', module='swingxbuilder', version='0.1.6'),
//    @Grab(group='net.java.dev.substance', module='substance', version='5.3'),
//    @Grab(group='net.java.dev.substance', module='substance-swingx', version='5.3'),
//    @Grab(group='net.java.dev.substance', module='swingx', version='5.3'),
//    @Grab(group='swingx', module='swingx-beaninfo', version='0.9.5'),
    //@Grab(group='org.swinglabs', module='swingx', version='0.9.2'),
    @Grab(group='org.mnode.base', module='base-commons', version='0.0.1-SNAPSHOT'),
    @Grab(group='org.mnode.base', module='base-log', version='0.0.1-SNAPSHOT'),
    @Grab(group='org.mnode.base', module='base-xmpp', version='0.0.1-SNAPSHOT'),
    @Grab(group='org.mnode.base', module='base-desktop', version='0.0.1-SNAPSHOT'),
    @Grab(group='org.mnode.base', module='base-substance', version='0.0.1-SNAPSHOT'),
    @Grab(group='org.mnode.base', module='base-feed', version='0.0.1-SNAPSHOT'),
    @Grab(group='org.mnode.base', module='base-wiki', version='0.0.1-SNAPSHOT'),
    //@Grab(group='jgoodies', module='forms', version='1.0.5'),
    //@Grab(group='org.codehaus.griffon.flamingobuilder', module='flamingobuilder', version='0.2'),
    @Grab(group='net.java.dev.flamingo', module='flamingo', version='4.2'),
//    @Grab(group='org.apache.xmlgraphics', module='batik-awt-util', version='1.7'),
//    @Grab(group='org.apache.xmlgraphics', module='batik-swing', version='1.7'),
//    @Grab(group='org.apache.xmlgraphics', module='batik-transcoder', version='1.7'),
//    @Grab(group='net.java.dev.datatips', module='datatips', version='20091219'),
//    @Grab(group='org.netbeans.wizard', module='wizard', version='0.998.1'),
    @Grab(group='javax.jcr', module='jcr', version='2.0'),
    //@Grab(group='org.apache.commons', module='commons-compress', version='1.0'),
    @Grab(group='org.apache.jackrabbit', module='jackrabbit-core', version='2.0.0'),
    //@Grab(group='org.apache.jackrabbit', module='jackrabbit-text-extractors', version='2.0.0'),
    @Grab(group='org.slf4j', module='slf4j-log4j12', version='1.5.8'),
    @Grab(group='net.fortuna.ical4j', module='ical4j-connector', version='0.9'),
    @Grab(group='net.fortuna.mstor', module='mstor', version='0.9.12'),
//    @Grab(group='com.miglayout', module='miglayout', version='3.7.2'),
//    @Grab(group='com.ocpsoft', module='ocpsoft-pretty-time', version='1.0.5'),
    @Grab(group='com.fifesoft.rsyntaxtextarea', module='rsyntaxtextarea', version='1.4.0')])
    */
public class Coucou{
     
    static final LogAdapter log = new Slf4jAdapter(LoggerFactory.getLogger(Coucou))
    static final LogEntry init_node = new FormattedLogEntry(Level.Info, 'Initialising %s node..')
    static final LogEntry delete_enabled = new FormattedLogEntry(Level.Info, 'Enable deletion for: %s')
    static final LogEntry delete_disabled = new FormattedLogEntry(Level.Info, 'Disable deletion')
    static final LogEntry unexpected_error = new FormattedLogEntry(Level.Error, 'An unexpected error has occurred')
    
//    static final def EMPTY_TABLE_MODEL = new DefaultTableModel()
     
    static void close(def frame, def exit) {
        if (exit) {
            if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(frame, "Exit Coucou?", 'Confirm shutdown', JOptionPane.OK_CANCEL_OPTION)) {
                System.exit(0)
            }
        }
        else {
            frame.visible = false
        }
    }
    
    static void main(def args) {
//         System.setProperty("java.net.useSystemProxies", "true");
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Coucou");
        
         UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0))
         UIManager.put(org.jvnet.lafwidget.LafWidget.ANIMATION_KIND, org.jvnet.lafwidget.utils.LafConstants.AnimationKind.FAST.derive(2))
         //UIManager.put(org.jvnet.lafwidget.LafWidget.TABBED_PANE_PREVIEW_PAINTER, new DefaultTabPreviewPainter())
         LookAndFeelHelper.instance.addLookAndFeelAlias('substance5', 'org.jvnet.substance.skin.SubstanceNebulaLookAndFeel')
//         LookAndFeelHelper.instance.addLookAndFeelAlias('seaglass', 'com.seaglasslookandfeel.SeaGlassLookAndFeel')
         
        ColumnFactory.instance = new RowLimitedColumnFactory(100)
        
        //System.setProperty("org.apache.jackrabbit.repository.home", new File(System.getProperty("user.home"), ".coucou/data").absolutePath)
        //System.setProperty("org.apache.jackrabbit.repository.conf", Coucou.class.getResource("/config.xml").file)
        
        def repoConfig = RepositoryConfig.create(Coucou.getResource("/config.xml").toURI(), new File(System.getProperty("user.home"), ".coucou/data").absolutePath)
        def repository = new TransientRepository(repoConfig)
        
//        def context = new InitialContext()
//        RegistryHelper.registerRepository(context, 'coucou', Coucou.getResource("/config.xml").file, new File(System.getProperty("user.home"), ".coucou/data").absolutePath, false)
//        def repository = context.lookup('coucou')
        
        def session = repository.login(new SimpleCredentials('admin', ''.toCharArray()))
//        Runtime.getRuntime().addShutdownHook(new SessionLogout(session))
        Runtime.getRuntime().addShutdownHook({
            session.logout()
//            RegistryHelper.unregisterRepository(context, 'coucou')
        })
        
        def mailSessionProps = new Properties()
        mailSessionProps.setProperty('mstor.repository.name', 'coucou')
        mailSessionProps.setProperty('mstor.repository.path', 'History')
        mailSessionProps.setProperty('mstor.repository.create', 'true')
        mailSessionProps.setProperty('mail.store.protocol', 'mstor')
        
        Session mailSession = Session.getInstance(mailSessionProps, {new PasswordAuthentication('mail', '')} as Authenticator)
        
//        def jcr = new JcrBuilder()
//        jcr.session = session
//        jcr.log = log

        def filterableLists = []
        
        def styleSheet = new StyleSheet()
        styleSheet.addRule("body {background-color:#ffffff; color:#444b56; font-family:verdana,sans-serif; margin:8px; }")
//        styleSheet.addRule("a {text-decoration:underline; color:blue; }")
//                            styleSheet.addRule("a:hover {text-decoration:underline; }")
//        styleSheet.addRule("img {border-width:0; }")
        
        def defaultEditorKit = new HTMLEditorKitExt(styleSheet: styleSheet)
        
        def initNode = { name, type = 'nt:unstructured' ->
            if (!session.rootNode.hasNode(name)) {
                log.log init_node, name
                session.rootNode.addNode(name, type)
                session.rootNode.save()
            }
        }
        
        initNode('contacts') //, 'nt:folder')
        initNode('planner') //, 'nt:folder')
        initNode('history') //, 'nt:folder')
//        initNode('archive')
//        initNode('presence')
        initNode('notes') //, 'nt:folder')
        initNode('journal') //, 'nt:folder')
        initNode('accounts') //, 'nt:folder')
        
        def getNode = { path, referenceable = false ->
            if (!session.nodeExists(path)) {
                log.log init_node, path
                def node = session.rootNode.addNode(path[1..-1])
                if (referenceable) {
                    node.addMixin('mix:referenceable')
                }
            }
            return session.getNode(path)
        }
        
        // save a node hierarchy irregardless of it being new..
        def saveNode = { node ->
            def parent = node.parent
            while (parent.isNew()) {
                parent = parent.parent
            }
            parent.save()
        }
        
        def removeNode = { node ->
            def parent = node.parent
            node.remove()
            parent.save()
        }
        
//        def editContext = new EditContext()
        def editContext = [:] as ObservableMap
        
        def compareByDate = { b, a ->
            def aDate
            if (a.hasProperty('date')) {
                aDate = a.getProperty('date')
            }
            else if (a.hasProperty('lastModified')) {
                aDate = a.getProperty('lastModified')
            }
            else if (a.hasProperty('lastUpdated')) {
                aDate = a.getProperty('lastUpdated')
            }
            def bDate
            if (b.hasProperty('date')) {
                bDate = b.getProperty('date')
            }
            else if (b.hasProperty('lastModified')) {
                bDate = b.getProperty('lastModified')
            }
            else if (b.hasProperty('lastUpdated')) {
                bDate = b.getProperty('lastUpdated')
            }
            aDate.date.time <=> bDate.date.time
        }
        
        def activityList = new BasicEventList()
        
        def swing = new SwingXBuilder()
//        swing.registerBeanFactory('frame', JRibbonFrame.class)
//        def flamingo = new FlamingoBuilder()

         def newAccountTab = { account ->
                 
             if (!account) {
                 account = new XmppAccount();
             }
             
             swing.panel(name: account.name) {
                 borderLayout()
                 panel() {
                     label(text: 'Service Name')
                     textField()
                     label(text: 'Username')
                     textField()
                     label(text: 'Password')
                     passwordField()
                 }
                 hbox(constraints: BorderLayout.SOUTH) {
                     hglue()
                     button(text: 'Connect', id: 'connectButton')
                     connectButton.actionPerformed = { account.connect() }
                 }
             }
         }

         def newContactTab = { contact ->
                 
             if (!contact) {
                 contact = new Contact();
             }
             
             swing.panel(name: contact.name) {
                 borderLayout()
                 hbox(constraints: BorderLayout.SOUTH) {
                     hglue()
                     button(text: 'Close')
                 }
             }
         }

        def getTabForNode = { tabs, node ->
            def tab
            if (tabs.tabCount > 0) {
                for (i in 0..tabs.tabCount - 1) {
                    if (tabs.getComponentAt(i).getClientProperty('coucou.node')?.path == node.path) {
                        tab = tabs.getComponentAt(i)
                    }
                }
            }
            return tab
        }
        
        def openNodeTab = { tabs, node ->
            def tab = getTabForNode(tabs, node)
            if (tab) {
                tabs.selectedComponent = tab
                return
            }
            
            swing.edt {
                def newTab = panel(name: node.name, border: emptyBorder(10)) {
                    borderLayout()
                    if (node.hasNodes()) {
                        splitPane(continuousLayout: true) {
                            scrollPane(constraints: 'left') {
                                list(id: 'entryList')
                                entryList.model = new RepositoryListModel(node, swing: swing)
                                entryList.cellRenderer = new RepositoryListCellRenderer()
                            }
                            scrollPane(constraints: 'right') {
                            }
                        }
                    }
                }
                newTab.putClientProperty(SubstanceLookAndFeel.TABBED_PANE_CLOSE_BUTTONS_PROPERTY, true)
                newTab.putClientProperty('coucou.node', node)
//                doOutside {
                tabs.selectedIndex = tabs.tabCount - 1
//                }
                tabs.add newTab
//                tabs.selectedComponent = newTab
//                doLater {
//                    tabs.actionMap.get('navigateNext').actionPerformed(new ActionEvent(tabs, ActionEvent.ACTION_PERFORMED, ''))
//                    def tabComponent = tabs.getTabComponentAt(tabs.tabCount - 1)
//                    tabComponent.scrollRectToVisible(new Rectangle(0, 0, tabComponent.width, tabComponent.height))
//                }
            }
        }
        
        def closeCurrentTab = { tabs ->
            if (tabs.selectedIndex > 0) {
                tabs.removeTabAt tabs.selectedIndex
            }
        }
        
        def closeOtherTabs = { tabs ->
            if (tabs.tabCount > 1) {
                for (index in (tabs.tabCount - 1)..1) {
                    if (index != tabs.selectedIndex) {
                        tabs.removeTabAt index
                    }
                }
            }
        }
        
        def closeAllTabs = { tabs ->
            if (tabs.tabCount > 1) {
                for (index in (tabs.tabCount - 1)..1) {
                    tabs.removeTabAt index
                }
            }
        }
        
        def openExplorerTab = { tabs, node ->
            if (tabs.tabCount > 0) {
                for (i in 0..tabs.tabCount - 1) {
                    if (tabs.getComponentAt(i).getClientProperty('coucou.node')?.path == node.path) {
                        tabs.selectedComponent = tabs.getComponentAt(i)
                        return
                    }
                }
            }
            
            swing.edt {
                def explorerTab = new ExplorerView(node, coucouFrame, editContext)
            
                explorerTab.putClientProperty(SubstanceLookAndFeel.TABBED_PANE_CLOSE_BUTTONS_PROPERTY, true)
                explorerTab.putClientProperty('coucou.node', node)
                tabs.add explorerTab
                tabs.selectedComponent = explorerTab
                
                def iconSize = new Dimension(16, 16)
                def taskIcon = SvgBatikResizableIcon.getSvgIcon(Coucou.getResource('/icons/task.svg'), iconSize)
                tabs.setIconAt(tabs.indexOfComponent(explorerTab), taskIcon)
            }
        }
        
        def markNodeRead = { node, read ->
            // e.g. feed
            if (node.hasNodes()) {
                for (child in node.nodes) {
                    child.setProperty('seen', read)
                }
            }
            // e.g. feed entry
            else {
                node.setProperty('seen', read)
            }
            node.save()
        }
        
        def openFeedView = { tabs, node, selectedItem = null ->
            def feedView = getTabForNode(tabs, node)
            if (feedView) {
                tabs.selectedComponent = feedView
                if (selectedItem) {
                    def childNodes = node.nodes
                    while (childNodes.hasNext()) {
                        if (selectedItem.isSame(childNodes.nextNode())) {
                            def entryList = feedView.getClientProperty('entryList')
                            def row = entryList.convertRowIndexToView(childNodes.position - 1 as Integer)
                            swing.edt {
                                entryList.selectionModel.setSelectionInterval(row, row)
                                entryList.scrollRectToVisible(entryList.getCellRect(row, 0, true))
                            }
                            break
                        }
                    }
                }
            }
            else {
              swing.edt {
                coucouFrame.cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
                
                def resultList = new BasicEventList()
                resultList.readWriteLock.readLock().lock()
                try {
                    feedView = new FeedView(node, resultList, editContext, defaultEditorKit)
                    feedView.markNodeRead = markNodeRead
                } finally {
                    resultList.readWriteLock.readLock().unlock()
                }
                feedView.putClientProperty(SubstanceLookAndFeel.TABBED_PANE_CLOSE_BUTTONS_PROPERTY, true)
                feedView.putClientProperty('coucou.node', node)
//                feedView.putClientProperty('entryList', entryList)
                tabs.add feedView
                tabs.selectedComponent = feedView
                
                def iconSize = new Dimension(16, 16)
                def feedIcon = SvgBatikResizableIcon.getSvgIcon(Coucou.getResource('/icons/feed.svg'), iconSize)
                tabs.setIconAt(tabs.indexOfComponent(feedView), feedIcon)
                
                def entryList = feedView.getClientProperty('entryList')
                boolean listPacked
                doOutside {
                    def selectedRow
//                    def childNodes = node.nodes
                    resultList.readWriteLock.writeLock().lock()
                    try {
                        int count = 0
                        for (entryNode in node.nodes) {
//                    while (childNodes.hasNext()) {
//                        def entryNode = childNodes.nextNode()
                            resultList.add(entryNode)
                            
                            if (count++ == 100) {
                                doLater {
                                    entryList.packAll()
                                    listPacked = true
                                }
                            }
/*                        
                        if (selectedItem?.isSame(entryNode)) {
                            selectedRow = entryList.convertRowIndexToView(childNodes.position - 1 as Integer)
                            entryList.selectionModel.setSelectionInterval(row, row)
                            entryList.scrollRectToVisible(entryList.getCellRect(row, 0, true))
                        }
*/
                        }
                    } finally {
                        resultList.readWriteLock.writeLock().unlock()
                    }
                    
                    doLater {
                        if (!listPacked) {
                            entryList.packAll()
                        }
                        if (selectedItem) {
                                def childNodes = node.nodes
                                while (childNodes.hasNext()) {
                                    if (selectedItem.isSame(childNodes.nextNode())) {
                                        def row = entryList.convertRowIndexToView(childNodes.position - 1 as Integer)
                                        edt {
                                            entryList.selectionModel.setSelectionInterval(row, row)
                                            entryList.scrollRectToVisible(entryList.getCellRect(row, 0, true))
                                        }
                                        break
                                    }
                                }
                        }
                        coucouFrame.cursor = Cursor.defaultCursor
                    }
                }
              }
            }
/*
            swing.doLater {
                        if (selectedItem) {
                                def childNodes = node.nodes
                                while (childNodes.hasNext()) {
                                    if (selectedItem.isSame(childNodes.nextNode())) {
                                        def entryList = feedView.getClientProperty('entryList')
                                        def row = entryList.convertRowIndexToView(childNodes.position - 1 as Integer)
                                        entryList.selectionModel.setSelectionInterval(row, row)
                                        entryList.scrollRectToVisible(entryList.getCellRect(row, 0, true))
                                        break
                                    }
                                }
                        }
//                coucouFrame.cursor = Cursor.defaultCursor
            }
*/
        }
        
        def updateProperty = { aNode, propertyName, value ->
            if (value && (!aNode.hasProperty(propertyName) || aNode.getProperty(propertyName).string != value)) {
                aNode.setProperty(propertyName, value)
            }
        }
        
        def updateFeed = { url ->
            // rome uses Thread.contextClassLoader..
            Thread.currentThread().contextClassLoader = Coucou.classLoader
            
            def feed = new SyndFeedInput().build(new XmlReader(new URL(url)))
//                                             def newNode = session.rootNode.getNode('feeds').addNode(Text.escapeIllegalJcrChars(newFeed.title))
            def feedNode = getNode("/feeds/${Text.escapeIllegalJcrChars(feed.title)}", true)
            updateProperty(feedNode, 'url', url)
            updateProperty(feedNode, 'title', feed?.title)
//            else {
//                feedNode.setProperty('title', feed.title)
//            }
            if (feed?.link) {
                updateProperty(feedNode, 'source', feed.link)
            }
            else if (!feed.links?.isEmpty()) {
                updateProperty(feedNode, 'source', feed.links[0])
            }
//            if (feed.uri) {
//                feedNode.setProperty('uri', feed.uri)
//            }

            def now = Calendar.instance
            
            for (entry in feed.entries) {
                def entryNode
                if (entry.uri) {
                    entryNode = getNode("${feedNode.path}/${Text.escapeIllegalJcrChars(entry.uri)}")
                }
                else if (entry.link) {
                    entryNode = getNode("${feedNode.path}/${Text.escapeIllegalJcrChars(entry.link)}")
                }
                else {
                    entryNode = getNode("${feedNode.path}/${Text.escapeIllegalJcrChars(entry.title)}")
                }
                updateProperty(entryNode, 'title', entry?.title)
                if (entry?.description) {
                    updateProperty(entryNode, 'description', entry.description.value)
                }
                else if (entry.contents && !entry.contents.isEmpty()) {
                    updateProperty(entryNode, 'description', entry.contents[0].value)
                }
                
                // entry link..
                if (entry?.uri) {
                    try {
                        new URL(entry.uri)
                        updateProperty(entryNode, 'link', entry.uri)
                    }
                    catch (Exception e) {
                        // not a valid url..
                    }
                }
                if (entry?.link) {
                    try {
                        new URL(entry.link)
                        updateProperty(entryNode, 'link', entry.link)
                    }
                    catch (Exception e) {
                        // not a valid url..
                    }
                }
                
                // XXX: properties below not being updated..
                
                if (entryNode.isNew()) {
                    if (!feedNode.hasProperty('date') || feedNode.getProperty('date')?.date.time.before(now.time)) {
                        feedNode.setProperty('date', now)
                    }
                    entryNode.setProperty('seen', false)
                    entryNode.setProperty('source', feedNode)
//                    entryNode.setProperty('date', now)
                }
                
                // XXX: future published dates are ignored..
                if (entry.publishedDate && (!entryNode.hasProperty('date') || entryNode.getProperty('date')?.date.time.after(entry.publishedDate.time))) {
                    def publishedDate = Calendar.instance
                    publishedDate.time = entry.publishedDate
                    entryNode.setProperty('date', publishedDate)
                }
                else if (entryNode.isNew()) {
                    entryNode.setProperty('date', now)
                }
            }
            saveNode feedNode
            return feedNode
        }
        
        def addFeed = { url ->
            swing.doOutside {
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
                             JOptionPane.showMessageDialog(coucouFrame, "Invalid URL: ${url}")
                         }
                     }
                 }
                 if (feedUrl) {
                     try {
                         feedNode = updateFeed(url)
                     }
                     catch (Exception e) {
                          try {
                              html = new XmlSlurper(new org.ccil.cowan.tagsoup.Parser()).parse(feedUrl.content)
                              def feeds = html.head.link.findAll { it.@type == 'application/rss+xml' || it.@type == 'application/atom+xml' }
                              println "Found ${feeds.size()} feeds: ${feeds.collect { it.@href.text() } }"
                              if (!feeds.isEmpty()) {
                                  feedNode = updateFeed(new URL(feedUrl, feeds[0].@href.text()).toString())
                              }
                              else {
                                  doLater {
                                      JOptionPane.showMessageDialog(coucouFrame, "No feeds found for site: ${url}")
                                  }
                              }
                          }
                          catch (Exception e2) {
                              doLater {
                                  JXErrorPane.showDialog(e2);
//                                  JOptionPane.showMessageDialog(coucouFrame, "Error analysing site: ${url}")
                              }
                          }
                     }
                 }
                 doLater {
                     if (feedNode) {
//                         feedList.model.fireTableDataChanged()
                         openFeedView(tabs, feedNode)
                     }
                 }
            }
            swing.edt {
                filterField.text = null
                for (list in filterableLists) {
                    list.rowFilter = null
                }
            }
        }
        
        def openSearchView = { tabs, searchTerms ->
            swing.edt {
                coucouFrame.cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
                
                    def resultList = new BasicEventList()

                    searchView = new SearchView(searchTerms, resultList, compareByDate)
                    searchView.showItem = { node -> openFeedView(tabs, node.parent, node)}
                    searchView.putClientProperty(SubstanceLookAndFeel.TABBED_PANE_CLOSE_BUTTONS_PROPERTY, true)
                    searchView.putClientProperty('coucou.query', searchTerms)
                    tabs.add searchView
                    tabs.selectedComponent = searchView
                
                    def iconSize = new Dimension(16, 16)
                    def searchIcon = SvgBatikResizableIcon.getSvgIcon(Coucou.getResource('/icons/search.svg'), iconSize)
                    tabs.setIconAt(tabs.indexOfComponent(searchView), searchIcon)

              doOutside {
//                Query q = session.workspace.queryManager.createQuery("select * from [nt:unstructured] as all_nodes where contains(all_nodes.*, '${searchTerms}')", Query.JCR_SQL2)
                QueryObjectModelBuilder queryBuilder = new QueryObjectModelBuilder(session.workspace.queryManager, session.valueFactory)
                Query q = queryBuilder.with {
                        query(source: selector(nodeType: 'nt:unstructured', name: 'all_nodes'),
                                constraint: fullTextSearch(selectorName: 'all_nodes', searchTerms: "${searchTerms}"))}
                
                def nodes = q.execute().nodes
//                println "Found ${nodes.size} matching nodes: ${nodes.collect { it.path }}"

                doLater {
                    for (node in nodes) {
//                    searchModel.addElement node
                        resultList.add(node)
                    }
                    coucouFrame.cursor = Cursor.defaultCursor
                }
              }
            }
        }
        
        def editNote = { parent = getNode('/notes'), nodeName = null ->
            def noteNode
            def title = 'Untitled Note'
            def format = 'Plain Text'
            if (nodeName) {
                noteNode = parent.getNode(nodeName)
                if (noteNode.hasProperty('title')) {
                    title = noteNode.getProperty('title').string
                }
                if (noteNode.hasProperty('markupLanguage')) {
                    format = noteNode.getProperty('markupLanguage').string
                }
                else if ('text/html' == noteNode.getProperty('contentType').string) {
                    format = 'HTML'
                }
            }
            swing.edt {
                frame(title: title, size: [430, 400], show: true, locationRelativeTo: coucouFrame, id: 'noteEditorFrame') {
                    actions() {
                        action(id: 'saveNoteAction', name: 'Save', accelerator: shortcut('S'), closure: {
//                                title = noteEditor.viewport.view.text.find(/(?m)\A.*$/).replaceAll(/^[\s]+|[\s]+$/, '')
                                if (titleField.text) {
                                    if (!noteNode) {
                                        noteNode = parent.addNode(Text.escapeIllegalJcrChars(titleField.text))
                                    }
                                    noteNode.setProperty('title', titleField.text)
                                    noteNode.setProperty('content', noteEditor.viewport.view.text)
                                    noteNode.setProperty('tags', tagsField.text)
                                    noteNode.setProperty('lastModified', Calendar.instance)
                                    if (formatCombo.selectedIndex == 0) {
                                        noteNode.setProperty('contentType', 'text/plain')
                                    }
                                    else {
                                        noteNode.setProperty('contentType', 'text/html')
                                        if (formatCombo.selectedIndex > 1) {
                                            noteNode.setProperty('markupLanguage', formatCombo.selectedItem)
                                        }
                                    }
                                    saveNode parent
                                    noteEditorFrame.dispose()
//                                    noteList.model.fireTableDataChanged()
                                }
                                else {
                                    JOptionPane.showMessageDialog(noteEditorFrame, "No title specified")
                                }
                        })
                        action(id: 'cancelEditAction', name: 'Cancel', accelerator: 'ESCAPE', closure: {noteEditorFrame.dispose()})
                    }
                    menuBar() {
                        menu(text: "File", mnemonic: 'F') {
                            menuItem(saveNoteAction)
                            menuItem(cancelEditAction)
                        }
                        menu(text: "View", mnemonic: 'V') {
                            checkBoxMenuItem(text: "Word Wrap", id: 'viewWordWrap')
                        }
                    }

                    panel(border: emptyBorder(10)) {
                        borderLayout()
                        RSyntaxTextArea editor = new RSyntaxTextArea()
                        editor.addHyperlinkListener(new HyperlinkListenerImpl())
                        if ('HTML' == format) {
                            editor.syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_HTML
                        }
                        hbox(border: emptyBorder([0, 0, 10, 0]), constraints: BorderLayout.NORTH) {
                            label(text: 'Title')
                            hstrut(5)
                            textField(text: title, id: 'titleField')
                            titleField.focusGained = {
                                titleField.selectAll()
                            }
                            hstrut(15)
                            label(text: 'Format')
                            hstrut(5)
                            def textFormats = ['Plain Text', 'HTML', 'Confluence', 'MediaWiki', 'Textile']
                            comboBox(model: new DefaultComboBoxModel(textFormats as Object[]), id: 'formatCombo', selectedItem: format)
                            formatCombo.itemStateChanged = {
                                if ('HTML' == formatCombo.selectedItem) {
                                    editor.syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_HTML
                                }
                                else {
                                    editor.syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_NONE
                                }
                            }
                            hglue()
                        }
//                                    scrollPane {
//                                        textArea(lineWrap: bind {viewWordWrap.selected}, wrapStyleWord: true)
//                                    }
//                                    editor.lineWrap = bind {viewWordWrap.selected}
                        panel {
                            borderLayout()
                            
                            bind(source: viewWordWrap, sourceProperty: 'selected', target: editor, targetProperty: 'lineWrap')
                            editor.wrapStyleWord = true
                            scrollPane(new RTextScrollPane(editor), id: 'noteEditor')
                            
                            vbox(constraints: BorderLayout.SOUTH, border: emptyBorder([10, 0, 0, 0])) {
                                titledSeparator(title: 'Tags')
                                vstrut(3)
                                scrollPane {
                                    textArea(rows: 3, lineWrap: true, wrapStyleWord: true, id: 'tagsField')
                                }
                            }
                            if (noteNode) {
                                editor.text = noteNode.getProperty('content').string
                                if (noteNode.hasProperty('tags')) {
                                    tagsField.text = noteNode.getProperty('tags').string
                                }
                            }
                        }
//                                    noteEditor.gutter.bookmarkingEnabled = true
//                                    panel(border: emptyBorder([5, 0, 0, 0]), constraints: BorderLayout.SOUTH) {
//                                        flowLayout(alignment: FlowLayout.TRAILING, hgap: 5)
                        hbox(border: emptyBorder([10, 0, 0, 0]), constraints: BorderLayout.SOUTH) {
                            button(text: 'Preview..')
                            hglue()
                            button(action: saveNoteAction)
                            hstrut(5)
                            button(action: cancelEditAction)
                        }
                    }
                }
                titleField.requestFocus()
            }
        }

        def openNoteView = { tabs, node ->
            def tab = getTabForNode(tabs, node)
            if (tab) {
                tabs.selectedComponent = tab
                return
            }
            
            swing.edt {
                def noteView = new NoteView(node, defaultEditorKit)
                noteView.editNote = editNote
                noteView.putClientProperty(SubstanceLookAndFeel.TABBED_PANE_CLOSE_BUTTONS_PROPERTY, true)
                noteView.putClientProperty('coucou.node', node)
                tabs.add noteView
                tabs.selectedComponent = noteView
                
                def iconSize = new Dimension(16, 16)
                def noteIcon = SvgBatikResizableIcon.getSvgIcon(Coucou.getResource('/icons/attachment.svg'), iconSize)
                tabs.setIconAt(tabs.indexOfComponent(noteView), noteIcon)
            }
        }
        
        def editJournalEntry = { parent = getNode('/journal'), nodeName = null ->
            def entryNode
            def subject = 'Untitled Journal Entry'
            if (nodeName) {
                entryNode = parent.getNode(nodeName)
                if (entryNode.hasProperty('subject')) {
                    subject = entryNode.getProperty('subject').string
                }
            }
            swing.edt {
                frame(title: subject, size: [430, 400], show: true, locationRelativeTo: coucouFrame, id: 'journalEditorFrame') {
                    actions() {
                        action(id: 'saveJournalAction', name: 'Save', accelerator: shortcut('S'), closure: {
                                if (subjectField.text) {
                                    if (!entryNode) {
                                        entryNode = parent.addNode(Text.escapeIllegalJcrChars(subjectField.text))
                                    }
                                    entryNode.setProperty('subject', subjectField.text)
                                    entryNode.setProperty('content', journalEditor.viewport.view.text)
                                    entryNode.setProperty('tags', tagsField.text)
                                    entryNode.setProperty('lastModified', Calendar.instance)
                                    saveNode parent
                                    journalEditorFrame.dispose()
//                                    journalList.model.fireTableDataChanged()
                                }
                                else {
                                    JOptionPane.showMessageDialog(journalEditorFrame, "No subject specified")
                                }
                        })
                        action(id: 'cancelEditAction', name: 'Cancel', accelerator: 'ESCAPE', closure: {journalEditorFrame.dispose()})
                    }
                    menuBar() {
                        menu(text: "File", mnemonic: 'F') {
                            menuItem(saveJournalAction)
                            menuItem(cancelEditAction)
                        }
                        menu(text: "View", mnemonic: 'V') {
                            checkBoxMenuItem(text: "Word Wrap", id: 'viewWordWrap')
                        }
                    }

                    panel(border: emptyBorder(10)) {
                        borderLayout()
                        RSyntaxTextArea editor = new RSyntaxTextArea()
                        editor.addHyperlinkListener(new HyperlinkListenerImpl())
                        vbox(border: emptyBorder([0, 0, 10, 0]), constraints: BorderLayout.NORTH) {
                            hbox {
                                label(text: 'Subject')
                                hstrut(5)
                                textField(text: subject, id: 'subjectField')
                                subjectField.focusGained = {
                                    subjectField.selectAll()
                                }
                                hglue()
                            }
                            vstrut(3)
                            hbox {
                                label(text: 'Date')
                                hstrut(5)
                                datePicker(id: 'dateField')
                                hglue()
                            }
                        }
                        
                        panel {
                            borderLayout()
                            
                            bind(source: viewWordWrap, sourceProperty: 'selected', target: editor, targetProperty: 'lineWrap')
                            editor.wrapStyleWord = true
                            scrollPane(new RTextScrollPane(editor), id: 'journalEditor')
                            
                            vbox(constraints: BorderLayout.SOUTH, border: emptyBorder([10, 0, 0, 0])) {
                                titledSeparator(title: 'Tags')
                                vstrut(3)
                                scrollPane {
                                    textArea(rows: 3, lineWrap: true, wrapStyleWord: true, id: 'tagsField')
                                }
                            }
                            if (entryNode) {
                                editor.text = entryNode.getProperty('content').string
                                tagsField.text = entryNode.getProperty('tags').string
                            }
                        }
                        
                        hbox(border: emptyBorder([10, 0, 0, 0]), constraints: BorderLayout.SOUTH) {
                            button(text: 'Preview..')
                            hglue()
                            button(action: saveJournalAction)
                            hstrut(5)
                            button(action: cancelEditAction)
                        }
                    }
                }
                subjectField.requestFocus()
            }
        }

        def openJournalEntryView = { tabs, node ->
            def tab = getTabForNode(tabs, node)
            if (tab) {
                tabs.selectedComponent = tab
                return
            }
            
            swing.edt {
/*
                def entryView = panel(name: node.getProperty('subject').string, border: emptyBorder(10)) {
                    borderLayout()
                    scrollPane() {
                        contentView = editorPane(editable: false, contentType: 'text/plain', opaque: true, border: null)
                        contentView.text = node.getProperty('content').string
                    }
                    hbox(border: emptyBorder([10, 0, 0, 0]), constraints: BorderLayout.SOUTH) {
                        button(text: 'Revisions..')
                        hglue()
                        button(text: 'Edit', actionPerformed: {editJournalEntry(node.parent, node.name)})
                    }
                }
*/
                def entryView = new JournalView(node)
                entryView.editJournalEntry = editJournalEntry
                entryView.putClientProperty(SubstanceLookAndFeel.TABBED_PANE_CLOSE_BUTTONS_PROPERTY, true)
                entryView.putClientProperty('coucou.node', node)
                tabs.add entryView
                tabs.selectedComponent = entryView
                
                def iconSize = new Dimension(16, 16)
                def noteIcon = SvgBatikResizableIcon.getSvgIcon(Coucou.getResource('/icons/attachment.svg'), iconSize)
                tabs.setIconAt(tabs.indexOfComponent(entryView), noteIcon)
            }
        }

         swing.edt {
             lookAndFeel('substance5', 'system')

//             def helpIcon = SvgBatikResizableIcon.getSvgIcon(Coucou.class.getResource('/icons/im.svg'), new java.awt.Dimension(20, 20))
             
//             imageIcon('/logo.png', id: 'logoIcon')
             
             frame(title: 'Coucou', id: 'coucouFrame', defaultCloseOperation: JFrame.DO_NOTHING_ON_CLOSE,
                     size: [640, 480], show: false, locationRelativeTo: null, iconImage: ImageIO.read(Coucou.getResource('/happy.png'))) {
                
                actions() {
                    action(id: 'busyAction', name: 'Busy', smallIcon: imageIcon('/busy.png'), closure: {})
                    action(id: 'invisibleAction', name: 'Invisible', smallIcon: imageIcon('/invisible.png'), closure: {})
                    action(id: 'workOfflineAction', name: 'Work Offline', smallIcon: imageIcon('/offline.png'), accelerator: shortcut('shift O'), closure: {})
                    
                    action(id: 'newAccountAction', name: 'Create Account..', closure: {
//                        def tab = newAccountTab()
//                        tab.putClientProperty(SubstanceLookAndFeel.TABBED_PANE_CLOSE_BUTTONS_PROPERTY, true)
//                        tabs.add(tab)
//                        tabs.selectedComponent = tab
                        def pageSize = new Dimension(250, 200)
                        def accountTypeSelect = new WizardPageImpl('accountTypeSelect', 'Select Account Type')
                        accountTypeSelect.preferredSize = pageSize
//                        accountTypeSelect.description = 'Select Account Type'
                        accountTypeSelect.validation = {
                            if (!accountButtonGroup.selection) {
                                return 'Please select an account type'
                            }
                            return null
                        }
                        accountTypeSelect.add panel(layout: new MigLayout('fill')) {
                            //gridLayout(cols: 1, rows: 2, vgap: 10)
                            buttonGroup(id: 'accountButtonGroup')
                            radioButton(text: 'XMPP', buttonGroup: accountButtonGroup, name: 'xmppAccount', constraints: 'wrap')
                            radioButton(text: 'Email', buttonGroup: accountButtonGroup, name: 'emailAccount')
                        }

                        def xmppAccountDetails = new WizardPageImpl('xmppAccountDetails', 'Provide XMPP Details')
                        xmppAccountDetails.preferredSize = pageSize
                        xmppAccountDetails.validation = {
                            null
                        }
                        xmppAccountDetails.add panel(layout: new MigLayout('fill')) {
                            //gridLayout(cols: 2, rows: 2, vgap: 10)
                            label(text: 'Username')
                            textField(name: 'usernameField', columns: 15, constraints: 'wrap')
                            label(text: 'Password')
                            passwordField(name: 'passwordField', columns: 15)
                        }

                        def emailAccountDetails = new WizardPageImpl('emailAccountDetails', 'Provide Email Details')
                        emailAccountDetails.preferredSize = pageSize
                        emailAccountDetails.validation = {
                            null
                        }
                        emailAccountDetails.add panel(layout: new MigLayout('fill')) {
                            //gridLayout(cols: 2, rows: 2, vgap: 10)
                            label(text: 'Email Address')
                            textField(name: 'emailAddressField', columns: 15, constraints: 'wrap')
                            label(text: 'Password')
                            passwordField(name: 'passwordField', columns: 15)
                        }

                        def createAccountProducer = new CreateAccountProducer(session.rootNode.getNode('accounts'))
                        def branchController = new WizardBranchControllerImpl(accountTypeSelect)
//                        branchController.keys = ['accountTypeSelect': []
                        branchController.wizards = [
                                'xmppAccount': WizardPage.createWizard("Add XMPP Account", (WizardPage[]) [xmppAccountDetails], createAccountProducer),
                                'emailAccount': WizardPage.createWizard("Add Email Account", (WizardPage[]) [emailAccountDetails], createAccountProducer)]
//                        Wizard wizard = WizardPage.createWizard("Add Account", (WizardPage[]) [accountTypeSelect]);
                        Wizard wizard = branchController.createWizard()
                        wizard.show();
                    })
                    
                    action(id: 'newContactAction', name: 'New Contact..', closure: {
//                        def tab = newContactTab()
//                        tab.putClientProperty(SubstanceLookAndFeel.TABBED_PANE_CLOSE_BUTTONS_PROPERTY, true)
//                        tabs.add(tab)
//                        tabs.selectedComponent = tab
                        def pageSize = new Dimension(250, 200)
                        def contactDetails = new WizardPageImpl('contactDetails', 'Specify contact details')
//                        accountTypeSelect.description = 
                        contactDetails.preferredSize = pageSize
                        contactDetails.add panel(layout: new MigLayout('fill')) {
                            //gridLayout(cols: 2, rows: 2, vgap: 10)
                            label(text: 'First Name')
                            textField(name: 'firstNameField', id: 'firstNameField', columns: 15, constraints: 'wrap')
                            label(text: 'Last Name')
                            textField(name: 'lastNameField', id: 'lastNameField', columns: 15, constraints: 'wrap')
                            label(text: 'Display Name')
                            textField(name: 'displayNameField', id: 'displayNameField', columns: 15)
                            
                            bind(source: firstNameField, sourceProperty: 'text', target: displayNameField, targetProperty: 'text', converter: { it + ' ' + lastNameField.text })
                            bind(source: lastNameField, sourceProperty: 'text', target: displayNameField, targetProperty: 'text', converter: { firstNameField.text + ' ' + it })
                        }
                        contactDetails.validation = {
                            if (!firstNameField.text) {
                                return 'Please specify the contact first name'
                            }
                            if (!lastNameField.text) {
                                return 'Please specify the contact last name'
                            }
                            return null
                        }
                        
                        Wizard wizard = WizardPage.createWizard("Add Contact", (WizardPage[]) [contactDetails], new CreateContactProducer(session.rootNode.getNode('contacts')));
                        wizard.show();
                    })

                    action(id: 'newTaskAction', name: 'Task', accelerator: shortcut('K'), closure: { })
                    action(id: 'newNoteAction', name: 'Note', accelerator: shortcut('N'), closure: { editNote() })
                    action(id: 'newJournalEntryAction', name: 'Journal Entry', accelerator: shortcut('J'), closure: { editJournalEntry() })
                    action(id: 'closeTabAction', name: 'Close Tab', accelerator: shortcut('W'), closure: { closeCurrentTab(tabs) })
                    action(id: 'closeOtherTabsAction', name: 'Close Other Tabs', closure: { closeOtherTabs(tabs) })
                    action(id: 'closeAllTabsAction', name: 'Close All Tabs', accelerator: shortcut('shift W'), closure: { closeAllTabs(tabs) })
                    action(id: 'printAction', name: 'Print', accelerator: shortcut('P'))
                    
                    action(id: 'importAction', name: 'Import..', closure: {
                        dialog(title: 'Import', id: 'importDialog', size: [350, 250], show: true, owner: coucouFrame, modal: true, locationRelativeTo: coucouFrame) {
                            borderLayout()
                            panel() {
//                                gridLayout(1, 1)
                                button(text: 'Import Feeds..', actionPerformed: {
                                    if (chooser.showOpenDialog() == JFileChooser.APPROVE_OPTION) {
                                        importDialog.dispose()
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
                                                        def future = updateFeed.callAsync(feed)
            //                                            future.get()
            //                                            doLater {
            //                                                feedList.model.fireTableDataChanged()
            //                                            }
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
                                                    JXErrorPane.showDialog(coucouFrame, error);
                                                }
                                            }
                                        }
                                    }
                                })
                                button(text: 'Import Email..', actionPerformed: {
                                    if (dirChooser.showOpenDialog() == JFileChooser.APPROVE_OPTION) {
                                        importDialog.dispose()
                                        doOutside {
                                            // load email..
                                            Session importSession = Session.getInstance(new Properties())
                                            Store importStore = importSession.getStore(new URLName("mstor:${dirChooser.selectedFile.absolutePath}"))
                                            importStore.connect()
                                            
                                            for (folder in importStore.defaultFolder.list()) {
                                                folder.open(Folder.READ_ONLY)
                                                try {
                                                    for (message in folder.messages) {
                                                        println message.subject
                                                    }
                                                    def localStore = mailSession.store
                                                    localStore.connect()
                                                    def inbox = localStore.defaultFolder.getFolder('Inbox')
                                                    if (!inbox.exists()) {
                                                        inbox.create(Folder.HOLDS_FOLDERS | Folder.HOLDS_MESSAGES)
                                                    }
                                                    inbox.open(Folder.READ_WRITE)
                                                    inbox.appendMessages(folder.messages)
                                                    inbox.close(false)
                                                    localStore.close()
                                                } catch (MessagingException e) {
                                                    log.log unexpected_error, e
                                                }
                                            }
                                        }
                                    }
                                })
                            }
                        }
                    })
                    
                    action(id: 'exitAction', name: 'Exit', smallIcon: imageIcon('/exit.png'), accelerator: shortcut('Q'), closure: { close(coucouFrame, true) })
                    
//                    action(id: 'deleteAction', name: 'Delete', accelerator: 'DELETE', enabled: bind(source: editContext, sourceProperty: 'delete'), closure: { editContext.delete() })
                    action(id: 'deleteAction', name: 'Delete', accelerator: 'DELETE', enabled: bind { editContext.delete != null }, closure: { editContext.delete() })
                    
                     action(id: 'onlineHelpAction', name: 'Online Help', accelerator: 'F1', closure: { Desktop.desktop.browse(URI.create('http://coucou.im')) })
                     action(id: 'showTipsAction', name: 'Tips', closure: { tips.showDialog(coucouFrame) })
                     action(id: 'aboutAction', name: 'About', closure: {
                         dialog(title: 'About Coucou', size: [350, 250], show: true, owner: coucouFrame, modal: true, locationRelativeTo: coucouFrame) {
                             borderLayout()
                             label(text: 'Coucou 1.0', constraints: BorderLayout.NORTH, border: emptyBorder(10))
                             panel(constraints: BorderLayout.CENTER, border: emptyBorder(10)) {
                                 borderLayout()
                                 scrollPane(horizontalScrollBarPolicy: JScrollPane.HORIZONTAL_SCROLLBAR_NEVER, border: null) {
                                     table(editable: false) {
                                         def systemProps = []
                                         for (propName in System.properties.keySet()) {
                                             systemProps.add([property: propName, value: System.properties.getProperty(propName)])
                                         }
                                         tableModel(list: systemProps) {
                                             propertyColumn(header:'Property', propertyName:'property')
                                             propertyColumn(header:'Value', propertyName:'value')
                                         }
                                     }
                                 }
                             }
                         }
                     })
                     
                    action(id: 'homeAction', name: 'Home', accelerator: KeyStroke.getKeyStroke(KeyEvent.VK_HOME, InputEvent.ALT_DOWN_MASK), closure: { tabs.selectedIndex = 0})
                    
                    action(id: 'markAsReadAction', name: 'Mark as Read', accelerator: 'R', enabled: bind { editContext.markAsRead != null }, closure: { editContext.markAsRead?.call() })
                    action(id: 'markAllReadAction', name: 'Mark All Read', accelerator: shortcut('alt R'), enabled: bind { editContext.markAllRead != null }, closure: { editContext.markAllRead?.call() })
                    action(id: 'markAsUnreadAction', name: 'Mark as Unread', accelerator: 'U', enabled: bind { editContext.markAsUnread != null }, closure: { editContext.markAsUnread?.call() })
                    action(id: 'replyAction', name: 'Reply', accelerator: shortcut('R'))
                    action(id: 'replyAllAction', name: 'Reply All', accelerator: shortcut('shift R'))
                    action(id: 'forwardAction', name: 'Forward', accelerator: shortcut('F'))
                    action(id: 'flagAction', name: 'Flag', accelerator: 'F', enabled: bind { editContext.flag != null }, closure: { editContext.flag?.call() })
                    
                    action(id: 'activityClearAction', name: 'Clear Selected', accelerator: 'C', closure: {
                        def selectedIndex = activity.selectedIndex
                        try {
                            // lock for list modification..
                            activityList.readWriteLock.writeLock().lock()
                            for (selectedValue in activity.selectedValues) {
                                activityList.remove(selectedValue)
                            }
                        }
                        finally {
                            // unlock post-list modification..
                            activityList.readWriteLock.writeLock().unlock()
                        }
                        for (index in selectedIndex..0) {
                            if (index < activity.model.size) {
                                activity.selectedIndex = index
                                break;
                            }
                        }
                    })
                    action(id: 'activityClearAllAction', name: 'Clear All', accelerator: shortcut('alt C'), closure: {
                        try {
                            // lock for list modification..
                            activityList.readWriteLock.writeLock().lock()
                            activityList.clear()
                        }
                        finally {
                            // unlock post-list modification..
                            activityList.readWriteLock.writeLock().unlock()
                        }
                    })
//                    bind(source: coucouFrame, sourceProperty: 'focusOwner', target: activityClearAllAction, targetProperty: 'enabled', converter: { activity.hasFocus() })
                    
                    action(id: 'logoutAction', name: 'Logout', closure: { session.logout() })
                    action(id: 'repositoryExplorerAction', name: 'Repository Explorer', closure: { openExplorerTab(tabs, session.rootNode) })
                    
                    action(id: 'internetSearchAction', name: 'Search The Internet..', closure: { Desktop.getDesktop().browse(URI.create("http://google.com/search?q=${URLEncoder.encode('ben fortuna')}")) })
                }
                
                fileChooser(id: 'chooser')
                fileChooser(id: 'imageChooser', fileFilter: new ImageFileFilter())
                fileChooser(id: 'dirChooser', fileSelectionMode: JFileChooser.FILES_AND_DIRECTORIES)
                
                tipOfTheDay(id: 'tips', model: defaultTipModel(tips: [
                    defaultTip(name: 'test', tip: '<html><em>testing</em>')
                ]))
                
                menuBar() {
                    menu(text: "File", mnemonic: 'F') {
                        menu(text: 'New') {
                            menuItem(text: "Email")
                            menuItem(text: "Appointment")
                            menuItem(text: "Task")
                            menuItem(newNoteAction)
                            menuItem(newJournalEntryAction)
                        }
                        separator()
                        menuItem(closeTabAction)
                        menuItem(closeOtherTabsAction)
                        menuItem(closeAllTabsAction)
                        separator()
                        menuItem(text: "Chat", icon: imageIcon('/chat.png'))
                        menuItem(text: "Call")
                        menuItem(text: "Send File..")
                    separator()
                        menuItem(printAction)
                    separator()
                        menuItem(importAction)
                    separator()
                        menuItem(exitAction)
                    }
                    menu(text: "Edit", mnemonic: 'E') {
                        menu(text: "Status") {
                            checkBoxMenuItem(busyAction, selectedIcon: imageIcon('/busy_selected.png', id: 'busySelectedIcon'),
                                    rolloverIcon: imageIcon('/busy_rollover.png', id: 'busyRolloverIcon'))
                            checkBoxMenuItem(invisibleAction, selectedIcon: imageIcon('/invisible_selected.png', id: 'invisibleSelectedIcon'),
                                    rolloverIcon: imageIcon('/invisible_rollover.png', id: 'invisibleRolloverIcon'))
                            separator()
                            menuItem(text: "Edit Status Message..")
                        }
                        separator()
                        menuItem(deleteAction)
                        menuItem(text: "Rename")
                        separator()
                        menuItem(text: "Mail Filters")
                        menuItem(text: "Accounts")
                        menuItem(text: "Preferences", icon: imageIcon('/preferences.png'))
                    }
                    menu(text: "View", mnemonic: 'V') {
                        checkBoxMenuItem(text: "Presence Bar", id: 'viewPresenceBar', state: false)
                        checkBoxMenuItem(text: "Status Bar", id: 'viewStatusBar')
                        checkBoxMenuItem(text: "Contact Groups", id: 'viewContactGroups')
                    }
                    menu(text: "Go", mnemonic: 'G') {
                        menuItem(homeAction)
                    }
                    menu(text: "Action", mnemonic: 'A') {
                        menuItem(markAsReadAction)
                        menuItem(markAllReadAction)
                        menuItem(markAsUnreadAction)
                        separator()
                        menuItem(replyAction)
                        menuItem(replyAllAction)
                        menuItem(forwardAction)
                    separator()
                    menu(text: "Tag") {
                        menuItem(text: "New Tag..")
                    }
                    separator()
                        menuItem(flagAction)
                        menuItem(text: "Annotate")
                    }
                    menu(text: "Tools", mnemonic: 'T') {
                        menu(text: "Activity Stream") {
                            menuItem(activityClearAction)
                            menuItem(activityClearAllAction)
                        }
                        menu(text: "Search") {
                            menuItem(text: "Advanced..")
                        }
                        separator()
                        checkBoxMenuItem(workOfflineAction, selectedIcon: imageIcon('/offline_selected.png', id: 'offlineSelectedIcon'),
                                rolloverIcon: imageIcon('/offline_rollover.png', id: 'offlineRolloverIcon'))
//                        menuItem(logoutAction)
                        menuItem(repositoryExplorerAction)
                    }
                    menu(text: "Help", mnemonic: 'H') {
                        menuItem(onlineHelpAction)
                        menuItem(showTipsAction)
                    separator()
                        menuItem(aboutAction)
                    }
                }
                
                borderLayout()
                
                vbox(constraints: BorderLayout.NORTH, border: emptyBorder([10, 20, 5, 10])) {
                    
                    panel(id: 'presencePane', border: emptyBorder([0, 0, 10, 0])) {
//                    flowLayout(alignment: FlowLayout.LEADING)
                        borderLayout()
                    
                        hbox {
                            button(id: 'photoButton', icon: imageIcon(ImageIO.read(Coucou.getResource('/avatar.png')).getScaledInstance(50, 50, Image.SCALE_SMOOTH)), focusPainted: false, toolTipText: 'Click to change photo') //, minimumSize: new Dimension(50, 50))
                            photoButton.actionPerformed = {
                                if (imageChooser.showOpenDialog() == JFileChooser.APPROVE_OPTION) {
                                    doLater {
                                       photoButton.icon = imageIcon(ImageIO.read(imageChooser.selectedFile).getScaledInstance(50, -1, Image.SCALE_SMOOTH))
                                    }
                                }
                            }
                            
                            hstrut(5)
                            
                            vbox {
                                textField(id: 'nameField', columns: 30, text: System.getProperty('user.name', '<Enter your name here>'), font: new Font('Arial', Font.PLAIN, 24), border: emptyBorder(1), foreground: new Color(0x444b56), opaque: false)
                                nameField.focusGained = { nameField.selectAll() }
                                vstrut(3)
                                textField(id: 'statusField', columns: 40, text: 'Comment \u00E7a va?', font: new Font('Arial', Font.PLAIN, 12), border: emptyBorder(1), foreground: new Color(0x444b56), opaque: false)
                                statusField.focusGained = { statusField.selectAll() }
                        
                        //textField(id: 'statusField', text: '<Enter your status here>', border: emptyBorder(1), font: new Font('Arial', Font.PLAIN, 14))
//                                comboBox(id: 'statusField', editable: true, border: lineBorder(color: new Color(230, 230, 230), thickness: 2, roundedCorners: true), font: new Font('Arial', Font.PLAIN, 12))
//                                statusField.putClientProperty(org.jvnet.lafwidget.LafWidget.TEXT_SELECT_ON_FOCUS, true)
                        //statusField.focusGained = { nameField.selectAll() }
                        
                        /*
                        def statusModel = new DefaultComboBoxModel()
                        statusModel.addElement('Available')
                        statusModel.addElement('Busy')
                        statusModel.addElement('Away')
                        statusField.model = statusModel
                        */
//                                statusField.model = new RepositoryComboBoxModel(session.rootNode.getNode('presence'))
//                                statusField.renderer = new RepositoryListCellRenderer()
                            }
                            hglue()
                        }
                    }
                    bind(source: viewPresenceBar, sourceProperty:'selected', target: presencePane, targetProperty:'visible')
                
                    hbox {
                        def navButtonSize = new java.awt.Dimension(20, 20)
                        def navButtons = new JCommandButtonStrip()
                        navButtons.displayState = CommandButtonDisplayState.FIT_TO_ICON
                        navButtons.add(new JCommandButton(SvgBatikResizableIcon.getSvgIcon(Coucou.getResource('/icons/back.svg'), navButtonSize)))
                        navButtons.add(new JCommandButton(SvgBatikResizableIcon.getSvgIcon(Coucou.getResource('/icons/forward.svg'), navButtonSize)))
                        widget(navButtons)
                        hstrut(5)

                        def actionButtonSize = new java.awt.Dimension(16, 16)
                        widget(new JCommandButton(SvgBatikResizableIcon.getSvgIcon(Coucou.getResource('/icons/reload.svg'), actionButtonSize)))
                        hstrut(3)
                        widget(new JCommandButton(SvgBatikResizableIcon.getSvgIcon(Coucou.getResource('/icons/cancel.svg'), actionButtonSize)))
                        hstrut(3)
                        
                        def homeButton = new JCommandButton(SvgBatikResizableIcon.getSvgIcon(Coucou.getResource('/icons/home.svg'), actionButtonSize))
                        homeButton.addActionListener(homeAction)
                        widget(homeButton)
                        hstrut(3)
                        
                        def searchText = 'Search Contacts, Feeds, History, etc.'
//                        textField(new FindField(text: searchText, defaultText: searchText, foreground: Color.LIGHT_GRAY, defaultForeground: Color.LIGHT_GRAY), id: 'filterField', border: compoundBorder([emptyBorder(2), lineBorder(color: Color.LIGHT_GRAY, roundedCorners: true), emptyBorder(3)]))
                        textField(new JTextFieldExt(promptText: searchText, promptColour: Color.LIGHT_GRAY, promptFontStyle: Font.ITALIC), id: 'filterField', border: compoundBorder([emptyBorder(2), lineBorder(color: Color.LIGHT_GRAY, roundedCorners: true), emptyBorder(3)]))
                        filterField.focusGained = { filterField.selectAll() }
                        
                        def filterUpdater = Executors.newSingleThreadScheduledExecutor()
                        def filterFuture
                        filterField.keyReleased = {
                            if (filterFuture) {
                                filterFuture.cancel(false)
                            }
                            filterFuture = filterUpdater.schedule({
                                if (filterField.text) {
                                    def filter = RowFilter.regexFilter("(?i)\\Q${filterField.text}\\E")
                                    for (list in filterableLists) {
                                        list.rowFilter = filter
                                    }
                                }
                                else {
                                    for (list in filterableLists) {
                                        list.rowFilter = null
                                    }
                                }
                            }, 200, TimeUnit.MILLISECONDS)
                        }
                        filterField.actionPerformed = {
                            if (filterField.text) {
//                                addFeed(filterField.text)
                                openSearchView(tabs, filterField.text)
                                filterField.text = null
                                for (list in filterableLists) {
                                    list.rowFilter = null
                                }
                                filterField.transferFocus()
                            }
                        }
                        
                        hstrut(3)
                        def addButtonPopup = new JCommandPopupMenu()
                        addButtonPopup.addMenuButton(new JCommandMenuButton('Compose Email', new EmptyResizableIcon(16)))
                        addButtonPopup.addMenuButton(new JCommandMenuButton('New Appointment', new EmptyResizableIcon(16)))
                        addButtonPopup.addMenuButton(new JCommandMenuButton('New Task', new EmptyResizableIcon(16)))
                        
                        def newNoteButton = new JCommandMenuButton('New Note', new EmptyResizableIcon(16))
                        newNoteButton.addActionListener({ editNote() } as ActionListener)
                        addButtonPopup.addMenuButton(newNoteButton)
                        
                        def newJournalEntryButton = new JCommandMenuButton('New Journal Entry', new EmptyResizableIcon(16))
                        newJournalEntryButton.addActionListener({ editJournalEntry() } as ActionListener)
                        addButtonPopup.addMenuButton(newJournalEntryButton)
                        
                        addButtonPopup.addMenuSeparator()
                        
                        def addFeedButton = new JCommandMenuButton('Add Feed', new EmptyResizableIcon(16))
                        addFeedButton.addActionListener({
                            if (filterField.text) {
                                addFeed(filterField.text)
                            }
                        } as ActionListener)
                        bind(source: filterField, sourceProperty: 'text', target: addFeedButton, targetProperty: 'enabled', converter: {it != null})
                        
                        addButtonPopup.addMenuButton(addFeedButton)
                        addButtonPopup.addMenuButton(new JCommandMenuButton('Add Contact..', new EmptyResizableIcon(16)))
                        addButtonPopup.addMenuButton(new JCommandMenuButton('Add Account..', new EmptyResizableIcon(16)))
                        def addButton = new JCommandButton(SvgBatikResizableIcon.getSvgIcon(Coucou.getResource('/icons/add.svg'), actionButtonSize)) //, displayState: CommandButtonDisplayState.FIT_TO_ICON)
                        addButton.commandButtonKind = JCommandButton.CommandButtonKind.ACTION_AND_POPUP_MAIN_POPUP
                        addButton.displayState = CommandButtonDisplayState.SMALL
                        addButton.popupCallback = { addButtonPopup } as PopupPanelCallback
                        widget(addButton)
                    }
                }
                
//                splitPane(id: 'splitPane', oneTouchExpandable: true, dividerLocation: 1.0) {
                tabbedPane(tabLayoutPolicy: JTabbedPane.SCROLL_TAB_LAYOUT, id: 'tabs') {
                    panel(name: 'Home', id: 'homeTab') {
                         borderLayout()
                         splitPane(id: 'splitPane', oneTouchExpandable: true, dividerLocation: 1.0, continuousLayout: true) {
                             tabbedPane(constraints: 'left', tabPlacement: JTabbedPane.BOTTOM, id: 'navTabs') {
                                 panel(name: 'Contacts') {
                                     borderLayout()
                                     
                                     tabbedPane(tabPlacement: JTabbedPane.RIGHT, id: 'contactViewTabs') {
                                         panel(name: 'Grid', border: emptyBorder([7, 6, 7, 10])) {
                                             borderLayout()
                                             scrollPane(horizontalScrollBarPolicy: JScrollPane.HORIZONTAL_SCROLLBAR_NEVER, border: null) {
//                                             list(id: 'contactsList')
//                                             contactsList.model = new RepositoryListModel(session.rootNode.getNode('contacts'))
//                                             contactsList.cellRenderer = new RepositoryListCellRenderer()

                                                def contactIcon = SvgBatikResizableIcon.getSvgIcon(Coucou.getResource('/icons/im.svg'), new java.awt.Dimension(20, 20))
                                                def contactGrid = new JCommandButtonPanel(50)
                                                contactGrid.addButtonGroup('Online')
                                                contactGrid.addButtonGroup('Offline')
                                                /*
                                                def executor = Executors.newSingleThreadExecutor()
                                                executor.execute({
                                                try {
                                                    final XMPPConnection connection = new XMPPConnection(new ConnectionConfiguration("talk.google.com", 5222, "gmail.com"))
//                                                    final XMPPConnection connection = new XMPPConnection("basepatterns.org");
                                                    connection.connect();
                                                    SASLAuthentication.supportSASLMechanism("PLAIN", 0)
                                                    connection.login("user@gmail.com", "password");
//                                                    connection.login("test", "!password");
                                                    
                                                    for (group in connection.roster.groups) {
                                                        swing.edt {
                                                            contactGrid.addButtonGroup(group.name)
                                                            for (entry in group.entries) {
                                                                def name = (entry.name) ? entry.name : entry.user.find(/^.+(?=@)/)
                                                                    println "${group.name}: ${name}"
                                                                    Icon icon = null
//                                                            try {
//                                                                VCard card = new VCard()
//                                                                card.load(connection, entry.user)
//                                                                icon = new ImageIcon(card.avatar)
//                                                            }
//                                                            catch (Exception e) {
//                                                                log.log unexpected_error, e
//                                                            }
                                                                contactGrid.addButtonToGroup(group.name, new JCommandButton(name, contactIcon))
                                                            }
                                                        }
                                                    }
                                                } catch (XMPPException ex) {
                                                    log.log unexpected_error, ex
                                                }
                                                })
                                                */
                                                for (c in ['Tom', 'Dick', 'Harry']) {
                                                    contactGrid.addButtonToGroup('Online', new JCommandButton(c, contactIcon))
                                                }
                                                for (c in ['Huey', 'Louey', 'Duey']) {
                                                    contactGrid.addButtonToGroup('Offline', new JCommandButton(c, contactIcon))
                                                }
                                                widget(contactGrid)
                                                bind(source: viewContactGroups, sourceProperty:'selected', target: contactGrid, targetProperty: 'toShowGroupLabels')
                                            }
                                         }
                                         panel(name: 'List', border: emptyBorder([7, 6, 7, 10])) {
                                             borderLayout()
                                             scrollPane(horizontalScrollBarPolicy: JScrollPane.HORIZONTAL_SCROLLBAR_NEVER, border: null) {
                                                 table(showHorizontalLines: false, id: 'contactList', columnControlVisible: true)
                                                 contactList.addHighlighter(simpleStripingHighlighter(stripeBackground: HighlighterFactory.GENERIC_GRAY))
                                                 contactList.model = new ContactTableModel(getNode('/contacts'))
                                             }
                                         }
                                     }
                                     contactViewTabs.putClientProperty(SubstanceLookAndFeel.TABBED_PANE_CONTENT_BORDER_KIND, SubstanceConstants.TabContentPaneBorderKind.SINGLE_FULL)
                                     
//                                         titledSeparator(title: 'Online Contacts', font: new Font('Arial', Font.PLAIN, 14), foreground: Color.WHITE)
//                                         label(text: 'Online Contacts', font: new Font('Arial', Font.PLAIN, 14), horizontalTextPosition: SwingConstants.LEFT, foreground: Color.WHITE)
                                     
//                                         titledSeparator(title: 'Saved Contacts', font: new Font('Arial', Font.PLAIN, 14), foreground: Color.WHITE)
/*
                                     panel(constraints: BorderLayout.NORTH, border: emptyBorder([0, 0, 5, 0])) {
                                         flowLayout(alignment: FlowLayout.TRAILING)
                                         
                                         def viewButtonSize = new java.awt.Dimension(16, 16)
                                         def viewButtons = new JCommandButtonStrip()
                                         viewButtons.displayState = CommandButtonDisplayState.FIT_TO_ICON
                                         
                                         def gridViewButton = new JCommandButton(SvgBatikResizableIcon.getSvgIcon(Coucou.getResource('/icons/home.svg'), viewButtonSize))
                                         gridViewButton.actionPerformed = { contactsPane.layout.show(contactsPane, 'gridView') }
                                         viewButtons.add(gridViewButton)
                                         
                                         def listViewButton = new JCommandButton(SvgBatikResizableIcon.getSvgIcon(Coucou.getResource('/icons/home.svg'), viewButtonSize))
                                         listViewButton.actionPerformed = { contactsPane.layout.show(contactsPane, 'listView') }
                                         viewButtons.add(listViewButton)
                                         widget(viewButtons)
                                     }
                                     
                                     panel(id: 'contactsPane') {
                                         cardLayout()
                                     }
*/
//                                         vglue()
//                                     }
                                     
//                                     hbox(constraints: BorderLayout.SOUTH) {
//                                         hglue()
//                                         hyperlink(new JXHyperlink(newContactAction))
//                                     }
                                 }
                                 panel(name: 'Planner', border: emptyBorder(10)) {
                                     borderLayout()
                                     
                                     scrollPane() {
                                        treeTable(id: 'plannerTree', columnControlVisible: true)
                                        getNode('/planner/Personal')
                                        getNode('/planner/Work')

                                        // XXX: Search folders...
                                        QueryObjectModelBuilder queryBuilder = new QueryObjectModelBuilder(session.workspace.queryManager, session.valueFactory)
                                        Query q = queryBuilder.with {
                                            query(
                                                source: selector(nodeType: 'nt:unstructured', name: 'all_nodes'),
                                                constraint: and(
                                                        constraint1: childNode(selectorName: 'all_nodes', path: '/planner'),
                                                        constraint2: comparison(
                                                                operand1: propertyValue(selectorName: 'all_nodes', propertyName: 'due'),
                                                                operator: QueryObjectModelConstants.JCR_OPERATOR_GREATER_THAN_OR_EQUAL_TO,
                                                                operand2: bindVariable(name: 'minDate'))))
                                        }
                                                                
                                        def todayNode = getNode('/planner/Today')
                                        if (!todayNode.hasNode("query")) {
                                            q.storeAsNode("${todayNode.path}/query")
                                        }
                                        def tomorrowNode = getNode('/planner/Tomorrow')
                                        if (!tomorrowNode.hasNode("query")) {
                                            q.storeAsNode("${tomorrowNode.path}/query")
                                        }
                                        def thisWeekNode = getNode('/planner/This Week')
                                        if (!thisWeekNode.hasNode("query")) {
                                            q.storeAsNode("${thisWeekNode.path}/query")
                                        }
                                        getNode('/planner/Next Week')
                                        getNode('/planner/This Month')
                                        getNode('/planner/Overdue')
                                        getNode('/planner/Deleted')
//                                        plannerTree.treeTableModel = new PlannerTreeTableModel(getNode('/planner'))
                                        plannerTree.treeTableModel = new DefaultTreeTableModel(new PlannerTreeTableNode(getNode('/planner')), ['Summary', 'Participants', 'Categories', 'Due'])
                                        plannerTree.selectionModel.selectionMode = ListSelectionModel.SINGLE_SELECTION
                                        plannerTree.packAll()
                                        plannerTree.focusLost = {
                                            plannerTree.clearSelection()
                                        }
                                     }
                                 }
                                 panel(name: 'History', border: emptyBorder(10)) {
                                     borderLayout()
//                                     label(text: 'Folders', constraints: BorderLayout.NORTH, font: new Font('Arial', Font.PLAIN, 14), foreground: Color.WHITE, background: Color.GRAY, opaque: true)
                                     scrollPane(horizontalScrollBarPolicy: JScrollPane.HORIZONTAL_SCROLLBAR_NEVER, border: null) {
//                                         tree(id: 'historyTree', rootVisible: false, showsRootHandles: true)
                                         /*
                                         historyTree.model.root.removeAllChildren()
                                         
                                         def mailNode = new TreeNode('Mail')
                                         mailNode.add(new TreeNode('Inbox'))
                                         mailNode.add(new TreeNode('Sent'))
                                         mailNode.add(new TreeNode('Drafts'))
                                         mailNode.add(new TreeNode('Deleted'))
                                         
                                         historyTree.model.root.add(mailNode)
                                         historyTree.model.root.add(new TreeNode('Conversations'))
                                         historyTree.model.root.add(new TreeNode('Events'))
                                         historyTree.model.root.add(new TreeNode('Tasks'))
                                         historyTree.model.reload(historyTree.model.root)
                                         */
//                                         historyTree.model = new RepositoryTreeModel(session.rootNode.getNode('history'))
//                                         historyTree.cellRenderer = new RepositoryTreeCellRenderer()

/*
                                        treeTable(id: 'historyTree', columnControlVisible: true)
                                        // email..
                                        getNode('/history/Inbox')
                                        getNode('/history/Templates')
                                        getNode('/history/Templates/Mail')
                                        getNode('/history/Templates/Meeting')
                                        getNode('/history/Templates/Task')
                                        // chat..
                                        getNode('/history/Conversations')
                                        
                                        // XXX: Search folders...
                                        getNode('/history/Outbox')
                                        getNode('/history/Sent')
                                        getNode('/history/Drafts')
                                        getNode('/history/Today')
                                        getNode('/history/Yesterday')
                                        getNode('/history/This Week')
                                        getNode('/history/Last Week')
                                        getNode('/history/This Month')
                                        getNode('/history/Deleted')
//                                        historyTree.treeTableModel = new HistoryTreeTableModel(getNode('/history'))
                                        historyTree.treeTableModel = new DefaultTreeTableModel(new HistoryTreeTableNode(getNode('/History')), ['Flags', 'Subject', 'From', 'Count', 'Last Updated'])
                                        historyTree.selectionModel.selectionMode = ListSelectionModel.SINGLE_SELECTION
//                                        historyTree.selectionModel.valueChanged = {
                                        historyTree.packAll()
                                        historyTree.focusLost = {
                                            historyTree.clearSelection()
                                        }
*/
                                        table(showHorizontalLines: false, id: 'historyTable', columnControlVisible: true)
                                        historyTable.addHighlighter(simpleStripingHighlighter(stripeBackground: HighlighterFactory.GENERIC_GRAY))
//                                        historyTable.model = new EventTableModel(historyList, new MyTableFormat())
                                     }
                                 }
                                 panel(name: 'Feeds', border: emptyBorder(10)) {
                                     borderLayout()

                                     scrollPane(border: null) {
//                                         list(id: 'feedList')
//                                         feedList.model = new RepositoryListModel(getNode('/feeds'))
//                                         feedList.cellRenderer = new FeedViewListCellRenderer()
                                         table(showHorizontalLines: false, id: 'feedList', columnControlVisible: true)
                                         feedList.addHighlighter(simpleStripingHighlighter(stripeBackground: HighlighterFactory.GENERIC_GRAY))
                                         feedList.setDefaultRenderer(Date, new DateCellRenderer(getNode('/feeds')))
                                         feedList.model = new FeedTableModel(getNode('/feeds'))
                                         feedList.packAll()
                                         feedList.setSortOrder(3, SortOrder.DESCENDING)
                                         feedList.sortsOnUpdates = true
                                         filterableLists << feedList
                                         feedList.selectionModel.valueChanged = { e ->
                                             if (!e.valueIsAdjusting) {
                                                 if (feedList.selectedRow >= 0) {
                                                     def feeds = getNode('/feeds').nodes
                                                     feeds.skip(feedList.convertRowIndexToModel(feedList.selectedRow))
                                                     def feed = feeds.nextNode()
                                                     swing.edt {
                                                         editContext.delete = {
                                                             if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(coucouFrame, "Delete feed: ${feed}?", 'Confirm delete', JOptionPane.OK_CANCEL_OPTION)) {
                                                                 println "Deleting feed: ${feed}"
                                                                 removeNode feed
        //                                                         swing.edt {
        //                                                             feedList.model.fireTableDataChanged()
        //                                                         }
                                                             }
                                                         }
                                                     }
    //                                                 editContext.enabled = true
                                                     log.log delete_enabled, feed
                                                 }
                                                 else {
                                                     swing.edt {
                                                         editContext.delete = null
                                                     }
    //                                                 editContext.enabled = false
                                                 }
                                             }
                                         }
                                         feedList.mouseClicked = { e ->
                                            if (e.button == MouseEvent.BUTTON1 && e.clickCount >= 2 && feedList.selectedRow >= 0) {
//                                                if (feedList.selectedValue) {
                                                def feeds = getNode('/feeds').nodes
                                                feeds.skip(feedList.convertRowIndexToModel(feedList.selectedRow))
                                                def feed = feeds.nextNode()
                                                openFeedView(tabs, feed)
//                                                }
                                            }
                                         }
                                         feedList.focusLost = {
                                             feedList.clearSelection()
                                         }
                                     }
                                 }
                                 panel(name: 'Notes', border: emptyBorder(10)) {
                                     borderLayout()

                                     scrollPane(border: null) {
                                         table(showHorizontalLines: false, id: 'noteList')
                                         noteList.addHighlighter(simpleStripingHighlighter(stripeBackground: HighlighterFactory.GENERIC_GRAY))
                                         noteList.model = new NoteTableModel(getNode('/notes'))
                                         noteList.packAll()
                                         noteList.setDefaultRenderer(Date, new DateCellRenderer(getNode('/notes')))
                                         noteList.setSortOrder(2, SortOrder.DESCENDING)
                                         noteList.sortsOnUpdates = true
                                         filterableLists << noteList
                                         noteList.selectionModel.valueChanged = { e ->
                                             if (!e.valueIsAdjusting) {
                                                 if (noteList.selectedRow >= 0) {
                                                     def notes = getNode('/notes').nodes
                                                     notes.skip(noteList.convertRowIndexToModel(noteList.selectedRow))
                                                     def note = notes.nextNode()
                                                     swing.edt {
                                                         editContext.delete = {
                                                             if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(coucouFrame, "Delete note: ${note}?", 'Confirm delete', JOptionPane.OK_CANCEL_OPTION)) {
                                                                 println "Deleting note: ${note}"
                                                                 removeNode note
        //                                                         swing.edt {
        //                                                             noteList.model.fireTableDataChanged()
        //                                                         }
                                                             }
                                                         }
                                                     }
    //                                                 editContext.enabled = true
                                                     log.log delete_enabled, note
                                                 }
                                                 else {
                                                     swing.edt {
                                                         editContext.delete = null
                                                     }
    //                                                 editContext.enabled = false
                                                 }
                                             }
                                         }
                                         noteList.mouseClicked = { e ->
                                            if (e.button == MouseEvent.BUTTON1 && e.clickCount >= 2 && noteList.selectedRow >= 0) {
                                                def notes = getNode('/notes').nodes
                                                notes.skip(noteList.convertRowIndexToModel(noteList.selectedRow))
                                                def note = notes.nextNode()
                                                openNoteView(tabs, note)
                                            }
                                         }
                                         noteList.focusLost = {
                                             noteList.clearSelection()
                                         }
                                     }
                                 }
                                 panel(name: 'Journal', border: emptyBorder(10)) {
                                     borderLayout()

                                     scrollPane(border: null) {
                                         /*
                                         table(showHorizontalLines: false, id: 'journalList')
                                         journalList.addHighlighter(simpleStripingHighlighter(stripeBackground: HighlighterFactory.GENERIC_GRAY))
                                         journalList.model = new JournalTableModel(getNode('/journal'))
                                         journalList.packAll()
                                         journalList.setDefaultRenderer(Date, new DateCellRenderer())
                                         journalList.setSortOrder(2, SortOrder.DESCENDING)
                                         journalList.sortsOnUpdates = true
                                         filterableLists << journalList
                                         journalList.selectionModel.valueChanged = { e ->
                                             if (!e.valueIsAdjusting) {
                                                 if (journalList.selectedRow >= 0) {
                                                     def journalEntries = getNode('/journal').nodes
                                                     journalEntries.skip(journalList.convertRowIndexToModel(journalList.selectedRow))
                                                     def journalEntry = journalEntries.nextNode()
                                                     swing.edt {
                                                         editContext.delete = {
                                                             if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(coucouFrame, "Delete journal entry: ${journalEntry}?", 'Confirm delete', JOptionPane.OK_CANCEL_OPTION)) {
                                                                 println "Deleting journal entry: ${journalEntry}"
                                                                 removeNode journalEntry
        //                                                         swing.edt {
        //                                                             journalList.model.fireTableDataChanged()
        //                                                         }
                                                             }
                                                         }
                                                     }
    //                                                 editContext.enabled = true
                                                     log.log delete_enabled, journalEntry
                                                 }
                                                 else {
                                                     swing.edt {
                                                         editContext.delete = null
                                                     }
    //                                                 editContext.enabled = false
                                                 }
                                             }
                                         }
                                         journalList.mouseClicked = { e ->
                                            if (e.button == MouseEvent.BUTTON1 && e.clickCount >= 2 && journalList.selectedRow >= 0) {
                                                def journalEntries = getNode('/journal').nodes
                                                journalEntries.skip(journalList.convertRowIndexToModel(journalList.selectedRow))
                                                def journalEntry = journalEntries.nextNode()
                                                openJournalEntryView(tabs, journalEntry)
                                            }
                                         }
                                         journalList.focusLost = {
                                             journalList.clearSelection()
                                         }
                                         */
                                         treeTable(id: 'journalTree', columnControlVisible: true)
                                         getNode('/journal/Personal')
                                         getNode('/journal/Work')

                                         // XXX: Search folders...
                                         QueryObjectModelBuilder queryBuilder = new QueryObjectModelBuilder(session.workspace.queryManager, session.valueFactory)
                                         Query q = queryBuilder.with {
                                             query(
                                                 source: selector(nodeType: 'nt:unstructured', name: 'all_nodes'),
                                                 constraint: and(
                                                         constraint1: childNode(selectorName: 'all_nodes', path: '/journal'),
                                                         constraint2: comparison(
                                                                 operand1: propertyValue(selectorName: 'all_nodes', propertyName: 'lastModified'),
                                                                 operator: QueryObjectModelConstants.JCR_OPERATOR_GREATER_THAN_OR_EQUAL_TO,
                                                                 operand2: bindVariable(name: 'minDate'))))
                                         }
                                                                 
                                         def todayNode = getNode('/journal/Today')
                                         if (!todayNode.hasNode("query")) {
//                                             q.storeAsNode("${todayNode.path}/query")
                                         }
                                         def yesterdayNode = getNode('/journal/Yesterday')
                                         if (!yesterdayNode.hasNode("query")) {
//                                             q.storeAsNode("${yesterdayNode.path}/query")
                                         }
                                         def thisWeekNode = getNode('/journal/This Week')
//                                         if (!thisWeekNode.hasNode("query")) {
//                                             q.storeAsNode("${thisWeekNode.path}/query")
//                                         }
                                         getNode('/journal/Last Week')
                                         getNode('/journal/This Month')
                                         getNode('/journal/Last Month')
                                         getNode('/journal/Deleted')
//                                         journalTree.treeTableModel = new JournalTreeTableModel(getNode('/journal'))
                                         journalTree.treeTableModel = new DefaultTreeTableModel(new JournalTreeTableNode(getNode('/journal')), ['Subject', 'Tags', 'Last Modified'])
                                         journalTree.selectionModel.selectionMode = ListSelectionModel.SINGLE_SELECTION
                                         journalTree.packAll()
                                         journalTree.focusLost = {
                                             journalTree.clearSelection()
                                         }
                                     }
                                 }
                                 panel(name: 'Accounts', border: emptyBorder(10)) {
                                     borderLayout()

                                     scrollPane(border: null) {
//                                         tree(id: 'accountsTree', rootVisible: false, showsRootHandles: true)
                                         /*
                                         accountsTree.model.root.removeAllChildren()
                                         def accountTypeNodes = session.rootNode.getNode('accounts').getNodes()
                                         while (accountTypeNodes.hasNext()) {
                                             def accountTypeNode = accountTypeNodes.nextNode()
                                             def treeNode = new TreeNode(accountTypeNode.name)
                                             def accountNodes = accountTypeNode.getNodes()
                                             while (accountNodes.hasNext()) {
                                                 treeNode.add(new TreeNode(accountNodes.next().name))
                                             }
                                             accountsTree.model.root.add(treeNode)
                                         }
                                         accountsTree.model.reload(accountsTree.model.root)
                                         */
//                                         accountsTree.model = new RepositoryTreeModel(session.rootNode.getNode('accounts'))
//                                         accountsTree.cellRenderer = new RepositoryTreeCellRenderer()
                                         //session.workspace.observationManager.addEventListener(new AccountsUpdateListener(accountsTree), Event.NODE_ADDED | Event.NODE_REMOVED, '/accounts/', true, null, null, false)
                                         //session.workspace.observationManager.addEventListener(accountsTree.model, Event.NODE_ADDED | Event.NODE_REMOVED, '/accounts/', true, null, null, false)
                                         table(showHorizontalLines: false, id: 'accountList')
                                         accountList.addHighlighter(simpleStripingHighlighter(stripeBackground: HighlighterFactory.GENERIC_GRAY))
                                         accountList.model = new AccountTableModel(getNode('/accounts'))
                                         accountList.packAll()
                                         accountList.focusLost = {
                                             accountList.clearSelection()
                                         }
/*
                                        accountsTree.valueChanged = { e ->
                                            if (e.path) {
                                                editContext.item = e.path.lastPathComponent
                                                editContext.enabled = true
                                                log.log delete_enabled, editContext.item
                                            }
                                            else {
                                                editContext.enabled = false
                                                log.log delete_disabled
                                            }
                                        }
                                        accountsTree.focusGained = {
                                             if (accountsTree.selectionPath) {
                                                 editContext.item = accountsTree.selectionPath.lastPathComponent
                                                 editContext.enabled = true
                                                 log.log delete_enabled, editContext.item
                                             }
                                             else {
                                                 editContext.enabled = false
                                                 log.log delete_disabled
                                             }
                                        }
                                        accountsTree.focusLost = {
                                            editContext.enabled = false
                                            log.log delete_disabled
                                        }
                                        accountsTree.mouseClicked = { e ->
                                            if (e.button == MouseEvent.BUTTON1 && e.clickCount >= 2) {
                                                if (accountsTree.selectionPath) {
                                                    def node = accountsTree.selectionPath.lastPathComponent
                                                    if (!node.hasNodes()) {
                                                        openNodeTab(tabs, node)
                                                    }
                                                }
                                            }
                                        }
*/
                                     }
                                     hbox(constraints: BorderLayout.SOUTH) {
                                         hglue()
                                         hyperlink(new JXHyperlink(newAccountAction))
                                     }
                                 }
/*
                                 panel(name: 'Files', border: emptyBorder(10)) {
                                     borderLayout()
                                     textField(new FindField(defaultText: 'Filter files..'), id: 'fileFilterField', foreground: Color.LIGHT_GRAY, border: emptyBorder(5), constraints: BorderLayout.NORTH)
                                     fileFilterField.focusGained = { fileFilterField.selectAll() }
                                     fileFilterField.keyReleased = {
                                         if (fileFilterField.text) {
                                             fileList.rowFilter = RowFilter.regexFilter("(?i)\\Q${fileFilterField.text}\\E")
                                         }
                                         else {
                                             fileList.rowFilter = null
                                         }
                                     }
                                     scrollPane(border: null) {
                                         table(showHorizontalLines: false, id: 'fileList')
                                         fileList.addHighlighter(simpleStripingHighlighter(stripeBackground: HighlighterFactory.GENERIC_GRAY))
//                                         fileList.model = new FileTableModel(getNode('/files'))
                                     }
                                 }
*/
                             }
                             navTabs.putClientProperty(SubstanceLookAndFeel.TABBED_PANE_CONTENT_BORDER_KIND, SubstanceConstants.TabContentPaneBorderKind.SINGLE_FULL)
                             panel(constraints: 'right', border: emptyBorder(10), opaque: true, backgroundPainter: imagePainter(image: ImageIO.read(Coucou.getResource('/avatar.png')))) {
                                 borderLayout()
                                 scrollPane(horizontalScrollBarPolicy: JScrollPane.HORIZONTAL_SCROLLBAR_NEVER, border: null) {
                                     list(opaque: false, id: 'activity')
                                     activity.cellRenderer = new ActivityListCellRenderer()
                                     activity.addHighlighter(simpleStripingHighlighter(stripeBackground: HighlighterFactory.GENERIC_GRAY))
                                     activity.mouseClicked = { e ->
                                         if (e.button == MouseEvent.BUTTON1 && e.clickCount >= 2) {
                                             if (activity.selectedValue) {
                                                 def node = activity.selectedValue
                                                 openFeedView(tabs, node.parent, node)
                                                 try {
                                                     // lock for list modification..
                                                     activityList.readWriteLock.writeLock().lock()
                                                     activityList.remove(node)
                                                 }
                                                 finally {
                                                     // unlock post-list modification..
                                                     activityList.readWriteLock.writeLock().unlock()
                                                 }
                                             }
                                         }
                                     }
                                     activity.focusLost = {
                                         activity.clearSelection()
                                     }
                                     bind(source: activity.selectionModel, sourceEvent: 'valueChanged', sourceValue: {activity.selectedValue != null}, target: activityClearAction, targetProperty: 'enabled')
                                     /*
                                    if (!session.rootNode.hasNode('feeds')) {
                                        log.log init_node, 'feeds'
                                        def feedsNode = session.rootNode.addNode('feeds')
                                        session.rootNode.save()
                                    }
                                     
                                     def weatherFeed = new XmlSlurper().parseText("http://weather.yahooapis.com/forecastrss?w=1103816&u=c".toURL().text)
                                     def feedsNode = session.rootNode.getNode('feeds')
//                                     jcr.parentNode = feedsNode
//                                     def weatherNode = jcr.weather('url': "http://weather.yahooapis.com/forecastrss?w=1103816&u=c") {
//                                             item('source': weatherFeed.channel.title,
//                                              'subject': weatherFeed.channel.item.condition.@text,
//                                              'date': Date.parse('EEE, dd MMM yyyy h:mm a', weatherFeed.channel.item.condition.@date.toString().split(' ')[0..-2].join(' '))) {}
//                                         }
//                                     }
                                     def weatherNode = feedsNode.addNode('weather')
                                     weatherNode.setProperty('type', 'feed')
                                     weatherNode.setProperty('source', weatherFeed.channel.title.toString())
                                     weatherNode.setProperty('subject', weatherFeed.channel.item.condition.@text.toString())
                                     def calendar = Calendar.instance
                                     calendar.setTime(Date.parse('EEE, dd MMM yyyy h:mm a', weatherFeed.channel.item.condition.@date.toString().split(' ')[0..-2].join(' ')))
                                     weatherNode.setProperty('date', calendar)
                                     
                                     def activityModel = new DefaultListModel()
                                     
                                     // rome uses Thread.contextClassLoader..
                                     Thread.currentThread().contextClassLoader = Coucou.class.classLoader
//                                     def hudsonFeed = new SyndFeedInput().build(new XmlReader(new URL("http://mdsbu04/hudson/view/Trade%20Analytics/job/AnalyticsServer/rssAll")))
                                     def coucouFeed = new SyndFeedInput().build(new XmlReader(new URL("http://coucou.im/feed")))
                                     for (entry in coucouFeed.entries) {
                                         entry.source = coucouFeed
//                                         activityModel.addElement(new FeedMessage(feedEntry: entry, buildActivityString: buildActivityString))
                                         def blogNode = feedsNode.addNode('blog')
                                         blogNode.setProperty('type', 'feed')
                                         blogNode.setProperty('source', entry.source.title)
                                         blogNode.setProperty('subject', entry.title)
                                         calendar = Calendar.instance
                                         calendar.setTime(entry.publishedDate)
                                         blogNode.setProperty('date', calendar)
                                         activityModel.addElement(blogNode)
                                     }
//                                     activityModel.addElement(new WeatherMessage(weatherNode: weatherNode, buildActivityString: buildActivityString))
                                     activityModel.addElement(weatherNode)
//                                     activityModel.addElement(new ImMessage('Coucou', 'test@example.com', new Date()))
//                                     activityModel.addElement(new MailMessage('Intro to Coucou', 'test@example.com', new Date(System.currentTimeMillis() - 10000), 3))
//                                     activityModel.addElement(new EventMessage('Meeting with associates', 'test@example.com', new Date(System.currentTimeMillis() - 100000)))
//                                     activityModel.addElement(new TaskMessage('Complete TPS Reports', 'test@example.com', new Date(System.currentTimeMillis() - 1000000)))
                                     */
                                     def boundedActivityList = new RangeList(new SortedList(activityList, compareByDate as Comparator))
                                     boundedActivityList.setHeadRange(0, 100)
                                     def activityModel = new EventListModel(boundedActivityList)
                                     activity.model = activityModel
//                                     filterableLists << activity

                                     // activity stream..
                                     session.workspace.observationManager.addEventListener(new ActivityStreamUpdater(activityList: activityList, activityStream: activity, session: session, swing: swing), Event.NODE_ADDED, '/', true, null, null, false)
                                     /*
                                     session.workspace.observationManager.addEventListener({ events ->
                                         println "Events fired: ${events.size}"
                                         while (events.hasNext()) {
                                             def event = events.nextEvent()
                                             println "Node added: ${event.path}"
                                             def node = session.getItem(event.path)
                                             swing.edt {
                                                 activityStream.model.insertElementAt node, 0
                                                 activityStream.clearSelection()
                                                 activityStream.ensureIndexIsVisible(0)
                                             }
                                         }
                                     } as javax.jcr.observation.EventListener)
                                     */
                                 }
                             }
                         }
                     }
                 }
                tabs.setIconAt(tabs.indexOfComponent(homeTab), new PaddedIcon(imageIcon('/logo-12.png'), 14, 20))
                 tabs.putClientProperty(SubstanceLookAndFeel.TABBED_PANE_CONTENT_BORDER_KIND, SubstanceConstants.TabContentPaneBorderKind.SINGLE_FULL)
                 tabs.putClientProperty(SubstanceLookAndFeel.TABBED_PANE_CLOSE_CALLBACK, new TabCloseCallbackImpl())
                 SubstanceLookAndFeel.registerTabCloseChangeListener(tabs, new VetoableMultipleTabCloseListenerImpl([homeTab]))
                 tabs.putClientProperty(LafWidget.TABBED_PANE_PREVIEW_PAINTER, new TabPreviewPainterImpl())
             
                 statusBar(constraints: BorderLayout.SOUTH, border:emptyBorder([0, 4, 0, 4]), id: 'cStatusBar') {
                     label(id: 'statusMessage', text: 'Ready', constraints: new JXStatusBar.Constraint(JXStatusBar.Constraint.ResizeBehavior.FILL))
                    toggleButton(busyAction,
                            text: null,
                            toolTipText: 'Busy',
                            selectedIcon: busySelectedIcon,
                            rolloverIcon: busyRolloverIcon,
                            margin: null,
                            border: emptyBorder([0, 4, 0, 4]),
                            borderPainted: false,
                            contentAreaFilled: false,
                            focusPainted: false,
                            opaque: false)
    //                        contraints: statusBarContraints)
                    toggleButton(invisibleAction,
                            text: null,
                            toolTipText: 'Invisible',
                            selectedIcon: invisibleSelectedIcon,
                            rolloverIcon: invisibleRolloverIcon,
                            margin: null,
                            border: emptyBorder([0, 4, 0, 4]),
                            borderPainted: false,
                            contentAreaFilled: false,
                            focusPainted: false,
                            opaque: false)
                    toggleButton(workOfflineAction,
                            text: null,
                            toolTipText: 'Work Offline',
                            selectedIcon: offlineSelectedIcon,
                            rolloverIcon: offlineRolloverIcon,
                            margin: null,
                            border: emptyBorder([0, 4, 0, 4]),
                            borderPainted: false,
                            contentAreaFilled: false,
                            focusPainted: false,
                            opaque: false)
                 }
                 bind(source: viewStatusBar, sourceProperty:'selected', target:cStatusBar, targetProperty:'visible')
             }

             if (SystemTray.isSupported()) {
                 TrayIcon trayIcon = new TrayIcon(ImageIO.read(Coucou.getResource('/happy-14.png')), 'Coucou')
                 trayIcon.imageAutoSize = false
                 trayIcon.mousePressed = { event ->
                     if (event.button == MouseEvent.BUTTON1) {
                         coucouFrame.visible = true
                     }
                 }
                 
                 PopupMenu popupMenu = new PopupMenu('Coucou')
                 MenuItem openMenuItem = new MenuItem('Open')
                 openMenuItem.actionPerformed = {
                    coucouFrame.visible = true
                 }
                 popupMenu.add(openMenuItem)
                 //openMenuItem.addNotify()
                 //openMenuItem.font = openMenuItem.font.deriveFont(Font.BOLD)
                 popupMenu.addSeparator()
                 MenuItem exitMenuItem = new MenuItem('Exit')
                 exitMenuItem.actionPerformed = {
                     close(coucouFrame, true)
                 }
                 popupMenu.add(exitMenuItem)
                 trayIcon.popupMenu = popupMenu
                 
                 SystemTray.systemTray.add(trayIcon)
                 
                 coucouFrame.windowClosing = {
                     close(coucouFrame, false)
                     
                     // show notification on hide..
                     trayIcon.displayMessage('Coucou Minimised', 'Right-click and select Exit to shutdown completely', TrayIcon.MessageType.INFO)
                 }
             }
             else {
                 coucouFrame.windowClosing = {
                     close(coucouFrame, true)
                 }
             }
             
//             coucouFrame.ribbon.configureHelp(helpIcon, onlineHelpAction)
             
//           TrackerRegistry.instance.register(coucouFrame, 'coucouFrame');
           
           // bonjour..
           /*
           try {
               JmDNS jmdns = JmDNS.create();
               ServiceInfo info = ServiceInfo.create("_presence._tcp.local.", "Coucou [" + username + "]", 1337, 0, 0, "");
               jmdns.registerService(info);
           } catch (IOException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
           }
           */
           
           // scheduled tasks..
           def feedUpdater = Executors.newSingleThreadScheduledExecutor()
           feedUpdater.scheduleAtFixedRate({
               for (feedNode in getNode("/feeds").nodes) {
                   if (feedNode.hasProperty('url')) {
                        println "Updating feed: ${feedNode.getProperty('title').value.string}"
//                        def entryCount = feedNode.nodes.size
                        
                        try {
                            Asynchronizer.doParallel(5) {
                                def future = updateFeed.callAsync(feedNode.getProperty('url').value.string)
//                                if (feedNode.nodes.size != entryCount) {
//                                }
//                                future.get()
//                                swing.edt {
//                                    feedList.model.fireTableDataChanged()
//                                }
                            }
                        }
                        catch (Exception e) {
                            log.log unexpected_error, e
                        }
                    }
                }
//                feedNode.parent.save()
               
           }, 0, 30, TimeUnit.MINUTES)
           
           coucouFrame.visible = true
        }
    }
}

class XmppAccount {
    
    def host
    def user
    def password
    
    String getName() {
        if (user) {
            return "${user}@${host}"
        }
        else {
            return "<New account>"
        }
    }
    
    void connect() {
        try {
            final XMPPConnection connection = new XMPPConnection("basepatterns.org");
            connection.connect();
            connection.login("test", "!password");
        } catch (XMPPException ex) {
            log.log unexpected_error, ex
        }
    }
}

class Contact {
    
    def firstName
    def lastName
    
    String getName() {
        if (firstName) {
            return "${firstName} ${lastName}" 
        }
        else {
            return "<New contact>"
        }
    }
}

class WizardPageImpl extends WizardPage {
    
//    static String description
    
    def validation
    
    public WizardPageImpl(String stepId, String stepDescription) {
        super(stepId, stepDescription)
    }
//    static String getDescription() {
//        return description
//    }
    
    protected String validateContents(Component component, Object event) {
        String validationString = validation()
        if (validationString) {
            return validationString;
        }
        return super.validateContents(component, event);
    }
}

class WizardBranchControllerImpl extends WizardBranchController {
    
//    def keys = [:]

    def wizards = [:]
    
    public WizardBranchControllerImpl(WizardPage page) {
        super(page)
    }
    
    protected Wizard getWizardForStep(String wizardStep, Map settings) {
        for (e in wizards) {
            if (settings[e.key]) {
                return e.value
            }
        }
        return null
    }
}

class CreateContactProducer implements WizardResultProducer {

    def contactsNode
    
    CreateContactProducer(def contactsNode) {
        this.contactsNode = contactsNode
    }
    
    Object finish(Map data) throws WizardException {
        //def node = contactsNode.addNode(Text.escape("${data['displayNameField']}"))
        //contactsNode.save()
        return null
    }

    boolean cancel(Map settings) {
        return true;
    }
}

class CreateAccountProducer implements WizardResultProducer {

    def accountsNode
    
    CreateAccountProducer(def accountsNode) {
        this.accountsNode = accountsNode
    }
    
    Object finish(Map data) throws WizardException {
        if (data['xmppAccount']) {
            if (!accountsNode.hasNode('xmpp')) {
                accountsNode.addNode('xmpp')
            }
            def node = accountsNode.addNode("xmpp/${data['usernameField']}")
            saveNode accountsNode
        }
        else if (data['emailAccount']) {
            if (!accountsNode.hasNode('mail')) {
                accountsNode.addNode('mail')
            }
            def node = accountsNode.addNode("mail/${data['emailAddressField']}")
            saveNode accountsNode
        }
        return null
    }

    boolean cancel(Map settings) {
        return true;
    }
}

class RepositoryTreeModel extends AbstractTreeModel implements javax.jcr.observation.EventListener {

    static def log = Logger.getInstance(RepositoryTreeModel)
    
    def root
    
    RepositoryTreeModel(def rootNode) {
        root = rootNode
        root.session.workspace.observationManager.addEventListener(this, Event.NODE_ADDED | Event.NODE_REMOVED, root.path, true, null, null, false)
    }
    
    Object getChild(Object parent, int index) {
        def nodes = parent.nodes
        if (index >= 0 && index < nodes.size) {
            nodes.skip(index)
            return nodes.nextNode()
        }
        return null
    }
    
    int getChildCount(Object parent) {
        return parent.nodes.size
    }
    
    int getIndexOfChild(Object parent, Object child) {
        def nodes = parent.nodes
        while (nodes.hasNext()) {
            if (nodes.nextNode() == child) {
                return nodes.position
            }
        }
        return -1
    }
    
    Object getRoot() {
        return root
    }
    
    boolean isLeaf(Object node) {
        return !node.hasNodes()
    }
    
    void onEvent(EventIterator events) {
        while (events.hasNext()) {
            def event = events.nextEvent()
            if (event.type == Event.NODE_ADDED) {
                log.info "Node added: ${event.path}"
                def path = []
                def childIndices = []
                try {
                    def node = root.session.getItem(event.path)
                    childIndices.add(node.nodes.size - 1)
                    while (node && node.parent) {
                        path.add(0, node.parent)
                        node = node.parent
                    }
                } catch (ItemNotFoundException e) {
                    // must be the root node..
                } catch (Exception e) {
                    log.error e
                }
                log.info "Firing path change event: ${path}, ${childIndices}"
//                fireTreeStructureChanged((Object[]) path)
                fireTreeNodesInserted((Object[]) path, (int[]) childIndices)
            }
            else if (event.type == Event.NODE_REMOVED) {
                log.info "Node removed: ${event.path}"
            }
        }
    }
}

class RepositoryTreeCellRenderer extends DefaultTreeCellRenderer {
    
    Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus)
        text = value.name
        return this
    }
}

class RepositoryListModel implements ListModel, javax.jcr.observation.EventListener {

    def node
    def swing
    
    RepositoryListModel(def node) {
        this.node = node
        node.session.workspace.observationManager.addEventListener(this, Event.NODE_ADDED | Event.NODE_REMOVED, node.path, false, null, null, false)
    }
    
    Object getElementAt(int index) {
        def nodes = node.nodes
        if (index >= 0 && index < nodes.size) {
            nodes.skip(index)
            return nodes.nextNode()
        }
        return null
    }
 
    int getSize() {
        return node.nodes.size
    }
 
    void addListDataListener(ListDataListener l) {}

    void removeListDataListener(ListDataListener l) {}
    
    void onEvent(EventIterator events) {
        println "Repo changed: ${events}"
        swing.edt {
            fireContentsChanged(this, 0, getSize())
        }
    }
}


class RepositoryListCellRenderer extends DefaultListCellRenderer {
    
    Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus)
        text = value.name
        return this
    }
}

class FeedViewListCellRenderer extends DefaultListCellRenderer {
    
    Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus)
        if (value.hasProperty('title')) {
            text = value.getProperty('title').value.string
        }
        else {
            text = value.name
        }
        return this
    }
}

class RepositoryComboBoxModel extends RepositoryListModel implements ComboBoxModel {

    def selectedNode

    RepositoryComboBoxModel(def node) {
        super(node)
    }
    
    Object getSelectedItem() {
        return selectedNode
    }
    
    void setSelectedItem(Object anItem) {
        selectedNode = anItem
    }
}

class ActivityStreamUpdater implements javax.jcr.observation.EventListener {

    def session
    def activityList
    def activityStream
    def swing

    void onEvent(EventIterator events) {
        swing.doOutside {
            println "Events fired: ${events.size}"
            while (events.hasNext()) {
                def event = events.nextEvent()
                println "Node added: ${event.path}"
                def node = session.getItem(event.path)
                try {
                    // lock for list modification..
                    activityList.readWriteLock.writeLock().lock()
                    activityList.add 0, node
                }
                finally {
                    // unlock post-list modification..
                    activityList.readWriteLock.writeLock().unlock()
                }
                doLater {
                    activityStream.clearSelection()
                    activityStream.ensureIndexIsVisible(0)
                }
            }
        }
    }
}
