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
/**
 * 
 */
package org.mnode.coucou.planner;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.nodetype.NodeType;

import net.fortuna.ical4j.connector.CalendarStore;
import net.fortuna.ical4j.connector.dav.CalDavCalendarCollection;

import org.mnode.coucou.NodePathResult;
import org.mnode.coucou.PathResult;
import org.mnode.coucou.PathResultException;

/**
 * @author fortuna
 *
 */
public class PlannerNodePathResult extends NodePathResult {

	private final CalendarStoreFactory calendarStoreFactory;
	
	/**
	 * @param node
	 */
	public PlannerNodePathResult(Node node) {
		super(node);
		calendarStoreFactory = new CalendarStoreFactory("-//Ben Fortuna//Coucou 1.0//EN");
	}

	@Override
	public List<PathResult<?, ?>> getChildren() throws PathResultException {
		final List<PathResult<?, ?>> children = new ArrayList<PathResult<?, ?>>();
		try {
			// add search queries..

			// accounts..
			final NodeIterator accountNodes = getElement().getNode("accounts").getNodes();
			while (accountNodes.hasNext()) {
				final Node accountNode = accountNodes.nextNode();
				children.add(getChild(accountNode));
			}
			
			// add collections..
			final NodeIterator collectionNodes = getElement().getNode("collections").getNodes();
			while (collectionNodes.hasNext()) {
				final Node node = collectionNodes.nextNode();
				if (node.isNodeType(NodeType.NT_UNSTRUCTURED)) {
					children.add(getChild(node));
				}
			}
		}
		catch (RepositoryException re) {
			throw new PathResultException(re);
		}
		return children;
	}
	
	@Override
	public PathResult<?, ?> getChild(Node result) throws PathResultException {
		try {
			if (result.getParent().getName().equals("accounts")) {
				final URL storeUrl = new URL(result.getProperty("url").getString());
				final String storeType = result.getProperty("type").getString();
				final CalendarStore<CalDavCalendarCollection> store = calendarStoreFactory.newInstance(storeUrl, storeType);
				return new CalendarStorePathResult<CalDavCalendarCollection>(store, result.getName());
			}
			else {
				return new CollectionNodePathResult(result);
			}
		}
		catch (RepositoryException e) {
			throw new PathResultException(e);
		}
		catch (MalformedURLException mue) {
			throw new PathResultException(mue);
		}
	}
	
	@Override
	public List<Node> getResults() throws PathResultException {
		final List<Node> results = new ArrayList<Node>();
		try {
			final NodeIterator collectionNodes = getElement().getNode("collections").getNodes();
			while (collectionNodes.hasNext()) {
				final Node node = collectionNodes.nextNode();
				if (node.isNodeType(NodeType.NT_UNSTRUCTURED)) {
					results.add(node);
				}
			}
			
			final NodeIterator accountNodes = getElement().getNode("accounts").getNodes();
			while (accountNodes.hasNext()) {
				final Node node = accountNodes.nextNode();
				if (node.isNodeType(NodeType.NT_UNSTRUCTURED)) {
					results.add(node);
				}
			}
		}
		catch (RepositoryException re) {
			throw new PathResultException(re);
		}
		return results;
	}

}
