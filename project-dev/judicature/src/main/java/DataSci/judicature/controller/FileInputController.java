package DataSci.judicature.controller;

import DataSci.judicature.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;

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
    @Qualifier("service2")
    private FileService fileService;

    @RequestMapping("/file")
    public String save(MultipartFile upload, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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


                if ((!"doc".equals(suffix)) && (!"txt".equals(suffix)))
                    return "请上传doc或者txt文件";


                //设置文件名信息,这个要先做
                session.setAttribute("filename", prefix);
                //设置文件格式
                session.setAttribute("format", "doc");


                String category = fileService.transfer(upload, suffix, session);

                //设置session 记录上传的是哪个文件
                //session.setAttribute("userUploadFile", location + "txt\\" + category + prefix + ".txt");

                //设置上传文件的种类信息
                //session.setAttribute("category", category);

                //转txt 顺便进一步文件分类
                if (suffix.equals("doc"))
                    flag = fileService.toTxt(location + "doc\\" + category + session.getAttribute("filename") + ".doc", session);

                System.out.println(originalFilename);
                System.out.println(session.getAttribute("userUploadFile"));
                System.out.println("上传成功！");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "上传失败！";
        }

        if (flag) {
            // return "上传成功！";
            session.setAttribute("static",false);//设为动态资源
            request.getRequestDispatcher("/download/view").forward(request, response);//转发
        } else
            return "上传失败！";
        return null;
    }


    /**
     * 保存在文本框里的信息为txt文件
     */
    @RequestMapping("/words")
    public String write(@RequestParam(value = "txt", defaultValue = "") String txt, HttpSession session) {
        if (txt.length() == 0)
            return "上传失败！";
        System.out.println(txt);
        String name = "文本";
        String newName = name;
        File f = new File(location + "txt\\else\\" + newName + ".txt");
        //这里传入的就直接覆盖掉
/*
        int i = 1;
        while ((f = new File(location + "txt\\else\\" + newName + ".txt")).exists()) {
            newName = name + "(" + i + ")";
            i++;
        }*/

        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f.getAbsolutePath())));
            bw.write(txt);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
            return "上传失败！";
        }


        //设置文件名信息
        session.setAttribute("filename", newName);
        //设置文件格式
        session.setAttribute("format", "txt");
        session.setAttribute("userUploadFile", f.getAbsolutePath());
        session.setAttribute("category", "else\\");
        session.setAttribute("static",false);//设为动态资源

        boolean success = fileService.transferTXT(session, true);

        if (success)
            return "上传成功！";
        else
            return "上传失败！";
    }
}
