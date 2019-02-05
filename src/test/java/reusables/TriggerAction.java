package com.proximus.salto.autopicking;

import org.testng.annotations.Test;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
//import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;

//import com.mysql.jdbc.Connection;
import com.proximus.salto.keywords.Modify;

import com.proximus.salto.keywords.ProvisionProductsWithCustomizations;

public class TriggerAction {

	public static String fileConfig = "";
	public static String ABOfileConfig = "";
	// public static String QuoteOrOrder="ERROR";
	public static String QuoteOrOrder = "ERROR";
	public static String BillProfile = "0";
	public static String CustomerID = "";
	public static String tcfilename = "";
	public boolean ABOflag;
	public static String Action = "";
	public static String SheetSelection = "";
	public static int QueueIter = 1;
	public static String TCID = "";
	public static String ReqID = "";
	public static String FileName = "";
	public static String OrderStatus = "";
	// public static String CusID="";
	public static String OrderDate = "1";
	public String ReqServerAddr = "";
	public static String ResServerAddr = "";
	public static String ServerAddr = "";
	public static String AckServerAddr = "";
	public static String NPSWOLoc = "";
	public static String Env = "";
	public static String Flow = "";
	public static String ENV = "";
	public static String strDBuserName = "";
	public static String strDBPassword = "";
	public static String strOMSorderRef = "";
	public static String strActualOAGID = "";
	public static String strNPSConnString = "";
	public static String strOMSConnString = "";
	public static String strNPSUserID = "";
	public static String strOMSUserID = "";
	public static String strNPSPassword = "";
	public static String strOMSPassword = "";
	public static String strNPSDB = "";
	public static String strOMSDB = "";
	public static String MachineName = "";
	public static String RegSheetName = "";
	public static String strConfigFilePath = "C:\\data\\Automation\\OneSellingAutomation\\RAF_config.properties";
	public static Connection con = null;
	public static Statement stmt = null;

	public void readConfigValues() {
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream(strConfigFilePath);
			prop.load(input);
			ReqServerAddr = prop.getProperty("ReqPickServerAddress");
			ResServerAddr = prop.getProperty("ResponseServerAddress");
			ServerAddr = prop.getProperty("ServAddr");
			AckServerAddr = prop.getProperty("AcknowledgeServAddr");
			NPSWOLoc = prop.getProperty("NPSWOLoc");
			Env = prop.getProperty("Environment");
			MachineName = System.getenv().get("COMPUTERNAME");
			System.out.println(MachineName);
			RegSheetName = prop.getProperty("RegSheetName");
			System.out.println(RegSheetName);
			Class.forName("com.mysql.jdbc.Driver");
			con = (Connection) DriverManager.getConnection("jdbc:mysql://bgc-be-qtp197:3306/e2eregression", "root", "");
			stmt = (Statement) con.createStatement();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void SetExecutingMachine(String Reqid, String TableName) throws SQLException {
		stmt.executeUpdate("Update InterimQ set ExecutingMachine=NULL where ExecutingMachine='" + MachineName + "';");
		stmt.executeUpdate("Update ReqQ set ExecutingMachine=NULL where ExecutingMachine='" + MachineName + "'");
		if (Reqid != null)
			stmt.executeUpdate(
					"Update " + TableName + " set ExecutingMachine='" + MachineName + "' where ReqID='" + Reqid + "'");
	}

	@SuppressWarnings({ "unused", "static-access", "resource" })
	@Test
	public void Triggerfn() throws FilloException, IOException, InterruptedException, SQLException {
		String[] TCIDs;
		String finalStatus = "";
		this.readConfigValues();
		String[] TCs;
		String tempOrdrDT = "";
		Date date = Calendar.getInstance().getTime();
		SimpleDateFormat Ordt = new SimpleDateFormat("ddMMyyyy");
		tempOrdrDT = Ordt.format(date);
		SetExecutingMachine(null, null);
		try {

		//	 checkSTPQue();
			setTimestampsforV1();

			ResultSet InterimQueResult = stmt
					.executeQuery("Select * from InterimQ where ExecutionStatus='Ready' and ExecutingMachine IS NULL");
			try {
				System.out.println("Checking Interim Que");
				while (InterimQueResult.next() && ChronoUnit.HOURS.between(LocalDateTime.now(),
						LocalDateTime.parse(InterimQueResult.getString("ENTRYTIME").toString())) < -24) {
					finalStatus = "D";
					Flow = "";
					try {
						ReqID = InterimQueResult.getString(1);
						TCID = InterimQueResult.getString(2);
						CustomerID = InterimQueResult.getString(3);
						BillProfile = InterimQueResult.getString(4);
						QuoteOrOrder = "ERROR";
						Action = InterimQueResult.getString("NextAction");
						ENV = InterimQueResult.getString("ENV");
						Flow = InterimQueResult.getString("FLow");
						FileName = InterimQueResult.getString("ReqFN");
						SetExecutingMachine(ReqID, "InterimQ");
						if (Action.equalsIgnoreCase("Add") || Action.equalsIgnoreCase("Cease")
								|| Action.equalsIgnoreCase("Swap") || Action.equalsIgnoreCase("Modify")
								|| Action.contains("Modify") || Action.contains("Swap")
								|| Action.equalsIgnoreCase("Cease") || Action.contains("Cease")
								|| Action.contains("Subscriber") || Action.contains("SN"))
							SheetSelection = "Modify";
						else
							SheetSelection = Action;
						String strQuery = "Select * from " + SheetSelection + " where TCID='" + TCID + "'";
						// Recordset RS=con.executeQuery(strQuery);
						Fillo f = new Fillo();
						com.codoid.products.fillo.Connection TCIDcon = f
								.getConnection(ServerAddr + "\\" + RegSheetName);
						Recordset RS = TCIDcon.executeQuery(strQuery);
						RS.moveFirst();
						String strConf = "";
						for (int k = 1; k < RS.getFieldNames().size(); k++) {
							strConf = strConf + RS.getField(k).value().trim();
							if (k != RS.getFieldNames().size() - 1)
								strConf = strConf + ";";
						}
						TCIDcon.close();
						if (Action.equalsIgnoreCase("Move")) {
							QuoteOrOrder = "ERROR";
							// com.codoid.products.fillo.Connection con =
							// f.getConnection(ServerAddr+""+RegSheetName);
							// Move M=new Move();
							// M.MoveMaster(CustomerID+";"+strConf);
							// M.MoveMaster(str);

						} else if (SheetSelection.equalsIgnoreCase("Modify")) {
							QuoteOrOrder = "ERROR";
							Modify Mod = new Modify();
							Mod.strEnvironment = ENV;
							Mod.ModifyMaster(CustomerID + ";" + strConf);
							// QuoteOrOrder=QuoteOrOrder+"2";
						}

					} catch (Exception e) {
						System.out.println(e);
					} finally {
						OrderDate = tempOrdrDT;
						// if(!Files.exists(Paths.get(ResServerAddr+"\\"+tcfilename)))
						// Files.createFile(Paths.get(ResServerAddr+"\\"+tcfilename));
						if (QuoteOrOrder == null)
							QuoteOrOrder = "ERROR";
						if (QuoteOrOrder.equalsIgnoreCase("null"))
							QuoteOrOrder = "ERROR";
						if (QuoteOrOrder.equalsIgnoreCase(""))
							QuoteOrOrder = "ERROR";

						updateSTPQue(TCID, ReqID, Action, BillProfile, CustomerID, QuoteOrOrder, FileName, OrderDate,
								ENV, Flow);
						stmt.executeUpdate("Update InterimQ set ABOorderID='" + QuoteOrOrder
								+ "',ExecutionStatus='Picked' where ReqID='" + ReqID + "'");
						SetExecutingMachine(null, "InterimQ");
						SendOrderForNPS(QuoteOrOrder);
					}
					InterimQueResult = stmt.executeQuery(
							"Select * from InterimQ where ExecutionStatus='Ready' and ExecutingMachine IS NULL");
				}
			} catch (Exception e) {
			}

			System.out.println("Checking Req Que");
			String ActualTCID = "";
			ResultSet ReqQue = stmt
					.executeQuery("Select * from ReqQ where OrderID is NULL and ExecutingMachine IS null");
			while (ReqQue.next()) {
				ABOflag = false;
				boolean SecPhs = false;
				boolean BiOrder = false;
				QuoteOrOrder = "ERROR";
				ENV = "";
				Flow = "";
				try {
					QuoteOrOrder = "ERROR";
					BillProfile = "0";
					// String[] tempTC = TCs[TCiter].split("\\|");
					ReqID = ReqQue.getString(1);
					TCID = ReqQue.getString(2);
					Action = ReqQue.getString(3);
					OrderStatus = ReqQue.getString(4);
					CustomerID = ReqQue.getString(5); //
					OrderDate = ReqQue.getString(6);
					FileName = ReqQue.getString("ReqFN");
					ENV = ReqQue.getString("ENV");
					SetExecutingMachine(ReqID, "ReqQ");
					if (OrderStatus.equalsIgnoreCase("I"))
						SecPhs = true;
					if (SecPhs) {
						System.out.println("Updating abo que with tcid,reqid,cusid:" + TCID + "," + ReqID + ","
								+ CustomerID + " respectively");
						stmt.executeUpdate("Update InterimQ set ExecutionStatus='Ready' where ReqID='" + ReqID + "'");
						stmt.executeUpdate(
								"Update ReqQ set OrderID='ABOPicked' where ReqID='" + ReqID + "' and Stepkey='I'");
					}
					if (OrderStatus.equalsIgnoreCase("S")) {
						String tempTCID = "";
						SheetSelection = "Provide";
						if (TCID.contains("B")) {
							BiOrder = true;
							tempTCID = TCID.replaceAll("B", "A");
						} else
							tempTCID = TCID;
						// TCID=TCID.replaceAll("A", "");
						Fillo f = new Fillo();
						com.codoid.products.fillo.Connection TCIDcon = f
								.getConnection(ServerAddr + "\\" + RegSheetName);
						String strQuery = "Select * from " + SheetSelection + " where TCID='" + tempTCID + "'";
						Recordset RS = TCIDcon.executeQuery(strQuery);
						RS.moveFirst();
						String[] str = new String[RS.getFieldNames().toArray().length - 1];
						String strConf = "";
						for (int k = 1; k < RS.getFieldNames().size(); k++) {
							if (RS.getField(k).value().trim().equalsIgnoreCase("V1"))
								Flow = "V1";
							else if (RS.getField(k).value().trim().equalsIgnoreCase("V2"))
								Flow = "V2";

							strConf = strConf + RS.getField(k).value().trim();
							if (k != RS.getFieldNames().size() - 1)
								strConf = strConf + ";";
						}
						TCIDcon.close();
						if (Action.equalsIgnoreCase("Provide") && !BiOrder) {
							finalStatus = "D";
						} else
							finalStatus = "I";
						ProvisionProductsWithCustomizations Provide = new ProvisionProductsWithCustomizations();
						Provide.strEnvironment = ENV;
						System.out.println(strConf);
						Provide.ProvisionMaster(CustomerID + ";" + strConf);

					}

				} catch (Exception e) {
					System.out.println(e);
				} finally {
					if (!SecPhs) {
						OrderDate = tempOrdrDT;
						if (QuoteOrOrder == null)
							QuoteOrOrder = "ERROR";
						if (QuoteOrOrder.equalsIgnoreCase("null"))
							QuoteOrOrder = "ERROR";
						if (QuoteOrOrder.equalsIgnoreCase(""))
							QuoteOrOrder = "ERROR";
						if (QuoteOrOrder.equalsIgnoreCase("ERROR")) {
							stmt.executeUpdate("Delete from ReqQ where ReqID='" + ReqID + "'");
							stmt.executeUpdate(
									"Insert INTO ReqQ(ReqID,TCID,OrderAction,Stepkey,CustomerID,OrderDate,ReqFN,ENV) VALUES('"
											+ ReqID + "','" + TCID + "','" + Action + "','" + OrderStatus + "','"
											+ CustomerID + "','" + OrderDate + "','" + FileName + "','" + ENV + "')");
							SetExecutingMachine(null, "StpQ");
						}

						else {
							stmt.executeUpdate("Update ReqQ SET CUSTOMERID='" + CustomerID + "' ,OrderID='"
									+ QuoteOrOrder + "' ,ORDERDATE='" + OrderDate + "' where TCID='" + TCID
									+ "' AND ReqID='" + ReqID + "'");
							SetExecutingMachine(null, "StpQ");
							SendOrderForNPS(QuoteOrOrder);
							System.out.println(TCID + "=" + Action + ";" + CustomerID + ";" + QuoteOrOrder);
							if (BiOrder && !QuoteOrOrder.equalsIgnoreCase("ERROR")) {
								stmt.executeUpdate(
										"INSERT INTO InterimQ(TCID,CustomerID,ReqID,BillingProfile,OrderID,ReqFN,NextAction,OrderDate,ENTRYTIME,ENV,Flow) VALUES('"
												+ TCID + "','" + CustomerID + "','" + ReqID + "','" + BillProfile
												+ "','" + QuoteOrOrder + "','" + FileName + "','" + Action + "','"
												+ OrderDate + "','" + LocalDateTime.now() + "','" + ENV + "','" + Flow
												+ "')");
							}
							if (!QuoteOrOrder.equalsIgnoreCase("ERROR") && !BiOrder) {
								System.out.println(ReqID);
								updateSTPQue(TCID, ReqID, Action, BillProfile, CustomerID, QuoteOrOrder, FileName,
										OrderDate, ENV, Flow);
							}
						}
					}

				}
				ReqQue = stmt.executeQuery("Select * from ReqQ where OrderID is NULL and ExecutingMachine IS null");
			}

		} catch (Exception e) {
			System.out.println(e);
		}

	}

	public boolean changeInorderDate(String OrderDate) throws ParseException {
		boolean flag = false;
		DateFormat format = new SimpleDateFormat("ddMMyyyy");
		Date orderdate = format.parse(OrderDate);
		Date PrsDate = Calendar.getInstance().getTime();
		int diffInDays = (int) ((PrsDate.getTime() - orderdate.getTime()) / (1000 * 60 * 60 * 24));
		if (diffInDays >= 1) {
			flag = true;
		}
		return flag;
	}

	public static void updateSTPQue(String TCID, String REFID, String Action, String FinalStatus, String CusID,
			String OrderID, String TCFN, String OrderDate, String ENV, String Flow)
			throws FilloException, SQLException {// ServerAddr="C:\\Data\\Auto\\AutoPick";
		System.out.println("Order sending to STPQ");
		stmt.executeUpdate(
				"INSERT INTO StpQ(TCID,ReqID,ACTION,BillingProfile,CustomerID,OrderID,ReqFN,ORDERDATE,EntryTime,ENV,Flow) VALUES('"
						+ TCID + "','" + REFID + "','" + Action + "','" + BillProfile + "','" + CusID + "','" + OrderID
						+ "','" + TCFN + "','" + OrderDate + "','" + LocalDateTime.now() + "','" + ENV + "','" + Flow
						+ "')");
	}

	public static String GetStringonexecuteSQLonOracle(String strSqlQuery, String strConnection, String strDBUser,
			String strDBPass) {
		String strRet = "";
		try {
			System.out.println(strSqlQuery);
			Class.forName("oracle.jdbc.OracleDriver");
			System.out.println(strConnection + strDBUser + strDBPass);
			java.sql.Connection conn = DriverManager.getConnection(strConnection, strDBUser, strDBPass);
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(strSqlQuery);
			while (rset.next()) {
				strRet = rset.getString(1);
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return strRet;
	}

	public static void SendOrderForNPS(String Order) throws IOException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conNPS = (Connection) DriverManager.getConnection("jdbc:mysql://bgc-be-qtp197:3306/execution",
					"root", "");
			Statement stmtNPS = (Statement) conNPS.createStatement();
			stmtNPS.executeUpdate(
					"Insert INTO npsordertrack(ENV,ORDERID,ENTRYDATE) VALUES('" + ENV + "','" + Order + "',now())");
			System.out.println("Order Entried in NPS DB:" + Order);
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("Order not Entried in NPS DB:" + Order);
		}
	}

	public static void setTimestampsforV1() throws FilloException {
		ResultSet STPQueV1 = null, QueV1 = null;

		try {
			STPQueV1 = stmt.executeQuery("Select * from StpQ where  NOT OrderID='ERROR' and FLOW='V1'");
			while (STPQueV1.next()) {
				String Ord = STPQueV1.getString("OrderID");
				String En = STPQueV1.getString("ENV");
				String duedt = getduedate(Ord, En).replaceAll(" ", "T");
				if (!duedt.equalsIgnoreCase(""))
					stmt.executeUpdate("Update StpQ set EntryTime='" + duedt + "' where OrderID='" + Ord + "' and ENV='"
							+ En + "'");
				System.out.println("updating STPQ V1 dates");
				// STPQueV1 = stmt.executeQuery("Select * from StpQ where NOT
				// OrderID='ERROR' and FLOW='V1'");
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			QueV1 = stmt.executeQuery("Select * from InterimQ where NOT OrderID='ERROR' and FLOW='V1'");
			while (QueV1.next()) {
				String Ord = QueV1.getString("OrderID");
				String En = QueV1.getString("ENV");
				String duedt = getduedate(Ord, En).replaceAll(" ", "T");
				if (!duedt.equalsIgnoreCase(""))
					stmt.executeUpdate("Update InterimQ set ENTRYTIME='" + duedt + "' where OrderID='" + Ord
							+ "' and ENV='" + En + "'");
				System.out.println("updating Queue V1 dates");
				// QueV1 = stmt.executeQuery("Select * from InterimQ where NOT
				// OrderID='ERROR' and FLOW='V1'");
			}
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	public static String getduedate(String orderid, String Envir) throws IOException {
		String duedate = "";

		duedate = executeSQLonOracle(
				"select due_date from tborder_action ta where ta.order_id IN (select order_unit_id as order_id FROM TBORDER_ADDITION WHERE EXT_REF_NUM='SHE"
						+ orderid + "')",
				"OMS", Envir);

		return duedate;

	}

	public static String executeSQLonOracle(String selectStat, String strDatabase, String strEnvironment)
			throws IOException {
		Properties prop = new Properties();
		InputStream input = null;
		input = new FileInputStream(strConfigFilePath);
		prop.load(input);
		String FirstColVal = "";
		strNPSConnString = prop.getProperty(strEnvironment + ".DB.NPS");
		strOMSConnString = prop.getProperty(strEnvironment + ".DB.OMS");
		strNPSDB = prop.getProperty(strEnvironment + ".DB.NPS.DB");
		strOMSDB = prop.getProperty(strEnvironment + ".DB.OMS.DB");
		strNPSUserID = prop.getProperty(strEnvironment + ".DB.NPS.USER");
		strOMSUserID = prop.getProperty(strEnvironment + ".DB.OMS.USER");
		strNPSPassword = prop.getProperty(strEnvironment + ".DB.NPS.PASS");
		strOMSPassword = prop.getProperty(strEnvironment + ".DB.OMS.PASS");
		String strDBUser = "";
		String strDBPass = "";
		try {
			String strConnection = "";
			System.out.println(selectStat);
			if (strDatabase == "OMS") {
				strConnection = "jdbc:oracle:thin:@//" + strOMSDB + ":1540/" + strOMSDB;
				strDBUser = strOMSUserID;
				strDBPass = strOMSPassword;
			} else {
				strConnection = "jdbc:oracle:thin:@//" + strNPSDB + ":1540/" + strNPSDB;
				strDBUser = strNPSUserID;
				strDBPass = strNPSPassword;
			}
			Class.forName("oracle.jdbc.OracleDriver");
			System.out.println(strConnection + strDBUser + strDBPass);
			Connection conn = DriverManager.getConnection(strConnection, strDBUser, strDBPass);
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(selectStat);

			if (rset.next()) {
				FirstColVal = rset.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return FirstColVal;
	}

}
