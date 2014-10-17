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
package org.jplus.hyberbin.excel.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.jplus.hyberbin.excel.language.ILanguage;
import org.jplus.hyberbin.excel.language.SimpleLanguage;
import org.jplus.hyberbin.excel.utils.DicCodePool;
import org.jplus.hyberbin.excel.utils.FieldUtils;
import org.jplus.hyberbin.excel.utils.ObjectHelper;

/**
 * Excel导出.
 * 可以将List<Map>或者List<Object>直接导出
 */
public class SimpleExportService extends BaseExcelService {
    protected final DicCodePool dicCodePool = new DicCodePool();
    /**要输出的字段*/
    private final String[] fieldNames;
    /**数据可以是List<VO>也可以是List<Map>*/
    private final List data;
    /**国际化头*/
    private  ILanguage language=new SimpleLanguage();
    /**表格标题*/
    private final String title;
    protected final Sheet sheet;
    private final Map<String ,String> fieldDicMap=new HashMap<String, String>();

    public SimpleExportService(Sheet sheet,List data,String[] fieldNames,String title) {
        this.fieldNames = fieldNames;
        this.data = data;
        this.sheet=sheet;
        this.title=title;
    }
    /**
     * 生成表头
     */
    private void createHead(){
        log.debug("生成表头");
        Row hashRow = sheet.createRow(HASH_ROW);
        addTitle(sheet, TITLE_ROW, fieldNames.length, language.translate(title));
        Row row = createRow(sheet, HIDDEN_FIELD_HEAD, fieldNames.length);
        List<String> columns=new ArrayList(fieldNames.length);
        for (String fieldName : fieldNames) {
            columns.add(language.translate(fieldName));
        }
        addRow(sheet, COLUMN_ROW, columns.toArray(new String[]{}));
        hashRow.setHeight(Short.valueOf("0"));
        row.setHeight(Short.valueOf("0"));
        log.debug("表头生成完毕");
    }

    /**
     * 执行导出
     * @return 
     */
    public SimpleExportService doExport() {
        createHead();
        if (ObjectHelper.isNotEmpty(data)) {
            for (int r = 0; r < data.size(); r++) {
                Object object = data.get(r);
                log.debug("写入第{}行",START_ROW + r);
                Row row = createRow(sheet, START_ROW + r, fieldNames.length);
                for (int i = 0; i < fieldNames.length; i++) {
                    Object value = fieldNames[i].contains(".") ? FieldUtils.getSuperFieldValue(object, fieldNames[i]) : FieldUtils.getFieldValue(object, fieldNames[i]);
                    if (value == null) continue;
                    if(fieldDicMap.containsKey(fieldNames[i])){
                        log.debug("发现有数据字典{}",fieldNames[i]);
                        Object oldValue=value;
                        value=dicCodePool.getByKey(fieldDicMap.get(fieldNames[i]),value.toString());
                        log.debug("查字典将{}转换为{}",value,oldValue);
                    }
                    if (value == null) continue;
                    getCell(row,i).setCellValue(value.toString());
                }
            }
        }
        log.debug("输出完毕！");
        return this;
    }
    /**
     * 添加数据字典
     * @param name 数据字典名称
     * @param maps
     * @return 
     */
    public SimpleExportService addDic(String name, List<Map> maps) {
        dicCodePool.addMap(name, maps);
        return this;
    }

    public SimpleExportService setLanguage(ILanguage language) {
        this.language = language;
        return this;
    }

    /**
     * 添加数据字典
     * @param fieldName 字段名称
     * @param dicCode
     * @return 
     */
    public SimpleExportService setDic(String fieldName,String dicCode) {
        fieldDicMap.put(fieldName,dicCode);
        return this;
    }
    
    public SimpleExportService addDic(String name, String key,String value) {
        dicCodePool.addMap(name, key, value);
        return this;
    }
}

