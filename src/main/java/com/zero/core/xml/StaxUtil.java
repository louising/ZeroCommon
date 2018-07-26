package com.zero.core.xml;

import java.io.FileReader;
import java.util.Iterator;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class StaxUtil {

    public static void main(String[] args) throws Exception {
        //testSTAXOfRead();
        //testSTAXofRead2();        
        //testSTAXofRead3();
        //testSTAXofRead4_Event();
    }

    static void testSTAXofRead4_Event() throws Exception {
        String xmlFile = StaxUtil.class.getResource("/").getFile() + "alldatas.xml";
        
        XMLInputFactory factory = XMLInputFactory.newFactory();

        XMLEventReader reader = factory.createXMLEventReader(new FileReader(xmlFile));
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                StartElement start = event.asStartElement();
                System.out.print(start.getName().getLocalPart());
                Iterator<?> attrs = start.getAttributes();
                while (attrs.hasNext()) {
                    Attribute attr = (Attribute) attrs.next();
                    System.out.print(":" + attr.getName().getLocalPart() + "=" + attr.getValue());
                }
                System.out.println();
            }
        }

        reader.close();
    }
    
    static void testSTAXofRead3() throws Exception {
        //reader.getText(): Will read enter and space after >
        String xmlFilePath = "users.xml";
        //String xmlFilePath = StaxDemo.class.getResource("/").getFile() + "users.xml";
        
        XMLInputFactory factory = XMLInputFactory.newFactory();
        XMLStreamReader reader = factory.createXMLStreamReader(new FileReader(xmlFilePath));

        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("user".equalsIgnoreCase(reader.getLocalName()))
                    System.out.println("Name:" + reader.getAttributeValue(null, "name"));
            }
        }
        reader.close();
    }
    
    static void testSTAXofRead2() throws Exception {
        String xmlFilePath = "alldatas.xml";
        //String xmlFilePath = Jdk6App.class.getResource("/").getFile() + "users.xml";
        
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(new FileReader(xmlFilePath));
        int eventType;
        while (reader.hasNext()) {
            eventType = reader.next();
            switch (eventType) {
            case XMLEvent.START_ELEMENT:
                System.out.printf("%s", reader.getName()); //reader.getAttributeValue(null, "corp")
                break;
            case XMLEvent.CHARACTERS:
                System.out.printf("\t%s", reader.getText());
                break;
            default:
                break;
            }
        }
    }
    
    static void testSTAXOfRead() throws Exception {
        String xmlFilePath = "alldatas.xml";
        //String xmlFilePath = StaxDemo.class.getResource("/").getFile() + "users.xml";
        
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLEventReader reader = factory.createXMLEventReader(new FileReader(xmlFilePath));
        XMLEvent event;
        while (reader.hasNext()) {
            event = reader.nextEvent();
            if (event.isStartElement()) {
                System.out.printf("%s", event.asStartElement().getName());
            } else if (event.isCharacters()) {
                System.out.printf(" %s", event.asCharacters().getData());
            }
        }
    }    
}
