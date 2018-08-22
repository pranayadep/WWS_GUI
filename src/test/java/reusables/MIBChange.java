package reusables;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class MIBChange {

	public static void MIBChane(WebDriver driver) throws InterruptedException {
		Thread.sleep(1000);
		driver.findElement(By.xpath(".//*[@id='txtUserID']")).sendKeys("snorlax");
		driver.findElement(By.xpath(".//*[@id='txtPassword']")).sendKeys("rules");
		driver.findElement(By.xpath(".//*[@id='sub']")).click();
		driver.findElement(By.xpath(".//*[@id='pySearchText']")).sendKeys("old:FindMIBResponse");
		driver.findElement(By.xpath(".//*[@id='pySearchText']")).sendKeys(Keys.ENTER);
		Thread.sleep(1000);
		driver.findElement(By.xpath("(.//*[@id='RULE_KEY']/table/tbody/tr/td/nobr/span/a)[4]")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(0);
		driver.findElement(By.xpath("(.//*[@id='RULE_KEY']/div/div/div/div/div/div/span/button)[1]")).click();
		Thread.sleep(1000);


/*		WebElement login = driver.findElement(By.xpath("//div[contains(@class, 'CodeMirror-vscrollbar')]"));
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("arguments[0].scrollIntoView(true);", login);
		
		WebElement scroll = driver.findElement(By.xpath("(//pre)[20]"));
		scroll.sendKeys(Keys.DOWN);
		scroll.sendKeys(Keys.DOWN);
		scroll.sendKeys(Keys.DOWN);
		scroll.sendKeys(Keys.DOWN);*/
		driver.findElement(By.xpath("(.//*[@id='RULE_KEY']/div/div/div/div[1]/div/div/span/button)[4]")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath(".//*[@id='$PRH_1$ppyFilePath']")).click();
		Thread.sleep(2000);
		
		//String xmlFile = driver.findElement(By.xpath("// div[contains(@class, 'code-editor-container')]/textarea")).getText();
		//System.out.println("XML FIle is  :" +xmlFile);

		// div[contains(@class, 'CodeMirror-lines')]
		List<WebElement> ele = driver.findElements(By.xpath("(//pre)"));
		System.out.println(ele.size());

	}

}
