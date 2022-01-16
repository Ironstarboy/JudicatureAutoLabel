package DataSci.judicature;

import DataSci.judicature.domain.CaseMarksArr;
import DataSci.judicature.service.WordService;
import DataSci.judicature.utils.FileUtil;
import com.hankcs.hanlp.model.crf.CRFLexicalAnalyzer;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.huaban.analysis.jieba.WordDictionary;
import com.qianxinyao.analysis.jieba.keyword.TFIDFAnalyzer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@SpringBootTest
public class WordTest {

    @Value("${PATH}")
    private String PATH;


    @Value("${spring.servlet.multipart.location}")
    private String location;


    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private WordService wordService;

    private final JiebaSegmenter jb = new JiebaSegmenter();
    private final WordDictionary wd = WordDictionary.getInstance();
    private final TFIDFAnalyzer tf = new TFIDFAnalyzer();


    @Test
    void testAdjudication() throws IOException {
        String type = "adjudication";
        File dir = new File(location + "txt\\" + type);

        String[] files = dir.list();
        assert files != null;
        for (String file : files) {
            CaseMarksArr marks = wordService.extract(dir.getAbsolutePath() + "\\" + file, type);
            System.out.println(marks);
        }
    }


    @Test
    void testNLP() throws IOException {

        Segment seg = NLPTokenizer.ANALYZER.enableOrganizationRecognize(true).enablePlaceRecognize(true);
        List<Term> s = seg.seg("陈述东与靖州苗族侗族自治县人民法院国家赔偿一案决定书");

        System.out.println(s);

        Segment crf = new CRFLexicalAnalyzer().enablePlaceRecognize(true).enableOrganizationRecognize(true);
        List<Term> seg1 = crf.seg("陈述东与靖州苗族侗族自治县人民法院国家赔偿一案决定书");
        System.out.println(seg1);

/*

        List<String> strings = HanLP.extractPhrase("再审申请人（一审原告、反诉被告、二审被上诉人）：史生来，男，1966年3月22日出生，汉族，住陕西省西安市户县。\n" +
                "　　被申请人（一审被告、反诉原告、二审上诉人）：甘肃省第八建设集团有限责任公司。住所地：甘肃省天水市秦州区建设路161号。", 50);
        System.out.println(strings.toString());
*/

    }

    @Test
    void testJudgment() throws IOException {
        String type = "judgment";
        File dir = new File(location + "txt\\" + type);

        String[] files = dir.list();
        assert files != null;
        for (String file : files) {
            CaseMarksArr marks = wordService.extract(dir.getAbsolutePath() + "\\" + file, type);
            System.out.println(marks);
        }
    }

    @Test
    void testMediate() throws IOException {
        String type = "mediate";
        File dir = new File(location + "txt\\" + type);

        String[] files = dir.list();
        assert files != null;
        for (String file : files) {
            CaseMarksArr marks = wordService.extract(dir.getAbsolutePath() + "\\" + file, type);
            System.out.println(marks);
        }
    }


    @Test
    void testNotification() throws IOException {
        String type = "notification";
        File dir = new File(location + "txt\\" + type);

        String[] files = dir.list();
        assert files != null;
        for (String file : files) {
            CaseMarksArr marks = wordService.extract(dir.getAbsolutePath() + "\\" + file, type);
            System.out.println(marks);
        }
    }


    @Test
    void testOrder() throws IOException {
        String type = "order";
        File dir = new File(location + "txt\\" + type);

        String[] files = dir.list();
        assert files != null;
        for (String file : files) {
            CaseMarksArr marks = wordService.extract(dir.getAbsolutePath() + "\\" + file, type);
            System.out.println(marks);
        }
    }

    @Test
    void testDecision() throws IOException {
        String type = "decision";
        File dir = new File(location + "txt\\" + type);

        String[] files = dir.list();
        assert files != null;
        for (String file : files) {
            CaseMarksArr marks = wordService.extract(dir.getAbsolutePath() + "\\" + file, type);
            System.out.println(marks);
        }
    }



    @Test
    void testJieba() {

        List<SegToken> tks = jb.process("",
                JiebaSegmenter.SegMode.SEARCH);


        //对关键词进行结巴keyword分词
        String keyword = "";
        String[] keyword_list;

        for (SegToken s : tks)
            if (s.word.length() > 1)
                keyword += " " + s.word;

        keyword_list = keyword.split("[,;\\s'\\*\\+|\\^]+");
        Set<String> keywordList = new LinkedHashSet<String>(Arrays.asList(keyword_list));//用set是为了去除文章的重复

        for (String s : keywordList) {
            System.out.println(wd.getProperties(s) + s);
        }

/*        List<Keyword> analyze = tf.analyze("", 100);
        for (Keyword keyword : analyze) {
            System.out.println(keyword.getName() + keyword.getTfidfvalue());
        }*/
       /* for (SegToken tk : tks) {
            System.out.println(wd.getProperties(tk.word));
            System.out.println(tk);
        }*/
    }

    @Test
    void testfenci() throws IOException {
/*
        System.out.println("你好啊" + wd.getProperties("你好啊"));
        System.out.println("鲁权锋" + wd.getProperties("鲁权锋"));
        System.out.println("你在干什么" + wd.getProperties("你在干什么"));
        System.out.println("睡觉" + wd.getProperties("睡觉"));
        System.out.println("抢劫" + wd.getProperties("抢劫"));
        System.out.println("犯罪" + wd.getProperties("犯罪"));
        System.out.println("大作业" + wd.getProperties("大作业"));
        System.out.println("司法大数据" + wd.getProperties("司法大数据"));
        System.out.println("踢球" + wd.getProperties("踢球"));
        System.out.println("踢足球" + wd.getProperties("踢足球"));
        System.out.println("广东省人民法院" + wd.getProperties("广东省人民法院"));

*/


        String type = "decision";
        File dir = new File(location + "txt\\" + type);

        String[] files = dir.list();
        CaseMarksArr marks = null;
        for (String file : files) {
            marks = wordService.extract(dir.getAbsolutePath() + "\\" + file, type);
            System.out.println(marks);
            break;
        }

        Set<String> criminals = marks.getCriminals();
        for (String c : criminals) {
            String properties = wd.getProperties(c);
            System.out.println(c + properties);
        }
    }


    @Test
    void proWords() throws IOException {
        File f = new File(location + "tools\\accusation.txt");
        BufferedReader br = new BufferedReader(new FileReader(f));
        BufferedWriter bw = new BufferedWriter(new FileWriter(new File(location + "tools\\dict.txt")));
        String line;
        while ((line = br.readLine()) != null) {
            String[] s = line.split("[\\s :]");
            for (String s1 : s) {
                if (s1.length() != 0 && !s1.equals(" "))
                    bw.write(s1 + "\r\n");
            }
        }
        bw.close();
        br.close();
    }

    @Test
    void addWords() throws IOException {
        File f = new File(location + "tools\\accusation.txt");
        BufferedReader br = new BufferedReader(new FileReader(f));
        BufferedWriter bw = new BufferedWriter(new FileWriter(new File(location + "tools\\dict.txt")));
        String line;
        while ((line = br.readLine()) != null) {
            String[] s = line.split("[\\s :]");
            for (String s1 : s) {
                if (s1.length() != 0 && !s1.equals(" ")) {
                    bw.write(s1 + "\r\n");
                    if (s1.contains("罪"))
                        bw.write(s1.replaceAll("罪", "") + "\r\n");
                    if (s1.contains("、"))
                        bw.write(s1.replaceAll("、", "") + "\r\n");
                    if (s1.contains("、") && s1.contains("罪"))
                        bw.write(s1.replaceAll("[、罪]", "") + "\r\n");
                }
            }
        }
        bw.close();
        br.close();
    }

}
