package DataSci.judicature;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@SpringBootTest
class JudicatureApplicationTests {

    @Test
    void contextLoads() {
        System.out.println("测试");
    }

    @Test
    void execPy() {
        Process proc;
        try {
            proc = Runtime.getRuntime().exec("python D:\\java\\DataSci\\lqf\\JudicatureAutoLabel\\spyder\\testbaidu.py");// 执行py文件
            //用输入输出流来截取结果
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            proc.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void execKNN() {
        Process proc;
        try {
            String[] args1 = new String[]{"python", "D:\\python\\pycharm\\PycharmProjects\\machineLearning_k\\python\\k\\variousK.py",
                    String.valueOf(5)};
            proc = Runtime.getRuntime().exec(args1);// 执行py文件
            //用输入输出流来截取结果
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream(), "GBK"));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            proc.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
