package com.example.mcpserver.service.impl;

import com.example.mcpserver.dto.book.BookRequestDto;
import com.example.mcpserver.model.Author;
import com.example.mcpserver.model.Book;
import com.example.mcpserver.repository.AuthorRepository;
import com.example.mcpserver.repository.BookRepository;
import com.example.mcpserver.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book Not Found"));
    }

    @Override
    public List<Book> getBooksByAuthorId(Long authorId){
        return bookRepository.findAllBooksByAuthorId(authorId);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    @Transactional
    public Book saveBook(BookRequestDto book) {
        Book bookEntity = new Book();
        bookEntity.setTitle(book.title());
        return bookRepository.save(bookEntity);
    }

    @Override
    @Transactional
    public Book updateBook(BookRequestDto book, Long id) {
        Book foundBook = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book Not Found"));
        foundBook.setTitle(book.title());
        return bookRepository.save(foundBook);
    }

    @Override
    @Transactional
    public boolean deleteBookById(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public void addAuthorToBook(Long bookId, Long authorId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book Not Found"));
        Author author = authorRepository.findById(authorId).orElseThrow(() -> new RuntimeException("Author Not Found"));
        
        book.getAuthors().add(author);
        author.getBooks().add(book);
    }

    @Override
    @Transactional
    public void removeAuthorFromBook(Long bookId, Long authorId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book Not Found"));
        Author author = authorRepository.findById(authorId).orElseThrow(() -> new RuntimeException("Author Not Found"));

        book.getAuthors().remove(author);
        author.getBooks().remove(book);
    }
}
