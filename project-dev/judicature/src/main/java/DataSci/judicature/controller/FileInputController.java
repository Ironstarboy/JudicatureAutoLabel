package DataSci.judicature.controller;

import DataSci.judicature.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
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
    public String save(MultipartFile upload, HttpSession session) {

        boolean flag = true;

        if (upload == null || upload.isEmpty()) {
            return "请上传文件";
        }

        try {
            //获取上传文件的名称
            String originalFilename = upload.getOriginalFilename();

            //文件转移到某个目录下
            if (originalFilename != null) {
                int i = originalFilename.lastIndexOf(".");
                if (i == -1) {
                    return "上传失败！";
                }
                String prefix = originalFilename.substring(0, i);//获取文件前缀
                String suffix = originalFilename.substring(i + 1);//获取文件后缀

                if (suffix.equals("docx"))
                    suffix = "doc";

                if ((!"doc".equals(suffix)) && (!"txt".equals(suffix)))
                    return "请上传doc或者txt文件";

                //todo 文件的分类没实现完
                String category = fileService.transfer(upload,prefix,suffix);

                //设置session 记录上传的是哪个文件
                session.setAttribute("userUploadFile", location + "txt\\" + category + prefix + ".txt");

                //设置上传文件的种类信息
                session.setAttribute("category", category);

                //设置文件名信息
                session.setAttribute("filename", prefix);

                //转txt 顺便进一步文件分类
                if (suffix.equals("doc"))
                    flag = fileService.toTxt(location + "doc\\" + category + prefix + ".doc", session);

                System.out.println(originalFilename);
                System.out.println(session.getAttribute("userUploadFile"));
                System.out.println("上传成功！");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "上传失败！";
        }

        if (flag)
            return "上传成功！";
        else
            return "上传失败！";
    }


    @RequestMapping("/words")
    public String write() {
        //todo 上传文字 转txt
        return null;
    }
}
