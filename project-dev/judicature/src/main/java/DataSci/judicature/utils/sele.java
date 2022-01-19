package DataSci.judicature.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;


public class sele {
    public static void main(String[] args) throws InterruptedException {
        WebDriver driver = new ChromeDriver();
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

            Thread.sleep(3000);

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
                Thread.sleep(1000);
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
        driver.close();
    }
}
