package bank.rabo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import bank.rabo.constant.FileextensionConstant;
import bank.rabo.util.FileprocessorUtil;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@SpringBootApplication(scanBasePackages = "bank.rabo")
public class AssignmentApplication implements CommandLineRunner{
	public static String destinationPath = null;

	public static void main(String[] args) {
		SpringApplication springApp = new SpringApplication(AssignmentApplication.class);
		springApp.setBannerMode(Banner.Mode.OFF);
		System.out.println("Hello World");
		springApp.run(args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		File srcDirectory = null;
		File destDirectory = null;
		List<String> listofCSVFiles = new ArrayList<>();
		List<String> listofXMLFiles = new ArrayList<>();
		Map<String, List<String>> map = new HashMap<>();
		StringBuilder sb = null;
		if (args.length > 0) {
			srcDirectory = new File(args[0]);
			destDirectory = new File(args[1]);
			destinationPath = args[1];
			
			if(!destDirectory.isDirectory()) {
				destDirectory.mkdir();
				System.out.println("Destination Directory not available, now creted");
			}
			
			if (srcDirectory.isDirectory() && srcDirectory.length() > 0) {
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
			}
			
			System.out.println("listofXMLFiles " +map);
			FileprocessorUtil.processFiles(map);
			
			

		}

	}
	

}
