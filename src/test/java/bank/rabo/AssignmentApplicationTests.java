package bank.rabo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hamcrest.collection.IsEmptyCollection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import bank.rabo.constant.FileextensionConstant;
import bank.rabo.model.Record;
import bank.rabo.util.FileprocessorUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(classes = AssignmentApplication.class)
class AssignmentApplicationTests {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AssignmentApplication.class);
	String args[] = {"C:\\Users\\tvenk\\Downloads\\Thangavel\\assignment-BE","C:\\Users\\tvenk\\Desktop\\Result"};
	File srcPath = new File(args[0]);
	File destPath = new File(args[1]);
	List<String> listofCSVFiles = new ArrayList<>();
	List<String> listofXMLFiles = new ArrayList<>();
	Map<String, List<String>> map = new HashMap<>();
	
	
	@Test
	void contextLoads()  {
	}
	
	@Test
	public void isDirectory() {
		log.info("Check is Directory Test Case");
		Assertions.assertEquals(true, srcPath.isDirectory());
		Assertions.assertEquals(true, destPath.isDirectory());
		log.info("Directory True");
	}
	
	@Test
	public void checkValidfileExtntion() {
		log.info("Check Valid File Extension Test Case");
		map = FileprocessorUtil.groupingFiles(srcPath, listofCSVFiles, listofXMLFiles, map);
		String CSV_EXTN = "";
		String XML_EXTN = "";
		Assertions.assertEquals(map.size(), 2);
		for(String fileExtn : map.keySet()) {
			if(fileExtn.equalsIgnoreCase(FileextensionConstant.XML_EXTN))
				XML_EXTN = fileExtn;
			else
				CSV_EXTN = fileExtn;
		}
		log.info(CSV_EXTN);
		log.info(XML_EXTN);
		Assertions.assertEquals(FileextensionConstant.CSV_EXTN, CSV_EXTN);
		Assertions.assertEquals(FileextensionConstant.XML_EXTN, XML_EXTN);
	}
	
	@Test
	public void grouptingFiles() {
		log.info("Check Grouping Files Test Case");
		map = FileprocessorUtil.groupingFiles(srcPath, listofCSVFiles, listofXMLFiles, map);
		Assertions.assertEquals(map.size(), 2);
		assertThat(map.get(FileextensionConstant.CSV_EXTN), not(IsEmptyCollection.empty()));
		assertThat(map.get(FileextensionConstant.XML_EXTN), not(IsEmptyCollection.empty()));
	}
	
	@Test
	public void findDuplicateReference() {
		log.info("Check Duplicate Reference Number Test Case");
		List<Record> records = mockObject();
		List<Record> findDuplicateRecord = FileprocessorUtil.findDuplicareRefno(records);
		Assertions.assertEquals(findDuplicateRecord.size() > 0, records.size() > 0);
	}
	
	@Test
	public void validateEndBalance() {
		log.info("Check valid End Balance or not Test Case");
		System.out.println("validateEndBalance");
		List<Record> records = mockObject();
		records = FileprocessorUtil.validateEndBalance(records);
		log.info("records " +records.size());
		Assertions.assertEquals(1, records.size());
		
	}

	private List<Record> mockObject() {
		List<Record> records = new ArrayList<>();
		records.add(new Record(BigInteger.valueOf(194261), "NL91RABO0315273637","Clothes from Jan Bakker", BigDecimal.valueOf(21.6),BigDecimal.valueOf(-41.83),BigDecimal.valueOf(-20.23)));
		records.add(new Record(BigInteger.valueOf(194261), "NL91RABO0315273637","Clothes from Jan Bakker", BigDecimal.valueOf(21.6),BigDecimal.valueOf(-41.83),BigDecimal.valueOf(20.23)));
		records.add(new Record(BigInteger.valueOf(112806), "NL27SNSB0917829871","Clothes for Willem Dekker", BigDecimal.valueOf(91.23),BigDecimal.valueOf(+15.57),BigDecimal.valueOf(106.8)));
		return records;
	}
	

}
