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

/**
 * @author fortuna
 *
 */
 /*
@Grapes([
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
//    @Grab(group='com.miglayout', module='miglayout', version='3.7.2'),
//    @Grab(group='com.ocpsoft', module='ocpsoft-pretty-time', version='1.0.5'),
    @Grab(group='com.fifesoft.rsyntaxtextarea', module='rsyntaxtextarea', version='1.4.0')])
    */
public class Coucou{
     
    static final LogAdapter log = new Slf4jAdapter(LoggerFactory.getLogger(Coucou.class))
    static final LogEntry init_node = new FormattedLogEntry(Level.Info, 'Initialising %s node..')
    static final LogEntry delete_enabled = new FormattedLogEntry(Level.Info, 'Enable deletion for: %s')
    static final LogEntry delete_disabled = new FormattedLogEntry(Level.Info, 'Disable deletion')
    static final LogEntry unexpected_error = new FormattedLogEntry(Level.Error, 'An unexpected error has occurred')
    
    static final def EMPTY_TABLE_MODEL = new DefaultTableModel()
     
    static void close(def frame, def exit) {
        if (exit) {
            System.exit(0)
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
         
        //System.setProperty("org.apache.jackrabbit.repository.home", new File(System.getProperty("user.home"), ".coucou/data").absolutePath)
        //System.setProperty("org.apache.jackrabbit.repository.conf", Coucou.class.getResource("/config.xml").file)
        
        def repoConfig = RepositoryConfig.create(Coucou.class.getResource("/config.xml").toURI(), new File(System.getProperty("user.home"), ".coucou/data").absolutePath)
        def repository = new TransientRepository(repoConfig)
        
        def session = repository.login(new SimpleCredentials('admin', ''.toCharArray()))
        Runtime.getRuntime().addShutdownHook(new SessionLogout(session))
//        def jcr = new JcrBuilder()
//        jcr.session = session
//        jcr.log = log

        def initNode = { name ->
            if (!session.rootNode.hasNode(name)) {
                log.log init_node, name
                session.rootNode.addNode(name)
                session.rootNode.save()
            }
        }
        
        initNode('accounts')
        initNode('contacts')
        initNode('planner')
        initNode('history')
        initNode('archive')
        initNode('presence')
        initNode('notes')
        
        def getNode = { path ->
            if (!session.nodeExists(path)) {
//                log.log init_node, path
                return session.rootNode.addNode(path[1..-1])
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
        
        def editContext = new EditContext()
        
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
            if (tabs.tabCount > 0) {
                for (i in 0..tabs.tabCount - 1) {
                    if (tabs.getComponentAt(i).getClientProperty('coucou.node') == node) {
                        return tabs.getComponentAt(i)
                    }
                }
            }
            return null
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
                                entryList.model = new RepositoryListModel(node)
                                entryList.cellRenderer = new RepositoryListCellRenderer()
                            }
                            scrollPane(constraints: 'right') {
                            }
                        }
                    }
                }
                newTab.putClientProperty(SubstanceLookAndFeel.TABBED_PANE_CLOSE_BUTTONS_PROPERTY, true)
                newTab.putClientProperty('coucou.node', node)
//                tabs.selectedIndex = tabs.tabCount - 1
                tabs.add newTab
//                tabs.selectedComponent = newTab
                doLater {
//                    tabs.actionMap.get('navigateNext').actionPerformed(new ActionEvent(tabs, ActionEvent.ACTION_PERFORMED, ''))
                    def tabComponent = tabs.getTabComponentAt(tabs.tabCount - 1)
                    tabComponent.scrollRectToVisible(new Rectangle(0, 0, tabComponent.width, tabComponent.height))
                }
            }
        }
        
        def openExplorerTab = { tabs, node ->
            if (tabs.tabCount > 0) {
                for (i in 0..tabs.tabCount - 1) {
                    if (tabs.getComponentAt(i).getClientProperty('coucou.node') == node) {
                        tabs.selectedComponent = tabs.getComponentAt(i)
                        return
                    }
                }
            }
            
            swing.edt {
                def explorerTab = panel(name: 'Repository Explorer', border: emptyBorder(10)) {
                    borderLayout()
                    splitPane(orientation: JSplitPane.VERTICAL_SPLIT, dividerLocation: 200, continuousLayout: true) {
                        scrollPane(constraints: 'left') {
                            treeTable(id: 'explorerTree')
                            explorerTree.treeTableModel = new RepositoryTreeTableModel(node)
                            explorerTree.selectionModel.selectionMode = ListSelectionModel.SINGLE_SELECTION
                            explorerTree.selectionModel.valueChanged = {
                                def selectedPath = explorerTree.getPathForRow(explorerTree.selectedRow)
                                if (selectedPath) {
                                    propertyTable.model = new PropertiesTableModel(selectedPath.lastPathComponent)
                                }
                                else {
                                    propertyTable.model = EMPTY_TABLE_MODEL
                                }
                            }
                            explorerTree.packAll()
                        }
                        scrollPane(constraints: 'right') {
                            table(showHorizontalLines: false, id: 'propertyTable')
                            propertyTable.addHighlighter(simpleStripingHighlighter(stripeBackground: HighlighterFactory.GENERIC_GRAY))
                        }
                    }
                }
            
                explorerTab.putClientProperty(SubstanceLookAndFeel.TABBED_PANE_CLOSE_BUTTONS_PROPERTY, true)
                explorerTab.putClientProperty('coucou.node', node)
                tabs.add explorerTab
                tabs.selectedComponent = explorerTab
                
                def iconSize = new Dimension(16, 16)
                def taskIcon = SvgBatikResizableIcon.getSvgIcon(Coucou.class.getResource('/task.svg'), iconSize)
                tabs.setIconAt(tabs.indexOfComponent(explorerTab), taskIcon)
            }
        }
        
        def openFeedView = { tabs, node ->
            def tab = getTabForNode(tabs, node)
            if (tab) {
                tabs.selectedComponent = tab
                return
            }
            
            swing.edt {
                def feedView = panel(name: node.getProperty('title').value.string, border: emptyBorder(10)) {
                    borderLayout()
                    splitPane(orientation: JSplitPane.VERTICAL_SPLIT, dividerLocation: 200, continuousLayout: true) {
                        def contentView = null
                        scrollPane(constraints: 'left') {
//                            def entryList = list()
//                            entryList.model = new RepositoryListModel(node)
//                            entryList.cellRenderer = new FeedViewListCellRenderer()
                            def entryList = table(showHorizontalLines: false)
                            entryList.addHighlighter(simpleStripingHighlighter(stripeBackground: HighlighterFactory.GENERIC_GRAY))
                            entryList.model = new FeedTableModel(node)
                            entryList.selectionModel.valueChanged = { e ->
                                if (!e.valueIsAdjusting) {
//                                if (entryList.selectedValue && entryList.selectedValue.hasProperty('description')) {
//                                    contentView.text = entryList.selectedValue.getProperty('description').value.string
                                    if (entryList.selectedRow >= 0) {
                                        def entries = node.nodes
                                        entries.skip(entryList.convertRowIndexToModel(entryList.selectedRow))
                                        def entry = entries.nextNode()
                                        if (entry.hasProperty('description')) {
//                                        println "Entry selected: ${entryList.model[entryList.selectedRow]}"
                                            def content = entry.getProperty('description').value.string.replaceAll(/(http:\/\/)?([a-zA-Z0-9\-.]+\.[a-zA-Z0-9\-]{2,}([\/]([a-zA-Z0-9_\/\-.?&%=+])*)*)(\s+|$)/, '<a href="http://$2">$2</a> ')
                                            contentView.text = content
                                            contentView.caretPosition = 0
                                        }
                                        else {
                                            contentView.text = null
                                        }
                                    }
                                    else {
                                        contentView.text = null
                                    }
                                }
                            }
                            entryList.mouseClicked = { e ->
                                if (e.button == MouseEvent.BUTTON1 && e.clickCount >= 2 && entryList.selectedRow >= 0) {
//                                    if (entryList.selectedRow >= 0 && entryList.model[entryList.selectedRow].hasProperty('link')) {
//                                        Desktop.desktop.browse(URI.create(entryList.model[entryList.selectedRow].getProperty('link').value.string))
//                                    }
                                    def entries = node.nodes
                                    entries.skip(entryList.convertRowIndexToModel(entryList.selectedRow))
                                    def entry = entries.nextNode()
                                    if (entry.hasProperty('link')) {
                                        Desktop.desktop.browse(URI.create(entry.getProperty('link').value.string))
                                    }
                                }
                            }
                            entryList.packAll()
                        }
                        scrollPane(constraints: 'right') {
                            contentView = editorPane(editable: false, contentType: 'text/html')
                            contentView.addHyperlinkListener(new HyperlinkListenerImpl())
                        }
                    }
                }
                feedView.putClientProperty(SubstanceLookAndFeel.TABBED_PANE_CLOSE_BUTTONS_PROPERTY, true)
                feedView.putClientProperty('coucou.node', node)
                tabs.add feedView
                tabs.selectedComponent = feedView
                
                def iconSize = new Dimension(16, 16)
                def feedIcon = SvgBatikResizableIcon.getSvgIcon(Coucou.class.getResource('/feed.svg'), iconSize)
                tabs.setIconAt(tabs.indexOfComponent(feedView), feedIcon)
            }
        }
        
        def updateFeed = { url ->
            // rome uses Thread.contextClassLoader..
            Thread.currentThread().contextClassLoader = Coucou.class.classLoader
            
            def feed = new SyndFeedInput().build(new XmlReader(new URL(url)))
//                                             def newNode = session.rootNode.getNode('feeds').addNode(Text.escapeIllegalJcrChars(newFeed.title))
            def feedNode = getNode("/feeds/${Text.escapeIllegalJcrChars(feed.title)}")
            feedNode.setProperty('url', url)
            feedNode.setProperty('title', feed.title)
            if (feed.link) {
                feedNode.setProperty('source', feed.link)
            }
//            if (feed.uri) {
//                feedNode.setProperty('uri', feed.uri)
//            }
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
                if (feed.link) {
                    entryNode.setProperty('source', feed.link)
                }
                entryNode.setProperty('title', entry.title)
                if (entry.description) {
                    entryNode.setProperty('description', entry.description.value)
                }
                
                // entry link..
                if (entry.uri) {
                    try {
                        new URL(entry.uri)
                        entryNode.setProperty('link', entry.uri)
                    }
                    catch (Exception e) {
                        // not a valid url..
                    }
                }
                if (entry.link) {
                    try {
                        new URL(entry.link)
                        entryNode.setProperty('link', entry.link)
                    }
                    catch (Exception e) {
                        // not a valid url..
                    }
                }
                
                def calendar = Calendar.instance
                if (entry.publishedDate) {
                    calendar.setTime(entry.publishedDate)
                }
                entryNode.setProperty('date', calendar)
            }
            saveNode feedNode
            return feedNode
        }
        
         swing.edt {
             lookAndFeel('substance5', 'system')

//             def helpIcon = SvgBatikResizableIcon.getSvgIcon(Coucou.class.getResource('/im.svg'), new java.awt.Dimension(20, 20))
             
             frame(title: 'Coucou', id: 'coucouFrame', defaultCloseOperation: JFrame.DO_NOTHING_ON_CLOSE,
                     size: [640, 480], show: false, locationRelativeTo: null, iconImage: imageIcon('/logo.png', id: 'logoIcon').image) {
                
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

                    action(id: 'closeTabAction', name: 'Close Tab', accelerator: shortcut('W'))
                    action(id: 'closeAllTabsAction', name: 'Close All Tabs', accelerator: shortcut('shift W'))
                    action(id: 'printAction', name: 'Print', accelerator: shortcut('P'))
                    
                    action(id: 'importAction', name: 'Import..', closure: {
                        if (chooser.showOpenDialog() == JFileChooser.APPROVE_OPTION) {
                            doLater {
                                def opml = new XmlSlurper().parse(chooser.selectedFile)
                                def feeds = opml.body.outline.outline.collect { it.@xmlUrl.text() }
                                println "Feeds: ${feeds}"
                                for (feed in feeds) {
                                    try {
                                        updateFeed(feed)
                                    }
                                    catch (Exception e) {
                                        def error = new ErrorInfo('Import Error', 'An error occurred importing feed',
                                                '<html><body>Error importing feed: ${feed}</body></html>', null, e, null, ['feed': feed])
                                        JXErrorPane.showDialog(coucouFrame, error);
                                    }
                                }
                            }
                               //def tab = newTab(chooser.selectedFile)
                               //tabs.add(tab)
                               //tabs.setIconAt(tabs.indexOfComponent(tab), FileSystemView.fileSystemView.getSystemIcon(chooser.selectedFile))
                               //tabs.selectedComponent = tab
//                               openTab(tabs, chooser.selectedFile)
//                            }
                        }
                    })
                    
                    action(id: 'exitAction', name: 'Exit', smallIcon: imageIcon('/exit.png'), accelerator: shortcut('Q'), closure: { close(coucouFrame, true) })
                    
                    action(id: 'deleteAction', name: 'Delete', accelerator: 'DELETE', enabled: bind(source: editContext, sourceProperty: 'enabled'), closure: {
                         if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(coucouFrame, "Delete item: ${editContext.item}?", 'Confirm delete', JOptionPane.OK_CANCEL_OPTION)) {
                             editContext.delete()
                         }
                     })
                    
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
                    
                    action(id: 'replyAction', name: 'Reply', accelerator: shortcut('R'))
                    action(id: 'replyAllAction', name: 'Reply All', accelerator: shortcut('shift R'))
                    action(id: 'forwardAction', name: 'Forward', accelerator: shortcut('F'))
                    action(id: 'logoutAction', name: 'Logout', closure: { session.logout() })
                    action(id: 'repositoryExplorerAction', name: 'Repository Explorer', closure: { openExplorerTab(tabs, session.rootNode) })
                }
                
                fileChooser(id: 'chooser')
                fileChooser(id: 'imageChooser', fileFilter: new ImageFileFilter())
                
                tipOfTheDay(id: 'tips', model: defaultTipModel(tips: [
                    defaultTip(name: 'test', tip: '<html><em>testing</em>')
                ]))
                
                menuBar() {
                    menu(text: "File", mnemonic: 'F') {
                        menuItem(closeTabAction)
                        menuItem(text: "Close Other Tabs")
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
                        separator()
                        menuItem(text: "Mail Filters")
                        menuItem(text: "Accounts")
                        menuItem(text: "Preferences", icon: imageIcon('/preferences.png'))
                    }
                    menu(text: "View", mnemonic: 'V') {
                        checkBoxMenuItem(text: "Presence Bar", id: 'viewPresenceBar', state: true)
                        checkBoxMenuItem(text: "Status Bar", id: 'viewStatusBar')
                        checkBoxMenuItem(text: "Contact Groups", id: 'viewContactGroups')
                    }
                    menu(text: "Action", mnemonic: 'A') {
                        menuItem(replyAction)
                        menuItem(replyAllAction)
                        menuItem(forwardAction)
                    separator()
                    menu(text: "Tag") {
                        menuItem(text: "New Tag..")
                    }
                    separator()
                        menuItem(text: "Flag")
                        menuItem(text: "Annotate")
                    }
                    menu(text: "Tools", mnemonic: 'T') {
                        menu(text: "Search") {
                            menuItem(text: "New Search..")
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
                
                panel(id: 'presencePane', constraints: BorderLayout.NORTH, border: emptyBorder(10)) {
//                    flowLayout(alignment: FlowLayout.LEADING)
                    borderLayout()
                    
                    button(constraints: BorderLayout.WEST, id: 'photoButton', icon: imageIcon(imageIcon('/avatar.png').image.getScaledInstance(50, 50, Image.SCALE_SMOOTH)), focusPainted: false, toolTipText: 'Click to change photo') //, minimumSize: new Dimension(50, 50))
                    photoButton.actionPerformed = {
                            if (imageChooser.showOpenDialog() == JFileChooser.APPROVE_OPTION) {
                                doLater {
                                   photoButton.icon = imageIcon(imageIcon(imageChooser.selectedFile.absolutePath).image.getScaledInstance(50, -1, Image.SCALE_SMOOTH))
                                }
                            }
                    }
                    
                    vbox(border: emptyBorder(5)) {
                        textField(id: 'nameField', text: System.getProperty('user.name', '<Enter your name here>'), border: emptyBorder(1), font: new Font('Arial', Font.PLAIN, 16))
                        nameField.focusGained = { nameField.selectAll() }
                        
                        //textField(id: 'statusField', text: '<Enter your status here>', border: emptyBorder(1), font: new Font('Arial', Font.PLAIN, 14))
                        comboBox(id: 'statusField', editable: true, border: lineBorder(color: new Color(230, 230, 230), thickness: 2, roundedCorners: true), font: new Font('Arial', Font.PLAIN, 12))
                        statusField.putClientProperty(org.jvnet.lafwidget.LafWidget.TEXT_SELECT_ON_FOCUS, true)
                        //statusField.focusGained = { nameField.selectAll() }
                        
                        /*
                        def statusModel = new DefaultComboBoxModel()
                        statusModel.addElement('Available')
                        statusModel.addElement('Busy')
                        statusModel.addElement('Away')
                        statusField.model = statusModel
                        */
                        statusField.model = new RepositoryComboBoxModel(session.rootNode.getNode('presence'))
                        statusField.renderer = new RepositoryListCellRenderer()
                    }
                }
                bind(source: viewPresenceBar, sourceProperty:'selected', target: presencePane, targetProperty:'visible')
                
//                splitPane(id: 'splitPane', oneTouchExpandable: true, dividerLocation: 1.0) {
                tabbedPane(tabLayoutPolicy: JTabbedPane.SCROLL_TAB_LAYOUT, id: 'tabs') {
                    panel(name: 'Home', id: 'homeTab') {
                         borderLayout()
                         splitPane(id: 'splitPane', oneTouchExpandable: true, dividerLocation: 1.0, continuousLayout: true) {
                             tabbedPane(constraints: 'left', tabPlacement: JTabbedPane.BOTTOM, id: 'navTabs') {
                                 panel(name: 'Contacts', border: emptyBorder(10)) {
                                     borderLayout()
                                     
//                                     vbox {
                                         /*
                                         def findFilter = new PatternFilter()
                                     
                                         def findText = 'Find a contact..'
                                         textField(text: findText, id: 'findField', foreground: Color.LIGHT_GRAY, border: null, constraints: BorderLayout.NORTH)
                                         findField.focusGained = {
                                             if (findField.text == findText) {
                                                 findField.text = null
                                             }
                                         }
                                         findField.focusLost = {
                                             if (!findField.text) {
                                                 findField.text = findText
                                             }
                                         }
                                         findField.keyPressed = { e ->
                                             if (e.keyCode == KeyEvent.VK_ESCAPE) {
                                                 findField.text = null
                                             }
                                         }
                                         findField.document.addDocumentListener(new FindFilterUpdater(findField, findFilter))
                                         */
                                         textField(new FindField(defaultText: 'Filter contacts..'), id: 'findField', foreground: Color.LIGHT_GRAY, border: emptyBorder(5), constraints: BorderLayout.NORTH)
                                         findField.toolTipText = '<CTRL> + Enter to create a new contact'
                                         
//                                         titledSeparator(title: 'Online Contacts', font: new Font('Arial', Font.PLAIN, 14), foreground: Color.WHITE)
//                                         label(text: 'Online Contacts', font: new Font('Arial', Font.PLAIN, 14), horizontalTextPosition: SwingConstants.LEFT, foreground: Color.WHITE)
                                     
//                                         titledSeparator(title: 'Saved Contacts', font: new Font('Arial', Font.PLAIN, 14), foreground: Color.WHITE)
                                     
                                         scrollPane(horizontalScrollBarPolicy: JScrollPane.HORIZONTAL_SCROLLBAR_NEVER, border: null) {
//                                             list(id: 'contactsList')
//                                             contactsList.model = new RepositoryListModel(session.rootNode.getNode('contacts'))
//                                             contactsList.cellRenderer = new RepositoryListCellRenderer()

                                                def contactIcon = SvgBatikResizableIcon.getSvgIcon(Coucou.class.getResource('/im.svg'), new java.awt.Dimension(20, 20))
                                                def contactGrid = new JCommandButtonPanel(50)
                                                contactGrid.addButtonGroup('Online')
                                                contactGrid.addButtonGroup('Offline')
                                                /*
                                                try {
//                                                    final XMPPConnection connection = new XMPPConnection(new ConnectionConfiguration("talk.google.com", 5222, "gmail.com"))
                                                    final XMPPConnection connection = new XMPPConnection("basepatterns.org");
                                                    connection.connect();
                                                    SASLAuthentication.supportSASLMechanism("PLAIN", 0)
//                                                    connection.login("user@gmail.com", "password");
                                                    connection.login("test", "!password");
                                                    
                                                    for (group in connection.roster.groups) {
                                                        for (entry in group.entries) {
                                                            println "${group.name}: ${entry.name}"
                                                            Icon icon = null
//                                                            try {
//                                                                VCard card = new VCard()
//                                                                card.load(connection, entry.user)
//                                                                icon = new ImageIcon(card.avatar)
//                                                            }
//                                                            catch (Exception e) {
//                                                                log.log unexpected_error, e
//                                                            }
                                                            contactGrid.addButtonToGroup(group.name, button(text: entry.name, icon: icon))
                                                        }
                                                    }
                                                } catch (XMPPException ex) {
                                                    log.log unexpected_error, ex
                                                }
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
//                                         vglue()
//                                     }
                                     
//                                     hbox(constraints: BorderLayout.SOUTH) {
//                                         hglue()
//                                         hyperlink(new JXHyperlink(newContactAction))
//                                     }
                                 }
                                 panel(name: 'Planner', border: emptyBorder(10)) {
                                     borderLayout()
                                     
//                                     def addTaskEventText = 'Find a task / appointment..'
//                                     textField(text: addTaskEventText, id: 'findTaskField', foreground: Color.LIGHT_GRAY, border: null, constraints: BorderLayout.NORTH)
                                     textField(new FindField(defaultText: 'Filter tasks / appointments..'), id: 'findTaskField', foreground: Color.LIGHT_GRAY, border: emptyBorder(5), constraints: BorderLayout.NORTH)
                                     findTaskField.toolTipText = '<CTRL> + Enter to create a new task / appointment'
                                     
                                     scrollPane() {
                                        treeTable(id: 'plannerTree', columnControlVisible: true)
                                        getNode('/planner/Today')
                                        getNode('/planner/Tomorrow')
                                        getNode('/planner/This Week')
                                        getNode('/planner/Next Week')
                                        getNode('/planner/This Month')
                                        getNode('/planner/Overdue')
                                        getNode('/planner/Deleted')
                                        plannerTree.treeTableModel = new PlannerTreeTableModel(getNode('/planner'))
                                        plannerTree.selectionModel.selectionMode = ListSelectionModel.SINGLE_SELECTION
                                        plannerTree.packAll()
                                     }
                                 }
                                 panel(name: 'History', border: emptyBorder(10)) {
                                     borderLayout()
//                                     label(text: 'Folders', constraints: BorderLayout.NORTH, font: new Font('Arial', Font.PLAIN, 14), foreground: Color.WHITE, background: Color.GRAY, opaque: true)
                                     scrollPane() {
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
                                        treeTable(id: 'historyTree', columnControlVisible: true)
                                        getNode('/history/Inbox')
                                        getNode('/history/Sent')
                                        getNode('/history/Drafts')
                                        getNode('/history/Templates')
                                        getNode('/history/Templates/Mail')
                                        getNode('/history/Templates/Meeting')
                                        getNode('/history/Templates/Task')
                                        getNode('/history/Deleted')
                                        historyTree.treeTableModel = new HistoryTreeTableModel(getNode('/history'))
                                        historyTree.selectionModel.selectionMode = ListSelectionModel.SINGLE_SELECTION
//                                        historyTree.selectionModel.valueChanged = {
                                        historyTree.packAll()
                                     }
                                 }
                                 panel(name: 'Feeds', border: emptyBorder(10)) {
                                     borderLayout()
                                     
//                                     def addFeedText = 'Add a new feed..'
//                                     textField(text: addFeedText, id: 'addFeedField', foreground: Color.LIGHT_GRAY, border: null, constraints: BorderLayout.NORTH)
                                     textField(new FindField(defaultText: 'Filter feeds..'), id: 'findFeedField', foreground: Color.LIGHT_GRAY, border: emptyBorder(5), constraints: BorderLayout.NORTH)
                                     findFeedField.focusGained = { findFeedField.selectAll() }
                                     findFeedField.keyReleased = {
                                         if (findFeedField.text) {
                                             feedList.rowFilter = RowFilter.regexFilter("(?i)\\Q${findFeedField.text}\\E")
                                         }
                                         else {
                                             feedList.rowFilter = null
                                         }
                                     }
                                     findFeedField.actionPerformed = {
                                         if (findFeedField.text) {
                                             /*
                                             def newFeed = new SyndFeedInput().build(new XmlReader(new URL(findFeedField.text)))
//                                             def newNode = session.rootNode.getNode('feeds').addNode(Text.escapeIllegalJcrChars(newFeed.title))
                                             def newNode = getNode("/feeds/${Text.escapeIllegalJcrChars(newFeed.title)}")
                                             newNode.setProperty('title', newFeed.title)
                                             if (newFeed.link) {
                                                 newNode.setProperty('url', newFeed.link)
                                             }
                                             for (entry in newFeed.entries) {
                                                 def entryNode = getNode("${newNode.path}/${Text.escapeIllegalJcrChars(entry.uri)}")
                                                 if (newFeed.uri) {
                                                     entryNode.setProperty('source', newFeed.uri)
                                                 }
                                                 entryNode.setProperty('title', entry.title)
                                                 if (entry.description) {
                                                     entryNode.setProperty('description', entry.description.value)
                                                 }
                                                 if (entry.link) {
                                                     entryNode.setProperty('url', entry.link)
                                                 }
                                                 if (entry.publishedDate) {
                                                     calendar = Calendar.instance
                                                     calendar.setTime(entry.publishedDate)
                                                     entryNode.setProperty('date', calendar)
                                                 }
                                             }
                                             newNode.parent.save()
                                             */
                                             def feedNode = null
                                             def feedUrl
                                             try {
                                                 feedUrl = new URL(findFeedField.text)
                                             }
                                             catch (MalformedURLException e) {
                                                 try {
                                                     feedUrl = new URL("http://${findFeedField.text}")
                                                 }
                                                 catch (MalformedURLException e1) {
                                                     JOptionPane.showMessageDialog(coucouFrame, "Invalid URL: ${findFeedField.text}")
                                                 }
                                             }
                                             if (feedUrl) {
                                                 try {
                                                     feedNode = updateFeed(findFeedField.text)
                                                 }
                                                 catch (Exception e) {
                                                      html = new XmlSlurper(new org.ccil.cowan.tagsoup.Parser()).parse(feedUrl.content)
                                                      def feeds = html.head.link.findAll { it.@type == 'application/rss+xml' || it.@type == 'application/atom+xml' }
                                                      println "Found ${feeds.size()} feeds: ${feeds.collect { it.@href.text() } }"
                                                      if (!feeds.isEmpty()) {
                                                          feedNode = updateFeed(new URL(feedUrl, feeds[0].@href.text()).toString())
                                                      }
                                                      else {
                                                          JOptionPane.showMessageDialog(coucouFrame, "No feeds found for site: ${findFeedField.text}")
                                                      }
                                                 }
                                             }
                                             findFeedField.text = null
                                             if (feedNode) {
                                                 openFeedView(tabs, feedNode)
                                             }
                                         }
                                     }
                                     scrollPane(border: null) {
//                                         list(id: 'feedList')
//                                         feedList.model = new RepositoryListModel(getNode('/feeds'))
//                                         feedList.cellRenderer = new FeedViewListCellRenderer()
                                         table(showHorizontalLines: false, id: 'feedList', columnControlVisible: true)
                                         feedList.addHighlighter(simpleStripingHighlighter(stripeBackground: HighlighterFactory.GENERIC_GRAY))
                                         feedList.model = new FeedTableModel(getNode('/feeds'))
                                         feedList.selectionModel.valueChanged = { e ->
                                             if (!e.valueIsAdjusting) {
                                             if (feedList.selectedRow >= 0) {
                                                 def feeds = getNode('/feeds').nodes
                                                 feeds.skip(feedList.convertRowIndexToModel(feedList.selectedRow))
                                                 def feed = feeds.nextNode()
                                                 editContext.item = feed
                                                 editContext.enabled = true
                                                 log.log delete_enabled, editContext.item
                                             }
                                             else {
                                                 editContext.item = null
                                                 editContext.enabled = false
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
                                     }
                                 }
                                 panel(name: 'Accounts', border: emptyBorder(10)) {
                                     borderLayout()
                                     vbox(constraints: BorderLayout.NORTH) {
//                                         def findAccountText = 'Find an account..'
//                                         label(text: 'Accounts', font: new Font('Arial', Font.PLAIN, 14), foreground: Color.WHITE)
                                         //searchPanel(fieldName: 'Filter accounts', id: 'findAccountField')
//                                         textField(text: addTaskEventText, id: 'findTaskField', foreground: Color.LIGHT_GRAY, border: null, constraints: BorderLayout.NORTH)
                                         textField(new FindField(defaultText: 'Filter accounts..'), id: 'findAccountField', foreground: Color.LIGHT_GRAY, border: emptyBorder(5))
                                         findAccountField.toolTipText = '<CTRL> + Enter to create a new account'
                                     }
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
                                 panel(name: 'Notes', border: emptyBorder(10)) {
                                     borderLayout()
                                     textField(new FindField(defaultText: 'Filter notes..'), id: 'notesFilterField', foreground: Color.LIGHT_GRAY, border: emptyBorder(5), constraints: BorderLayout.NORTH)
                                     notesFilterField.focusGained = { notesFilterField.selectAll() }
                                     notesFilterField.keyReleased = {
                                         if (notesFilterField.text) {
                                             noteList.rowFilter = RowFilter.regexFilter("(?i)\\Q${notesFilterField.text}\\E")
                                         }
                                         else {
                                             noteList.rowFilter = null
                                         }
                                     }
                                     notesFilterField.actionPerformed = {
                                         if (notesFilterField.text) {
                                             // create a new note here..
                                         }
                                     }
                                     scrollPane(border: null) {
                                         table(showHorizontalLines: false, id: 'noteList')
                                         noteList.addHighlighter(simpleStripingHighlighter(stripeBackground: HighlighterFactory.GENERIC_GRAY))
                                         noteList.model = new NoteTableModel(getNode('/notes'))
                                     }
                                 }                                 
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
                             }
                             navTabs.putClientProperty(SubstanceLookAndFeel.TABBED_PANE_CONTENT_BORDER_KIND, SubstanceConstants.TabContentPaneBorderKind.SINGLE_FULL)
                             panel(constraints: 'right', border: emptyBorder(10)) {
                                 borderLayout()
                                 scrollPane(horizontalScrollBarPolicy: JScrollPane.HORIZONTAL_SCROLLBAR_NEVER, border: null) {
                                     list(id: 'activity')
                                     activity.cellRenderer = new ActivityListCellRenderer()
                                     activity.addHighlighter(simpleStripingHighlighter(stripeBackground: HighlighterFactory.GENERIC_GRAY))
                                     activity.mouseClicked = { e ->
                                         if (e.button == MouseEvent.BUTTON1 && e.clickCount >= 2) {
                                             if (activity.selectedValue) {
                                                 def node = activity.selectedValue
                                                 openNodeTab(tabs, node)
                                             }
                                         }
                                     }
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
                                     def activityModel = new DefaultListModel()
                                     activity.model = activityModel
           
                                     // activity stream..
                                     session.workspace.observationManager.addEventListener(new ActivityStreamUpdater(model: activityModel, session: session, swing: swing), Event.NODE_ADDED, '/', true, null, null, false)
                                 }
                             }
                         }
                     }
                 }
                tabs.setIconAt(tabs.indexOfComponent(homeTab), new PaddedIcon(imageIcon('/logo-12.png'), 14, 18))
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
                 TrayIcon trayIcon = new TrayIcon(imageIcon('/logo-14.png').image, 'Coucou')
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
             }
             
//             coucouFrame.ribbon.configureHelp(helpIcon, onlineHelpAction)
             
//           TrackerRegistry.instance.register(coucouFrame, 'coucouFrame');
           coucouFrame.windowClosing = {
               close(coucouFrame, !SystemTray.isSupported())
           }
           
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
                       /*
                       def feedSource = feedNode.getProperty('url').value.string
                       def feed = new SyndFeedInput().build(new XmlReader(new URL(feedSource)))
                        for (entry in feed.entries) {
                            def entryNode = getNode("${feedNode.path}/${Text.escapeIllegalJcrChars(entry.uri)}")
                            if (feed.uri) {
                                entryNode.setProperty('source', feed.uri)
                            }
                            entryNode.setProperty('title', entry.title)
                            if (entry.description) {
                                entryNode.setProperty('description', entry.description.value)
                            }
                            if (entry.link) {
                                entryNode.setProperty('url', entry.link)
                            }
                            if (entry.publishedDate) {
                                calendar = Calendar.instance
                                calendar.setTime(entry.publishedDate)
                                entryNode.setProperty('date', calendar)
                            }
                        }
                        */
                        println "Updating feed: ${feedNode.getProperty('title').value.string}"
                        try {
                            updateFeed(feedNode.getProperty('url').value.string)
                        }
                        catch (Exception e) {
                            log.log unexpected_error, e
                        }
                    }
                }
//                feedNode.parent.save()
               
           } as Runnable, 0, 30, TimeUnit.MINUTES)
           
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

class FindFilterUpdater implements DocumentListener {

    def findField
    
    def findFilter
    
    public FindFilterUpdater(def field, def filter) {
        findField = field
        findFilter = filter
    }
    
    void insertUpdate(DocumentEvent e) {
        if (findField.text) {
            findFilter.setPattern("\\Q${findField.text}\\E", Pattern.CASE_INSENSITIVE)
        }
        else {
            findFilter.pattern = null
        }
    }
    
    void removeUpdate(DocumentEvent e) {
        if (findField.text) {
            findFilter.setPattern("\\Q${findField.text}\\E", Pattern.CASE_INSENSITIVE)
        }
        else {
            findFilter.pattern = null
        }
    }
    
    void changedUpdate(DocumentEvent e) {
        if (findField.text) {
            findFilter.setPattern("\\Q${findField.text}\\E", Pattern.CASE_INSENSITIVE)
        }
        else {
            findFilter.pattern = null
        }
    }
}
/*
class ImMessage {
    
    def sender
    def text
    def time
    
    public ImMessage(def text, def sender, def time) {
        this.text = text
        this.sender = sender
        this.time = time
    }
    
    String toString() {
        return "<html><table width=100%>" \
            + "<tr><td style='font-size:1em;font-weight:bold;color:silver;text-align:left'>${sender}</td></tr>" \
            + "<tr><td style='font-size:1em;text-align:left'>${text}</td></tr>" \
            + "<tr><td style='font-size:1em;font-style:italic;color:silver;text-align:left'>${new PrettyTime().format(time)}</td></tr></table></html>"
    }
}

class MailMessage {
    
    def sender
    def text
    def time
    def count
    
    public MailMessage(def text, def sender, def time, def count) {
        this.text = text
        this.sender = sender
        this.time = time
        this.count = count
    }
    
    String toString() {
        return "<html><table width=100%>" \
            + "<tr><td style='font-size:1em;font-weight:bold;color:silver;text-align:left'>${sender}</td><td style='font-size:1em;color:silver;text-align:right'>${count} Messages</td></tr>" \
            + "<tr><td colspan=2 style='font-size:1em;text-align:left'>${text}</td></tr>" \
            + "<tr><td colspan=2 style='font-size:1em;font-style:italic;color:silver;text-align:left'>${new PrettyTime().format(time)}</td></tr></table></html>"
    }
}

class EventMessage {
    
    def sender
    def text
    def time
    
    public EventMessage(def text, def sender, def time) {
        this.text = text
        this.sender = sender
        this.time = time
    }
    
    String toString() {
        return "<html><table width=100%>" \
            + "<tr><td style='font-size:1em;font-weight:bold;color:silver;text-align:left'>${sender}</td></tr>" \
            + "<tr><td style='font-size:1em;text-align:left'>${text}</td></tr>" \
            + "<tr><td style='font-size:1em;font-style:italic;color:silver;text-align:left'>${new PrettyTime().format(time)}</td></tr></table></html>"
    }
}

class TaskMessage {
    
    def sender
    def text
    def time
    
    public TaskMessage(def text, def sender, def time) {
        this.text = text
        this.sender = sender
        this.time = time
    }
    
    String toString() {
        return "<html><table width=100%>" \
            + "<tr><td style='font-size:1em;font-weight:bold;color:silver;text-align:left'>${sender}</td></tr>" \
            + "<tr><td style='font-size:1em;text-align:left'>${text}</td></tr>" \
            + "<tr><td style='font-size:1em;font-style:italic;color:silver;text-align:left'>${new PrettyTime().format(time)}</td></tr></table></html>"
    }
}

class WeatherMessage {
    
    def weatherNode
    def buildActivityString
    
    String toString() {
//        return "<html><table width=100%>" \
//            + "<tr><td style='font-size:1em;font-weight:bold;color:silver;text-align:left'>${weatherNode.channel.title}</td></tr>" \
//            + "<tr><td style='font-size:1em;text-align:left'>${weatherNode.channel.item.condition.@text}</td></tr>" \
//            + "<tr><td style='font-size:1em;font-style:italic;color:silver;text-align:left'>${new PrettyTime().format(Date.parse('EEE, dd MMM yyyy h:mm a', weatherNode.channel.item.condition.@date.toString().split(' ')[0..-2].join(' ')))}</td></tr></table></html>"
        return buildActivityString(weatherNode.channel.title,
             weatherNode.channel.item.condition.@text, Date.parse('EEE, dd MMM yyyy h:mm a',
             weatherNode.channel.item.condition.@date.toString().split(' ')[0..-2].join(' ')))
    }
}

class FeedMessage {
    def feedEntry
    def buildActivityString
    
    String toString() {
        return buildActivityString(feedEntry.source.title, feedEntry.title, feedEntry.publishedDate)
    }
}
*/
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
            accountsNode.save()
        }
        else if (data['emailAccount']) {
            if (!accountsNode.hasNode('mail')) {
                accountsNode.addNode('mail')
            }
            def node = accountsNode.addNode("mail/${data['emailAddressField']}")
            accountsNode.save()
        }
        return null
    }

    boolean cancel(Map settings) {
        return true;
    }
}

class RepositoryTreeModel extends AbstractTreeModel implements javax.jcr.observation.EventListener {

    static def log = Logger.getInstance(RepositoryTreeModel.class)
    
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
        fireContentsChanged(this, 0, getSize())
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

class EditContext {
    
    @Bindable Boolean enabled
    
    def item
    
    void delete() {
        if (item instanceof Node) {
            item.remove()
            item.parent.save()
        }
    }
    
    void copy() {}
    
    void cut() {}
    
    void paste() {}
}

class RepositoryTreeTableModel extends AbstractNodeTreeTableModel {

    RepositoryTreeTableModel(def node) {
        super(node, (String[]) ['Name', 'Type', 'State'])
    }
    
    Object getValueAt(Object node, int column) {
        def value
        switch(column) {
            case 0:
                value = node.name
                break
            case 1:
                value = node.primaryNodeType.name
                break
            case 2:
                value = node.isNew() ? 'N' : node.isModified() ? 'M' : null
                break
        }
        return value
    }
}

class PropertiesTableModel extends AbstractTableModel {
    
    def node
    def columnNames = ['Property Name', 'Type', 'Value']
    
    PropertiesTableModel(def node) {
        this.node = node
    }

    int getRowCount() {
        return node.properties.size
    }
    
    int getColumnCount() {
        return columnNames.size
    }
    
    String getColumnName(int column) {
        return columnNames[column]
    }
    
    Object getValueAt(int row, int column) {
        def props = node.properties
        props.skip(row)
        def prop = props.nextProperty()
        def value
        switch(column) {
            case 0:
                value = prop.name
                break
            case 1:
                if (prop.isMultiple()) {
                }
                else {
                    value = PropertyType.nameFromValue(prop.value.type)
                }
                break
            case 2:
                if (prop.isMultiple()) {
                }
                else {
                    value = prop.value.string
                }
                break
        }
        return value
    }
}

class FeedTableModel extends AbstractNodeTableModel {
    
    def df = new PrettyTime()
    
    FeedTableModel(def node) {
        super(node, (String[]) ['Title', 'Source', 'Last Updated'])
    }
    
    Object getValueAt(int row, int column) {
        def node = getNodeAt(row)
        def value
        switch(column) {
            case 0:
                value = node.getProperty('title').value.string
                break
            case 1:
                if (node.hasProperty('source')) {
                    value = node.getProperty('source').value.string
                }
                break
            case 2:
                if (node.hasProperty('date')) {
                    value = df.format(node.getProperty('date').value.date.time)
                }
                break
        }
        return value
    }
}

class FindField extends JTextField {

    def patternFilter
    def defaultText
    
    FindField() {
        patternFilter = new PatternFilter()
        focusGained = {
            if (text == defaultText) {
                text = null
            }
        }
        focusLost = {
            if (!text) {
                text = defaultText
            }
        }
        keyPressed = { e ->
            if (e.keyCode == KeyEvent.VK_ESCAPE) {
                text = null
            }
        }
        document.addDocumentListener(new FindFilterUpdater(this, patternFilter))
    }
}

class HyperlinkListenerImpl implements HyperlinkListener {

    /**
     * {@inheritDoc}
     */
    public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.eventType == HyperlinkEvent.EventType.ACTIVATED) {
            try {
                Desktop.getDesktop().browse(e.getURL().toURI());
            } catch (Exception ex) {
                JXErrorPane.showDialog(ex);
            }
        }
    }

}

class HistoryTreeTableModel extends AbstractNodeTreeTableModel {

    HistoryTreeTableModel(def node) {
        super(node, (String[]) ['Subject', 'From', 'Count', 'Last Updated'])
    }
    
    Object getValueAt(Object node, int column) {
        def value
        switch(column) {
            case 0:
                value = node.name
                break
            case 1:
                if (node.hasProperty('from')) {
                    value = node.hasProperty('from').value.string
                }
                break
            case 2:
                value = node.nodes.size
                break
            case 2:
                if (node.hasProperty('lastModified')) {
                    value = node.hasProperty('lastModified').value.date
                }
                break
        }
        return value
    }
}

class AccountTableModel extends AbstractNodeTableModel {
    
    AccountTableModel(def node) {
        super(node, (String[]) ['Account Name', 'Status'])
    }
    
    Object getValueAt(int row, int column) {
        def node = getNodeAt(row)
        def value
        switch(column) {
            case 0:
                if (node.hasProperty('accountName')) {
                    value = node.getProperty('accountName').value.string
                }
                else {
                    value = node.name
                }
                break
            case 1:
                if (node.hasProperty('status')) {
                    value = node.getProperty('status').value.string
                }
                break
        }
        return value
    }
}

class PlannerTreeTableModel extends AbstractNodeTreeTableModel {

    PlannerTreeTableModel(def node) {
        super(node, (String[]) ['Summary', 'Participants', 'Categories', 'Due'])
    }
    
    Object getValueAt(Object node, int column) {
        def value
        switch(column) {
            case 0:
                value = node.name
                break
            case 1:
                if (node.hasProperty('organiser')) {
                    value = node.hasProperty('organiser').value.string
                }
                break
            case 1:
                if (node.hasProperty('categories')) {
                    value = node.hasProperty('categories').value.date
                }
                break
            case 2:
                if (node.hasProperty('due')) {
                    value = node.hasProperty('due').value.date
                }
                break
        }
        return value
    }
}

class NoteTableModel extends AbstractNodeTableModel {
    
    NoteTableModel(def node) {
        super(node, (String[]) ['Subject', 'Last Modified'])
    }
    
    Object getValueAt(int row, int column) {
        def node = getNodeAt(row)
        def value
        switch(column) {
            case 0:
                value = node.name
                break
            case 1:
                if (node.hasProperty('lastModified')) {
                    value = node.getProperty('lastModified').value.date
                }
                break
        }
        return value
    }
}

class ActivityStreamUpdater implements javax.jcr.observation.EventListener {

    def session
    def model
    def swing

    void onEvent(EventIterator events) {
        println "Events fired: ${events.size}"
        while (events.hasNext()) {
            def event = events.nextEvent()
            println "Node added: ${event.path}"
            def node = session.getItem(event.path)
            swing.edt {
            	model.addElement node
            }
        }
    }
}
