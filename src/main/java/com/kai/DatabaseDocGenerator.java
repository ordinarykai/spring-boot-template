package com.kai;

import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineFileType;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import cn.smallbun.screw.core.execute.DocumentationExecute;
import cn.smallbun.screw.core.process.ProcessConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * 数据库文档生成
 *
 * @author kai
 */
public class DatabaseDocGenerator {

    private static final String FILE_OUTPUT_DIR = "/application/spring-boot-template/sql";
    private static final String FILE_NAME = "database-doc";
    private static final String VERSION = "1.0";
    private static final String DESCRIPTION = "数据库设计文档";

    private static final String[] IGNORE_TABLE_NAME = {"test_user","test_group"};
    private static final String[] IGNORE_PREFIX = {"test_"};
    private static final String[] IGNORE_SUFFIX = {"_test"};

    private static final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
    private static final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/spring_boot_template";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";

    public static void main(String[] args) {
        documentGeneration();
    }

    /**
     * 文档生成
     */
    public static void documentGeneration() {
        // 配置
        Configuration config = Configuration.builder()
                // 版本
                .version(VERSION)
                // 描述
                .description(DESCRIPTION)
                // 数据源
                .dataSource(dataSource())
                // 文件生成配置
                .engineConfig(engineConfig())
                // 数据处理
                .produceConfig(processConfig())
                .build();
        // 执行生成
        new DocumentationExecute(config).execute();
    }

    /**
     * 获取数据源
     */
    private static DataSource dataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(DRIVER_CLASS_NAME);
        hikariConfig.setJdbcUrl(JDBC_URL);
        hikariConfig.setUsername(USERNAME);
        hikariConfig.setPassword(PASSWORD);
        // 设置可以获取tables remarks信息
        hikariConfig.addDataSourceProperty("useInformationSchema",
                "true");
        hikariConfig.setMinimumIdle(2);
        hikariConfig.setMaximumPoolSize(5);
        return new HikariDataSource(hikariConfig);
    }

    /**
     * 文件生成配置
     */
    private static EngineConfig engineConfig() {
        return EngineConfig.builder()
                // 生成文件路径
                .fileOutputDir(FILE_OUTPUT_DIR)
                // 打开目录
                .openOutputDir(true)
                // 文件类型
                .fileType(EngineFileType.HTML)
                // 生成模板实现
                .produceType(EngineTemplateType.freemarker)
                // 自定义文件名称
                .fileName(FILE_NAME).build();
    }

    /**
     * 数据处理
     */
    private static ProcessConfig processConfig() {
        // 指定生成逻辑
        // 当存在指定表、指定表前缀、指定表后缀时，将生成指定表
        // 其余表不生成，并跳过忽略表配置
        return ProcessConfig.builder()
                // 根据名称指定表生成
                .designatedTableName(new ArrayList<>())
                // 根据表前缀生成
                .designatedTablePrefix(new ArrayList<>())
                // 根据表后缀生成
                .designatedTableSuffix(new ArrayList<>())
                // 忽略表名
                .ignoreTableName(Arrays.asList(IGNORE_PREFIX))
                // 忽略表前缀
                .ignoreTablePrefix(Arrays.asList(IGNORE_SUFFIX))
                // 忽略表后缀
                .ignoreTableSuffix(Arrays.asList(IGNORE_TABLE_NAME)).build();
    }

}
