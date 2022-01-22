package DataSci.judicature.service.impl;

import DataSci.judicature.service.ExcelService;
import DataSci.judicature.utils.ExcelUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ExcelServiceImpl implements ExcelService {


    @Value("${PATH}")
    private String PATH;


    @Override
    public String proRecommend(String fileName) {
        String res = "";

        String path1 = PATH + "nlp\\相似文章路径.xlsx";

        String path2 = PATH + "nlp\\相似文章得分.xlsx";

        ExcelUtil nameSheet = new ExcelUtil(path1, "Sheet1");
        ExcelUtil rateSheet = new ExcelUtil(path2, "Sheet1");

        int r = nameSheet.caseNamePos(fileName, 1);

        //['adjudication/史生来谭承天建设工程施工合同纠纷再审审查与审判监督民事裁定书.txt', 'adjudication/陕西宽建实业有限公司中铁二十局集团第六工程有限公司建设工程施工合同纠纷再审审查与审判监督民事裁定书.txt', 'adjudication/陈明贵州建工集团第八建筑工程有限责任公司等建设工程施工合同纠纷民事申请再审审查民事裁定书.txt', 'adjudication/欧珠伦珠建设工程施工合同纠纷再审审查与审判监督民事裁定书.txt', 'adjudication/涧县华阳鸿基置地有限责任公司湖南鹏华装饰设计工程有限责任公司等建设工程施工合同纠纷其他民事民事裁定.txt', 'adjudication/王玲对杨新宏申请支付令.txt']
        String nameStr = nameSheet.getCell(r, 2);
        String rateStr = rateSheet.getCell(r, 2);

        String[] nameList = cleanName(nameStr);
        String[] rateList = cleanRate(rateStr);

        String[] answerList = new String[nameList.length];

        for (int i = 0; i < nameList.length; i++) {
            answerList[i] = nameList[i] +" "+ rateList[i];
        }

        res= StringUtils.join(answerList,",");
        return res;
    }

    private String[] cleanName(String str) {
        str = str.substring(1, str.length() - 1);
        String[] res = str.split(",");
        String[] ans = new String[res.length - 1];
        for (int i = 1; i < res.length; i++) {
            int srt = res[i].lastIndexOf("/");
            int end = res[i].indexOf('.');
            ans[i - 1] = res[i].substring(srt + 1, end);
        }
        return ans;
    }

    private String[] cleanRate(String str) {
        //[0.5868907, 0.29708967, 0.19692193, 0.19065535, 0.17666051, 0.17015891]
        str = str.substring(1, str.length() - 1);
        String[] res = str.split(",");
        String[] ans = new String[res.length - 1];
        for (int i = 1; i < res.length; i++) {
            String d = res[i];
            double rate = Double.parseDouble(d);
            rate *= 100;//十倍根号法调整相似度值
            rate = Math.round(10 * Math.sqrt(rate));
            ans[i - 1] = (int)rate + "%";
        }
        return ans;
    }
}
