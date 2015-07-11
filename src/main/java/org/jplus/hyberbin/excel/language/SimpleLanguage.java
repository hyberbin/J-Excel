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
package org.jplus.hyberbin.excel.language;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import org.jplus.hyberbin.excel.annotation.Lang;

/**
 *
 * @author Hyberbin
 */
public class SimpleLanguage implements ILanguage {

    @Override
    public String translate(Object key, Object... args) {
        if (key instanceof Field) {
            Field field = (Field) key;
            if (field.isAnnotationPresent(Lang.class)) {
                Lang lang = field.getAnnotation(Lang.class);
                return lang.value();
            }
            return field.getName();
        }
        return TextFormat.format(key.toString(), args);
    }
    

}

class TextFormat {

    /**
     * 格式化字符串
     * @param message 要格式化的内容
     * @param objects 参数
     * @return
     */
    public static String format(String message, Object... objects) {
        StringBuilder builder = new StringBuilder(message);
        return MessageFormat.format(replace(builder, objects), objects);
    }

    /**
     * 将msg{},{},{}替换成msg{0},{1},{2}的形式
     * @param message 要替换的内容
     * @param objects 替换对象
     * @return
     */
    private static String replace(StringBuilder message, Object... objects) {
        Integer n = 0;
        String res = message.toString();
        for (Object object : objects) {
            if (object instanceof Throwable) {
                ((Throwable) object).printStackTrace();
            } else {
                int indexOf = message.indexOf("{}");
                res = indexOf > 0 ? message.insert(indexOf + 1, n++).toString() : message.toString();
            }
        }
        return res;
    }

}
