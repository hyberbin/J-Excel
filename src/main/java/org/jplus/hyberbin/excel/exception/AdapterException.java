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
package org.jplus.hyberbin.excel.exception;

import org.apache.poi.ss.usermodel.Cell;
import org.jplus.hyberbin.excel.language.LanguageUtils;

/**
 * User: Hyberbin
 * Date: 13-12-4
 * Time: 上午10:47
 */
public class AdapterException extends Exception {
    private String fieldName;
    private String message;

    public AdapterException(String fieldName,String message) {
        this.fieldName = fieldName;
        this.message = LanguageUtils.translate(message,fieldName);
    }

    public AdapterException(String message,Cell cell) {
        this.message = LanguageUtils.translate(message,cell.getRowIndex(),cell.getColumnIndex(),cell.toString());
    }

    public AdapterException(String fieldName,String message,Cell cell) {
        this.fieldName = fieldName;
        this.message = LanguageUtils.translate(message,fieldName,cell.getRowIndex(),cell.getColumnIndex(),cell.toString());
    }

    public String getMessage() {
        return message;
    }
}
