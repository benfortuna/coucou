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
package org.mnode.coucou.layer;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.jdesktop.jxlayer.JXLayer;
import org.jdesktop.jxlayer.plaf.AbstractLayerUI;

public class StatusLayerUI extends AbstractLayerUI<JComponent> implements ComponentListener {

	private static final long serialVersionUID = 1L;

	private JLabel statusPane;
	
	public StatusLayerUI() {
		statusPane = new JLabel();
		statusPane.setBackground(new Color(192, 192, 192, 192));
		statusPane.setOpaque(true);
		statusPane.setVisible(false);
	}
	
	@Override
	public void installUI(JComponent c) {
		super.installUI(c);
		@SuppressWarnings("unchecked")
		JXLayer<JComponent> l = (JXLayer<JComponent>) c;
        l.getGlassPane().setLayout(null);
        l.getGlassPane().add(statusPane);
        l.addComponentListener(this);
	}
    
    @Override
    public void uninstallUI(JComponent c) {
        super.uninstallUI(c);
        @SuppressWarnings("unchecked")
		JXLayer<JComponent> l = (JXLayer<JComponent>) c;
        l.getGlassPane().setLayout(new FlowLayout());
        l.getGlassPane().remove(statusPane);
        l.removeComponentListener(this);
    }
    
    public void showStatusMessage(String message) {
    	statusPane.setText(message);
    	statusPane.setVisible(true);
    }
    
    public void hideStatusMessage() {
    	statusPane.setVisible(false);
    }
	
	@Override
	public void componentShown(ComponentEvent e) {
	}
	
	@Override
	public void componentResized(ComponentEvent e) {
		statusPane.setLocation(10, e.getComponent().getHeight() - 30);
		statusPane.setSize(e.getComponent().getWidth() - 20, 30);
	}
	
	@Override
	public void componentMoved(ComponentEvent e) {
	}
	
	@Override
	public void componentHidden(ComponentEvent e) {
	}
}
