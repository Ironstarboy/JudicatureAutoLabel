package DataSci.judicature.service.impl;

import DataSci.judicature.domain.CaseMsg;
import DataSci.judicature.service.FileService;
import DataSci.judicature.utils.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.charset.StandardCharsets;


@Service("service1")
public class FileServiceImpl implements FileService {

    @Autowired
    private FileUtil fileUtil;


    @Value("${spring.servlet.multipart.location}")
    private String location;

    /**
     * 根据文件名分类，顺带转移
     * 返回文书类型
     */
    @Override
    public String transfer(MultipartFile upload, String suffix, HttpSession session) throws IOException {
        String name = (String) session.getAttribute("filename");

        if (!upload.isEmpty() && name != null) {
            String ver = name.substring(Math.max(name.length() - 3, 0));

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
            } else if ("支付令".equals(ver)) {
                type = "order\\";
            } else {
                type = "else\\";
            }

            int i = 1;
            String newName = name;
            File f;
            while ((f = new File(location + suffix + "\\" + type + newName + "." + suffix)).exists()) {//文件名已存在
                newName = name + "(" + i + ")";
                i++;
            }
            upload.transferTo(f);
            session.setAttribute("filename", newName);
            session.setAttribute("userUploadFile", location + "txt\\" + type + newName + ".txt");
            session.setAttribute("category", type);

            return type;
        }
        return null;
    }

    /**
     * word 转TXT
     *
     * @param filepath 文件路径
     * @return
     */
    @Override
    public boolean toTxt(String filepath, HttpSession session) {
        String txtPath = (String) session.getAttribute("userUploadFile");


        fileUtil.word2Txt(filepath, txtPath);

        boolean flag;
        flag = transferTXT(session, false);

        return flag;
    }

    @Override
    public boolean transferTXT(HttpSession session, boolean isUTF8) {
        boolean flag = false;
        String category = (String) session.getAttribute("category");
        String txtPath = (String) session.getAttribute("userUploadFile");

        //是其他 进一步分类
        if (category.startsWith("else")) {
            String prefix = (String) session.getAttribute("filename");

            try {
                BufferedReader br;
                if (!isUTF8)
                    br = new BufferedReader(new InputStreamReader(new FileInputStream(txtPath), "GBK"));
                else
                    br = new BufferedReader(new InputStreamReader(new FileInputStream(txtPath), StandardCharsets.UTF_8));

                String line;
                int index = 1;
                while ((line = br.readLine()) != null && index < 6) {
                    String[] words = line.split(" ");
                    line = StringUtils.join(words, "");
                    if (line.contains("裁定书")) {
                        session.setAttribute("category", "adjudication\\");
                        session.setAttribute("userUploadFile", location + "txt\\" + "adjudication\\" + prefix + ".txt");
                        flag = true;
                        break;
                    } else if (line.contains("决定书")) {
                        session.setAttribute("category", "decision\\");
                        session.setAttribute("userUploadFile", location + "txt\\" + "decision\\" + prefix + ".txt");
                        flag = true;
                        break;
                    } else if (line.contains("判决书")) {
                        session.setAttribute("category", "judgment\\");
                        session.setAttribute("userUploadFile", location + "txt\\" + "judgment\\" + prefix + ".txt");
                        flag = true;
                        break;
                    } else if (line.contains("调解书")) {
                        session.setAttribute("category", "mediate\\");
                        session.setAttribute("userUploadFile", location + "txt\\" + "mediate\\" + prefix + ".txt");
                        flag = true;
                        break;
                    } else if (line.contains("通知书")) {
                        session.setAttribute("category", "notification\\");
                        session.setAttribute("userUploadFile", location + "txt\\" + "notification\\" + prefix + ".txt");
                        flag = true;
                        break;
                    } else if (line.contains("支付令")) {
                        session.setAttribute("category", "order\\");
                        session.setAttribute("userUploadFile", location + "txt\\" + "order\\" + prefix + ".txt");
                        flag = true;
                        break;
                    } else {//还是无法识别，就不跳转了
                    }

                    index++;
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        //能识别出来，转移文件
        if (flag) {
            try {
                fileUtil.transfer(txtPath, (String) session.getAttribute("userUploadFile"));
                return new File(txtPath).delete();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }


}
