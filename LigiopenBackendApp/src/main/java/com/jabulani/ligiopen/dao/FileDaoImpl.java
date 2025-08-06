package com.jabulani.ligiopen.dao;

import com.jabulani.ligiopen.model.tables.File;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class FileDaoImpl implements FileDao{
    private final EntityManager entityManager;
    @Autowired
    public FileDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public File getFileById(Integer fileId) {
        TypedQuery<File> query = entityManager.createQuery("from File where id = :fileId", File.class);
        query.setParameter("fileId", fileId);
        return query.getSingleResult();
    }

    @Override
    public String deleteFile(Integer fileId) {
        Query query = entityManager.createQuery("delete from File where id = :fileId");
        query.setParameter("fileId", fileId);
        return "Deleted "+query.executeUpdate()+" rows";
    }
}
