package bank.rabo.services;

import org.springframework.stereotype.Service;

import bank.rabo.constant.FileextensionConstant;

@Service
public class FileobjectFactory {
	
	public Fileprocessor getInstance(String fileExtn) {
		Fileprocessor fileprocessor = null;
		if(fileExtn.equalsIgnoreCase(FileextensionConstant.CSV_EXTN)) {
			fileprocessor = new CSVFileprocess();
		}else if(fileExtn.equalsIgnoreCase(FileextensionConstant.XML_EXTN)) {
			fileprocessor = new XMLFileprocess();
		}		
		return fileprocessor;
	}

}
