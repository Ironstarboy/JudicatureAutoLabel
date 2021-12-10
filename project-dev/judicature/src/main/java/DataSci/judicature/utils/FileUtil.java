package DataSci.judicature.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface FileUtil {
    /**
     * 文件解压
     */
    void zipUncompress(String inputFile, String destDirPath) throws Exception;

    /**
     * 文件压缩
     */
    void ZipCompress(String inputFile, String outputFile) throws Exception;

    /**
     * word转txt
     */
    void word2Txt(String wordPath, String txtPath);

    /**
     * todo 先留着
     * 文件解压并转txt
     */
    //void zipUncompress2txt(String inputFile, String outputFile);

    /**
     * 文件下载
     */
    void download(String filename, HttpServletResponse res) throws IOException;

}
