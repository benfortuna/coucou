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

import org.mnode.base.log.FormattedLogEntry;
import org.mnode.base.log.LogEntry;
import org.mnode.base.log.LogEntry.Level;

/**
 * @author Ben
 *
 */
public final class LogEntries {
	
	public static final LogEntry UNEXPECTED_ERROR = new FormattedLogEntry(Level.Error,
			"An unexpected error has occurred");

    public static final LogEntry NODE_ERROR = new FormattedLogEntry(Level.Error,
            "An unexpected error occurred reading node: %s");

    public static final LogEntry EVENT_PATH_ERROR = new FormattedLogEntry(Level.Error,
            "An unexpected error occurred reading path from event: %s");

    public static final LogEntry QUERY_ERROR = new FormattedLogEntry(Level.Error,
            "An unexpected error occurred executing query: %s");
    
    public static final LogEntry NODE_ADDED = new FormattedLogEntry(Level.Debug, "Node added: %");
}
