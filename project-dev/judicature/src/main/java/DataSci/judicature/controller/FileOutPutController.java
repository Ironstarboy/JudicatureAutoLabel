package DataSci.judicature.controller;

import DataSci.judicature.domain.CaseInfoSets;
import DataSci.judicature.domain.CaseMarks;
import DataSci.judicature.domain.CaseMarksArr;
import DataSci.judicature.domain.CaseMsg;
import DataSci.judicature.service.PythonService;
import DataSci.judicature.service.SearchService;
import DataSci.judicature.service.SpyderService;
import DataSci.judicature.service.WordService;
import DataSci.judicature.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

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

    @Autowired
    private PythonService pythonService;

    @Autowired
    private SpyderService spyderService;

    @Autowired
    private SearchService searchService;

    /**
     * @return 返回案件信息的json文件
     */
    @RequestMapping("/json")
    public CaseMarks json(CaseMsg caseMsg, HttpServletResponse response, HttpSession session) {
        System.out.println("传进来了");
        System.out.println(caseMsg);


        response.reset();
        response.setContentType("application/octet-stream");// 设置强制下载不打开
        response.setCharacterEncoding("utf8");
        response.addHeader("Content-Disposition", "attachment;fileName=" + new String("标注.json".getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));

        CaseMarks marks = wordService.toJSON(caseMsg);
        session.setAttribute("cassMarks", marks);//加进session里
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

        if (downloadFilePath == null) {
            return "请先上传文书!";
        }
        File file = new File(downloadFilePath);
        if (file.exists()) {
            try {
                fileUtil.show(downloadFilePath, response, name);
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
            session.setAttribute("caseSet", caseSet);//把分好词封装类的加入到session里
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
            session.setAttribute("caseInfo", caseInfo);//把分好词封装类的加入到session里
        } catch (IOException e) {
            return null;
        }

        System.out.println("结束基本信息");
        System.out.println(caseInfo);
        //courts, type, accusation, procedure, date, criminals
        return caseInfo;
    }

    /**
     * 相似案例推荐
     */
    @RequestMapping("/recommend")
    public String caseRecommend(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        String downloadFilePath = (String) session.getAttribute("userUploadFile");
        System.out.println(downloadFilePath);
        String type = (String) session.getAttribute("category");
        System.out.println(type);

        if (downloadFilePath == null) {//还没上传文件，session里没有记录
            return null;
        }

        if ((boolean) session.getAttribute("static")) {//静态资源 直接跳转
            try {
                request.getRequestDispatcher("/static/recommend").forward(request, response);//转发
                return null;
            } catch (ServletException | IOException e) {
                return "返回失败！";
            }
        }

        String recommend;
        try {
            recommend = pythonService.recommend(downloadFilePath);
        } catch (Exception e) {
            e.printStackTrace();
            return "返回失败！";
        }

        return recommend;
    }

    /**
     * 爬虫
     */
    @RequestMapping("/spyder")
    public String spyder(String start, String end, int num, HttpSession session, HttpServletResponse response) {


        response.reset();
        response.setContentType("application/octet-stream");// 设置强制下载不打开
        response.setCharacterEncoding("utf8");
        response.addHeader("Content-Disposition", "attachment;fileName=" + new String("爬取文书.zip".getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));


        String tag = new Date().getTime() + "";
        session.setAttribute("tag", tag);
        String path;
        try {
            path = spyderService.spyder(start, end, num, tag);
        } catch (Exception e) {
            e.printStackTrace();
            response.reset();
            return "当前网络拥堵，请稍后再试!";
        }

        File file = new File(path);
        if (file.exists()) {
            try {
                fileUtil.download(path, response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * 自动摘要
     */
    @RequestMapping(value = "/sentence", produces = "text/html;charset=utf-8")
    public String sentence(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        response.setCharacterEncoding("utf-8");
        String downloadFilePath = (String) session.getAttribute("userUploadFile");
        System.out.println(downloadFilePath);
        String type = (String) session.getAttribute("category");
        System.out.println(type);

        if (downloadFilePath == null) {//还没上传文件，session里没有记录
            return null;
        }
        if ((boolean) session.getAttribute("static")) {//静态资源 直接跳转
            try {
                request.getRequestDispatcher("/static/sentence").forward(request, response);//转发
                return null;
            } catch (ServletException | IOException e) {
                return "返回失败！";
            }
        }
        String sentence = null;
        try {
            sentence = pythonService.sentence(session);
        } catch (Exception e) {
            e.printStackTrace();
            return "分析失败！";
        }

        //这边返回的是一个字符串，和案件文本展示一样
        return sentence;
    }

    /**
     * 搜索推荐实现
     */
    @RequestMapping("/search")
    public String search(String name, String time) {
        System.out.println(name);
        System.out.println(time);
        //return "文件名1,文件名2,文件名3";
        return searchService.search(name);
    }

    /**
     * 动词，形容词
     */
    @RequestMapping("/vadj")
    public String vadj(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        String downloadFilePath = (String) session.getAttribute("userUploadFile");
        System.out.println(downloadFilePath);
        String type = (String) session.getAttribute("category");
        System.out.println(type);

        if (downloadFilePath == null) {//还没上传文件，session里没有记录
            return null;
        }

        if ((boolean) session.getAttribute("static")) {//静态资源 直接跳转
            try {
                request.getRequestDispatcher("/static/vadj").forward(request, response);//转发
                return null;
            } catch (ServletException | IOException e) {
                return "返回失败！";
            }
        }

        String vadj;
        try {
            vadj = pythonService.vadj(session);

        } catch (Exception e) {
            e.printStackTrace();
            return "返回失败！";
        }
        return vadj;
    }
}
