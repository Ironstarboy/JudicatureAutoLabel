package DataSci.judicature;

import DataSci.judicature.domain.CaseMarksArr;
import DataSci.judicature.service.WordService;
import DataSci.judicature.service.impl.WordServiceImpl;
import DataSci.judicature.utils.FileUtil;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.hankcs.hanlp.model.crf.CRFLexicalAnalyzer;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import org.apache.catalina.session.StandardSession;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.List;

@SpringBootTest
public class WordTest {

    @Value("${PATH}")
    private String PATH;


    @Value("${spring.servlet.multipart.location}")
    private String location;


    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private WordServiceImpl wordService;


    private static Segment nlp = null;
    private static Segment crf = null;

/*

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

*/

    @BeforeAll
    static void init() throws IOException {
        nlp = NLPTokenizer.ANALYZER.enableOrganizationRecognize(true).enablePlaceRecognize(true).enableCustomDictionary(true).enableCustomDictionaryForcing(true);
        crf = new CRFLexicalAnalyzer().enablePlaceRecognize(true).enableOrganizationRecognize(true).enableCustomDictionary(true).enableCustomDictionaryForcing(true);
        BufferedReader br = new BufferedReader(new FileReader("D:\\java\\DataSci\\lqf\\JudicatureAutoLabel\\project-dev\\judicature\\src\\main\\resources\\case\\" + "tools\\dict.txt"));
        String line;
        while ((line = br.readLine()) != null) {
            if (line.length() != 0) {
                CustomDictionary.remove(line);
                CustomDictionary.add(line, "accu 1024 nz 0");
            }
        }
        br.close();
        System.out.println("前奏");
    }

    @Test
    void testNLP() {
        List<Term> s = nlp.seg("　　被申请人顾爱媛，女，1970年9月29日生，汉族。");
        System.out.println(s);
        List<Term> seg1 = crf.seg("　　被申请人顾爱媛，女，1970年9月29日生，汉族。");
        System.out.println(seg1);

/*

        List<String> strings = HanLP.extractPhrase("再审申请人（一审原告、反诉被告、二审被上诉人）：史生来，男，1966年3月22日出生，汉族，住陕西省西安市户县。\n" +
                "　　被申请人（一审被告、反诉原告、二审上诉人）：甘肃省第八建设集团有限责任公司。住所地：甘肃省天水市秦州区建设路161号。", 50);
        System.out.println(strings.toString());
*/

    }

    /*

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

    */
    @Test
    void testNotification() throws IOException {

        String type = "judgment";
        File dir = new File(location + "txt\\" + type);

        String[] files = dir.list();
        assert files != null;
        for (String file : files) {
            CaseMarksArr marks = wordService.extract(dir.getAbsolutePath() + "\\" + file);
            System.out.println(marks);
        }
    }

/*
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

*/


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
