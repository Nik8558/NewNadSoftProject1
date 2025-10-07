package ecommerceproject;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class testFile {

	WebDriver driver;

	@Parameters("browser")
	@BeforeTest
	public void setup(String browser) {
		if (browser.equalsIgnoreCase("chrome")) {
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
		} else if (browser.equalsIgnoreCase("edge")) {
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
		} else if (browser.equalsIgnoreCase("firefox")) {
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
		}
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(40));
	}

	@Test
	public void ecommerceFlow() throws InterruptedException {
		// 1. Open URL
		driver.get("https://rohanqa.deposyt.store/");

		// 2. Click on Shop now button (CSS)
		driver.findElement(By.xpath("//a[normalize-space()='SHOP NOW']")).click();

		// 3. Open Apple Watch product (Search flow)

		// Click on the search icon/button
		driver.findElement(By.xpath("//a[@data-testid='nav-search-link']")).click();
		driver.findElement(By.xpath("//input[@placeholder='Search products...']")).sendKeys("apple watch");
		
		driver.findElement(By.xpath("(//img[@alt='Thumbnail'])[1]")).click();

		// 4. Validate price on listing vs product page
		String productPrice = driver.findElement(By.xpath("//span[@data-testid='product-price']")).getText();
		String listPrice1 = "$240.00";
		Assert.assertEquals(listPrice1, productPrice, "Price match!");
		Thread.sleep(1000);

		// 5. Add product to cart
		driver.findElement(By.xpath("//button[normalize-space()='Add to cart']")).click();
		//Thread.sleep(1000);

		// 6. Go to cart
		driver.findElement(By.xpath("//a[normalize-space()='Cart (1)']")).click();

		// Set quantity = 2
		//driver.findElement(By.xpath("(//*[name()='svg'][contains(@class,'svg-inline--fa fa-plus fa-sm')])[1]")).click();

		// 7. Validate Total price updates
		
		String productPrice2 = driver.findElement(By.xpath("(//span[contains(@class,'block text-[#030712] !text-[18px] !font-[500] tracking-[-0.33px] store-front-font-family-match-medusa')])[1]")).getText();
		String listPrice2 = "$276.00";
		Assert.assertEquals(listPrice1, productPrice, "Price match!");
		Thread.sleep(1000);
		

		// 8. Go to Checkout page
		driver.findElement(By.xpath("//button[normalize-space()='Go to checkout']")).click();
		Thread.sleep(2000);

		// Validate Subtotal
		double subtotal = Double.parseDouble(driver.findElement(By.xpath("//div[contains(@class,'flex p-3 flex-col flex-nowrap my-6 gap-3 !p-0')]//div[1]//div[2]")).getText().replace("$", "$276.00").trim());
		double expectedSubtotal = 276.00;
		Assert.assertEquals(subtotal, expectedSubtotal, "Subtotal mismatch!");

		// Validate Shipping
		String shipping = driver.findElement(By.xpath("//span[normalize-space()='$0.00']")).getText();
		Assert.assertTrue(shipping.equals("$0.00") || shipping.equals("$0.00") || shipping.contains("$0.00"), "Shipping charges not applied!");

		// Validate Taxes
		double tax = Double.parseDouble(driver.findElement(By.xpath("//span[normalize-space()='$36.00']")).getText().replace("$", "$36.00").trim());
		double expectedTax = 36.00;
		Assert.assertEquals(tax, expectedTax, "Tax match!");

		// Validate Total = Subtotal + Taxes
		double finalTotal = Double.parseDouble(driver.findElement(By.xpath("//div[contains(@class,'flex py-3 flex-row flex-wrap items-center mt-5 p-0')]//div[2]")).getText().replace("$", "").trim());
		Assert.assertEquals(finalTotal, subtotal + tax, "Total amount calculation match!");

	}
	@AfterTest
	public void teardown() {
		{
			driver.quit();
		}
	}
}
