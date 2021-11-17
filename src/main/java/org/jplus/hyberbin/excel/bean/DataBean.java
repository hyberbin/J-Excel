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
package org.jplus.hyberbin.excel.bean;

import org.apache.commons.collections4.CollectionUtils;
import org.jplus.hyberbin.excel.annotation.ExcelColumnGroup;
import org.jplus.hyberbin.excel.annotation.output.OutputTargetConfig;
import org.jplus.hyberbin.excel.exception.AdapterException;
import org.jplus.hyberbin.excel.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 一个Sheet的数据模型
 * User: Hyberbin
 * Date: 13-12-3
 * Time: 上午9:22
 */
public class DataBean {
    protected final static Logger log = LoggerFactory.getLogger(DataBean.class);
    /**字段列表*/
    private List<Field> filedList=new ArrayList<Field>(0);
    /**字段列表*/
    private String[]  filedNames;
    private Map<String,Field> targetField=new HashMap<String, Field>(0);
    /**字段信息映射*/
    private Map<String, FieldBean> fieldMap = new HashMap<String, FieldBean>(0);
    /**字段信息列表*/
    private List<FieldBean> fieldBeanList = new ArrayList<FieldBean>(0);
    /**输入适配器*/
    private Class inputAdapter;
    /**输出适配器*/
    private Class outputAdapter;
    /**校验适配器*/
    private Class validateAdapter;
    /**是否使用getter和setter方法*/
    private boolean useGetterSetter = false;
    /**如果中复杂循环类型的字段 将生成子数据模型*/
    private Map<String, DataBean> dataBeanMap = new HashMap<String, DataBean>();
    /**如果中复杂循环类型的字段 fatherDataBean表示父级数据模型*/
    private DataBean fatherDataBean;

    public DataBean(Class excelVo, Class inputAdapter, Class outputAdapter, Class validateAdapter) {
        this(Map.class.isAssignableFrom(excelVo) ? Collections.<Field>emptyList() : Reflections.getAllFields(excelVo, BaseExcelVo.class), inputAdapter, outputAdapter, validateAdapter);
    }

    public DataBean(List filedList, Class inputAdapter, Class outputAdapter, Class validateAdapter) {
        this.inputAdapter = inputAdapter;
        this.outputAdapter = outputAdapter;
        this.validateAdapter = validateAdapter;
        if(CollectionUtils.isNotEmpty(filedList)){
            filedNames=new String[filedList.size()];
            if(filedList.get(0) instanceof Field){
                for(Object fieldo:filedList){
                    Field field=(Field) fieldo;
                    if(field.isAnnotationPresent(OutputTargetConfig.class)){
                        targetField.put(field.getName().toLowerCase(),field);
                        continue;
                    }
                    this.filedList.add(field);
                }
                for (int i = 0; i < this.filedList.size(); i++) {
                    FieldBean fieldBean;
                    Field field = this.filedList.get(i);
                    filedNames[i]=field.getName();
                    if (Collection.class.isAssignableFrom(field.getType())) {
                        ExcelColumnGroup annotation = field.getAnnotation(ExcelColumnGroup.class);
                        if (BaseExcelVo.class.isAssignableFrom(annotation.type())) {
                            DataBean dataBean = new DataBean(Reflections.getAllFields(annotation.type(), BaseExcelVo.class), inputAdapter, outputAdapter, validateAdapter);
                            dataBean.fatherDataBean = this;
                            dataBeanMap.put(field.getName().toLowerCase(), dataBean);
                            fieldBean = new FieldBean(FieldType.ColumnGroup_ARRAY, field);
                        } else {
                            fieldBean = new FieldBean(FieldType.BAS_ARRAY, field);
                        }
                    } else {
                        fieldBean = new FieldBean(FieldType.BASIC, field);
                    }
                    Annotation[] annotations = field.getDeclaredAnnotations();
                    for (Annotation annotation : annotations) {
                        log.debug("has annotation:{}",annotation.annotationType().getSimpleName());
                        if (AdapterConstant.inputConfigs.contains(annotation.annotationType().getSimpleName())) {
                            fieldBean.setInputConfig(annotation);
                            if (inputAdapter != null) fieldBean.setInputMethod(inputAdapter);
                        } else if (AdapterConstant.outputConfigs.contains(annotation.annotationType().getSimpleName())) {
                            fieldBean.setOutputConfig(annotation);
                            if (outputAdapter != null) fieldBean.setOutputMethod(outputAdapter);
                        } else if (AdapterConstant.validateConfigs.contains(annotation.annotationType().getSimpleName())) {
                            fieldBean.setValidateConfig(annotation);
                            if (validateAdapter != null) fieldBean.setValidateMethod(validateAdapter);
                        }
                    }
                    fieldMap.put(field.getName().toLowerCase(), fieldBean);
                    fieldBeanList.add(fieldBean);
                }
            }else {
                filedNames = (String[]) filedList.toArray(new String[]{});
                this.filedList=filedList;
                for(String field:filedNames){
                    FieldBean fieldBean = new FieldBean(FieldType.BASIC, field);
                    fieldMap.put(field.toLowerCase(), fieldBean);
                    fieldBeanList.add(fieldBean);
                }
            }
        }
    }

    public <A> A getInputConfig(String fieldName) {
        return (A) fieldMap.get(fieldName.toLowerCase()).getInputConfig();
    }

    public <A> A getOutputConfig(String fieldName) {
        return (A) fieldMap.get(fieldName.toLowerCase()).getOutputConfig();
    }

    public <A> A getValidateConfig(String fieldName) {
        return (A) fieldMap.get(fieldName.toLowerCase()).getValidateConfig();
    }

    public void setUseGetterSetter(boolean useGetterSetter) {
        this.useGetterSetter = useGetterSetter;
    }

    public Field getField(String fieldName) {
        return fieldMap.get(fieldName.toLowerCase()).getField();
    }

    public Object getFieldValue(String fieldName, Object excelVo) throws AdapterException {
        if(excelVo instanceof Map){
            return ((Map) excelVo).get(fieldName);
        }
        FieldBean fieldBean = fieldMap.get(fieldName.toLowerCase());
        try {
            Object o = useGetterSetter ? Reflections.invokeGetter(excelVo, fieldName) : fieldBean.getField().get(excelVo);
            if(!fieldBean.isNullAble()&&o==null){
                throw new AdapterException(fieldName, Message.FIELD_NULL_ERROR);
            }else{
                return o;
            }
        } catch (Exception e) {
            throw new AdapterException(fieldName, Message.FIELD_GET_ERROR);
        }
    }

    public void setTargetFieldValue(String fieldName, Object excelVo, Object value)throws AdapterException{
        if(excelVo instanceof Map){
            Map map=(Map) excelVo;
            map.put(fieldName,value);
            return;
        }
        Field field = targetField.get(fieldName.toLowerCase());
        if(field==null){
            log.error("找不到TargetField");
            return;
        }
        if (value != null && !value.getClass().equals(field.getType())) {
            value = ConverString.asType(field.getType(), value.toString(), null);
        }
        if (value!=null){
            try {
                if (useGetterSetter) {
                    Reflections.invokeSetter(excelVo, fieldName, value);
                } else {
                    field.set(excelVo, value);
                }
            } catch (Exception e) {
                throw new AdapterException(fieldName, Message.FIELD_SET_ERROR);
            }
        }
    }

    public void setFieldValue(String fieldName, Object excelVo, Object value) throws AdapterException {
        FieldBean fieldBean = fieldMap.get(fieldName.toLowerCase());
        if (fieldBean.isDefaultByUp()) {
            if (value!=null) {
                value = fieldBean.getLastValue();
            } else {
                fieldBean.setLastValue(value);
            }
        }
        if(value!=null &&value instanceof String&& ObjectHelper.isNullOrEmptyString(value)&&!fieldBean.isNullAble()){
            throw new AdapterException(fieldName, Message.FIELD_NULL_ERROR);
        } else if (excelVo instanceof Map) {
            Map map = (Map) excelVo;
            map.put(fieldName, value);
        } else if (value != null) {
            try {
                if (useGetterSetter) {
                    Reflections.invokeSetter(excelVo, fieldName, value);
                } else {
                    fieldBean.getField().set(excelVo, value);
                }
            } catch (Exception e) {
                throw new AdapterException(fieldName, Message.FIELD_SET_ERROR);
            }
        }else{
            if(!fieldBean.isNullAble()){
                throw new AdapterException(fieldName, Message.FIELD_NULL_ERROR);
            }
        }
    }

    public List<Field> getFiledList() {
        return filedList;
    }

    public String[] getFiledNames() {
        return filedNames;
    }

    public List<FieldBean> getFiledBeanList() {
        return fieldBeanList;
    }

    public FieldBean getFieldBean(String fieldName) {
        return fieldMap.get(fieldName.toLowerCase());
    }

    public DataBean getChildDataBean(String fieldName) {
        return dataBeanMap.get(fieldName.toLowerCase());
    }

    public DataBean getFatherDataBean() {
        return fatherDataBean;
    }

    public Class getInputAdapter() {
        return inputAdapter;
    }

    public void setInputAdapter(Class inputAdapter) {
        this.inputAdapter = inputAdapter;
    }

    public Class getOutputAdapter() {
        return outputAdapter;
    }

    public void setOutputAdapter(Class outputAdapter) {
        this.outputAdapter = outputAdapter;
    }

    public Class getValidateAdapter() {
        return validateAdapter;
    }

    public void setValidateAdapter(Class validateAdapter) {
        this.validateAdapter = validateAdapter;
    }
}
