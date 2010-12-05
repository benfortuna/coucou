/**
 * 
 */
package org.mnode.coucou;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 * @author fortuna
 *
 */
public class LookAndFeelInfoRenderer extends DefaultListCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index,
			boolean isSelected, boolean cellHasFocus) {
		
		LookAndFeelInfo info = (LookAndFeelInfo) value;
		return super.getListCellRendererComponent(list, info.getName(), index, isSelected, cellHasFocus);
	}
}
