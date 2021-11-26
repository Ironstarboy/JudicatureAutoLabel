package DataSci.judicature.controller;

import DataSci.judicature.domain.CaseMsg;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文件输出控制器
 * 输出案件.txt
 * 分词.json
 */
@RestController
@RequestMapping("/out")
public class FileOutPutController {


    /**
     * @return 返回案件信息的json文件
     * todo service层逻辑没实现，未确定python脚本的传入参数
     */
    @RequestMapping("/json")
    public CaseMsg json() {
        return null;
    }
}
