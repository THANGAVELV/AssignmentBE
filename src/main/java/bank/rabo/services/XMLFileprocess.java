package bank.rabo.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import bank.rabo.model.Record;

@Service
public class XMLFileprocess implements Fileprocessor {

	@Override
	public void processFile(List<String> inputPath, String destinationPath) {
		Record record = null;
		List<Record> list = new ArrayList<>();
		for(String path : inputPath) {
		try {

			File fXmlFile = new File(path);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("record");

			// System.out.println("----------------------------");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				// System.out.println("\nCurrent Element :" + nNode.getNodeName());

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;

					record = new Record();

					record.setReference_No(Integer.valueOf(eElement.getAttribute("reference")));
					record.setAcc_No(eElement.getElementsByTagName("accountNumber").item(0).getTextContent());
					record.setDescription(eElement.getElementsByTagName("description").item(0).getTextContent());
					record.setStart_Bal(
							Double.valueOf(eElement.getElementsByTagName("startBalance").item(0).getTextContent()));
					record.setMutation(
							Double.valueOf(eElement.getElementsByTagName("mutation").item(0).getTextContent()));
					record.setEnd_Bal(
							Double.valueOf(eElement.getElementsByTagName("endBalance").item(0).getTextContent()));

					/*
					 * System.out.println("Reference : " + eElement.getAttribute("reference"));
					 * System.out.println( "Account No : " +
					 * eElement.getElementsByTagName("accountNumber").item(0).getTextContent());
					 * System.out.println( "Description : " +
					 * eElement.getElementsByTagName("description").item(0).getTextContent());
					 * System.out.println("Start Balance : " +
					 * eElement.getElementsByTagName("startBalance").item(0).getTextContent());
					 * System.out.println( "Mutation : " +
					 * eElement.getElementsByTagName("mutation").item(0).getTextContent());
					 * System.out.println( "End Balance : " +
					 * eElement.getElementsByTagName("endBalance").item(0).getTextContent());
					 */

				}
				list.add(record);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

		List<Record> duplicates = list.stream().collect(Collectors.groupingBy(Record::getReference_No)).entrySet()
				.stream().filter(e -> e.getValue().size() > 1).flatMap(e -> e.getValue().stream())
				.collect(Collectors.toList());

		 StringBuilder sb = new StringBuilder("Reference   Description                    Reason\r\n");
		 
		 System.out.println("sb " +sb.length());
		    
		    for(Record records: duplicates) {
		    	String str = records.getReference_No()+"      "+records.getDescription()+"                    "+"\r\n";
		    	sb.append(str);
		    }
			
			  if (sb.length() <= 41) sb.append("No duplicate in transaction reference");
			 

		try {
			Files.write(Paths.get(destinationPath+"\\"+"duplicateRecordinXML.txt"), sb.toString().getBytes());
			System.out.println("Duplicate records in XML Files generated");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
