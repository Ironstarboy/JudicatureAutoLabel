package DataSci.judicature.utils;

import DataSci.judicature.service.impl.PythonServiceImpl;

import java.io.BufferedReader;
import java.util.Scanner;

public class test {
    public static void main(String[] args) {
        PythonServiceImpl pythonService = new PythonServiceImpl();

        Scanner sc = new Scanner(System.in);
        String line;
        while (true) {
            line = sc.nextLine();
            String search = pythonService.search(line);
            System.out.println(search);
        }
    }
}
