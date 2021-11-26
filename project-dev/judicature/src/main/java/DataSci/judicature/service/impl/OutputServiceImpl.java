package DataSci.judicature.service.impl;

import DataSci.judicature.domain.CaseMsg;
import DataSci.judicature.service.OutputService;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class OutputServiceImpl implements OutputService {

    @Override
    public String FileToJson(File f) {
        CaseMsg Case = msgToDomain(f);
        String json = domainToJson(Case);
        return json;
    }

    /**
     * todo
     * 传入案件文档，生成案件实体类
     *
     * @param f 案件文档
     * @return 案件实体类
     */
    CaseMsg msgToDomain(File f) {
        return null;
    }

    /**
     * todo
     * 传入案件实体类，生成对应的json文件
     *
     * @param caseMsg 案件实体类
     * @return 对应的json文件
     */
    String domainToJson(CaseMsg caseMsg) {
        return null;
    }


}
