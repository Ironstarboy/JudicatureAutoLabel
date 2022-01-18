package DataSci.judicature.controller;

import DataSci.judicature.domain.CaseMarks;
import DataSci.judicature.domain.CaseMarksArr;
import DataSci.judicature.domain.CaseMsg;
import DataSci.judicature.service.FileService;
import DataSci.judicature.service.testService;
import DataSci.judicature.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

/**
 * 测试controller
 * localhost/test/xxx
 */
@Controller
@RequestMapping("/test")
public class testController {

    @Autowired
    private testService testService;

    @Autowired
    private FileService fileService;

    @Value("${PATH}")
    private String PATH;


    @Value("${spring.servlet.multipart.location}")
    private String location;

    @Autowired
    private FileUtil fileUtil;


    /**
     * 直接输出文字测试
     */
    @ResponseBody
    @RequestMapping("/hello")
    public String hello() {
        return "大作业！";
    }


    /**
     * 跳转静态页面测试
     */
    @RequestMapping(value = "/rec")
    public ModelAndView receive(String message, ModelAndView modelAndView) {
        modelAndView.addObject("username", "哈哈哈");
        System.out.println(message);
        modelAndView.setViewName("/login.html");
        return modelAndView;
    }

    /**
     * 转发测试
     */
    @RequestMapping(value = "/go")
    public ModelAndView go(ModelAndView modelAndView) {
        modelAndView.setViewName("forward:/test/hello");
        return modelAndView;
    }

    /**
     * 重定向测试
     */
    @RequestMapping(value = "/re")
    public ModelAndView redirect(ModelAndView modelAndView) {
        modelAndView.setViewName("redirect:/test/hello");
        return modelAndView;
    }

    /**
     * 返回json字符串测试
     */
    @RequestMapping("/json")
    @ResponseBody
    public CaseMsg jsonTest() {
        return new CaseMsg(
                "哈", "哈哈", "哈哈哈",
                "哈哈哈哈", "哈哈哈哈哈", "哈哈哈哈哈哈","hahahaahhahaha"
        );

    }

    /**
     * 返回json文件测试
     */
    @RequestMapping(value = "/jsonFile")
    @ResponseBody
    public CaseMarksArr jsonFileTest(HttpServletResponse response) {
/*
        response.reset();
        response.setContentType("application/octet-stream");// 设置强制下载不打开
        response.setCharacterEncoding("utf8");
        response.addHeader("Content-Disposition", "attachment;fileName=" + new String("标注.json".getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
*/
        String[] s = {"哈", "哈哈", "哈哈哈","哈哈哈哈哈","哈哈哈"};

        return new CaseMarksArr(
                new HashSet<>(Arrays.asList(s)),
                new HashSet<>(Arrays.asList(s)),
                new HashSet<>(Arrays.asList(s)),
                new HashSet<>(Arrays.asList(s)),
                new HashSet<>(Arrays.asList(s)),
                new HashSet<>(Arrays.asList(s))
        );

    }

    @RequestMapping("/download")
    @ResponseBody
    public String downloadFile(HttpServletResponse response) {
        String downloadFilePath = location + "txt\\中铁九局集团成都工程有限公司中诚信托有限责任公司信托纠纷民事申请再审审查民事裁定书.txt";//被下载的文件在服务器中的路径,
        String fileName = "中铁九局集团成都工程有限公司中诚信托有限责任公司信托纠纷民事申请再审审查民事裁定书.txt";//被下载文件的名称

        File file = new File(downloadFilePath);
        if (file.exists()) {

            response.reset();
            response.setContentType("application/octet-stream");// 设置强制下载不打开
            response.setCharacterEncoding("GBK");
            response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);

            try {
                fileUtil.download(downloadFilePath, response);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "下载成功！";
        }
        return "下载失败！";
    }


    /**
     * 调用python脚本测试
     */
    @RequestMapping("/py")
    @ResponseBody
    public void execPyTest() {

        String path = PATH + "spyder\\testbaidu.py";
        testService.testPy(path);
    }

    /**
     * 自动爬虫测试
     */
    @RequestMapping("/sele")
    @ResponseBody
    public void climb() {
        String path = PATH + "spyder\\1.py";
        testService.testPy(path);
    }

}
