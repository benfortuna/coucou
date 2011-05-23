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

import javax.swing.JOptionPane;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;
import org.pushingpixels.flamingo.api.common.JCommandButton.CommandButtonKind;

class PlannerModule {
	
	def planner
	
	def initUI = { ousia ->
		ousia.edt {
			
			actions {
				
				action id: 'addCalendarAction', name: rs('Calendar'), closure: {
					def url = JOptionPane.showInputDialog(frame, rs('URL'))
					if (url) {
						doOutside {
							try {
								planner.addCalendar(url)
							}
							catch (MalformedURLException e) {
								doLater {
									JOptionPane.showMessageDialog(frame, "Invalid URL: ${url}")
								}
							}
							catch (Exception e2) {
								doLater {
									ErrorInfo error = ['Error', "${e2.message}",
										"<html><body>Error adding feed: ${e2}</body></html>", null, null, null, null]
									JXErrorPane.showDialog(frame, error);
								}
							}
						}
					}
				}
			}
			
			ribbonApplicationMenuEntrySecondary(id: 'newCalendar', icon: eventIcon, text: rs('Calendar'), kind: CommandButtonKind.ACTION_ONLY, actionPerformed: addCalendarAction)
		}
	}
}
