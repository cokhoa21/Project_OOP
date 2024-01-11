package com.example.datacollection.nftexchanges;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.example.datacollection.DataCollector;
import com.example.model.BinanceItem;

public class BinanceDataCollection implements DataCollector<BinanceItem> {

    @Override
    public List<BinanceItem> collectData() {
        List<BinanceItem> binanceItems = new ArrayList<>();

        // Set the path to your chromedriver executable
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver-win64/chromedriver.exe");

        // Initialize the ChromeDriver
        WebDriver driver = new ChromeDriver();

        try {
            // Navigate to the URL
            driver.get("https://www.binance.com/en/markets/overview");

            // Assuming there are multiple items with the same structure, find all of them
            List<WebElement> itemElements = driver.findElements(By.className("css-1ydqfmf"));

            // Loop through each item element and extract data
            for (WebElement itemElement : itemElements) {
                String name = itemElement.findElement(By.className("text-t-third")).getText();
                
                // Assume acronym is the content of subtitle3 class
                String acronym = itemElement.findElement(By.className("subtitle3")).getText();
                
                String price = itemElement.findElement(By.cssSelector("[data-area='right']")).getText();

                // Correct the selector for the volume
                WebElement volumeElement = itemElement.findElement(By.xpath(".//div[@data-area='right'][3]"));
                String volume = volumeElement.getText();

                System.out.println("name: " + name);
                System.out.println("acronym: " + acronym);
                System.out.println("price: " + price);
                System.out.println("volume: " + volume);

                BinanceItem binanceItem = new BinanceItem(name, acronym, price, volume);
                binanceItems.add(binanceItem);
            }
        } finally {
            // Close the WebDriver
            driver.quit();
        }

        return binanceItems;
    }
}
