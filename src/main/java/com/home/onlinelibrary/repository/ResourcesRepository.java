package com.home.onlinelibrary.repository;

import com.home.onlinelibrary.domain.Book;
import com.home.onlinelibrary.domain.Resource;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ResourcesRepository extends CrudRepository<Book, Long> {

    //todo: получать вместе с книгой
    @Transactional
    @Query("select new Resource(r.id, r.icon) from Resource r WHERE r.id in (:ids)")
    List<Resource> findIconByIds(@Param("ids") List<Long> ids);
}
