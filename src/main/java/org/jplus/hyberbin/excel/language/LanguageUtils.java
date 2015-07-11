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

/**
 * 语言国际化工具.
 * 用户可以自己实现国际化接口
 * @author Hyberbin
 */
public class LanguageUtils {

    private static ILanguage language = new SimpleLanguage();

    /**
     * @param key
     * @param args the command line arguments
     * @return
     */
    public static String translate(String key, Object... args) {
        return language.translate(key, args);
    }

    public static void setLanguage(ILanguage lang) {
        language = lang;
    }

}
