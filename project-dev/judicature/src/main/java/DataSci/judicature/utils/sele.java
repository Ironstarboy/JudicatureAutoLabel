package DataSci.judicature.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;


public class sele {
    public static void main(String[] args) throws InterruptedException {
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.pkulaw.com/case/");
        Thread.sleep(2000);
        driver.findElement(By.id("CaseGradeport_40_a")).click();//选择普通文书案例
        Thread.sleep(2000);
        driver.findElement(By.id("choose-all")).click();//全选

        driver.findElement(By.className("down-all")).click();//下载
        Thread.sleep(500);
        driver.findElement(By.id("tool-txt")).click();//下载为txt

        Thread.sleep(100);
        driver.findElement(By.id("batchDownload")).click();//确定



        String title = driver.getTitle();
        System.out.println(title);


        Thread.sleep(10000);
       // driver.close();


    }
}
