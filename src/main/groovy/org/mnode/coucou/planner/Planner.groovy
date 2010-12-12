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
package org.mnode.coucou.planner

import javax.jcr.Repository;

import net.fortuna.ical4j.connector.jcr.JcrCalendarCollection;
import net.fortuna.ical4j.connector.jcr.JcrCalendarStore;
import net.fortuna.ical4j.util.Calendars;

import org.jcrom.Jcrom;
import org.mnode.coucou.AbstractNodeManager;

/**
 * @author fortuna
 *
 */
class Planner extends AbstractNodeManager {

	def repository
	
	Planner(Repository repository, String nodeName) {
		super(repository, 'planner', nodeName)
		this.repository = repository
	}
	
	def addCalendar = {url ->
		JcrCalendarStore store = new JcrCalendarStore(new Jcrom(), repository, rootNode.path)
		store.connect 'planner', ''.toCharArray()
		JcrCalendarCollection collection = store.addCollection('Public Calendars')
		collection.addCalendar Calendars.load(new URL(url))
	}
}
