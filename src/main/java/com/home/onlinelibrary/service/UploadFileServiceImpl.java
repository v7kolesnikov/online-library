package com.home.onlinelibrary.service;

import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import java.io.File;

@Service
public class UploadFileServiceImpl implements UploadFileService {
   private final ServletContext context;

    public UploadFileServiceImpl(ServletContext context) {
        this.context = context;
    }

    @Override
    public File getFilePath(String modifiedFileName, String path) {
        boolean exists = new File(context.getRealPath("/" + path + "/")).exists();
        if (!exists) {
            new File(context.getRealPath("/" + path + "/")).mkdir();
        }
        String modifiedFilePath = context.getRealPath("/" + path + "/" + File.separator + modifiedFileName);
        File file = new File(modifiedFilePath);
        return file;
    }
}