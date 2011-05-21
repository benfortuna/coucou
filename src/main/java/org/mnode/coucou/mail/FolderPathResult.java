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
package org.mnode.coucou.mail;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;

import org.mnode.coucou.AbstractPathResult;
import org.mnode.coucou.PathResult;
import org.mnode.coucou.PathResultException;

import edu.emory.mathcs.backport.java.util.Arrays;

public class FolderPathResult extends AbstractPathResult<Folder, Object> {

	public FolderPathResult(Folder folder) {
		super(folder, null);
	}
	
	@Override
	public String getName() throws PathResultException {
		return getElement().getName();
	}

	@Override
	public List<PathResult<?, ?>> getChildren() throws PathResultException {
		try {
			final List<PathResult<?, ?>> children = new ArrayList<PathResult<?, ?>>();
			for (Folder folder : getElement().list()) {
				children.add(getChild(folder));
			}
			return children;
		}
		catch (MessagingException me) {
			throw new PathResultException(me);
		}
	}

	@Override
	public PathResult<?, ?> getChild(Object result) throws PathResultException {
		return new FolderPathResult((Folder) result);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getResults() throws PathResultException {
		try {
			if (!getElement().isOpen()) {
				getElement().open(Folder.READ_ONLY);
			}
			if ((getElement().getType() & Folder.HOLDS_MESSAGES) > 0) {
				final Message[] messages = getElement().getMessages();
				
				FetchProfile fp = new FetchProfile();
				fp.add(FetchProfile.Item.ENVELOPE);
				fp.add(FetchProfile.Item.FLAGS);

				getElement().fetch(messages, fp);
				
				List<Message> messageList = new ArrayList<Message>(Arrays.asList(messages));
				for (Iterator<Message> it = messageList.iterator(); it.hasNext();) {
					if (it.next().isExpunged()) {
						it.remove();
					}
				}
				
				return new ArrayList<Object>(messageList);
			}
			else {
				return Arrays.asList(getElement().list());
			}
		} catch (MessagingException e) {
			throw new PathResultException(e);
		}
	}

}
