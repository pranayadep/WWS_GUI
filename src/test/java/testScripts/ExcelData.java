package testScripts;

public interface ExcelData {
	int stepDescription = 2;
	int stepName = 3;
	int steptoBeExecuted = 4;
	int stepType = 6;
	int reportNeeded = 5;
	int testStepInputStart = 7;
	int testcaseStartRow = 4;
	int testcaseStepCount = 5;
	int testCaseData = 6;
	int testcasebrowser = 7;
	int testCaseInputStart = 8;
	String JDBC_Conn_String_WWS = "jdbc:oracle:thin:wws_ro/wws_ro@IP-TORA131:1540/WWS02T";
	
	String JDBC_Conn_String_WWS1 = "jdbc:oracle:thin:wws_ro/wws_ro@IP-TORA131:1540/WWS01T";
	String JDBC_Conn_String_WWSD1 = "jdbc:oracle:thin:wws_schema/wws_schema_dev1@IP-DORA144:1540/WWS01D";
	String JDBC_Conn_String_WSL = "jdbc:oracle:thin:wsl_ro/wsl_ro@IP-TORA131:1540/WSL02T";
	String JDBC_Conn_String_WSL_ST2 = "jdbc:oracle:thin:wsl_schema/wsl_schema_DEVTST3@IP-TORA131:1540/WSL03T";
	String JDBC_Conn_String_WSL_ST1 = "jdbc:oracle:thin:wsl_schema/wsl_schema_devtst1@IP-TORA131:1540/WSL01T";
	String JDBC_Conn_String_WSL_DEV = "jdbc:oracle:thin:wsl_schema/wsl_schema_devtst2@IP-TORA131:1540/WSL02T";
	String JDBC_Conn_String_WSL_DEV2 = "jdbc:oracle:thin:wsl_schema/wsl_schema_dev2@IP-DORA144:1540/WSL02D";
	String JDBC_Conn_String_MCOM_DEV2 = "jdbc:oracle:thin:mcom_schema/mcom_schema@IP-DORA144:1540/MCOM02D";
	String JDBC_Conn_String_MCOM_DEVTEST2 = "jdbc:oracle:thin:mcom_ro/mcom_ro@IP-TORA131:1540/MCOM02T";
}
