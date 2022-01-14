package DataSci.judicature;

import DataSci.judicature.utils.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;


@SpringBootTest
public class FileTest {

    @Value("${PATH}")
    private String PATH;


    @Value("${spring.servlet.multipart.location}")
    private String location;


    @Autowired
    private FileUtil fileUtil;

    @Test
    void testDOC() {
        String fileName = location + "doc\\decision\\";
        String outName = location + "txt\\decision\\";

        fileUtil.word2Txt(fileName, outName);

    }


    @Test
    void testZip() throws Exception {
        File dir = new File("C:\\Users\\18933\\Desktop\\DataSciProject\\");
        String destDirPath = location + "doc";
        String[] fns = dir.list();
        for (String fn : fns) {
            if (fn.endsWith("zip")) {
                String inputName = "C:\\Users\\18933\\Desktop\\DataSciProject\\" + fn;
                fileUtil.zipUncompress(inputName, destDirPath);
            }
        }


    }

    @Test
    void testZipCompress() throws Exception {
        String input = location + "txt";
        String dest = location + "zip\\test.zip";
        fileUtil.ZipCompress(input, dest);
    }

    @Test
    void testTransfer() throws IOException {
        fileUtil.transfer(location + "doc\\");
    }

    @Test
    void testString() {
        String[] s = {"sf", "111", "dsjflsjfs"};
        String line = StringUtils.join(s, "");
        System.out.println(line);
    }

}
