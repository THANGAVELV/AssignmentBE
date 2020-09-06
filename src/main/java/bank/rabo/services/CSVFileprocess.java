package bank.rabo.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import bank.rabo.constant.FileextensionConstant;
import bank.rabo.model.Record;
import bank.rabo.util.FileprocessorUtil;

@Service
public class CSVFileprocess implements Fileprocessor{

	@Override
	public void processFile(List<String> inputPath, String destinationPath) {
		String reportName = "\\duplicateRecordsinCSV.txt";
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
	    
	    FileprocessorUtil.generateReport(destinationPath, records, reportName, FileextensionConstant.CSV_EXTN);
	    
	}


	public Function<String, Record> mapToItem = (line) -> {
		  String[] p = line.split(",");// a CSV has comma separated lines
		  Record record = new Record();
		  record.setReference_No(Integer.valueOf(p[0]));
		  record.setAcc_No(p[1]);
		  record.setDescription(p[2]);
		  record.setStart_Bal(new BigDecimal(p[3]));
		  record.setMutation(new BigDecimal(p[4]));
		  record.setEnd_Bal(new BigDecimal(p[5]));
		  
		  return record;
	};
	

}
