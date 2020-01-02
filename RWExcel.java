package utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

/**
 * POI解析Excel
 */
public class RWExcel {
    /**
     * 根据fileType不同读取excel文件
     *
     * @param path 文件路径
     * @param requridIndex 必填列下标，0开始
     * @throws IOException
     */
    public static LinkedHashMap<String,ArrayList<List<String>>> readExcel(String path,int requridIndex) {
        LinkedHashMap<String,ArrayList<List<String>>> sheetMap = new LinkedHashMap<>();;
        // return a list contains many list
        ArrayList<List<String>> lists;
        //读取excel文件
        InputStream is = null;
        try {
            is = new FileInputStream(path);
            //获取工作薄
            Workbook wb = null;
            if (path.endsWith("xls")) {
                wb = new HSSFWorkbook(is);
            } else if (path.endsWith("xlsx")) {
                wb = new XSSFWorkbook(is);
            } else {
                return null;
            }
            int numberOfSheets = wb.getNumberOfSheets();
            for (int i = 0; i < numberOfSheets; i++) {
                // return a list contains many list
                lists = new ArrayList<List<String>>();
                //读取每一个工作页sheet
                Sheet sheet = wb.getSheetAt(i);
                String sheetName = sheet.getSheetName();
                //第一行为标题
                for (Row row : sheet) {
                    //得到该列必填单元格内容是否为空字符串 如果是则为空行
                    if(row==null){
                        continue;
                    }
                    Cell c = row.getCell(requridIndex);
                    if(c==null) {
                        continue;
                    }else {
                        c.setCellType(Cell.CELL_TYPE_STRING);
                        if(c.getStringCellValue().trim().equals("")){
                            continue;
                        }
                     }
                    ArrayList<String> list = new ArrayList<String>();
                    for (Cell cell : row) {
                        //根据不同类型转化成字符串
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        list.add(cell.getStringCellValue());
                    }
                    lists.add(list);
                }
                sheetMap.put(sheetName,lists);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sheetMap;
    }
    /**
    * 将工作簿输出到文件
    *@param wb 需要生成的工作簿
    *@param path 需要生成的目标文件路径
    */
    public static void createWbToFile(Workbook wb, String path){
        FileOutputStream fileOut= null;
        try {
            fileOut = new FileOutputStream(path);
            wb.write(fileOut);
            System.out.println("----------------------------------");
            System.out.println("已将文件成功输出到: "+path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (fileOut != null) fileOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建Excel.xls
     * @param wb 工作簿
     * @param lists 需要写入xls的数据
     * @param titles 列标题
     * @param sheetName 工作表名
     * @return 工作簿
     * @throws IOException
     */
    public static Workbook creatExcel(Workbook wb,List<List<String>> lists, List<String> titles, String sheetName) throws IOException {
        Sheet sheet = null;
        if (titles!=null){
            // 创建第一个sheet（页），并命名
            sheet = wb.createSheet(sheetName);
            // 手动设置列宽。第一个参数表示要为第几列设；，第二个参数表示列的宽度，n为列高的像素数。
            for(int i=0;i<titles.size();i++){
                sheet.setColumnWidth((short) i, (short) (35.7 * 150));
            }
            // 创建第一行作为列名
            Row row = sheet.createRow((short) 0);
            // 创建列名单元格格式
            CellStyle cs = wb.createCellStyle();
            // 创建列名字体
            Font f = wb.createFont();
            // 创建列名字体样式（用于列名）
            f.setFontHeightInPoints((short) 10);
            f.setColor(IndexedColors.BLACK.getIndex());
            f.setBoldweight(Font.BOLDWEIGHT_BOLD);
            // 设置用于列名单元格的样式
            cs.setFont(f);
            cs.setBorderLeft(CellStyle.BORDER_THIN);
            cs.setBorderRight(CellStyle.BORDER_THIN);
            cs.setBorderTop(CellStyle.BORDER_THIN);
            cs.setBorderBottom(CellStyle.BORDER_THIN);
            cs.setAlignment(CellStyle.ALIGN_CENTER);
            //设置列名
            for(int i=0;i<titles.size();i++){
                Cell cell = row.createCell(i);
                cell.setCellValue(titles.get(i));
                cell.setCellStyle(cs);
            }
        }else {
            if(lists == null || lists.size() == 0){
                return wb;
            }
            sheet = wb.getSheet(sheetName);
            // 创建数据内容单元格格式
            CellStyle cs2 = wb.createCellStyle();
            // 创建数据内容字体
            Font f2 = wb.createFont();
            // 创建第二种字体样式（用于值）
            f2.setFontHeightInPoints((short) 10);
            f2.setColor(IndexedColors.BLACK.getIndex());
            // 设置第二种单元格的样式（用于值）
            cs2.setFont(f2);
            cs2.setBorderLeft(CellStyle.BORDER_THIN);
            cs2.setBorderRight(CellStyle.BORDER_THIN);
            cs2.setBorderTop(CellStyle.BORDER_THIN);
            cs2.setBorderBottom(CellStyle.BORDER_THIN);
            cs2.setAlignment(CellStyle.ALIGN_CENTER);
            //设置每行每列的值
            for (int i = 0; i < lists.size(); i++) {
                // Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
                // 创建新行，在页sheet上
                List<String> vRow = lists.get(i);
                String name = vRow.get(2);
                System.out.println(name);
                int lastRowNum = sheet.getLastRowNum();
                Row row = sheet.createRow(lastRowNum+1);
                for(short j=0;j<vRow.size();j++){
                    // 在row行上创建一个方格
                    Cell cell = row.createCell(j);
                    cell.setCellValue(lists.get(i).get(j));
                    cell.setCellStyle(cs2);
                }
            }
        }
        return wb;
    }
}