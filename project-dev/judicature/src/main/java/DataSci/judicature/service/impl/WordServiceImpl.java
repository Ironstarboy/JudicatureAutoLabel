package DataSci.judicature.service.impl;

import DataSci.judicature.domain.CaseMarksArr;
import DataSci.judicature.service.WordService;
import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.hankcs.hanlp.model.crf.CRFLexicalAnalyzer;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
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

    //HanLP分词器
    private final Segment nlp = NLPTokenizer.ANALYZER.enableOrganizationRecognize(true).enablePlaceRecognize(true).enableCustomDictionary(true).enableCustomDictionaryForcing(true);

    private final Segment crf = new CRFLexicalAnalyzer().enablePlaceRecognize(true).enableOrganizationRecognize(true).enableCustomDictionary(true).enableCustomDictionaryForcing(true);

    public WordServiceImpl() throws IOException {
        init();
    }

    //初始化nlp分词器
    //TODO 这里路径耦合死了
    private void init() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("D:\\java\\DataSci\\lqf\\JudicatureAutoLabel\\project-dev\\judicature\\src\\main\\resources\\case\\" + "tools\\dict.txt"));
        String line;
        while ((line = br.readLine()) != null) {
            if (line.length() != 0)
                CustomDictionary.add(line, "accu 1024");
        }
        br.close();
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

    private CaseMarksArr notification(String fileName) throws IOException {
        //当事人
        String regEx = "^((.*)人（(.*)）：(.*))|((.*)人(.*))|((.*)罪犯(.*))|((.*)机关(.*))|((.*)被告(.*))$";
        Pattern pattern = Pattern.compile(regEx);

        //屏蔽词
        String NO_regEx = "^((.*)代理人(.*))|((.*)代表人(.*))|((.*)负责人(.*))|((.*)合伙人(.*))$";
        Pattern NO_pattern = Pattern.compile(NO_regEx);


        //法院 检察院
        String court_regEx = "^(.*)(法院|检察院)(.*)$";
        Pattern courtPattern = Pattern.compile(court_regEx);

        CaseMarksArr marks = new CaseMarksArr();

        boolean isNormal = false;

        //题目读案由
        String name = new File(fileName).getName();
        System.out.println(name);
        List<Term> seg = nlp.seg(name);
        for (Term term : seg) {
            term.word = term.word.replaceAll("[，。：,\\s\\.]+", "");
            if (term.word.length() > 1) {
                if (isAccusation(term))
                    marks.setAccusation(term.word);
            }
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "GBK"));
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
                //通 知 书
                //（2021）最高法刑申155号
            } else if (lineNo == 4 && line.endsWith("：")) {
                isNormal = true;//是正常的

                //刘希明：
                List<String> info = new ArrayList<>();
                String[] words = line.split("[，。：,、\\s]+");
                for (String word : words) {
                    if (!StringUtils.isEmpty(word)) {
                        info.add(word);
                    }
                }
                for (String s : info) {
                    marks.setCriminals(s);
                }
            } else {
                if (!isNormal && (line.matches(regEx) && !line.matches(NO_regEx) && line.length() < 80)) {
                    //奇怪写法

                    //名字和住所地
                    List<Term> Seg = nlp.seg(line);
                    Seg.addAll(crf.seg(line));
                    for (Term term : Seg) {
                        term.word = term.word.replaceAll("[，。：,\\s]+", "");
                        if (term.word.length() >= 2) {
                            if (isName(term)) {//名字
                                marks.setCriminals(term.word);
                            }
                            if (isPlace(term))//住址,用NLP的更准确
                                marks.setBirthplace(term.word);
                        }
                    }
                    List<String> info = new ArrayList<>();
                    String[] words = line.split("[，。：,\\s]");
                    for (String word : words) {
                        if (!StringUtils.isEmpty(word)) {
                            info.add(word);
                        }
                    }
                    //民族和性别
                    for (String value : info) {
                        if ("男".equals(value) || "女".equals(value)) {
                            marks.setGender(value);//性别
                        } else if (value.contains("族") && value.length() <= 5) {
                            marks.setEthnicity(value);//民族
                        }
                    }
                }
                List<Term> Segs = nlp.seg(line);
                Segs.addAll(crf.seg(line));

                for (Term term : Segs) {
                    term.word = term.word.replaceAll("[，。：,\\s\\.]+", "");
                    if (term.word.length() >= 2) {
                        if (isCourt(term, court_regEx))//法院
                            marks.setCourts(term.word);
                        if (term.nature.toString().equals("accu"))//案由
                            marks.setAccusation(term.word);
                    }
                }
            }
            lineNo++;
        }
        return marks;
    }

    private CaseMarksArr order(String fileName) throws IOException {
        //当事人
        String regEx = "^((.*)人（(.*)）：(.*))|((.*)人(.*))|((.*)罪犯(.*))|((.*)机关(.*))|((.*)被告(.*))$";
        Pattern pattern = Pattern.compile(regEx);

        //屏蔽词
        String NO_regEx = "^((.*)代理人(.*))|((.*)代表人(.*))|((.*)负责人(.*))|((.*)合伙人(.*))|((.*)付申(.*))$";
        Pattern NO_pattern = Pattern.compile(NO_regEx);


        //法院 检察院
        String court_regEx = "^(.*)(法院|检察院)(.*)$";
        Pattern courtPattern = Pattern.compile(court_regEx);

        CaseMarksArr marks = new CaseMarksArr();

        //题目都要读
        //名字 法院 案由
        String name = new File(fileName).getName();
        System.out.println(name);
        List<Term> seg = nlp.seg(name);
        for (Term term : seg) {
            term.word = term.word.replaceAll("[，。：,\\s\\.]+", "");
            if (term.word.length() > 1) {
                if (isName(term))
                    marks.setCriminals(term.word);
                if (isCourt(term, court_regEx))
                    marks.setCourts(term.word);
                if (isAccusation(term))
                    marks.setAccusation(term.word);
            }
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "GBK"));
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
                //通 知 书
                //（2021）最高法刑申155号
            } else {
                if ((line.matches(regEx) && !line.matches(NO_regEx) && line.length() < 80)) {

                    //名字和住所地
                    List<Term> Seg = nlp.seg(line);
                    Seg.addAll(crf.seg(line));
                    for (Term term : Seg) {
                        term.word = term.word.replaceAll("[，。：,\\s]+", "");
                        if (term.word.length() >= 2) {
                            if (isName(term)) {//名字
                                marks.setCriminals(term.word);
                            }
                            if (isPlace(term))//住址,用NLP的更准确
                                marks.setBirthplace(term.word);
                        }
                    }
                    List<String> info = new ArrayList<>();
                    String[] words = line.split("[，。：,\\s]");
                    for (String word : words) {
                        if (!StringUtils.isEmpty(word)) {
                            info.add(word);
                        }
                    }
                    //民族和性别
                    for (String value : info) {
                        if ("男".equals(value) || "女".equals(value)) {
                            marks.setGender(value);//性别
                        } else if (value.contains("族") && value.length() <= 5) {
                            marks.setEthnicity(value);//民族
                        }
                    }
                }
                List<Term> Segs = nlp.seg(line);
                Segs.addAll(crf.seg(line));

                for (Term term : Segs) {
                    term.word = term.word.replaceAll("[，。：,\\s\\.]+", "");
                    if (term.word.length() >= 2) {
                        if (isCourt(term, court_regEx))//法院
                            marks.setCourts(term.word);
                        if (term.nature.toString().equals("accu"))//案由
                            marks.setAccusation(term.word);
                    }
                }
            }
            lineNo++;
        }
        return marks;
    }

    private CaseMarksArr mediate(String fileName) throws IOException {
        return adjudication(fileName);
    }

    private CaseMarksArr judgment(String fileName) throws IOException {
        return adjudication(fileName);
    }

    private CaseMarksArr adjudication(String fileName) throws IOException {

        //当事人
        String regEx = "^((.*)人（(.*)）：(.*))|((.*)人(.*))|((.*)罪犯(.*))|((.*)机关(.*))|((.*)被告(.*))$";
        Pattern pattern = Pattern.compile(regEx);

        //屏蔽词
        String NO_regEx = "^((.*)代理人(.*))|((.*)代表人(.*))|((.*)负责人(.*))|((.*)合伙人(.*))$";
        Pattern NO_pattern = Pattern.compile(NO_regEx);

        //年份
        String year_regEx = "^(.*)(一|二|〇|三|四|五|六|七|八|九|十|(\\d\\d\\d\\d))年(.*)$";
        //法院 检察院
        String court_regEx = "^(.*)(法院|检察院)(.*)$";
        Pattern courtPattern = Pattern.compile(court_regEx);

        //重庆市高级人民法院（2019）渝民终795号
        //江苏省高级人民法院（2019）苏民终1487号
        //  String court_regEx = "^(.*)法院（\\d+）(.{0,3})民(.{0,3})终(.{0,3})\\d+号(.*)$";

        CaseMarksArr marks = new CaseMarksArr();

        //题目都要读
        //名字 法院 案由
        String name = new File(fileName).getName();
        System.out.println(name);
        List<Term> seg = nlp.seg(name);
        for (Term term : seg) {
            term.word = term.word.replaceAll("[，。：,\\s\\.]+", "");
            if (term.word.length() > 1) {
                if (isName(term))
                    marks.setCriminals(term.word);
                if (isCourt(term, court_regEx))
                    marks.setCourts(term.word);
                if (isAccusation(term))
                    marks.setAccusation(term.word);
            }
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "GBK"));
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

                ArrayList<String> info;
                //先把一行字切成几句话来处理
                String[] words;

                List<Term> Segs = nlp.seg(line);
                Segs.addAll(crf.seg(line));

                for (Term term : Segs) {
                    term.word = term.word.replaceAll("[，。：,\\s\\.]+", "");
                    if (term.word.length() >= 2) {
                        if (isCourt(term, court_regEx))//法院
                            marks.setCourts(term.word);
                        if (term.nature.toString().equals("accu"))//案由
                            marks.setAccusation(term.word);
                    }
                }

                //提取人名，地名，性别，民族
                if (pattern.matcher(line).matches() && !NO_pattern.matcher(line).matches() && (line.length() < 200 || lineNo == 4)) {

                    //名字和住所地
                    List<Term> Seg = nlp.seg(line);
                    Seg.addAll(crf.seg(line));
                    for (Term term : Seg) {
                        term.word = term.word.replaceAll("[，。：,\\s]+", "");
                        if (term.word.length() >= 2) {
                            if (isName(term)) {//名字
                                if (term.word.endsWith("犯"))
                                    term.word = term.word.replaceAll("犯", "");
                                marks.setCriminals(term.word);
                            }
                            if (isPlace(term))//住址,用NLP的更准确
                                marks.setBirthplace(term.word);
                        }
                    }

                    info = new ArrayList<>();
                    words = line.split("[，。：,\\s]");
                    for (String word : words) {
                        if (!StringUtils.isEmpty(word)) {
                            info.add(word);
                        }
                    }
                    String s;
                    //民族和性别
                    for (int i = 0; i < info.size(); i++) {
                        s = info.get(i);
                        if ("男".equals(s) || "女".equals(s)) {
                            marks.setGender(s);//性别
                        } else if (s.contains("族") && s.length() <= 5) {
                            marks.setEthnicity(s);//民族
                        }
                    }
                }
/*
                //单独处理地名
                if (lineNo >= 4 && line.length() < 60 && line.contains("市")) {
                    for (String s : line.split("[，。：,\\s]")) {
                        if (s.contains("市") && s.length() <= 30)
                            marks.setBirthplace(s);
                    }
                }*/
            }
            lineNo++;
        }
        return marks;


    }

    private CaseMarksArr decision(String fileName) throws IOException {
        return adjudication(fileName);
        /*        //当事人
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


        CaseMarksArr marks = new CaseMarksArr();

        //题目都要读
        //名字 法院 案由
        String name = new File(fileName).getName();
        System.out.println(name);
        List<Term> seg = nlp.seg(name);
        for (Term term : seg) {
            term.word = term.word.replaceAll("[，。：,\\s\\.]+", "");
            if (term.word.length() > 1) {
                if (isName(term))
                    marks.setCriminals(term.word);
                if (isCourt(term, court_regEx))
                    marks.setCourts(term.word);
                if (isAccusation(term))
                    marks.setAccusation(term.word);
            }
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "GBK"));
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

                ArrayList<String> info;
                //先把一行字切成几句话来处理
                String[] words;

                List<Term> Segs = nlp.seg(line);
                Segs.addAll(crf.seg(line));

                for (Term term : Segs) {
                    term.word = term.word.replaceAll("[，。：,\\s\\.]+", "");
                    if (term.word.length() >= 2) {
                        if (isCourt(term, court_regEx))//法院
                            marks.setCourts(term.word);
                        if (term.nature.toString().equals("accu"))//案由
                            marks.setAccusation(term.word);
                    }
                }

                //提取人名，地名，性别，民族
                if (pattern.matcher(line).matches() && !NO_pattern.matcher(line).matches() && (line.length() < 200 || lineNo == 4)) {

                    //名字和住所地
                    List<Term> Seg = nlp.seg(line);
                    Seg.addAll(crf.seg(line));
                    for (Term term : Seg) {
                        term.word = term.word.replaceAll("[，。：,\\s\\.]+", "");
                        if (term.word.length() >= 2) {
                            if (isName(term)) {//名字
                                if (term.word.endsWith("犯"))
                                    term.word = term.word.replaceAll("犯", "");
                                marks.setCriminals(term.word);
                            }
                            if (isPlace(term))//住址,用NLP的更准确
                                marks.setBirthplace(term.word);
                        }
                    }

                    info = new ArrayList<>();
                    words = line.split("[，。：,\\s]");
                    for (String word : words) {
                        if (!StringUtils.isEmpty(word)) {
                            info.add(word);
                        }
                    }
                    String s;
                    //民族和性别
                    for (int i = 0; i < info.size(); i++) {
                        s = info.get(i);
                        if ("男".equals(s) || "女".equals(s)) {
                            marks.setGender(s);//性别
                        } else if (s.contains("族") && s.length() <= 5) {
                            marks.setEthnicity(s);//民族
                        }
                    }
                }
*//*
                //单独处理地名
                if (lineNo >= 4 && line.length() < 60 && line.contains("市")) {
                    for (String s : line.split("[，。：,\\s]")) {
                        if (s.contains("市") && s.length() <= 30)
                            marks.setBirthplace(s);
                    }
                }*//*
            }
            lineNo++;
        }
        return marks;*/
    }


    private boolean isName(Term term) {
        return (term.nature == Nature.nr || term.nature == Nature.nrf ||
                term.nature == Nature.ntu || term.nature == Nature.nts || term.nature == Nature.nto ||
                term.nature == Nature.nth || term.nature == Nature.ntch || term.nature == Nature.ntcf ||
                term.nature == Nature.nit || term.nature == Nature.nic || term.nature == Nature.ni ||
                term.nature == Nature.ntcb || term.nature == Nature.ntc || term.nature == Nature.nt) && (!term.word.equals("液透析") && !term.word.equals("据此") && !term.word.equals("txt"));
    }

    private boolean isCourt(Term term, String court_regEx) {
        return term.nature == Nature.nt && term.word.matches(court_regEx);
    }

    private boolean isAccusation(Term term) {
        return term.nature.toString().equals("accu") || term.word.equals("合同纠纷") || term.word.equals("故意伤害") || term.word.equals("故意伤害罪");
    }

    private boolean isPlace(Term term) {
        return term.word.length() >= 3 && term.nature == Nature.ns && !term.word.equals("之日起");
    }


}
