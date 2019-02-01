package ru.qaliti;


import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileWriter;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Zakupki {
    static WebDriver driver;
    static FileWriter writer;
    static String baseUrl = "http://www.zakupki.gov.ru";
    static String mainSearch = "Поставка";
    static String paramSposobOpred = "Аукцион";
    static String paramEtapZakupki = "подача заявок";
    static String doownPrice = "1000000";
    static String upPrice = "3000000";
    static String currency = "1"; // 1 = rub, 2 = eur, 3=usd ...


    public static void main(String args[]){
        try {
            writer = new FileWriter("result.txt",false);
        } catch (Exception ex){
            System.out.println("Файл не найден");
        }
        long summ = 0;
        System.setProperty("webdriver.gecko.driver", "D:\\JavaProject\\geckodriver.exe");
        driver = new FirefoxDriver();
        driver.get(baseUrl);
        WebDriverWait wait = new WebDriverWait(driver, 50);
        WebDriver.Timeouts timeouts = driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.findElement(By.linkText("Закупки")).click(); ///

        driver.findElement(By.linkText("Расширенный поиск")).click();
        wait.until(ExpectedConditions.visibilityOfAllElements(driver.findElements(By.xpath("//*[@id=\"quickSearchForm_" +
                "header\"]/div[2]/div/div[2]"))));

// input data

        driver.findElement(By.xpath("//*[@id=\"placingWaysTag\"]/div/div[1]/span[2]")).click();
        WebElement sposobOpredelenia = driver.findElement(By.id("placingWayList"));
        wait.until(ExpectedConditions.visibilityOf(sposobOpredelenia));
        Ul ul = new Ul(sposobOpredelenia,driver);
        List<WebElement> li= ul.getLi();
        for(int i = 0; i<li.size(); i++){
            String st = li.get(i).getText();
            if(st.toLowerCase().contains(paramSposobOpred.toLowerCase())){
                li.get(i).findElement(By.xpath("./label")).click();
            }
        }
        driver.findElement(By.id("placingWaysSelectBtn")).click();


        driver.findElement(By.xpath("//*[@id=\"orderStages\"]/div/div[1]")).click();
        WebElement etapZakupki = driver.findElement(By.xpath("//*[@id=\"orderStages\"]/div/div[2]/div[1]/ul"));
        ul = new Ul(etapZakupki, driver);
        li = ul.getLi();
        for(int i = 0; i<li.size(); i++){
               String st = li.get(i).getText();
            if(!(st.toLowerCase().contains(paramEtapZakupki.toLowerCase()))){
                li.get(i).findElement(By.xpath("./label")).click();
            }
        }

        driver.findElement(By.id("priceFromGeneral")).sendKeys(doownPrice);
        driver.findElement(By.id("priceToGeneral")).sendKeys(upPrice);
        driver.findElement(By.id("currencyChangecurrencyIdGeneral")).click();
        driver.findElement(By.id(currency)).click();

        driver.findElement(By.xpath("//*[@id=\"orderStagesSelectBtn\"]/span")).click();

        driver.findElement(By.id("searchString")).sendKeys(mainSearch);
        driver.findElement(By.xpath("//*[@id=\"quickSearchForm_header\"]/div[2]/div/div[3]/div[1]/span")).click();
        String mainWindow =driver.getWindowHandle();
        wait.until(ExpectedConditions.visibilityOfAllElements(driver.findElements(By.xpath("/html/body/div[2]"))));


//getting result
        String pages = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[1]/p/strong")).getText();
        int iPages = Integer.parseInt(pages);
        iPages = iPages/10;
        System.out.println(iPages);

        for(int j = 1; j<(iPages+1); j++) {
            List<WebElement> results = driver.findElements(By.className("registerBox"));

            for (int i = 0; i < results.size(); i++) {
                String strResult;
                Result result = new Result(results.get(i), driver);
                String price = result.getPrice();
                strResult = (result.getLink().getText() + "  ");
                System.out.print(result.getLink().getText() + "  ");
                result.getLink().click();
                for (String windowHandel : driver.getWindowHandles()) {
                    driver.switchTo().window(windowHandel);
                }
                // открываем общую информацию
                strResult = (strResult + result.getNamePurchase().getText() + "\r\n");
                System.out.println(result.getNamePurchase().getText());
                strResult = (strResult + result.getEndData().getText() + "      ");
                System.out.print(result.getEndData().getText() + "      ");
                strResult = (strResult + "Цена лота: " + price + "\r\n");
                System.out.println("Цена лота: " + price);
                price = price.replace(" ", "");
                price = price.replace(",", "");
                summ = summ + (Long.parseLong(price));
                // закрываем общую информацию
                driver.close();
                driver.switchTo().window(mainWindow);
                String sertificatLinck = result.getSertificat().getAttribute("href");
                driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "t");
                // страница с подписью
                for (String windowHandel : driver.getWindowHandles()) {
                    driver.switchTo().window(windowHandel);
                }
                driver.get(sertificatLinck);
                strResult = (strResult + result.getPerson().getText() + "\r\n \r\n");
                System.out.println(result.getPerson().getText());
                strResult = (strResult + result.getSignature().getText() + "\r\n \r\n");
                System.out.println(result.getSignature().getText());

                driver.close();
                driver.switchTo().window(mainWindow);
                strResult = (strResult + "________________________________\r\n \r\n");
                System.out.println("________________________________");
                System.out.println();
                try {
                    writer.append(strResult);
                } catch (Exception ex) {
                    System.out.println("Запись в файл не удалась");
                }
            }
            driver.findElement(By.xpath("//a[@data-pagenumber=\""+ (j+1) +"\"]")).click();
            wait.until(ExpectedConditions.visibilityOfAllElements(driver.findElements(By.xpath("/html/body/div[2]"))));
        }
//        String price = "" +summ;
//        char[] rub = new char[price.length()-2];
//        price.getChars(0,price.length()-2,rub,0);
//        char[] kop = new char[2];
//        price.getChars(price.length()-2, price.length(), kop, 0);
//        System.out.print("ИТОГО: ");
//        for(int i = 0; i < rub.length; i++) System.out.print(rub[i]);
//        System.out.println("," + kop[0] + kop[1] );

    }




}
