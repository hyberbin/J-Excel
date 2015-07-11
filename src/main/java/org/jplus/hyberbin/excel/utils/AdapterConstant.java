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
package org.jplus.hyberbin.excel.utils;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Hyberbin
 * Date: 13-12-3
 * Time: 下午6:59
 */
public final class AdapterConstant {

    public final static Set<String> inputConfigs=new HashSet<String>(0);
    public final static Set<String> outputConfigs=new HashSet<String>(0);
    public final static Set<String> validateConfigs=new HashSet<String>(0);

    public static void ini(){
        inputConfigs.add("InputTextConfig");
        inputConfigs.add("InputDateConfig");
        inputConfigs.add("InputDefaultConfig");
        inputConfigs.add("InputDicConfig");
        inputConfigs.add("InputIntConfig");
        outputConfigs.add("OutputDateConfig");
        outputConfigs.add("OutputDefaultConfig");
        outputConfigs.add("OutputDicConfig");
        outputConfigs.add("OutputIntConfig");
        outputConfigs.add("OutputNumericConfig");
        validateConfigs.add("DateValidateConfig");
        validateConfigs.add("DicValidateConfig");
        validateConfigs.add("IntValidateConfig");
        validateConfigs.add("NumericValidateConfig");
        validateConfigs.add("TextValidateConfig");
        validateConfigs.add("HiddenValidateConfig");
    }

    static {
        ini();
    }
}
