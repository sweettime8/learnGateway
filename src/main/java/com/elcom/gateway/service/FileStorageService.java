package com.elcom.gateway.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author anhdv
 */
public interface FileStorageService {

    Resource loadFileAsResource(String fileName);
    String storeFile(MultipartFile file);
}