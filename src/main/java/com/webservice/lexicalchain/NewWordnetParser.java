//parser for kemal oflazer's turkish wordnet
package com.webservice.lexicalchain;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.DocumentBuilder;
//import org.w3c.dom.Document;
//import org.w3c.dom.NodeList;
//import org.w3c.dom.Node;
//import org.w3c.dom.Element;
//import java.io.File;
//
//public class NewWordnetParser {
//
//  public static void main(String argv[]) {
//
//    try {
//
//	File fXmlFile = new File("src/test/res/datasets/lexical/wordnet/tur.xml");
//	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//	Document doc = dBuilder.parse(fXmlFile);
//			
//	//optional, but recommended
//	//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
//	doc.getDocumentElement().normalize();
//
//	System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
//			
//	NodeList nList = doc.getElementsByTagName("SYNSET");
//			
//	System.out.println("----------------------------");
//
//	for (int temp = 0; temp < nList.getLength(); temp++) {
//
//		Node nNode = nList.item(temp);
//				
//		System.out.println("\nCurrent Element :" + nNode.getNodeName());
//				
//		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
//
//			Element eElement = (Element) nNode;
//
//			System.out.println("Synonym: " + eElement.getAttribute("SYNONYM"));
//			System.out.println("Literal : " + eElement.getElementsByTagName("LITERAL").item(0).getTextContent());
//
//		}
//	}
//    } catch (Exception e) {
//	e.printStackTrace();
//    }
//  }
//
//}

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class NewWordnetParser {
	public static void main(String[] args) throws ParserConfigurationException, IOException {
		// NewWordnetParser newWordnetParser = new NewWordnetParser();
		// NodeList nodeList = newWordnetParser.readXml();
		// Set synonyms = newWordnetParser.getSynonyms(nodeList);
		// String filename =
		// "src/test/res/datasets/lexical/wordnet/synonyms.csv";
		// synonyms = newWordnetParser.cleanStopWords(synonyms);
		// newWordnetParser.writeNewList(synonyms, filename);

		NewWordnetParser newWordnetParser = new NewWordnetParser();
		NodeList nodeList = newWordnetParser.readXml();
		Set<String> relations = newWordnetParser.getRelationIDs(nodeList);
		Set<String> relationNames = newWordnetParser.getRelationsName(relations, nodeList);
		Set<String> relationIDsWithNames = newWordnetParser.setRelations(relations, relationNames);
		String filename = "src/main/resources/datasets/lexical/wordnet/relationIdsWithNames.csv";
		newWordnetParser.writeNewList(relationIDsWithNames, filename);
	}

	public void writeNewList(Set<String> sentences, String filename) throws IOException {
		StringBuilder sb = new StringBuilder();

		for (String sentence : sentences) {
			sb.append(sentence.toLowerCase());
			sb.append(System.lineSeparator());
		}

		String text = sb.toString();
		BufferedWriter writer = null;
		writer = new BufferedWriter(new FileWriter(filename));
		writer.write(text);
		writer.flush();
		writer.close();
	}

	public String readTextFile(String fileName) throws IOException {
		BufferedReader input = new BufferedReader(new FileReader(fileName));
		StringBuilder sb = new StringBuilder();
		String line = input.readLine();
		while ((line != null)) // metin dosyası satır satır okundu.
		{
			sb.append(line.toLowerCase());
			sb.append(System.lineSeparator());
			line = input.readLine();
		}
		input.close();
		String text = sb.toString();
		return text;
	}

	public Set<String> cleanStopWords(Set<String> words) throws IOException {
		Set<String> cleanedSet = new HashSet<String>();
		String stopWordsFilename = "src/main/resources/datasets/stopwordsTurkish.txt";
		String stopWordsText = readTextFile(stopWordsFilename);

		String stopWords[] = stopWordsText.split("\\r?\\n");
		List<String> stopWordSet = new ArrayList<String>(Arrays.asList(stopWords));

		for (String s : words) {
			if (!stopWordSet.contains(s.split(":")[0]))
				cleanedSet.add(s);
		}

		return cleanedSet;
	}

	public Set<String> getSynonyms(NodeList nodeList) {
		Set<String> synonyms = new HashSet<String>();
		System.out.println("----------------------------");

		for (int temp = 0; temp < nodeList.getLength(); temp++) {

			Node nNode = nodeList.item(temp);

			System.out.println("\nCurrent Element :" + nNode.getNodeName());

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement = (Element) nNode;
				NodeList n = eElement.getChildNodes();
				if (eElement.getElementsByTagName("POS").item(0).getTextContent().equals("n")) {
					for (int i = 0; i < n.getLength(); i++) {
						Node ntemp = n.item(i);

						if (ntemp.getNodeName().equals("SYNONYM")) {
							if (eElement.getElementsByTagName("SYNONYM").item(0).getTextContent() != null) {
								// System.out.println("Synonym: " +
								// eElement.getElementsByTagName("SYNONYM").item(0).getTextContent());
								NodeList literals = eElement.getElementsByTagName("LITERAL");
								System.out.println(literals.getLength());
								if (literals.getLength() > 2) {
									Node templiteral = literals.item(0);
									String tempWord = templiteral.getTextContent();
									tempWord = tempWord.replaceAll("\\d", "").replaceAll("\\r?\\n", "")
											.replaceAll("\\t", "");
									System.out.println(tempWord);
									for (int j = 1; j < literals.getLength(); j++) {
										templiteral = literals.item(j);
										// System.out.println(templiteral.getTextContent());
										String templiteraltext = templiteral.getTextContent();
										templiteraltext = templiteraltext.replaceAll("\\d", "")
												.replaceAll("\\r?\\n", "").replaceAll("\\t", "");
										System.out.println(templiteraltext);
										synonyms.add(tempWord + ":SYNONYM:" + templiteraltext);
									}
								}
							}
						}
					}
				}
			}
		}

		return synonyms;
	}

	public Set<String> getRelationIDs(NodeList nodeList) {
		Set<String> relations = new HashSet<String>();
		System.out.println("----------------------------");

		for (int temp = 0; temp < nodeList.getLength(); temp++) {

			Node nNode = nodeList.item(temp);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement = (Element) nNode;
				NodeList n = eElement.getChildNodes();
				if (eElement.getElementsByTagName("POS").item(0).getTextContent().equals("n")) {
					String ntempID = "";
					for (int i = 0; i < n.getLength(); i++) {
						Node ntemp = n.item(i);
						if (ntemp.getNodeName().equals("ID")) {
							ntempID = ntemp.getTextContent().replaceAll("\\r?\\n", "").replaceAll("\\t", "");
						}
						if (ntemp.getNodeName().equals("ILR")) {
							String nrelationID = ntemp.getTextContent().split("\t")[0];
							if (eElement.getElementsByTagName("TYPE").item(0).getTextContent() != null) {
								// System.out.println("Synonym: " +
								// eElement.getElementsByTagName("SYNONYM").item(0).getTextContent());
								NodeList literals = eElement.getElementsByTagName("TYPE");
								for (int j = 1; j < literals.getLength(); j++) {
									String relation = literals.item(j).getTextContent().replaceAll("\\r?\\n", "")
											.replaceAll("\\t", "");
									String sentence = ntempID + ":" + relation + ":" + nrelationID;
									relations.add(sentence.replaceAll("\\r?\\n", ""));
								}
							}
						}
					}
				}
			}
		}
		return relations;

	}

	public Set<String> getRelationsName(Set<String> relationIDs, NodeList nodeList) {
		Set<String> relations = new HashSet<String>();

		for (String tempRel : relationIDs) {
			String ID = tempRel.split(":")[0];
			String relationID = tempRel.split(":")[2];
			for (int temp = 0; temp < nodeList.getLength(); temp++) {

				Node nNode = nodeList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
					NodeList n = eElement.getChildNodes();
					if (!eElement.getElementsByTagName("POS").item(0).getTextContent().equals("v")) {
						String ntempID = "";
						boolean equal = false;
						for (int i = 0; i < n.getLength(); i++) {
							Node ntemp = n.item(i);
							if (ntemp.getNodeName().equals("ID")) {
								ntempID = ntemp.getTextContent().replaceAll("\\r?\\n", "").replaceAll("\\t", "");
								if (ntempID.equalsIgnoreCase(ID) || ntempID.equalsIgnoreCase(relationID)) {
									equal = true;
								}
							}
							if (ntemp.getNodeName().equals("SYNONYM") && equal) {
								if (eElement.getElementsByTagName("SYNONYM").item(0).getTextContent() != null) {
									// System.out.println("Synonym: " +
									// eElement.getElementsByTagName("SYNONYM").item(0).getTextContent());
									NodeList literals = eElement.getElementsByTagName("LITERAL");
									if (literals.getLength() > 0) {
										Node templiteral = literals.item(0);
										String tempWord = templiteral.getTextContent();
										tempWord = tempWord.replaceAll("\\d", "").replaceAll("\\r?\\n", "")
												.replaceAll("\\t", "");
										relations.add(ntempID + ":" + tempWord);
									}
								}
							}
						}
					}
				}
			}
		}
		return relations;
	}
	
	public Set<String> setRelations(Set<String> relationIDs,Set<String> words){
		Set<String> relationWithNames = new HashSet<String>();
		for(String temp:relationIDs){
			String tmpArr[] = temp.split(":");
			String ID = tmpArr[0];
			String relation = tmpArr[1];
			String relationID = tmpArr[2];
			boolean boolID = false;
			boolean boolRelationID = false;
			for(String word : words){
				String tmpID = word.split(":")[0];
				String tmpWord = word.split(":")[1];
				if(tmpID.equals(ID)){
					ID=tmpWord;
					boolID = true;
				}
				else if(tmpID.equals(relationID))
				{
					boolRelationID = true;
					relationID = tmpWord;
				}
				if(boolID && boolRelationID)
				{
					relationWithNames.add(ID+":"+relation+":"+relationID);
					break;
				}
			}
		}
		return relationWithNames;
	}

	public NodeList readXml() throws ParserConfigurationException {
		File fXmlFile = new File("src/main/resources/datasets/lexical/wordnet/tur.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = null;
		try {
			doc = dBuilder.parse(fXmlFile);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		doc.getDocumentElement().normalize();

		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

		NodeList nList = doc.getElementsByTagName("SYNSET");
		return nList;
		//
		// System.out.println("----------------------------");
		//
		// for (int temp = 0; temp < nList.getLength(); temp++) {
		//
		// Node nNode = nList.item(temp);
		//
		// System.out.println("\nCurrent Element :" + nNode.getNodeName());
		//
		// if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		//
		// Element eElement = (Element) nNode;
		//
		// System.out.println("Item No : " +
		// eElement.getElementsByTagName("SYNONYM").item(0).getTextContent());
		// System.out
		// .println("Description : " +
		// eElement.getElementsByTagName("LITERAL").item(0).getTextContent());
		// System.out.println("price : " +
		// eElement.getElementsByTagName("SNOTE").item(0).getTextContent());
		//
		// }
		// }
	}
}