package DataSci.judicature.utils;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Excel 工具类
 */
public class ExcelUtil {
    private XSSFSheet sheet;

    /**
     * 构造函数，初始化excel数据
     *
     * @param filePath  excel路径
     * @param sheetName sheet表名
     */
    public ExcelUtil(String filePath, String sheetName) {
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(filePath);
            XSSFWorkbook sheets = new XSSFWorkbook(fileInputStream);
            //获取sheet
            sheet = sheets.getSheet(sheetName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据某一列的某一行行名，获取该行对应的列名
     * @param caseName 特定的行名
     * @param rowNum 该行名在的列数
     * @param newRowNum 目标列数
     * @return 目标列的内容
     */
    public String getCase(String caseName, int rowNum, int newRowNum) {
        int c = caseNamePos(caseName, rowNum);
        return getCell(c,newRowNum);
    }

    /**
     * 根据行号，获取某一列的值
     *
     * @param c 行号r
     * @param r  列号
     * @return
     */
    public String getCell(int c, int r) {
        XSSFRow row = sheet.getRow(c);
        if (row.getCell(r)!=null)
            return  row.getCell(r).toString();
        return null;
    }

    /**
     * 查询某一列的的某个特定值
     *
     * @param caseName 特定值
     * @param rowNum   查的是第几列
     * @return 返回行号
     */
    public int caseNamePos(String caseName, int rowNum) {
        //获取行数
        int rows = sheet.getPhysicalNumberOfRows();
        for (int i = 0; i < rows; i++) {//遍历每一行
            XSSFRow row = sheet.getRow(i);//第几行
            if (row.getCell(rowNum) == null)
                continue;
            String cell = row.getCell(rowNum).toString();
            if (cell.contains(caseName)) {
                return i;
            }
        }
        return -1;
    }

    //打印excel数据
    public void readExcelData() {
        //获取行数
        int rows = sheet.getLastRowNum();
        for (int i = 0; i < rows; i++) {
            //获取列数
            Row row = sheet.getRow(i);
            int columns = row.getLastCellNum();
            int srt = row.getFirstCellNum();
            for (int j = srt; j < columns; j++) {
                String cell = row.getCell(j).toString();
                System.out.println(cell);
            }
        }
    }

    //测试方法
    public static void main(String[] args) throws IOException {
        ExcelUtil sheet1 = new ExcelUtil("D:\\java\\DataSci\\lqf\\JudicatureAutoLabel\\nlp\\相似文章路径.xlsx", "Sheet1");

        System.out.println();

        int i = sheet1.caseNamePos("川天府银行股份有限公司成都锦江支行四川千业环保产业发展有限公司金融借款合同纠纷再审审查与审判监督民", 1);
        System.out.println(i);

        String cell = sheet1.getCell(i, 2);
        System.out.println(cell);


   /* }
        int rows = sheet.getLastRowNum();
        System.out.println(rows);
        for(int i=0;i<rows;i++){
            //获取列数
            Row row = sheet.getRow(i);
            int columns = row.getLastCellNum();
            int srt = row.getFirstCellNum();
            *//*System.out.println(columns);
            System.out.println(srt);*//*
            for(int j=srt;j<columns;j++){

                String cell = row.getCell(j).toString();
                System.out.println(cell);
            }
        }*/

    }
}

