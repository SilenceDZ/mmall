package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

public interface IFileServer {
	public String upload(MultipartFile file ,String path);
}
