package DataSci.judicature.service;

import DataSci.judicature.domain.CaseMsg;
import DataSci.judicature.domain.Words;

import javax.servlet.http.HttpSession;
import java.io.File;

/**
 * 和 python 脚本交互的接口
 */
public interface PythonService {


    /**
     * 相似文书推荐
     */
    String recommend(String filePath) throws Exception;

    /**
     * 摘要
     */
    String sentence(HttpSession session) throws Exception;

    /**
     * 动词和形容词
     */
    String vadj(HttpSession session) throws Exception;



}
