package org.mnode.coucou

import java.awt.Insets
import javax.swing.JFrame
import javax.swing.JTabbedPane
import javax.swing.UIManager

import groovy.swing.SwingBuilder

public class CoucouFrame {
    public static void main(String[] args) {
        
        UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));
        def swing = new SwingBuilder()



        swing.frame(title: 'Coucou', defaultCloseOperation: JFrame.DISPOSE_ON_CLOSE,
            size: [800, 600], show: true, locationRelativeTo: null) {
            lookAndFeel("system")
            menuBar() {
                menu(text: "File", mnemonic: 'F') {
                    menuItem(text: "Close Tab")
                    menuItem(text: "Close Other Tabs")
                    menuItem(text: "Close All Tabs")
                    separator()
                    menuItem(text: "Chat")
                    menuItem(text: "Call")
                    menuItem(text: "Send File..")
        	    separator()
                    menuItem(text: "Print")
        	    separator()
                    menuItem(text: "Import..")
        	    separator()
                    menuItem(text: "Exit", mnemonic: 'X', actionPerformed: {dispose() })
                }
                menu(text: "Edit", mnemonic: 'E') {
        			menu(text: "Status") {
        				menuItem(text: "Busy")
        				menuItem(text: "Invisible")
        				separator()
        				menuItem(text: "Edit Status Message..")
        			}
        			separator()
                    menuItem(text: "Delete")
        			separator()
                    menuItem(text: "Mail Filters")
                    menuItem(text: "Accounts")
                    menuItem(text: "Preferences")
                }
                menu(text: "View", mnemonic: 'V') {
                    menuItem(text: "Status Bar")
                }
                menu(text: "Action", mnemonic: 'A') {
                    menuItem(text: "Reply")
                    menuItem(text: "Reply All")
                    menuItem(text: "Forward")
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
        			menuItem(text: "Work Offline")
                }
                menu(text: "Help", mnemonic: 'H') {
                    menuItem(text: "Online Help")
                    menuItem(text: "Tips")
        	    separator()
                    menuItem(text: "About")
                }
            }
        	tabbedPane(tabLayoutPolicy: JTabbedPane.SCROLL_TAB_LAYOUT) {
        		panel(name: 'Home')
        	}
        }	
    }
}
