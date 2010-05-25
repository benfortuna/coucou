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

package org.mnode.coucou;

import java.util.Comparator;
import java.util.Date;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.commons.logging.LogFactory;
import org.mnode.base.log.LogAdapter;
import org.mnode.base.log.adapter.JclAdapter;

import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.gui.AdvancedTableFormat;

/**
 * @author Ben
 *
 */
public class FeedEntryTableFormat implements AdvancedTableFormat<Node> {

    private static final LogAdapter LOG = new JclAdapter(LogFactory.getLog(FeedEntryTableFormat.class));
    
    public int getColumnCount() {
        return 4;
    }
    
    public String getColumnName(int column) {
        switch (column) {
            case 0: return "Flags";
            case 1: return "Title";
            case 2: return "Source";
            case 3: return "Last Updated";
            default: return null;
        }
    }
    
    public Object getColumnValue(Node node, int column) {
        Object value = null;
        try {
            switch(column) {
                case 0:
                    if (node.hasProperty("flag")) {
                        if (node.getProperty("flag").getBoolean()) {
                            value = "*";
                        }
                    }
                    break;
                case 1:
                    value = node.getProperty("title").getString();
                    break;
                case 2:
                    if (node.hasProperty("source")) {
                        value = node.getProperty("source").getNode().getProperty("title").getString();
                    }
                    break;
                case 3:
                    if (node.hasProperty("date")) {
                        value = node.getProperty("date").getDate().getTime();
                    }
                    break;
            }
        }
        catch (RepositoryException e) {
            LOG.log(LogEntries.NODE_ERROR, e, node);
        }
        return value;
    }
    
    public Class<?> getColumnClass(int column) {
        switch (column) {
            case 0: return Object.class;
            case 1: return String.class;
            case 2: return String.class;
            case 3: return Date.class;
            default: return Object.class;
        }
    }
    
    public Comparator<? extends Comparable<?>> getColumnComparator(int column) {
        switch (column) {
            default: return GlazedLists.comparableComparator();
        }
    }

}
