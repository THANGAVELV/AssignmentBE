package bank.rabo.util;

import java.util.List;
import java.util.Map;
import java.util.Optional;


import bank.rabo.AssignmentApplication;
import bank.rabo.constant.FileextensionConstant;
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
	
}
