package DataSci.judicature.service.impl;

import DataSci.judicature.domain.CaseInfoSets;
import DataSci.judicature.domain.CaseMarks;
import DataSci.judicature.domain.CaseMarksArr;
import DataSci.judicature.domain.CaseMsg;
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

import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
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
    private static Segment nlp;

    private static Segment crf;

    private static Set<String> accu;

    private static Set<String> type;


    //初始化nlp分词器
    //TODO 这里路径耦合死了
    static {
        System.out.println("静态代码块");
        accu = new HashSet<>();
        type = new HashSet<>();
        try {
            nlp = NLPTokenizer.ANALYZER.enableOrganizationRecognize(true).enablePlaceRecognize(true).enableCustomDictionary(true).enableCustomDictionaryForcing(true);
            crf = new CRFLexicalAnalyzer().enablePlaceRecognize(true).enableOrganizationRecognize(true).enableCustomDictionary(true).enableCustomDictionaryForcing(true);

            BufferedReader br = new BufferedReader(new FileReader("D:\\java\\DataSci\\lqf\\JudicatureAutoLabel\\project-dev\\judicature\\src\\main\\resources\\case\\" + "tools\\dict.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.length() != 0) {
                    accu.add(line);
                    CustomDictionary.add(line, "accu 1024");
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader("D:\\java\\DataSci\\lqf\\JudicatureAutoLabel\\project-dev\\judicature\\src\\main\\resources\\case\\" + "tools\\type.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.length() != 0) {
                    type.add(line);
                    CustomDictionary.add(line, "type 1024");
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public CaseMarksArr extract(String fileName, HttpSession session) throws IOException {
        CaseMarksArr marks = null;
        String type = (String) session.getAttribute("category");

        if (type.startsWith("adjudication")) {
            marks = adjudication(fileName, session);
        } else if (type.startsWith("decision")) {
            marks = decision(fileName, session);
        } else if (type.startsWith("judgment")) {
            marks = judgment(fileName, session);
        } else if (type.startsWith("mediate")) {
            marks = mediate(fileName, session);
        } else if (type.startsWith("notification")) {
            marks = notification(fileName, session);
        } else if (type.startsWith("order")) {
            marks = order(fileName, session);
        }
        return marks;
    }


    private CaseMarksArr notification(String fileName, HttpSession session) throws IOException {
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

        String format = (String) session.getAttribute("format");
        BufferedReader br;

        if ("txt".equals(format))
            br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8));
        else
            br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "GBK"));

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
                        if (isAccusation(term))//案由
                            marks.setAccusation(term.word);
                    }
                }
            }
            lineNo++;
        }
        return marks;
    }

    private CaseMarksArr order(String fileName, HttpSession session) throws IOException {
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

        String format = (String) session.getAttribute("format");
        BufferedReader br;

        if ("txt".equals(format))
            br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8));
        else
            br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "GBK"));

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
                        if (isAccusation(term))//案由
                            marks.setAccusation(term.word);
                    }
                }
            }
            lineNo++;
        }
        return marks;
    }

    private CaseMarksArr mediate(String fileName, HttpSession session) throws IOException {
        return adjudication(fileName, session);
    }

    private CaseMarksArr judgment(String fileName, HttpSession session) throws IOException {
        return adjudication(fileName, session);
    }

    private CaseMarksArr adjudication(String fileName, HttpSession session) throws IOException {

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

        String format = (String) session.getAttribute("format");
        BufferedReader br;

        if ("txt".equals(format))
            br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8));
        else
            br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "GBK"));

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
                        if (isAccusation(term))//案由
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

    private CaseMarksArr decision(String fileName, HttpSession session) throws IOException {
        return adjudication(fileName, session);
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
                term.nature == Nature.ntcb || term.nature == Nature.ntc || term.nature == Nature.nt) && (!term.word.equals("液透析") && !term.word.equals("据此") && !term.word.equals("txt") && !term.word.matches("(.*)\\d(.*)"));
    }

    private boolean isCourt(Term term, String court_regEx) {
        return term.nature == Nature.nt && term.word.matches(court_regEx);
    }

    private boolean isAccusation(Term term) {
        return accu.contains(term.word);
    }

    private boolean addType(CaseInfoSets info, String word) {
        if (word.equals("管辖")) {
            info.setType("管辖案件");
            return true;
        }
        if (word.equals("刑事")) {
            info.setType("刑事案件");
            return true;
        }
        if (word.equals("民事")) {
            info.setType("民事案件");
            return true;
        }
        if (word.equals("行政")) {
            info.setType("行政案件");
            return true;
        }
        if (word.contains("赔偿") || word.contains("救助")) {
            info.setType("国家赔偿与司法救助案件");
            return true;
        }
        if (word.contains("制裁")) {
            info.setType("司法制裁案件");
            return true;
        }
        if (word.equals("区域司法协助")) {
            info.setType("区域司法协助案件");
            return true;
        }
        if (word.contains("清算") || word.contains("破产")) {
            info.setType("强制清算与破产案件");
            return true;
        }
        if (word.contains("执行")) {
            info.setType("执行案件");
            return true;
        }
        if (word.equals("国际司法协助")) {
            info.setType("国际司法协助案件");
            return true;
        }
        if (word.equals("非法保全审查")) {
            info.setType("非法保全审查案件");
            return true;
        }
        return false;
    }

    private boolean isPlace(Term term) {
        return term.word.length() >= 3 && term.nature == Nature.ns && !term.word.equals("之日起");
    }

    @Override
    public CaseMarks toJSON(CaseMsg caseMsg) {
        CaseMarks marks = new CaseMarks();
        String acc = caseMsg.getAccusation_text();
        String bir = caseMsg.getBirthplace_text();
        String cur = caseMsg.getCourts_text();
        String gen = caseMsg.getGender_text();
        String eth = caseMsg.getEthnicity_text();
        String cri = caseMsg.getCriminals_text();
        String sum = caseMsg.getSummary_text();
        marks.setCriminals(new ArrayList<>(Arrays.asList(cri.split("[,，]+"))));
        marks.setCourts(new ArrayList<>(Arrays.asList(cur.split("[,，]+"))));
        marks.setGender(new ArrayList<>(Arrays.asList(gen.split("[,，]+"))));
        marks.setBirthplace(new ArrayList<>(Arrays.asList(bir.split("[,，]+"))));
        marks.setAccusation(new ArrayList<>(Arrays.asList(acc.split("[,，]+"))));
        marks.setElse(new ArrayList<>(Arrays.asList(sum.split("[,，]+"))));
        marks.setEthnicity(new ArrayList<>(Arrays.asList(eth.split("[,，]+"))));

        return marks;
    }


    /**
     * 生成案件基本信息
     */
    @Override
    public CaseInfoSets proInfo(String fileName, HttpSession session) throws IOException {
        boolean flag = false;
        Set<String> cat = new HashSet<>();
        initSet(cat);

        //年份
        String year_regEx = "^(.*)[一二〇三四五六七八九十零０]{4}年[一二三四五六七八九十]{1,2}月[一二三四五六七八九十]{1,3}日(.*)$";

        CaseInfoSets info = new CaseInfoSets();


        //题目读案由
        String name = (String) session.getAttribute("filename");
        info.setTitle(name);
        List<Term> seg = nlp.seg(name);
        for (Term term : seg) {
            term.word = term.word.replaceAll("[，。：,\\s\\.]+", "");
            if (term.word.length() > 1) {
                if (isAccusation(term))
                    info.setAccusation(term.word);
                if (addType(info, term.word)) {
                    flag = true;
                }
            }
        }

        String format = (String) session.getAttribute("format");
        BufferedReader br;

        if ("txt".equals(format))
            br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8));
        else
            br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "GBK"));

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
                line = line.replaceAll(" ", "");
                if (line.contains("法院")) {
                    info.setCourts(line);
                }
            } else if (lineNo == 2) {
                //民 事 裁 定 书
                //todo 可能出问题
                line = line.replaceAll(" ", "");
                String category = line.substring(Math.max(line.length() - 3, 0));
                if (cat.contains(category)) {
                    info.setCategory(category);
                } else {
                    info.setCategory("其他");
                }

                List<Term> segs = nlp.seg(line);
                for (Term term : segs) {
                    if (!flag && term.word.length() > 1) {
                        flag = addType(info, term.word);
                    }
                }
            } else if (lineNo == 3) {
                //（2021）最高法民申5039号
                info.setCaseno(line);
            } else {
                //裁判日期
                //反复迭代的方法做
                if (line.matches(year_regEx)) {
                    String[] split = line.split("[，。：,\\s\\.]+");
                    for (String s : split) {
                        if (s.matches(year_regEx)) {
                            if (!s.matches(//只有一个年月日
                                    "((.*)[一二〇三四五六七八九十零０]{4}年[一二三四五六七八九十]{1,2}月[一二三四五六七八九十]{1,3}日(.*)([一二〇三四五六七八九十零０]){4}年[一二三四五六七八九十]{1,2}月[一二三四五六七八九十]{1,3}日(.*))")) {
                                int ri = s.indexOf("日");
                                int nian = s.indexOf("年");
                                String date;
                                while (!(date = s.substring(Math.max(0, nian - 4), ri + 1)).matches(year_regEx)) {
                                    s = s.substring(ri + 1);
                                    ri = s.indexOf("日");
                                    nian = s.indexOf("年");
                                    if (ri == -1 || nian == -1)
                                        break;
                                }
                                info.setDate(proDate(date));
                            }
                        }
                    }
                }

                //案由
                List<Term> Segs = nlp.seg(line);
                Segs.addAll(crf.seg(line));

                for (Term term : Segs) {
                    term.word = term.word.replaceAll("[，。：,\\s\\.]+", "");
                    if (term.word.length() >= 2 && isAccusation(term)) {
                        info.setAccusation(term.word);
                    }
                    if (!flag) {
                        addType(info, term.word);
                    }
                }

            }
            lineNo++;
        }

        fixType(info);
        return info;
    }

    private void initSet(Set<String> set) {
        set.add("裁定书");
        set.add("判决书");
        set.add("支付令");
        set.add("决定书");
        set.add("通知书");
        set.add("调解书");
    }

    private void fixType(CaseInfoSets info) {
        Set<String> set = info.getType();
        if (set.isEmpty()) {
            if (info.getCategory().contains("支付令")) {
                info.setType("民事案件");
                return;
            }
            info.setType("其他案件");
            return;
        }
        if (set.contains("管辖案件")) {
            info.setType(new HashSet<>(Collections.singleton("管辖案件")));
            return;
        }
        if (set.contains("刑事案件")) {
            info.setType(new HashSet<>(Collections.singleton("刑事案件")));
            return;
        }
        if (set.contains("民事案件")) {
            info.setType(new HashSet<>(Collections.singleton("民事案件")));
            return;
        }
    }

    private String proDate(String date) {
        String res;

        String number = "^(.*)(一|二|三)(.*)$";
        //二〇二一年五月二十五日
        res = date.replaceAll("[年月]", "-").replaceAll("日", "");
        res = res.replaceAll("[零〇０]", "0").
                replaceAll("一", "1").
                replaceAll("二", "2").
                replaceAll("三", "3").
                replaceAll("四", "4").
                replaceAll("五", "5").
                replaceAll("六", "6").
                replaceAll("七", "7").
                replaceAll("八", "8").
                replaceAll("九", "9");

        String[] s = res.split("-");
        String month = s[1];
        String d = s[2];

        //月份
        if (month.startsWith("十")) {
            if (month.length() == 1) {//十月
                month = month.replaceAll("十", "10");
            } else {
                month = month.replaceAll("十", "1");
            }
        }

        //日
        if (d.contains("十")) {
            if (d.length() == 1) {//十
                d = d.replaceAll("十", "10");
            } else if (d.length() == 3) {//二十一
                d = d.replaceAll("十", "");
            } else {
                if (d.startsWith("十"))//十九
                    d = d.replaceAll("十", "1");
                else//二十
                    d = d.replaceAll("十", "0");
            }
        }

        res = s[0] + "-" + month + "-" + d;
        return res;
    }

    /**
     * 仅用于测试
     */
    public CaseInfoSets extract(String fileName) throws IOException {
        boolean flag = false;
        Set<String> cat = new HashSet<>();
        initSet(cat);

        //年份
        String year_regEx = "^(.*)[一二〇三四五六七八九十零０]{4}年[一二三四五六七八九十]{1,2}月[一二三四五六七八九十]{1,3}日(.*)$";

        CaseInfoSets info = new CaseInfoSets();

        //题目读案由
        String name = new File(fileName).getName();
        System.out.println(name);
        List<Term> seg = nlp.seg(name);
        for (Term term : seg) {
            term.word = term.word.replaceAll("[，。：,\\s\\.]+", "");
            if (term.word.length() > 1) {
                if (isAccusation(term))
                    info.setAccusation(term.word);
                if (addType(info, term.word)) {
                    flag = true;
                }
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
                line = line.replaceAll(" ", "");
                if (line.contains("法院")) {
                    info.setCourts(line);
                }
            } else if (lineNo == 2) {
                //民 事 裁 定 书
                //todo 可能出问题
                line = line.replaceAll(" ", "");
                String category = line.substring(Math.max(line.length() - 3, 0));
                if (cat.contains(category)) {
                    info.setCategory(category);
                } else {
                    info.setCategory("其他");
                }

                List<Term> segs = nlp.seg(line);
                for (Term term : segs) {
                    if (!flag && term.word.length() > 1) {
                        flag = addType(info, term.word);
                    }
                }
            } else if (lineNo == 3) {
                //（2021）最高法民申5039号
                info.setCaseno(line);
            } else {
                //裁判日期
                //反复迭代的方法做
                if (line.matches(year_regEx)) {
                    String[] split = line.split("[，。：,\\s\\.]+");
                    for (String s : split) {
                        if (s.matches(year_regEx)) {
                            if (!s.matches(//只有一个年月日
                                    "((.*)[一二〇三四五六七八九十零０]{4}年[一二三四五六七八九十]{1,2}月[一二三四五六七八九十]{1,3}日(.*)([一二〇三四五六七八九十零０]){4}年[一二三四五六七八九十]{1,2}月[一二三四五六七八九十]{1,3}日(.*))")) {
                                int ri = s.indexOf("日");
                                int nian = s.indexOf("年");
                                String date;
                                while (!(date = s.substring(Math.max(0, nian - 4), ri + 1)).matches(year_regEx)) {
                                    s = s.substring(ri + 1);
                                    ri = s.indexOf("日");
                                    nian = s.indexOf("年");
                                    if (ri == -1 || nian == -1)
                                        break;
                                }
                                info.setDate(proDate(date));
                            }
                        }
                    }
                }

                //案由
                List<Term> Segs = nlp.seg(line);
                Segs.addAll(crf.seg(line));

                for (Term term : Segs) {
                    term.word = term.word.replaceAll("[，。：,\\s\\.]+", "");
                    if (term.word.length() >= 2 && isAccusation(term)) {
                        info.setAccusation(term.word);
                    }
                    if (!flag) {
                        addType(info, term.word);
                    }
                }

            }
            lineNo++;
        }

        fixType(info);
        return info;
    }


}
