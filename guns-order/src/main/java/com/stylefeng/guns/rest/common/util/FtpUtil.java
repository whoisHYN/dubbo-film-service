package com.stylefeng.guns.rest.common.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @Author: HYN
 * @Description:
 * @Date: 2020/4/11 6:07 下午
 * @Modified By:
 */
@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "ftp")
public class FtpUtil {
    /**
     * 地址 端口 用户名 密码
     */
    private String hostName;
    private Integer port;
    private String userName;
    private String password;

    private FTPClient ftpClient = null;

    private void initFTPClient(){
        try{
            ftpClient = new FTPClient();
            ftpClient.setControlEncoding("utf-8");
            ftpClient.connect(hostName, port);
            ftpClient.login(userName, password);
        }catch (Exception e){
            log.error("初始化FTP失败", e);
        }
    }

    /**
     * 输入一个路径，然后将路径里的文件转换成字符串返回
     * @param fileAddress
     * @return
     */
    public String getFileStrByAddress(String fileAddress){
        BufferedReader bufferedReader = null;
        try{
            initFTPClient();
            bufferedReader = new BufferedReader(
                    new InputStreamReader(ftpClient.retrieveFileStream(fileAddress)));

            StringBuilder sb = new StringBuilder();
            while(true){
                String lineStr = bufferedReader.readLine();
                if(lineStr == null){
                    break;
                }
                sb.append(lineStr);
            }
            ftpClient.logout();
            return sb.toString();
        }catch (Exception e){
            log.error("获取文件信息失败",e);
        }finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
