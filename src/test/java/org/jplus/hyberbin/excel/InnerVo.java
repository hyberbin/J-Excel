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
package org.jplus.hyberbin.excel;

import org.jplus.hyberbin.excel.annotation.ExcelVoConfig;
import org.jplus.hyberbin.excel.annotation.output.OutputTargetConfig;
import org.jplus.hyberbin.excel.bean.BaseExcelVo;

/**
 *
 * @author Hyberbin
 */
@ExcelVoConfig//Excel导出的配置
public class InnerVo  extends BaseExcelVo {
    @OutputTargetConfig
    private String hiddenvalue;
    private String key;
    private String value;

    public InnerVo() {
    }

    public InnerVo(String key, String value) {
        this.key = key;
        this.value = value;
    }

    

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getHiddenvalue() {
        return hiddenvalue;
    }

    public void setHiddenvalue(String hiddenvalue) {
        this.hiddenvalue = hiddenvalue;
    }

    @Override
    public int getHashVal() {
        return 0;
    }

    
}
