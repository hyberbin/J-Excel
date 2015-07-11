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
package org.jplus.hyberbin.excel.adapter;

import java.text.ParseException;
import java.util.Date;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Workbook;
import org.jplus.hyberbin.excel.annotation.DefaultAdapterMethod;
import org.jplus.hyberbin.excel.annotation.input.InputDateConfig;
import org.jplus.hyberbin.excel.annotation.output.OutputDateConfig;
import org.jplus.hyberbin.excel.annotation.output.OutputDicConfig;
import org.jplus.hyberbin.excel.annotation.output.OutputNumericConfig;
import org.jplus.hyberbin.excel.bean.DataBean;
import org.jplus.hyberbin.excel.exception.AdapterException;
import org.jplus.hyberbin.excel.utils.AdapterUtil;
import org.jplus.hyberbin.excel.utils.DateUtil;
import org.jplus.hyberbin.excel.utils.DicCodePool;
import org.jplus.hyberbin.excel.utils.Message;
import org.jplus.hyberbin.excel.utils.NumberUtils;
import org.jplus.hyberbin.excel.utils.ObjectHelper;

/**
 * User: Hyberbin
 * Date: 13-12-3
 * Time: 上午10:27
 */
public class DefaultOutputAdapter extends IOAdapter {

    public DefaultOutputAdapter(DicCodePool dicCodePool) {
        super(dicCodePool);
    }

    /**
     * 默认的字典输出适配器
     * @param fieldValue
     * @param fieldName
     * @param cell
     * @throws AdapterException
     */
    public void outputDicCodeAdapter(DataBean dataBean, Object fieldValue, String fieldName,Cell cell) throws AdapterException {
        log.debug("in DefaultOutputAdapter:outputDicCodeAdapter fieldName:{} fieldValue:{}",fieldName,fieldValue);
        OutputDicConfig config =dataBean.getOutputConfig(fieldName);
        String dicCode = config.dicCode();
        if(fieldValue==null){
            log.debug("fieldValue is null return");
            cell.setCellValue("");
            return;
        }else{
            String byKey = dicCodePool.getByKey(dicCode, fieldValue.toString());
            if(byKey==null){
                if(AdapterUtil.getAllMatch(config)){
                    throw new AdapterException(Message.DIC_ERROR,cell);
                }else{
                    cell.setCellValue(fieldValue.toString());
                }
            }else{
                cell.setCellValue(byKey);
            }
        }
    }
    /**
     * 导出时间适配器
     *
     * @param fieldValue
     * @param fieldName
     * @return
     * @throws AdapterException
     */
    public void outputDateAdapter(DataBean dataBean, Object fieldValue, String fieldName,Cell cell) throws AdapterException {
        log.debug("in DefaultOutputAdapter:outputDateAdapter fieldName:{} fieldValue:{}",fieldName,fieldValue);
        Date date=null;
        if(fieldValue==null){
            log.debug("fieldValue is null return");
            cell.setCellValue("");
            return;
        }else if (fieldValue instanceof Date) {
            log.debug("fieldValue instanceof Date ");
            date=(Date)fieldValue;
        } else if (fieldValue instanceof String) {
            log.debug("fieldValue instanceof String ");
            InputDateConfig config = dataBean.getInputConfig(fieldName);
            try {
                date= DateUtil.formatToDate((String) fieldValue, config.format());
            } catch (ParseException e) {
                throw new AdapterException(fieldName, Message.DATE_TYPE_ERROR,cell);
            }
        } else if (fieldValue instanceof Long) {
            log.debug("fieldValue instanceof Long ");
            date=new Date((Long)fieldValue);
        } else {
            throw new AdapterException(fieldName,Message.DATE_TYPE_ERROR,cell);
        }
        Workbook workbook = cell.getSheet().getWorkbook();
        OutputDateConfig outputConfig = dataBean.getOutputConfig(fieldName);
        CellStyle cellStyle = cell.getCellStyle();
        if(cellStyle==null)cellStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat(outputConfig.format()));
        cell.setCellStyle(cellStyle);
        cell.setCellValue(date);
    }

    public void outputNumericAdapter(DataBean dataBean, Object fieldValue, String fieldName,Cell cell) throws AdapterException {
        log.debug("in DefaultOutputAdapter:outputNumericAdapter fieldName:{} fieldValue:{}",fieldName,fieldValue);
        if(ObjectHelper.isNullOrEmptyString(fieldValue)) return;
        OutputNumericConfig config=dataBean.getOutputConfig(fieldName);
        Workbook workbook = cell.getSheet().getWorkbook();
        CellStyle cellStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        StringBuilder format=new StringBuilder("0");
        for(int i=0;i<config.floatCount();i++){
            if(i==0)format.append(".");
            format.append("0");
        }
        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat(format.toString()));
        cell.setCellValue(NumberUtils.format(fieldValue,config.floatCount()));
        cell.setCellStyle(cellStyle);
    }

    public void outputIntAdapter(DataBean dataBean, Object fieldValue, String fieldName,Cell cell) throws AdapterException {
        log.debug("in DefaultOutputAdapter:outputIntAdapter fieldName:{} fieldValue:{}",fieldName,fieldValue);
        if(ObjectHelper.isNullOrEmptyString(fieldValue)) return;
        Workbook workbook = cell.getSheet().getWorkbook();
        CellStyle cellStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("#"));
        cell.setCellValue(NumberUtils.format(fieldValue,0));
        cell.setCellStyle(cellStyle);
    }


    /**
     * 默认的导出适配器
     *
     * @param fieldValue
     * @param fieldName
     * @return
     * @throws IllegalAccessException
     */
    @DefaultAdapterMethod
    public void defaultOutputAdapter(DataBean dataBean, Object fieldValue, String fieldName,Cell cell) throws AdapterException {
        log.debug("in DefaultOutputAdapter:defaultOutputAdapter fieldName:{} fieldValue:{}",fieldName,fieldValue);
        cell.setCellValue(fieldValue == null ? "" : fieldValue.toString());
    }

}
