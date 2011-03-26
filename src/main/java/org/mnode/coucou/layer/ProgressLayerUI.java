package org.mnode.coucou.layer;

import java.awt.FlowLayout;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JProgressBar;

import org.jdesktop.jxlayer.JXLayer;
import org.jdesktop.jxlayer.plaf.AbstractLayerUI;

public class ProgressLayerUI extends AbstractLayerUI<JComponent> {

	private static final long serialVersionUID = 1L;

	private JProgressBar progressBar;
	
	public ProgressLayerUI() {
		progressBar = new JProgressBar();
		progressBar.setVisible(false);
	}
	
	@Override
	public void installUI(JComponent c) {
		super.installUI(c);
		@SuppressWarnings("unchecked")
		JXLayer<JComponent> l = (JXLayer<JComponent>) c;
        l.getGlassPane().setLayout(null);
        l.getGlassPane().add(progressBar);
	}
    
    @Override
    public void uninstallUI(JComponent c) {
        super.uninstallUI(c);
        @SuppressWarnings("unchecked")
		JXLayer<JComponent> l = (JXLayer<JComponent>) c;
        l.getGlassPane().setLayout(new FlowLayout());
        l.getGlassPane().remove(progressBar);
    }
    
    @Override
    protected void processMouseEvent(MouseEvent mouseEvent,
    		JXLayer<? extends JComponent> layer) {
    	super.processMouseEvent(mouseEvent, layer);
    	if (mouseEvent.getID() == MouseEvent.MOUSE_ENTERED) {
    		progressBar.setLocation(layer.getView().getWidth() - 220, 5);
    		progressBar.setSize(200, layer.getView().getHeight() - 10);
    	}
    }
    
    public void setProgress(int progress) {
    	progressBar.setValue(progress);
    	if (progress >= progressBar.getMinimum() && progress <= progressBar.getMaximum()) {
    		if (!progressBar.isVisible()) {
        		progressBar.setVisible(true);
    		}
    	}
    	else if (progressBar.isVisible()) {
    		progressBar.setVisible(false);
    	}
    }
    
    public int getProgress() {
    	return progressBar.getValue();
    }
    
    public void setMaximum(int maximum) {
    	progressBar.setMaximum(maximum);
    }
}
