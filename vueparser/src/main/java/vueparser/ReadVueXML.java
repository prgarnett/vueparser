package vueparser;

import javax.xml.parsers.DocumentBuilderFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ReadVueXML {

	private List<DotNode> nodes; 
	private List<Edge> edges; 
	
	public ReadVueXML()
	{
		this.nodes = new ArrayList<DotNode>();
		this.edges = new ArrayList<Edge>();
	}
	
	public void readTheFile(String filePath)
	{
        try {

            // Creating a DocumentBuilderFactory
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            // Parsing the XML file
            Document doc = dBuilder.parse(filePath);
            doc.getDocumentElement().normalize();
            
            // HashSet to store unique element names
            Set<String> elementNames = new HashSet<>();

            // Populate the set with unique element names
            listElementNames(doc, elementNames);

            // Print the unique element names
            System.out.println("Unique Element Names:");
            for(String aElement : elementNames)
            {
            	System.out.println("*** Element: " + aElement);
//            	this.printAttrubtes(doc, aElement);
            }

            // Example: Reading "employee" elements
            NodeList nodeList = doc.getElementsByTagName("child");

            for (int temp = 0; temp < nodeList.getLength(); temp++)
            {
                Element element = (Element) nodeList.item(temp);

                if(element.hasAttribute("xsi:type"))
                {
                	if(element.getAttribute("xsi:type").equals("node"))
        			{
                		this.parseTheNodes(element);
        			}
                	else if(element.getAttribute("xsi:type").equals("link"))
        			{
                		this.parseTheEdges(element);
        			}
                }
//                // Assuming "employee" has an attribute "id"
//                String id = element.getAttribute("id");
//                // Assuming "employee" has a sub-element "name"
//                String node = element.getElementsByTagName("node").item(0).getTextContent();
//                
//                System.out.println("node : " + node);
//                System.out.println("id : " + id);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
	}
	
	private void parseTheNodes(Element element)
	{
		System.out.println("Current Node: " + element.getNodeName());
		DotNode aNode = new DotNode();
		
		if(element.hasAttribute("ID"))
        {
			System.out.println("Attribute name: ID Value:" + element.getAttribute("ID"));
			aNode.setID(Integer.parseInt(element.getAttribute("ID")));
        }
		if(element.hasAttribute("label"))
        {
			System.out.println("Attribute name: label Value:" + element.getAttribute("label"));
			aNode.setLabel(element.getAttribute("label"));
        }
        if(element.hasAttribute("x"))
        {
        	System.out.println("Attribute name: x Value:" + element.getAttribute("x"));
        	aNode.setX(Double.parseDouble(element.getAttribute("x")));
        }
        if(element.hasAttribute("y"))
        {
        	System.out.println("Attribute name: y Value:" + element.getAttribute("y"));
        	aNode.setY(Double.parseDouble(element.getAttribute("y")));
        }
        nodes.add(aNode);
	}
	
	private void parseTheEdges(Element element)
	{
		System.out.println("Current Edge: " + element.getNodeName());
		
		if(element.hasAttribute("ID"))
        {
			System.out.println("Attribute name: id Value:" + element.getAttribute("ID"));
        }
		if(element.hasAttribute("label"))
        {
			System.out.println("Attribute name: label Value:" + element.getAttribute("label"));
        }
		
		NodeList links = element.getChildNodes();
		Edge aEdge = new Edge();
		for (int temp = 0; temp < links.getLength(); temp++)
        {
			Node node = links.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
            	Element elementLink = (Element) node;
	
	            if(elementLink.hasAttribute("xsi:type"))
	            {
	            	System.out.println("Link node " + temp +": " + elementLink.getAttribute("xsi:type") + " element value: " + elementLink.getTextContent());
	            }
	            if(elementLink.getTagName().equals("ID1"))
	            {
	            	aEdge.setStartNode(Integer.parseInt(elementLink.getTextContent()));
	            }
	            if(elementLink.getTagName().equals("ID2"))
	            {
	            	aEdge.setEndNode(Integer.parseInt(elementLink.getTextContent()));
	            }
            }
        }
		this.edges.add(aEdge);
	}
	
	public void saveTheDotFile(String savePath)
	{
		try (FileWriter writer = new FileWriter(savePath)) {
			writer.write("digraph G {\n"
					+ "\toverlap=false;\n"
					+ "\tsplines=true;\n"
					+ "\tnode [shape=ellipse];\n\n");
			
			
		
			for(DotNode aNode : nodes)
			{
				System.out.println("Node ID: " + aNode.getID() + " Label: " + aNode.getLabel());
				writer.write("\t Node_" + aNode.getID() + " [label=\"" + aNode.getLabel() + "\", pos=\"" + aNode.getX() +"," + aNode.getY() +"!\", fontsize=30];\n");
			}
			
			writer.write("\n");
			
			for(Edge aEdge : edges)
			{
				System.out.println("Start Node ID: " + aEdge.getStartNode() + " End Node ID: " + aEdge.getEndNode());
				writer.write("Node_" + aEdge.getStartNode() + " -> " + "Node_" + aEdge.getEndNode() + " [dir=none];\n");
			}

            writer.write("}");
			System.out.println("Data has been written to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred:");
            e.printStackTrace();
        }
	}
	
	private void listElementNames(Node node, Set<String> names) {
        // Only process element nodes
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            names.add(node.getNodeName());
        }

        // Recurse for each child node
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            listElementNames(children.item(i), names);
        }
    }
	
	private void printAttrubtes(Document doc, String elementName)
	{
		NodeList nodeList = doc.getElementsByTagName(elementName);
	
	    // Iterate over the list of employees
	    for (int i = 0; i < nodeList.getLength(); i++) {
	        Node node = nodeList.item(i);
	        if (node.getNodeType() == Node.ELEMENT_NODE) {
	            Element element = (Element) node;
	            System.out.println("Current Element: " + element.getNodeName());
	
	            // Retrieve all attributes of the element
	            NamedNodeMap attributes = element.getAttributes();
	            for (int j = 0; j < attributes.getLength(); j++) {
	                Node attribute = attributes.item(j);
	                System.out.println("Attribute name: " + attribute.getNodeName() + ", Value: " + attribute.getNodeValue());
	            }
	        }
	    }
	}
}
