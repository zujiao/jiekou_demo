package hainan_upload;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

public class java_load_excel {
    public static void main(String[] args) throws IOException {
        FileInputStream is = new FileInputStream("C:\\Users\\范伟\\Desktop\\哔哩哔哩.csv");
        XSSFWorkbook workbook = new XSSFWorkbook(is);
        //读取Sheet
        Sheet sheet = workbook.getSheetAt(0);
        Row row = sheet.getRow(0);
        //获取最大行数
        int rownum = sheet.getPhysicalNumberOfRows();
        //获取最大列数
        int colnum = row.getPhysicalNumberOfCells();
        int aa=0;
        for (int i = 0; i < rownum; i++) {
            aa+=1;
            //获取第i行数据
            row = sheet.getRow(i);
            for (int j = 0; j < colnum; j++) {
                Cell cell = row.getCell(j);
                if(cell!=null && cell.toString()!=""){
                    cell.setCellValue(cell.toString());
                    String cellText = cell.getStringCellValue();
                    System.out.print(cellText + ",");
                }else{
                    System.out.print("null" + ",");
                }

            }
            System.out.println();
        }
        System.out.println(aa);
    }
}
