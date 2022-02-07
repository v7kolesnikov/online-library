package com.home.onlinelibrary.service;

import com.home.onlinelibrary.domain.Book;

import java.util.List;

public interface BooksService {
    List<Book> getAllBooks();

    Book getBookById(Long bookId);

    Book getBookByIdAndFetchResource(Long bookId);

    void save(Book book);

    void updateBook(Book book);

    List<Book> findBooks(String searchText);

    void deleteBook(Book book);
}
