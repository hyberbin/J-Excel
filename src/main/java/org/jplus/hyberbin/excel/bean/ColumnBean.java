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

/**
 * Created by Hyberbin on 14-1-22.
 */
public class ColumnBean {
    private String columnName="";
    private String innerColumn="";
    private int length=0;
    private int size=0;
    private String tookValue="";
    private String tookName="";

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getInnerColumn() {
        return innerColumn;
    }

    public void setInnerColumn(String innerColumn) {
        this.innerColumn = innerColumn;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getTookName() {
        return tookName;
    }

    public void setTookName(String tookName) {
        this.tookName = tookName;
    }

    public String getTookValue() {
        return tookValue;
    }

    public void setTookValue(String tookValue) {
        this.tookValue = tookValue;
    }
}
