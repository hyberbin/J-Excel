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
import org.jplus.hyberbin.excel.adapter.IOAdapter;
import org.jplus.hyberbin.excel.annotation.ExcelColumnGroup;
import org.jplus.hyberbin.excel.annotation.ExcelVoConfig;
import org.jplus.hyberbin.excel.bean.*;
import org.jplus.hyberbin.excel.exception.AdapterException;
import org.jplus.hyberbin.excel.exception.ColumnErrorException;
import org.jplus.hyberbin.excel.exception.ExcelHeaderErrorException;
import org.jplus.hyberbin.excel.exception.ExcelVoErrorException;
import org.jplus.hyberbin.excel.json.JsonUtil;
import org.jplus.hyberbin.excel.utils.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 默认：
 * 第一行为标题
 * 第二行为隐藏的字段名
 * 第三行为表头
 * 从第四行开始为导入数据
 * 第四行从第0列开始必须连续，否则视为后面的无效
 * User: Hyberbin
 * Date: 13-12-3
 * Time: 下午4:01
 * @param <T>
 */
public class ImportExcelService<T> extends BaseExcelService {
    protected final Class<T> voClass;
    protected final List fieldList = new ArrayList<>();
    protected final DicCodePool dicCodePool;
    protected final Sheet sheet;
    protected final DataBean dataBean;
    protected final ExcelVoConfig config;
    protected final int columnLenth;
    protected final IOAdapter inputFactory;
    protected final Method defaultAdapterMethod;
    protected final List<T> errorList = new ArrayList<T>();
    protected Map<String, GroupConfig> groupConfig = new HashMap<String, GroupConfig>();
    /**标签的哈希密码*/
    protected long hashVal=0;
    /**文件校验是否通过*/
    protected boolean checked=false;
    protected final Map<Integer,ColumnBean> columnBeanMap=new HashMap(0);

    /**
     * @param voClass
     * @param sheet
     * @throws java.lang.Exception
     */
    public ImportExcelService(Class<T> voClass, Sheet sheet,String... fieldNames) throws Exception {
        if (voClass.isAnnotationPresent(ExcelVoConfig.class)) {
            config = voClass.getAnnotation(ExcelVoConfig.class);
        } else {
            config=ExcelVoConfig.defaultConfig;
        }
        if(fieldNames!=null&&fieldNames.length>0){
            fieldList.addAll(Arrays.asList(fieldNames));
        }
        this.voClass = voClass;
        this.sheet = sheet;
        log.debug("准备读取表头。。。");
        readHead();
        log.debug("表头读取成功，导入字段共有{}个", columnBeanMap.size());
        columnLenth = columnBeanMap.size();
        dataBean = new DataBean(fieldList, config.inputFactory(), config.outputFactory(), config.validateClass());
        Class factoryClass = config.inputFactory();
        log.debug("创建导入适配器工厂：{}", factoryClass.getName());
        Constructor constructor = factoryClass.getConstructor(AdapterUtil.Constructor);
        dicCodePool = new DicCodePool();
        inputFactory = (IOAdapter) constructor.newInstance(dicCodePool);
        log.debug("寻找默认的适配器");
        defaultAdapterMethod = AdapterUtil.getDefaultAdapterMethod(factoryClass);
        if (defaultAdapterMethod == null) {
            throw new IllegalArgumentException("找不到默认的适配器方法");
        }
        log.debug("默认的适配器为：{}", defaultAdapterMethod.getName());
        log.debug("创建导入适配器工厂成功！");
    }

    public ImportExcelService addDic(String name, List<Map> maps) {
        dicCodePool.addMap(name, maps);
        return this;
    }

    public ImportExcelService addDic(String name, Map<String, String> map) {
        dicCodePool.addMap(name, map);
        return this;
    }
    
    public ImportExcelService addDic(String name, String key,String value) {
        dicCodePool.addMap(name, key, value);
        return this;
    }

    /**
     * 读入表头
     *
     * @throws ExcelHeaderErrorException
     */
    private void readHead() throws ExcelHeaderErrorException {
        int index = 0;
        try{//读取hash密码
            hashVal=getHashVal(sheet);
        }catch (Exception e){
             throw new ExcelHeaderErrorException(Message.FLAG_ERROR);
        }
        List<String> columns = new ArrayList<String>();
        Cell cell = BaseExcelService.getCell(sheet, HIDDEN_FIELD_HEAD, index);
        while (index == 0 || !isEmpty(cell)) {
            String field = getString(cell);
            ColumnBean columnBean= JsonUtil.toObject(field,ColumnBean.class);
            columns.add(field);
            if(columnBean.getLength()>0){//代表是有循环项
                GroupConfig noNameGroup=groupConfig.get(columnBean.getColumnName());
                if(noNameGroup==null){
                    noNameGroup = GroupConfig.getNoNameGroup(columnBean.getSize(),columnBean.getLength());
                    groupConfig.put(columnBean.getColumnName(),noNameGroup);
                    Field accessibleField = Reflections.getAccessibleField(voClass, columnBean.getColumnName());
                    fieldList.add(accessibleField);
                }
                if(!ObjectHelper.isNullOrEmptyString(columnBean.getInnerColumn())){//复杂的循环节
                    noNameGroup.addField(columnBean.getInnerColumn());
                }else{
                    noNameGroup.addField(field);
                }
            }else if (!Map.class.isAssignableFrom(voClass)){
                Field accessibleField = Reflections.getAccessibleField(voClass, columnBean.getColumnName());
                if(accessibleField==null){
                    log.error("{}找不到指定的字段{}",voClass.getSimpleName(),columnBean.getColumnName());
                }else{
                    fieldList.add(accessibleField);
                }
            }
            columnBeanMap.put(index,columnBean);
            index++;
            cell = BaseExcelService.getCell(sheet, HIDDEN_FIELD_HEAD, index);
        }
        if (ObjectHelper.isEmpty(columns)) {
            throw new ExcelHeaderErrorException(Message.HEAD_ERROR);
        }
    }

    /**
     * 读取一行
     *
     * @param row 行号
     * @param excelVo
     * @param indexClo
     * @return ExcelVo对象
     * @throws org.jplus.hyberbin.excel.exception.ColumnErrorException
     * @throws AdapterException
     */
    public T getRow(int row, T excelVo, int indexClo) throws ColumnErrorException, AdapterException {
        Row sheetRow = getRow(sheet, row);
        if (!isEmpty(sheetRow, columnLenth)) {//空行校验
            List<FieldBean> filedBeanList = dataBean.getFiledBeanList();
            int cloIndex=0;
            for (FieldBean fieldBean : filedBeanList) {
                if (fieldBean.getFieldType() == FieldType.BASIC) {
                    setSimpleField(fieldBean, excelVo, sheetRow, row, cloIndex, dataBean);
                    cloIndex++;
                } else if (fieldBean.getFieldType() == FieldType.BAS_ARRAY) {
                    GroupConfig groupConfig = this.groupConfig.get(fieldBean.getFieldName());
                    setBasArrayField(fieldBean, excelVo, sheetRow, row, cloIndex + indexClo, dataBean,groupConfig);
                    cloIndex+=groupConfig.getRealLength();
                } else if (fieldBean.getFieldType() == FieldType.ColumnGroup_ARRAY) {
                    GroupConfig groupConfig = this.groupConfig.get(fieldBean.getFieldName());
                    setColumnGroupField(fieldBean, excelVo, sheetRow, row, cloIndex + indexClo, dataBean,groupConfig);
                    cloIndex+=groupConfig.getRealLength()*groupConfig.getGroupSize();
                }
            }
            log.debug("获取一行数据为："+JsonUtil.toJSON(excelVo));
            return excelVo;
        }
        return null;
    }

    /**
     * 获取复杂类型的数据
     * @param fieldBean
     * @param excelVo
     * @param sheetRow
     * @param row
     * @param index
     * @param dataBean
     * @throws AdapterException
     * @throws ColumnErrorException
     */
    private void setColumnGroupField(FieldBean fieldBean, Object excelVo, Row sheetRow, int row, int index, DataBean dataBean,GroupConfig groupConfig) throws AdapterException, ColumnErrorException {
        ExcelColumnGroup annotation = fieldBean.getField().getAnnotation(ExcelColumnGroup.class);
        Integer length = groupConfig.getRealLength();
        List list = new ArrayList();
        List<String> fieldNames = groupConfig.getFieldNames();
        DataBean childDataBean = dataBean.getChildDataBean(fieldBean.getFieldName());
        int childSize=fieldNames.size();
        for (int i = 0; i < length; i++) {//循环体外
            try {
                BaseExcelVo baseColumnGroup = (BaseExcelVo) annotation.type().newInstance();
                for (int j=0;j<fieldNames.size();j++) {
                    String field=fieldNames.get(j);
                    FieldBean childFieldBean=childDataBean.getFieldBean(field);
                    if (childFieldBean.getFieldType() == FieldType.BASIC) {
                        setSimpleField(childFieldBean, baseColumnGroup, sheetRow, row, index+i*childSize+j, childDataBean);
                    } else if (childFieldBean.getFieldType() == FieldType.BAS_ARRAY) {
                        GroupConfig childGroupConfig = this.groupConfig.get(childFieldBean.getFieldName());
                        setBasArrayField(childFieldBean, baseColumnGroup, sheetRow, row, index+i*childSize, childDataBean,childGroupConfig);
                    } else if (childFieldBean.getFieldType() == FieldType.ColumnGroup_ARRAY) {
                        GroupConfig childGroupConfig = this.groupConfig.get(childFieldBean.getFieldName());
                        setColumnGroupField(childFieldBean, baseColumnGroup, sheetRow, row,index+i*childSize, childDataBean,childGroupConfig);
                    }
                }
                list.add(baseColumnGroup);
            } catch (AdapterException e) {
               throw (AdapterException) e;
            }catch (Exception e) {
                throw new ColumnErrorException(row+1, index+1, Message.COLUMN_ERROR);
            }
        }
        dataBean.setFieldValue(fieldBean.getFieldName(), excelVo, list);
    }

    /**
     * 获取简单循环的数据
     * @param fieldBean
     * @param excelVo
     * @param sheetRow
     * @param row
     * @param index
     * @param dataBean
     * @throws AdapterException
     * @throws ColumnErrorException
     */
    private void setBasArrayField(FieldBean fieldBean, Object excelVo, Row sheetRow, int row, int index, DataBean dataBean,GroupConfig groupConfig) throws AdapterException, ColumnErrorException {
        ExcelColumnGroup annotation = fieldBean.getField().getAnnotation(ExcelColumnGroup.class);
        Integer length = groupConfig.getRealLength();
        Method inputMethod = fieldBean.getInputMethod();
        inputMethod = inputMethod == null ? defaultAdapterMethod : inputMethod;
        List list = new ArrayList();
        for (int i = 0; i < length; i++) {
            Cell cell=null;
            try {
                cell = getCell(sheetRow, i + index);
                if(excelVo instanceof BaseExcelVo){
                    ((BaseExcelVo)excelVo).setCol(i + index);
                }
                Object o = AdapterUtil.invokeInputAdapterMethod(inputFactory, inputMethod,dataBean, annotation.type(), fieldBean.getFieldName(), cell);
                list.add(o);
            } catch (Exception e) {
                setErrorStyle(cell);
                if (e instanceof AdapterException) {
                    throw (AdapterException) e;
                } else {
                    throw new ColumnErrorException(row+1,i+index+1, Message.COLUMN_ERROR);
                }
            }
        }
        dataBean.setFieldValue(fieldBean.getFieldName(), excelVo, list);
    }

    /**
     * 获取简单不循环单一数据
     * @param fieldBean
     * @param excelVo
     * @param sheetRow
     * @param row
     * @param index
     * @param dataBean
     * @throws AdapterException
     * @throws ColumnErrorException
     */
    private void setSimpleField(FieldBean fieldBean, Object excelVo, Row sheetRow, int row, int index, DataBean dataBean) throws AdapterException, ColumnErrorException {
        Cell cell = getCell(sheetRow, index);
        try {
            ColumnBean columnBean = columnBeanMap.get(index);
            if(!ObjectHelper.isNullOrEmptyString(columnBean.getTookValue())){
                dataBean.setTargetFieldValue(columnBean.getTookName(), excelVo, columnBean.getTookValue());
            }
            Method inputMethod = fieldBean.getInputMethod();
            inputMethod = inputMethod == null ? defaultAdapterMethod : inputMethod;
            String name = fieldBean.getFieldName();
            if(excelVo instanceof BaseExcelVo){
                ((BaseExcelVo)excelVo).setCol(index);
                ((BaseExcelVo)excelVo).setCell(name,cell);
            }
            Object o = AdapterUtil.invokeInputAdapterMethod(inputFactory, inputMethod,dataBean, fieldBean.getFieldValueType(), name, cell);
            dataBean.setFieldValue(name, excelVo, o);
            log.debug("geted field value:row:{},index:{},name:{},value:{}",row,index,name,o);
        } catch (Exception e) {
            setErrorStyle(cell);
            if (e instanceof AdapterException) {
                throw (AdapterException) e;
            } else {
                throw new ColumnErrorException(row+1, fieldBean.getFieldName(), Message.COLUMN_ERROR);
            }
        }
    }

    /**
     * 导入对象到List,数据如果太多可能会内存溢出
     *
     * @return
     * @throws org.jplus.hyberbin.excel.exception.ExcelVoErrorException
     */
    public List<T> doImport() throws ExcelVoErrorException {
        final List list = new ArrayList();
        doImport(new ObjectHandler<T>() {
            @Override
            public void handleObject(T o) {
                list.add(o);
            }
        });
        return list;
    }

    /**
     * 导入数据，逐条处理，内存不会溢出，数据量大建议使用
     * @param handler
     * @throws ExcelVoErrorException
     */
    public void doImport(ObjectHandler<T> handler) throws ExcelVoErrorException {
        long hasCode=0;
        for (int i = START_ROW; i <= sheet.getLastRowNum(); i++) {
            T t = null;
            String message=null;
            try {
                t = voClass.newInstance();
                T row = getRow(i, t, 0);
                if(row!=null){
                    if(row instanceof BaseExcelVo){
                        BaseExcelVo baseExcelVo=(BaseExcelVo) row;
                        baseExcelVo.setRow(i);
                        if(!baseExcelVo.validate()) {
                            errorList.add(row);
                            continue;
                        }
                    }
                }else{
                    continue;//如果返回值为空代表是空行
                }
                handler.handleObject(row);
            } catch (ColumnErrorException e) {//Excel表格有错误
                log.error(e.getMessage(), e);
                message=e.getMessage();
                errorList.add(t);
            } catch (AdapterException e) {//适配器有错误
                log.error(e.getMessage(), e);
                message=e.getMessage();
                errorList.add(t);
            } catch (Exception e) {//实例不能新建
                ExcelVoErrorException errorException = new ExcelVoErrorException(voClass, Message.VO_INSTANCE_ERROR);
                log.error(errorException.getMessage(), e);
                throw errorException;
            }
            if(t instanceof BaseExcelVo) {
                BaseExcelVo baseExcelVo = (BaseExcelVo) t;
                hasCode+=baseExcelVo.getHashVal();
                if(message!=null){
                    baseExcelVo.setMessage(message);
                }
            }

        }
        checked=hasCode==hashVal;
    }

    public List<T> getErrorList() {
        return errorList;
    }

    public ImportExcelService finish() {
        inputFactory.finish();
        return this;
    }

    public Map<Integer, ColumnBean> getColumnBeanMap() {
        return columnBeanMap;
    }

    /**
     * 返回文件内容校验是否通过
     * @return
     */
    public boolean isChecked() {
        return checked;
    }
}
