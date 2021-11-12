package DataSci.judicature.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 测试controller
 * 测试是否乱码
 * 是否能够正确跳转页面
 */
@Controller
@RequestMapping("/test")
public class testController {


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


}
