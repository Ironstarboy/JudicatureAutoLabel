package DataSci.judicature.service.impl;

import DataSci.judicature.domain.CaseMarksArr;
import DataSci.judicature.service.WordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 针对不同书的关键字提取
 */
@Service
public class WordServiceImpl implements WordService {

    @Value("${spring.servlet.multipart.location}")
    private String location;


    @Override
    public CaseMarksArr extract(String fileName, String type) throws IOException {
        CaseMarksArr marks = null;
        if ("adjudication".equals(type)) {
            marks = adjudication(fileName);
        }
        return marks;
    }

    private CaseMarksArr adjudication(String fileName) throws IOException {

        String regEx = "^((.*)人（(.*)）：(.*))|((.*)人：(.*))$";
        Pattern pattern = Pattern.compile(regEx);

        //屏蔽词
        String NO_regEx = "^((.*)代理人(.*))|((.*)代表人(.*))|((.*)负责人(.*))$";
        Pattern NO_pattern = Pattern.compile(NO_regEx);

        String year_regEx = "^(.*)\\d\\d\\d\\d年(.*)$";
        Pattern yearPattern = Pattern.compile(year_regEx);

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "GBK"));
        CaseMarksArr marks = new CaseMarksArr();
        String line;
        int lineNo = 1;//行号
        while ((line = br.readLine()) != null) {

            line = line.replaceAll(":", "：");
            line = line.replaceAll("\\(", "（");
            line = line.replaceAll("\\)", "）");

            if (lineNo == 1) {
                //中华人民共和国最高人民法院
                String[] s = line.split(" ");
                line = StringUtils.join(s, "");
                if (line.contains("法院")) {
                    marks.setCourts(line);
                }
            } else if (lineNo == 2 || lineNo == 3) {
                //民 事 裁 定 书
                //（2021）最高法民申5039号
            } else {
                if (pattern.matcher(line).matches() && !NO_pattern.matcher(line).matches()) {//匹配了 "\\^(.*)人（(.*)）：(.*)$"
                    String[] words = line.split("：");
                    words[0] = "";
                    String realInfo = StringUtils.join(words, "");
                    realInfo = realInfo.replaceAll("住所地", "");
                    //白福贵，男，1962年10月21日出生，汉族，住黑龙江省北安市。
                    //济南荣耀房地产开发有限公司，住所地山东省济南市历城区郭店三区38号209室。

                    words = realInfo.split("，|。|：|,");

                    ArrayList<String> info = new ArrayList<>();

                    for (String word : words) {
                        if (!StringUtils.isEmpty(word)) {
                            info.add(word);
                        }
                    }
                    String s;
                    for (int i = 0; i < info.size(); i++) {
                        if (i == 0)
                            marks.setCriminals(info.get(0));//人名
                        else {
                            s = info.get(i);
                            if ("男".equals(s) || "女".equals(s)) {
                                marks.setGender(s);//性别
                            } else if (s.contains("族")) {
                                marks.setEthnicity(s);//民族
                            } else if (!yearPattern.matcher(s).matches() || s.startsWith("住")) {
                                if (s.startsWith("住"))
                                    s = s.substring(1);
                                marks.setBirthplace(s);//住址
                            }//唯一剩下的就是出生日期了
                        }

                    }
                }
                else {
                    //todo 识别案由  xxx一案
                }
            }
            lineNo++;
        }
        return marks;
    }
}
