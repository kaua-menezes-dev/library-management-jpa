package br.com.libraryManagement.dao;

import br.com.libraryManagement.model.entity.Book;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.List;

public class BookDAO {

    private EntityManager entityManager;

    public BookDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void registerBook(Book book) {
        this.entityManager.persist(book);
    }

    public void removeBook(Book book) {
        this.entityManager.remove(book);
    }

    public Book findBookByTitle(String bookTitle) {
        String jpql = "SELECT b FROM Book b WHERE b.title = ?1";

        try {
            return entityManager.createQuery(jpql, Book.class)
                    .setParameter(1, bookTitle)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }

    public Book findByBookId(Long id) {
        return entityManager.find(Book.class, id);
    }

    public List<Book> findAllBooks() {
        String jqpl = "SELECT b FROM Book b";
        return entityManager.createQuery(jqpl, Book.class)
                .getResultList();
    }

    public List<Book> findBookByAuthor(String authorName) {
        String jpql = "SELECT b FROM Book b WHERE b.author.name = ?1";
        return entityManager.createQuery(jpql, Book.class)
                .setParameter(1, authorName)
                .getResultList();
    }
}
