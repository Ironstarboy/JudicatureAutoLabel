package DataSci.judicature;

import DataSci.judicature.service.impl.FileServiceImpl;
import DataSci.judicature.utils.FileUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;

@SpringBootTest
public class FileTest {

    @Value("${PATH}")
    private String PATH;


    @Value("${spring.servlet.multipart.location}")
    private String location;


    @Autowired
    private FileServiceImpl fileService;

    @Autowired
    private FileUtil fileUtil;

    @Test
    void testDOC() {
        String fileName = location + "doc\\";
        String outName = location + "txt\\";

        fileUtil.word2Txt(fileName, outName);

    }


    @Test
    void testZip() throws Exception {
        String inputName = "C:\\Users\\18933\\Desktop\\DataSciProject\\20211206200917211206FARAM81W6W.zip";
        String destDirPath = location + "doc";
        fileUtil.zipUncompress(inputName, destDirPath);
    }

    @Test
    void testZipCompress() throws Exception {
        String input = location + "txt";
        String dest = location + "zip\\test.zip";
        fileUtil.ZipCompress(input,dest);
    }
}
