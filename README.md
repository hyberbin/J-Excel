J-Excel
=======

Universal Excel import and export tools.<br/>
Support is derived from the List&lt;Map&gt;.<br/>
Support from the List&lt;POJO&gt; import and export.<br/>
Support from inside the List&lt;POJO and List&lt;POJO&gt; inside&gt; import and export.<br/>
To support the export of similar curriculum structure type cross table.<br/>
Support for Internationalization.<br/>
Don't write a configuration file.<br/>
Use the adapter pattern, data import and export support arbitrary types, users can also write your own adapter custom data types!<br/>
Example please refer to: test package.<br/>
<br/>
<br/>
万能的Excel导入导出工具.<br/>
支持从List&lt;Map&gt;中导出.<br/>
支持从List&lt;POJO&gt;中导入导出.<br/>
支持从List&lt;POJO里面还有List&lt;POJO&gt;&gt;中导入导出.<br/>
支持导出类似课程表结构类型纵表.<br/>
支持国际化.<br/>
支持数据字典.<br/>
支持单元格中下拉框数据校验.<br/>
支持自动标红错误的数据.<br/>
支持模板校验，用hash值相加校验模板中的数据是否被改动过.<br/>
支持自由交换表格中的行或者列.<br/>
不写一个配置文件.<br/>
用到了适配器模式，支持任意类型的数据导入导出，用户还可以自己编写适配器操作自定义的数据类型！
<br/>
示例请参照：<br/>
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
