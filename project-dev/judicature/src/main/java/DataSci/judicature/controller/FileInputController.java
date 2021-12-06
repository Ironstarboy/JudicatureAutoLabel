package DataSci.judicature.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * 文件上传控制器
 * 用户传来的文件将会被保存在/resources/case/下
 */
@RestController
@RequestMapping("/file")
public class FileInputController {

    @RequestMapping("/save")
    public void save(MultipartFile upload) throws IOException {

        //获取上传文件的名称
        String originalFilename = upload.getOriginalFilename();

        //System.out.println(originalFilename);

        //文件转移到某个目录下
        if (originalFilename != null)
            upload.transferTo(new File(originalFilename));
    }
}
