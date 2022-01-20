package DataSci.judicature.service.impl;

import DataSci.judicature.domain.Words;
import DataSci.judicature.service.PythonService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
     * 搜索推荐
     */
    @Override
    public String search(String str) {
        //TODO 这里先把内容写死
        //String path = PATH + "nlp\\searchRecommend.py";
        //D:\java\DataSci\lqf\JudicatureAutoLabel\nlp\searchRecommend.py
        String path = /*"D:\\java\\DataSci\\lqf\\JudicatureAutoLabel\\" + "nlp\\*/"searchRecommend.py";
        String[] args = {"cmd d: && cd D:\\java\\DataSci\\lqf\\JudicatureAutoLabel\\nlp\\ && python", path, str};
        List<String> arr = execWithReturn("python searchRecommend.py " + str, "GBK");
        System.out.println(arr);
        String res = "";
        String s;
        for (int i = 0; i < arr.size(); i++) {
            s = arr.get(i);
            int end = s.indexOf(".");
            int srt = s.lastIndexOf("\\");
            res += s.substring(srt + 1, end);

            if (i != arr.size() - 1)
                res += ",";
        }

        return res;
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


    /**
     * 执行python脚本,并截取控制台获取输出流返回
     *
     * @param args1   参数集 {"python","python脚本的绝对路径","参数1","参数2","参数3..."}
     * @param charset 字符编码 默认为 GBK
     */
    private List<String> execWithReturn(String[] args1, String charset) {
        List<String> arr = new ArrayList<>();
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
                arr.add(line);
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

        return arr;
    }

    private List<String> execWithReturn(String s, String charset) {
        List<String> arr = new ArrayList<>();
        Process proc;
        if (charset == null || charset.length() == 0)
            charset = "GBK";
        try {
            proc = Runtime.getRuntime().exec(s, null, new File("D:\\java\\DataSci\\lqf\\JudicatureAutoLabel\\nlp\\"));
            //proc = Runtime.getRuntime().exec(s);// 执行py文件
            //用输入输出流来截取结果
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream(), charset));
            //获取错误流
            BufferedReader err = new BufferedReader(new InputStreamReader(proc.getErrorStream(), charset));

            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
                arr.add(line);
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

        return arr;
    }


}
