package com.qingli.projectmanager.repositories;

import com.qingli.projectmanager.domian.Backlog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BacklogRepository extends CrudRepository<Backlog, Long> {
    Backlog findByProjectIdentifier(String Identifier);
}
