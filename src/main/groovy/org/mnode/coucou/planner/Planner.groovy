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
package org.mnode.coucou.planner

import javax.jcr.Repository

import net.fortuna.ical4j.connector.ObjectNotFoundException
import net.fortuna.ical4j.connector.jcr.JcrCalendarCollection
import net.fortuna.ical4j.connector.jcr.JcrCalendarStore
import net.fortuna.ical4j.util.Calendars

import org.apache.jackrabbit.util.Text
import org.jcrom.Jcrom
import org.mnode.base.log.LogAdapter
import org.mnode.base.log.adapter.Slf4jAdapter
import org.mnode.coucou.AbstractNodeManager
import org.slf4j.LoggerFactory

/**
 * @author fortuna
 *
 */
class Planner extends AbstractNodeManager {

	private static LogAdapter log = new Slf4jAdapter(LoggerFactory.getLogger(Planner))
	
	def repository
	
	Planner(Repository repository, String nodeName) {
		super(repository, 'planner', nodeName)
		this.repository = repository
		if (!baseNode.hasNode('accounts')) {
			baseNode.addNode('accounts')
		}
		if (!baseNode.hasNode('collections')) {
			baseNode.addNode('collections')
		}
		save baseNode
	}
	
	def addCalendar = {url ->
		JcrCalendarStore store = new JcrCalendarStore(new Jcrom(), repository, baseNode.path)
		store.connect 'planner', ''.toCharArray()
		def calendar = Calendars.load(new URL(url))
		def collectionName = calendar.properties.getProperty('X-WR-CALNAME').value
		JcrCalendarCollection collection
		try {
			collection = store.getCollection(collectionName)
		}
		catch (ObjectNotFoundException e) {
			collection = store.addCollection(collectionName)
		}
		
		Calendars.split(calendar).each {
			collection.addCalendar it
		}
	}
	
	def addAccount = { url ->
		def accountNode = getNode(baseNode.accounts, Text.escapeIllegalJcrChars(url), true)
		accountNode.url = url
		if (url =~ /^.*chandlerproject\.org$/) {
			accountNode.type = 'chandler'
		}
		else if (url =~ /^.*google\.com\/calendar\/dav\/$/) {
			accountNode.type = 'gcal'
		}
		save accountNode
	}
}
