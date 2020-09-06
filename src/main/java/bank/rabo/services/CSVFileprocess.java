package bank.rabo.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CSVFileprocess implements Fileprocessor{
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CSVFileprocess.class);
	
	@Override
	public void processFile(List<String> inputPath, String destinationPath) {
		log.info("method processFile entering ");
		int reportNo = 1;
		String reportName = null;
	    List<Record> records = null;
	    for(String filePath : inputPath) {
	    	records = new ArrayList<Record>();
	    try{
	      File inputF = new File(filePath);
	      InputStream inputFS = new FileInputStream(inputF);
	      BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
	      // skip the header of the csv
	      records = br.lines().skip(1).map(mapToItem).collect(Collectors.toList());
	      reportName = "\\duplicateRecordsinCSV"+reportNo+".txt";
	      FileprocessorUtil.generateReport(destinationPath, records, reportName, FileextensionConstant.CSV_EXTN);
	      br.close();
	      reportNo++;
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	  }
	    
	    log.info("method processFile entering ");
	    
	}


	public Function<String, Record> mapToItem = (line) -> {
		log.info("method mapToItem entering ");
		  String[] p = line.split(",");// a CSV has comma separated lines
		  Record record = new Record();
		  record.setReference_No(new BigInteger(p[0]));
		  record.setAcc_No(p[1]);
		  record.setDescription(p[2]);
		  record.setStart_Bal(new BigDecimal(p[3]));
		  record.setMutation(new BigDecimal(p[4]));
		  record.setEnd_Bal(new BigDecimal(p[5]));
		  log.info("method mapToItem entering ");
		  return record;
	};
	

}
