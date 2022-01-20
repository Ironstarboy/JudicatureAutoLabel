package DataSci.judicature.utils;

import DataSci.judicature.service.impl.PythonServiceImpl;

import java.io.*;
import java.util.Scanner;

public class test {
    public static void main(String[] args) throws IOException {
        PythonServiceImpl pythonService = new PythonServiceImpl();

        BufferedReader b = new BufferedReader(new InputStreamReader(System.in));
        String line;
        Process proc = Runtime.getRuntime().exec("python searchRecommend.py", null, new File("D:\\java\\DataSci\\lqf\\JudicatureAutoLabel\\nlp\\"));
        BufferedWriter br = new BufferedWriter(new OutputStreamWriter(proc.getOutputStream(), "GBK"));
        BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream(), "GBK"));

        while (true) {
            line = b.readLine();
            try {
                br.write(line + System.lineSeparator());
                br.flush();

                String msg = in.readLine();
                    System.out.println(msg);

            } catch (IOException /*| InterruptedException*/ e) {
                e.printStackTrace();
                br.close();
                in.close();
                System.out.println("IO异常");
                proc.destroy();
            }
        }
    }
}
