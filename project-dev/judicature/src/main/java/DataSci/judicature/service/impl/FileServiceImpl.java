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



}
