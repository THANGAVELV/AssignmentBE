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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import bank.rabo.constant.FileextensionConstant;
import bank.rabo.model.Record;
import bank.rabo.services.CSVFileprocess;
import bank.rabo.util.FileprocessorUtil;

@SpringBootTest(classes = AssignmentApplication.class)
class AssignmentApplicationTests {
	
	String args[] = {"C:\\Users\\tvenk\\Downloads\\Thangavel\\assignment-BE","C:\\Users\\tvenk\\Desktop\\Result"};
	File srcPath = new File(args[0]);
	File destPath = new File(args[1]);
	List<String> listofCSVFiles = new ArrayList<>();
	List<String> listofXMLFiles = new ArrayList<>();
	Map<String, List<String>> map = new HashMap<>();
	
	@Autowired
	FileprocessorUtil fileprocessorUtil;
	
	
	@Test
	void contextLoads()  {
	}
	
	@Test
	public void isDirectory() {
		Assertions.assertEquals(true, srcPath.isDirectory());
		Assertions.assertEquals(true, destPath.isDirectory());
		System.out.println("Directory True");
	}
	
	@SuppressWarnings("static-access")
	@Test
	public void grouptingFiles() {
		map = fileprocessorUtil.groupingFiles(srcPath, listofCSVFiles, listofXMLFiles, map);
		Assertions.assertEquals(map.size(), 2);
		assertThat(map.get(FileextensionConstant.CSV_EXTN), not(IsEmptyCollection.empty()));
		assertThat(map.get(FileextensionConstant.XML_EXTN), not(IsEmptyCollection.empty()));
		System.out.println("groptingFiles True");
	}
	
	@SuppressWarnings("static-access")
	@Test
	public void validateEndBalance() {
		System.out.println("validateEndBalance True");
		List<Record> records = new ArrayList<>();
		records.add(new Record(BigInteger.valueOf(194261), "NL91RABO0315273637","Clothes from Jan Bakker", BigDecimal.valueOf(21.6),BigDecimal.valueOf(-41.83),BigDecimal.valueOf(-20.23)));
		records.add(new Record(BigInteger.valueOf(194261), "NL91RABO0315273637","Clothes from Jan Bakker", BigDecimal.valueOf(21.6),BigDecimal.valueOf(-41.83),BigDecimal.valueOf(20.23)));
		records.add(new Record(BigInteger.valueOf(112806), "NL27SNSB0917829871","Clothes for Willem Dekker", BigDecimal.valueOf(91.23),BigDecimal.valueOf(+15.57),BigDecimal.valueOf(106.8)));
		records = fileprocessorUtil.validateEndBalance(records);
		System.out.println("records " +records.size());
		Assertions.assertEquals(1, records.size());
		
	}
	

}
