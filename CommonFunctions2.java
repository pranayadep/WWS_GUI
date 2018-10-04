package reusables;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import testScripts.ExcelData;
import testScripts.ExcelRead;

public class CommonFunctions2 implements ExcelData {

	private static Properties objectMapProps;
	public static ThreadLocal<WebDriver> driverobj = new ThreadLocal<WebDriver>();
	private static String baseUrl;
	public static WebElement eleSearched;
	public static Robot robot;

	public static void sleep(String millSecs) {
		long millSec = Integer.parseInt(millSecs) * 1000;
		try {
			Thread.sleep(millSec);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.getMessage();
		}
	}

	public static WebDriver SetBrowser(String strBrowser) throws IOException, InterruptedException {

		try {

			if (strBrowser.equalsIgnoreCase("IE") || strBrowser.equalsIgnoreCase("Internet Explorer")) {
				driverobj.set(setIEDriver(System.getProperty("user.dir") + "\\drivers\\IEDriverServer64.exe"));
			} else if (strBrowser.equalsIgnoreCase("Chrome") || strBrowser.equalsIgnoreCase("GoogleChrome")
					|| strBrowser.equalsIgnoreCase("Google Chrome")) {
				Thread.sleep(500);
				driverobj.set(setChromeDriver(
						"C:\\Program Files (x86)\\Selenium\\WebDrivers\\ChromeDrivers\\2.37\\chromedriver.exe"));
			}
			ExcelRead.testCaseStatus = "pass";
		} catch (Exception e) {
			e.printStackTrace();
			ExcelRead.testCaseError = e.getMessage();
			CommonFunctions2.rfWriteToExcel(ExcelRead.currTestCaseId, e.getMessage());
			ExcelRead.testCaseStatus = "fail";
		}
		return driverobj.get();
	}

	public static WebDriver SetBrowser2(String strBrowser) throws IOException, InterruptedException {

		try {

			if (strBrowser.equalsIgnoreCase("IE") || strBrowser.equalsIgnoreCase("Internet Explorer")) {
				driverobj.set(setIEDriver(System.getProperty("user.dir") + "\\drivers\\IEDriverServer64.exe"));
			} else if (strBrowser.equalsIgnoreCase("Chrome") || strBrowser.equalsIgnoreCase("GoogleChrome")
					|| strBrowser.equalsIgnoreCase("Google Chrome")) {
				Thread.sleep(500);
				driverobj.set(setChromeDriver("C:\\Users\\id848699\\Desktop\\Driver\\IBM\\chromedriver2.exe"));
			}
			ExcelRead.testCaseStatus = "pass";
		} catch (Exception e) {
			e.printStackTrace();
			ExcelRead.testCaseError = e.getMessage();
			CommonFunctions2.rfWriteToExcel(ExcelRead.currTestCaseId, e.getMessage());
			ExcelRead.testCaseStatus = "fail";
		}
		return driverobj.get();
	}

	public static void sendRes(String filepath, String coId, String queueName) {

		try {
			Producer.jmsCall(filepath, coId, queueName);
			ExcelRead.testCaseStatus = "pass";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.getMessage();
			ExcelRead.testCaseError = e.getMessage();
			ExcelRead.testCaseStatus = "fail";
		}
	}

	public static void StoreXmlValue(String filepath, String xpath, String variableName) {

		try {
			String arr[] = XMlGetValue.main(filepath, xpath);
			String varibaleName[] = variableName.split(",");
			System.out.println("array length" + arr.length);
			if (arr.length < 0) {
				ExcelRead.testCaseError = "No Data in the XML";
				ExcelRead.testCaseStatus = "fail";
			} else {
				for (int i = 0; i < arr.length; i++) {
					ExcelRead.runTimeVar.put(varibaleName[i], arr[i]);
					System.out.println("Runtime variable value is" + ExcelRead.runTimeVar.get(varibaleName[i]));
				}
			}
			ExcelRead.testCaseStatus = "pass";
		} catch (Exception e) {
			e.printStackTrace();
			ExcelRead.testCaseError = e.getMessage();
			CommonFunctions2.rfWriteToExcel(ExcelRead.currTestCaseId, e.getMessage());
			ExcelRead.testCaseStatus = "fail";
		}
	}

	public static void validateTagValues(String filepath, String xpath, String valueToBeCompared) {

		try {
			String arr[] = XMlGetValue.main(filepath, xpath);
			if (arr.equals(null)) {
				System.out.println("no values returned in xpath");
			} else {
				String expectedValues[] = valueToBeCompared.split(",");
				System.out.println("array length" + arr.length);
				if (arr.length < 0) {
					ExcelRead.testCaseError = "No Data in the XML";
					ExcelRead.testCaseStatus = "fail";
				} else {
					for (int i = 0; i < arr.length; i++) {
						String CompareValue = "";
						if (expectedValues[i].contains("date")) {
							String dateField[] = expectedValues[i].split("-");
							String dateFieldValues[] = dateField[1].split("/");
							CompareValue = dateFieldValues[2] + "-" + dateFieldValues[0] + "-" + dateFieldValues[1];
						} else if (expectedValues[i].contains("TEMP")) {
							CompareValue = ExcelRead.runTimeVar.get(expectedValues[i]);
						} else {
							CompareValue = expectedValues[i];
						}

						if (arr[i].contains(CompareValue)) {
							System.out.println("Value matching with the expected value : " + CompareValue);
							ExcelRead.testCaseStatus = "pass";
						} else {
							System.out.println("Value not matching with the expected value : " + CompareValue);
							ExcelRead.testCaseStatus = "fail";
							ExcelRead.testCaseError = "";
						}
					}
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ExcelRead.testCaseError = e.getMessage() + "No values returned from xpath";
			CommonFunctions2.rfWriteToExcel(ExcelRead.currTestCaseId, e.getMessage());
			ExcelRead.testCaseStatus = "fail";
		}
	}

	public static void sendReq(String queName, String xmlString, String coId) {

		try {
			if (xmlString.equalsIgnoreCase("req")) {
				xmlString = ExcelRead.receivedXML;
			} else if (xmlString.contains("TEMP")) {
				xmlString = ExcelRead.runTimeVar.get(xmlString);
			} else if (xmlString.contains(".xml")) {
				File loadFile = new File(xmlString);
				System.out.println("in conversion");
				StringBuffer fileContents = new StringBuffer();
				@SuppressWarnings("resource")
				BufferedReader input = new BufferedReader(new FileReader(loadFile));
				String line = null;
				while ((line = input.readLine()) != null) {
					Matcher junkMatcher = (Pattern.compile("^([\\W]+)<")).matcher(line.trim());
					line = junkMatcher.replaceFirst("<");
					fileContents.append(line);
				}
				xmlString = fileContents.toString();
			}
			System.out.println(queName + " : " + coId);
			if (xmlString.contains(">Order Enriched<") || (xmlString.contains(">Order Decomposed<"))) {
				System.out.println("BOTS notification ... IGNORING !!!!");
				ExcelRead.testCaseStatus = "pass";
			} else {
				SendWSL.jmsCall(xmlString, queName, coId);
				ExcelRead.testCaseStatus = "pass";
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ExcelRead.testCaseError = e.getMessage();
			CommonFunctions2.rfWriteToExcel(ExcelRead.currTestCaseId, e.getMessage());
			ExcelRead.testCaseStatus = "fail";
		}
	}

	public static void waitForAction() {
		try {
			System.out.println("Before waitForACtion");
			JOptionPane.showMessageDialog(null, "To Continue Press OK.");
			System.out.println("After waitForACtion");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void replaceToVariable(String xmlValue, String keys, String values, String variableName)
			throws IOException {
		if (xmlValue.contains("TEMP")) {
			xmlValue = ExcelRead.runTimeVar.get(xmlValue);
		} else {
			System.out.println("in conversion");
			File loadFile = new File(xmlValue);
			StringBuffer fileContents = new StringBuffer();
			@SuppressWarnings("resource")
			BufferedReader input = new BufferedReader(new FileReader(loadFile));
			String line = null;
			while ((line = input.readLine()) != null) {
				Matcher junkMatcher = (Pattern.compile("^([\\W]+)<")).matcher(line.trim());
				line = junkMatcher.replaceFirst("<");
				fileContents.append(line);
			}
			xmlValue = fileContents.toString();
		}
		System.out.println("Before conversion : " + xmlValue);
		String keys1[] = keys.split(",");
		String values1[] = values.split(",");
		for (int i = 0; i < keys1.length; i++) {
			if (values1[i].contains("TEMP")) {
				xmlValue = xmlValue.replaceAll(keys1[i], ExcelRead.runTimeVar.get(values1[i]));
				System.out.println(xmlValue);
			} else {
				xmlValue = xmlValue.replaceAll(keys1[i], values1[i]);
				System.out.println(xmlValue);
			}
		}
		ExcelRead.runTimeVar.put(variableName, xmlValue);
	}

	public static void getRespFlag(String connection, String coId) {

		try {
			Thread.sleep(500);
			if (connection.equalsIgnoreCase("wws")) {
				connection = JDBC_Conn_String_WWS;
			} else if (connection.equalsIgnoreCase("wws1")) {
				connection = JDBC_Conn_String_WWS1;
			}
			JDBC_Test.getRespFlag(connection, coId);

			ExcelRead.testCaseStatus = "pass";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.getMessage();
			ExcelRead.testCaseError = e.getMessage();
			ExcelRead.testCaseStatus = "fail";
		}
	}

	public static void printValue(String Message, String value) {

		try {
			System.out.println(Message + " : " + value);
			ExcelRead.testCaseStatus = "pass";
			ExcelRead.testCasePrint = Message + " : " + value;
			CommonFunctions2.rfWriteToExcel(ExcelRead.currTestCaseId, value);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.getMessage();
			ExcelRead.testCaseError = e.getMessage();
			CommonFunctions2.rfWriteToExcel(ExcelRead.currTestCaseId, e.getMessage());
			ExcelRead.testCaseStatus = "fail";
		}
	}

	public static void getReq(String queName, String coId, String reqXMLkey) {

		try {
			for (int i = 0; i < 2; i++) {
				System.out.println("Correlation id is - " + coId);
				String reqXML = MessageReceiver.getReq(queName, coId);
				System.out.println("Request XML is " + reqXML);
				if (reqXML.equalsIgnoreCase("")) {
					ExcelRead.testCaseError = "No request XML present on the queue";
					ExcelRead.testCaseStatus = "fail";
				} else {
					if (reqXMLkey.contains(".xml")) {
						Path pathXMLFile = Paths.get(reqXMLkey);
						Files.write(pathXMLFile, reqXML.getBytes(), StandardOpenOption.TRUNCATE_EXISTING,
								StandardOpenOption.WRITE, StandardOpenOption.CREATE);
					} else {
						ExcelRead.receivedXML = reqXML;
						ExcelRead.runTimeVar.put(reqXMLkey, reqXML);
					}
					ExcelRead.testCaseStatus = "pass";
					break;
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			ExcelRead.testCaseError = e.getMessage();
			CommonFunctions2.rfWriteToExcel(ExcelRead.currTestCaseId, e.getMessage());
			ExcelRead.testCaseStatus = "fail";
		}
	}

	public static void getReqMsg(String queName, String MsgID, String reqXMLkey) {

		try {
			for (int i = 0; i < 2; i++) {
				System.out.println("Correlation id is - " + MsgID);
				String reqXML = MessageReceiverMsgID.getReq(queName, MsgID);
				System.out.println("Request XML is " + reqXML);
				if (reqXML.equalsIgnoreCase("")) {
					ExcelRead.testCaseError = "No request XML present on the queue";
					ExcelRead.testCaseStatus = "fail";
				} else {
					if (reqXMLkey.contains(".xml")) {
						Path pathXMLFile = Paths.get(reqXMLkey);
						Files.write(pathXMLFile, reqXML.getBytes(), StandardOpenOption.TRUNCATE_EXISTING,
								StandardOpenOption.WRITE, StandardOpenOption.CREATE);
					} else {
						ExcelRead.receivedXML = reqXML;
						ExcelRead.runTimeVar.put(reqXMLkey, reqXML);
					}
					ExcelRead.testCaseStatus = "pass";
					break;
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			ExcelRead.testCaseError = e.getMessage();
			ExcelRead.testCaseStatus = "fail";
		}
	}

	public static void getDatafromDB(String connString, String query1, String varName) {
		try {
			String DBValue = "";
			String query = query1.replaceAll("//", "'");
			System.out.println("query is" + query);
			if (connString.equalsIgnoreCase("wws")) {
				DBValue = JDBC_Test.getData(JDBC_Conn_String_WWS1, query);
			} else if (connString.equalsIgnoreCase("wsl_st1")) {
				DBValue = JDBC_Test.getData(JDBC_Conn_String_WSL_ST1, query);
			} else if (connString.equalsIgnoreCase("wsl_dev")) {
				DBValue = JDBC_Test.getData(JDBC_Conn_String_WSL_DEV, query);
			} else if (connString.equalsIgnoreCase("wws1")) {
				DBValue = JDBC_Test.getData(JDBC_Conn_String_WWS1, query);
			} else if (connString.equalsIgnoreCase("wsl_dev2")) {
				System.out.println("Connection string is :" + JDBC_Conn_String_WSL_DEV2);
				DBValue = JDBC_Test.getData(JDBC_Conn_String_WSL_DEV2, query);
			} else if (connString.equalsIgnoreCase("wwsd1")) {
				System.out.println("Connection string is :" + JDBC_Conn_String_WWSD1);
				DBValue = JDBC_Test.getData(JDBC_Conn_String_WWSD1, query);
			} else if (connString.equalsIgnoreCase("mcom_dev2")) {
				System.out.println("Connection string is :" + JDBC_Conn_String_MCOM_DEVTEST2);
				DBValue = JDBC_Test.getData(JDBC_Conn_String_MCOM_DEVTEST2, query);
			} else {
				System.out.println("In else");
			}

			if (DBValue.equals("")) {
				ExcelRead.testCaseStatus = "fail";
			} else {
				ExcelRead.runTimeVar.put(varName, DBValue);
				System.out.println("RunTime Variable Value is " + ExcelRead.runTimeVar.get(varName));
				// ExcelRead.corrid = corrid;
				ExcelRead.testCaseStatus = "pass";
			}
		} catch (Exception e) {
			e.getMessage();
			ExcelRead.testCaseError = e.getMessage();
			CommonFunctions2.rfWriteToExcel(ExcelRead.currTestCaseId, e.getMessage());
			ExcelRead.testCaseStatus = "fail";
		}
	}

	public static void selectAutoComplete(String elename, String value, WebDriver driver1) {
		try {
			String rfElemenntSearch = null;
			rfElemenntSearch = elementSearch(driver1, elename);
			if (rfElemenntSearch.equalsIgnoreCase("Pass")) {
				ExcelRead.testCaseStatus = "pass";
				eleSearched.clear();
				System.out.println("Sending this :  " + value + " to " + elename);
				eleSearched.sendKeys(value);
				System.out.println("Sent this : " + value + " to " + elename);
				Thread.sleep(500);
				eleSearched.sendKeys(Keys.DOWN);
				Thread.sleep(500);
				eleSearched.sendKeys(Keys.TAB);
				System.out.println("Selected the value from auto complete checkbox");
			} else {
				ExcelRead.testCaseStatus = "fail";
				// need to write
				System.out.println("Element not found " + rfElemenntSearch);
			}
		} catch (Exception ex) {
			// need to write
			ExcelRead.testCaseError = ex.getMessage();
			CommonFunctions2.rfWriteToExcel(ExcelRead.currTestCaseId, ex.getMessage());
			ExcelRead.testCaseStatus = "fail";
		}
	}

	/**
	 * code to execute any command from the command line
	 * 
	 * @author Pranay Kumar
	 * @param Command
	 *            - Command to execute from the command line
	 * @return
	 */
	public void executeCommand(String Command) throws Exception {
		try {
			Runtime.getRuntime().exec(Command);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new Exception();
		}
	}

	public static void goUP() {
		try {
			robot.keyPress(KeyEvent.VK_PAGE_UP);
		} catch (Exception e) {
			// need to handle
			e.getMessage();
			ExcelRead.testCaseStatus = "fail";
		}
	}

	public static void goDOWN() {
		try {
			robot.keyPress(KeyEvent.VK_PAGE_DOWN);
		} catch (Exception e) {
			// need to handle
			e.getMessage();
			ExcelRead.testCaseStatus = "fail";
		}
	}

	public static void logout(WebDriver driver1) {
		try {
			driver1.switchTo().defaultContent();
			driver1.findElement(
					By.xpath(".//*[@id='RULE_KEY']/div/div/div/div[1]/div/div/div/div/div[1]/div/div/span/a")).click();
			Thread.sleep(5000);
			driver1.findElement(By.xpath("(.//div[contains(@class,'menu-panel-wrapper')]//span/span[.='Log off'])"))
					.click();
			ExcelRead.testCaseStatus = "pass";
		} catch (Exception ex) {
			// need to write
			ExcelRead.testCaseError = ex.getMessage();
			CommonFunctions2.rfWriteToExcel(ExcelRead.currTestCaseId, ex.getMessage());
			ExcelRead.testCaseStatus = "fail";
		}
	}

	/**
	 * code to compare 2 Strings
	 * 
	 * @author Pranay Adep
	 * @param expected
	 *            - Expected String
	 * @param actual
	 *            - Actual String
	 * @return
	 */
	public boolean StringCompare(String expected, String actual) throws Exception {
		if (expected.equalsIgnoreCase(actual)) {
			System.out.println("Compare 2 strings" + "Compare 2 strings" + expected + " " + expected + " PASSED");
			return true;
		} else {
			System.out.println("Compare 2 strings" + "Compare 2 strings" + expected + " " + actual + " FAILED");
			return false;
		}
	}

	public static void launchUrl(WebDriver driver, String url) {
		try {
			driver.get(url);
			ExcelRead.testCaseStatus = "pass";
		} catch (Exception e) {
			e.getMessage();
			ExcelRead.testCaseError = e.getMessage();
			ExcelRead.testCaseStatus = "fail";
		}
	}

	public static InternetExplorerDriver setIEDriver(String strIEDriverPath) throws IOException {
		try {
			System.setProperty("webdriver.ie.driver", strIEDriverPath);
			DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
			caps.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);

			return new InternetExplorerDriver(caps);
		} catch (Exception e) {
			return null;
		}
	}

	public static ChromeDriver setChromeDriver(String strChromeDriverPath) throws IOException {
		DesiredCapabilities dcChrome = null;
		try {
			System.setProperty("webdriver.chrome.driver", strChromeDriverPath);
			ChromeOptions options = new ChromeOptions();
			options.addArguments("start-maximized");
			options.addArguments("--incognito");
			options.setExperimentalOption("useAutomationExtension", false);
			dcChrome = DesiredCapabilities.chrome();
			dcChrome.setCapability(ChromeOptions.CAPABILITY, options);
			ExcelRead.testCaseStatus = "pass";
		} catch (Exception e) {
			CommonFunctions2.rfWriteToExcel(ExcelRead.currTestCaseId, e.getMessage());
			ExcelRead.testCaseStatus = "fail";
		}
		return new ChromeDriver(dcChrome);
	}

	public static void setTimeOuts(int pageLoadTimeOutInSec, int implicitWaitInSec, WebDriver driver1) {
		driver1.manage().timeouts().implicitlyWait(implicitWaitInSec, TimeUnit.SECONDS);
		driver1.manage().timeouts().pageLoadTimeout(pageLoadTimeOutInSec, TimeUnit.SECONDS);
	}

	public static WebDriver switchToDefaultContent(WebDriver driver1) {
		try {
			driver1.switchTo().defaultContent();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return driver1;
	}

	public static void quit(WebDriver driver1) {
		try {
			driver1.quit();
		} catch (Exception e) {
			// need to write
			e.printStackTrace();
			CommonFunctions2.rfWriteToExcel(ExcelRead.currTestCaseId, e.getMessage());
			ExcelRead.testCaseStatus = "fail";
		}
	}

	public static WebDriver switchtoFrame(String frame1, WebDriver driver1) {
		int frame = Integer.parseInt(frame1);
		try {
			WebDriverWait wait = new WebDriverWait(driver1, 10);
			wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frame));
			System.out.println("Frame switched to " + frame);
		} catch (Exception e) {
			// need to write
			e.printStackTrace();
			ExcelRead.testCaseError = "Frame error : " + e.getMessage();
			CommonFunctions2.rfWriteToExcel(ExcelRead.currTestCaseId, e.getMessage());
			ExcelRead.testCaseStatus = "fail";
		}
		return driver1;
	}

	public static void refresh(WebDriver driver1) {
		try {
			driver1.navigate().refresh();
		} catch (Exception e) {
			// need to write
			e.printStackTrace();
			CommonFunctions2.rfWriteToExcel(ExcelRead.currTestCaseId, e.getMessage());
			ExcelRead.testCaseStatus = "fail";
		}
	}

	public static void storeValue(String soureVal, String targetVal) {
		try {
			ExcelRead.runTimeVar.put(targetVal, soureVal);
			System.out.println("Stored value is  : " + ExcelRead.runTimeVar.get(targetVal));
		} catch (Exception e) {
			// need to write
			e.printStackTrace();
			CommonFunctions2.rfWriteToExcel(ExcelRead.currTestCaseId, e.getMessage());
			ExcelRead.testCaseStatus = "fail";
		}
	}

	public static void waitForElementDisplayed(String elename, WebDriver driver1) {
		try {
			WebDriverWait wait = new WebDriverWait(driver1, 10);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(elename)));
		} catch (Exception e) {
			// need to write
			e.printStackTrace();
			CommonFunctions2.rfWriteToExcel(ExcelRead.currTestCaseId, e.getMessage());
			ExcelRead.testCaseStatus = "fail";
		}
	}

	public static void setPropFile(String configpath) {
		objectMapProps = new Properties();

		InputStream fis;
		try {
			fis = new FileInputStream(configpath);
			objectMapProps.load(fis);
		} catch (IOException e) {
			e.getMessage();
			CommonFunctions2.rfWriteToExcel(ExcelRead.currTestCaseId, e.getMessage());
			ExcelRead.testCaseStatus = "fail";
		}
	}

	public static String getObjectValue(String objectName) {
		String propValue = objectMapProps.getProperty(objectName);
		System.out.println("Property value of element" + objectName + " is " + propValue);
		return propValue;
	}

	public static String[] getObjectValue2(String objectName) {
		String arr[] = null;
		try {
			arr = objectMapProps.getProperty(objectName).split(":");
			System.out.println("Element name is : " + arr[0] + " Element type is : " + arr[1]);
		} catch (Exception e) {
			// need to write
			e.printStackTrace();
			CommonFunctions2.rfWriteToExcel(ExcelRead.currTestCaseId, "Element not present in the property file");
			ExcelRead.testCaseStatus = "fail";
			ExcelRead.testCaseError = "Element not present in the property file";
		}
		return arr;
	}

	public static void captureScreenshot(String screenshotFileName, WebDriver driver1) {

		try {
			File scrFile = ((TakesScreenshot) driver1).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(System.getProperty("user.dir") + "\\WWS_GUI\\errorScreens\\"
					+ LocalDate.now() + "\\" + ExcelRead.currTestCaseName + "\\" + screenshotFileName + ".jpg"));
			ExcelRead.screenshotPath = System.getProperty("user.dir") + "\\WWS_GUI\\errorScreens\\" + LocalDate.now()
					+ "\\" + ExcelRead.currTestCaseName + "\\" + screenshotFileName + ".jpg";
		} catch (IOException e1) {
			e1.printStackTrace();
			CommonFunctions2.rfWriteToExcel(ExcelRead.currTestCaseId, e1.getMessage());
			ExcelRead.testCaseStatus = "fail";
		} catch (NullPointerException npe) {
			ExcelRead.screenshotPath = System.getProperty("user.dir") + "\\WWS_GUI\\errorScreens\\" + LocalDate.now()
					+ "\\" + ExcelRead.currTestCaseName + "\\" + screenshotFileName + ".jpg";
			npe.printStackTrace();
			CommonFunctions2.rfWriteToExcel(ExcelRead.currTestCaseId, npe.getMessage());
			ExcelRead.testCaseStatus = "fail";
		}

	}

	public static void goToBaseUrl(WebDriver driver1) {
		driver1.get(baseUrl);
	}

	public static String elementSearch(WebDriver driver, String locator) throws InterruptedException {
		try {
			String element[] = null;
			element = getObjectValue2(locator);
			// System.out.println(element);
			WebDriverWait wait = new WebDriverWait(driver, 20);
			Thread.sleep(500);
			if (element[1].trim().equalsIgnoreCase("id")) {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(element[0])));
				eleSearched = driver.findElement(By.id(element[0]));
				return "Pass";
			} else if (element[1].trim().equalsIgnoreCase("xpath")) {
				System.out.println("in xpath");
				// wait.until(ExpectedConditions.elementToBeClickable(locator));
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(element[0])));
				eleSearched = driver.findElement(By.xpath(element[0]));
				return "Pass";
			} else if (element[1].trim().equalsIgnoreCase("name")) {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(element[0])));
				eleSearched = driver.findElement(By.name(element[0]));
				return "Pass";
			} else if (element[1].trim().equalsIgnoreCase("linkText")) {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(element[0])));
				eleSearched = driver.findElement(By.linkText(element[0]));
				return "Pass";
			} else if (element[1].trim().equalsIgnoreCase("cssSelector")) {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(element[0])));
				eleSearched = driver.findElement(By.cssSelector(element[0]));
				return "Pass";
			} else if (element[1].trim().equalsIgnoreCase("partialLinkText")) {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText(element[0])));
				eleSearched = driver.findElement(By.partialLinkText(element[0]));
				return "Pass";
			} else if (element[1].trim().equalsIgnoreCase("tagName")) {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText(element[0])));
				eleSearched = driver.findElement(By.partialLinkText(element[0]));
				return "Pass";
			} else {
				return "Fail@" + "please select valid locator type";
			}
		} catch (NoClassDefFoundError ex) {
			captureScreenshot(locator, driverobj.get());
			ExcelRead.testCaseError = "Not able to find element on screen due to exception >>> " + ex.getMessage()
					+ " Please check element name";
			System.out.println(ExcelRead.testCaseError);
			CommonFunctions2.rfWriteToExcel(ExcelRead.currTestCaseId, ExcelRead.testCaseError);
			ExcelRead.testCaseStatus = "fail";
			return "Fail" + ex.getMessage();
		}

	}

	public static void WWS_Login(WebDriver driver1, String user, String password) throws Exception {
		Thread.sleep(100);
		try {
			System.out.println("In login");
			sendValue("userTxtBox", user, driver1);
			sendValue("pwdTxtBox", password, driver1);
			elementclick("LoginBtnClick", driver1);
		} catch (Exception e) {
			e.getMessage();
			CommonFunctions2.rfWriteToExcel(ExcelRead.currTestCaseId, e.getMessage());
			ExcelRead.testCaseStatus = "fail";
		}
	}

	public static boolean waitUntilElementDisplayed(final WebElement webElement, WebDriver driver) {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		WebDriverWait wait = new WebDriverWait(driver, 10);
		ExpectedCondition<Boolean> elementIsDisplayed = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver arg0) {
				try {
					webElement.isDisplayed();
					return true;
				} catch (NoSuchElementException e) {
					return false;
				} catch (StaleElementReferenceException f) {
					return false;
				}
			}
		};
		return wait.until(elementIsDisplayed);
	}

	public static void elementclick(String locator, WebDriver driver1) {
		try {
			String rfElemenntSearch = null;
			rfElemenntSearch = elementSearch(driver1, locator);
			if (rfElemenntSearch.equalsIgnoreCase("Pass")) {
				ExcelRead.testCaseStatus = "pass";
				System.out.println("Clicking on :  " + locator);
				System.out.println(eleSearched.getText());
				// WebDriverWait wait = new WebDriverWait(driver1, 10);
				// wait.until(ExpectedConditions.elementToBeClickable(eleSearched));
				// ((JavascriptExecutor)
				// driver1).executeScript("arguments[0].click();", eleSearched);
				eleSearched.click();
				Thread.sleep(200);
				System.out.println("Clicked on : " + locator);
			} else {
				// need to write
				ExcelRead.testCaseStatus = "fail";
				System.out.println("Status is : " + rfElemenntSearch);
				System.out.println("Element not found " + locator);
				CommonFunctions2.rfWriteToExcel(ExcelRead.currTestCaseId, ExcelRead.testCaseError);
			}
		} catch (Exception ex) {
			// need to write
			ExcelRead.testCaseError = ex.getMessage();
			CommonFunctions2.rfWriteToExcel(ExcelRead.currTestCaseId, ex.getMessage());
			ExcelRead.testCaseStatus = "fail";
			System.out.println("Element not clickable ");
		}
	}

	/*
	 * Method : multipleClicks Input : Elements names separated by comma It will
	 * click the non of elements passed to the method Editor : PranayKumar
	 * Infosys ltd
	 */
	public static void multipleClicks(String elements, WebDriver driver1) {
		try {
			String rfElemenntSearch = null;

			String[] elementList = new String[20];
			elementList = elements.split(",");
			int size = elementList.length;
			for (int i = 0; i < size; i++) {
				rfElemenntSearch = elementSearch(driver1, elementList[i]);
				if (rfElemenntSearch.equalsIgnoreCase("Pass")) {
					ExcelRead.testCaseStatus = "pass";
					System.out.println("Clicking on :  " + elementList[i]);
					System.out.println(eleSearched.getText());
					WebDriverWait wait = new WebDriverWait(driver1, 10);
					wait.until(ExpectedConditions.elementToBeClickable(eleSearched));
					((JavascriptExecutor) driver1).executeScript("arguments[0].click();", eleSearched);
					Thread.sleep(200);
					System.out.println("Clicked on : " + elementList[i]);
				} else {
					ExcelRead.testCaseStatus = "fail";
					System.out.println("Status is : " + rfElemenntSearch);
					System.out.println("Element not found " + elementList[i]);
				}
			}

		} catch (Exception ex) {
			// need to write
			ExcelRead.testCaseError = ex.getMessage();
			ex.getMessage();
			CommonFunctions2.rfWriteToExcel(ExcelRead.currTestCaseId, ex.getMessage());
			ExcelRead.testCaseStatus = "fail";
			System.out.println("Element not clickable ");
		}
	}

	/*
	 * Method : doubleClick Input : Element name Double clicks on the given
	 * element using Action class Editor : PranayKumar Infosys ltd
	 */
	public static void doubleClick(String locator, WebDriver driver1) {
		try {
			String rfElemenntSearch = null;
			rfElemenntSearch = elementSearch(driver1, locator);

			if (rfElemenntSearch.equalsIgnoreCase("Pass")) {
				ExcelRead.testCaseStatus = "pass";
				System.out.println("Double Clicking on :  " + locator);
				System.out.println(eleSearched.getText());
				WebDriverWait wait = new WebDriverWait(driver1, 10);
				wait.until(ExpectedConditions.elementToBeClickable(eleSearched));
				Actions action = new Actions(driver1);
				action.moveToElement(eleSearched).doubleClick().perform();
				Thread.sleep(200);
				System.out.println("Double Clicked on : " + locator);
			} else {
				// need to write
				ExcelRead.testCaseStatus = "fail";
				System.out.println("Status is : " + rfElemenntSearch);
				System.out.println("Element not found " + locator);
			}
		} catch (Exception ex) {
			// need to write
			ExcelRead.testCaseError = ex.getMessage();
			ex.getMessage();
			CommonFunctions2.rfWriteToExcel(ExcelRead.currTestCaseId, ex.getMessage());
			ExcelRead.testCaseStatus = "fail";
			System.out.println("Element not clickable ");
		}
	}

	/*
	 * Method : highlightElement Inputs : Locator and WebDriver It will locate
	 * the element and set the property of the elements to background yellow and
	 * give border using JavascriptExecutor
	 */
	public static void highlightElement(String locator, WebDriver driver1) {
		try {
			String rfElemenntSearch = null;
			rfElemenntSearch = elementSearch(driver1, locator);

			if (rfElemenntSearch.equalsIgnoreCase("Pass")) {
				ExcelRead.testCaseStatus = "pass";
				JavascriptExecutor js = (JavascriptExecutor) driver1;
				js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');",
						eleSearched);

			} else {
				// need to write
				ExcelRead.testCaseStatus = "fail";
				System.out.println("Status is : " + rfElemenntSearch);
				System.out.println("Element not found " + locator);
			}
		} catch (Exception ex) {
			// need to write
			ExcelRead.testCaseError = ex.getMessage();
			ex.getMessage();
			CommonFunctions2.rfWriteToExcel(ExcelRead.currTestCaseId, ex.getMessage());
			ExcelRead.testCaseStatus = "fail";
			System.out.println("Element not clickable ");
		}
	}

	public static void sendValue(String elename, String value, WebDriver driver1) {
		try {
			String rfElemenntSearch = null;
			rfElemenntSearch = elementSearch(driver1, elename);
			if (rfElemenntSearch.equalsIgnoreCase("Pass")) {
				ExcelRead.testCaseStatus = "pass";
				System.out.println("Sending this :  " + value + " to " + elename);
				eleSearched.clear();
				Thread.sleep(500);
				eleSearched.sendKeys(value);
				eleSearched.sendKeys(Keys.TAB);
				System.out.println("Sent this : " + value + " to " + elename);
			} else {
				ExcelRead.testCaseStatus = "fail";
				// need to write
				System.out.println("Element not found " + rfElemenntSearch);
			}
		} catch (Exception ex) {
			// need to write
			ExcelRead.testCaseError = ex.getMessage();
			CommonFunctions2.rfWriteToExcel(ExcelRead.currTestCaseId, ex.getMessage());
			ExcelRead.testCaseStatus = "fail";
			System.out.println(ex.getMessage());
		}
	}

	public static void clear(String elename, WebDriver driver1) {
		try {
			String rfElemenntSearch = null;
			rfElemenntSearch = elementSearch(driver1, elename);
			if (rfElemenntSearch.equalsIgnoreCase("Pass")) {
				System.out.println("Clearing the field " + elename);
				eleSearched.clear();
				System.out.println("Cleared the field " + elename);
			} else {
				ExcelRead.testCaseStatus = "fail";
				// need to write
				System.out.println("Element not found " + rfElemenntSearch);
				// need to write
			}
		} catch (Exception ex) {
			ExcelRead.testCaseError = ex.getMessage();
			ex.getMessage();
			// need to write
		}
	}

	public static void selectDate(String elename, String dateValue, WebDriver driver1) {

		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyddMM");
			Date date = formatter.parse(dateValue);
			// System.out.println(date.toString());

			// Get all the data of the date
			String strYear = (new SimpleDateFormat("yyyy")).format(date);
			System.out.println(strYear);
			String strMonth = (new SimpleDateFormat("MMM")).format(date);
			System.out.println(strMonth);
			String strDate = (new SimpleDateFormat("d")).format(date);
			System.out.println(strDate);

			String rfElemenntSearch = null;
			rfElemenntSearch = elementSearch(driver1, elename);
			if (rfElemenntSearch.equalsIgnoreCase("Pass")) {
				System.out.println("Pass");

				// click the calender icon of the calender field
				WebDriverWait wait = new WebDriverWait(driver1, 10);
				wait.until(ExpectedConditions.elementToBeClickable(eleSearched));
				((JavascriptExecutor) driver1).executeScript("arguments[0].click();", eleSearched);
				Thread.sleep(200);
				System.out.println("Clicked");
				Thread.sleep(2000);

				// select the Month
				// month_input is an xpath
				rfElemenntSearch = elementSearch(driver1, "month_input");
				if (rfElemenntSearch.equalsIgnoreCase("Pass")) {
					eleSearched.sendKeys(strMonth);
					String month = eleSearched.getAttribute("value");
					System.out.println("Month after selection : " + month);
				} else {
					System.out.println("Month field not found");
				}

				// select the year
				// year_input is an xpath
				rfElemenntSearch = elementSearch(driver1, "year_input");
				if (rfElemenntSearch.equalsIgnoreCase("Pass")) {
					eleSearched.sendKeys(strYear);
					String year = eleSearched.getAttribute("value");
					System.out.println("Year after slection : " + year);
				} else {
					System.out.println("Year field not found");
				}

				Thread.sleep(2000);

				// select the date from the table using anchor tag and click on
				// the anchor tag to click the date
				// date_table is an xpath
				rfElemenntSearch = elementSearch(driver1, "date_table");
				if (rfElemenntSearch.equalsIgnoreCase("Pass")) {
					List<WebElement> td = eleSearched.findElements(By.tagName("a"));
					{
						for (WebElement data : td) {
							String value = data.getText();
							System.out.println("Td value is " + value);
							if (value.equalsIgnoreCase(strDate) && data.isEnabled()) {
								wait = new WebDriverWait(driver1, 10);
								wait.until(ExpectedConditions.elementToBeClickable(data));
								((JavascriptExecutor) driver1).executeScript("arguments[0].click();", data);
								System.out.println("date selected");
							}
						}
					}

				} else {
					System.out.println("Date table not found");
				}
			} else {
				System.out.println("Calender icon not found");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			CommonFunctions2.rfWriteToExcel(ExcelRead.currTestCaseId, e.getMessage());
			ExcelRead.testCaseStatus = "fail";
			ExcelRead.testCaseError = e.getMessage();
		}

	}

	public static void getText(String elename, String variableName, WebDriver driver1) {
		try {
			String rfElemenntSearch = elementSearch(driver1, elename);
			if (rfElemenntSearch.equalsIgnoreCase("Pass")) {
				System.out.println("Value found is " + eleSearched.getText());
				ExcelRead.runTimeVar.put(variableName, eleSearched.getText());
				ExcelRead.testCaseStatus = "pass";
			} else {
				System.out.println();
				ExcelRead.testCaseError = rfElemenntSearch;
				ExcelRead.testCaseStatus = "fail";
			}
		} catch (Exception e) {
			ExcelRead.testCaseError = e.getMessage();
			CommonFunctions2.rfWriteToExcel(ExcelRead.currTestCaseId, e.getMessage());
			ExcelRead.testCaseStatus = "fail";
		}
	}

	/*
	 * Function : Get Text from an element and compare with element provided in
	 * Parameter Input Parameters : ElementName present in properties file name
	 * you need to fetch value , WebDriver
	 * 
	 * Excel input as : Elename , TagName , ValueTo Compare
	 * 
	 */
	public static String getTextandCompare(String elename, String value2Compare, WebDriver driver1) {

		String text = null;
		try {
			String rfElemenntSearch = elementSearch(driver1, elename);
			if (rfElemenntSearch.equalsIgnoreCase("Pass")) {
				text = eleSearched.getText();
				if (text.equalsIgnoreCase("")) {
					ExcelRead.testCaseStatus = "fail";
					ExcelRead.testCaseError = "No value in the element : " + elename;
					System.out.println("No value in the element : " + elename);
				} else {
					if (value2Compare.equalsIgnoreCase(text)) {
						ExcelRead.testCaseStatus = "pass";
						System.out.println(
								"Value matching with given value : " + value2Compare + " and web value " + text);
					} else {

						ExcelRead.testCaseStatus = "fail";
						ExcelRead.testCaseError = "Value not matching";
						System.out.println(
								"Value not matching with given value : " + value2Compare + " and web value " + text);
					}
				}
			} else {
				captureScreenshot(elename, driverobj.get());
				ExcelRead.testCaseStatus = "fail";
				System.out.println("Element not found " + rfElemenntSearch);
			}
		} catch (Exception e) {
			// write something here
			CommonFunctions2.rfWriteToExcel(ExcelRead.currTestCaseId, e.getMessage());
			ExcelRead.testCaseStatus = "fail";
			ExcelRead.testCaseError = e.getMessage();
		}
		return text;
	}

	/*
	 * Function : Get Text from an element and compare with element provided in
	 * Parameter Input Parameters : ElementName present in properties file , Tag
	 * name you need to fetch value , WebDriver
	 * 
	 * Excel input as : Elename , TagName , ValueTo Compare
	 * 
	 */
	public static void getAttrandCompareWith(String elename, String tagName, String value2Compare, WebDriver driver1) {

		String text = "";

		try {
			String rfElemenntSearch = elementSearch(driver1, elename);
			if (rfElemenntSearch.equalsIgnoreCase("Pass")) {
				text = eleSearched.getAttribute(tagName);
				if (text.equalsIgnoreCase("")) {
					ExcelRead.testCaseStatus = "fail";
					ExcelRead.testCaseError = "No value in the element : " + elename;
					System.out.println("No value in the element : " + elename);
				} else {
					if (value2Compare.equalsIgnoreCase(text)) {
						ExcelRead.testCaseStatus = "pass";
						System.out.println(
								"Value matching with given value : " + value2Compare + " and tag value :  " + text);
					} else {
						ExcelRead.testCaseStatus = "fail";
						ExcelRead.testCaseError = "Value not matching";
						System.out.println(
								"Value not matching with given value : " + value2Compare + " and tag value " + text);
					}
				}
			} else {
				ExcelRead.testCaseStatus = "fail";
				System.out.println("Element not found " + rfElemenntSearch);
			}
		} catch (Exception e) {
			CommonFunctions2.rfWriteToExcel(ExcelRead.currTestCaseId, e.getMessage());
			ExcelRead.testCaseStatus = "fail";
			ExcelRead.testCaseError = e.getMessage();
			e.printStackTrace();
		}
	}

	/*
	 * Mehtod Name : selectDropDown Inputs : Element Name , Dropdown value ,
	 * WebDriver Working : Value of the dropdown will be selected based on the
	 * value provided by the use it might be ByValue , ByIndex , By Text.
	 * Autohor : PranayakUmar Adepu : Infosys ltd
	 */

	public static void selectDropDown(String elename, String value, WebDriver driver1) {
		try {
			int i;
			String rfElemenntSearch = elementSearch(driver1, elename);
			String[] temp = value.split(":");
			Select select = new Select(eleSearched);
			if (rfElemenntSearch.equalsIgnoreCase("Pass")) {
				System.out.println("Trying to select from Dropdown-----------");
				if (temp[1].equalsIgnoreCase("text")) {
					select.selectByVisibleText(temp[0]);
				} else if (temp[1].equalsIgnoreCase("index")) {
					i = Integer.parseInt(temp[0]);
					select.selectByIndex(i);
				} else if (temp[1].equalsIgnoreCase("value")) {
					select.selectByValue(temp[0]);
				}
				ExcelRead.testCaseStatus = "pass";
				Thread.sleep(1000);
			} else {
				ExcelRead.testCaseStatus = "fail";
				System.out.println("Element not present");
				ExcelRead.testCasePrint = "Element not present";
				CommonFunctions2.rfWriteToExcel(ExcelRead.currTestCaseId, ExcelRead.testCasePrint);
			}

		} catch (Exception e) {
			CommonFunctions2.rfWriteToExcel(ExcelRead.currTestCaseId, e.getMessage());
			ExcelRead.testCaseStatus = "fail";
			System.out.println("Element not selected");
			System.out.println(e.getMessage() + elename);
			e.getMessage();

		}

	}

	/*
	 * Mehtod Name : selectDropDownText Inputs : Element Name , Dropdown value ,
	 * WebDriver Working : Value of the dropdown will be selected based on the
	 * value provided by the user. Autohor : PranayakUmar Adepu : Infosys ltd
	 */
	public static void selectDropDownText(String elename, String value, WebDriver driver1) {
		try {
			String rfElemenntSearch = elementSearch(driver1, elename);
			Select select = new Select(eleSearched);
			if (rfElemenntSearch.equalsIgnoreCase("Pass")) {
				System.out.println("Trying to select from Dropdown-----------" + value);
				select.selectByVisibleText(value);
				ExcelRead.testCaseStatus = "pass";
				Thread.sleep(1000);
			} else {
				ExcelRead.testCaseStatus = "fail";
				ExcelRead.testCasePrint = "Element not present";
				CommonFunctions2.rfWriteToExcel(ExcelRead.currTestCaseId, ExcelRead.testCasePrint);
				System.out.println("Element not present");
			}
		} catch (Exception e) {
			CommonFunctions2.rfWriteToExcel(ExcelRead.currTestCaseId, e.getMessage());
			ExcelRead.testCaseStatus = "fail";
			System.out.println("Element not selected");
			System.out.println(e.getMessage() + elename);
		}

	}

	/*
	 * Mehtod Name : selectByPartialText Inputs : Element Name , Dropdown value
	 * , WebDriver Working : Value of the dropdown will be selected based on the
	 * value provided by the user. Autohor : PranayakUmar Adepu : Infosys ltd
	 */

	public static void selectByPartialText(String elename, String value, WebDriver driver1) {
		try {
			String rfElemenntSearch = elementSearch(driver1, elename);
			Select select = new Select(eleSearched);
			if (rfElemenntSearch.equalsIgnoreCase("Pass")) {
				System.out.println("Trying to select from Dropdown-----------");
				List<WebElement> options = eleSearched.findElements(By.tagName("option"));
				int i = 0, count = 0;
				for (WebElement text : options) {
					i++;
					String textContent = text.getAttribute("value");
					if (textContent.toLowerCase().contains(value.toLowerCase())) {
						count = i - 1;
						break;
					}
				}
				if (count == 0) {
					ExcelRead.testCaseStatus = "fail";
					System.out.println("Text nmot present in dropdown");
				} else {
					select.selectByIndex(count);
					ExcelRead.testCaseStatus = "pass";
					Thread.sleep(1000);
				}

			} else {
				ExcelRead.testCaseStatus = "fail";
				ExcelRead.testCasePrint = "Element not present";
				CommonFunctions2.rfWriteToExcel(ExcelRead.currTestCaseId, ExcelRead.testCasePrint);
				System.out.println("Element not present");
			}

		} catch (Exception e) {
			CommonFunctions2.rfWriteToExcel(ExcelRead.currTestCaseId, e.getMessage());
			ExcelRead.testCaseStatus = "fail";
			System.out.println("Element not selected");
			System.out.println(e.getMessage() + elename);
		}

	}

	public static void rfWriteToExcel(String pKey, String usrComment) {
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			System.out.println(dateFormat.format(date)); // 2016/11/16 12:08:43
			String vDataWB = System.getProperty("user.dir") + "\\Reports\\Data.xls";
			File file = new File(vDataWB);
			// Workbook.getWorkbook(file);
			// Workbook wb = Workbook.getWorkbook(file);
			// Sheet vSheet = wb.getSheet("Data");
			WritableWorkbook workbook;
			WritableSheet sheet;
			Workbook workbookRead = Workbook.getWorkbook(new File(vDataWB));
			workbook = Workbook.createWorkbook(file, workbookRead);
			sheet = workbook.getSheet("Data");
			int vRowCount = sheet.getRows();
			System.out.println("Ths is rows : " + vRowCount);
			WritableCellFormat cellFormat = new WritableCellFormat();
			cellFormat.setWrap(true);
			Label label0 = new Label(0, (vRowCount), Integer.toString(vRowCount), cellFormat);
			sheet.addCell(label0);
			// System.out.println("Ths is rows : 1");
			Label label1 = new Label(1, (vRowCount), pKey, cellFormat);
			sheet.addCell(label1);
			// System.out.println("Ths is rows : 2");
			Label label2 = new Label(2, (vRowCount), usrComment, cellFormat);
			sheet.addCell(label2);
			// System.out.println("Ths is rows : 3");
			Label label3 = new Label(3, (vRowCount), dateFormat.format(date), cellFormat);
			sheet.addCell(label3);
			// System.out.println("Ths is rows : 4");
			workbook.write();
			workbook.close();
			System.out.println("Data added : " + usrComment);
		} catch (Exception ex) {
			ExcelRead.testCaseStatus = "fail";
			ExcelRead.testCaseError = ex.getMessage();
			System.out.println(ex.getStackTrace());
		}
	}

	public static void uploadFile(String elename, String value, WebDriver driver1) {
		try {
			String rfElemenntSearch = elementSearch(driver1, elename);
			if (rfElemenntSearch.equalsIgnoreCase("Pass")) {

			} else {
				ExcelRead.testCaseStatus = "fail";
				System.out.println("Element not present");
				driver1.findElement(By.name("send")).click();
			}

		} catch (Exception e) {
			CommonFunctions2.rfWriteToExcel(ExcelRead.currTestCaseId, e.getMessage());
			ExcelRead.testCaseStatus = "fail";
			System.out.println("Element not found");
			System.out.println(e.getMessage() + elename);
		}

	}

	public static void selectPriorityBit(String value, WebDriver driver1) {
		try {
			if (value.equalsIgnoreCase("P0") || value.equalsIgnoreCase("P0BIS")) {
				selectDropDown("order_Priority_BitO", value, driver1);
			} else if (value.equalsIgnoreCase("P1") || value.equalsIgnoreCase("P1BIS")) {
				selectDropDown("order_Priority_Bit1", value, driver1);
			} else if (value.equalsIgnoreCase("P3") || value.equalsIgnoreCase("P3BIS")) {
				selectDropDown("order_Priority_Bit3", value, driver1);
			} else if (value.equalsIgnoreCase("P5") || value.equalsIgnoreCase("P5BIS")) {
				selectDropDown("order_Priority_Bit3", value, driver1);
			}
		} catch (Exception ex) {
			System.out.println("Error : " + ex.getMessage());
			CommonFunctions2.rfWriteToExcel(ExcelRead.currTestCaseId, ex.getMessage());
			ExcelRead.testCaseStatus = "fail";
		}
	}

	public boolean elementEnabled(String elename, String eleType, WebDriver driver1) {
		try {
			String rfElemenntSearch = null;
			rfElemenntSearch = elementSearch(driver1, elename);
			if (rfElemenntSearch.equalsIgnoreCase("Pass")) {
				System.out.println("Checking element is enabled or not at  " + elename);
				if (eleSearched.isEnabled()) {
					System.out.println("Element is enabled at  " + elename);
					return true;
				} else {
					System.out.println("Element is not enabled " + elename);
					return false;
				}
			} else {
				// need to write
			}
		} catch (Exception ex) {
			CommonFunctions2.rfWriteToExcel(ExcelRead.currTestCaseId, ex.getMessage());
			ExcelRead.testCaseStatus = "fail";
		}
		return false;
	}

	public static boolean elementDisplayed(String elename, WebDriver driver1) {
		try {
			String rfElemenntSearch = null;
			rfElemenntSearch = elementSearch(driver1, elename);
			if (rfElemenntSearch.equalsIgnoreCase("Pass")) {
				System.out.println("Checking element is displayed or not at  " + elename);
				if (eleSearched.isDisplayed()) {
					System.out.println("Element is displayed at  " + elename);
					return true;
				} else {
					System.out.println("Element is not displayed " + elename);
					return false;
				}
			} else {
				ExcelRead.testCaseStatus = "fail";
				System.out.println("Element not present");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			CommonFunctions2.rfWriteToExcel(ExcelRead.currTestCaseId, ex.getMessage());
			ExcelRead.testCaseStatus = "fail";
		}
		return false;
	}

	public static void validateTitle(WebDriver driver1, String expectedTitle) {
		try {
			String actualTitle = driver1.getTitle();
			if (!actualTitle.equalsIgnoreCase(expectedTitle)) {
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			CommonFunctions2.rfWriteToExcel(ExcelRead.currTestCaseId, ex.getMessage());
			ExcelRead.testCaseStatus = "fail";
		}
	}
	/*
	 * Author : PRanay Adep IVS-ECS Date : 25/01/2018 Inputs : Xpath , Input
	 * Values separated by comma Output : result with unmacthed values Dropdown
	 * Values validation with the given inputs
	 */

	public static void dropdownValueValidation(WebDriver driver1, String elename, String ExpecteddropdownValues) {
		String expValueString[] = ExpecteddropdownValues.split(",");
		String rfElemenntSearch = null;
		System.out.println("------------------------------------------");
		try {
			rfElemenntSearch = elementSearch(driver1, elename);
			if (rfElemenntSearch.equalsIgnoreCase("Pass")) {
				Select dropdown = new Select(eleSearched);
				List<WebElement> values = dropdown.getOptions();

				List<String> dropdownValueitem = new ArrayList<String>();
				for (WebElement item : values) {
					dropdownValueitem.add(item.getText());
					// System.out.println(item.getText());
				}
				int dropdownLength = dropdownValueitem.size() - 1;
				System.out.println("----------------------------------------");
				System.out.println("Expected Value size : " + expValueString.length);
				System.out.println("Dropdown Value size : " + dropdownLength);
				int length = expValueString.length + 1;
				if (dropdownValueitem.size() == length) {
					for (int i = 0; i < expValueString.length; i++) {
						// if (ArrayUtils.contains(expValueString,
						// dropdownValueitem.get(i))) {
						if (dropdownValueitem.contains(expValueString[i].trim())) {
						} else {
							System.out.println("Expected values are not present in the dropdown " + expValueString[i]);
						}

					}
				} else {
					System.out.println("Expected values and Dropdown values count mismacth ");
					System.out.println("Expected Value size : " + expValueString.length);
					System.out.println("Dropdown Value size : " + dropdownLength);
					System.out.println("Dropdown values are ");
					for (WebElement item : values) {
						System.out.println(item.getText());
					}
				}

				// }
			} else {
				ExcelRead.testCaseStatus = "fail";
				System.out.println("Element not present");
			}
		} catch (Exception e) {
			e.printStackTrace();
			CommonFunctions2.rfWriteToExcel(ExcelRead.currTestCaseId, e.getMessage());
			ExcelRead.testCaseStatus = "fail";
		}

	}
}
