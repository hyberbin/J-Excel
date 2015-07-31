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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.jplus.hyberbin.excel.adapter.ICellReaderAdapter;
import org.jplus.hyberbin.excel.bean.CellBean;
import org.jplus.hyberbin.excel.bean.TableBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * 导入纵表到对象中.
 * 每个单元格都必须要有一个适配器读取数据.
 * 适配器由用户自己定义.当然,可以批量设置适配器.
 * Created by hyberbin on 15/7/31.
 */
public class ImportTableService {
    private Sheet sheet;
    private TableBean tableBean;
    private ICellReaderAdapter[][] iCellReaderAdapters;

    public ImportTableService(Sheet sheet) {
        this.sheet = sheet;
    }

    /**
     * 从Excel导入到TableBean
     */
    public void doImport(){
        int rowNum=sheet.getLastRowNum()+1;
        int columnNum=0;
        for(int i=0;i<rowNum;i++){
            int last=sheet.getRow(i).getLastCellNum();
            columnNum=last>columnNum?last:columnNum;
        }
        iCellReaderAdapters=new ICellReaderAdapter[rowNum+1][columnNum+1];
        tableBean=new TableBean(rowNum,columnNum);
        Collection<CellBean> cellBeans=new ArrayList<CellBean>();
        for(int r=0;r<rowNum;r++){
            Row row = sheet.getRow(r);
            for(int c=0;c<row.getLastCellNum();c++){
                Cell cell=row.getCell(c);
                if(cell!=null){
                    String cellValue=null;
                    if(Cell.CELL_TYPE_BOOLEAN==cell.getCellType()){
                        cellValue=cell.getBooleanCellValue()+"";
                    }else if(Cell.CELL_TYPE_FORMULA==cell.getCellType()){
                        try {
                            cellValue= String.valueOf(cell.getNumericCellValue());
                        } catch (IllegalStateException e) {
                            cellValue= String.valueOf(cell.getRichStringCellValue()).trim();
                        }
                    }else if(Cell.CELL_TYPE_NUMERIC==cell.getCellType()){
                        cellValue= String.valueOf(cell.getNumericCellValue());
                    }else if(Cell.CELL_TYPE_STRING==cell.getCellType()){
                        cellValue=cell.getStringCellValue();
                    }
                    CellBean cellBean=new CellBean(cellValue,r,c);
                    cellBean.setCell(cell);
                    cellBeans.add(cellBean);
                }
            }
        }
        tableBean.setCellBeans(cellBeans);
    }

    /**
     * 执行数据读取.
     */
    public void doRead(){
        if(tableBean==null){
            doImport();
        }
        for(CellBean cellBean:tableBean.getCellBeans()){
            iCellReaderAdapters[cellBean.getRowIndex()][cellBean.getColumnIndex()].read(cellBean);
        }
    }

    /**
     * 批量设置读取器,该读取器将应用到所有单元格.
     * @param cellReaderAdapter
     */
    public void setDefaultReader(ICellReaderAdapter cellReaderAdapter){
        Arrays.fill(iCellReaderAdapters, cellReaderAdapter);
    }

    /**
     * 给指定行号列号的单元格设置读取器.
     * @param readerAdapter
     * @param row
     * @param column
     */
    public void setReader(ICellReaderAdapter readerAdapter,int row,int column){
        iCellReaderAdapters[row][column]=readerAdapter;
    }

    public TableBean getTableBean() {
        return tableBean;
    }
}
