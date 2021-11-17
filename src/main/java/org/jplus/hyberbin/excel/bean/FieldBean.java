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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.jplus.hyberbin.excel.utils.AdapterUtil;

/**
 * 一个字段的数据模型
 * User: Hyberbin
 * Date: 13-12-3
 * Time: 下午6:55
 */
public class FieldBean {
    private Field field;
    private String fieldName;
    /**输入适配器配置*/
    private Annotation inputConfig;
    /**输出适配器配置*/
    private Annotation outputConfig;
    /**校验适配器配置*/
    private Annotation validateConfig;
    /**输入适配器的方法*/
    private Method inputMethod;
    /**输出适配器的方法*/
    private Method outputMethod;
    /**校验适配器的方法*/
    private Method validateMethod;
    /**字段类型 简单、简单循环、复杂循环*/
    private FieldType fieldType;
    private Class fieldValueType;
    /**该字段的最后一次值*/
    private Object lastValue;
    /**是否默认为上一次的值*/
    private boolean defaultByUp=false;
    /**是否允许为空*/
    private boolean nullAble=true;

    public FieldBean(FieldType fieldType,Field field) {
        this.field = field;
        this.fieldType=fieldType;
        this.fieldName = field.getName();
        this.fieldValueType=field.getType();
    }

    public FieldBean(FieldType fieldType,String fieldName) {
        this.fieldName = fieldName;
        this.fieldType=fieldType;
        this.fieldValueType=String.class;
    }

    public Field getField() {
        return field;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Annotation getInputConfig() {
        return inputConfig;
    }

    public Annotation getValidateConfig() {
        return validateConfig;
    }

    public Annotation getOutputConfig() {
        return outputConfig;
    }

    public void setInputMethod(Class adapter) {
        inputMethod= AdapterUtil.getInputAdapterMethod(inputConfig, adapter);
        defaultByUp=AdapterUtil.getInputDefaultByUp(inputConfig);
        nullAble=AdapterUtil.getNullAble(inputConfig);
    }

    public void setOutputMethod(Class adapter) {
        outputMethod= AdapterUtil.getOutputAdapterMethod(outputConfig,adapter);
        nullAble=AdapterUtil.getNullAble(outputConfig);
    }

    public void setValidateMethod(Class adapter) {
        validateMethod=AdapterUtil.getValidateAdapterMethod(validateConfig,adapter);
    }

    public void setValidateConfig(Annotation validateConfig) {
        this.validateConfig = validateConfig;
    }

    public void setOutputConfig(Annotation outputConfig) {
        this.outputConfig = outputConfig;
    }

    public void setInputConfig(Annotation inputConfig) {
        this.inputConfig = inputConfig;
    }

    public Method getInputMethod() {
        return inputMethod;
    }

    public Method getOutputMethod() {
        return outputMethod;
    }

    public Method getValidateMethod() {
        return validateMethod;
    } 
    public FieldType getFieldType() {
        return fieldType;
    }

    public Class getFieldValueType() {
        return fieldValueType;
    }

    public Object getLastValue() {
        return lastValue;
    }

    public boolean isDefaultByUp() {
        return defaultByUp;
    }

    public boolean isNullAble() {
        return nullAble;
    }

    public void setLastValue(Object lastValue) {
        this.lastValue = lastValue;
    }
}
