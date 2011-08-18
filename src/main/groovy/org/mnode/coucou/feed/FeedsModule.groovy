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

import groovy.xml.MarkupBuilder

import java.awt.Color
import java.awt.Desktop
import java.awt.event.ActionListener

import javax.jcr.PropertyType
import javax.swing.JFileChooser
import javax.swing.JOptionPane
import javax.swing.text.html.StyleSheet

import org.jdesktop.swingx.JXErrorPane
import org.jdesktop.swingx.error.ErrorInfo
import org.mnode.coucou.DateCellRenderer
import org.mnode.coucou.DefaultNodeTableCellRenderer
import org.mnode.coucou.layer.StatusLayerUI
import org.mnode.coucou.util.HtmlCodes
import org.mnode.ousia.HTMLEditorKitExt
import org.mnode.ousia.HyperlinkBrowser
import org.mnode.ousia.HyperlinkBrowser.HyperlinkFeedback
import org.pushingpixels.flamingo.api.common.JCommandButton.CommandButtonKind
import org.pushingpixels.flamingo.api.ribbon.RibbonContextualTaskGroup
import org.pushingpixels.flamingo.api.ribbon.RibbonElementPriority

class FeedsModule {

	def aggregator
	
	def initUI = { ousia ->
		ousia.edt {
			
			resizableIcon('/feed.svg', size: [16, 16], id: 'feedIcon')
			resizableIcon('/feed.svg', size: [12, 12], id: 'feedIconSmall')
			resizableIcon('/star.svg', size: [16, 16], id: 'bookmarkIcon')
			
			actions {
				
				action id: 'importFeedsAction', name: rs('Feeds'), closure: {
					if (chooser.showOpenDialog() == JFileChooser.APPROVE_OPTION) {
						doOutside {
							aggregator.loadOpml(chooser.selectedFile)
		/*
							def opml = new XmlSlurper().parse(chooser.selectedFile)
							def feeds = opml.body.outline.outline.collect { it.@xmlUrl.text() }
							if (feeds.isEmpty()) {
								feeds = opml.body.outline.collect { it.@xmlUrl.text() }
							}
							println "Feeds: ${feeds}"
							def errorMap = [:]
							GParsExecutorsPool.withPool(10) {
								for (feed in feeds) {
									try {
										def future = aggregator.updateFeed.callAsync(feed)
		//	                            future.get()
		//	                            doLater {
		//	                                feedList.model.fireTableDataChanged()
		//	                            }
									}
									catch (Exception ex) {
										log.log unexpected_error, ex
										errorMap.put(feed, ex)
									}
								}
							}
							if (!errorMap.isEmpty()) {
								doLater {
									def error = new ErrorInfo('Import Error', 'An error occurred importing feeds - see log for details',
										"<html><body>Error importing feeds: ${errorMap}</body></html>", null, null, null, null)
									JXErrorPane.showDialog(frame, error)
								}
							}
		*/
						}
					}
				}
				
				action id: 'exportFeedsAction', name: rs('Feeds'), closure: {
					if (chooser.showSaveDialog() == JFileChooser.APPROVE_OPTION) {
						doOutside {
							FileWriter writer = [chooser.selectedFile]
							MarkupBuilder opmlBuilder = [writer]
							opmlBuilder.opml(version: '1.0') {
								body {
									for (feedNode in session.rootNode.getNode('Feeds').nodes) {
										if (!feedNode.hasNode('query')) {
											outline(title: "${feedNode.getProperty('title').string}",
												xmlUrl: "${feedNode.getProperty('url').string}")
										}
									}
								}
							}
						}
					}
				}
		
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
									JXErrorPane.showDialog(frame, error)
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
			
			ribbonApplicationMenuEntrySecondary(id: 'importFeeds', icon: feedIcon, text: rs('Feed Subscriptions'), kind: CommandButtonKind.ACTION_ONLY, actionPerformed: importFeedsAction)
			ribbonApplicationMenuEntrySecondary(id: 'exportFeeds', icon: feedIcon, text: rs('Feed Subscriptions'), kind: CommandButtonKind.ACTION_ONLY, actionPerformed: exportFeedsAction)
			
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
			
			// add result list for feeds and feed items..
			
			// add preview pane for feeds and feed items..
			panel(id: 'feedItemView') {
				borderLayout()
				
				def statusLayer = new StatusLayerUI()
				layer(statusLayer) {
					scrollPane {
						def styleSheet = new StyleSheet()
						styleSheet.addRule('body {background-color:#ffffff; color:#444b56; font-family:verdana,sans-serif; margin:8px; }')
				//        styleSheet.addRule("a {text-decoration:underline; color:blue; }")
				//                            styleSheet.addRule("a:hover {text-decoration:underline; }")
				//        styleSheet.addRule("img {border-width:0; }")
						
						defaultEditorKit = new HTMLEditorKitExt(styleSheet: styleSheet)
				
						editorPane(id: 'feedItemContent', editorKit: defaultEditorKit, editable: false, contentType: 'text/html', opaque: true, border: null)
						feedItemContent.addHyperlinkListener(new HyperlinkBrowser(feedback: [
								show: { uri -> statusLayer.showStatusMessage uri.toString() },
								hide: { statusLayer.hideStatusMessage() }
							] as HyperlinkFeedback))
					}
				}
			}
		}
	}
	
	def loadResults = { ousia, activities, ttsupport, pathResult ->
		ousia.doLater {
			
			for (i in 0..frame.ribbon.contextualTaskGroupCount - 1) {
				if (frame.ribbon.getContextualTaskGroup(i).title == 'Feeds') {
					frame.ribbon.setVisible frame.ribbon.getContextualTaskGroup(i), true
					
					if (breadcrumb.model.items[-1].data.name == 'Feeds') {
						frame.ribbon.selectedTask = feedRibbonTask
					}
				}
				else {
					frame.ribbon.setVisible frame.ribbon.getContextualTaskGroup(i), false
				}
			}
			
			// install new renderer..
			DefaultNodeTableCellRenderer defaultRenderer = [activityTree, ['Today', 'Yesterday', 'Older Items']]
			defaultRenderer.background = Color.WHITE
			
			DateCellRenderer dateRenderer = [defaultRenderer]
			dateRenderer.background = Color.WHITE
			
			ttsupport.delegateRenderer = defaultRenderer
			activityTable.columnModel.getColumn(1).cellRenderer = defaultRenderer
			activityTable.columnModel.getColumn(2).cellRenderer = dateRenderer
			
			activities.withWriteLock {
				clear()
			}
		}
	
//			items.reverseEach {
		for (itemNode in pathResult.element.nodes) {
			 def item = [:]
			 // feeds / items..
			 if (itemNode.hasProperty('title')) {
				 item['title'] = HtmlCodes.unescape(itemNode.title.string)
			 }
			 else {
				 item['title'] = HtmlCodes.unescape(itemNode.name)
			 }
			 
			 if (itemNode.hasProperty('source')) {
				 if (itemNode.source.type == PropertyType.REFERENCE) {
//						 item['source'] = HtmlCodes.unescape(itemNode.source.node.title.string).intern()
					 item['source'] = HtmlCodes.unescape(itemNode.getProperty('source').getNode().getProperty('title').string).intern()
				 }
				 else {
					 item['source'] = HtmlCodes.unescape(itemNode.source.string).intern()
				 }
			 }
			 else {
				 item['source'] = itemNode.parent.name
			 }

			 if (itemNode.hasProperty('date')) {
				 item['date'] = itemNode.date.date.time
			 }
			 else if (itemNode.hasProperty('received')) {
				 item['date'] = itemNode.received.date.time
			 }

			 item['node'] = itemNode
			 item.seen = { itemNode.seen?.boolean == true }
			 item.flagged = { itemNode.flagged?.boolean == true }
			 
			 ousia.doLater {
				 activities.withWriteLock {
					 add(item)
				}
			}
		}
	}
	
	def loadPreview = { ousia, entry ->
		ousia.edt {
			if (entry) {
				contentTitle.text = "<html><strong>${entry['title']}</strong><br/>${entry['source']} <em>${entry['date']}</em></html>"
				
				feedItemContent.editorKit = defaultEditorKit
				
				if (entry['node'].description) {
					def content = entry['node'].description.string.replaceAll(/(?<=img src\=\")http:\/\/.+:.*(?=")/, 'http://coucou.im/favicon.gif') //.replaceAll(/(http:\/\/)?([a-zA-Z0-9\-.]+\.[a-zA-Z0-9\-]{2,}([\/]([a-zA-Z0-9_\/\-.?&%=+])*)*)(\s+|$)/, '<a href="http://$2">$2</a> ')
					feedItemContent.text = content
					feedItemContent.caretPosition = 0
				}
				else {
					feedItemContent.text = null
				}
			}
			else {
				contentTitle.text = ''
				feedItemContent.text = null
			}
			
			previewPane.layout.show(previewPane, 'feedItemView')
		}
	}
}
