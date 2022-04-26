package com.kai.controller.common;

import cn.hutool.core.io.FileUtil;
import com.kai.config.UploadConfig;
import com.kai.config.api.Result;
import com.kai.config.api.exception.ApiException;
import com.kai.util.MyStringUtil;
import com.kai.bo.vo.BaseUploadUrlVO;
import com.kai.bo.vo.FileVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author kai
 * @date 2022/3/12 14:38
 */
@Slf4j
@Api(tags = "文件上传")
@RestController
@RequestMapping("/api/file")
public class FileController {

    @Autowired
    UploadConfig uploadConfig;

    @PostMapping(value = "upload")
    @ApiOperation(value = "上传文件", notes = "上传文件")
    public Result<FileVO> upload(@RequestPart(value = "file") MultipartFile file) {
        //上传文件存储根目录
        String path = uploadConfig.getPath();
        //上传文件上级目录
        String parentDirectory = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM")) + File.separator;
        //随机文件名
        String fileName = MyStringUtil.getUuid() + MyStringUtil.getFileSuffix(file.getOriginalFilename());
        //创建文件并复制上传文件内容
        File targetFile = FileUtil.touch(path + parentDirectory + fileName);
        try (InputStream inputStream = file.getInputStream()) {
            FileUtil.writeFromStream(inputStream, targetFile);
        } catch (IOException e) {
            log.error("上传文件发生异常", e);
            throw new ApiException("上传文件发生异常");
        }
        FileVO fileDTO = new FileVO();
        fileDTO.setName(fileName);
        fileDTO.setUri(parentDirectory + fileName);
        return Result.success(fileDTO);
    }

    @GetMapping("/getBaseUploadUrl")
    @ApiOperation(value = "获取文件地址前缀", notes = "获取文件地址前缀")
    public Result<BaseUploadUrlVO> getBaseUploadUrl() {
        BaseUploadUrlVO baseUploadUrlDTO = new BaseUploadUrlVO();
        baseUploadUrlDTO.setBaseUploadUrl(uploadConfig.getUrl());
        return Result.success(baseUploadUrlDTO);
    }

}
