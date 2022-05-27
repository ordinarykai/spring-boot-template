//package com.kai;
//
//import cn.hutool.core.lang.Assert;
//import com.baomidou.mybatisplus.annotation.DbType;
//import com.baomidou.mybatisplus.annotation.IdType;
//import com.baomidou.mybatisplus.core.toolkit.StringPool;
//import com.baomidou.mybatisplus.generator.AutoGenerator;
//import com.baomidou.mybatisplus.generator.InjectionConfig;
//import com.baomidou.mybatisplus.generator.config.*;
//import com.baomidou.mybatisplus.generator.config.po.TableFill;
//import com.baomidou.mybatisplus.generator.config.po.TableInfo;
//import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
//import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
//import lombok.Data;
//import org.springframework.cglib.beans.BeanMap;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static com.baomidou.mybatisplus.annotation.FieldFill.INSERT_UPDATE;
//import static com.kai.CodeGenerator.DataSourceConfigParam.*;
//import static com.kai.CodeGenerator.GlobalConfigParam.*;
//import static com.kai.CodeGenerator.StrategyConfigParam.*;
//
///**
// * @author kai
// * @date 2022/3/12 14:41
// */
//public class CodeGenerator {
//
//    // 代码生成器示例（旧）
//    // https://baomidou.com/pages/d357af/
//    // 代码生成器配置说明（旧）
//    // https://baomidou.com/pages/061573/
//
//    public static void main(String[] args) {
//        // 传入要生成的表名, 不传代表全部表生成
//        executeAutoGenerator("spring_boot_template","t_admin");
//    }
//
//    /**
//     * 数据源配置
//     */
//    public static class DataSourceConfigParam {
//        // 数据库类型
//        public static DbType dbType = DbType.MYSQL;
//        // 数据库主机地址
//        public static String host = "127.0.0.1";
//        // 数据库端口
//        public static String port = "3306";
//        // 数据库驱动
//        public static String driverName = "com.mysql.cj.jdbc.Driver";
//        // 数据库密码
//        public static String userName = "root";
//        // 数据库用户名
//        public static String password = "123456";
//    }
//
//    /**
//     * 全局配置
//     */
//    public static class GlobalConfigParam {
//        // 输出目录
//        public static String outPutDir = "/application/code-generator/main";
//        // 作者
//        public static String author = "kai";
//        // 是否打开文件夹
//        public static boolean open = true;
//        // 是否启用实体类swagger2注解
//        public static boolean swagger2 = true;
//        // 是否开启ActiveRecord模式
//        public static boolean activeRecord = true;
//        // 是否覆盖原文件
//        public static boolean fileOverride = true;
//        // 是否生成xml通用查询映射结果
//        public static boolean baseResultMap = true;
//        // 是否生成xml通用查询结果列
//        public static boolean baseColumnList = true;
//        // Service文件名称方式，例如：%sService生成UserService，%s为占位符
//        public static String serviceName = "%sService";
//        // 实体类名称（%s指代对应表名）
////        public static String entityName = "%sEntity";
//        // 主键生成策略
//        public static IdType idType = IdType.ASSIGN_UUID;
//    }
//
//    /**
//     * 包名配置
//     */
//    public static class PackageConfigParam {
//        // 父级包名
//        public static String parent = "com.kai";
//        // 模块名
//        public static String moduleName = "";
//    }
//
//    /**
//     * 自定义配置
//     */
//    @Data
//    public static class InjectionConfigParam {
//        // controller请求前缀
//        private final String api = "/api";
//    }
//
//    /**
//     * 模板配置
//     */
//    public static class TemplateConfigParam {
//        public static String controller = "/templates/controller.java";
//        public static String service = "/templates/service.java";
//        public static String serviceImpl = "/templates/serviceImpl.java";
//        public static String mapper = "/templates/mapper.java";
//        public static String entity = "/templates/entity.java";
//        public static String xml = "/templates/mapper.xml";
//    }
//
//    /**
//     * 策略配置
//     */
//    public static class StrategyConfigParam {
//        // 数据库表映射到实体的命名策略
//        public static NamingStrategy naming = NamingStrategy.underline_to_camel;
//        // 数据库表字段映射到实体的命名策略, 未指定按照 naming 执行
//        public static NamingStrategy columnNaming = NamingStrategy.underline_to_camel;
//        // 表名前缀，生成实体类的时候不带这些前缀
//        public static String[] tablePrefix = {"t_", "s_", "r_"};
//        // 你自己的父类实体,没有就不用设置!
////        public static Class<?> superEntity = SuperEntity.class;
//        // 【实体】是否为链式模型（默认 false）（since 3.3.2）
//        public static boolean chainModel = true;
//        // 【实体】是否为lombok模型（默认 false）
//        public static boolean entityLombokModel = true;
//        // 是否生成 @RestController 控制器
//        public static boolean restControllerStyle = true;
//        // 是否驼峰转连字符
//        public static boolean controllerMappingHyphenStyle = true;
//        // 表填充字段
//        public static List<TableFill> tableFillList = new ArrayList<TableFill>() {{
//            add(new TableFill("update_time", INSERT_UPDATE));
//        }};
//    }
//
//    /**
//     * @param database 数据库名
//     * @param tables   要生成的表名。不传代表全部表生成
//     */
//    private static void executeAutoGenerator(String database,
//                                             String... tables) {
//
//        Assert.notBlank(database);
//
//        // 代码生成器
//        AutoGenerator autoGenerator = new AutoGenerator();
//
//        // 全局配置
//        GlobalConfig globalConfig = new GlobalConfig()
//                .setOutputDir(GlobalConfigParam.outPutDir + "/java")
//                .setAuthor(author)
//                .setOpen(open)
//                .setSwagger2(swagger2)
//                .setActiveRecord(activeRecord)
//                .setFileOverride(fileOverride)
//                .setBaseResultMap(baseResultMap)
//                .setBaseColumnList(baseColumnList)
//                .setServiceName(serviceName)
////                .setEntityName(entityName)
//                .setIdType(idType);
//        autoGenerator.setGlobalConfig(globalConfig);
//
//        // 数据源配置
//        DataSourceConfig dataSourceConfig = new DataSourceConfig()
//                .setDbType(dbType)
//                .setUrl("jdbc:" + dbType.getDb() + "://" + host + ":" + port + "/" + database + "?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8")
//                .setDriverName(driverName)
//                .setUsername(userName)
//                .setPassword(password);
//        autoGenerator.setDataSource(dataSourceConfig);
//
//        // 包名配置
//        PackageConfig packageConfig = new PackageConfig()
//                .setParent(PackageConfigParam.parent)
//                .setModuleName(PackageConfigParam.moduleName);
//        autoGenerator.setPackageInfo(packageConfig);
//
//        // 自定义配置
//        InjectionConfig injectionConfig = new InjectionConfig() {
//            @Override
//            @SuppressWarnings("unchecked")
//            public void initMap() {
//                this.setMap(BeanMap.create(new InjectionConfigParam()));
//            }
//        };
//        autoGenerator.setCfg(injectionConfig);
//
//        // 自定义输出配置
//        List<FileOutConfig> fileOutConfigList = new ArrayList<>();
//        fileOutConfigList.add(new FileOutConfig(TemplateConfigParam.xml + ".ftl") {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                return outPutDir + "/resources/mapper/" + packageConfig.getModuleName()
//                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
//            }
//        });
//        injectionConfig.setFileOutConfigList(fileOutConfigList);
//
//        // 配置模板
//        TemplateConfig templateConfig = new TemplateConfig()
//                .setController(TemplateConfigParam.controller)
//                .setService(TemplateConfigParam.service)
//                .setServiceImpl(TemplateConfigParam.serviceImpl)
//                .setMapper(TemplateConfigParam.mapper)
//                .setEntity(TemplateConfigParam.entity)
//                .setXml(null);
//        autoGenerator.setTemplate(templateConfig);
//
//        // 策略配置
//        StrategyConfig strategy = new StrategyConfig()
//                .setNaming(naming)
//                .setColumnNaming(columnNaming)
//                .setTablePrefix(tablePrefix)
////                .setSuperEntityClass(superEntity)
//                .setChainModel(chainModel)
//                .setEntityLombokModel(entityLombokModel)
//                .setRestControllerStyle(restControllerStyle)
//                .setControllerMappingHyphenStyle(controllerMappingHyphenStyle)
//                .setTableFillList(tableFillList)
//                .setInclude(tables);
//        autoGenerator.setStrategy(strategy);
//
//        // 模板引擎
//        autoGenerator.setTemplateEngine(new FreemarkerTemplateEngine());
//
//        // 执行
//        autoGenerator.execute();
//    }
//
//}