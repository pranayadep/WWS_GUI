package MainClass;

import com.codoid.products.exception.FilloException;

import reusables.ExcelUtil;

public class SuiteRunner {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			ExcelUtil.getSheet("C:\\Users\\id848699\\workspace\\WWSGUI_Automation\\WWS_Test_Data.xlsx");
			System.out.println(ExcelUtil.getTestCaseData("TestCases", "Run"));
		} catch (FilloException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
