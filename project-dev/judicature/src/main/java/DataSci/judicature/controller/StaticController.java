package DataSci.judicature.controller;

import DataSci.judicature.service.ExcelService;
import DataSci.judicature.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 本地资源库的展示类
 */
@Controller
@RequestMapping("/static")
public class StaticController {


    @Value("${spring.servlet.multipart.location}")
    private String location;

    @Value("${PATH}")
    private String PATH;

    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private ExcelService  excelService;


    /**
     * 跳转指令全部发到这里
     */
    @RequestMapping("/init")
    public ModelAndView init(String fileName, HttpSession session, ModelAndView modelAndView) {
        session.setAttribute("static", true);//设为静态资源
        String name = fileName + ".txt";

        //递归查找文件
        File dir = new File(location + "\\txt\\");
        if (getFile(dir, session, name, fileName)) {
            modelAndView.setViewName("forward:/static/view");
        } else {
            modelAndView.addObject("查询失败！本地库里没有该文件!");
        }
        return modelAndView;
    }

    private boolean getFile(File dir, HttpSession session, String name, String fileName) {
        for (File sonDir : Objects.requireNonNull(dir.listFiles())) {
            for (File son : Objects.requireNonNull(sonDir.listFiles())) {
                if (son.getName().equals(name)) {
                    session.setAttribute("filename", fileName);
                    session.setAttribute("userUploadFile", son.getAbsolutePath());
                    session.setAttribute("category", sonDir.getName() + "\\");
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 前端视图展示
     * http://localhost/static/init?fileName=%E4%BB%BB%E6%85%A7%E6%B8%8A%E4%B8%8E%E8%B5%B5%E6%B6%9B%E4%B8%AD%E5%8D%8E%E8%81%94%E5%90%88%E8%B4%A2%E4%BA%A7%E4%BF%9D%E9%99%A9%E8%82%A1%E4%BB%BD%E6%9C%89%E9%99%90%E5%85%AC%E5%8F%B8%E4%BF%9D%E5%AE%9A%E4%B8%AD%E5%BF%83%E6%94%AF%E5%85%AC%E5%8F%B8%E6%9C%BA%E5%8A%A8%E8%BD%A6%E4%BA%A4%E9%80%9A%E4%BA%8B%E6%95%85%E8%B4%A3%E4%BB%BB%E7%BA%A0%E7%BA%B7%E4%B8%80%E5%AE%A1%E6%B0%91%E4%BA%8B%E5%88%A4%E5%86%B3%E4%B9%A6
     */
    @ResponseBody
    @RequestMapping(value = "/view", produces = "text/html;charset=utf-8")
    public void view(HttpSession session,  HttpServletResponse response) {
        response.setCharacterEncoding("UTF8");
        StringBuilder sb = new StringBuilder();
        String encoding = "GBK";
        try {
            String path = (String) session.getAttribute("userUploadFile");
            BufferedReader br;
            encoding = fileUtil.getEncoding(new File(path));
            br = new BufferedReader(new InputStreamReader(new FileInputStream(path), encoding));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                 response.getWriter().write("加载失败！");
            } catch (Exception ioException) {
                ioException.printStackTrace();
            }
        }
        try {
            response.getWriter().write(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 相似案例推荐
     */
    @RequestMapping("/recommend")
    public String caseRecommend(HttpSession session) {
        String downloadFilePath = (String) session.getAttribute("userUploadFile");
        System.out.println(downloadFilePath);
        String filename = (String) session.getAttribute("filename");
        System.out.println(filename);

        //就先不遵守代码规范了
        if (downloadFilePath == null) {//还没上传文件，session里没有记录
            return null;
        }

        //陕西宽建实业有限公司中铁二十局集团第六工程有限公司建设工程施工合同纠纷再审审查与审判监督民事裁定书 39%,陈明贵州建工集团第八建筑工程有限责任公司等建设工程施工合同纠纷民事申请再审审查民事裁定书 36%,欧珠伦珠建设工程施工合同纠纷再审审查与审判监督民事裁定书 36%,涧县华阳鸿基置地有限责任公司湖南鹏华装饰设计工程有限责任公司等建设工程施工合同纠纷其他民事民事裁定 35%,王玲对杨新宏申请支付令 34%
        return excelService.proRecommend(filename);
        //文书名1 xx%,文书名2 xx%,文书名3 xx%,文书名4 xx%,文书名5 xx%
    }

    /**
     * 自动摘要
     */
    @ResponseBody
    @RequestMapping(value = "/sentence", produces = "text/html;charset=utf-8")
    public String sentence(HttpSession session, HttpServletResponse response) {
        String downloadFilePath = (String) session.getAttribute("userUploadFile");
        String fileName = (String) session.getAttribute("filename");
        System.out.println(downloadFilePath);
        String type = (String) session.getAttribute("category");
        System.out.println(type);

        if (downloadFilePath == null) {//还没上传文件，session里没有记录
            return null;
        }

        File f = new File(PATH + "nlp\\summarise\\" + type + fileName + ".txt");
        try {
            fileUtil.showWithoutTitle(f.getAbsolutePath(), response);
        } catch (IOException e) {
            e.printStackTrace();
            return "展示失败！";
        }
        return null;
    }

    /**
     * 动词，形容词
     */
    @RequestMapping("/vadj")
    public String vadj(HttpSession session) {
        String downloadFilePath = (String) session.getAttribute("userUploadFile");
        String fileName = (String) session.getAttribute("filename");
        System.out.println(downloadFilePath);
        String type = (String) session.getAttribute("category");
        System.out.println(type);

        if (downloadFilePath == null) {//还没上传文件，session里没有记录
            return null;
        }
        StringBuilder sb = new StringBuilder();

        String line;
        File f = new File(PATH + "nlp\\词性标注\\" + type + fileName + ".json");
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            while ((line = br.readLine()) != null) {
                if (!line.contains("fileName"))
                    sb.append(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "返回失败！";
        }
        return sb.toString();

        //{
        //    "动词": "裁定,发生,执行,下有,冻结,负责",
        //    "形容词": "悦"
        //}
    }
}