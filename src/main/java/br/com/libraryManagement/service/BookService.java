package br.com.libraryManagement.service;

import br.com.libraryManagement.dao.AuthorDAO;
import br.com.libraryManagement.dao.BookDAO;
import br.com.libraryManagement.model.entity.Author;
import br.com.libraryManagement.model.entity.Book;
import br.com.libraryManagement.model.exceptions.AuthorNotFoundException;
import br.com.libraryManagement.model.exceptions.BookAlreadyExistsException;
import br.com.libraryManagement.model.exceptions.BookNotFoundException;
import br.com.libraryManagement.util.Validator;
import jakarta.persistence.EntityManager;

import java.util.List;

public class BookService {

    private BookDAO bookDAO;
    private AuthorDAO authorDAO;
    private EntityManager entityManager;

    public BookService(EntityManager entityManager) {
        this.bookDAO = new BookDAO(entityManager);
        this.authorDAO = new AuthorDAO(entityManager);
        this.entityManager = entityManager;
    }

    public void registerBook(String title, Long idAuthor, Integer publicationYear, String description) {
        validateBookData(title, idAuthor, publicationYear, description);

        Book existingBook = bookDAO.findBookByTitle(title);
        requireBookNotExists(existingBook);

        try {
            entityManager.getTransaction().begin();

            Author author = authorDAO.findByAuthorId(idAuthor);
            requireExistAuthor(author);

            Book book = new Book(title, author, publicationYear, description);
            bookDAO.registerBook(book);

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }

            throw e;
        }
    }

    public void updateBookTitle(Long id, String newBookTitle) {
        Validator.requireNotNull(id, "ID do livro");
        Validator.requireTitleLength(newBookTitle, "Novo titulo do livro");

        try {
            entityManager.getTransaction().begin();

            Book book = bookDAO.findByBookId(id);
            requireExistBook(book);

            book.updateBookTitle(newBookTitle);

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }

            throw e;
        }
    }

    public void updateBookDescription(Long id, String newBookDescription) {
        Validator.requireNotNull(id, "ID do livro");
        Validator.requireDescriptionLength(newBookDescription, "Nova descrição do livro");

        try {
            entityManager.getTransaction().begin();

            Book book = bookDAO.findByBookId(id);
            requireExistBook(book);

            book.updateBookDescription(newBookDescription);

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }

            throw e;
        }
    }

    public void updateBookPublicationYear(Long id, Integer newBookYear) {
        Validator.requireNotNull(id, "ID do livro");
        Validator.requirePublicationYearValid(newBookYear, "Novo ano do livro");

        try {
            entityManager.getTransaction().begin();

            Book book = bookDAO.findByBookId(id);
            requireExistBook(book);

            book.updatePublicationYear(newBookYear);

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }

            throw e;
        }
    }

    public void updateBookAuthor(Long id, Long idAuthor) {
        Validator.requireNotNull(id, "ID do livro");
        Validator.requireNotNull(idAuthor, "ID do autor");

        try {
            entityManager.getTransaction().begin();

            Book book = bookDAO.findByBookId(id);
            requireExistBook(book);

            Author newAuthor = authorDAO.findByAuthorId(idAuthor);
            requireExistAuthor(newAuthor);

            book.updateBookAuthor(newAuthor);

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }

            throw e;
        }
    }

    public void setBookStatusReading(Long id) {
        Validator.requireNotNull(id, "ID do livro");

        try {
            entityManager.getTransaction().begin();

            Book book = bookDAO.findByBookId(id);
            requireExistBook(book);

            book.setBookStatusReading();

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }

            throw e;
        }
    }

    public void setBookStatusWantToRead(Long id) {
        Validator.requireNotNull(id, "ID do livro");

        try {
            entityManager.getTransaction().begin();

            Book book = bookDAO.findByBookId(id);
            requireExistBook(book);

            book.setBookStatusWantToRead();

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }

            throw e;
        }
    }

    public void setBookStatusFinished(Long id) {
        Validator.requireNotNull(id, "ID do livro");

        try {
            entityManager.getTransaction().begin();

            Book book = bookDAO.findByBookId(id);
            requireExistBook(book);

            book.setBookStatusFinished();

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }

            throw e;
        }
    }

    public void removeBookById(Long id) {
        Validator.requireNotNull(id, "ID do livro");

        try {
            entityManager.getTransaction().begin();

            Book book = bookDAO.findByBookId(id);
            requireExistBook(book);

            bookDAO.removeBook(book);

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }

            throw e;
        }
    }

    public Book findBookById(Long id) {
        Validator.requireNotNull(id, "ID do livro");
        Book book = bookDAO.findByBookId(id);
        requireExistBook(book);
        return book;
    }

    public Book findBookByTitle(String bookTitle) {
        Validator.requireTitleLength(bookTitle, "Nome do livro");
        Book book = bookDAO.findBookByTitle(bookTitle);
        requireExistBook(book);
        return book;
    }

    public List<Book> findBookByAuthorName(String authorName) {
        Validator.requireNameLength(authorName, "Nome do autor");
        List<Book> books = bookDAO.findBookByAuthor(authorName);
        requireExistBookOnList(books);
        return books;
    }

    public List<Book> findAllBooks() {
        List<Book> books = bookDAO.findAllBooks();
        requireExistBookOnList(books);
        return books;
    }

    private void requireBookNotExists(Book book) {
        if (book != null) {
            throw new BookAlreadyExistsException("Este Livro já foi registrado.");
        }
    }

    private void requireExistBook(Book book) {
        if (book == null) {
            throw new BookNotFoundException("Livro não encontrado");
        }
    }

    private void requireExistAuthor(Author author) {
        if (author == null) {
            throw new AuthorNotFoundException("Autor não encontrado");
        }
    }

    private void requireExistBookOnList(List<Book> books) {
        if (books == null || books.isEmpty()) {
            throw new BookNotFoundException("Nenhum livro encontrado");
        }
    }

    private void validateBookData(String title, Long authorId, Integer publicationYear, String description) {
        Validator.requireTitleLength(title, "Titulo do livro");
        Validator.requireNotNull(authorId, "ID do autor");
        Validator.requirePublicationYearValid(publicationYear, "Ano de publicação do livro");
        Validator.requireDescriptionLength(description, "Descrição do livro");
    }
}
