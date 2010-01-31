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

import groovy.swing.SwingXBuilder
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Component
import java.awt.Desktop
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.Font
import java.awt.Image
import java.awt.Insets
import java.awt.event.KeyEvent
import java.awt.event.MouseEvent
import javax.swing.UIManager
import groovy.swing.LookAndFeelHelper
import java.awt.TrayIcon
import java.awt.PopupMenu
import java.awt.SystemTray
import java.awt.MenuItem
import java.util.regex.Pattern
import javax.swing.DefaultComboBoxModel
import javax.swing.JFrame
import javax.swing.JFileChooser
import javax.swing.JScrollPane
import javax.swing.JTabbedPane
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
import org.jvnet.flamingo.svg.SvgBatikResizableIcon
import org.jdesktop.swingx.JXHyperlink
import org.jdesktop.swingx.JXStatusBar
import org.jdesktop.swingx.JXStatusBar.Constraint
import org.jdesktop.swingx.decorator.PatternFilter
import org.jdesktop.swingx.decorator.HighlighterFactory
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
import javax.jcr.SimpleCredentials
import javax.jcr.observation.*
import org.apache.jackrabbit.core.TransientRepository
import net.miginfocom.swing.MigLayout
import org.mnode.base.desktop.AbstractTreeModel
import javax.swing.tree.TreePath
import org.apache.jackrabbit.core.config.RepositoryConfig
import javax.swing.tree.DefaultTreeCellRenderer
import javax.swing.JTree
import javax.swing.ListModel
import javax.swing.DefaultListCellRenderer
import javax.swing.event.ListDataListener
import org.apache.log4j.Logger

/**
 * @author fortuna
 *
 */
 /*
@Grapes([
    @Grab(group='org.codehaus.griffon.swingxbuilder', module='swingxbuilder', version='0.1.6'),
    @Grab(group='net.java.dev.substance', module='substance', version='5.3'),
    @Grab(group='net.java.dev.substance', module='substance-swingx', version='5.3'),
    //@Grab(group='org.swinglabs', module='swingx', version='0.9.2'),
    @Grab(group='org.mnode.base', module='base-views', version='0.0.1-SNAPSHOT'),
    @Grab(group='org.mnode.base', module='base-xmpp', version='0.0.1-SNAPSHOT'),
    @Grab(group='org.mnode.base', module='base-desktop', version='0.0.1-SNAPSHOT'),
    //@Grab(group='jgoodies', module='forms', version='1.0.5'),
    //@Grab(group='org.codehaus.griffon.flamingobuilder', module='flamingobuilder', version='0.2'),
    @Grab(group='net.java.dev.flamingo', module='flamingo', version='4.2'),
    @Grab(group='org.apache.xmlgraphics', module='batik-awt-util', version='1.7'),
    @Grab(group='org.apache.xmlgraphics', module='batik-swing', version='1.7'),
    @Grab(group='org.apache.xmlgraphics', module='batik-transcoder', version='1.7'),
    @Grab(group='net.java.dev.datatips', module='datatips', version='20091219'),
    @Grab(group='org.netbeans.wizard', module='wizard', version='0.998.1'),
    @Grab(group='javax.jcr', module='jcr', version='2.0'),
    //@Grab(group='org.apache.commons', module='commons-compress', version='1.0'),
    @Grab(group='org.apache.jackrabbit', module='jackrabbit-core', version='2.0.0'),
    //@Grab(group='org.apache.jackrabbit', module='jackrabbit-text-extractors', version='2.0.0'),
    @Grab(group='org.slf4j', module='slf4j-log4j12', version='1.5.8'),
    @Grab(group='com.miglayout', module='miglayout', version='3.7.2'),
    @Grab(group='com.fifesoft.rsyntaxtextarea', module='rsyntaxtextarea', version='1.4.0')])
    */
public class Coucou{
     
    static Logger log = Logger.getInstance(Coucou.class)
     
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
         LookAndFeelHelper.instance.addLookAndFeelAlias('seaglass', 'com.seaglasslookandfeel.SeaGlassLookAndFeel')
         
        //System.setProperty("org.apache.jackrabbit.repository.home", new File(System.getProperty("user.home"), ".coucou/data").absolutePath)
        //System.setProperty("org.apache.jackrabbit.repository.conf", Coucou.class.getResource("/config.xml").file)
        
        def repoConfig = RepositoryConfig.create(Coucou.class.getResource("/config.xml").toURI(), new File(System.getProperty("user.home"), ".coucou/data").absolutePath)
        def repository = new TransientRepository(repoConfig)
        
        def session = repository.login(new SimpleCredentials('admin', ''.toCharArray()))
        Runtime.getRuntime().addShutdownHook(new SessionLogout(session))
        
        if (!session.rootNode.hasNode('accounts')) {
            log.info 'Initialising accounts node..'
            def accountsNode = session.rootNode.addNode('accounts')
            session.rootNode.save()
        }
        if (!session.rootNode.hasNode('contacts')) {
            log.info 'Initialising contacts node..'
            def contactsNode = session.rootNode.addNode('contacts')
            session.rootNode.save()
        }
        if (!session.rootNode.hasNode('history')) {
            log.info 'Initialising history node..'
            def historyNode = session.rootNode.addNode('history')
            session.rootNode.save()
        }
        if (!session.rootNode.hasNode('archive')) {
            log.info 'Initialising archive node..'
            def archiveNode = session.rootNode.addNode('archive')
            session.rootNode.save()
        }
        
         def swing = new SwingXBuilder()

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

        def openFolderTab = { folder ->
             swing.panel(name: folder.getProperty('displayName').string)
        }
        
         swing.edt {
             lookAndFeel('seaglass', 'substance5', 'system')

             frame(title: 'Coucou', id: 'coucouFrame', defaultCloseOperation: JFrame.DO_NOTHING_ON_CLOSE,
                     size: [800, 600], show: false, locationRelativeTo: null, iconImage: imageIcon('/logo.png', id: 'logoIcon').image) {
                
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
                            textField(name: 'usernameField', columns: 20, constraints: 'wrap')
                            label(text: 'Password')
                            passwordField(name: 'passwordField', columns: 20)
                        }

                        def emailAccountDetails = new WizardPageImpl('emailAccountDetails', 'Provide Email Details')
                        emailAccountDetails.preferredSize = pageSize
                        emailAccountDetails.validation = {
                            null
                        }
                        emailAccountDetails.add panel(layout: new MigLayout('fill')) {
                            //gridLayout(cols: 2, rows: 2, vgap: 10)
                            label(text: 'Email Address')
                            textField(name: 'emailAddressField', columns: 20)
                            label(text: 'Password')
                            passwordField(name: 'passwordField', columns: 20)
                        }

                        def branchController = new WizardBranchControllerImpl(accountTypeSelect)
//                        branchController.keys = ['accountTypeSelect': []
                        branchController.wizards = [
                                'xmppAccount': WizardPage.createWizard("Add XMPP Account", (WizardPage[]) [xmppAccountDetails], new CreateAccountProducer(session.rootNode.getNode('accounts'))),
                                'emailAccount': WizardPage.createWizard("Add Email Account", (WizardPage[]) [emailAccountDetails])]
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
                            textField(name: 'firstNameField', id: 'firstNameField', columns: 20, constraints: 'wrap')
                            label(text: 'Last Name')
                            textField(name: 'lastNameField', id: 'lastNameField', columns: 20)
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
                        
                        Wizard wizard = WizardPage.createWizard("Add Contact", (WizardPage[]) [contactDetails]);
                        wizard.show();
                    })

                    action(id: 'closeTabAction', name: 'Close Tab', accelerator: shortcut('W'))
                    action(id: 'closeAllTabsAction', name: 'Close All Tabs', accelerator: shortcut('shift W'))
                    action(id: 'printAction', name: 'Print', accelerator: shortcut('P'))
                    action(id: 'exitAction', name: 'Exit', smallIcon: imageIcon('/exit.png'), accelerator: shortcut('Q'), closure: { close(coucouFrame, true) })
                    
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
                }
                
                fileChooser(id: 'chooser', fileFilter: new ImageFileFilter())
                
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
                        menuItem(text: "Import..")
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
                        menuItem(text: "Delete")
                        separator()
                        menuItem(text: "Mail Filters")
                        menuItem(text: "Accounts")
                        menuItem(text: "Preferences", icon: imageIcon('/preferences.png'))
                    }
                    menu(text: "View", mnemonic: 'V') {
                        checkBoxMenuItem(text: "Status Bar", id: 'viewStatusBar')
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
                        menuItem(logoutAction)
                    }
                    menu(text: "Help", mnemonic: 'H') {
                        menuItem(onlineHelpAction)
                        menuItem(showTipsAction)
                    separator()
                        menuItem(aboutAction)
                    }
                }
                
                borderLayout()
                
                panel(id: 'presencePane', constraints: BorderLayout.NORTH, border: emptyBorder(5)) {
                    flowLayout(alignment: FlowLayout.LEADING)
                    
                    button(id: 'photoButton', icon: imageIcon(imageIcon('/avatar.png').image.getScaledInstance(50, 50, Image.SCALE_SMOOTH)), focusPainted: false, toolTipText: 'Click to change photo') //, minimumSize: new Dimension(50, 50))
                    photoButton.actionPerformed = {
                            if (chooser.showOpenDialog() == JFileChooser.APPROVE_OPTION) {
                                doLater {
                                   photoButton.icon = imageIcon(imageIcon(chooser.selectedFile.absolutePath).image.getScaledInstance(50, -1, Image.SCALE_SMOOTH))
                                }
                            }
                    }
                    
                    vbox() {
                        textField(id: 'nameField', text: '<Enter your name here>', border: emptyBorder(1), font: new Font('Arial', Font.PLAIN, 24))
                        nameField.focusGained = { nameField.selectAll() }
                        
                        //textField(id: 'statusField', text: '<Enter your status here>', border: emptyBorder(1), font: new Font('Arial', Font.PLAIN, 14))
                        comboBox(id: 'statusField', editable: true, border: lineBorder(color: new Color(230, 230, 230), thickness: 2, roundedCorners: true), font: new Font('Arial', Font.PLAIN, 14))
                        statusField.putClientProperty(org.jvnet.lafwidget.LafWidget.TEXT_SELECT_ON_FOCUS, true)
                        //statusField.focusGained = { nameField.selectAll() }
                        
                        def statusModel = new DefaultComboBoxModel()
                        statusModel.addElement('Available')
                        statusModel.addElement('Busy')
                        statusModel.addElement('Away')
                        statusField.model = statusModel
                    }
                }
                
                tabbedPane(tabLayoutPolicy: JTabbedPane.SCROLL_TAB_LAYOUT, id: 'tabs') {
                    panel(name: 'Home', id: 'homeTab') {
                         borderLayout()
                         splitPane(id: 'splitPane', oneTouchExpandable: true, dividerLocation: 1.0) {
                             panel(constraints: 'left', border: emptyBorder(10)) {
                                 borderLayout()
                                 scrollPane(horizontalScrollBarPolicy: JScrollPane.HORIZONTAL_SCROLLBAR_NEVER, border: null) {
                                     list(id: 'activity')
                                     activity.cellRenderer = new ActivityListCellRenderer()
                                     activity.addHighlighter(simpleStripingHighlighter(stripeBackground: HighlighterFactory.GENERIC_GRAY))
                                     
                                     def activityModel = new DefaultListModel()
                                     activityModel.addElement(new ImMessage('Coucou', 'test@example.com', '9:23am'))
                                     activityModel.addElement(new MailMessage('Intro to Coucou', 'test@example.com', '9:23am', 3))
                                     activityModel.addElement(new EventMessage('Meeting with associates', 'test@example.com', '9:23am'))
                                     activityModel.addElement(new TaskMessage('Complete TPS Reports', 'test@example.com', '9:23am'))
                                     activity.model = activityModel
                                 }
                             }
                             tabbedPane(constraints: 'right', tabPlacement: JTabbedPane.BOTTOM, id: 'navTabs') {
                                 panel(name: 'Contacts', border: emptyBorder(10)) {
                                     borderLayout()
                                     label(text: 'Contacts', constraints: BorderLayout.NORTH, font: new Font('Arial', Font.PLAIN, 14), foreground: Color.WHITE, background: Color.GRAY)
                                     
                                     panel() {
                                         borderLayout()
                                         
                                         def findFilter = new PatternFilter()
                                     
                                         def findText = 'Find..'
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
                                     
                                         scrollPane(horizontalScrollBarPolicy: JScrollPane.HORIZONTAL_SCROLLBAR_NEVER, border: null) {
                                             list(id: 'contactsList')
                                             contactsList.model = new RepositoryListModel(session.rootNode.getNode('contacts'))
                                             contactsList.cellRenderer = new RepositoryListCellRenderer()
                                         }
                                         hbox(constraints: BorderLayout.SOUTH) {
                                             hglue()
                                             hyperlink(new JXHyperlink(newContactAction))
                                         }
                                     }
                                 }
                                 panel(name: 'History', border: emptyBorder(10)) {
                                     borderLayout()
                                     label(text: 'History', constraints: BorderLayout.NORTH, font: new Font('Arial', Font.PLAIN, 14), foreground: Color.WHITE, background: Color.GRAY)
                                     scrollPane(border: null) {
                                         tree(id: 'historyTree', rootVisible: false, showsRootHandles: true)
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
                                         historyTree.model = new RepositoryTreeModel(session.rootNode.getNode('history'))
                                         historyTree.cellRenderer = new RepositoryTreeCellRenderer()
                                     }
                                 }
                                 panel(name: 'Accounts', border: emptyBorder(10)) {
                                     borderLayout()
                                     label(text: 'Accounts', constraints: BorderLayout.NORTH, font: new Font('Arial', Font.PLAIN, 14), foreground: Color.WHITE, background: Color.GRAY)
                                     scrollPane(border: null) {
                                         tree(id: 'accountsTree', rootVisible: false, showsRootHandles: true)
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
                                         accountsTree.model = new RepositoryTreeModel(session.rootNode.getNode('accounts'))
                                         accountsTree.cellRenderer = new RepositoryTreeCellRenderer()
                                         //session.workspace.observationManager.addEventListener(new AccountsUpdateListener(accountsTree), Event.NODE_ADDED | Event.NODE_REMOVED, '/accounts/', true, null, null, false)
                                         //session.workspace.observationManager.addEventListener(accountsTree.model, Event.NODE_ADDED | Event.NODE_REMOVED, '/accounts/', true, null, null, false)

                                     }
                                     hbox(constraints: BorderLayout.SOUTH) {
                                         hglue()
                                         hyperlink(new JXHyperlink(newAccountAction))
                                     }
                                 }
                             }
                             navTabs.putClientProperty(SubstanceLookAndFeel.TABBED_PANE_CONTENT_BORDER_KIND, SubstanceConstants.TabContentPaneBorderKind.SINGLE_FULL)
                         }
                     }
                 }
                 tabs.putClientProperty(SubstanceLookAndFeel.TABBED_PANE_CONTENT_BORDER_KIND, SubstanceConstants.TabContentPaneBorderKind.SINGLE_FULL)
                 tabs.putClientProperty(SubstanceLookAndFeel.TABBED_PANE_CLOSE_CALLBACK, new TabCloseCallbackImpl())
                 SubstanceLookAndFeel.registerTabCloseChangeListener(tabs, new VetoableMultipleTabCloseListenerImpl([homeTab]))
                 tabs.putClientProperty(LafWidget.TABBED_PANE_PREVIEW_PAINTER, new DefaultTabPreviewPainter())
             
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
             }
             bind(source: viewStatusBar, sourceProperty:'selected', target:cStatusBar, targetProperty:'visible')

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
            log.error ex;
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

class TabCloseCallbackImpl implements TabCloseCallback {

      public TabCloseKind onAreaClick(JTabbedPane tabbedPane, int tabIndex, MouseEvent mouseEvent) {
        if (mouseEvent.getButton() != MouseEvent.BUTTON2)
          return TabCloseKind.NONE;
        if (mouseEvent.isShiftDown()) {
          return TabCloseKind.ALL;
        }
        return TabCloseKind.THIS;
      }

      public TabCloseKind onCloseButtonClick(JTabbedPane tabbedPane,
          int tabIndex, MouseEvent mouseEvent) {
        if (mouseEvent.isAltDown()) {
          return TabCloseKind.ALL_BUT_THIS;
        }
        if (mouseEvent.isShiftDown()) {
          return TabCloseKind.ALL;
        }
        return TabCloseKind.THIS;
      }

      public String getAreaTooltip(JTabbedPane tabbedPane, int tabIndex) {
        return null;
      }

      public String getCloseButtonTooltip(JTabbedPane tabbedPane,
          int tabIndex) {
        StringBuffer result = new StringBuffer();
        result.append("<html><body>");
        result.append("Mouse click closes <b>"
            + tabbedPane.getTitleAt(tabIndex) + "</b> tab");
        result
            .append("<br><b>Alt</b>-Mouse click closes all tabs but <b>"
                + tabbedPane.getTitleAt(tabIndex) + "</b> tab");
        result.append("<br><b>Shift</b>-Mouse click closes all tabs");
        result.append("</body></html>");
        return result.toString();
      }
}

class VetoableMultipleTabCloseListenerImpl implements VetoableMultipleTabCloseListener {
    
    def vetoedTabs
    
    public VetoableMultipleTabCloseListenerImpl(def tabs) {
        this.vetoedTabs = tabs
    }
    
    public boolean vetoTabsClosing(JTabbedPane tabbedPane, Set<Component> tabComponents) {
        tabComponents.removeAll(vetoedTabs)
        return false
    }
    
    public void tabsClosing(JTabbedPane tabbedPane, Set<Component> tabComponents) {}
    
    public void tabsClosed(JTabbedPane tabbedPane, Set<Component> tabComponents) {}
}


class VetoableTabCloseListenerImpl implements VetoableTabCloseListener {
    
    def vetoedTab
    
    public VetoableTabCloseListenerImpl(def tab) {
        this.vetoedTab = tab
    }
    
    public boolean vetoTabClosing(JTabbedPane tabbedPane, Component tabComponent) {
        return tabComponent == vetoedTab
    }
    
    public void tabClosing(JTabbedPane tabbedPane, Component tabComponent) {}
    
    public void tabClosed(JTabbedPane tabbedPane, Component tabComponent) {}
}

class ImageFileFilter extends FileFilter {
    
    boolean accept(File file) {
        return file.directory || file.name =~ /\.(gif|jpg|png|bmp)$/
    }
    
    String getDescription() {
        return 'All Image Files'
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
        return "<html><table width=100%><tr><th><font color=silver>${sender}</font></th><th><font color=silver>${time}</font></th></tr><tr><td colspan=2><font size=+1>${text}</font></td></tr></table></html>"
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
        return "<html><table width=100%><tr><th><font color=silver>${sender}</font></th><th><font color=silver>${time}</font></th></tr><tr><td colspan=2><font size=+1>${text}</font></td></tr><td colspan=2><font color=silver size=-1>${count} Messages</font></td></tr></table></html>"
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
        return "<html><table width=100%><tr><th><font color=silver>${sender}</font></th><th><font color=silver>${time}</font></th></tr><tr><td colspan=2><font size=+1>${text}</font></td></tr></table></html>"
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
        return "<html><table width=100%><tr><th><font color=silver>${sender}</font></th><th><font color=silver>${time}</font></th></tr><tr><td colspan=2><font size=+1>${text}</font></td></tr></table></html>"
    }
}

class ActivityListCellRenderer extends DefaultListCellRenderer {

    def iconSize = new Dimension(32, 32)
    def imIcon = SvgBatikResizableIcon.getSvgIcon(Coucou.class.getResource('/im.svg'), iconSize)
    def mailIcon = SvgBatikResizableIcon.getSvgIcon(Coucou.class.getResource('/mail.svg'), iconSize)
    def eventIcon = SvgBatikResizableIcon.getSvgIcon(Coucou.class.getResource('/event.svg'), iconSize)
    def taskIcon = SvgBatikResizableIcon.getSvgIcon(Coucou.class.getResource('/task.svg'), iconSize)
    
    public ActivityListCellRenderer() {
        iconTextGap = 10
        verticalAlignment = CENTER
        //alignmentY = 0.5
        verticalTextPosition = TOP
//        border = BorderFactory.createEmptyBorder(2, 5, 2, 0)
    }
    
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus)
        if (value instanceof ImMessage) {
            setIcon(imIcon)
        }
        else if (value instanceof MailMessage) {
            setIcon(mailIcon)
        }
        else if (value instanceof EventMessage) {
            setIcon(eventIcon)
        }
        else if (value instanceof TaskMessage) {
            setIcon(taskIcon)
        }
        else {
            setIcon(null)
        }
        return this
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
            def node = accountsNode.addNode("mail/${data['emaiLAddressField']}")
            accountsNode.save()
        }
        return null
    }

    boolean cancel(Map settings) {
        return true;
    }
}

class SessionLogout extends Thread {

    static def log = Logger.getInstance(SessionLogout.class)
    
    def session
    
    SessionLogout(def session) {
        this.session = session
    }
    
    void run() {
        log.info 'Logging out session..'
        session.logout()
        log.info 'Session logged out.'
    }
}

class RepositoryTreeModel extends AbstractTreeModel implements javax.jcr.observation.EventListener {
    
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
                try {
                    def node = root.session.getItem(event.path)
                    while (node) {
                        path.add(0, node)
                        node = node.parent
                    }
                } catch (ItemNotFoundException e) {
                    // must be the root node..
                } catch (Exception e) {
                    println e
                }
                log.info "Firing path change event: ${path}"
                fireTreeStructureChanged(new TreePath((Object[]) path))
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

class RepositoryListModel implements ListModel {

    def node
    
    RepositoryListModel(def node) {
        this.node = node
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
}


class RepositoryListCellRenderer extends DefaultListCellRenderer {
    
    Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus)
        text = value.name
        return this
    }
}
