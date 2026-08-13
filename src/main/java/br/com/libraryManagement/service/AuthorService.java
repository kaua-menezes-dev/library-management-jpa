package br.com.libraryManagement.service;

import br.com.libraryManagement.dao.AuthorDAO;
import br.com.libraryManagement.dao.BookDAO;
import br.com.libraryManagement.model.entity.Author;
import br.com.libraryManagement.model.entity.Book;
import br.com.libraryManagement.model.exceptions.AuthorNotFoundException;
import br.com.libraryManagement.model.exceptions.BookNotFoundException;
import br.com.libraryManagement.util.Validator;
import jakarta.persistence.EntityManager;

import java.util.List;

public class AuthorService {

    private AuthorDAO authorDAO;
    private BookDAO bookDAO;
    private EntityManager entityManager;

    public AuthorService(EntityManager entityManager) {
        this.authorDAO = new AuthorDAO(entityManager);
        this.bookDAO = new BookDAO(entityManager);
        this.entityManager = entityManager;
    }

    public void registerAuthor(String name) {
        validateAuthorData(name);

        try {
            entityManager.getTransaction().begin();

            Author author = new Author(name);
            authorDAO.registerAuthor(author);

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }

            throw e;
        }
    }

    public void addAuthorToBooks(Long authorId, Long bookId) {
        Validator.requireNotNull(bookId, "ID do livro");
        Validator.requireNotNull(authorId, "ID do autor");

        try {
            entityManager.getTransaction().begin();

            Book book = bookDAO.findByBookId(bookId);
            requireExistBook(book);

            Author author = authorDAO.findByAuthorId(authorId);
            requireExistAuthor(author);

            author.addBook(book);

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }

            throw e;
        }
    }

    public void removeAuthorById(Long id) {
        Validator.requireNotNull(id, "ID do autor");

        try {
            entityManager.getTransaction().begin();

            Author author = authorDAO.findByAuthorId(id);
            requireExistAuthor(author);

            authorDAO.removeAuthor(author);

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }

            throw e;
        }
    }

    public void updateAuthorName(Long id, String newAuthorName) {
        Validator.requireNotNull(id, "ID do autor");
        Validator.requireNameLength(newAuthorName, "Novo nome do autor");

        try {
            entityManager.getTransaction().begin();

            Author author = authorDAO.findByAuthorId(id);
            requireExistAuthor(author);

            author.updateAuthorName(newAuthorName);

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }

            throw e;
        }
    }

    public Author findByAuthorId(Long id) {
        Validator.requireNotNull(id, "ID do autor");
        Author author = authorDAO.findByAuthorId(id);
        requireExistAuthor(author);
        return author;
    }

    public Author findByAuthorName(String authorName) {
        Validator.requireNameLength(authorName, "Nome do autor");
        Author author = authorDAO.findByAuthorName(authorName);
        requireExistAuthor(author);
        return author;
    }

    public List<Author> findAllAuthors() {
        List<Author> authors = authorDAO.findAllAuthors();
        requireExistAuthorOnList(authors);
        return authors;
    }

    public List<Author> findAuthorByBookTitle(String bookTitle) {
        Validator.requireTitleLength(bookTitle, "Titulo do livro");
        List<Author> authors = authorDAO.findAuthorByBookTitle(bookTitle);
        requireExistAuthorOnList(authors);
        return authors;
    }

    private void requireExistAuthorOnList(List<Author> authors) {
        if (authors == null || authors.isEmpty()) {
            throw new AuthorNotFoundException("Nenhum autor encontrado.");
        }
    }

    private void requireExistAuthor(Author author) {
        if (author == null) {
            throw new AuthorNotFoundException("Autor não encontrado.");
        }
    }

    private void requireExistBook(Book book) {
        if (book == null) {
            throw new BookNotFoundException("Livro não encontrado.");
        }
    }

    private void validateAuthorData(String name) {
        Validator.requireNameLength(name, "Nome do autor");
    }
}
