package DataSci.judicature.service.impl;

import DataSci.judicature.service.SpyderService;
import DataSci.judicature.utils.FileUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
public class SpyderServiceImpl implements SpyderService {

    @Value("${spring.servlet.multipart.location}")
    private String location;

    @Autowired
    private FileUtil fileUtil;

    /**
     * @return 返回文件名集合
     * zip\\tag(时间戳)\\download 爬下来的路径
     * zip\\tag(时间戳)\\txt      解压的文件路径
     * zip\\tag(时间戳)\\         打的压缩包路径
     */
    @Override
    public String spyder(String srt, String end, int max, String tag) throws Exception {
        max = Math.min(max, 100);

        String s = timeTrans(srt);
        String et = timeTrans(end);


        //TODO 做测试
       /* String downloadPath = location + "zip\\" + tag + "\\download\\";
        File f = new File(downloadPath);
        f.getParentFile().mkdir();
        f.mkdir();*/
        String downloadPath = location + "zip\\";

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("download.default_directory", downloadPath);

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("prefs", hashMap);

        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);

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

            driver.findElement(By.id("LastInstanceDate1")).sendKeys(s);//开始日期
            driver.findElement(By.id("LastInstanceDate2")).sendKeys(et);//结束日期

            Thread.sleep(800);

            driver.findElement(By.id("advsearchbtn")).click();//点击搜索

            Thread.sleep(3000);

            try {
                driver.findElement(By.partialLinkText("普通案例")).click();
            } catch (Exception e) {
                Thread.sleep(5000);
                try {
                    driver.findElement(By.partialLinkText("普通案例")).click();
                } catch (Exception exception) {
                    Thread.sleep(15000);
                    driver.findElement(By.partialLinkText("普通案例")).click();
                }
            }


            // Thread.sleep(20000);
            int i = 0;
            while (i < max) {
                i += 10;
                Thread.sleep(5000);
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

        Thread.sleep(5000);
        //driver.close();

        String zipPath = location + "zip\\" + tag + "\\爬虫.zip";
        zip(downloadPath, location + "zip\\" + tag + "\\txt\\",zipPath);

        return zipPath;
    }

    //todo 时间转换
    public String timeTrans(String time) {
        String newTime = time.replaceAll("-",".");
        return newTime;
    }

    //解压和打包
    public void zip(String xiazaipath,String txtpath,String uploadpath) throws Exception {
        File dir = new File(xiazaipath);
        for (File file : Objects.requireNonNull(dir.listFiles())) {//解压
            fileUtil.zipUncompress(file.getAbsolutePath(),txtpath);
        }

        fileUtil.ZipCompress(txtpath,uploadpath);
    }
}
