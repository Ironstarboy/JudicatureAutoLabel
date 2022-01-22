package DataSci.judicature.controller;

import DataSci.judicature.domain.CaseInfoSets;
import DataSci.judicature.domain.CaseMarks;
import DataSci.judicature.domain.CaseMarksArr;
import DataSci.judicature.domain.CaseMsg;
import DataSci.judicature.service.WordService;
import DataSci.judicature.utils.FileUtil;
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
    private FileUtil fileUtil;

    @Autowired
    private WordService wordService;

    /**
     * @return 返回案件信息的json文件
     */
    @RequestMapping("/json")
    public CaseMarks json(CaseMsg caseMsg, HttpServletResponse response,HttpSession session) {
        System.out.println("传进来了");
        System.out.println(caseMsg);


        response.reset();
        response.setContentType("application/octet-stream");// 设置强制下载不打开
        response.setCharacterEncoding("utf8");
        response.addHeader("Content-Disposition", "attachment;fileName=" + new String("标注.json".getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));

        CaseMarks marks = wordService.toJSON(caseMsg);
        session.setAttribute("cassMarks",marks);//加进session里
        return marks;
    }

    /**
     * @return 返回案件文本.txt
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
        System.out.println("开始下载了");
        return null;
    }

    /**
     * 案件文本前端展示
     */
    @RequestMapping(value = "/view", produces = "text/html;charset=utf-8")
    private String txtView(HttpServletResponse response, HttpSession session) {
        response.setCharacterEncoding("utf-8");
        String downloadFilePath = (String) session.getAttribute("userUploadFile");
        String name = (String) session.getAttribute("filename");
        String format = (String) session.getAttribute("format");

        if (downloadFilePath == null) {
            return "请先上传文书!";
        }
        File file = new File(downloadFilePath);
        if (file.exists()) {
            try {
                fileUtil.show(downloadFilePath, response, name, format);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 分词
     */
    @RequestMapping("/fenci")
    public CaseMarksArr fenci(HttpSession session) {
        System.out.println("开始分词了");
        String downloadFilePath = (String) session.getAttribute("userUploadFile");
        System.out.println(downloadFilePath);
        String type = (String) session.getAttribute("category");
        System.out.println(type);

        if (downloadFilePath == null) {//还没上传文件，session里没有记录
            return null;
        }

        CaseMarksArr caseSet;
        try {//分词 返回的是set类型的实体类
            caseSet = wordService.extract(downloadFilePath, session);
            session.setAttribute("caseSet",caseSet);//把分好词封装类的加入到session里
        } catch (IOException e) {
            return null;
        }

        System.out.println("结束分词了");
        System.out.println(caseSet);
        //criminals, gender, ethnicity, birthplace, accusation, courts
        return caseSet;
    }

    /**
     * 文书基本信息
     */
    @RequestMapping("/info")
    public CaseInfoSets baseInfo(HttpSession session) {
        System.out.println("基本信息开始");

        String downloadFilePath = (String) session.getAttribute("userUploadFile");
        System.out.println(downloadFilePath);
        String type = (String) session.getAttribute("category");
        System.out.println(type);

        if (downloadFilePath == null) {//还没上传文件，session里没有记录
            return null;
        }

        CaseInfoSets caseInfo;
        try {//分词 返回的是set类型的实体类
            caseInfo = wordService.proInfo(downloadFilePath, session);
            session.setAttribute("caseInfo",caseInfo);//把分好词封装类的加入到session里
        } catch (IOException e) {
            return null;
        }

        System.out.println("结束基本信息");
        System.out.println(caseInfo);
        //courts, type, accusation, procedure, date, criminals
        return caseInfo;
    }

}
