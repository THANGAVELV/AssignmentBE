package bank.rabo.util;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import bank.rabo.AssignmentApplication;
import bank.rabo.constant.FileextensionConstant;
import bank.rabo.model.Record;
import bank.rabo.services.FileobjectFactory;
import bank.rabo.services.Fileprocessor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileprocessorUtil {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FileprocessorUtil.class);
	
	public static Optional<String> getExtensionByStringHandling(String filename) {
		log.info("method getExtensionByStringHandling entering ");
		return Optional.ofNullable(filename).filter(f -> f.contains("."))
				.map(f -> f.substring(filename.lastIndexOf(".") + 1));
	}
	
	public static void processFiles(Map<String, List<String>> map) {
		log.info("method processFiles entering ");
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
		log.info("method processFiles existing ");
	}
	
	public static Map<String, List<String>> groupingFiles(File srcDirectory, List<String> listofCSVFiles, List<String> listofXMLFiles,
			Map<String, List<String>> map) {
		log.info("method groupingFiles entering ");
		StringBuilder sb;
		String[] listOffiles = srcDirectory.list();
		for (String fileName : listOffiles) {
			Optional<String> fileExtn = FileprocessorUtil.getExtensionByStringHandling(fileName);
			if(fileExtn.isPresent()) {
				if(fileExtn.get().equalsIgnoreCase(FileextensionConstant.CSV_EXTN)) {
					sb = new StringBuilder();
					sb.append(srcDirectory.getAbsolutePath()).append("\\"+fileName);
					listofCSVFiles.add(sb.toString());
					map.put(FileextensionConstant.CSV_EXTN, listofCSVFiles);
				}else if(fileExtn.get().equalsIgnoreCase(FileextensionConstant.XML_EXTN)) {
					sb = new StringBuilder();
					sb.append(srcDirectory.getAbsolutePath()).append("\\"+fileName);
					listofXMLFiles.add(sb.toString());
					map.put(FileextensionConstant.XML_EXTN, listofXMLFiles);
				}
			}
			
		}
		log.info("method groupingFiles existing ");
		return map;
	}
	
	public static void generateReport(final String destinationPath, final List<Record> records, final String reportName, String fileType) {
		/*
		 * List<Record> duplicates = records.stream()
		 * .collect(Collectors.groupingBy(Record::getReference_No)) .entrySet().stream()
		 * .filter(e->e.getValue().size() > 1) .flatMap(e->e.getValue().stream())
		 * .collect(Collectors.toList());
		 */
	    //duplicates.forEach(System.out::println);
		log.info("method generateReport entering ");
		List<Record> findDuplicateRecord = findDuplicareRefno(records);
		List<Record> validatedEndBalRecords = validateEndBalance(records);
	    
	    StringBuilder sb = new StringBuilder("Reference  Description\r\n");
	    if(findDuplicateRecord != null && findDuplicateRecord.size() > 0 || validatedEndBalRecords != null && validatedEndBalRecords.size() > 0) {
	    reportContentBuilder(findDuplicateRecord, sb);
	    reportContentBuilder(validatedEndBalRecords, sb);
	   // System.out.println("SB : "+sb);
	    }else {
	    	sb.append("No duplicate in transaction reference");
	    }
	    try {
	    	log.error("REPORT NAME : " + reportName);
			Files.write(Paths.get(destinationPath+reportName), sb.toString().getBytes());
			log.info("Duplicate records in "+fileType+" Files generated");
		} catch (IOException e) {
			e.printStackTrace();
		}
	    log.info("method generateReport existing ");
	}

	public static List<Record> findDuplicareRefno(final List<Record> records) {
		Set<BigInteger> items = new HashSet<>();
		List<Record> findDuplicateRecord = new ArrayList<>();
		for(Record records1 : records) {
			if(!items.add(records1.getReference_No())) {
				records1.setDuplicate("Yes");
				findDuplicateRecord.add(records1);
			}
		}
		return findDuplicateRecord;
	}

	private static void reportContentBuilder(List<Record> duplicates, StringBuilder sb) {
		log.info("method reportContentBuilder entering ");
		for(Record record: duplicates) {
	    	String str = record.getReference_No()+"     "+record.getDescription()+"\r\n";
	    	sb.append(str);
	    }
		log.info("method reportContentBuilder existing ");
	}
	
	public static List<Record> validateEndBalance(List<Record> records) {
		log.info("method validateEndBalance entering ");
		List<Record> validatedEndBalRecords = new ArrayList<>();
		BigDecimal sumOfstartAndMutBal = new BigDecimal(0);
		for(Record recordData : records) {
			BigDecimal startBal = recordData.getStart_Bal();
			BigDecimal mutation = recordData.getMutation();
			sumOfstartAndMutBal = startBal.add(mutation);
			
			if(recordData.getEnd_Bal().compareTo(sumOfstartAndMutBal) != 0) {
				recordData.setEndBalInvalid("No");
				validatedEndBalRecords.add(recordData);
			}
		}
		log.info("method validateEndBalance existing ");
		return validatedEndBalRecords;
		
	}
	
}
