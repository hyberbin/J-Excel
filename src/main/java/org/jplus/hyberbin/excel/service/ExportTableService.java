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

import java.util.Collection;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.jplus.hyberbin.excel.bean.CellBean;
import org.jplus.hyberbin.excel.bean.TableBean;
import org.jplus.hyberbin.excel.json.JsonUtil;
import org.jplus.hyberbin.excel.utils.ObjectHelper;

/**
 * 导出一个表格
 * Created by Hyberbin on 2014/6/18.
 */
public class ExportTableService extends BaseExcelService {
    private final  Sheet sheet;
    private final TableBean tableBean;

    public ExportTableService(Sheet sheet, TableBean tableBean) {
        this.sheet = sheet;
        this.tableBean = tableBean;
        ini();
    }

    /**
     * 初始化单元格
     */
    private void ini(){
        log.debug("初始化单元格总共:{}行，{}列",tableBean.getRowCount(),tableBean.getColumnCount());
        for(int r=0;r<tableBean.getRowCount();r++){
            Row row = sheet.createRow(r);
            if(tableBean.getRowHeight()!=null){
                row.setHeightInPoints(tableBean.getRowHeight());
            }
            for(int c=0;c<tableBean.getColumnCount();c++){
                row.createCell(c);
            }
        }
    }

    public void doExport(){
        Collection<CellBean> cellBeans = tableBean.getCellBeans();
        if(ObjectHelper.isNotEmpty(cellBeans)){
            for(CellBean cellBean:cellBeans){
                if(cellBean.getXSize()>1||cellBean.getYSize()>1){
                    log.debug("有合并单元格：{}", JsonUtil.toJSON(cellBean));
                    CellRangeAddress range=new CellRangeAddress(cellBean.getRowIndex(),cellBean.getRowIndex()+cellBean.getYSize()-1,cellBean.getColumnIndex(),cellBean.getColumnIndex()+cellBean.getXSize()-1);
                    sheet.addMergedRegion(range);
                }
                log.debug("set row:{},column:{},content:{}",cellBean.getRowIndex(),cellBean.getColumnIndex(),cellBean.getContent());
                Cell cell = sheet.getRow(cellBean.getRowIndex()).getCell(cellBean.getColumnIndex());
                cell.setCellValue(cellBean.getContent());
                CellStyle cellStyle = cell.getCellStyle();
                if(cellStyle==null){
                    cellStyle=sheet.getWorkbook().createCellStyle();
                }
                if(cellBean.isAlignCenter()){
                    cellStyle.setAlignment(CellStyle.ALIGN_CENTER);//水平居中
                }
                if(cellBean.isVerticalCenter()){
                    cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//垂直居中
                }
                cellStyle.setWrapText(cellBean.isWrapText());
                cell.setCellStyle(cellStyle);
            }
        }
    }
}
