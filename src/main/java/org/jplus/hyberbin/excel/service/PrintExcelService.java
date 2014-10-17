/*
 * Copyright 2014 Hyberbin.
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

import java.util.List;
import org.apache.poi.ss.usermodel.Sheet;
import org.jplus.hyberbin.excel.annotation.ExcelPrinter;
import org.jplus.hyberbin.excel.annotation.Local;
import org.jplus.hyberbin.excel.bean.BaseExcelVo;
import org.jplus.hyberbin.excel.bean.FieldBean;
import org.jplus.hyberbin.excel.exception.AdapterException;
import org.jplus.hyberbin.excel.exception.ColumnErrorException;
import org.jplus.hyberbin.excel.utils.ExcelUtility;

/**
 * Created by Hyberbin on 14-1-21.
 * @param <T>
 */
public class PrintExcelService<T extends BaseExcelVo> extends ExportExcelService {
    protected final ExcelPrinter excelPrinter;

    public PrintExcelService(List<T> data, Sheet sheet) throws Exception {
        super(data, sheet, "");
        if (!voClass.isAnnotationPresent(ExcelPrinter.class)) {
            IllegalArgumentException exception = new IllegalArgumentException("打印模板VO必需要有@ExcelPrinter注解");
            log.error("打印模板VO必需要有@ExcelPrinter注解", exception);
            throw exception;
        } else {
            excelPrinter = (ExcelPrinter) voClass.getAnnotation(ExcelPrinter.class);
        }
    }

    @Override
    public PrintExcelService doExport() throws AdapterException, ColumnErrorException {
        int rowIndex = excelPrinter.startRow();
        for(int i=0;i<dataList.size();i++){
            Object t=dataList.get(i);
            writeOne((T)t,rowIndex,i!=dataList.size()-1);
            rowIndex+=  excelPrinter.endRow()-excelPrinter.startRow()+1+excelPrinter.spaceRow();
        }
        return this;
    }

    protected void writeOne(T t,int rowIndex,boolean copy) throws AdapterException, ColumnErrorException {
        List<FieldBean> filedList = dataBean.getFiledBeanList();
        for (FieldBean field : filedList) {
            if (field.getField().isAnnotationPresent(Local.class)) {
                Local local = field.getField().getAnnotation(Local.class);
                getSimpleField(field, t, getRow(sheet, rowIndex + local.row()), rowIndex + local.row(), local.column(), dataBean);
            }
        }
        if(copy)ExcelUtility.copyRows(sheet,excelPrinter.startRow(),excelPrinter.endRow(), excelPrinter.endRow()+1+rowIndex+excelPrinter.spaceRow());
    }
}
