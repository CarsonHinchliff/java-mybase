package cn.strivers.mybase.web.util;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

/**
 * MultipartFile和File互转工具类
 *
 * @author mozhu
 * @date 2023/11/20 13:19
 */
@Slf4j
public class MultipartFileUtil {

    /**
     * 输入流转MultipartFile
     *
     * @param fileName
     * @param inputStream
     * @return
     */
    public static MultipartFile getMultipartFile(String fileName, InputStream inputStream) {
        MultipartFile multipartFile = null;
        try {
            multipartFile = new MockMultipartFile(fileName, fileName, MediaType.MULTIPART_FORM_DATA_VALUE, inputStream);
        } catch (IOException e) {
            log.error("文件转换异常", e);
        }
        return multipartFile;
    }

    /**
     * 读取网络文件
     *
     * @param url      文件地址
     * @param fileName 文件名称（需带文件名后缀）
     * @return
     */
    public static MultipartFile getMultipartFile(String url, String fileName) {
        InputStream inputStream;
        try (HttpResponse response = HttpRequest.get(url).execute()) {
            inputStream = response.bodyStream();
        }
        return getMultipartFile(fileName, inputStream);
    }

    /**
     * File 转MultipartFile
     *
     * @param file
     * @return
     */
    public static MultipartFile getMultipartFile(File file) {
        FileInputStream fileInputStream = null;
        MultipartFile multipartFile = null;
        try {
            fileInputStream = new FileInputStream(file);
            multipartFile = new MockMultipartFile(file.getName(), file.getName(), MediaType.MULTIPART_FORM_DATA_VALUE, fileInputStream);
        } catch (IOException e) {
            log.error("文件转换异常", e);
        }
        return multipartFile;
    }

    /**
     * MultipartFileUtil 转File
     *
     * @param multipartFile
     * @return
     */
    public static File getFile(MultipartFile multipartFile) {
        if (multipartFile == null) {
            return null;
        }
        String fileName = multipartFile.getOriginalFilename();
        if (fileName == null) {
            return null;
        }
        File file = new File(fileName);
        OutputStream out = null;
        try {
            out = Files.newOutputStream(file.toPath());
            byte[] ss = multipartFile.getBytes();
            for (byte s : ss) {
                out.write(s);
            }
        } catch (IOException e) {
            log.error("文件转换异常", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    log.error("文件流关闭异常", e);
                }
            }
        }
        return file;
    }

}
