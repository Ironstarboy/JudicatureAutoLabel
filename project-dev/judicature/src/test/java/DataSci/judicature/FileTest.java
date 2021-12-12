package DataSci.judicature;

import DataSci.judicature.service.impl.FileServiceImpl;
import DataSci.judicature.utils.FileUtil;
import DataSci.judicature.utils.impl.FileUtilImpl;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Arrays;


@SpringBootTest
public class FileTest {

    @Value("${PATH}")
    private String PATH;


    @Value("${spring.servlet.multipart.location}")
    private String location;


    @Autowired
    private FileUtilImpl fileUtil;

    @Test
    void testDOC() {
        String fileName = location + "doc\\adjudication\\";
        String outName = location + "txt\\adjudication\\";

        fileUtil.word2Txt(fileName, outName);

    }


    @Test
    void testZip() throws Exception {
        String inputName = "C:\\Users\\18933\\Desktop\\DataSciProject\\20211212143128211212AA70NGABHH.zip";
        String destDirPath = location + "doc";
        fileUtil.zipUncompress(inputName, destDirPath);
    }

    @Test
    void testZipCompress() throws Exception {
        String input = location + "txt";
        String dest = location + "zip\\test.zip";
        fileUtil.ZipCompress(input,dest);
    }

    @Test
    void testTransfer() throws IOException {
        fileUtil.transfer(location+"doc\\");
    }

    @Test
    void testString(){
        String[] s={"sf","111","dsjflsjfs"};
        String line= StringUtils.join(s,"");
        System.out.println(line);
    }

}
