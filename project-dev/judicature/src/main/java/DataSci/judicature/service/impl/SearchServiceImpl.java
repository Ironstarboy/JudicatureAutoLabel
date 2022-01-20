package DataSci.judicature.service.impl;

import DataSci.judicature.service.SearchService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {


    @Value("${PATH}")
    private String PATH;

    private BufferedWriter br;

    private BufferedReader in;

    private Process proc;

    public SearchServiceImpl() {
        init();
    }

    /**
     * 每次ajax都调用这个方法
     */
    @Override
    public String search(String str) {
        if (proc == null){
            init();
        }
        String msg;
        try {
            br.write(str+System.lineSeparator());
            br.flush();

             msg = in.readLine();
            System.out.println(msg);
        }catch (Exception e){
            e.printStackTrace();
            try {
                br.close();
                in.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            System.out.println("IO异常");
            proc.destroy();
            proc = null;
            return "";
        }
        return clear(msg);
    }

    /**
     * 初始化进程 一直不关 先不优化了
     */
    private void init(){
        try{
             proc = Runtime.getRuntime().exec("python searchRecommend.py", null, new File("D:\\java\\DataSci\\lqf\\JudicatureAutoLabel\\nlp\\"));
             br = new BufferedWriter(new OutputStreamWriter(proc.getOutputStream(), "GBK"));
             in = new BufferedReader(new InputStreamReader(proc.getInputStream(), "GBK"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //清洗数据
    public String clear(String str){
        String res = "";
        //['segfile\\judgment\\李某与杨某离婚纠纷一审民事判决书.txt', 'segfile\\adjudication\\中欧汽车电器有限公司吴国琳等合伙协议纠纷股权转让纠纷其他民事民事裁定书.txt', 'segfile\\decision\\赵鹏飞合同纠纷执行决定书.txt', 'segfile\\mediate\\李某、张某离婚纠纷民事一审民事调解书(FBM-CLI-C-407872141).txt', 'segfile\\decision\\刘帅新租赁合同纠纷执行决定书.txt']
        String s = str.substring(1, str.length() - 1);//去掉[]
        String ss;
        String[] strs = s.split(",");
        for (int i=0;i<strs.length;i++) {
            ss = strs[i];
            int srt = ss.lastIndexOf('\\');
            int end = ss.indexOf(".");
            strs[i] = ss.substring(srt+1,end);
        }
        res = StringUtils.join(strs,",");
        return res;
    }
}
