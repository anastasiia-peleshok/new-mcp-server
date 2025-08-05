package com.example.mcpserver.service;


import com.example.mcpserver.dto.book.BookRequestDto;
import com.example.mcpserver.model.Book;

import java.util.List;

public interface BookService {
    Book getBookById(Long id);

    List<Book> getBooksByAuthorId(Long authorId);

    List<Book> getAllBooks();

    Book saveBook(BookRequestDto book);

    Book updateBook(BookRequestDto book, Long id);

    boolean deleteBookById(Long id);

    void addAuthorToBook(Long bookId, Long authorId);

    void removeAuthorFromBook(Long bookId, Long authorId);
}
