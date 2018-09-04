package testScripts;

import java.io.IOException;

import org.openqa.selenium.WebDriver;

import reusables.CommonFunctions2;
import reusables.MIBChange;

public class ExcelDriverBridge extends CommonFunctions2 {
	public static ThreadLocal<WebDriver> driverobj = new ThreadLocal<WebDriver>();

	public static void reusableFunctions(String currTestCaseBrowser, String currTestStepUDF, String[] currTestStepIP)
			throws InterruptedException, IOException {
		// String locator = ExcelRead.mapOR.get(currTestStepIP[0]);
		if (currTestStepUDF.equalsIgnoreCase("rfElementClick") || currTestStepUDF.equalsIgnoreCase("logout")
				|| currTestStepUDF.equalsIgnoreCase("getTextandCompare")) {
			captureScreenshot(currTestStepIP[0], driverobj.get());
		}
		switch (currTestStepUDF) {
		case "rfOpenBrowser":
			driverobj.set(SetBrowser(currTestCaseBrowser));
			break;
		case "rfStartApp":
			launchUrl(driverobj.get(), currTestStepIP[0]);
			break;
		case "rfSendText":
			sendValue(currTestStepIP[0], currTestStepIP[1], driverobj.get());
			break;
		case "rfElementClick":
			elementclick(currTestStepIP[0], driverobj.get());
			break;
		case "multipleClick":
			multipleClicks(currTestStepIP[0], driverobj.get());
			break;
		case "doubleClick":
			doubleClick(currTestStepIP[0], driverobj.get());
			break;
		case "frameSwitch":
			driverobj.set(switchtoFrame(currTestStepIP[0], driverobj.get()));
			break;
		case "refresh":
			refresh(driverobj.get());
			break;
		case "sleep":
			sleep(currTestStepIP[0]);
			break;
		case "selectFromDropDown":
			selectDropDown(currTestStepIP[0], currTestStepIP[1], driverobj.get());
			break;
		case "selectDropDownText":
			selectDropDownText(currTestStepIP[0], currTestStepIP[1], driverobj.get());
			break;
		case "selectByPartialText":
			selectByPartialText(currTestStepIP[0], currTestStepIP[1], driverobj.get());
			break;
		case "sendRes":
			sendRes(currTestStepIP[0], currTestStepIP[1], currTestStepIP[2]);
			break;
		case "goUp":
			goUP();
			break;
		case "goDown":
			goDOWN();
			break;
		case "goDefault":
			driverobj.set(switchToDefaultContent(driverobj.get()));
			break;
		case "getAttrAndCompare":
			getAttrandCompareWith(currTestStepIP[0], currTestStepIP[1], currTestStepIP[2], driverobj.get());
			break;
		case "getTextandCompare":
			getTextandCompare(currTestStepIP[0], currTestStepIP[1], driverobj.get());
			break;
		case "getData":
			getDatafromDB(currTestStepIP[0], currTestStepIP[1], currTestStepIP[2]);
			break;
		case "selectAutoComplete":
			selectAutoComplete(currTestStepIP[0], currTestStepIP[1], driverobj.get());
			break;
		case "sendReq":
			sendReq(currTestStepIP[0], currTestStepIP[1], currTestStepIP[2]);
			break;
		case "getReq":
			getReq(currTestStepIP[0], currTestStepIP[1], currTestStepIP[2]);
			break;
		case "getReqMsg":
			getReqMsg(currTestStepIP[0], currTestStepIP[1], currTestStepIP[2]);
			break;
		case "logout":
			logout(driverobj.get());
			break;
		case "print":
			printValue(currTestStepIP[0], currTestStepIP[1]);
			break;
		case "getRespFlag":
			getRespFlag(currTestStepIP[0], currTestStepIP[1]);
			break;
		case "mibchange":
			MIBChange.MIBChane(driverobj.get());
			break;
		case "storeValue":
			storeValue(currTestStepIP[0], currTestStepIP[1]);
			break;
		case "replaceToVariable":
			replaceToVariable(currTestStepIP[0], currTestStepIP[1], currTestStepIP[2], currTestStepIP[3]);
			break;
		case "waitForAction":
			waitForAction();
			break;
		case "StoreXmlValue":
			StoreXmlValue(currTestStepIP[0], currTestStepIP[1], currTestStepIP[2]);
			break;
		case "validateTagValues":
			validateTagValues(currTestStepIP[0], currTestStepIP[1], currTestStepIP[2]);
			break;
		case "dropdownValueValidation":
			dropdownValueValidation(driverobj.get(), currTestStepIP[0], currTestStepIP[1]);
			break;
		case "screenshot":
			captureScreenshot(currTestStepIP[0], driverobj.get());
			break;
		case "clear":
			clear(currTestStepIP[0], driverobj.get());
			break;
		case "selectDate":
			selectDate(currTestStepIP[0],currTestStepIP[1], driverobj.get());
			break;
		case "highlight":
			highlightElement(currTestStepIP[0], driverobj.get());
			break;
		// captureScreenshot
		default:
			System.out.println("Invalid Step");
			break;
		}
	}
}
