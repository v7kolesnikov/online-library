package com.home.onlinelibrary.repository;

import com.home.onlinelibrary.domain.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BooksRepository extends CrudRepository<Book, Long> {

    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrGenreContainingIgnoreCase(String title, String author, String genre);

    @Transactional
    @Query("from Book b join b.resource r WHERE b.id = (:id)")
    Book findByIdAndFetchResource(@Param("id") Long id);
}