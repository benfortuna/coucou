/**
 * 
 */
package org.mnode.coucou;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JCheckBox;

import ca.odell.glazedlists.Filterator;
import ca.odell.glazedlists.matchers.AbstractMatcherEditor;
import ca.odell.glazedlists.matchers.Matcher;

/**
 * @author fortuna
 *
 */
public class JCheckboxMatcherEditor<E> extends AbstractMatcherEditor<E> {

	private final Filterator<Boolean, E> filterator;

	private final Matcher<E> matcher;
	
	private final JCheckBox component;
	
	JCheckboxMatcherEditor(JCheckBox component, Filterator<Boolean, E> filterator) {
		this.filterator = filterator;
		this.component = component;
		matcher = new BooleanMatcher();
		
		component.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				refilter();
			}
		});
	}
	
	private void refilter() {
		Logger.getLogger("Global").info("Refiltering..");
		if (!component.isSelected()) {
			fireMatchAll();
		}
		else {
			fireChanged(matcher);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Matcher<E> getMatcher() {
		return matcher;
	}
	
	private class BooleanMatcher implements Matcher<E> {
		
		/**
		 * {@inheritDoc}
		 */
		public boolean matches(E paramE) {
			if (component.isSelected()) {
				List<Boolean> matches = new ArrayList<Boolean>();
				filterator.getFilterValues(matches, paramE);
				if (!matches.isEmpty()) {
					return matches.get(0);
				}
			}
			return true;
		};
	}
}
