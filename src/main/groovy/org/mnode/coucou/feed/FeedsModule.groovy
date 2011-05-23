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
package org.mnode.coucou.feed

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;
import org.pushingpixels.flamingo.api.ribbon.RibbonContextualTaskGroup;
import org.pushingpixels.flamingo.api.ribbon.RibbonElementPriority;

class FeedsModule {

	def aggregator
	
	def initUI = { ousia ->
		ousia.edt {
			
			actions {
				
				action id: 'addFeedAction', name: rs('Add Subscription'), SmallIcon: feedIcon, closure: {
					url = JOptionPane.showInputDialog(frame, rs('URL'))
					if (url) {
						doOutside {
							try {
								if (breadcrumb.model.items[-1].data instanceof FeedsNodePathResult) {
									aggregator.addFeed(url, breadcrumb.model.items[-1].data.element)
								}
								else {
									aggregator.addFeed(url)
								}
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
				
				action id: 'bookmarkFeedAction', name: rs('Bookmark'), closure: {
					if (activityTable.selectedRow >= 0) {
						int entryIndex = activityTable.convertRowIndexToModel(activityTable.selectedRow)
						def entry = activityTree[entryIndex]
						entry['node'].flagged = !(entry['node'].flagged && entry['node'].flagged.boolean)
						aggregator.save entry['node']
						activityTable.model.fireTableRowsUpdated activityTable.selectedRow, activityTable.selectedRow
					}
				}
		
			}
			
			ribbonTask(rs('Action'), id: 'feedRibbonTask', bands: [
			
				ribbonBand(rs('Subscribe'), id: 'feedSubscriptionBand', resizePolicies: ['mirror']) {
					ribbonComponent([
						component: commandButton(addFeedAction),
						priority: RibbonElementPriority.TOP
					])
				},
			
				ribbonBand(rs('Update'), icon: forwardIcon, id: 'updateBand', resizePolicies: ['mirror']) {
					ribbonComponent([
						component: commandToggleButton(bookmarkIcon, id: 'bookmarkFeedButton', enabled: false, action: bookmarkFeedAction),
						priority: RibbonElementPriority.TOP
					])
					ribbonComponent([
						component: commandButton(okIcon, action: markAsReadAction),
						priority: RibbonElementPriority.MEDIUM
					])
					ribbonComponent([
						component: commandButton(okAllIcon, action: markAllReadAction),
						priority: RibbonElementPriority.MEDIUM
					])
					ribbonComponent([
						component: commandButton(deleteAction),
						priority: RibbonElementPriority.MEDIUM
					])
				},
	
				ribbonBand(rs('Share'), icon: forwardIcon, id: 'shareBand', resizePolicies: ['mirror']) {
					ribbonComponent([
						component: commandButton(rs('Post To Buzz'), actionPerformed: {
							def selectedItem = activityTree[activityTable.convertRowIndexToModel(activityTable.selectedRow)]
							// feed item..
							if (selectedItem.node.hasProperty('link')) {
								Desktop.desktop.browse(URI.create("http://www.google.com/buzz/post?url=${selectedItem.node.getProperty('link').value.string}"))
							}
						} as ActionListener),
						priority: RibbonElementPriority.TOP
					])
					ribbonComponent([
						component: commandButton(rs('Twitter'), actionPerformed: {
							def selectedItem = activityTree[activityTable.convertRowIndexToModel(activityTable.selectedRow)]
							// feed item..
							if (selectedItem.node.hasProperty('link')) {
								Desktop.desktop.browse(URI.create("http://twitter.com/share?url=${selectedItem.node.getProperty('link').value.string}"))
							}
						} as ActionListener),
						priority: RibbonElementPriority.MEDIUM
					])
					ribbonComponent([
						component: commandButton(rs('Facebook'), actionPerformed: {
							def selectedItem = activityTree[activityTable.convertRowIndexToModel(activityTable.selectedRow)]
							// feed item..
							if (selectedItem.node.hasProperty('link')) {
								Desktop.desktop.browse(URI.create("http://www.facebook.com/sharer.php?u=${selectedItem.node.getProperty('link').value.string}"))
							}
						} as ActionListener),
						priority: RibbonElementPriority.MEDIUM
					])
					ribbonComponent(
						component: commandButton(rs('Reddit'), actionPerformed: {
							def selectedItem = activityTree[activityTable.convertRowIndexToModel(activityTable.selectedRow)]
							// feed item..
							if (selectedItem.node.hasProperty('link')) {
								Desktop.desktop.browse(URI.create("http://reddit.com/submit?url=${selectedItem.node.getProperty('link').value.string}&title=${URLEncoder.encode(selectedItem.node.getProperty('title').value.string, 'UTF-8')}"))
							}
						} as ActionListener),
						priority: RibbonElementPriority.MEDIUM
					)
				},
			])
			
			frame.ribbon.addContextualTaskGroup new RibbonContextualTaskGroup(rs('Feeds'), Color.CYAN, feedRibbonTask)
			
		}
	}
}
