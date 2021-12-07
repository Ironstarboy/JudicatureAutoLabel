package DataSci.judicature.controller;

import DataSci.judicature.domain.CaseMsg;
import DataSci.judicature.service.testService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 测试controller
 * localhost/test/xxx
 */
@Controller
@RequestMapping("/test")
public class testController {

    @Autowired
    private testService testService;

    @Value("${PATH}")
    private String PATH;


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
                "哈哈哈哈", "哈哈哈哈哈", "哈哈哈哈哈哈"
        );

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
        String path = "D:\\java\\DataSci\\JudicatureAutoLabel\\spyder\\1.py";
        testService.testPy(path);
    }

}
