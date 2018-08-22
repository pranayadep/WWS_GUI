package reusables;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CommonFunctions {

	private static Properties objectMapProps;
	public static ThreadLocal<WebDriver> driverobj = new ThreadLocal<WebDriver>();
	private static String baseUrl;
	public static WebElement eleSearched;
	public static Robot robot;

	public static void sleep(int millSec) {

		try {
			Thread.sleep(millSec);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static WebDriver SetBrowser(String strBrowser) throws IOException, InterruptedException {
		if (strBrowser.equalsIgnoreCase("IE") || strBrowser.equalsIgnoreCase("Internet Explorer")) {
			driverobj.set(setIEDriver(System.getProperty("user.dir") + "\\drivers\\IEDriverServer64.exe"));
		} else if (strBrowser.equalsIgnoreCase("Chrome") || strBrowser.equalsIgnoreCase("GoogleChrome")
				|| strBrowser.equalsIgnoreCase("Google Chrome")) {
			Thread.sleep(500);
			driverobj.set(setChromeDriver("C:\\Users\\id848699\\Desktop\\Driver\\IBM\\chromedriver\\chromedriver.exe"));
		}
		return driverobj.get();
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

	public void goUP() {
		try {
			robot.keyPress(KeyEvent.VK_PAGE_UP);
		} catch (Exception e) {
			// need to handle
			e.getMessage();
		}
	}

	public void goDOWN() {
		try {
			robot.keyPress(KeyEvent.VK_PAGE_DOWN);
		} catch (Exception e) {
			// need to handle
			e.getMessage();
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
	public void StringCompare(String expected, String actual) throws Exception {
		if (expected.equalsIgnoreCase(actual)) {
			System.out.println("Compare 2 strings" + "Compare 2 strings" + expected + " " + expected + " PASSED");
		} else {
			System.out.println("Compare 2 strings" + "Compare 2 strings" + expected + " " + actual + " FAILED");
			throw new Exception();
		}
	}

	public static void launchUrl(String url) {
		try {
			driverobj.get().get(url);
		} catch (Exception e) {
			e.getMessage();
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
			options.setExperimentalOption("useAutomationExtension", false);
			dcChrome = DesiredCapabilities.chrome();
			dcChrome.setCapability(ChromeOptions.CAPABILITY, options);
		} catch (Exception e) {
			e.getMessage();
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
		}
	}

	public static WebDriver switchtoFrame(int frame, WebDriver driver1) {
		try {
			WebDriverWait wait = new WebDriverWait(driver1, 10);
			wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frame));
		} catch (Exception e) {
			// need to write
		}
		return driver1;
	}

	public static void refresh(WebDriver driver1) {
		try {
			driver1.navigate().refresh();
		} catch (Exception e) {
			// need to write
		}
	}

	public static void waitForElementDisplayed(String elename, WebDriver driver1) {
		try {
			WebDriverWait wait = new WebDriverWait(driver1, 10);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(elename)));
		} catch (Exception e) {
			// need to write
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
			e.getMessage();
		}
		return arr;
	}

	public static String captureScreenshot(String screenshotFileName, WebDriver driver1) {
		String path = "";
		try {
			File scrFile = ((TakesScreenshot) driver1).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(
					System.getProperty("user.dir") + "\\WWS_GUI\\errorScreens\\" + screenshotFileName + ".jpg"));
			path = System.getProperty("user.dir") + "\\WWS_GUI\\errorScreens\\" + screenshotFileName + ".jpg";
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (NullPointerException npe) {
			path = System.getProperty("user.dir") + "\\WWS_GUI\\errorScreens\\" + screenshotFileName + ".jpg";
			npe.printStackTrace();
		}
		return path;
	}

	public static void goToBaseUrl(WebDriver driver1) {
		driver1.get(baseUrl);
	}

	public static String elementSearch(WebDriver driver, String locator) throws InterruptedException {
		try {
			String element[] = null;
			element = getObjectValue2(locator);
			WebDriverWait wait = new WebDriverWait(driver, 5);
			Thread.sleep(500);
			if (element[1].equalsIgnoreCase("id")) {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(element[0])));
				eleSearched = driver.findElement(By.id(element[0]));
				return "Pass";
			} else if (element[1].equalsIgnoreCase("xpath")) {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(element[0])));
				eleSearched = driver.findElement(By.xpath(element[0]));
				return "Pass";
			} else if (element[1].equalsIgnoreCase("name")) {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(element[0])));
				eleSearched = driver.findElement(By.name(element[0]));
				return "Pass";
			} else if (element[1].equalsIgnoreCase("linkText")) {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(element[0])));
				eleSearched = driver.findElement(By.linkText(element[0]));
				return "Pass";
			} else if (element[1].equalsIgnoreCase("cssSelector")) {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(element[0])));
				eleSearched = driver.findElement(By.cssSelector(element[0]));
				return "Pass";
			} else if (element[1].equalsIgnoreCase("partialLinkText")) {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText(element[0])));
				eleSearched = driver.findElement(By.partialLinkText(element[0]));
				return "Pass";
			} else if (element[1].equalsIgnoreCase("tagName")) {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText(element[0])));
				eleSearched = driver.findElement(By.partialLinkText(element[0]));
				return "Pass";
			} else {
				return "Fail@" + "please select valid locator type";
			}
		} catch (Exception ex) {
			return "Fail@" + ex.getMessage();
		}

	}

	public static void WWS_Login(WebDriver driver1, String user, String password) throws Exception {
		Thread.sleep(100);
		try {
			System.out.println("In login");
			sendValue("userTxtBox", user);
			sendValue("pwdTxtBox", password);
			elementclick("LoginBtnClick");
		} catch (Exception e) {
			e.getMessage();
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

	public static void elementclick(String locator) {
		try {
			String rfElemenntSearch = null;
			rfElemenntSearch = elementSearch(driverobj.get(), locator);

			if (rfElemenntSearch.equalsIgnoreCase("Pass")) {
				System.out.println("Clicking on :  " + locator);
				eleSearched.click();
				System.out.println("Clicked on : " + locator);
			} else {
				// need to write
				System.out.println("Status is : " + rfElemenntSearch);
				System.out.println("Element not found " + locator);
			}
		} catch (Exception ex) {
			// need to write
			ex.getMessage();
		}
	}

	public static void sendValue(String elename, String value) {
		try {
			String rfElemenntSearch = null;
			rfElemenntSearch = elementSearch(driverobj.get(), elename);
			if (rfElemenntSearch.equalsIgnoreCase("Pass")) {
				System.out.println("Sending this :  " + value + " to " + elename);
				eleSearched.sendKeys(value);
				System.out.println("Sent this : " + value + " to " + elename);
			} else {
				// need to write
				System.out.println("Element not found " + rfElemenntSearch);
			}
		} catch (Exception ex) {
			// need to write
			ex.getMessage();
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
				// need to write
			}
		} catch (Exception ex) {
			// need to write
		}
	}

	public static String getText(String elename, String tagname, WebDriver driver1) {

		String text = null;
		String element[] = null;
		element = getObjectValue2(elename);
		try {
			if (element[1].equalsIgnoreCase("xpath")) {
				text = driver1.findElement(By.xpath(element[0])).getAttribute(tagname);
			} else if (element[1].equalsIgnoreCase("id")) {
				text = driver1.findElement(By.id(element[0])).getAttribute(tagname);
			} else if (element[1].equalsIgnoreCase("name")) {
				text = driver1.findElement(By.name(element[0])).getAttribute(tagname);
			} else if (element[1].equalsIgnoreCase("css")) {
				text = driver1.findElement(By.cssSelector(element[0])).getAttribute(tagname);
			}
		} catch (Exception e) {
			// write something here
		}
		return text;
	}

	public static void selectDropDown(String elename, String value, WebDriver driver1) {
		int i;
		String element[] = null;
		element = getObjectValue2(elename);
		if (element[1].equalsIgnoreCase("xpath")) {
			if (element[2].equalsIgnoreCase("text")) {
				new Select(driver1.findElement(By.xpath(element[0]))).selectByVisibleText(value);
			} else if (element[2].equalsIgnoreCase("index")) {
				i = Integer.parseInt(value);
				new Select(driver1.findElement(By.xpath(element[0]))).selectByIndex(i);
			} else if (element[2].equalsIgnoreCase("value")) {
				new Select(driver1.findElement(By.xpath(element[0]))).selectByValue(value);
			}
		} else if (element[1].equalsIgnoreCase("id")) {
			if (element[2].equalsIgnoreCase("text")) {
				new Select(driver1.findElement(By.id(element[0]))).selectByVisibleText(value);
			} else if (element[2].equalsIgnoreCase("index")) {
				i = Integer.parseInt(value);
				new Select(driver1.findElement(By.id(element[0]))).selectByIndex(i);
			} else if (element[2].equalsIgnoreCase("value")) {
				new Select(driver1.findElement(By.id(element[0]))).selectByValue(value);
			}
		} else if (element[1].equalsIgnoreCase("name")) {
			if (element[2].equalsIgnoreCase("text")) {
				new Select(driver1.findElement(By.name(element[0]))).selectByVisibleText(value);
			} else if (element[2].equalsIgnoreCase("index")) {
				i = Integer.parseInt(value);
				new Select(driver1.findElement(By.name(element[0]))).selectByIndex(i);
			} else if (element[2].equalsIgnoreCase("value")) {
				new Select(driver1.findElement(By.name(element[0]))).selectByValue(value);
			}
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
			// need to write
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
				// need to write
			}
		} catch (Exception ex) {
			// need to write
		}
		return false;
	}

	public static void validateTitle(String expectedTitle) {
		try {
			String actualTitle = driverobj.get().getTitle();
			if (!actualTitle.equalsIgnoreCase(expectedTitle)) {
			}
		} catch (Exception ex) {
			// need to write something
		}
	}
}
