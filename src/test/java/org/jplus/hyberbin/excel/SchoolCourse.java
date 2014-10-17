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
package org.jplus.hyberbin.excel;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.jplus.hyberbin.excel.annotation.ExcelColumnGroup;
import org.jplus.hyberbin.excel.annotation.ExcelVoConfig;
import org.jplus.hyberbin.excel.annotation.Lang;
import org.jplus.hyberbin.excel.annotation.output.OutputDicConfig;
import org.jplus.hyberbin.excel.annotation.validate.DicValidateConfig;
import org.jplus.hyberbin.excel.bean.BaseExcelVo;

/**
 *
 * @author Hyberbin
 */
@Table(name = "dc_xxkc")
@ExcelVoConfig//Excel导出的配置
public class SchoolCourse extends BaseExcelVo {

    @Column(name = "id")
    @Lang(value = "ID")//Excel导出的配置
    private String id;
    @Column(name = "kcmc")
    @Lang(value = "课程名称")//Excel导出的配置
    private String courseName;
    @Column(name = "kclx")
    @OutputDicConfig(dicCode = "KCLX")//Excel导出的配置
    @DicValidateConfig(dicCode = "KCLX")//如果要导出下拉框就加这个
    @Lang(value = "课程类型")//Excel导出的配置
    private String type;
    @Transient
    @ExcelColumnGroup(type = String.class)
    private List<String> baseArray;
    @Transient
    @ExcelColumnGroup(type = InnerVo.class)
    private List<InnerVo> innerVoArray;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getBaseArray() {
        return baseArray;
    }

    public void setBaseArray(List<String> baseArray) {
        this.baseArray = baseArray;
    }

    public List<InnerVo> getInnerVoArray() {
        return innerVoArray;
    }

    public void setInnerVoArray(List<InnerVo> innerVoArray) {
        this.innerVoArray = innerVoArray;
    }

    @Override
    public int getHashVal() {
        return 0;
    }

}
