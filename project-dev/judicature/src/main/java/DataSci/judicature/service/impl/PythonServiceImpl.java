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
    public void recommend(String filePath) {
        String path = PATH + "nlp\\caseRecommend.py";
        String[] args = {"python", path, filePath};
        exec(args, "GBK");

        //todo 把拿到的推荐用实体类封装后返回
    }

    @Override
    public String sentence(String filePath) {

        return null;
        //todo 也没实现
    }

    /**
     * 执行python脚本
     *
     * @param args1   参数集 {"python","python脚本的绝对路径","参数1","参数2","参数3..."}
     * @param charset 字符编码 默认为 GBK
     */
    private void exec(String[] args1, String charset) {
        Process proc;
        if (charset == null || charset.length() == 0)
            charset = "GBK";
        try {
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
            System.out.println("IO异常");
        }
    }

}
