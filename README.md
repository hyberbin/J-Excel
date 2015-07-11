J-Excel
=======

* Universal Excel import and export tools.
* Support is derived from the List&lt;Map&gt;.
* Support from the List&lt;POJO&gt; import and export.
* Support from inside the List&lt;POJO and List&lt;POJO&gt; inside&gt; import and export.
* To support the export of similar curriculum structure type cross table.
* Support for Internationalization.
* Don't write a configuration file.
* Use the adapter pattern, data import and export support arbitrary types, users can also write your own adapter custom data types!
Example please refer to: test package.
<br/>
<br/>
* 万能的Excel导入导出工具.
* 支持从List&lt;Map&gt;中导出.
* 支持从List&lt;POJO&gt;中导入导出.
* 支持从List&lt;POJO里面还有List&lt;POJO&gt;&gt;中导入导出.
* 支持导出类似课程表结构类型纵表.
* 支持国际化.
* 支持数据字典.
* 支持单元格中下拉框数据校验.
* 支持自动标红错误的数据.
* 支持模板校验，用hash值相加校验模板中的数据是否被改动过.
* 支持自由交换表格中的行或者列.
* 不写一个配置文件.
* 用到了适配器模式，支持任意类型的数据导入导出，用户还可以自己编写适配器操作自定义的数据类型！
<br/>
示例请参照：<br/>
```java

package org.jplus.hyberbin.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jplus.hyberbin.excel.bean.CellBean;
import org.jplus.hyberbin.excel.bean.GroupConfig;
import org.jplus.hyberbin.excel.bean.TableBean;
import org.jplus.hyberbin.excel.service.ExportExcelService;
import org.jplus.hyberbin.excel.service.ExportTableService;
import org.jplus.hyberbin.excel.service.ImportExcelService;
import org.jplus.hyberbin.excel.service.SimpleExportService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

/**
 *
 * @author Hyberbin
 */
public class TestExcel {
    private Workbook workbook;

    @BeforeClass
    public static void setUpClass() {
    }

    @Before
    public void setUp() {
        workbook = new HSSFWorkbook();

    }
    private static Map buildMap(String id,String kcmc,String kclx){
        Map map=new HashMap();
        map.put("id", id);
        map.put("kcmc", kcmc);
        map.put("kclx", kclx);
        return map;
    }
    private static List<SchoolCourse> getList(){
        List<SchoolCourse> list=new ArrayList<SchoolCourse>();
        list.add(new SchoolCourse("1", "语文","1"));
        list.add(new SchoolCourse("2", "数学","1"));
        list.add(new SchoolCourse("3", "英语","1"));
        list.add(new SchoolCourse("4", "政治","2"));
        list.add(new SchoolCourse("5", "历史","2"));
        list.add(new SchoolCourse("6", "地理","2"));
        list.add(new SchoolCourse("7", "生物","2"));
        list.add(new SchoolCourse("8", "物理","1"));
        list.add(new SchoolCourse("9", "化学","1"));
        list.add(new SchoolCourse("11", "体育","2"));
        list.add(new SchoolCourse("12", "音乐","2"));
        list.add(new SchoolCourse("13", "美术","2"));
        list.add(new SchoolCourse("14", "写字","2"));
        return list;
    }

    private static List<Map> getMapList(){
        List<Map> list=new ArrayList<Map>();
        list.add(buildMap("1", "语文","1"));
        list.add(buildMap("2", "数学","1"));
        list.add(buildMap("3", "英语","1"));
        list.add(buildMap("4", "政治","2"));
        list.add(buildMap("5", "历史","2"));
        list.add(buildMap("6", "地理","2"));
        list.add(buildMap("7", "生物","2"));
        list.add(buildMap("8", "物理","1"));
        list.add(buildMap("9", "化学","1"));
        list.add(buildMap("11", "体育","2"));
        list.add(buildMap("12", "音乐","2"));
        list.add(buildMap("13", "美术","2"));
        list.add(buildMap("14", "写字","2"));
        return list;
    }
    /**
     * 从List<Map>中导出
     * @throws Exception
     */
    @Test
    public void testSimpleMapExport() throws Exception {
        Sheet sheet = workbook.createSheet("testSimpleMapExport");
        SimpleExportService service = new SimpleExportService(sheet, getMapList(), new String[]{"id","KCMC","KCLX"}, "学校课程");
        service.setDic("KCLX", "KCLX").addDic("KCLX", "1", "国家课程").addDic("KCLX", "2", "学校课程");//设置数据字典
        service.doExport();
    }

    /**
     * 从List<Vo>中导出
     * @throws Exception
     */
    @Test
    public void testSimpleVoExport() throws Exception {
        Sheet sheet = workbook.createSheet("testSimpleVoExport");
        //ExportExcelService service = new ExportExcelService(list, sheet, "学校课程");
        ExportExcelService service = new ExportExcelService(getList(), sheet, new String[]{"id", "courseName", "type"}, "学校课程");
        service.addDic("KCLX", "1", "国家课程").addDic("KCLX", "2", "学校课程");//设置数据字典
        service.doExport();
    }

    /**
     * 从List<Vo>，vo中还有简单循环节中导出
     * @throws Exception
     */
    @Test
    public void testVoHasListExport() throws Exception {
        List<String> strings = new ArrayList<String>();
        List<SchoolCourse> list = getList();
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
     * @throws Exception
     */
    @Test
    public void testVoHasListVoExport() throws Exception {
        List<SchoolCourse> list = getList();
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
     * @throws Exception
     */
    @Test
    public void testTableExport() throws Exception {
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
     * @throws Exception
     */
    @Test
    public void testSimpleVoImport() throws Exception {
        testSimpleVoExport();
        Sheet sheet = workbook.getSheet("testSimpleVoExport");
        ImportExcelService service = new ImportExcelService(SchoolCourse.class, sheet);
        service.addDic("KCLX", "1", "国家课程").addDic("KCLX", "2", "学校课程");//设置数据字典
        List list = service.doImport();
        System.out.println("成功导入：" + list.size() + "条数据");
    }

    /**
     * 从List<Vo>，vo中还有简单循环节中导入
     * @throws Exception
     */
    @Test
    public void testVoHasListImport() throws Exception {
        testVoHasListExport();
        Sheet sheet = workbook.getSheet("testVoHasListExport");
        ImportExcelService service = new ImportExcelService(SchoolCourse.class, sheet);
        service.addDic("KCLX", "1", "国家课程").addDic("KCLX", "2", "学校课程");//设置数据字典
        List list = service.doImport();
        System.out.println("成功导入：" + list.size() + "条数据");
    }

    /**
     * 从List<Vo>，vo中还有复杂循环节中导入
     * @throws Exception
     */
    @Test
    public void testVoHasListVoImport() throws Exception {
        testVoHasListVoExport();
        Sheet sheet = workbook.getSheet("testVoHasListVoExport");
        ImportExcelService service = new ImportExcelService(SchoolCourse.class, sheet);
        service.addDic("KCLX", "1", "国家课程").addDic("KCLX", "2", "学校课程");//设置数据字典
        List list = service.doImport();
        System.out.println("成功导入：" + list.size() + "条数据");
    }
}

```
