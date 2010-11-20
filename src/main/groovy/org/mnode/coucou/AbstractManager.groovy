/**
 * 
 */
package org.mnode.coucou

/**
 * @author fortuna
 *
 */
abstract class AbstractManager {
	
	def getNode = { rootNode, path, referenceable = false ->
		if (!rootNode.hasNode(path)) {
//			log.log init_node, path
			println "Adding path: ${path}"
			def node = rootNode.addNode(path)
			if (referenceable) {
				node.addMixin('mix:referenceable')
			}
		}
		return rootNode.getNode(path)
	}

	def updateProperty = { aNode, propertyName, value ->
		if (value && (!aNode.hasProperty(propertyName) || aNode.getProperty(propertyName).string != value)) {
			aNode.setProperty(propertyName, value)
		}
	}

	// save a node hierarchy..
	def save = { node ->
		def parent = node.parent
		while (parent.isNew()) {
			parent = parent.parent
		}
		parent.save()
	}

}
