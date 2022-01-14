package DataSci.judicature.service.impl;

import DataSci.judicature.domain.CaseMarksArr;
import DataSci.judicature.service.WordService;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.model.crf.CRFLexicalAnalyzer;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.WordDictionary;
import com.qianxinyao.analysis.jieba.keyword.TFIDFAnalyzer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 针对不同书的关键字提取
 * todo 基本想法：宁滥勿缺 全部搞进来之后再词性清洗
 * 正则 + HanLP
 */
@Service
public class WordServiceImpl implements WordService {

    @Value("${spring.servlet.multipart.location}")
    private String location;

    //jieba分词器
    private final JiebaSegmenter jb = new JiebaSegmenter();

    //jieba词库
    private final WordDictionary wd = WordDictionary.getInstance();

    //tfidf
    private final TFIDFAnalyzer tf = new TFIDFAnalyzer();

    //HanLP分词器
    private final Segment nlp = NLPTokenizer.ANALYZER.enableOrganizationRecognize(true).enablePlaceRecognize(true);

    private final Segment crf = new CRFLexicalAnalyzer().enablePlaceRecognize(true).enableOrganizationRecognize(true);

    public WordServiceImpl() throws IOException {
    }


    @Override
    public CaseMarksArr extract(String fileName, String type) throws IOException {
        CaseMarksArr marks = null;
        if ("adjudication".equals(type)) {
            marks = adjudication(fileName);
        } else if ("decision".equals(type)) {
            marks = decision(fileName);
        } else if ("judgment".equals(type)) {
            marks = judgment(fileName);
        } else if ("mediate".equals(type)) {
            marks = mediate(fileName);
        } else if ("notification".equals(type)) {
            marks = notification(fileName);
        } else if ("order".equals(type)) {
            marks = order(fileName);
        }
        return marks;
    }

    private CaseMarksArr notification(String fileName) {
        return null;
    }

    private CaseMarksArr order(String fileName) {
        return null;
    }

    private CaseMarksArr mediate(String fileName) {
        return null;
    }

    private CaseMarksArr judgment(String fileName) {
        return null;
    }

    private CaseMarksArr adjudication(String fileName) throws IOException {

        //当事人
        String regEx = "^((.*)人（(.*)）：(.*))|((.*)请求人(.*))|((.*)人：(.*))|((.*)被告：(.*))$";
        Pattern pattern = Pattern.compile(regEx);

        //屏蔽词
        String NO_regEx = "^((.*)代理人(.*))|((.*)代表人(.*))|((.*)负责人(.*))|((.*)合伙人(.*))$";
        Pattern NO_pattern = Pattern.compile(NO_regEx);

        //年份
        String year_regEx = "^(.*)\\d\\d\\d\\d年(.*)$";
        Pattern yearPattern = Pattern.compile(year_regEx);

        //重庆市高级人民法院（2019）渝民终795号
        //江苏省高级人民法院（2019）苏民终1487号
        String court_regEx = "^(.*)法院（\\d+）(.{0,3})民终\\d+号(.*)$";
        Pattern courtPattern = Pattern.compile(court_regEx);

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "GBK"));
        CaseMarksArr marks = new CaseMarksArr();
        String line;
        int lineNo = 1;//行号
        while ((line = br.readLine()) != null) {

            //写文书的人能不能走点心啊啊啊
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
                if (pattern.matcher(line).matches() && !NO_pattern.matcher(line).matches()) {
                    //匹配了 "\\^(.*)人（(.*)）：(.*)$" 而且不是屏蔽词
                    String[] words = line.split("：");
                    words[0] = "";
                    String realInfo = StringUtils.join(words, "");
                    realInfo = realInfo.replaceAll("住所地", "");
                    //白福贵，男，1962年10月21日出生，汉族，住黑龙江省北安市。
                    //济南荣耀房地产开发有限公司，住所地山东省济南市历城区郭店三区38号209室。

                    words = realInfo.split("[，。：,]");

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
                            } else if (s.contains("族") && s.length() <= 5) {
                                marks.setEthnicity(s);//民族
                            } else if (!yearPattern.matcher(s).matches() || s.startsWith("住")) {
                                if (s.startsWith("住"))
                                    s = s.substring(1);
                                marks.setBirthplace(s);//住址，这里可能混入其他东西
                            }//唯一剩下的就是出生日期了
                        }

                    }
                } else {//接下来一行就是案由+法院
                    //重庆市高级人民法院（2019）渝民终795号
                    //todo 识别案由  xxx一案 这里可能直接把所有案由一一拿来比对，效果更好一些
                    if (courtPattern.matcher(line).matches()) {
                        String[] sentence = line.split("[，。：,]");//切成句子
                        for (String s : sentence) {
                            if (s.matches(court_regEx)) {
                                //todo 这你妈怎么把前面分离
                                s = s.split("（\\d+）(.*)民终\\d+号")[0];
                                s = s.substring(2);//todo 这里过拟合
                                marks.setCourts(s);
                            }
                        }
                    }
                }
            }
            lineNo++;
        }
        return marks;
    }

    private CaseMarksArr decision(String fileName) throws IOException {

        //当事人
        String regEx = "^((.*)人（(.*)）：(.*))|((.*)人(.*))|((.*)罪犯(.*))|((.*)机关(.*))|((.*)被告(.*))$";
        Pattern pattern = Pattern.compile(regEx);

        //屏蔽词
        String NO_regEx = "^((.*)代理人(.*))|((.*)代表人(.*))|((.*)负责人(.*))|((.*)合伙人(.*))$";
        Pattern NO_pattern = Pattern.compile(NO_regEx);

        //年份
        String year_regEx = "^(.*)(一|二|〇|三|四|五|六|七|八|九|十|(\\d\\d\\d\\d))年(.*)$";
        Pattern yearPattern = Pattern.compile(year_regEx);

        //法院 检察院
        String court_regEx = "^(.*)(法院|检察院)(.*)$";
        Pattern courtPattern = Pattern.compile(court_regEx);

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "GBK"));
        CaseMarksArr marks = new CaseMarksArr();
        String line;
        int lineNo = 1;//行号
        while ((line = br.readLine()) != null) {

            //写文书的人能不能走点心啊啊啊
            line = line.replaceAll(":", "：");
            line = line.replaceAll("\\(", "（");
            line = line.replaceAll("\\)", "）");
            line = line.replaceAll("[\\s\\p{Zs}]", "");

            if (line.length() == 0)
                continue;

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
                //重庆市渝中区人民检察院指控被告人王风、于思佳犯诈骗罪、非法拘禁罪一案，本院经审查，依照《中华人民共和国刑事诉讼法》第二十七条的规定，决定如下：
                if (courtPattern.matcher(line).matches()) {//法院

                    ArrayList<String> info = new ArrayList<>();
                    //先把一行字切成几句话来处理
                    String[] words = line.split("[，。：,\\s]");

                    for (String word : words) {
                        if (!StringUtils.isEmpty(word)) {
                            info.add(word);
                        }
                    }
                    for (String s : info) {
                        List<Term> nlpSeg = nlp.seg(s);
                        List<Term> crfSeg = crf.seg(s);

                        for (Term term : nlpSeg) {
                            if (term.nature == Nature.nt && term.word.matches(court_regEx))
                                marks.setCourts(term.word);
                        }
                        for (Term term : crfSeg) {
                            if (term.nature == Nature.nt && term.word.matches(court_regEx))
                                marks.setCourts(term.word);
                        }


                    }

                }
                //提取人名，地名，性别，民族
                if (pattern.matcher(line).matches() && !NO_pattern.matcher(line).matches() && (line.length() < 200 || lineNo == 4)) {
/*

                    String[] sentence = line.split("[，。：,]");//切成句子
                    for (String s : sentence) {
                        s = StringUtils.trimToEmpty(s);


                        String ss = s;
                        while (ss.matches("(.*)被告人(.*)犯(.*)")) {
                            //由于有多个 循环来
                            //todo 可能过拟合
                            int srt = ss.indexOf("被告人");
                            ss = ss.substring(srt + 3);
                            int end = ss.indexOf("犯");
                            marks.setCriminals(ss.substring(0, end));
                            ss = ss.substring(end + 1);
                        }
                    }
*/

                    ArrayList<String> info = new ArrayList<>();

                    //TODO 这里到底要怎么切
                    //先把一行字切成几句话来处理
                    String[] words = line.split("[，。：,\\s]");

                    for (String word : words) {
                        if (!StringUtils.isEmpty(word)) {
                            info.add(word);
                        }
                    }
                    String s;
                    for (int i = 0; i < info.size(); i++) {
                        List<Term> nlpSeg = nlp.seg(info.get(i));
                        List<Term> crfSeg = crf.seg(info.get(i));


                        for (Term term : nlpSeg) {
                            if (isName(term)) {//名字
                                if (term.word.endsWith("犯"))
                                    term.word = term.word.replaceAll("犯", "");
                                marks.setCriminals(term.word);
                            }
                            if (term.nature == Nature.ns)//住址,用NLP的更准确
                                marks.setBirthplace(term.word);
                        }
                        for (Term term : crfSeg) {
                            if (isName(term)) {//名字
                                if (term.word.endsWith("犯"))
                                    term.word = term.word.replaceAll("犯", "");
                                marks.setCriminals(term.word);
                            }
                        }
                        s = info.get(i);
                        if ("男".equals(s) || "女".equals(s)) {
                            marks.setGender(s);//性别
                        } else if (s.contains("族") && s.length() <= 5) {
                            marks.setEthnicity(s);//民族
                        }
                    }
                }
            }
            lineNo++;
        }
        return marks;
    }


    private boolean isName(Term term) {
        return term.nature == Nature.nr || term.nature == Nature.nrf ||
                term.nature == Nature.ntu || term.nature == Nature.nts || term.nature == Nature.nto ||
                term.nature == Nature.nth || term.nature == Nature.ntch || term.nature == Nature.ntcf ||
                term.nature == Nature.nit || term.nature == Nature.nic || term.nature == Nature.ni ||
                term.nature == Nature.ntcb || term.nature == Nature.ntc || term.nature == Nature.nt;
    }
}
