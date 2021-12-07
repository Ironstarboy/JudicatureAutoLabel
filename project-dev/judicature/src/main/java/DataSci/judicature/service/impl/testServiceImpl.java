package DataSci.judicature.service.impl;

import DataSci.judicature.service.testService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class testServiceImpl implements testService {
    @Override
    public void testPy(String path) {
        String[] args1 = new String[]{"python", path, String.valueOf(5)};
        exec(args1, "GBK");
    }

    private void exec(String[] args1, String charset) {
        Process proc;
        if (charset == null || charset.length() == 0)
            charset = "utf8";
        try {
            //String[] args1 = new String[]{"python", "D:\\python\\pycharm\\PycharmProjects\\machineLearning_k\\python\\k\\variousK.py",
            //        String.valueOf(5)};
            proc = Runtime.getRuntime().exec(args1);// 执行py文件
            //用输入输出流来截取结果
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream(), charset));
            //获取错误流
            BufferedReader err = new BufferedReader(new InputStreamReader(proc.getErrorStream(), charset));

            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            String error;
            while ((error = err.readLine()) != null) {
                System.err.println(error);
            }


            in.close();
            err.close();
            proc.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
