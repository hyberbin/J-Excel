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

/**
 * json工具.
 * 这里只是引用一个简单的，如果要性能更高或者项目本身有可以自己设置
 * @author Hyberbin
 */
public class JsonUtil {

    private static IJsonUtil jsonUtil = new SimpleJsonUtil();

    public static String toJSON(Object o) {
        return jsonUtil.toJSON(o);
    }

    public static <T> T toObject(String s, Class type) {
        return jsonUtil.toObject(s, type);
    }

    public static void setJsonUtil(IJsonUtil jsonUtil) {
        JsonUtil.jsonUtil = jsonUtil;
    }
}
