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

import java.lang.annotation.Annotation;
import java.util.Set;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.jplus.hyberbin.excel.annotation.validate.DateValidateConfig;
import org.jplus.hyberbin.excel.annotation.validate.DicValidateConfig;
import org.jplus.hyberbin.excel.annotation.validate.IntValidateConfig;
import org.jplus.hyberbin.excel.annotation.validate.NumericValidateConfig;
import org.jplus.hyberbin.excel.annotation.validate.TextValidateConfig;
import org.jplus.hyberbin.excel.bean.DataBean;
import org.jplus.hyberbin.excel.language.LanguageUtils;
import org.jplus.hyberbin.excel.service.BaseExcelService;
import org.jplus.hyberbin.excel.utils.AdapterUtil;
import org.jplus.hyberbin.excel.utils.DicCodePool;
import org.jplus.hyberbin.excel.utils.Message;
import org.jplus.hyberbin.excel.utils.ObjectHelper;

/**
 * User: Hyberbin
 * Date: 13-12-3
 * Time: 上午11:02
 */
public class DefaultValidateAdapter extends IOAdapter{
    private final static String DICCODE_SHEET_NAME="diccodeSheet";

    public DefaultValidateAdapter( DicCodePool dicCodePool) {
        super(dicCodePool);
    }

    /**
     * 数据有效性自定义序列适配器
     * @param dataBean
     * @param sheet
     * @param columnIndex
     * @param filedName
     */
    public void DicCodeValidateAdapter(DataBean dataBean, Sheet sheet, int columnIndex, String filedName) {
        DicValidateConfig config = dataBean.getValidateConfig(filedName);
        String dicCode = config.dicCode();
        Set<String> set = dicCodePool.getDicValueSet().get(dicCode);
        if (ObjectHelper.isEmpty(set)) {
            log.error("找不到字典：{}", dicCode);
            return;
        }
        if (config.columnName()!=0) {//表示是引用的形式
            createDicCodeSheet(config, sheet, columnIndex, set);
        } else {
            String[] strings = set.toArray(new String[]{});
            DVConstraint constraint = DVConstraint.createExplicitListConstraint(strings);
            // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
            CellRangeAddressList regions = new CellRangeAddressList(BaseExcelService.START_ROW, Short.MAX_VALUE, columnIndex, columnIndex);
            // 数据有效性对象
            HSSFDataValidation data_validation_list = new HSSFDataValidation(regions, constraint);
            setValidationTip(data_validation_list,config);
            sheet.addValidationData(data_validation_list);
        }
    }

    /**
     * 设置输入提示
     * @param validation
     * @param config
     */
    protected void  setValidationTip(DataValidation validation,Annotation config){
        String tipLangName = AdapterUtil.getTipLangName(config);
        if(!ObjectHelper.isNullOrEmptyString(tipLangName)){
            validation.createPromptBox(LanguageUtils.translate(Message.TIP), LanguageUtils.translate(tipLangName));
        }
    }

    /**
     * 用引用的方式添加码表
     * @param config
     * @param sheet
     * @param columnIndex
     * @param valueSet
     */
    protected void createDicCodeSheet(DicValidateConfig config,Sheet sheet,int columnIndex,Set<String> valueSet){
        Workbook workbook = sheet.getWorkbook();
        Sheet codeSheet = workbook.getSheet(DICCODE_SHEET_NAME);
        if(codeSheet==null){
            log.debug("没有码表Sheet创建码表Sheet");
            codeSheet=workbook.createSheet(DICCODE_SHEET_NAME);
        }
        int codeIndex=config.columnName()-'A';
        log.debug("codeIndex：{}",codeIndex);
        if (codeSheet.getRow(0) == null || codeSheet.getRow(0).getCell(codeIndex) == null) {
            log.debug("没有当前码表，填写码表数据");
            int i = 0;
            for (String dic : valueSet) {
                Row row =codeSheet.getRow(i);
                if(row==null)row=codeSheet.createRow(i);
                Cell cell = row.createCell(codeIndex);
                cell.setCellValue(dic);
                i++;
            }
        }else{
            log.debug("码表数据已经存在，直接读取");
        }
        Name name = workbook.getName(config.columnName()+"");
        if(name==null||name.isDeleted()){
            log.debug("没有码表Name创建码表Name");
            name= workbook.createName();
            name.setNameName(config.columnName()+"");
        }
        name.setRefersToFormula(DICCODE_SHEET_NAME +"!$"+config.columnName()+"$1:$"+config.columnName()+"$" + valueSet.size());
        DVConstraint constraint = DVConstraint.createFormulaListConstraint(name.getNameName());
        CellRangeAddressList addressList = new CellRangeAddressList(BaseExcelService.START_ROW, Short.MAX_VALUE,columnIndex,columnIndex);
        HSSFDataValidation validation = new HSSFDataValidation(addressList, constraint);
        workbook.setSheetHidden(workbook.getSheetIndex(DICCODE_SHEET_NAME), Workbook.SHEET_STATE_VERY_HIDDEN);
        setValidationTip(validation,config);
        sheet.addValidationData(validation);
        log.debug("用引用方式设置码表完毕");
    }

    /**
     * 时间校验适配器
     * @param dataBean
     * @param sheet
     * @param columnIndex
     * @param filedName
     */
    public void DateValidateAdapter(DataBean dataBean, Sheet sheet, int columnIndex, String filedName) {
        DateValidateConfig config = dataBean.getValidateConfig(filedName);
        DVConstraint dateConstraint = DVConstraint.createDateConstraint(DataValidationConstraint.OperatorType.BETWEEN, config.min(),config.max(),  config.format());
        // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(BaseExcelService.START_ROW, Short.MAX_VALUE, columnIndex, columnIndex);
        // 数据有效性对象
        DataValidation data_validation_list = new HSSFDataValidation(regions, dateConstraint);
        setValidationTip(data_validation_list,config);
        sheet.addValidationData(data_validation_list);
    }

    /**
     * 数字校验适配器
     * @param dataBean
     * @param sheet
     * @param columnIndex
     * @param filedName
     */
    public void NumericValidateAdapter(DataBean dataBean, Sheet sheet, int columnIndex, String filedName) {
        NumericValidateConfig config = dataBean.getValidateConfig(filedName);
        DVConstraint constraint = DVConstraint.createNumericConstraint(DataValidationConstraint.ValidationType.DECIMAL, DataValidationConstraint.OperatorType.BETWEEN, config.min(), config.max());
        // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(BaseExcelService.START_ROW, Short.MAX_VALUE, columnIndex, columnIndex);
        // 数据有效性对象
        DataValidation data_validation_list = new HSSFDataValidation(regions, constraint);
        setValidationTip(data_validation_list,config);
        sheet.addValidationData(data_validation_list);
    }

    /**
     * 数字校验适配器
     * @param dataBean
     * @param sheet
     * @param columnIndex
     * @param filedName
     */
    public void IntegerValidateAdapter(DataBean dataBean, Sheet sheet, int columnIndex, String filedName) {
        IntValidateConfig config = dataBean.getValidateConfig(filedName);
        DVConstraint constraint = DVConstraint.createNumericConstraint(DataValidationConstraint.ValidationType.INTEGER, DataValidationConstraint.OperatorType.BETWEEN, config.min(), config.max());
        // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(BaseExcelService.START_ROW, Short.MAX_VALUE, columnIndex, columnIndex);
        // 数据有效性对象
        DataValidation data_validation_list = new HSSFDataValidation(regions, constraint);
        setValidationTip(data_validation_list,config);
        sheet.addValidationData(data_validation_list);
    }

    /**
     * 长度校验适配器
     * @param dataBean
     * @param sheet
     * @param columnIndex
     * @param filedName
     */
    public void TextValidateAdapter(DataBean dataBean, Sheet sheet, int columnIndex, String filedName) {
        TextValidateConfig config = dataBean.getValidateConfig(filedName);
        if (config.length() != 0) {
            DVConstraint constraint = DVConstraint.createNumericConstraint(
                    DVConstraint.ValidationType.TEXT_LENGTH,
                    DVConstraint.OperatorType.LESS_OR_EQUAL, config.length() + "", null);
            // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
            CellRangeAddressList regions = new CellRangeAddressList(BaseExcelService.START_ROW, Short.MAX_VALUE, columnIndex, columnIndex);
            // 数据有效性对象
            DataValidation data_validation_list = new HSSFDataValidation(regions, constraint);
            setValidationTip(data_validation_list,config);
            sheet.addValidationData(data_validation_list);
        }
    }

    /**
     * 隐藏校验适配器
     * @param dataBean
     * @param sheet
     * @param columnIndex
     * @param filedName
     */
    public void HiddenValidateAdapter(DataBean dataBean,Sheet sheet, int columnIndex, String filedName) {
        sheet.setColumnHidden(columnIndex,true);
    }
}
