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

import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;

public class PeerListCellRenderer extends AbstractChatListCellRenderer<RosterEntry> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public PeerListCellRenderer(XMPPConnection xmpp) {
		super(xmpp, true);
	}
	
	@Override
	protected RosterEntry getRosterEntry(RosterEntry value) {
		return value;
	}
	
	@Override
	protected String getText(RosterEntry value, RosterEntry participant) {
        if (participant.getName() != null) {
            return participant.getName();
        }
        else {
            return participant.getUser();
        }
	}
	
	@Override
	protected Font getDefaultFont() {
		return getFont().deriveFont(14f);
	}
}
