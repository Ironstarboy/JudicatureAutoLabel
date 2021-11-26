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
     * 根据案件文本，生成 json 标注
     *
     * @param f 案件文本
     * @return json 字符串
     */
    String FileToJson(File f);
}
