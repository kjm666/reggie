package com.itheima.reggie.controller;

import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/*
文件上传下载
 */
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {
    @Value("${reggie.path}")
    private String basePath;

    /**
     * description: 文件上传
     *
     * @since: 1.0.0
     * @author: KangJiaMing
     * @date: 2023/3/16 20:31
     * @Param file:
     * @return: com.itheima.reggie.common.R<java.lang.String>
     */
    @PostMapping("/upload")
    //参数名只能是file，需要转存在临时文件夹中,否则上传完成临时文件会删除
    public R<String> upload(MultipartFile file) {
        log.info("=====================");
        //获得文件原始名
        String originalFilename = file.getOriginalFilename();
        //使用uuid重新生成文件名，防止重复文件名覆盖
        String fileName = UUID.randomUUID().toString();
        //获取文件后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //文件名==前缀+后缀
        fileName = fileName+suffix;
        //创建目录对象
        File dir = new File(basePath);
        //判断当前目录是否存在
        if (!dir.exists()) {
            //创建文件
            dir.mkdirs();
        }
        //将临时文件转存到指定位置
        try {
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //返回文件名
        return R.success(fileName);
    }

    /**
     * description: 文件下载
     *
     * @since: 1.0.0
     * @author: KangJiaMing
     * @date: 2023/3/16 21:19
     * @Param name:
     * @Param response:
     * @return: com.itheima.reggie.common.R<java.lang.String>
     */
    @RequestMapping("/download")
    public void download(String name, HttpServletResponse response) {
        log.info("文件名:"+name);
        //输入流,通过输入流读取文件内容
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
            //获取输出流,通过输出流将文件写回到浏览器
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != 1) {
                outputStream.write(bytes, 0, len);
                //刷新
                outputStream.flush();
            }
            //关闭流
            fileInputStream.close();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
