package cz.zcu.kiv.md2odt.document.odfdom

import org.w3c.dom.Node

/**
 * Created by pepe on 5. 4. 2017.
 */
class LastNode {
    Node root

    LastNode(OdfdomDocument doc) {
        this.root = doc.odt.contentDom.getElementsByTagName("office:text").item(0)
    }

    Node getNode() {
        return root.lastChild
    }
    String getNodeS() {
        return node.toString()
    }
    String getTextContent() {
        return node.textContent
    }
    String getTextStyleName() {
        return node.attributes.getNamedItem("text:style-name").nodeValue
    }
    String getTextOutlineLevel() {
        return node.attributes.getNamedItem("text:outline-level").nodeValue
    }
    String getNodeName() {
        return node.nodeName
    }
    String getXLinkHref() {
        return node.attributes.getNamedItem("xlink:href").nodeValue
    }
    String getXLinkType() {
        return node.attributes.getNamedItem("xlink:type").nodeValue
    }
    String getTextAnchorType() {
        return node.attributes.getNamedItem("text:anchor-type").nodeValue
    }
    String getDrawStyleName() {
        return node.attributes.getNamedItem("draw:style-name").nodeValue
    }

    void switchToLastChild() {
        root = root.lastChild
    }
}
