package DataSci.judicature.controller;

import DataSci.judicature.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private FileService fileService;

    @RequestMapping("/file")
    public String save(MultipartFile upload, HttpSession session) throws IOException {

        if (upload == null || upload.isEmpty()) {
            return "请上传文件";
        }

        //获取上传文件的名称
        String originalFilename = upload.getOriginalFilename();

        //文件转移到某个目录下
        if (originalFilename != null) {
            String prefix = originalFilename.split("\\.")[0];//获取文件前缀
            String suffix = originalFilename.split("\\.")[1];//获取文件后缀

            if (suffix.equals("docx"))
                suffix = "doc";

            String category = fileService.transfer(originalFilename);

            File f = new File(location + suffix + "\\" + category + originalFilename);
            if (f.exists())
                System.out.println(f.delete());

            upload.transferTo(new File(suffix + "\\" + category + originalFilename));

            //设置session 记录上传的是哪个文件
            session.setAttribute("userUploadFile", location + "txt\\" + category + prefix + ".txt");

            System.out.println(originalFilename);
            System.out.println(session.getAttribute("userUploadFile"));
            System.out.println("上传成功！！！！");
            return "上传成功！";
        }
        return "上传失败！";
    }
}
