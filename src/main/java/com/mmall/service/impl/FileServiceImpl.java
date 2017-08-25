package com.mmall.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.mmall.service.IFileServer;
import com.mmall.util.FTPUtil;

@Service("iFileServer")
public class FileServiceImpl implements IFileServer {
	private  final Logger logger=LoggerFactory.getLogger(FileServiceImpl.class);
	
	@Override
	public String upload(MultipartFile file ,String path){
		String fileName=file.getOriginalFilename();
		String fileExtensionName=fileName.substring(fileName.lastIndexOf(".")+1);
		//防止文件重名
		String uploadFileName=UUID.randomUUID().toString()+"."+fileExtensionName;
		logger.info("开始上传文件，上传文件的文件名:{},上传的路径:{},新文件名:{}",fileName,path,uploadFileName);
		
		File fileDir=new File(path);
		if(!fileDir.exists()){
			fileDir.setWritable(true);
			//创建多重文件夹
			fileDir.mkdirs();
		}
		File targetFile =new File(path,uploadFileName);
		try {
			file.transferTo(targetFile);
			//将targetFile上传到FTP服务器上
			FTPUtil.uploadFile(Lists.newArrayList(targetFile));
			//上传完后，删除upload下面的文件夹；
			targetFile.delete();
			
		} catch (IllegalStateException | IOException e) {
			logger.error("上传文件异常", e);
			return null;
		}
		return targetFile.getName();
	}
}
