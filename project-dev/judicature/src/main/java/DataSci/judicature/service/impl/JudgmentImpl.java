package DataSci.judicature.service.impl;

import DataSci.judicature.domain.CaseMarks;
import DataSci.judicature.domain.CaseMsg;
import DataSci.judicature.service.WordService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * 针对判决书的关键字提取
 */
@Service
public class JudgmentImpl implements WordService {

    @Value("${spring.servlet.multipart.location}")
    private String location;


    @Override
    public void extract(String fileName) throws IOException {



        BufferedReader br = new BufferedReader(new FileReader(location + "txt\\" + "白福贵嫩江市白云乡人民政府合同纠纷民事申请再审审查民事裁定书.txt"));

        CaseMarks caseMarks = new CaseMarks();

        String line;
        int i = 1;
        while ((line = br.readLine()) != null) {
            if (i==4){

            }


            i++;
        }


    }

    private CaseMarks pro(CaseMarks caseMarks,String line){
        //再审申请人（一审原告、二审上诉人）：白福贵，男，1962年10月21日出生，汉族，住黑龙江省北安市。



        return caseMarks;
    }
}
