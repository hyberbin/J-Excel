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
package org.jplus.hyberbin.excel.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.jplus.hyberbin.excel.adapter.DefaultInputAdapter;
import org.jplus.hyberbin.excel.adapter.DefaultOutputAdapter;
import org.jplus.hyberbin.excel.adapter.DefaultValidateAdapter;
import org.jplus.hyberbin.excel.adapter.IOAdapter;

/**
 * Created with IntelliJ IDEA.
 * User: Hyberbin
 * Date: 13-12-3
 * Time: 上午10:54
 */
@Target({java.lang.annotation.ElementType.TYPE})//该注解只能用在类上
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelVoConfig {
    /**导出工厂
     * @return 类*/
    Class<? extends IOAdapter> outputFactory() default DefaultOutputAdapter.class;
    /**导入工厂
     * @return 类*/
    Class<? extends IOAdapter> inputFactory() default DefaultInputAdapter.class;
    /**验证
     * @return 类*/
    Class validateClass() default DefaultValidateAdapter.class;

    public static ExcelVoConfig defaultConfig=new ExcelVoConfig(){
        @Override
        public Class<? extends Annotation> annotationType() {
            return ExcelVoConfig.class;
        }

        @Override
        public Class<? extends IOAdapter> outputFactory() {
            return DefaultOutputAdapter.class;
        }

        @Override
        public Class<? extends IOAdapter> inputFactory() {
            return DefaultInputAdapter.class;
        }

        @Override
        public Class validateClass() {
            return DefaultValidateAdapter.class;
        }
    };
}
