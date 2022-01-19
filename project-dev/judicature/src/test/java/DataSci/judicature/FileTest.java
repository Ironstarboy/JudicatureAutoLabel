package DataSci.judicature;

import DataSci.judicature.utils.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.nio.charset.StandardCharsets;


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
        String fileName = location + "doc\\order\\";
        String outName = location + "txt\\order\\";

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
                new File(inputName).delete();
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
        fileUtil.transfer(location + "txt\\");
    }

    @Test
    void testString() {
        String[] s = {"sf", "111", "dsjflsjfs"};
        String line = StringUtils.join(s, "");
        System.out.println(line);
    }

    @Test
    void util() {
        String s =
                "";
        String res = s;
        res = res.replaceAll("\\s", " ");
        System.out.println(res);
        System.out.println();
        System.out.println();

    }

    @Test
    void encoding() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("D:\\java\\DataSci\\lqf\\JudicatureAutoLabel\\project-dev\\judicature\\src\\main\\resources\\case\\txt\\adjudication\\安徽河海水利水电机械维护有限公司西宁特殊钢股份有限公司合同纠纷再审审查与审判监督民事裁定书(1).txt"), "GBK"));
        String line;
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream("D:\\java\\DataSci\\lqf\\JudicatureAutoLabel\\project-dev\\judicature\\src\\main\\resources\\case\\txt\\测试编码.txt"), StandardCharsets.UTF_8);
        while ((line = br.readLine()) != null) {
            writer.write(line + System.lineSeparator());
        }

        writer.close();
        br.close();
    }

}
