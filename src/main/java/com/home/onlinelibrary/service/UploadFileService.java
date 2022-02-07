package com.home.onlinelibrary.service;

import java.io.File;

public interface UploadFileService {
    File getFilePath(String modifiedFileName, String path);
}
