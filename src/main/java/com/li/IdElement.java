package com.li;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class IdElement {

    protected String id;

    protected IdElement() {
    }

    protected IdElement(Node node) {
        NamedNodeMap map = node.getAttributes();
        for (int index = 0; index < map.getLength(); index++) {
            Node attr = map.item(index);
            if (attr.getNodeName().equals("id")) {
                id = attr.getNodeValue();
            }
        }
    }


    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
