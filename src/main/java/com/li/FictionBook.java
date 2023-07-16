package com.li;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

public class FictionBook {

    protected Xmlns[] xmlns;
    protected Description description;
    protected List<Body> bodies = new ArrayList<>();
    protected Map<String, Binary> binaries;

    public String encoding = "utf-8";


    public FictionBook(String str) throws IOException, ParserConfigurationException, SAXException {
        init(str.getBytes());
    }

    public FictionBook(InputStream inputStream) throws IOException, ParserConfigurationException, SAXException {
        init(IOUtils.toByteArray(inputStream));
    }

    public void init(byte[] bytes) throws ParserConfigurationException, IOException, SAXException, OutOfMemoryError {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        InputStream inputStream = new ByteArrayInputStream(bytes);
        BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes)));


        boolean foundIllegalCharacters = false;
        try {
            String line = br.readLine().trim();
            if (!line.startsWith("<")) {
                foundIllegalCharacters = true;
            }
            while (!line.endsWith("?>")) {
                line += "\n" + br.readLine().trim();
            }
            int start = line.indexOf("encoding") + 8;
            String substring = line.substring(start);
            substring = substring.substring(substring.indexOf("\"") + 1);
            encoding = substring.substring(0, substring.indexOf("\"")).toLowerCase();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Document doc;
        if (foundIllegalCharacters) {
            StringBuilder text = new StringBuilder();
            br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes)));
            String line = br.readLine();
            if (line != null && line.contains("<")) {
                line = line.substring(line.indexOf("<"));
            }
            while (line != null) {
                text.append(line);
                line = br.readLine();
            }
            br.close();
            doc = db.parse(new InputSource(new StringReader(text.toString())));
        } else {
            doc = db.parse(new InputSource(new InputStreamReader(inputStream, encoding)));
        }
        initXmlns(doc);
        description = new Description(doc);
        NodeList bodyNodes = doc.getElementsByTagName("body");
        for (int item = 0; item < bodyNodes.getLength(); item++) {
            bodies.add(new Body(bodyNodes.item(item)));
        }
        NodeList binary = doc.getElementsByTagName("binary");
        for (int item = 0; item < binary.getLength(); item++) {
            if (binaries == null) binaries = new HashMap<>();
            Binary binary1 = new Binary(binary.item(item));
            binaries.put(binary1.getId().replace("#", ""), binary1);
        }
    }

    protected void setXmlns(ArrayList<Node> nodeList) {
        xmlns = new Xmlns[nodeList.size()];
        for (int index = 0; index < nodeList.size(); index++) {
            Node node = nodeList.get(index);
            xmlns[index] = new Xmlns(node);
        }
    }

    protected void initXmlns(Document doc) {
        NodeList fictionBook = doc.getElementsByTagName("FictionBook");
        ArrayList<Node> xmlns = new ArrayList<>();
        for (int item = 0; item < fictionBook.getLength(); item++) {
            NamedNodeMap map = fictionBook.item(item).getAttributes();
            for (int index = 0; index < map.getLength(); index++) {
                Node node = map.item(index);
                xmlns.add(node);
            }
        }
        setXmlns(xmlns);
    }

    public ArrayList<Person> getAuthors() {
        return description.getDocumentInfo().getAuthors();
    }

    public Xmlns[] getXmlns() {
        return xmlns;
    }

    public Description getDescription() {
        return description;
    }

    public Body getBody() {
        return getBody(null);
    }

    public Body getNotes() {
        return getBody("notes");
    }

    public Body getComments() {
        return getBody("comments");
    }

    private Body getBody(String name) {
        for (Body body : bodies) {
            if ((name + "").equals(body.getName() + "")) {
                return body;
            }
        }
        return bodies.get(0);
    }


    public Map<String, Binary> getBinaries() {
        return binaries == null ? new HashMap<String, Binary>() : binaries;
    }

    public String getTitle() {
        return description.getTitleInfo().getBookTitle();
    }

    public String getLang() {
        return description.getTitleInfo().getLang();
    }

    public Annotation getAnnotation() {
        return description.getTitleInfo().getAnnotation();
    }
}

