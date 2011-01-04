/**
 * This file is part of Coucou.
 *
 * Copyright (c) 2010, Ben Fortuna [fortuna@micronode.com]
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
package org.mnode.coucou;

import java.util.Calendar;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingx.treetable.TreeTableNode;
import org.mnode.base.log.LogAdapter;
import org.mnode.base.log.adapter.JclAdapter;

/**
 * @author Ben
 *
 */
public class PlannerQueryTreeTableNode extends AbstractQueryTreeTableNode<PlannerTreeTableNode> {
    
    private static final LogAdapter LOG = new JclAdapter(LogFactory.getLog(PlannerQueryTreeTableNode.class));
    
    /**
     * @param query
     */
    public PlannerQueryTreeTableNode(Node node) {
        super(node);
        try {
            if ("Today".equals(node.getName()) || "This Week".equals(node.getName())) {
                Calendar todayCal = Calendar.getInstance();
                todayCal.set(Calendar.HOUR_OF_DAY, 0);
                todayCal.clear(Calendar.MINUTE);
                todayCal.clear(Calendar.SECOND);
                todayCal.clear(Calendar.MILLISECOND);
                getBindVariables().put("minDate", node.getSession().getValueFactory().createValue(todayCal));
            }
            else if ("Tomorrow".equals(node.getName())) {
                Calendar tomorrowCal = Calendar.getInstance();
                tomorrowCal.add(Calendar.DAY_OF_MONTH, 1);
                tomorrowCal.set(Calendar.HOUR_OF_DAY, 0);
                tomorrowCal.clear(Calendar.MINUTE);
                tomorrowCal.clear(Calendar.SECOND);
                tomorrowCal.clear(Calendar.MILLISECOND);
                getBindVariables().put("minDate", node.getSession().getValueFactory().createValue(tomorrowCal));
            }
        } catch (RepositoryException e) {
            LOG.log(LogEntries.NODE_ERROR, e, node);
        }
    }

    /**
     * @param query
     * @param parent
     */
    public PlannerQueryTreeTableNode(Node node, TreeTableNode parent) {
        super(node, parent);
    }
    
    @Override
    protected PlannerTreeTableNode createChildNode(Node node) {
        return new PlannerTreeTableNode(node, this);
    }

}
