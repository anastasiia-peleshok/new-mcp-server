package com.example.mcpserver.service.impl;

import com.example.mcpserver.dto.author.AuthorRequestDto;
import com.example.mcpserver.model.Author;
import com.example.mcpserver.model.Book;
import com.example.mcpserver.repository.AuthorRepository;
import com.example.mcpserver.repository.BookRepository;
import com.example.mcpserver.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Override
    public Author getAuthorById(Long id) {
        return authorRepository.findById(id).orElseThrow(() -> new RuntimeException("Author Not Found"));
    }

    @Override
    public List<Author> getAuthorsByBook(Long bookId) {
        return authorRepository.findAllAuthorsByBookId(bookId);
    }

    @Override
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    @Override
    @Transactional
    public Author saveAuthor(AuthorRequestDto author) {
        Author authorEntity = new Author();
        authorEntity.setFirstName(author.firstName());
        authorEntity.setLastName(author.lastName());
        return authorRepository.save(authorEntity);
    }

    @Override
    @Transactional
    public Author updateAuthor(AuthorRequestDto author, Long id) {
        Author foundAuthor = authorRepository.findById(id).orElseThrow(() -> new RuntimeException("Author Not Found"));
        foundAuthor.setFirstName(author.firstName());
        foundAuthor.setLastName(author.lastName());
        return authorRepository.save(foundAuthor);
    }

    @Override
    @Transactional
    public boolean deleteAuthorById(Long id) {
        if (authorRepository.existsById(id)) {
            authorRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public void addBookToAuthor(Long authorId, Long bookId) {
        Author author = authorRepository.findById(authorId).orElseThrow(() -> new RuntimeException("Author Not Found"));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book Not Found"));

        author.getBooks().add(book);
        book.getAuthors().add(author);
    }

    @Override
    @Transactional
    public void removeBookFromAuthor(Long authorId, Long bookId) {
        Author author = authorRepository.findById(authorId).orElseThrow(() -> new RuntimeException("Author Not Found"));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book Not Found"));

        author.getBooks().remove(book);
        book.getAuthors().remove(author);
    }
}