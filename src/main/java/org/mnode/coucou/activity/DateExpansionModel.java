/**
 * 
 */
package org.mnode.coucou.activity;

import ca.odell.glazedlists.TreeList.ExpansionModel;

/**
 * @author fortuna
 *
 */
public class DateExpansionModel implements ExpansionModel<Object> {

	public boolean isExpanded(Object arg0, java.util.List<Object> arg1) {
		return !"Older Items".equals(arg0);
	};
	
	public void setExpanded(Object arg0, java.util.List<Object> arg1, boolean arg2) {};
}
