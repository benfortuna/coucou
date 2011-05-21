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
import java.util.List;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Store;

import org.mnode.coucou.AbstractPathResult;
import org.mnode.coucou.PathResult;
import org.mnode.coucou.PathResultException;

import edu.emory.mathcs.backport.java.util.Arrays;

public class StorePathResult extends AbstractPathResult<Store, Folder> {
	
	public StorePathResult(Store store, String name) {
		super(store, name);
	}

	@Override
	public List<PathResult<?, ?>> getChildren() throws PathResultException {
		try {
			final List<PathResult<?, ?>> children = new ArrayList<PathResult<?, ?>>();
			if (!getElement().isConnected()) {
				getElement().connect();
			}
			for (Folder folder : getElement().getDefaultFolder().list()) {
				children.add(getChild(folder));
			}
			return children;
		}
		catch (MessagingException me) {
			throw new PathResultException(me);
		}
	}

	@Override
	public PathResult<?, Object> getChild(Folder result) throws PathResultException {
		return new FolderPathResult(result);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Folder> getResults() throws PathResultException {
		try {
			if (!getElement().isConnected()) {
				getElement().connect();
			}
			return Arrays.asList(getElement().getDefaultFolder().list());
		} catch (MessagingException e) {
			throw new PathResultException(e);
		}
	}
	
}
