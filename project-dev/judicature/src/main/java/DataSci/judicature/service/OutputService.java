package DataSci.judicature.service;

import DataSci.judicature.domain.CaseMsg;

import java.io.File;

/**
 * 输出文本接口
 * 生成案件文本 txt
 * 生成标注 json
 */
public interface OutputService {

    /**
     * 根据前端页面传来的词语生成案件信息
     */
    public CaseMsg proCaseMsg(String[] words);
}
