package org.mnode.coucou

import java.awt.Color
import java.awt.Font
import java.awt.Insets
import java.awt.BorderLayout
import java.awt.FlowLayout
import javax.swing.JFrame
import javax.swing.JTabbedPane
import javax.swing.JScrollPane
import javax.swing.UIManager
import org.jdesktop.swingx.JXStatusBarimport groovy.swing.LookAndFeelHelper//import org.jdesktop.swingx.ResizeBehavior
import groovy.swing.SwingXBuilder
import griffon.builder.flamingo.FlamingoBuilder

class CoucouFrame {
    public static void main(String[] args) {
        
        UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));
        LookAndFeelHelper.instance.addLookAndFeelAlias('substance5', 'org.jvnet.substance.skin.SubstanceBusinessLookAndFeel')
        def swing = new SwingXBuilder()
        swing.edt {
            lookAndFeel('substance5', 'system')
        }
        def flamingo = new FlamingoBuilder()

        def headingFont = new Font('Arial', Font.PLAIN, 14)

//        def statusBarConstraints = new JXStatusBar.Constraint(ResizeBehavior.FIXED)
        
        swing.edt {
            frame(title: 'Coucou', defaultCloseOperation: JFrame.DISPOSE_ON_CLOSE,
                size: [800, 500], show: true, locationRelativeTo: null) {
    //            lookAndFeel('substance')
                actions() {
                    action(id: 'closeTabAction', name: 'Close Tab', accelerator: shortcut('W'))
                    action(id: 'closeAllTabsAction', name: 'Close All Tabs', accelerator: shortcut('shift W'))
                    action(id: 'printAction', name: 'Print', accelerator: shortcut('P'))
                    action(id: 'exitAction', name: 'Exit', smallIcon: imageIcon('/icons/liquidicity/exit.png'), accelerator: shortcut('Q'), closure: { dispose() })
    
                    action(id: 'busyAction', name: 'Busy', smallIcon: imageIcon('/icons/liquidicity/busy.png'), closure: {})
                    action(id: 'invisibleAction', name: 'Invisible', smallIcon: imageIcon('/icons/liquidicity/invisible.png'), closure: {})
                    
                    action(id: 'replyAction', name: 'Reply', accelerator: shortcut('R'))
                    action(id: 'replyAllAction', name: 'Reply All', accelerator: shortcut('shift R'))
                    action(id: 'forwardAction', name: 'Forward', accelerator: shortcut('F'))
    
                    action(id: 'workOfflineAction', name: 'Work Offline', smallIcon: imageIcon('/icons/liquidicity/offline.png'), accelerator: shortcut('shift O'))
    
                    action(id: 'onlineHelpAction', name: 'Online Help', smallIcon: imageIcon('/icons/liquidicity/help.png'), accelerator: 'F1')
                }
                menuBar() {
                    menu(text: "File", mnemonic: 'F') {
                        menuItem(closeTabAction)
                        menuItem(text: "Close Other Tabs")
                        menuItem(closeAllTabsAction)
                        separator()
                        menuItem(text: "Chat", icon: imageIcon('/icons/liquidicity/chat.png'))
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
                            menuItem(busyAction)
                            menuItem(invisibleAction)
                            separator()
                            menuItem(text: "Edit Status Message..")
                        }
                        separator()
                        menuItem(text: "Delete")
                        separator()
                        menuItem(text: "Mail Filters")
                        menuItem(text: "Accounts")
                        menuItem(text: "Preferences", icon: imageIcon('/icons/liquidicity/preferences.png'))
                    }
                    menu(text: "View", mnemonic: 'V') {
                        menuItem(text: "Status Bar")
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
                        menuItem(workOfflineAction)
                    }
                    menu(text: "Help", mnemonic: 'H') {
                        menuItem(onlineHelpAction)
                        menuItem(text: "Tips")
                    separator()
                        menuItem(text: "About")
                    }
                }
                tabbedPane(tabLayoutPolicy: JTabbedPane.SCROLL_TAB_LAYOUT) {
                    panel(name: 'Home') {
                        borderLayout()
                        vbox(constraints: BorderLayout.CENTER) {
    //                        borderLayout()
                            panel(constraints: BorderLayout.CENTER, border: emptyBorder(10)) {
                                borderLayout()
                                label('Unread', font: headingFont, constraints: BorderLayout.NORTH)
                                table(constraints: BorderLayout.CENTER)
                            }
                            panel(constraints: BorderLayout.SOUTH, border: emptyBorder(10)) {
                                borderLayout()
                                label('Flagged', font: headingFont, constraints: BorderLayout.NORTH)
                                table(constraints: BorderLayout.CENTER)
                            }
                            vglue()
                        }
                        vbox(constraints: BorderLayout.EAST) {
    //                        borderLayout()
                            panel(constraints: BorderLayout.NORTH, border: emptyBorder(10)) {
                                borderLayout()
                                label('Search', font: headingFont, constraints: BorderLayout.NORTH)
    //                            scrollPane(horizontalScrollBarPolicy: JScrollPane.HORIZONTAL_SCROLLBAR_NEVER, border: null) {
    //                                textArea(rows: 3, lineWrap: true, wrapStyleWord: true)
    //                            }
                                panel(constraints: BorderLayout.CENTER) {
                                    borderLayout()
                                    textField('Enter query', font: headingFont,  foreground: Color.LIGHT_GRAY, border: null, constraints: BorderLayout.NORTH)
                                    vbox(constraints: BorderLayout.CENTER) {
                                        hyperlink('How to create rollover')
                                        hyperlink('3 priests walk into a bar')
                                        hyperlink('appointment january 10')
                                        vglue()
                                    }
                                }
                            }
                            panel(constraints: BorderLayout.SOUTH, border: emptyBorder(10)) {
                                borderLayout()
                                label('Tags', font: headingFont, constraints: BorderLayout.NORTH)
                                panel(constraints: BorderLayout.CENTER) {
                                    hyperlink('Work')
                                    hyperlink('Humour', font: headingFont)
                                    hyperlink('Finance')
                                    hyperlink('Shopping')
                                }
                            }
                            panel(constraints: BorderLayout.SOUTH, border: emptyBorder(10)) {
                                borderLayout()
                                label('Attachments', font: headingFont, constraints: BorderLayout.NORTH)
                                vbox(constraints: BorderLayout.CENTER) {
                                    hyperlink('image.png')
                                    hyperlink('Work report.doc')
                                    hyperlink('Slides.ppt')
                                    hyperlink('Monthly charts.xls')
                                }
                            }
                            panel(constraints: BorderLayout.NORTH, border: emptyBorder(10)) {
                                borderLayout()
                                label('Contacts', font: headingFont, constraints: BorderLayout.NORTH)
                                scrollPane(horizontalScrollBarPolicy: JScrollPane.HORIZONTAL_SCROLLBAR_NEVER, border: null) {
                                    list()
                                }
                            }
                            panel(constraints: BorderLayout.CENTER, border: emptyBorder(10)) {
                                borderLayout()
                                label('Accounts', font: headingFont, constraints: BorderLayout.NORTH)
                                list(constraints: BorderLayout.CENTER)
                                hyperlink('Create account..', constraints: BorderLayout.SOUTH)
                            }
                            vglue()
                        }
                    }
                    panel(name: 'Inbox') {
                        flamingo.treeBreadcrumbBar(tree: tree())
                    }
                    panel(name: 'Work') {
                    }
                    panel(name: 'RE: Test') {
                        borderLayout()
                        panel(constraints: BorderLayout.NORTH) {
                            flowLayout(alignment: FlowLayout.LEADING)
                            label('RE: Test', font: headingFont)
                        }
                        scrollPane(horizontalScrollBarPolicy: JScrollPane.HORIZONTAL_SCROLLBAR_NEVER, border: null, constraints: BorderLayout.CENTER) {
                            editorPane(editable: false)
                        }
                        panel(constraints: BorderLayout.SOUTH) {
                            flowLayout(alignment: FlowLayout.TRAILING)
                            hyperlink('Show related messages..')
                        }
                    }
                }
                /*
                statusBar(constraints: BorderLayout.SOUTH) {
                    flowLayout(alignment: FlowLayout.TRAILING)
                    toggleButton(busyAction,
                            text: null,
                            toolTipText: 'Busy',
                            selectedIcon: imageIcon('/icons/liquidicity/busy_selected.png'),
                            rolloverIcon: imageIcon('/icons/liquidicity/busy_rollover.png'),
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
                            selectedIcon: imageIcon('/icons/liquidicity/invisible_selected.png'),
                            rolloverIcon: imageIcon('/icons/liquidicity/invisible_rollover.png'),
                            margin: null,
                            border: emptyBorder([0, 4, 0, 4]),
                            borderPainted: false,
                            contentAreaFilled: false,
                            focusPainted: false,
                            opaque: false)
                    toggleButton(workOfflineAction,
                            text: null,
                            toolTipText: 'Work Offline',
                            selectedIcon: imageIcon('/icons/liquidicity/offline_selected.png'),
                            rolloverIcon: imageIcon('/icons/liquidicity/offline_rollover.png'),
                            margin: null,
                            border: emptyBorder([0, 4, 0, 4]),
                            borderPainted: false,
                            contentAreaFilled: false,
                            focusPainted: false,
                            opaque: false)
                }
                */
            }
        }
    }
}
