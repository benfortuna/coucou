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
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.XMPPException
import javax.swing.filechooser.FileFilter
import java.io.File
import javax.swing.DefaultListCellRendererimport javax.swing.JListimport javax.swing.DefaultListModel/**
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
    //@Grab(group='jgoodies', module='forms', version='1.0.5'),
    //@Grab(group='org.codehaus.griffon.flamingobuilder', module='flamingobuilder', version='0.2'),
    @Grab(group='net.java.dev.flamingo', module='flamingo', version='4.2'),
    @Grab(group='org.apache.xmlgraphics', module='batik-awt-util', version='1.7'),
    @Grab(group='org.apache.xmlgraphics', module='batik-swing', version='1.7'),
    @Grab(group='org.apache.xmlgraphics', module='batik-transcoder', version='1.7'),
    @Grab(group='net.java.dev.datatips', module='datatips', version='20091219'),
    @Grab(group='com.fifesoft.rsyntaxtextarea', module='rsyntaxtextarea', version='1.4.0')])
    */
public class Coucou{
     
     static void close(def frame, def exit) {
         if (exit) {
             System.exit(0)
         }
         else {
             frame.visible = false
         }
     }
     
     static void main(def args) {
         UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0))
         UIManager.put(org.jvnet.lafwidget.LafWidget.ANIMATION_KIND, org.jvnet.lafwidget.utils.LafConstants.AnimationKind.FAST.derive(2))
         //UIManager.put(org.jvnet.lafwidget.LafWidget.TABBED_PANE_PREVIEW_PAINTER, new DefaultTabPreviewPainter())
         LookAndFeelHelper.instance.addLookAndFeelAlias('substance5', 'org.jvnet.substance.skin.SubstanceNebulaLookAndFeel')
         LookAndFeelHelper.instance.addLookAndFeelAlias('seaglass', 'com.seaglasslookandfeel.SeaGlassLookAndFeel')
        
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

         swing.edt {
             lookAndFeel('seaglass', 'substance5', 'system')

             frame(title: 'Coucou', id: 'coucouFrame', defaultCloseOperation: JFrame.DO_NOTHING_ON_CLOSE,
                     size: [800, 600], show: false, locationRelativeTo: null, iconImage: imageIcon('/logo.png', id: 'logoIcon').image) {
                
                actions() {
                    action(id: 'busyAction', name: 'Busy', smallIcon: imageIcon('/busy.png'), closure: {})
                    action(id: 'invisibleAction', name: 'Invisible', smallIcon: imageIcon('/invisible.png'), closure: {})
                    action(id: 'workOfflineAction', name: 'Work Offline', smallIcon: imageIcon('/offline.png'), accelerator: shortcut('shift O'), closure: {})
                    
                    action(id: 'newAccountAction', name: 'Create Account..', closure: {
                        def tab = newAccountTab()
                        tab.putClientProperty(SubstanceLookAndFeel.TABBED_PANE_CLOSE_BUTTONS_PROPERTY, true)
                        tabs.add(tab)
                        tabs.selectedComponent = tab
                    })
                    
                    action(id: 'newContactAction', name: 'New Contact..', closure: {
                        def tab = newContactTab()
                        tab.putClientProperty(SubstanceLookAndFeel.TABBED_PANE_CLOSE_BUTTONS_PROPERTY, true)
                        tabs.add(tab)
                        tabs.selectedComponent = tab
                    })

                    action(id: 'closeTabAction', name: 'Close Tab', accelerator: shortcut('W'))
                    action(id: 'closeAllTabsAction', name: 'Close All Tabs', accelerator: shortcut('shift W'))
                    action(id: 'printAction', name: 'Print', accelerator: shortcut('P'))
                    action(id: 'exitAction', name: 'Exit', smallIcon: imageIcon('/exit.png'), accelerator: shortcut('Q'), closure: { close(coucouFrame, true) })
                    
                     action(id: 'onlineHelpAction', name: 'Online Help', accelerator: 'F1', closure: { Desktop.desktop.browse(URI.create('http://wiki.mnode.org/coucou')) })
                     action(id: 'showTipsAction', name: 'Tips', closure: { tips.showDialog(coucouFrame) })
                     action(id: 'aboutAction', name: 'About', closure: {
                         dialog(title: 'About Coucou', size: [300, 200], show: true, owner: coucouFrame, modal: true, locationRelativeTo: coucouFrame) {
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
                                     list(id: 'timeline')
                                     timeline.cellRenderer = new TimelineListCellRenderer()
                                     
                                     def timelineModel = new DefaultListModel()
                                     timelineModel.addElement(new ImMessage('Coucou'))
                                     timelineModel.addElement(new MailMessage('Coucou'))
                                     timelineModel.addElement(new EventMessage('Coucou'))
                                     timelineModel.addElement(new TaskMessage('Coucou'))
                                     timeline.model = timelineModel
                                 }
                             }
                             tabbedPane(constraints: 'right', tabPlacement: JTabbedPane.BOTTOM, id: 'navTabs') {
                                 panel(name: 'Contacts') {
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
                                         list()
                                     }
                                     hbox(constraints: BorderLayout.SOUTH) {
                                         hglue()
                                         hyperlink(new JXHyperlink(newContactAction))
                                     }
                                 }
                                 panel(name: 'History') {
                                     scrollPane(horizontalScrollBarPolicy: JScrollPane.HORIZONTAL_SCROLLBAR_NEVER, border: null) {
                                         list()
                                     }
                                 }
                                 panel(name: 'Accounts') {
                                     borderLayout()
                                     scrollPane(horizontalScrollBarPolicy: JScrollPane.HORIZONTAL_SCROLLBAR_NEVER, border: null) {
                                         list(id: 'accountList')
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
            ex.printStackTrace();
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
    
    def text
    
    public ImMessage(def text) {
        this.text = text
    }
    
    String toString() {
        return text
    }
}

class MailMessage {
    
    def text
    
    public MailMessage(def text) {
        this.text = text
    }
    
    String toString() {
        return text
    }
}

class EventMessage {
    
    def text
    
    public EventMessage(def text) {
        this.text = text
    }
    
    String toString() {
        return text
    }
}

class TaskMessage {
    
    def text
    
    public TaskMessage(def text) {
        this.text = text
    }
    
    String toString() {
        return text
    }
}

class TimelineListCellRenderer extends DefaultListCellRenderer {

    def iconSize = new Dimension(20, 20)
    def imIcon = SvgBatikResizableIcon.getSvgIcon(Coucou.class.getResource('/im.svg'), iconSize)
    def mailIcon = SvgBatikResizableIcon.getSvgIcon(Coucou.class.getResource('/mail.svg'), iconSize)
    def eventIcon = SvgBatikResizableIcon.getSvgIcon(Coucou.class.getResource('/event.svg'), iconSize)
    def taskIcon = SvgBatikResizableIcon.getSvgIcon(Coucou.class.getResource('/task.svg'), iconSize)
    
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
