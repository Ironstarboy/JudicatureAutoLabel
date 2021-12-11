package DataSci.judicature.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

/**
 * 文件上传控制器
 * 用户传来的文件将会被保存在/resources/case/下
 */
@RestController
@RequestMapping("/upload")
public class FileInputController {

    @Value("${spring.servlet.multipart.location}")
    private String location;

    @RequestMapping("/file")
    public void save(MultipartFile upload, HttpSession session) throws IOException {


        //获取上传文件的名称
        String originalFilename = upload.getOriginalFilename();

        //文件转移到某个目录下
        if (originalFilename != null) {
            String prefix = originalFilename.split("\\.")[0];//获取文件前缀
            String suffix = originalFilename.split("\\.")[1];//获取文件后缀

            if (suffix.equals("docx"))
                suffix = "doc";

            File f = new File(location + suffix + "\\" + originalFilename);
            if (f.exists())
                f.delete();

            upload.transferTo(new File(suffix + "\\" + originalFilename));

            //设置session 记录上传的是哪个文件
            session.setAttribute("userUploadFile", location + "txt\\" + prefix + ".txt");
        }
    }
}
