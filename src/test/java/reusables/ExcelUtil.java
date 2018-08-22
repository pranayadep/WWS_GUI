package reusables;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;

public class ExcelUtil {
	static Connection connection;

	public static void getSheet(String sheetPath) {
		Fillo fillo = new Fillo();
		try {
			connection = fillo.getConnection(sheetPath);
		} catch (FilloException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getData(String sheet , String fieldName) throws FilloException {
		String value = null;
		String strQuery = "select * from " + sheet +" where Key = '"+fieldName+"'";
		System.out.println("Query is " + strQuery);
		Recordset recordset = connection.executeQuery(strQuery);
		if (recordset.next()) {
			value = recordset.getField("value");
		} else {
			System.out.println("No such data available in the sheet");
		}
		return value;
	}
	
	public static String getTestCaseData(String sheet , String fieldName) throws FilloException {
		String value = null;
		String strQuery = "select * from " + sheet +" where "+fieldName+"= 'Y'";
		System.out.println("Query is " + strQuery);
		Recordset recordset = connection.executeQuery(strQuery);
		if (recordset.next()) {
			value = recordset.getField("TestCaseName");
		} else {
			System.out.println("No such data available in the sheet");
		}
		return value;
	}
}
