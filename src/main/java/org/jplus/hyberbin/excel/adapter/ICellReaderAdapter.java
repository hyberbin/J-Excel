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
package org.jplus.hyberbin.excel.adapter;

import org.jplus.hyberbin.excel.bean.CellBean;
import org.jplus.hyberbin.excel.bean.TableBean;

/**
 * 单元格读取适配器接口.
 * 适用于纵表导入到复杂对象中.
 * Created by hyberbin on 15/7/31.
 */
public interface ICellReaderAdapter {
    /**
     * 整个读取
     * @param tableBean
     * @return
     */
    Object read(TableBean tableBean);

    /**
     * 预读取,用于数据校验
     * @param tableBean
     * @return
     */
    void preRead(TableBean tableBean);
}
