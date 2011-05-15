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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.packet.VCard;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

/**
 * A cache for vCard instances.
 * 
 * @author fortuna
 *
 */
public class VCardCache {

    private static final Log LOG = LogFactory.getLog(VCardCache.class);

    private Cache cache;
    
    private final ExecutorService vcardLoaders;

    /**
     * @param cache an underlying ehcache instance
     */
    public VCardCache(Cache cache) {
        this.cache = cache;
        vcardLoaders = Executors.newCachedThreadPool();
    }

    /**
     * @param connection an active XMPP connection to retrieve a vCard from
     * @param user the user to retrieve the vCard for
     * @return a vCard instance for the specified user
     */
    public VCard getVCard(final XMPPConnection connection, final String user) {
        final Element cardElement = cache.get(user);
        if (cardElement == null) {
        	vcardLoaders.execute(new Runnable() {
				
				@Override
				public void run() {
		            try {
		                VCard card = new VCard();
		                card.load(connection, user);
		                cache.put(new Element(user, card));
		            } catch (XMPPException e) {
		                LOG.info("[" + user + "]: vCard not available");
		                
		                VCard card = new VCard();
		                card.setAvatar(getClass().getResource("/icons/liquidicity/avatar.png"));
		                cache.put(new Element(user, card));
		            }
				}
			});
        }
        
        return cardElement != null ? (VCard) cardElement.getObjectValue() : null;
    }
}
