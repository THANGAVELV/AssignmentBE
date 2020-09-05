package bank.rabo.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import bank.rabo.model.Record;

@Service
public class CSVFileprocess implements Fileprocessor{

	@Override
	public void processFile(List<String> inputPath, String destinationPath) {
		
	    List<Record> records = new ArrayList<Record>();
	    for(String filePath : inputPath) {
	    try{
	      File inputF = new File(filePath);
	      InputStream inputFS = new FileInputStream(inputF);
	      BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
	      // skip the header of the csv
	      records = br.lines().skip(1).map(mapToItem).collect(Collectors.toList());
	      br.close();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	  }
	    
	    List<Record> duplicates = records.stream()
	    		  .collect(Collectors.groupingBy(Record::getReference_No))
	    		  .entrySet().stream()
	    		  .filter(e->e.getValue().size() > 1)
	    		  .flatMap(e->e.getValue().stream())
	    		  .collect(Collectors.toList());
	    //duplicates.forEach(System.out::println);
	    
	    StringBuilder sb = new StringBuilder("Reference   Description                    Reason\r\n");
	    
	    for(Record record: duplicates) {
	    	String str = record.getReference_No()+"      "+record.getDescription()+"                    "+"\r\n";
	    	sb.append(str);
	    }
	    if (sb.length() <= 41) sb.append("No duplicate in transaction reference");
	    try {
			Files.write(Paths.get(destinationPath+"\\"+"duplicateRecordsinCSV.txt"), sb.toString().getBytes());
			System.out.println("Duplicate records in CSV Files generated");
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	}
		
	
	public Function<String, Record> mapToItem = (line) -> {
		  String[] p = line.split(",");// a CSV has comma separated lines
		  Record record = new Record();
		  record.setReference_No(Integer.valueOf(p[0]));
		  record.setAcc_No(p[1]);
		  record.setDescription(p[2]);
		  record.setStart_Bal(Double.valueOf(p[3]));
		  record.setMutation(Double.valueOf(p[4]));
		  record.setEnd_Bal(Double.valueOf(p[5]));
		  
		  return record;
	};

}
