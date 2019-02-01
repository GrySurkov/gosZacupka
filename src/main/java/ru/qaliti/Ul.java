package ru.qaliti;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;

public class Ul {
    private WebElement ulElement;
    private WebDriver driver;

    public Ul(WebElement ulElement, WebDriver driver){
        this.ulElement = ulElement;
        this.driver = driver;
    }

    public List<WebElement> getLi(){
        List<WebElement> li = ulElement.findElements(By.xpath(".//li"));
        return li;
    }


}
