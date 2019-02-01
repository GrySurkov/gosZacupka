package ru.qaliti;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class Result {
    private WebElement resultElement;
    private static WebDriver driver;

    public  Result(WebElement resultElement, WebDriver driver){
        this.resultElement = resultElement;
        this.driver = driver;
    }


    public WebElement getLink(){
            WebElement link = resultElement.findElement(By.xpath("./table/tbody/tr/td[2]/dl/dt/a"));

        return link;
    }

    public WebElement getNamePurchase(){
        WebElement namePurchase = driver.findElement(By.xpath("/html/body/div[3]/div/div/div[2]/div/div/div[2" +
                                                                          "]/div[2]/div[1]/table/tbody/tr[3]/td[2]/span"));
        return namePurchase;

    }

    public String getPrice(){
        String price;
        if(!(resultElement.findElements(By.xpath("./table/tbody/tr/td[1]/dl/dd[2]/strong")).isEmpty())) {
            price = resultElement.findElement(By.xpath("./table/tbody/tr/td[1]/dl/dd[2]/strong")).getText();
        } else {
            long lPrice = 0;
            resultElement.findElement(By.xpath("//p[contains(text(),\"Информация о лотах\")]")).click();
            List<WebElement> listPrise = resultElement.findElements(By.xpath("./table/tbody/tr[2]/td[2]/div/table" +
                                                                                     "/tbody//i/strong"));
            for(int i = 0; i < listPrise.size(); i++){
                String sPrice = listPrise.get(i).getText();
                sPrice = sPrice.replace(" ","");
                sPrice = sPrice.replace(",","");
                lPrice = lPrice + (Long.parseLong(sPrice));
            }
            price = "" + lPrice;
        }
        return price;
    }

    public WebElement getEndData(){
        WebElement endData;
        if(!(driver.findElements(By.xpath("/html/body/div[3]/div/div/div[2]/div/div/div[2]/div[2]/div[6]/table/tbody" +
                                                 "/tr" +
                                               "[3]/td[2]/span")).isEmpty())) {
            endData = driver.findElement(By.xpath("/html/body/div[3]/div/div/div[2]/div/div/div[2]/div[2]/div[6]/" +
                                                          "table/tbody/tr[3]/td[2]/span"));
        } else {
            endData = driver.findElement(By.xpath("/html/body/div[3]/div/div/div[2]/div/div/div[2]/div[2]/div[7" +
                                                          "]/table/tbody/tr[3]/td[2]/span"));

        }
            return endData;
    }

    public WebElement getSertificat() {
        WebElement sertificat = resultElement.findElement(By.xpath("./div[1]/a[1]"));

        return sertificat;
    }

    public WebElement getSignature(){
        driver.findElement(By.xpath("/html/body/div[1]/div/div[3]/table/tbody/tr[1]/td[1]/span")).click();

        WebElement signature = driver.findElement(By.xpath("/html/body/div[1]/div/div[3]/table/tbody/tr[2]/td/div"));
        return signature;
    }

    public WebElement getPerson(){
        WebElement person =driver.findElement(By.className("elSignName"));
        return person;


    }


}
