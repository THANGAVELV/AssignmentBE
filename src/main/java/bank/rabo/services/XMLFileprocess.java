package bank.rabo.services;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
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

import bank.rabo.constant.FileextensionConstant;
import bank.rabo.model.Record;
import bank.rabo.util.FileprocessorUtil;

@Service
public class XMLFileprocess implements Fileprocessor {

	@Override
	public void processFile(List<String> inputPath, String destinationPath) {
		Record record = null;
		List<Record> listOfRecords = new ArrayList<>();
		String reportName = "\\duplicateRecordsinXML.txt";
		for (String path : inputPath) {
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
								new BigDecimal(eElement.getElementsByTagName("startBalance").item(0).getTextContent()));
						record.setMutation(
								new BigDecimal(eElement.getElementsByTagName("mutation").item(0).getTextContent()));
						record.setEnd_Bal(
								new BigDecimal(eElement.getElementsByTagName("endBalance").item(0).getTextContent()));

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
					listOfRecords.add(record);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		FileprocessorUtil.generateReport(destinationPath, listOfRecords, reportName, FileextensionConstant.XML_EXTN);

	}
}
