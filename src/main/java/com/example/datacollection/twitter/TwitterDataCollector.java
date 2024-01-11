

package com.example.datacollection.twitter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor; 

import com.example.datacollection.DataCollector;
import com.example.model.Tweet;
//import com.example.datacollection.twitter.Tweet;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TwitterDataCollector implements DataCollector{
	
	private String keyword;
	private String startTime;
	private String endTime;
	
	
    
    public TwitterDataCollector(String keyword, String startTime, String endTime) {
		super();
		this.keyword = keyword;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	@Override
    public List<Tweet> collectData() {
        List<Tweet> tweetList = null;

        Selenium data = new Selenium();
        tweetList = data.getTweets(keyword, startTime, endTime);
         
        return tweetList;
    }

}
