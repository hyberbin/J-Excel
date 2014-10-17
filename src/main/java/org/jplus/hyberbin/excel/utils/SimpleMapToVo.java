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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.jplus.hyberbin.excel.bean.BaseExcelVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Map到Object转换
 * 要求各自属性类型一致
 * User: Hyberbin
 * Date: 13-12-4
 * Time: 下午3:37
 */
public class SimpleMapToVo {
    private static final Logger log = LoggerFactory.getLogger(SimpleMapToVo.class);

    public static Object formMap(Map map, Class vo) {
        try {
            Object voObject = vo.newInstance();
            List<Field> allFields = Reflections.getAllFields(voObject, BaseExcelVo.class);
            for(Field field:allFields){
                if(map.containsKey(field.getName())){
                    Object value = map.get(field.getName());
                    if(value==null)continue;
                    if(field.getType().isAssignableFrom(value.getClass())){
                        field.set(voObject,value);
                    }else{
                        field.set(voObject, ConverString.asType(field.getType(),value.toString()));
                    }
                }
            }
            return voObject;
        } catch (InstantiationException e) {
            log.error("创建实例失败！", e);
        } catch (IllegalAccessException e) {
            log.error("类不能访问", e);
        }
        return null;
    }

    public static List forMapList(List<Map> maps,Class vo){
        List list=new ArrayList();
        if(ObjectHelper.isNotEmpty(maps)){
            for(Map map:maps){
                list.add(formMap(map,vo));
            }
        }
        return list;
    }
}
