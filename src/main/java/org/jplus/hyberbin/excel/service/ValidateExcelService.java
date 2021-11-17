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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Sheet;
import org.jplus.hyberbin.excel.adapter.IOAdapter;
import org.jplus.hyberbin.excel.annotation.ExcelVoConfig;
import org.jplus.hyberbin.excel.bean.BaseExcelVo;
import org.jplus.hyberbin.excel.bean.DataBean;
import org.jplus.hyberbin.excel.bean.FieldBean;
import org.jplus.hyberbin.excel.bean.FieldType;
import org.jplus.hyberbin.excel.bean.GroupConfig;
import org.jplus.hyberbin.excel.exception.AdapterException;
import org.jplus.hyberbin.excel.exception.ExcelVoErrorException;
import org.jplus.hyberbin.excel.utils.AdapterUtil;
import org.jplus.hyberbin.excel.utils.DicCodePool;
import org.jplus.hyberbin.excel.utils.Message;
import org.jplus.hyberbin.excel.utils.ObjectHelper;

/**
 * User: Hyberbin
 * Date: 13-12-4
 * Time: 下午2:15
 * @param <T>
 */
public class ValidateExcelService<T extends BaseExcelVo> extends BaseExcelService {
    private final DicCodePool dicCodePool;
    private final Class<T> voClass;
    private final String[] fields;
    private final Sheet sheet;
    private final DataBean dataBean;
    private final ExcelVoConfig config;
    private final IOAdapter validateFactory;
    protected Map<String, GroupConfig> groupConfig=new HashMap<String, GroupConfig>();

    protected ValidateExcelService(Class<T> voClass, Sheet sheet,String[] fields) throws Exception {
        this(voClass, sheet, fields,new DicCodePool());
    }

    protected ValidateExcelService(Class<T> voClass, Sheet sheet,String[] fields,DicCodePool dicCodePool) throws Exception {
        this(voClass, sheet, fields, dicCodePool,new DataBean(voClass,voClass.getAnnotation(ExcelVoConfig.class).inputFactory(),
                voClass.getAnnotation(ExcelVoConfig.class).outputFactory(),
                voClass.getAnnotation(ExcelVoConfig.class).validateClass()));
    }

    protected ValidateExcelService(Class<T> voClass, Sheet sheet,String[] fields,DicCodePool dicCodePool,DataBean dataBean) throws Exception {
        if (voClass.isAnnotationPresent(ExcelVoConfig.class)) {
            config = voClass.getAnnotation(ExcelVoConfig.class);
        } else {
            throw new ExcelVoErrorException(voClass.getClass(), Message.EXCEL_VO_ERROR);
        }
        this.dicCodePool=dicCodePool;
        this.voClass = voClass;
        this.sheet = sheet;
        this.fields=fields;
        this.dataBean = dataBean;
        Class factoryClass = config.validateClass();
        log.debug("创建校验适配器工厂：{}", factoryClass.getName());
        Constructor constructor = factoryClass.getConstructor(AdapterUtil.Constructor);
        validateFactory =(IOAdapter)constructor.newInstance(dicCodePool);
        log.debug("创建校验适配器工厂成功！");
    }

    public ValidateExcelService addDic(String name, List<Map> maps) {
        dicCodePool.addMap(name, maps);
        return this;
    }
    
    public ValidateExcelService addDic(String name, String key,String value) {
        dicCodePool.addMap(name, key, value);
        return this;
    }
    
    public ValidateExcelService doValidate() throws AdapterException, ExcelVoErrorException {
        log.debug("开始进行数据有效性设置");
        validate(fields,dataBean,0);
        log.debug("数据有效性设置完毕");
        return this;
    }
    private void validate(String[] fields,DataBean dataBean,int baseIndex) throws ExcelVoErrorException, AdapterException {
        int cloIndex=baseIndex;
        for(String field:fields){
            T t = null;
            try {
                t = voClass.newInstance();
            } catch (Exception e) {
                ExcelVoErrorException errorException = new ExcelVoErrorException(voClass, Message.VO_INSTANCE_ERROR);
                log.error(errorException.getMessage(), e);
                throw errorException;
            }
            FieldBean fieldBean = dataBean.getFieldBean(field);
            if(fieldBean.getFieldType()== FieldType.ColumnGroup_ARRAY){
                GroupConfig group = groupConfig.get(fieldBean.getFieldName());
                groupValidate(fieldBean,dataBean,cloIndex,group);
                cloIndex+=group.getLength()*group.getGroupSize();
            }else if(fieldBean.getFieldType()== FieldType.BAS_ARRAY){
                GroupConfig group = groupConfig.get(fieldBean.getFieldName());
                for(int i=0;i<group.getLength();i++){
                    simpleValidate(t,fieldBean,dataBean,cloIndex);
                    cloIndex++;
                }
            } else{
                simpleValidate(t,fieldBean,dataBean,cloIndex);
                cloIndex++;
            }
        }
    }

    private void groupValidate(FieldBean fieldBean,DataBean dataBean,int baseIndex,GroupConfig group) throws AdapterException, ExcelVoErrorException {
        DataBean childDataBean = dataBean.getChildDataBean(fieldBean.getFieldName());
        for(int i=0;i<group.getLength();i++){
            int size= ObjectHelper.isEmpty(group.getFieldNames())?group.getGroupSize():group.getFieldNames().size();
            validate(childDataBean.getFiledNames(), childDataBean,baseIndex+i*size);
        }
    }

    public void setGroupConfig(String fieldName,GroupConfig groupConfig) {
        this.groupConfig.put(fieldName,groupConfig);
    }

    private void simpleValidate(BaseExcelVo baseExcelVo,FieldBean fieldBean,DataBean dataBean,int baseIndex) throws AdapterException {
        Method validateMethod = fieldBean.getValidateMethod();
        if(validateMethod==null){
            return;
        }
        try {
            baseExcelVo.setCol(baseIndex);
            AdapterUtil.invokeValidateAdapterMethod(validateFactory, validateMethod,dataBean, sheet, baseIndex, fieldBean.getFieldName());
        } catch (Exception e) {
            if(e instanceof AdapterException){
                throw (AdapterException)e;
            }else{
                log.error("未知错误！",e);
            }
        }
    }
}