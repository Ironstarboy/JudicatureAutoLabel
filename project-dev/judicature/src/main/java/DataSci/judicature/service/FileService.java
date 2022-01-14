package DataSci.judicature.service;

import DataSci.judicature.domain.CaseMsg;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

/**
 * 处理文件操作接口
 * 生成案件文本 txt
 * 生成标注 json
 * 解压
 */
public interface FileService {

    /**
     * 根据前端页面传来的词语生成案件信息
     */
    public CaseMsg proCaseMsg(String[] words);

    public String transfer(MultipartFile upload, String name) throws IOException;

    public boolean toTxt(String fileName, HttpSession session);

}
