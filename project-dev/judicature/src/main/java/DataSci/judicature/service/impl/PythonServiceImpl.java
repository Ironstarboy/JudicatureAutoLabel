package DataSci.judicature.service.impl;

import DataSci.judicature.service.PythonService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class PythonServiceImpl implements PythonService {

    @Value("${PATH}")
    private String PATH;


    /**
     * 相似案例推荐
     * @param filePath
     * @throws Exception
     */
    @Override
    public String recommend(String filePath) throws Exception {
        Process proc;
        BufferedWriter br;
        BufferedReader in;

        proc = Runtime.getRuntime().exec("python getCaseRecommendation.py", null, new File("D:\\java\\DataSci\\lqf\\JudicatureAutoLabel\\nlp\\"));
        br = new BufferedWriter(new OutputStreamWriter(proc.getOutputStream(), "GBK"));
        in = new BufferedReader(new InputStreamReader(proc.getInputStream(), "GBK"));

        br.write(filePath);
        br.flush();

        br.close();
        in.close();
        proc.waitFor();
        proc.destroy();

        String[] split = in.readLine().split("---");
        String[] nameList = cleanName(split[0]);
        String[] rateList = cleanRate(split[1]);

        String[] answerList = new String[nameList.length];

        for (int i = 0; i < nameList.length; i++) {
            answerList[i] = nameList[i] +" "+ rateList[i];
        }

        String res= StringUtils.join(answerList,",");
        return res;

        //todo 把拿到的推荐用实体类封装后返回
    }

    @Override
    public String sentence(String filePath) throws Exception {
       /* Process proc;
        BufferedWriter br;
        BufferedReader in;

        proc = Runtime.getRuntime().exec("python getCaseRecommendation.py", null, new File("D:\\java\\DataSci\\lqf\\JudicatureAutoLabel\\nlp\\"));
        br = new BufferedWriter(new OutputStreamWriter(proc.getOutputStream(), "GBK"));
        in = new BufferedReader(new InputStreamReader(proc.getInputStream(), "GBK"));

        br.write(filePath);
        br.flush();

        in.readLine();
*/




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

    private String[] cleanName(String str) {
        //['中欧汽车电器有限公司吴国琳等合伙协议纠纷股权转让纠纷其他民事民事裁定书.txt', '山东宁津农村商业银行股份有限公司吴国强等金融借款合同纠纷民事再审民事调解书.txt', '孙家凤孟晓培等股权转让纠纷民事申请再审审查民事裁定书.txt', '常州通灵信息科技有限公司福启文化北京有限责任公司等计算机软件开发合同纠纷民事二审民事判决书.txt', '苏州安靠电源有限公司与江西昌河汽车有限责任公司买卖合同纠纷再审审查民事裁定书.txt', '涧县华阳鸿基置地有限责任公司湖南鹏华装饰设计工程有限责任公司等建设工程施工合同纠纷其他民事民事裁定.txt']
        str = str.substring(1, str.length() - 1);
        String[] res = str.split(",");
        String[] ans = new String[res.length - 1];
        for (int i = 1; i < res.length; i++) {
            int srt = 0;
            int end = res[i].indexOf('.');
            ans[i - 1] = res[i].substring(srt + 1, end);
        }
        return ans;
    }

    private String[] cleanRate(String str) {
        //[0.8602495, 0.11796849, 0.108259305, 0.101992205, 0.07128522, 0.07090668]
        str = str.substring(1, str.length() - 1);
        String[] res = str.split(",");
        String[] ans = new String[res.length - 1];
        for (int i = 1; i < res.length; i++) {
            String d = res[i];
            double rate = Double.parseDouble(d);
            rate *= 100;//十倍根号法调整相似度值
            rate = Math.round(10 * Math.sqrt(rate));
            ans[i - 1] = (int)rate + "%";
        }
        return ans;
    }


}
