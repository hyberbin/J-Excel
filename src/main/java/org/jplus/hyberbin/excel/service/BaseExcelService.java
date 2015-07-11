/*
 * Copyright 2015 www.hyberbin.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * Email:hyberbin@qq.com
 */
package org.jplus.hyberbin.excel.service;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.jplus.hyberbin.excel.utils.ObjectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Hyberbin
 * Date: 13-12-3
 * Time: 下午4:55
 */
public class BaseExcelService {
    protected Logger log = LoggerFactory.getLogger(getClass());
    /**存放Hash密码的行*/
    public final static int HASH_ROW=0;
    /**标题行*/
    public final static int TITLE_ROW=1;
    /**隐藏字段行*/
    public final static int HIDDEN_FIELD_HEAD = TITLE_ROW+1;
    /**表头列名*/
    public final static int COLUMN_ROW = HIDDEN_FIELD_HEAD+1;
    /**数据开始行*/
    public final static int START_ROW = COLUMN_ROW+1;

    /**
     * 判断一行内容是否为空
     * @param sheetRow
     * @param length
     * @return
     */
    public static boolean isEmpty(Row sheetRow, int length) {
        if(sheetRow!=null){
            for (int i = 0; i < length; i++) {
                Cell cell = sheetRow.getCell(i);
                if (!isEmpty(cell)) {
                    return false;
                }
            }
        }
        return true;
    }

    /***
     * 将本次导出数据的hash密码写在Excel上
     * @param sheet
     * @param hashCode
     */
    protected  static void setHashVal(Sheet sheet,long hashCode){
        Row sheetRow = sheet.getRow(HASH_ROW);
        Cell cell = sheetRow.createCell(0);
        cell.setCellValue(hashCode);
        sheetRow.setHeight(Short.valueOf("0"));
    }

    /**
     * 读取模板中的hash密码
     * @param sheet
     * @return
     */
    protected  static long getHashVal(Sheet sheet){
        Row sheetRow = sheet.getRow(HASH_ROW);
        Cell cell = sheetRow.getCell(0);
        return ((Double)cell.getNumericCellValue()).longValue();
    }

    /**
     * 给单元格设置红色错误背景
     * @param cell
     */
    protected static void setErrorStyle(Cell cell){
        if(cell!=null){
            CellStyle newstyle = cell.getSheet().getWorkbook().createCellStyle();
            CellStyle style = cell.getCellStyle();
            if(style!=null){
                newstyle.cloneStyleFrom(style);
            }
            newstyle.setFillForegroundColor(IndexedColors.RED.getIndex());
            newstyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            cell.setCellStyle(newstyle);
        }
    }

   

    /**
     * 判断一个单元格是否为空
     * @param cell
     * @return
     */
    public static boolean isEmpty(Cell cell) {
        if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
            if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                if (!ObjectHelper.isNullOrEmptyString(cell.getStringCellValue())) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * 给sheet加上标题 居中对齐
     * @param sheet
     * @param row
     * @param length
     * @param data
     */
    public static void addTitle(Sheet sheet,int row,int length,String data){
        Row sheetRow = sheet.createRow(row);
        for(int i=0;i<length;i++){
            sheetRow.createCell(i);
        }
        CellStyle style = sheet.getWorkbook().createCellStyle(); // 样式对象
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
        CellRangeAddress cellRangeAddress = new CellRangeAddress(row, row, 0, length - 1);
        sheet.addMergedRegion(cellRangeAddress);
        Cell cell = sheetRow.getCell(0);
        cell.setCellStyle(style);
        cell.setCellValue(data);
    }

    /**
     * 添加一行数据
     * @param sheet
     * @param row
     * @param data
     * @return
     */
    public static Row addRow(Sheet sheet,int row,String[] data){
        Row sheetRow = sheet.createRow(row);
        CellStyle style = sheet.getWorkbook().createCellStyle(); // 样式对象
        style.setWrapText(true);
        for(int i=0;i<data.length;i++){
            Cell cell = sheetRow.createCell(i);
            cell.setCellValue(data[i]);
            cell.setCellStyle(style);
        }
        return sheetRow;
    }

    /**
     * 初始化一行
     * @param sheet
     * @param row
     * @param length
     * @return
     */
    public static Row createRow(Sheet sheet,int row,int length){
        Row sheetRow = sheet.createRow(row);
        for(int i=0;i<length;i++){
            sheetRow.createCell(i);
        }
        return sheetRow;
    }

    /**
     * 获取一个单元格
     * @param sheet
     * @param row
     * @param col
     * @return
     */
    public static Cell getCell(Sheet sheet, int row, int col) {
        return sheet.getRow(row).getCell(col);
    }

    /**
     * 获取一个单元格
     * @param sheetRow
     * @param col
     * @return
     */
    public static Cell getCell(Row sheetRow, int col) {
        Cell cell = sheetRow.getCell(col);
        if(cell==null){
            cell=sheetRow.createCell(col);
        }
        return cell;
    }

    /**
     * 获取一行
     * @param sheet
     * @param row
     * @return
     */
    public static Row getRow(Sheet sheet, int row) {
        return sheet.getRow(row);
    }

    /**
     * 获取单元格中的文本内容
     * @param cell
     * @return
     */
    public static String getString(Cell cell) {
        return cell.getStringCellValue();
    }

    /**
     * 创建一个文件
     * @return
     */
    public static Workbook createWorkbook(){
        return new HSSFWorkbook();
    }

    /**
     * 创建一个工作表
     * @param workbook
     * @param name
     * @return
     */
    public static Sheet createSheet(Workbook workbook,String name){
        return workbook.createSheet(name);
    }
    
    /**
     * 获取一个工作表
     * @param workbook
     * @param index
     * @return
     */
    public static Sheet getSheet(Workbook workbook,int  index){
        return workbook.getSheetAt(index);
    }
    
}
