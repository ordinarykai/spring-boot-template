package com.kai.controller.common;

import com.easy.boot.core.api.Result;
import com.easy.boot.core.upload.UploadUtil;
import com.easy.boot.core.util.bo.FileVO;
import com.kai.dto.BaseUploadUrlVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author kai
 * @date 2022/3/12 14:38
 */
@Slf4j
@Api(tags = "文件上传")
@RestController
@RequestMapping("/file")
public class FileController {

    @PostMapping
    @ApiOperation(value = "上传文件", notes = "上传文件")
    public Result<FileVO> upload(@RequestPart(value = "file") MultipartFile file) throws IOException {
        FileVO fileVO = UploadUtil.uploadToLocal(file);
        return Result.success(fileVO);
    }

    @GetMapping("/baseUploadUrl")
    @ApiOperation(value = "获取文件地址前缀", notes = "获取文件地址前缀")
    public Result<BaseUploadUrlVO> getBaseUploadUrl() {
        BaseUploadUrlVO baseUploadUrlDTO = new BaseUploadUrlVO();
        baseUploadUrlDTO.setBaseUploadUrl(UploadUtil.getBaseUploadUrl());
        return Result.success(baseUploadUrlDTO);
    }

}
