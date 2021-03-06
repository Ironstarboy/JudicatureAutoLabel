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

    public String transfer(MultipartFile upload, String suffix, HttpSession session) throws IOException;

    public boolean toTxt(String fileName, HttpSession session);

    public boolean transferTXT(HttpSession session, boolean flag);

}
