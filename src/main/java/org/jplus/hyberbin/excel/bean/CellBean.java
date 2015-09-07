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

import org.apache.poi.ss.usermodel.Cell;

/**
 * 单元格模型，可以是合并的情况
 * Created by Hyberbin on 2014/6/18.
 */
public class CellBean {
    /**单元格对象*/
    private Cell cell;
    /**单元格内容*/
    private String content;
    /**开始行*/
    private int rowIndex;
    /**开始列*/
    private int columnIndex;
    /**横向合并个数*/
    private int xSize;
    /**纵向合并个数*/
    private int ySize;
    /**水平居中*/
    private boolean alignCenter=true;
    /**垂直居中*/
    private boolean verticalCenter=true;
    /**自动换行*/
    private boolean wrapText=true;

    public CellBean(String content, int rowIndex, int columnIndex) {
        this.content = content;
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        xSize=1;
        ySize=1;
    }

    public CellBean(String content, int rowIndex, int columnIndex, int xSize, int ySize) {
        this.content = content;
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.xSize = xSize<1?1:xSize;
        this.ySize = ySize<1?1:ySize;
    }

    public int getXSize() {
        return xSize;
    }

    public void setXSize(int xSize) {
        this.xSize = xSize;
    }

    public int getYSize() {
        return ySize;
    }

    public void setYSize(int ySize) {
        this.ySize = ySize;
    }

    public String getContent() {
        return content;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public boolean isAlignCenter() {
        return alignCenter;
    }

    public void setAlignCenter(boolean alignCenter) {
        this.alignCenter = alignCenter;
    }

    public boolean isVerticalCenter() {
        return verticalCenter;
    }

    public void setVerticalCenter(boolean verticalCenter) {
        this.verticalCenter = verticalCenter;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public int getxSize() {
        return xSize;
    }

    public void setxSize(int xSize) {
        this.xSize = xSize;
    }

    public int getySize() {
        return ySize;
    }

    public void setySize(int ySize) {
        this.ySize = ySize;
    }

    public boolean isWrapText() {
        return wrapText;
    }

    public void setWrapText(boolean wrapText) {
        this.wrapText = wrapText;
    }

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }
    
}
