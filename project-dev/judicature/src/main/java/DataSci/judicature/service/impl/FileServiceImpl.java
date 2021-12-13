package DataSci.judicature.service.impl;

import DataSci.judicature.domain.CaseMsg;
import DataSci.judicature.service.FileService;
import DataSci.judicature.utils.FileUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;


@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileUtil fileUtil;


    @Value("${spring.servlet.multipart.location}")
    private String location;


    //todo 生成案例信息的实现
    @Override
    public CaseMsg proCaseMsg(String[] words) {
        return null;
    }

    /**
     * 根据文件名分类，顺带转移
     */
    @Override
    public String transfer(String fileName) throws IOException {
        if (fileName != null) {
            String name = fileName.split("\\.")[0];
            String ver = name.substring(name.length() - 3);

            String type;

            if ("裁定书".equals(ver)) {
                type = "adjudication\\";
            } else if ("判决书".equals(ver)) {
                type = "judgment\\";
            } else if ("调解书".equals(ver)) {
                type = "mediate\\";
            } else if ("通知书".equals(ver)) {
                type = "notification\\";
            } else if ("决定书".equals(ver)) {
                type = "decision\\";
            } else {
                type = "else\\";
            }
            fileUtil.transfer(fileName, location + type);

            return type;
        }
        return null;
    }

}
