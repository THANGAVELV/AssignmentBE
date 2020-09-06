package bank.rabo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import bank.rabo.constant.FileextensionConstant;
import bank.rabo.util.FileprocessorUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication(scanBasePackages = "bank.rabo")
public class AssignmentApplication implements CommandLineRunner {
	public static String destinationPath = null;

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AssignmentApplication.class);

	public static void main(String[] args) {
		SpringApplication springApp = new SpringApplication(AssignmentApplication.class);
		springApp.setBannerMode(Banner.Mode.OFF);
		springApp.run(args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("Main run method entering");
		File srcDirectory = null;
		File destDirectory = null;
		List<String> listofCSVFiles = new ArrayList<>();
		List<String> listofXMLFiles = new ArrayList<>();
		Map<String, List<String>> map = new HashMap<>();
		if (args.length > 0) {
			srcDirectory = new File(args[0]);
			destDirectory = new File(args[1]);
			destinationPath = args[1];

			if (!destDirectory.isDirectory()) {	
				destDirectory.mkdir();
				log.info("Destination Directory not available, now creted");
			}

			if (srcDirectory.isDirectory() && srcDirectory.length() > 0) {
				map = FileprocessorUtil.groupingFiles(srcDirectory, listofCSVFiles, listofXMLFiles, map);
				if (map != null && map.size() > 0) {
					listofCSVFiles = map.get(FileextensionConstant.CSV_EXTN);
					listofXMLFiles = map.get(FileextensionConstant.XML_EXTN);
				}
			}

			FileprocessorUtil.processFiles(map);
			log.info("Main run method ending");

		}else {
            log.error("Need argument 1 as input full file path and argument 2 output full folder path");
		}

	}

}
