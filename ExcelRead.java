package testScripts;

import java.io.File;
import java.util.HashMap;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import jxl.Sheet;
import jxl.Workbook;
import reusables.CommonFunctions2;

public class ExcelRead extends ExcelDriverBridge implements ExcelData {
	public static HashMap<String, String> mapOR = new HashMap<String, String>();
	public static HashMap<String, String> runTimeVar = new HashMap<String, String>();
	public static String testCaseError, receivedXML, corrid, currTestCaseName, currTestCaseId;
	public static String testCaseStatus = "none", testCasePrint = "none";
	public static String projectPath = System.getProperty("user.dir"), screenshotPath = "";
	static ExtentReports extent;
	static ExtentTest test;

	@SuppressWarnings("unused")
	public static void main(String[] args) throws Exception {

		String executionWorkBook = projectPath + "\\ProvideRuns.xls";
		CommonFunctions2.setPropFile(projectPath + "\\src\\main\\resources\\element.properties");
		extent = new ExtentReports(projectPath + "/Reports/ITT/WWS_ITT_ProvideVolumeRun.html", false);
		extent.addSystemInfo("Environment", " ITT").addSystemInfo("User", "id848699");
		extent.loadConfig(new File(projectPath + "/extent-config.xml"));

		File file = new File(executionWorkBook);

		Workbook.getWorkbook(file);
		Workbook wb = Workbook.getWorkbook(file);

		/*
		 * Sheet orSheet = wb.getSheet("OR"); int orSheetRowCount =
		 * orSheet.getRows(); // int = orSheet.getColumns(); for (int
		 * orSheetRowIterator = 1; orSheetRowIterator < orSheetRowCount;
		 * orSheetRowIterator++) { mapOR.put(orSheet.getCell(2,
		 * orSheetRowIterator).getContents(), orSheet.getCell(3,
		 * orSheetRowIterator).getContents()); }
		 */

		Sheet tsSheet = wb.getSheet("TestSuites");
		int tsSheetRowCount = tsSheet.getRows();
		// int tsSheetColCount = tsSheet.getColumns();

		for (int tsSheetIterator = 1; tsSheetIterator < tsSheetRowCount; tsSheetIterator++) {
			String currTestSuiteName = tsSheet.getCell(1, tsSheetIterator).getContents();
			// System.out.println(currTestSuiteName);

			String currTestSuiteFlag = tsSheet.getCell(2, tsSheetIterator).getContents();
			// System.out.println(currTestSuiteFlag);

			if (currTestSuiteFlag.equalsIgnoreCase("y")) {
				runTimeVar = new HashMap<String, String>();
				// runTimeVar.put("startingMap", "startingMap");
				Sheet tcSheet = wb.getSheet("TestCases");
				int tcSheetRowCount = tcSheet.getRows();
				int tcSheetColCount = tcSheet.getColumns();

				for (int tcSheetIterator = 1; tcSheetIterator < tcSheetRowCount; tcSheetIterator++) {
					currTestCaseId = tcSheet.getCell(0, tcSheetIterator).getContents();
					currTestCaseName = tcSheet.getCell(2, tcSheetIterator).getContents();
					System.out.println(currTestCaseName);
					String currTestCaseFlag = tcSheet.getCell(3, tcSheetIterator).getContents();
					System.out.println(currTestCaseFlag);

					if (currTestCaseFlag.equalsIgnoreCase("y")
							&& tcSheet.getCell(1, tcSheetIterator).getContents().equalsIgnoreCase(currTestSuiteName)) {
						test = extent.startTest(currTestCaseName);
						// driver.findElement(By.xpath("//*[contains(@href,'new_lang=fr')]")).click();
						int currTestCaseStartRow = Integer
								.parseInt(tcSheet.getCell(testcaseStartRow, tcSheetIterator).getContents());
						int currTestCaseStepCount = Integer
								.parseInt(tcSheet.getCell(testcaseStepCount, tcSheetIterator).getContents());
						// String currTestCaseData =
						// tcSheet.getCell(testCaseData,
						// tcSheetIterator).getContents();
						String currTestCaseBrowser = tcSheet.getCell(testcasebrowser, tcSheetIterator).getContents();
						String[] currTestCaseIP = new String[30];

						for (int tcIPIterator = testCaseInputStart; tcIPIterator < tcSheetColCount; tcIPIterator++) {
							currTestCaseIP[tcIPIterator - testCaseInputStart] = tcSheet
									.getCell(tcIPIterator, tcSheetIterator).getContents();
							// System.out.println(currTestCaseIP[tcIPIterator -
							// 8]);
						}

						// WebDriver driver = openBrowser(currTestCaseBrowser);
						// Thread.sleep(2000);
						// driver.get("https://webmail.itt.proximus.be/");
						Sheet tsSSheet = wb.getSheet("TestSteps");
						Sheet bpcSheet = wb.getSheet("BPCs");
						// int tsSSheetRowCount = tsSSheet.getRows();
						int tsSSheetColCount = tsSSheet.getColumns();

						for (int tsSSheetIterator = currTestCaseStartRow - 1; tsSSheetIterator < (currTestCaseStartRow
								+ currTestCaseStepCount - 1); tsSSheetIterator++) {
							// System.out.println(stepType +" , "+
							// tsSSheetIterator);
							String currTestStepType = tsSSheet.getCell(stepType, tsSSheetIterator).getContents();
							// System.out.println(currTestStepType);
							String currTestStepUDF = tsSSheet.getCell(stepName, tsSSheetIterator).getContents();
							String currTestStepDescription = tsSSheet.getCell(stepDescription, tsSSheetIterator)
									.getContents();
							String currTestStepReport = tsSSheet.getCell(reportNeeded, tsSSheetIterator).getContents();
							String currTestStepExec = tsSSheet.getCell(steptoBeExecuted, tsSSheetIterator)
									.getContents();
							;
							String[] currTestStepIP = new String[15];

							for (int tsSIPIterator = testStepInputStart; tsSIPIterator < tsSSheetColCount; tsSIPIterator++) {
								// System.out.println(tsSIPIterator);

								if (!tsSSheet.getCell(tsSIPIterator, tsSSheetIterator).getContents().isEmpty()) {
									currTestStepIP[tsSIPIterator - testStepInputStart] = tsSSheet
											.getCell(tsSIPIterator, tsSSheetIterator).getContents();
									// System.out.println(currTestStepIP[tsSIPIterator
									// - 5]);
									// System.out.println(currTestStepIP[tsSIPIterator-5].substring(0,
									// 1));
									if (currTestStepIP[tsSIPIterator - testStepInputStart].contains("::")) {
										int indexIP = currTestStepIP[tsSIPIterator - testStepInputStart].indexOf("::");
										String[] customIP = currTestStepIP[tsSIPIterator - testStepInputStart]
												.substring(indexIP + 2).split("_");

										switch (customIP[0]) {
										case "TCIP":
											currTestStepIP[tsSIPIterator
													- testStepInputStart] = currTestStepIP[tsSIPIterator
															- testStepInputStart].substring(0, indexIP).concat(
																	currTestCaseIP[Integer.parseInt(customIP[1]) - 1]);
											break;
										case "TEMP":
											while (currTestStepIP[tsSIPIterator - testStepInputStart]
													.contains("::TEMP")) {
												currTestStepIP[tsSIPIterator - testStepInputStart] = retRunTimeVar(
														currTestStepIP[tsSIPIterator - testStepInputStart]);
											}
											break;
										default:
											System.out.println("Invalid input");
											break;
										}
									}
								}
							}
							System.out.println("This is current row : " + tsSSheetIterator);
							if (currTestStepType.equalsIgnoreCase("BPC") && currTestStepExec.equalsIgnoreCase("Y")) {
								int bpcStartRow = Integer.parseInt(currTestStepIP[0]);
								int bpcStepCount = Integer.parseInt(currTestStepIP[1]);

								for (int bpcRowIterator = bpcStartRow - 1; bpcRowIterator < (bpcStartRow + bpcStepCount
										- 1); bpcRowIterator++) {
									String currBPCTestStepType = bpcSheet.getCell(stepType, bpcRowIterator)
											.getContents();
									String currBPCTestStepUDF = bpcSheet.getCell(stepName, bpcRowIterator)
											.getContents();
									String currBPCTestStepDescription = bpcSheet
											.getCell(stepDescription, bpcRowIterator).getContents();
									String currBPCTestStepReport = bpcSheet.getCell(reportNeeded, bpcRowIterator)
											.getContents();
									String currBPCTestStepExec = bpcSheet.getCell(steptoBeExecuted, bpcRowIterator)
											.getContents();
									String[] currBPCTestStepIP = new String[10];

									for (int bpcIPIterator = testStepInputStart; bpcIPIterator < bpcSheet
											.getColumns(); bpcIPIterator++) {
										// System.out.println(bpcIPIterator);
										if (!bpcSheet.getCell(bpcIPIterator, bpcRowIterator).getContents().isEmpty()) {
											currBPCTestStepIP[bpcIPIterator - testStepInputStart] = bpcSheet
													.getCell(bpcIPIterator, bpcRowIterator).getContents();

											if (currBPCTestStepIP[bpcIPIterator - testStepInputStart].contains("::")) {
												int indexIP = currBPCTestStepIP[bpcIPIterator - testStepInputStart]
														.indexOf("::");
												String[] customIP = currBPCTestStepIP[bpcIPIterator
														- testStepInputStart].substring(indexIP + 2).split("_");

												switch (customIP[0]) {
												case "TCIP":
													currBPCTestStepIP[bpcIPIterator
															- testStepInputStart] = currBPCTestStepIP[bpcIPIterator
																	- testStepInputStart].substring(0, indexIP).concat(
																			currTestCaseIP[Integer.parseInt(customIP[1])
																					- 1]);
													break;
												case "TSIP":
													currBPCTestStepIP[bpcIPIterator
															- testStepInputStart] = currBPCTestStepIP[bpcIPIterator
																	- testStepInputStart].substring(0, indexIP).concat(
																			currTestStepIP[Integer.parseInt(customIP[1])
																					- 1]);
													break;
												case "TEMP":
													while (currBPCTestStepIP[bpcIPIterator - testStepInputStart]
															.contains("::TEMP")) {
														currBPCTestStepIP[bpcIPIterator
																- testStepInputStart] = retRunTimeVar(
																		currBPCTestStepIP[bpcIPIterator
																				- testStepInputStart]);
													}
													break;
												default:
													System.out.println("Invalid input");
													break;
												}
											}
											// System.out.println(currBPCTestStepIP[bpcIPIterator
											// - 5]);
										}
									}
									if (currBPCTestStepExec.equalsIgnoreCase("Y")) {
										System.out.println("This is current row  in BPC: " + bpcRowIterator);

										System.out.println("-------------------------------------------");
										System.out.println("Step Name is : " + currBPCTestStepDescription);
										testCasePrint = "none";
										testCaseStatus = "none";
										System.out.println(ExcelRead.testCaseStatus
												+ " --------before reusable function-----------------------------------");
										reusableFunctions(currTestCaseBrowser, currBPCTestStepUDF, currBPCTestStepIP);
										System.out.println(ExcelRead.testCaseStatus
												+ " --------after reusable function-----------------------------------");
										if (currBPCTestStepReport.equalsIgnoreCase("Y")) {
											if (ExcelRead.testCaseStatus.equalsIgnoreCase("fail")) {
												test.log(LogStatus.FAIL,
														"Test step failed for : " + currBPCTestStepDescription);
												test.log(LogStatus.FAIL, "with error as  : " + testCaseError);
												// driverobj.get().close();
												System.out
														.println("--------to break-----------------------------------");
												break;
											} else {
												if (testCasePrint.equals("none")) {
													test.log(LogStatus.PASS,
															"Test step passed for : " + currBPCTestStepDescription);

												} else {
													test.log(LogStatus.PASS,
															"Test step passed for : " + currBPCTestStepDescription);
													test.log(LogStatus.PASS, "Your message is : " + testCasePrint);

												}
											}
										}
										// if
										// (currBPCTestStepUDF.equalsIgnoreCase("rfElementClick")
										// ||
										// currBPCTestStepUDF.equalsIgnoreCase("logout")
										// ||
										// currBPCTestStepUDF.equalsIgnoreCase("getTextandCompare"))
										// {
										// test.log(LogStatus.PASS, "Screenshot
										// is :",
										// test.addScreenCapture(screenshotPath));
										// }
										System.out.println("-------------------------------------------");
									}

								}
								if (testCaseStatus.equalsIgnoreCase("fail")) {
									System.out.println("-------------------TO break after BPC------------------------");
									break;
								}

							} else {
								// test step starting

								if (currTestStepExec.equalsIgnoreCase("Y")) {
									System.out.println("This is current row : " + tsSSheetIterator);
									System.out.println("-------------------------------------------");
									System.out.println("Step Name is : " + currTestStepDescription);
									testCasePrint = "none";
									reusableFunctions(currTestCaseBrowser, currTestStepUDF, currTestStepIP);
									System.out.println("testcase status" + testCaseStatus);
									if (currTestStepReport.equalsIgnoreCase("Y")) {
										if (testCaseStatus.equalsIgnoreCase("fail")) {
											test.log(LogStatus.FAIL,
													"Test step failed for : " + currTestStepDescription);
											test.log(LogStatus.FAIL, "with error as  : " + testCaseError);
											// test.log(LogStatus.FAIL,
											// "Screenshot is :",
											// test.addScreenCapture(screenshotPath));
											// driverobj.get().close();
											// driverobj.get().quit();
											break;
										} else {
											if (testCasePrint.equals("none")) {
												test.log(LogStatus.PASS,
														"Test step passed for : " + currTestStepDescription);

											} else {
												test.log(LogStatus.PASS,
														"Test step passed for : " + currTestStepDescription);
												test.log(LogStatus.PASS, "Your message is : " + testCasePrint);

											}
										}
									}
									System.out.println("-------------------------------------------");
								}
								// if
								// (currTestStepUDF.equalsIgnoreCase("rfElementClick")
								// || currTestStepUDF.equalsIgnoreCase("logout")
								// ||
								// currTestStepUDF.equalsIgnoreCase("getTextandCompare"))
								// {
								// test.log(LogStatus.PASS, "Screenshot is :",
								// test.addScreenCapture(screenshotPath));
								// }

							}

						}
						if (testCaseStatus.equalsIgnoreCase("fail")) {
							// driverobj.get().close();
							extent.endTest(test);
							driverobj.get().quit();
							// break;
						} else {
							extent.endTest(test);
							driverobj.get().quit();
						}
						Thread.sleep(3000);

						// driverobj.get().close();
						// driverobj.get().quit();
					}
				}
			}
		}
		wb.close();
		tearDown();
	}

	private static void tearDown() {
		extent.flush();
		extent.close();
		driverobj.get().quit();
		System.gc();
		System.exit(0);
	}

	public static String retRunTimeVar(String inputParam) {
		System.out.println("Inside : returnTimeVar " + inputParam);
		if (inputParam.contains("TEMP")) {
			int indexOF = inputParam.indexOf("::");
			int spaceIndex = indexOF + inputParam.substring(indexOF).indexOf(" ") + 1;
			String runTimeVariableKey = inputParam.substring(indexOF + 2, spaceIndex);
			String newrunTimeVariableKey = runTimeVariableKey.replaceAll("\\s+", "");
			if (!runTimeVar.containsKey(newrunTimeVariableKey)) {
				runTimeVar.put(newrunTimeVariableKey, newrunTimeVariableKey);
			}
			String runTimeVariableValue = runTimeVar.get(newrunTimeVariableKey);
			inputParam = inputParam.substring(0, indexOF).concat(runTimeVariableValue)
					.concat(inputParam.substring(spaceIndex));
			System.out.println("Variable key is : " + runTimeVariableKey);
			System.out.println("Variable value is : " + runTimeVariableValue);
		}
		return inputParam;
	}

}
