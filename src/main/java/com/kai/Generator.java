package com.kai;

import com.easy.boot.core.util.PermissionGenerator;
import com.easy.boot.mybatis.gennerator.CodeGenerator;
import com.easy.boot.mybatis.gennerator.DatabaseDocGenerator;

/**
 * @author kai
 */
public class Generator {

    public static void main(String[] args) {
        // 生成代码
        CodeGenerator.execute();
        // 生成数据库文档
        DatabaseDocGenerator.execute();
        // 打印controller方法PreAuthorize注解权限
        PermissionGenerator.execute();
    }

}
