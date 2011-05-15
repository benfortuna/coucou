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

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JList;

import net.sf.ehcache.CacheManager;

import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.packet.VCard;

public abstract class AbstractChatListCellRenderer<T> extends DefaultListCellRenderer {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private static final Icon DEFAULT_AVATAR = new ImageIcon(new ImageIcon(
    		AbstractChatListCellRenderer.class.getResource("/icons/liquidicity/avatar.png")).getImage().getScaledInstance(-1, 48, Image.SCALE_SMOOTH));
    
    private final XMPPConnection xmpp;
	private final VCardCache vcardCache;
	
	private final Color defaultForeground;
	private final Color offlineForeground;
	private final boolean showAvatar;

	public AbstractChatListCellRenderer(XMPPConnection xmpp, boolean showAvatar) {
		this(xmpp, showAvatar, null);
	}
	
	public AbstractChatListCellRenderer(XMPPConnection xmpp, boolean showAvatar, Font defaultFont) {
		this.xmpp = xmpp;
		this.showAvatar = showAvatar;
		
		final CacheManager cacheManager = CacheManager.create();
		if (cacheManager.getCache("vcards") == null) {
			cacheManager.addCache("vcards");
		}
		vcardCache = new VCardCache(cacheManager.getCache("vcards"));
		
        defaultForeground = Color.BLACK;
        offlineForeground = Color.LIGHT_GRAY;
	}
	
    @SuppressWarnings("unchecked")
	@Override
    public final Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {

        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        
        setForeground(defaultForeground);
        setIcon(null);
    	setFont(getDefaultFont());
        
		final RosterEntry participant = getRosterEntry((T) value);
        if (participant != null) {
            final Presence presence = xmpp.getRoster().getPresence(participant.getUser());
            if (!presence.isAvailable()) {
            	setForeground(offlineForeground);
            }
            
            if (showAvatar) {
                VCard card = vcardCache.getVCard(xmpp, participant.getUser());
                if (card != null && card.getAvatar() != null) {
                	final Icon icon = new ImageIcon(new ImageIcon(card.getAvatar()).getImage().getScaledInstance(-1, 48, Image.SCALE_FAST));
                    setIcon(icon);
                }
                else {
                	setIcon(DEFAULT_AVATAR);
                }
            }
        }
        
        setText(getText((T) value, participant));
        
        return this;
    }

    protected abstract RosterEntry getRosterEntry(T value);
    
    protected abstract String getText(T value, RosterEntry participant);
    
    protected abstract Font getDefaultFont();
}
