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
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.jplus.hyberbin.excel.adapter.ICellReaderAdapter;
import org.jplus.hyberbin.excel.bean.CellBean;
import org.jplus.hyberbin.excel.bean.TableBean;
import org.jplus.hyberbin.excel.utils.ConverString;
import org.jplus.hyberbin.excel.utils.FieldUtils;
import org.jplus.hyberbin.excel.utils.ObjectHelper;
import org.jplus.hyberbin.excel.utils.Reflections;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 对规定格式的Excel进行导入.
 * 适配器由用户自己定义或者直接将常规类型的数据直接批量读取到数据库.
 * Created by hyberbin on 15/7/31.
 */
public class ImportTableService {
    private Sheet sheet;
    private TableBean tableBean;
    private ICellReaderAdapter defaultCellReaderAdapter;
    private Map<Integer,Integer> forceCellType=new HashMap();
    private String dateFormat="yyyy/MM/dd";

    public ImportTableService(Sheet sheet) {
        this.sheet = sheet;
    }

    public ImportTableService(Sheet sheet, ICellReaderAdapter defaultCellReaderAdapter) {
        this.sheet = sheet;
        this.defaultCellReaderAdapter=defaultCellReaderAdapter;
    }

    public void setCellType(int columnIndex,int type){
        forceCellType.put(columnIndex, type);
    }

    /**
     * 从Excel导入到TableBean
     */
    public void doImport(){
        int rowNum=sheet.getLastRowNum()+1;
        int columnNum=0;
        for(int i=0;i<rowNum;i++){
            if(sheet.getRow(i)!=null){
                int last=sheet.getRow(i).getLastCellNum();
                columnNum=last>columnNum?last:columnNum;
            }
        }
        tableBean=new TableBean(rowNum,columnNum);
        Collection<CellBean> cellBeans=new ArrayList<CellBean>();
        for(int r=0;r<rowNum;r++){
            Row row = sheet.getRow(r);
            if (row!=null){
                for(int c=0;c<row.getLastCellNum();c++){
                    Cell cell=row.getCell(c);
                    if(cell!=null){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        Integer type = forceCellType.get(c);
                        if(type !=null){
                            cell.setCellType(type);
                        }
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
                            if(DateUtil.isCellDateFormatted(cell)){
                                Date date2 = cell.getDateCellValue();
                                SimpleDateFormat dff = new SimpleDateFormat(dateFormat);
                                cellValue = dff.format(date2);   //日期转化
                            }else
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
        }
        tableBean.setCellBeans(cellBeans);
    }


    /**
     * 整个读取
     *
     * @return
     */
    public Object read() {
        if(tableBean==null){
            doImport();
        }
        return   defaultCellReaderAdapter.read(tableBean);
    }

    /**
     * 读取内容到List,可以是List<Map>也可以是List<Object>
     * @param sortedColumns 从第0列开始,数组中每个元素对应type中的一个字段,元素为空则忽略
     * @param type 读取数据存放的类型
     * @param <T> 泛型
     * @return
     */
    public <T>List<T> read(String[] sortedColumns,Class<? extends T> type){
        List<T> list=new ArrayList();
        for(int i=0;i<tableBean.getRowCount();i++){
            Object bean=Map.class.isAssignableFrom(type) ? new HashMap(): Reflections.instance(type.getName());
            for (int j = 0; j < sortedColumns.length; j++) {
                String column = sortedColumns[j];
                if(ObjectHelper.isNotEmpty(column)){
                    CellBean cellBean = tableBean.getCellBean(i, j);
                    if(cellBean!=null&& ObjectHelper.isNotEmpty(cellBean.getContent())){
                        if(bean instanceof Map){
                            FieldUtils.setFieldValue(bean, column, cellBean.getContent());
                        }else{
                            Field accessibleField = Reflections.getAccessibleField(bean, column);
                            Class<?> fieldType = accessibleField.getType();
                            Object value=cellBean.getContent();
                            if(!fieldType.equals(String.class)){
                                value= ConverString.asType(fieldType, cellBean.getContent());
                            }
                            FieldUtils.setFieldValue(bean,column,value);
                        }
                    }
                }
            }
            list.add((T) bean);
        }
        return list;
    }

    /**
     * 预读取,用于数据校验
     *
     * @param tableBean
     * @return
     */
    public void preRead(TableBean tableBean) {
        defaultCellReaderAdapter.preRead(tableBean);
    }


    public TableBean getTableBean() {
        if(tableBean==null){
            doImport();
        }
        return tableBean;
    }

    public String getDateFormat() {
        return dateFormat;
    }
}
