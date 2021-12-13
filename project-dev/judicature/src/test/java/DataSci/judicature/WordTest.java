package DataSci.judicature;

import DataSci.judicature.domain.CaseMarksArr;
import DataSci.judicature.service.WordService;
import DataSci.judicature.utils.impl.FileUtilImpl;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;

@SpringBootTest
public class WordTest {

    @Value("${PATH}")
    private String PATH;


    @Value("${spring.servlet.multipart.location}")
    private String location;


    @Autowired
    private FileUtilImpl fileUtil;

    @Autowired
    private WordService wordService;

    @Test
    void testAdjudication() throws IOException {

        String type = "adjudication";
        File dir = new File(location + "txt\\" + type);

        String[] files = dir.list();

        for (String file : files) {
            CaseMarksArr marks = wordService.extract(dir.getAbsolutePath() +"\\"+ file, type);
            System.out.println(marks);
        }


    }

}
