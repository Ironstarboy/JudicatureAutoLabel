package DataSci.judicature.service.impl;

import DataSci.judicature.domain.Words;
import DataSci.judicature.service.PythonService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class PythonServiceImpl implements PythonService {

    @Value("${PATH}")
    private String PATH;


    @Override
    public Words jieba(File file) {
        //todo 分词的实现
        return null;
    }

    /**
     * 执行python脚本
     *
     * @param args1   参数集 {"python","python脚本的绝对路径","参数1","参数2","参数3..."}
     * @param charset 字符编码 默认为GBK
     */
    private void exec(String[] args1, String charset) {
        Process proc;
        if (charset == null || charset.length() == 0)
            charset = "GBK";
        try {
            proc = Runtime.getRuntime().exec(args1);// 执行py文件
            //用输入输出流来截取结果
            BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream(), charset));
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            br.close();
            proc.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("IO异常");
        }
    }

}
