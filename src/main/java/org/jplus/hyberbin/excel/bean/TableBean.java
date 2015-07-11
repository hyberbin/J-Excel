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

import java.util.Collection;

/**
 * 表格模型，用于导出表格
 * Created by Hyberbin on 2014/6/18.
 */
public class TableBean {
    /**行数*/
    private int rowCount;
    /**列数*/
    private int columnCount;
    /**行高*/
    private float rowHeight=400f;
    /**单元格,可以不按顺序存放**/
    private Collection<CellBean> cellBeans;

    public TableBean(int rowCount, int columnCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
    }

    public Collection<CellBean> getCellBeans() {
        return cellBeans;
    }

    public void setCellBeans(Collection<CellBean> cellBeans) {
        this.cellBeans = cellBeans;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public float getRowHeight() {
        return rowHeight;
    }

    public void setRowHeight(float rowHeight) {
        this.rowHeight = rowHeight;
    }
    
}
