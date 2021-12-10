package DataSci.judicature.controller;

import DataSci.judicature.domain.CaseMsg;
import DataSci.judicature.service.FileService;
import DataSci.judicature.utils.FileUtil;
import DataSci.judicature.utils.impl.FileUtilImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 文件输出控制器
 * 输出案件.txt
 * 分词.json
 */
@RestController
@RequestMapping("/download")
public class FileOutPutController {

    @Autowired
    private FileService fileService;

    @Autowired
    private FileUtilImpl fileUtil;

    /**
     * @return 返回案件信息的json文件
     * todo service层逻辑没实现，未确定python脚本的传入参数
     */
    @RequestMapping("/json")
    public CaseMsg json(HttpServletResponse response) {
        response.reset();
        response.setContentType("application/octet-stream");// 设置强制下载不打开
        response.setCharacterEncoding("utf8");
        response.addHeader("Content-Disposition", "attachment;fileName=" + new String("标注.json".getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));

        //TODO 确定应该返回json的对象
        return null;
    }

    /**
     * 返回案件文本.txt
     */
    @RequestMapping("/txt")
    public String txt(HttpServletResponse response, HttpSession session) {

        String downloadFilePath = (String) session.getAttribute("userUploadFile");
        if (downloadFilePath == null) {
            return "请先上传文书!";
        }

        response.reset();
        response.setContentType("application/octet-stream");// 设置强制下载不打开
        response.setCharacterEncoding("utf8");
        response.addHeader("Content-Disposition", "attachment;fileName=" + new String("案件文本.txt".getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));


        File file = new File(downloadFilePath);
        if (file.exists()) {
            try {
                fileUtil.download(downloadFilePath, response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
