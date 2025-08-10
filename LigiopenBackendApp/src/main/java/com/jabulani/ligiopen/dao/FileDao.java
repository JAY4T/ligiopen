package com.jabulani.ligiopen.dao;

import com.jabulani.ligiopen.entity.file.File;

public interface FileDao {
    File getFileById(Integer fileId);
    String deleteFile(Integer fileId);
}
