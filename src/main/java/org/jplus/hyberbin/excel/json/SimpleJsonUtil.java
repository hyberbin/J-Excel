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
package org.jplus.hyberbin.excel.json;

import java.lang.reflect.Field;
import java.util.List;
import org.jplus.hyberbin.excel.utils.ConverString;
import org.jplus.hyberbin.excel.utils.Reflections;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 简单的json转换工具
 * @author Hyberbin
 */
public class SimpleJsonUtil implements IJsonUtil {

    private static final Logger log = LoggerFactory.getLogger(SimpleJsonUtil.class);

    @Override
    public String toJSON(Object o) {
        return new JSONObject(o).toString();
    }

    @Override
    public <T> T toObject(String s, Class type) {
        try {
            JSONObject json = new JSONObject(s);
            log.trace("instance for class:{}", type.getName());
            Object object = Reflections.instance(type.getName());
            List<Field> fields = Reflections.getAllFields(object);
            for (Field field : fields) {
                log.trace("try to set field value for field:{}", field.getName());
                Object value = json.getString(field.getName());
                if (value == null) {
                    continue;
                }
                if (!value.getClass().isAssignableFrom(field.getType())) {
                    value = ConverString.asType(field.getType(), value);
                }
                if (value == null) {
                    continue;
                }
                log.trace("set field value :{}", value);
                Reflections.invokeSetter(object, field.getName(), value,field.getType());
            }
            return (T) object;
        } catch (JSONException ex) {
            log.error("toObject error!", ex);
            return null;
        }
    }

}
