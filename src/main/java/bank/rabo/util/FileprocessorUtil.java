package bank.rabo.util;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import bank.rabo.AssignmentApplication;
import bank.rabo.constant.FileextensionConstant;
import bank.rabo.model.Record;
import bank.rabo.services.FileobjectFactory;
import bank.rabo.services.Fileprocessor;

public class FileprocessorUtil {
	
	
	public static Optional<String> getExtensionByStringHandling(String filename) {
		return Optional.ofNullable(filename).filter(f -> f.contains("."))
				.map(f -> f.substring(filename.lastIndexOf(".") + 1));
	}
	
	public static void processFiles(Map<String, List<String>> map) {
		FileobjectFactory fileobjectFactory = new FileobjectFactory();
		Fileprocessor fileprocessor = null;
		for(String fileExtn : map.keySet()) {
			if(fileExtn.equalsIgnoreCase(FileextensionConstant.CSV_EXTN)) {
				fileprocessor = fileobjectFactory.getInstance(fileExtn);
				fileprocessor.processFile(map.get(fileExtn), AssignmentApplication.destinationPath);
			}else if(fileExtn.equalsIgnoreCase(FileextensionConstant.XML_EXTN)) {
				fileprocessor = fileobjectFactory.getInstance(fileExtn);
				fileprocessor.processFile(map.get(fileExtn), AssignmentApplication.destinationPath);
			}
			
		}
	}
	
	public static Map<String, List<String>> groupingFiles(File srcDirectory, List<String> listofCSVFiles, List<String> listofXMLFiles,
			Map<String, List<String>> map, String... args) {
		StringBuilder sb;
		String[] listOffiles = srcDirectory.list();
		for (String fileName : listOffiles) {
			Optional<String> fileExtn = FileprocessorUtil.getExtensionByStringHandling(fileName);
			if(fileExtn.isPresent()) {
				if(fileExtn.get().equalsIgnoreCase(FileextensionConstant.CSV_EXTN)) {
					sb = new StringBuilder();
					sb.append(args[0]).append("\\"+fileName);
					listofCSVFiles.add(sb.toString());
					map.put(FileextensionConstant.CSV_EXTN, listofCSVFiles);
				}else if(fileExtn.get().equalsIgnoreCase(FileextensionConstant.XML_EXTN)) {
					sb = new StringBuilder();
					sb.append(args[0]).append("\\"+fileName);
					listofXMLFiles.add(sb.toString());
					map.put(FileextensionConstant.XML_EXTN, listofXMLFiles);
				}
			}
			
		}
		return map;
	}
	
	public static void generateReport(final String destinationPath, final List<Record> records, final String reportName, String fileType) {
		List<Record> duplicates = records.stream()
	    		  .collect(Collectors.groupingBy(Record::getReference_No))
	    		  .entrySet().stream()
	    		  .filter(e->e.getValue().size() > 1)
	    		  .flatMap(e->e.getValue().stream())
	    		  .collect(Collectors.toList());
	    //duplicates.forEach(System.out::println);
		
		List<Record> validatedEndBalRecords = validateEndBalance(records, fileType);
	    
	    StringBuilder sb = new StringBuilder("Reference		Description\r\n");
	    if(duplicates != null && duplicates.size() > 0 || validatedEndBalRecords != null && validatedEndBalRecords.size() > 0) {
	    reportContentBuilder(duplicates, sb);
	    reportContentBuilder(validatedEndBalRecords, sb);
	   // System.out.println("SB : "+sb);
	    }
	    if (sb.length() <= 41) sb.append("No duplicate in transaction reference");
	    try {
			Files.write(Paths.get(destinationPath+reportName), sb.toString().getBytes());
			System.out.println("Duplicate records in "+fileType+" Files generated");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void reportContentBuilder(List<Record> duplicates, StringBuilder sb) {
		for(Record record: duplicates) {
	    	String str = record.getReference_No()+"		"+record.getDescription()+"\r\n";
	    	sb.append(str);
	    }
	}
	
	private static List<Record> validateEndBalance(List<Record> records, String fileType) {
		List<Record> validatedEndBalRecords = new ArrayList<>();
		BigDecimal sumOfstartAndMutBal = new BigDecimal(0);
		for(Record recordData : records) {
			BigDecimal startBal = recordData.getStart_Bal();
			BigDecimal mutation = recordData.getMutation();
			sumOfstartAndMutBal = startBal.add(mutation);
			/*
			 * System.out.println("Start Balance : " +startBal);
			 * System.out.println("Mutation Balance : " +mutation);
			 * System.out.println("Sum of Start and Mutation Balance : "
			 * +sumOfstartAndMutBal); System.out.println("End Balance : "
			 * +recordData.getEnd_Bal());
			 */
			
			if(recordData.getEnd_Bal().compareTo(sumOfstartAndMutBal) != 0) {
				validatedEndBalRecords.add(recordData);
			}
			//System.out.println(fileType +"validatedEndBalRecords " +validatedEndBalRecords);
		}
		
		return validatedEndBalRecords;
		
	}
	
}
