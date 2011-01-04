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

import javax.jcr.Session;

import org.apache.commons.logging.LogFactory;
import org.mnode.base.log.FormattedLogEntry;
import org.mnode.base.log.LogAdapter;
import org.mnode.base.log.LogEntry;
import org.mnode.base.log.LogEntry.Level;
import org.mnode.base.log.adapter.JclAdapter;

/**
 * @author Ben
 *
 */
public class SessionLogout extends Thread {

    private static final LogAdapter LOG = new JclAdapter(LogFactory.getLog(SessionLogout.class));
    
    private static final LogEntry LOGGING_OUT = new FormattedLogEntry(Level.Info, "Logging out session..");
    private static final LogEntry LOGGED_OUT = new FormattedLogEntry(Level.Info, "Session logged out.");
    
    private final Session session;
    
    public SessionLogout(Session session) {
        this.session = session;
    }
    
    @Override
    public void run() {
        LOG.log(LOGGING_OUT);
        session.logout();
        LOG.log(LOGGED_OUT);
    }
}
