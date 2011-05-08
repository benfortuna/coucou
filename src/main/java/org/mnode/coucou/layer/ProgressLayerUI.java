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

import java.awt.FlowLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JComponent;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import org.jdesktop.jxlayer.JXLayer;
import org.jdesktop.jxlayer.plaf.AbstractLayerUI;

public class ProgressLayerUI extends AbstractLayerUI<JComponent> implements ComponentListener {

	private static final long serialVersionUID = 1L;

	private final JProgressBar progressBar;
	
	private final Lock progressLock;
	
	public ProgressLayerUI() {
		progressBar = new JProgressBar();
		progressBar.setVisible(false);
		progressLock = new ReentrantLock();
	}
	
	@Override
	public void installUI(JComponent c) {
		super.installUI(c);
		@SuppressWarnings("unchecked")
		JXLayer<JComponent> l = (JXLayer<JComponent>) c;
        l.getGlassPane().setLayout(null);
        l.getGlassPane().add(progressBar);
        l.addComponentListener(this);
	}
    
    @Override
    public void uninstallUI(JComponent c) {
        super.uninstallUI(c);
        @SuppressWarnings("unchecked")
		JXLayer<JComponent> l = (JXLayer<JComponent>) c;
        l.getGlassPane().setLayout(new FlowLayout());
        l.getGlassPane().remove(progressBar);
        l.removeComponentListener(this);
    }
    
    public void setProgress(final int progress) {
    	SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				try {
					progressLock.lock();
			    	progressBar.setValue(progress);
			    	if (progress > progressBar.getMinimum() && progress < progressBar.getMaximum()) {
			    		if (!progressBar.isVisible()) {
			        		progressBar.setVisible(true);
			    		}
			    	}
			    	else if (progressBar.isVisible()) {
			    		progressBar.setVisible(false);
			    	}
				}
				finally {
					progressLock.unlock();
				}
			}
		});
    }
    
    public int getProgress() {
    	return progressBar.getValue();
    }
    
    public void setMaximum(final int maximum) {
    	SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
		    	progressBar.setMaximum(maximum);
			}
		});
    }
	
	@Override
	public void componentShown(ComponentEvent e) {
	}
	
	@Override
	public void componentResized(ComponentEvent e) {
		progressBar.setLocation(e.getComponent().getWidth() - 220, 5);
		progressBar.setSize(200, e.getComponent().getHeight() - 10);
	}
	
	@Override
	public void componentMoved(ComponentEvent e) {
	}
	
	@Override
	public void componentHidden(ComponentEvent e) {
	}
}
