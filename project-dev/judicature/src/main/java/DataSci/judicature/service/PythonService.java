package DataSci.judicature.service;

import DataSci.judicature.domain.CaseMsg;
import DataSci.judicature.domain.Words;

import java.io.File;

/**
 * 和 python 脚本交互的接口
 */
public interface PythonService {

    /**
     * 分词
     */
    String recommend(String filePath) throws Exception;

    String sentence(String filePath) throws Exception;



}
