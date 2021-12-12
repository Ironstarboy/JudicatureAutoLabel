package DataSci.judicature.service.impl;

import DataSci.judicature.domain.CaseMsg;
import DataSci.judicature.service.FileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.*;


@Service
public class FileServiceImpl implements FileService {



    //todo 生成案例信息的实现
    @Override
    public CaseMsg proCaseMsg(String[] words) {
        return null;
    }

    /**
     * 根据文件名分类
     */
    @Override
    public String transfer(String fileName){
        if (fileName!=null){
            String name = fileName.split("\\.")[0];
            String ver = name.substring(name.length() - 3);
            if ("裁定书".equals(ver)) {
                return "adjudication\\";
            } else if ("判决书".equals(ver)) {
                return "judgment\\";
            } else if ("调解书".equals(ver)) {
                return "mediate\\";
            } else if ("通知书".equals(ver)) {
                return "notification\\";
            } else if ("决定书".equals(ver)) {
                return "decision\\";
            } else {
                return "else\\";
            }
        }
        return null;
    }

}
