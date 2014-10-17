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
package org.jplus.hyberbin.excel.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.jplus.hyberbin.excel.adapter.IOAdapter;
import org.jplus.hyberbin.excel.annotation.DefaultAdapterMethod;
import org.jplus.hyberbin.excel.bean.DataBean;

/**
 * User: Hyberbin
 * Date: 13-12-3
 * Time: 下午6:20
 */
public class AdapterUtil {
    public static Class[] outputAdapterParms= new Class[]{DataBean.class,Object.class,String.class,Cell.class} ;
    public static Class[] inputAdapterParms= new Class[]{DataBean.class,Class.class,String.class,Cell.class} ;
    public static Class[] ValidateAdapterParms= new Class[]{DataBean.class,Sheet.class,int.class,String.class} ;
    public static Class[] Constructor=new Class[]{DicCodePool.class};

    public static Method getDefaultAdapterMethod(Class clazz){
        List<Method> allMethods = Reflections.getAllMethods(clazz);
        for(Method method:allMethods){
            if(method.isAnnotationPresent(DefaultAdapterMethod.class)){
                return method;
            }
        }
        return null;
    }

    /**
     * 执行导出时的适配器方法
     * @param adapter 适配器工厂
     * @param method  适配器
     * @param dataBean 数据模型
     * @param fieldValue 字段值
     * @param fieldName 字段名
     * @param cell 单元格
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static void invokeOutputAdapterMethod(IOAdapter adapter,Method method,DataBean dataBean, Object fieldValue, String fieldName,Cell cell) throws InvocationTargetException, IllegalAccessException {
        method.invoke(adapter,new Object[]{dataBean,fieldValue,fieldName,cell});
    }

    /**
     *  执行导入时的适配器方法
     * @param adapter 适配器工厂
     * @param method   适配器
     * @param dataBean 数据模型
     * @param type  字段类型
     * @param fieldName 字段名
     * @param cell  单元格
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static Object invokeInputAdapterMethod(IOAdapter adapter,Method method,DataBean dataBean,Class type, String fieldName,Cell cell) throws InvocationTargetException, IllegalAccessException {
        return cell==null?null:method.invoke(adapter,new Object[]{dataBean,type,fieldName,cell});
    }

    /**
     * 执行验证时的适配器方法
     * @param adapter 适配器工厂
     * @param method  适配器
     * @param dataBean 数据模型
     * @param sheet 表格
     * @param index 字段是在第几列
     * @param fieldName 字段名
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static void invokeValidateAdapterMethod(IOAdapter adapter,Method method,DataBean dataBean,Sheet sheet, int index,String fieldName) throws InvocationTargetException, IllegalAccessException {
        method.invoke(adapter,new Object[]{dataBean,sheet,index,fieldName});
    }

    /**
     * 根据配置获取导入的适配器
     * @param clazz 配置
     * @param adapter 适配器
     * @return
     */
    public static Method getInputAdapterMethod(Annotation clazz,Class adapter){
        String method=(String)Reflections.invokeMethod(clazz, "method", new Class[]{}, new Object[]{});
        return Reflections.getAccessibleMethod(adapter,method,inputAdapterParms);
    }
    /**
     * 根据配置获取导出的适配器
     * @param clazz 配置
     * @param adapter 适配器
     * @return
     */
    public static Method getOutputAdapterMethod(Annotation clazz,Class adapter){
        String method=(String)Reflections.invokeMethod(clazz, "method", new Class[]{}, new Object[]{});
        return Reflections.getAccessibleMethod(adapter,method,outputAdapterParms);
    }
    /**
     * 根据配置获取验证的适配器
     * @param clazz 配置
     * @param adapter 适配器
     * @return
     */
    public static Method getValidateAdapterMethod(Annotation clazz,Class adapter){
        String method=(String)Reflections.invokeMethod(clazz, "method", new Class[]{}, new Object[]{});
        return Reflections.getAccessibleMethod(adapter,method,ValidateAdapterParms);
    }

    /**
     * 是否当值为空时取上一行的值
     * @param clazz
     * @return
     */
    public static boolean getInputDefaultByUp(Annotation clazz){
        return (Boolean)Reflections.invokeMethod(clazz, "defaultByUp", new Class[]{}, new Object[]{});
    }

    /**
     * 是否允许有空
     * @param clazz
     * @return
     */
    public static boolean getNullAble(Annotation clazz){
        return (Boolean)Reflections.invokeMethod(clazz, "nullAble", new Class[]{}, new Object[]{});
    }
    /**
     * 是否允许有空
     * @param clazz
     * @return
     */
    public static String getTipLangName(Annotation clazz){
        return (String)Reflections.invokeMethod(clazz, "tipLangName", new Class[]{}, new Object[]{});
    }

    public static boolean getAllMatch(Annotation clazz){
        return (Boolean)Reflections.invokeMethod(clazz, "allMatch", new Class[]{}, new Object[]{});
    }
}
