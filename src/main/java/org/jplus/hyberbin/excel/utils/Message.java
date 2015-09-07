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


/**
 * @author Hyberbin
 */
public class Message {
    public static final String RESOURCE_NAME = Message.class.getPackage().getName() + ".message";

    public static final String ERROR_TEST = "error.test";
    /**
     * 模块名
     */
    private static final String MODULE_NAME = "tools.excel";
    private static final String DOT = ".";
    private static final String ERROR = "error";
    private static final String WARN = "warn";
    private static final String INFO = "info";
    private static final String SUCCESS = "success";
    /**
     * 错误信息前缀
     */
    private static final String MSG_ERROR = MODULE_NAME + DOT + ERROR + DOT;
    /**
     * 警告信息前缀
     */
    private static final String MSG_WARN = MODULE_NAME + DOT + WARN + DOT;
    /**
     * 一般信息前缀
     */
    private static final String MSG_INFO = MODULE_NAME + DOT + INFO + DOT;
    /**
     * 成功信息前缀
     */
    private static final String MSG_SUCCESS = MODULE_NAME + DOT + SUCCESS;
    public static final String COLUMN_ERROR = MSG_ERROR + "column.error";
    public static final String ROW_ERROR = MSG_ERROR + "row.error";
    public static final String HEAD_ERROR = MSG_ERROR + "head.error";
    public static final String VO_INSTANCE_ERROR = MSG_ERROR + "vo.instance.error";
    public static final String FIELD_SET_ERROR = MSG_ERROR + "field.set.error";
    public static final String FIELD_NULL_ERROR = MSG_ERROR + "field.null.error";
    public static final String FIELD_GET_ERROR = MSG_ERROR + "field.get.error";
    public static final String INPUT_CELL_DATA_ERROR = MSG_ERROR + "input.cell.data.error";
    public static final String DATE_TYPE_ERROR = MSG_ERROR + "date.type.error";
    public static final String EXCEL_VO_ERROR = MSG_ERROR + "excel.vo.error";
    public static final String INPUT_TEMPLATE = MSG_INFO + "input.template";
    public static final String ERROR_IMPORT = MSG_ERROR + "import";
    public static final String SUCCESS_IMPORT = MSG_SUCCESS + "import";
    public static final String ERROR_EXPORT = MSG_ERROR + "export";
    public static final String FLAG_ERROR = MSG_ERROR + "flag.error";
    public static final String DIC_ERROR = MSG_ERROR + "dic.error";
    public static final String VALUE_MAX_ERROR = MSG_ERROR + "value.max";
    public static final String LENGTH_MAX_ERROR = MSG_ERROR + "length.max";
    public static final String REGX_ERROR = MSG_ERROR + "regx";
    public static final String VALUE_MIN_ERROR = MSG_ERROR + "value.min";
    public static final String LENGTH_MIN_ERROR = MSG_ERROR + "length.min";
    public static final String SUCCESS_EXPORT = MSG_SUCCESS + "export";
    public static final String TIP = MSG_INFO + "warn.tip";
}
