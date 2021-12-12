package DataSci.judicature.utils.impl;

import DataSci.judicature.utils.FileUtil;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * 文件操作工具类
 * <p>
 * 解压
 * 压缩
 * doc 转 txt
 */
@Component
public class FileUtilImpl implements FileUtil {

    /**
     * 文件解压
     */
    @Override
    public void zipUncompress(String inputFile, String destDirPath) throws Exception {
        File srcFile = new File(inputFile);//获取当前压缩文件
        // 判断源文件是否存在
        if (!srcFile.exists()) {
            throw new Exception(srcFile.getPath() + "所指文件不存在");
        }
        ZipFile zipFile = new ZipFile(srcFile, Charset.forName(System.getProperty("sun.jnu.encoding")));//创建压缩文件对象
        //开始解压
        Enumeration<?> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            // 如果是文件夹，就创建个文件夹
            if (entry.isDirectory()) {
                String dirPath = destDirPath + "\\" + entry.getName();
                srcFile.mkdirs();
            } else {
                // 如果是文件，就先创建一个文件，然后用io流把内容copy过去
                File targetFile = new File(destDirPath + "\\" + entry.getName());
                // 保证这个文件的父文件夹必须要存在
                if (!targetFile.getParentFile().exists()) {
                    targetFile.getParentFile().mkdirs();
                }
                targetFile.createNewFile();
                // 将压缩文件内容写入到这个文件中
                InputStream is = zipFile.getInputStream(entry);
                FileOutputStream fos = new FileOutputStream(targetFile);
                int len;
                byte[] buf = new byte[1024];
                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                }
                // 关流顺序，先打开的后关闭
                fos.close();
                is.close();
            }
        }
    }

    /**
     * zip文件压缩
     *
     * @param inputFile  待压缩文件夹/文件名
     * @param outputFile 生成的压缩包名字
     */
    @Override
    public void ZipCompress(String inputFile, String outputFile) throws Exception {
        //创建zip输出流
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outputFile));
        //创建缓冲输出流
        BufferedOutputStream bos = new BufferedOutputStream(out);
        File input = new File(inputFile);
        compress(out, bos, input, null);
        bos.close();
        out.close();
    }

    /**
     * @param name 压缩文件名，可以写为null保持默认
     */
    //递归压缩
    public void compress(ZipOutputStream out, BufferedOutputStream bos, File input, String name) throws IOException {
        if (name == null) {
            name = input.getName();
        }
        //如果路径为目录（文件夹）
        if (input.isDirectory()) {
            //取出文件夹中的文件（或子文件夹）
            File[] flist = input.listFiles();

            if (flist == null || flist.length == 0) {//如果文件夹为空，则只需在目的地zip文件中写入一个目录进入

                out.putNextEntry(new ZipEntry(name + "/"));
            } else {//如果文件夹不为空，则递归调用compress，文件夹中的每一个文件（或文件夹）进行压缩

                for (File file : flist) {
                    compress(out, bos, file, name + "/" + file.getName());
                }
            }
        } else {//如果不是目录（文件夹），即为文件，则先写入目录进入点，之后将文件写入zip文件中
            out.putNextEntry(new ZipEntry(name));
            FileInputStream fos = new FileInputStream(input);
            BufferedInputStream bis = new BufferedInputStream(fos);
            int len;
            //将源文件写入到zip文件中
            byte[] buf = new byte[1024];
            while ((len = bis.read(buf)) != -1) {
                bos.write(buf, 0, len);
            }
            bis.close();
            fos.close();
        }
    }

    /**
     * 目录下所有文件 转 txt
     *
     * @param wordPath word文件的路径
     * @param txtPath  txt文件的路径
     */
    @Override
    public void word2Txt(String wordPath, String txtPath) {

        File file = new File(wordPath);

        if (file.isDirectory()) {
            String[] list = file.list();

            if (list != null)
                for (String doc : list) {
                    if (new File(wordPath + doc).isFile()) {//是文件才操作
                        String txt = doc.split("\\.")[0] + ".txt";
                        if (!new File(txtPath + txt).exists())
                            word2txt(wordPath + doc, txtPath + txt, 7);
                    }
                }
        } else
            word2txt(wordPath, txtPath, 7);//单个文件就直接转
    }

    /**
     * 单个doc转txt
     *
     * @param format 转换格式 7为txt格式， 8保存为html格式
     */
    private void word2txt(String wordPath, String txtPath, int format) {
        ActiveXComponent app = new ActiveXComponent("Word.Application");
        app.setProperty("Visible", new Variant(false));
        Dispatch doc1 = app.getProperty("Documents").toDispatch();
        Dispatch doc2 = Dispatch.invoke(
                doc1,
                "Open",
                Dispatch.Method,
                new Object[]{wordPath, new Variant(false), new Variant(true)},
                new int[1]
        ).toDispatch();
        Dispatch.invoke(
                doc2,
                "SaveAs",
                Dispatch.Method,
                new Object[]{txtPath, new Variant(format)//7为txt格式， 8保存为html格式
                },
                new int[1]
        );
        Variant f = new Variant(false);
        Dispatch.call(doc2, "Close", f);
    }

    /**
     * 文件下载
     *
     * @param filename
     */
    @Override
    public void download(String filename, HttpServletResponse res) throws IOException {
        // 发送给客户端的数据
        OutputStream outputStream = res.getOutputStream();
        byte[] buff = new byte[1024];
        BufferedInputStream bis;
        // 读取filename
        bis = new BufferedInputStream(new FileInputStream(filename));
        int i = bis.read(buff);
        while (i != -1) {
            outputStream.write(buff, 0, i);
            outputStream.flush();
            i = bis.read(buff);
        }
    }


    /**
     * 转移文件
     * 根据文件性质转移到不同的目录下
     */
    public void transfer(String dirPath) throws IOException {
        File dir = new File(dirPath);
        File[] files = dir.listFiles();


        assert files != null;
        for (File file : files) {
            if (!file.isDirectory()) {
                String name = file.getName().split("\\.")[0];
                String ver = name.substring(name.length() - 3);
                if ("裁定书".equals(ver)) {
                    readAndWrite(file, dirPath + "adjudication\\" + file.getName());
                } else if ("判决书".equals(ver)) {
                    readAndWrite(file, dirPath + "judgment\\" + file.getName());
                } else if ("调解书".equals(ver)) {
                    readAndWrite(file, dirPath + "mediate\\" + file.getName());
                } else if ("通知书".equals(ver)) {
                    readAndWrite(file, dirPath + "notification\\" + file.getName());
                } else if ("决定书".equals(ver)) {
                    readAndWrite(file, dirPath + "decision\\" + file.getName());
                } else {
                    readAndWrite(file, dirPath + "else\\" + file.getName());
                }
            }
        }
    }


    /**
     * 处理 其他
     */
    public void dealWithElse(String dirPath) throws IOException {
        File dir = new File(dirPath);
        File[] files = dir.listFiles();


        assert files != null;
        for (File file : files) {
            if (!file.isDirectory()) {
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));

            }
        }
    }

    /**
     * 读写操作
     *
     * @param file 需要剪切的文件
     * @param path 对应的地址
     */
    private void readAndWrite(File file, String path) throws IOException {
        byte[] buff = new byte[1024];
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path));

        int i = bis.read(buff);
        while (i != -1) {
            bos.write(buff, 0, i);
            bos.flush();
            i = bis.read(buff);
        }

        bos.close();
        bis.close();
    }

}
