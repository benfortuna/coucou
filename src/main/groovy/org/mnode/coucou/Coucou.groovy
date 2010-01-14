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

import groovy.swing.SwingXBuilderimport java.awt.Insetsimport javax.swing.UIManagerimport groovy.swing.LookAndFeelHelperimport java.awt.TrayIconimport java.awt.PopupMenuimport java.awt.SystemTrayimport java.awt.MenuItemimport javax.swing.JFrame

/**
 * @author fortuna
 *
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

         swing.edt {
             lookAndFeel('seaglass', 'substance5', 'system')

             frame(title: 'Coucou', id: 'coucouFrame', defaultCloseOperation: JFrame.DO_NOTHING_ON_CLOSE,
                     size: [800, 600], show: false, locationRelativeTo: null, iconImage: imageIcon('/logo.png', id: 'logoIcon').image) {
             }
             
             if (SystemTray.isSupported()) {
                 TrayIcon trayIcon = new TrayIcon(logoIcon.image, 'Coucou')
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
