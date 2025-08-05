package com.example.mcpserver.service;


import com.example.mcpserver.dto.author.AuthorRequestDto;
import com.example.mcpserver.model.Author;

import java.util.List;

public interface AuthorService {
    Author getAuthorById(Long id);

    List<Author> getAuthorsByBook(Long bookId);

    List<Author> getAllAuthors();

    Author saveAuthor(AuthorRequestDto author);

    Author updateAuthor(AuthorRequestDto author, Long id);

    boolean deleteAuthorById(Long id);

    void addBookToAuthor(Long authorId, Long bookId);

    void removeBookFromAuthor(Long authorId, Long bookId);
}
