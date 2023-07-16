package com.li;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

//http://www.fictionbook.org/index.php/Элемент_body
public class Body {

    protected String lang;
    protected String name;
    protected Title title;
    protected Image image;
    protected ArrayList<Section> sections = new ArrayList<>();
    protected ArrayList<Epigraph> epigraphs;

    public Body() {
    }

    Body(Node body) {
        NamedNodeMap attrs = body.getAttributes();
        for (int index = 0; index < attrs.getLength(); index++) {
            Node attr = attrs.item(index);
            if (attr.getNodeName().equals("name")) {
                name = attr.getNodeValue();
            }
            if (attr.getNodeName().equals("xml:lang")) {
                lang = attr.getNodeValue();
            }
        }
        NodeList map = body.getChildNodes();
        for (int index = 0; index < map.getLength(); index++) {
            Node node = map.item(index);
            switch (node.getNodeName()) {
                case "section":
                    sections.add(new Section(node));
                    break;
                case "title":
                    title = new Title(node);
                    break;
                case "name":
                    name = node.getTextContent();
                    break;
                case "image":
                    image = new Image(node);
                    break;
                case "epigraph":
                    if (epigraphs == null) epigraphs = new ArrayList<>();
                    epigraphs.add(new Epigraph(node));
                    break;
            }
        }
    }

    
    public ArrayList<Section> getSections() {
        return sections;
    }

    
    public Title getTitle() {
        return title;
    }

    
    public ArrayList<Epigraph> getEpigraphs() {
        return epigraphs;
    }

    
    public Image getImage() {
        return image;
    }

    
    public String getName() {
        return name;
    }

    
    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setSections(ArrayList<Section> sections) {
        this.sections = sections;
    }

    public void setEpigraphs(ArrayList<Epigraph> epigraphs) {
        this.epigraphs = epigraphs;
    }
}