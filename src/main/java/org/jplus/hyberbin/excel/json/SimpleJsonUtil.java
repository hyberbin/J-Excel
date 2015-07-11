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
package org.jplus.hyberbin.excel.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 简单的json转换工具
 * @author Hyberbin
 */
public class SimpleJsonUtil implements IJsonUtil {

    private static final Logger log = LoggerFactory.getLogger(SimpleJsonUtil.class);
    public static final JsonUtil IN_STANCE = new JsonUtil();
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String NULL_STRING = "";

    /**
     * 把 json 字符串转换为List对象
     *
     * @param jsonString  json 字符串
     * @param elementType list 元素的类型
     * @param <T>         list 元素类型
     * @return list
     */
    public static <T> List<T> toList(String jsonString, Class<T> elementType) {
        try {
            return mapper.readValue(jsonString, mapper.getTypeFactory().constructCollectionType(List.class, elementType));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 把 json 字符串转换为Map对象
     *
     * @param jsonString json 字符串
     * @param keyType    键类型
     * @param valueType  值类型
     * @param <T1>       键类型
     * @param <T2>       值类型
     * @return map
     */
    public static <T1, T2> Map<T1, T2> toMap(String jsonString, Class<T1> keyType, Class<T2> valueType) {
        try {
            return mapper.readValue(jsonString, mapper.getTypeFactory().constructMapType(Map.class, keyType, valueType));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toJSON(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return NULL_STRING;
    }

    /**
     *
     * @param <T>
     * @param jsonString
     * @param requiredType
     * @return
     */
    @Override
    public <T> T toObject(String jsonString, Class requiredType) {
        try {
            return (T) mapper.readValue(jsonString, requiredType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
