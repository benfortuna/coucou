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
package org.mnode.coucou.chat;

import java.awt.Font;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.util.StringUtils;

/**
 * @author Ben
 *
 */
public class MessageListCellRenderer extends AbstractChatListCellRenderer<Message> {

	private static final long serialVersionUID = 1L;
	
	private Roster roster;
    
    /**
     * @param roster a roster from an active XMPP connection
     */
    public MessageListCellRenderer(XMPPConnection xmpp) {
        super(xmpp, false);
        this.roster = xmpp.getRoster();
    }

    @Override
    protected RosterEntry getRosterEntry(Message value) {
    	return roster.getEntry(StringUtils.parseBareAddress(value.getFrom()));
    }
    
    @Override
    protected String getText(Message value, RosterEntry participant) {
        StringBuilder b = new StringBuilder();
        if (participant != null) {
            if (participant.getName() != null) {
                b.append(participant.getName());
            }
            else {
                b.append(participant.getUser());
            }
        }
        else {
            b.append(StringUtils.parseName(value.getFrom()));
        }
        b.append(": ");
        b.append(value.getBody());
        return b.toString();
    }
    
    @Override
    protected Font getDefaultFont() {
    	return getFont();
    }
}
