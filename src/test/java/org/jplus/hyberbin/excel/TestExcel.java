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

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jplus.hyb.database.bean.FieldColumn;
import org.jplus.hyb.database.config.DbConfig;
import org.jplus.hyb.database.config.SimpleConfigurator;
import org.jplus.hyb.database.crud.Hyberbin;
import org.jplus.hyberbin.excel.bean.CellBean;
import org.jplus.hyberbin.excel.bean.GroupConfig;
import org.jplus.hyberbin.excel.bean.TableBean;
import org.jplus.hyberbin.excel.service.ExportExcelService;
import org.jplus.hyberbin.excel.service.ExportTableService;
import org.jplus.hyberbin.excel.service.ImportExcelService;
import org.jplus.hyberbin.excel.service.SimpleExportService;

/**
 *
 * @author Hyberbin
 */
public class TestExcel {

    static {
        SimpleConfigurator.addConfigurator(new DbConfig("jdbc:mysql://localhost/digitalcampus?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=utf-8&autoReconnect=true", DbConfig.DEFAULT_CONFIG_NAME));
    }

    /**
     * 从List<Map>中导出
     * @param workbook
     * @throws Exception
     */
    public static void testSimpleMapExport(Workbook workbook) throws Exception {
        Hyberbin hyberbin = new Hyberbin();
        List<Map> list = hyberbin.getMapList("select * from dc_xxkc");
        Sheet sheet = workbook.createSheet("testSimpleMapExport");
        List<String> cols = new ArrayList<String>();
        List<FieldColumn> fieldColumns = hyberbin.getFieldColumns();
        for (FieldColumn column : fieldColumns) {
            cols.add(column.getColumn());
        }
        SimpleExportService service = new SimpleExportService(sheet, list, (String[]) cols.toArray(new String[]{}), "学校课程");
        service.setDic("KCLX", "KCLX").addDic("KCLX", "1", "国家课程").addDic("KCLX", "2", "学校课程");//设置数据字典
        service.doExport();
    }

    /**
     * 从List<Vo>中导出
     * @param workbook
     * @throws Exception
     */
    public static void testSimpleVoExport(Workbook workbook) throws Exception {
        Hyberbin<SchoolCourse> hyberbin = new Hyberbin(new SchoolCourse());
        List<SchoolCourse> list = hyberbin.showAll();
        Sheet sheet = workbook.createSheet("testSimpleVoExport");
        //ExportExcelService service = new ExportExcelService(list, sheet, "学校课程");
        ExportExcelService service = new ExportExcelService(list, sheet, new String[]{"id", "courseName", "type"}, "学校课程");
        service.addDic("KCLX", "1", "国家课程").addDic("KCLX", "2", "学校课程");//设置数据字典
        service.doExport();
    }

    /**
     * 从List<Vo>，vo中还有简单循环节中导出
     * @param workbook
     * @throws Exception
     */
    public static void testVoHasListExport(Workbook workbook) throws Exception {
        Hyberbin<SchoolCourse> hyberbin = new Hyberbin(new SchoolCourse());
        List<SchoolCourse> list = hyberbin.showAll();
        List<String> strings = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            strings.add("我是第" + i + "个循环字段");
        }
        for (SchoolCourse course : list) {
            course.setBaseArray(strings);
        }
        Sheet sheet = workbook.createSheet("testVoHasListExport");
        ExportExcelService service = new ExportExcelService(list, sheet, new String[]{"id", "courseName", "type", "baseArray"}, "学校课程");
        service.addDic("KCLX", "1", "国家课程").addDic("KCLX", "2", "学校课程");//设置数据字典
        service.setGroupConfig("baseArray", new GroupConfig(10) {

            @Override
            public String getLangName(int innerIndex, int index) {
                return "我是第" + index + "个循环字段";
            }
        });
        service.doExport();
        service.exportTemplate();//生成下拉框
    }

    /**
     * 从List<Vo>，vo中还有复杂循环节中导出
     * @param workbook
     * @throws Exception
     */
    public static void testVoHasListVoExport(Workbook workbook) throws Exception {
        Hyberbin<SchoolCourse> hyberbin = new Hyberbin(new SchoolCourse());
        List<SchoolCourse> list = hyberbin.showAll();
        for (SchoolCourse course : list) {
            List<InnerVo> innerVos = new ArrayList<InnerVo>();
            for (int i = 0; i < 10; i++) {
                innerVos.add(new InnerVo("key1", "value1"));
            }
            course.setInnerVoArray(innerVos);
        }
        Sheet sheet = workbook.createSheet("testVoHasListVoExport");
        ExportExcelService service = new ExportExcelService(list, sheet, new String[]{"id", "courseName", "type", "innerVoArray"}, "学校课程");
        service.addDic("KCLX", "1", "国家课程").addDic("KCLX", "2", "学校课程");//设置数据字典
        for (int i = 0; i < 10; i++) {
            service.addTook("hiddenvalue", "key", i, "something");
        }
        service.setGroupConfig("innerVoArray", new GroupConfig(2, 10) {
            @Override
            public String getLangName(int innerIndex, int index) {
                return "我是第" + index + "个循环字段,第" + innerIndex + "个属性";
            }
        });
        service.doExport();
    }

    /**
     * 导出一个纵表（课程表之类的）
     * @param workbook
     * @throws Exception
     */
    public static void testTableExport(Workbook workbook) throws Exception {
        Sheet sheet = workbook.createSheet("testTableExport");
        TableBean tableBean = new TableBean(3, 3);
        Collection<CellBean> cellBeans = new HashSet<CellBean>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                CellBean cellBean = new CellBean(i * 3 + j + "", i, j);
                cellBeans.add(cellBean);
            }
        }
        tableBean.setCellBeans(cellBeans);
        ExportTableService tableService = new ExportTableService(sheet, tableBean);
        tableService.doExport();
    }

    /**
     * 从List<Vo>中入
     * @param workbook
     * @throws Exception
     */
    public static void testSimpleVoImport(Workbook workbook) throws Exception {
        Sheet sheet = workbook.getSheet("testSimpleVoExport");
        ImportExcelService service = new ImportExcelService(SchoolCourse.class, sheet);
        service.addDic("KCLX", "1", "国家课程").addDic("KCLX", "2", "学校课程");//设置数据字典
        List list = service.doImport();
        System.out.println("成功导入：" + list.size() + "条数据");
    }

    /**
     * 从List<Vo>，vo中还有简单循环节中导入
     * @param workbook
     * @throws Exception
     */
    public static void testVoHasListImport(Workbook workbook) throws Exception {
        Sheet sheet = workbook.getSheet("testVoHasListExport");
        ImportExcelService service = new ImportExcelService(SchoolCourse.class, sheet);
        service.addDic("KCLX", "1", "国家课程").addDic("KCLX", "2", "学校课程");//设置数据字典
        List list = service.doImport();
        System.out.println("成功导入：" + list.size() + "条数据");
    }

    /**
     * 从List<Vo>，vo中还有复杂循环节中导入
     * @param workbook
     * @throws Exception
     */
    public static void testVoHasListVoImport(Workbook workbook) throws Exception {
        Sheet sheet = workbook.getSheet("testVoHasListVoExport");
        ImportExcelService service = new ImportExcelService(SchoolCourse.class, sheet);
        service.addDic("KCLX", "1", "国家课程").addDic("KCLX", "2", "学校课程");//设置数据字典
        List list = service.doImport();
        System.out.println("成功导入：" + list.size() + "条数据");
    }

    public static void main(String[] args) throws Exception {
        Workbook workbook = new HSSFWorkbook();
        testSimpleMapExport(workbook);
        testSimpleVoExport(workbook);
        testVoHasListExport(workbook);
        testVoHasListVoExport(workbook);
        testTableExport(workbook);
        testSimpleVoImport(workbook);
        testVoHasListImport(workbook);
        testVoHasListVoImport(workbook);
        workbook.write(new FileOutputStream("D:\\excel.xls"));
    }
}
