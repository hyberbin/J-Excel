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

import org.jplus.hyberbin.excel.language.LanguageUtils;


/**
 * Created with IntelliJ IDEA.
 * User: Hyberbin
 * Date: 13-12-4
 * Time: 上午9:57
 */
public class ColumnErrorException extends Exception {
    private int row, clo;
    private String message;

    public ColumnErrorException(int row, int clo) {
        this.row = row;
        this.clo = clo;
    }

    public ColumnErrorException(int row, int clo, String message) {
        this(row, clo);
        this.message = LanguageUtils.translate(message,row,clo);
    }

    public ColumnErrorException(int row, String fieldName, String message) {
        this.row = row;
        this.message = LanguageUtils.translate(message,row,fieldName);
    }


    public void setMessage(String message) {
        this.message = LanguageUtils.translate(message,row,clo);
    }

    public String getMessage() {
        return message;
    }
}
