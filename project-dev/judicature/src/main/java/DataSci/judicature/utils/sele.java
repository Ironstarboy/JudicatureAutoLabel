package DataSci.judicature.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.HashMap;


public class sele {
    public static void main(String[] args) throws InterruptedException {
// 2.新的下载地址为桌面（可以弄成某个文件夹路径而不要直接弄成死的静态路径）
        String downloadPath = "D:\\java\\DataSci\\lqf\\JudicatureAutoLabel\\project-dev\\judicature\\src\\main\\resources\\case\\zip\\";

// 3.HashMap 中保存下载地址信息
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("download.default_directory", downloadPath);

// 4.ChromeOptions 中设置下载路径信息，需要传入保存有下载路径的 HashMap
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("prefs", hashMap);

// 依据 ChromeOptions 来产生 DesiredCapbilities，这时 DesiredCapbilities 就也具备了下载路径的信息了
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);

// 5.依据 ChromeOptions 产生驱动，此时的 driver 已经具备了新的下载路径的
        WebDriver driver = new ChromeDriver(desiredCapabilities);


        try {
            driver.get("https://www.pkulaw.com/case/");
            Thread.sleep(2000);

            try {
                driver.findElement(By.id("newloginbtn")).click();
            } catch (Exception ignored) {
            }

            driver.get("https://www.pkulaw.com/case/adv");//查询界面
            Thread.sleep(2000);

            driver.findElement(By.id("LastInstanceDate1")).sendKeys("2014.01.10");//开始日期
            driver.findElement(By.id("LastInstanceDate2")).sendKeys("2022.01.10");//结束日期

            Thread.sleep(800);

            driver.findElement(By.id("advsearchbtn")).click();//点击搜索

            Thread.sleep(5000);

            driver.findElement(By.partialLinkText("普通案例")).click();
            //driver.findElement(By.cssSelector("form#form > span > input")
            //driver.findElement(By.cssSelector("div#recordPage > dl > dd[filter_value='100']")).click();


            String url = "https://www.pkulaw.com/case/search/RecordSearch";


            // Thread.sleep(20000);
            int i = 0;
            while (i < 100) {
                i += 10;
                Thread.sleep(4000);
                try {
                    driver.findElement(By.id("choose-all")).click();//全选
                } catch (Exception e) {
                    Thread.sleep(2000);
                    driver.findElement(By.id("choose-all")).click();//全选
                }
                Thread.sleep(2000);
                try {
                    driver.findElement(By.className("down-all")).click();//下载
                } catch (Exception e) {
                    Thread.sleep(2000);
                    driver.findElement(By.className("down-all")).click();//下载
                }
                Thread.sleep(2000);
                try {
                    driver.findElement(By.id("tool-txt")).click();//下载为txt
                } catch (Exception e) {
                    Thread.sleep(2000);
                    try {
                        driver.findElement(By.id("tool-txt")).click();
                    } catch (Exception exception) {
                        Thread.sleep(2000);
                        driver.findElement(By.id("tool-txt")).click();
                    }
                }
                Thread.sleep(1000);
                try {
                    driver.findElement(By.id("batchDownload")).click();//确定
                } catch (Exception e) {
                    Thread.sleep(2000);
                    driver.findElement(By.id("batchDownload")).click();
                }
                Thread.sleep(800);
                try {
                    driver.findElement(By.linkText("下一页")).click();
                } catch (Exception e) {
                    Thread.sleep(3000);
                    driver.findElement(By.linkText("下一页")).click();
                }
            }
        } catch (InterruptedException e) {
            driver.close();
        }
        Thread.sleep(3000);
        driver.close();
    }
}
