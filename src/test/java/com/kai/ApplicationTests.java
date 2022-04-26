package com.kai;

import com.kai.service.PermissionService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
class ApplicationTests {

    @Autowired
    private PermissionService permissionService;

    @Test
    public void contextLoads() {
        permissionService.getTree(null);
    }

}
