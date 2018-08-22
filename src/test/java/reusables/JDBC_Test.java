package reusables;

import java.sql.*;

public class JDBC_Test {

	static final String URL = "jdbc:oracle:thin:wws_ro/wws_ro@IP-TORA131:1540/WWS02T";
	static String URL1;
	static String sqlQuery, coId = "", flag = "";

	static void getConn(String connString) {
		try {
			// URL1 = ExcelUtil.getData("Credentials", "ConnectionString");//
			// "jdbc:oracle:thin:wws_ro/wws_ro@IP-TORA131:1540/WWS02T
			// ";
			URL1 = connString;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getCoid(String connString) {
		getConn(connString);
		sqlQuery = "select CORRELATIONID from pr_bgc_cws_wws_data_wwsjmsresp order by PXSAVEDATETIME desc";
		coId = main(sqlQuery);
		return coId;
	}

	public static String getData(String connString, String query) throws InterruptedException {
		getConn(connString);
		for (int i = 0; i < 5; i++) {
			coId = main(query);
			if ((coId.equalsIgnoreCase("null")) || (coId.equalsIgnoreCase(null))) {
				System.out.println("result is null running again for " + i + "time");
				Thread.sleep(1000);
			} else {
				break;
			}
		}
		if (coId.equalsIgnoreCase("null")) {
			return "";
		} else {
			return coId;
		}
	}

	public static String getRespFlag(String connString, String query) {
		getConn(connString);
		System.out.println(query);
		flag = main(query);
		return flag;
	}

	public static String main(String query) {
		Connection conn = null;
		Statement stmt = null;
		String ID = "null";
		try {

			// Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(URL1);
			System.out.println("Connected to DB");
			// Execute a query
			System.out.println("Creating statement...");
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			// Extract data from result set
			if (rs.next()) {
				// Retrieve by column name
				ID = rs.getString(1);
				System.out.println("DB value is : " + ID);
			} else {
				System.out.println("Result is null");
				return "null";
			}
			// Clean-up environment
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			} // nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			} // end finally try
		} // end try
		System.out.println("Connection Closed");
		return ID;
	}// end main
}
