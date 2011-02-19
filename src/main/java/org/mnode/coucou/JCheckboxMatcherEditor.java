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
