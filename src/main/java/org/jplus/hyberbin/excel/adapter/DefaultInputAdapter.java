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

import org.jplus.hyberbin.excel.utils.DicCodePool;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import org.apache.poi.ss.usermodel.Cell;
import org.jplus.hyberbin.excel.annotation.DefaultAdapterMethod;
import org.jplus.hyberbin.excel.annotation.input.InputDateConfig;
import org.jplus.hyberbin.excel.annotation.input.InputDicConfig;
import org.jplus.hyberbin.excel.annotation.input.InputIntConfig;
import org.jplus.hyberbin.excel.annotation.input.InputTextConfig;
import org.jplus.hyberbin.excel.annotation.output.OutputDateConfig;
import org.jplus.hyberbin.excel.bean.DataBean;
import org.jplus.hyberbin.excel.exception.AdapterException;
import org.jplus.hyberbin.excel.language.LanguageUtils;
import org.jplus.hyberbin.excel.utils.AdapterUtil;
import org.jplus.hyberbin.excel.utils.ConverString;
import org.jplus.hyberbin.excel.utils.DateUtil;
import org.jplus.hyberbin.excel.utils.DicCodePool;
import org.jplus.hyberbin.excel.utils.Message;
import org.jplus.hyberbin.excel.utils.NumberUtils;
import org.jplus.hyberbin.excel.utils.ObjectHelper;

/**
 * User: Hyberbin
 * Date: 13-12-3
 * Time: 上午11:02
 */
public class DefaultInputAdapter extends IOAdapter {

    public DefaultInputAdapter(DicCodePool dicCodePool) {
        super(dicCodePool);
    }
    /**
     * 默认的导入数据适配器
     *
     * @param cell
     * @throws IllegalAccessException
     */
    @DefaultAdapterMethod
    public Object defaultInputAdapter(DataBean dataBean, Class type,String fieldName, Cell cell) throws AdapterException {
        log.debug("in DefaultInputAdapter:defaultInputAdapter fieldName:{} type:{}",fieldName,type.getSimpleName());
        Object cellValue = getCellValue(cell, type);
        return asType(type, cellValue);
    }

    /**
     * 文本输入适配器
     * @param dataBean
     * @param type
     * @param fieldName
     * @param cell
     * @return
     * @throws AdapterException
     */
    public Object inputTextAdapter(DataBean dataBean, Class type,String fieldName, Cell cell) throws AdapterException {
        log.debug("in DefaultInputAdapter:inputTextAdapter fieldName:{} type:{}",fieldName,type.getSimpleName());
        String cellValue = asType(type,getCellValue(cell, type)).toString();
        if(ObjectHelper.isNullOrEmptyString(cellValue)) return cellValue;
        InputTextConfig annotation = dataBean.getInputConfig(fieldName);
        int maxLength = annotation.maxLength();
        int minLength = annotation.minLength();
        String regx = annotation.regx();
        if(maxLength !=0&&cellValue.length()> maxLength){
            log.error("超出了最大长度：{}>{}",cellValue.length(),maxLength);
             throw new AdapterException(LanguageUtils.translate(Message.LENGTH_MAX_ERROR,maxLength),cell);
        }
        if(minLength !=0&&cellValue.length()< minLength){
            log.error("小于最小长度：{}<{}",cellValue.length(),minLength);
            throw new AdapterException(LanguageUtils.translate(Message.LENGTH_MAX_ERROR,minLength),cell);
        }
        if(!ObjectHelper.isNullOrEmptyString(regx)){
             if(!cellValue.matches(regx)){
                log.error("值：{}，与正则表达式:{},不匹配",cellValue,regx);
                 throw new AdapterException(Message.REGX_ERROR,cell);
             }
        }
        return asType(type, cellValue);
    }

    /**
     * 文本输入适配器
     * @param dataBean
     * @param type
     * @param fieldName
     * @param cell
     * @return
     * @throws AdapterException
     */
    public Object inputIntAdapter(DataBean dataBean, Class type,String fieldName, Cell cell) throws AdapterException {
        log.debug("in DefaultInputAdapter:inputIntAdapter fieldName:{} type:{}",fieldName,type.getSimpleName());
        Object cellValue = asType(type,getCellValue(cell, type));
        if(cellValue==null) return null;
        Integer intValue=NumberUtils.parseInt(cellValue);
        InputIntConfig annotation = dataBean.getInputConfig(fieldName);
        int max = annotation.max();
        int min = annotation.min();
        if(intValue> max){
            log.error("超出了最大值：{}>{}",intValue,max);
            throw new AdapterException(LanguageUtils.translate(Message.VALUE_MAX_ERROR,max),cell);
        }
        if(intValue< min){
            log.error("小于最小值：{}<{}",intValue,min);
            throw new AdapterException(LanguageUtils.translate(Message.VALUE_MAX_ERROR,min),cell);
        }
        return asType(type, cellValue);
    }
    /**
     * 字典输入适配器
     *
     * @param type
     * @param fieldName
     * @param cell
     * @throws IllegalAccessException
     */
    public Object inputDicCodeAdapter(DataBean dataBean, Class type, String fieldName, Cell cell) throws AdapterException {
        log.debug("in DefaultInputAdapter:inputDicCodeAdapter fieldName:{} type:{}",fieldName,type.getSimpleName());
        InputDicConfig annotation = dataBean.getInputConfig(fieldName);
        String dicCode = annotation.dicCode();
        String cellValue = getCellValue(cell, type).toString();
        if(ObjectHelper.isNullOrEmptyString(cellValue))return null;
        String byValue = dicCodePool.getByValue(dicCode, cellValue);
        if(byValue==null&& AdapterUtil.getAllMatch(annotation))throw new AdapterException(Message.DIC_ERROR,cell);
        return asType(type,byValue);
    }

    /**
     * 时间导入适配器
     *
     * @param type
     * @param fieldName
     * @param cell
     * @throws IllegalAccessException
     * @throws ParseException
     */
    public Object inputDateAdapter(DataBean dataBean, Class type, String fieldName, Cell cell) throws AdapterException {
        log.debug("in DefaultInputAdapter:inputDateAdapter fieldName:{} type:{}",fieldName,type.getSimpleName());
        InputDateConfig inputDateConfig = dataBean.getInputConfig(fieldName);
        Object o=null;
        if(Cell.CELL_TYPE_BLANK==cell.getCellType()){
            log.debug("cell is blank ");
            return o;
        }else  if (Date.class.isAssignableFrom(type)) {
            if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
                Date date=cell.getDateCellValue();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int i = calendar.get(Calendar.YEAR);
                try { //日期格式也是数字格式这里判断用户输入的到底是不是2014026这种格式
                    o =i>2500? DateUtil.formatToDate(String.format("%.0f", cell.getNumericCellValue()), inputDateConfig.format()):date;
                } catch (ParseException e) {
                    throw new AdapterException(fieldName, Message.DATE_TYPE_ERROR,cell);
                }
            } else if (Cell.CELL_TYPE_STRING == cell.getCellType()) {
                try {
                    o = DateUtil.formatToDate(trim(cell.getStringCellValue()), inputDateConfig.format());
                } catch (ParseException e) {
                    throw new AdapterException(fieldName, Message.DATE_TYPE_ERROR,cell);
                }
            }else if(Cell.CELL_TYPE_BLANK==cell.getCellType()){
                 return null;
            }else {
                throw new AdapterException(fieldName,Message.DATE_TYPE_ERROR,cell);
            }
        } else if (String.class.isAssignableFrom(type)) {
            if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
                Date dateCellValue = cell.getDateCellValue();
                o= DateUtil.format(dateCellValue, inputDateConfig.format());
            } else if (Cell.CELL_TYPE_STRING == cell.getCellType()) {
                OutputDateConfig outputDateConfig = dataBean.getOutputConfig(fieldName);
                try {
                    o= DateUtil.format(trim(cell.getStringCellValue()), outputDateConfig.format(), inputDateConfig.format());
                } catch (ParseException e) {
                    throw new AdapterException(fieldName,Message.DATE_TYPE_ERROR,cell);
                }
            } else {
                throw new AdapterException(fieldName,Message.DATE_TYPE_ERROR,cell);
            }
        } else {
            throw new AdapterException(fieldName,Message.DATE_TYPE_ERROR,cell);
        }
        return o;
    }


    public static Object asType(Class type, Object value) {
        if (value != null && !value.getClass().equals(type)) {
            value = ConverString.asType(type, value.toString(), null);
        }
        return value;
    }

    public static Object getCellValue(Cell cell, Class type) throws AdapterException {
        if (cell == null) return "";
        try {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    return cell.getBooleanCellValue();
                case Cell.CELL_TYPE_BLANK:
                    return "";
                case Cell.CELL_TYPE_NUMERIC:
                    if(String.class.isAssignableFrom(type)){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        return trim(cell.getStringCellValue());
                    }else if (Date.class.isAssignableFrom(type)) {
                        return cell.getDateCellValue();
                    }
                    return cell.getNumericCellValue();
                case Cell.CELL_TYPE_STRING:
                    return trim(cell.getStringCellValue());
                case Cell.CELL_TYPE_FORMULA:
                    try {
                        return String.valueOf(cell.getNumericCellValue());
                    } catch (IllegalStateException e) {
                        return trim(String.valueOf(cell.getRichStringCellValue()));
                    }
            }
        } catch (Exception e) {
            throw new AdapterException(Message.INPUT_CELL_DATA_ERROR, cell);
        }
        throw new AdapterException(Message.INPUT_CELL_DATA_ERROR, cell);
    }

    private static String trim(String str){
        return ObjectHelper.isNullOrEmptyString(str)?"":str.trim();
    }
}
