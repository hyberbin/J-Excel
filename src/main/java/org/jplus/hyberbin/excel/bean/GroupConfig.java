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
package org.jplus.hyberbin.excel.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 循环节的配置
 */
public abstract class GroupConfig {
    private int groupSize;
    private int length;
    private List<String> fieldNames=new ArrayList<String>();
    private int globalLength=0;

    public GroupConfig(int groupSize, int length) {
        this.groupSize = groupSize;
        this.length = length;
    }
    public GroupConfig(int length) {
        this.groupSize = 1;
        this.length = length;
    }

    public int getRealLength(){
          return globalLength/groupSize;
    }

    public abstract String getLangName(int innerIndex,int index);

    public int getLength() {
        return length;
    }

    public int getGroupSize() {
        return groupSize;
    }

    public void addField(String fieldName){
        if(!fieldNames.contains(fieldName)){
            this.fieldNames.add(fieldName);
        }
        globalLength++;
    }

    public List<String> getFieldNames() {
        return fieldNames;
    }

    public void setFieldNames(List<String> fieldNames) {
        this.fieldNames=fieldNames;
    }
    /**获取一个匿名的循环节配置（主要是在导入的时候用到）*/
    public static GroupConfig getNoNameGroup(int groupSize,int length){
        return new GroupConfig(groupSize,length) {
            @Override
            public String getLangName(int innerIndex, int index) {
                return null;
            }
        };
    }

}
