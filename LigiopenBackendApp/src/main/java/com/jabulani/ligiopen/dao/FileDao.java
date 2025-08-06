package com.jabulani.ligiopen.dao;

import com.jabulani.ligiopen.model.tables.File;

public interface FileDao {
    File getFileById(Integer fileId);
    String deleteFile(Integer fileId);
}
