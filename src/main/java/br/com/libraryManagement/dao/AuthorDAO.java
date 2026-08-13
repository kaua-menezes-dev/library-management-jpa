package br.com.libraryManagement.dao;

import br.com.libraryManagement.model.entity.Author;
import jakarta.persistence.EntityManager;

import java.util.List;

public class AuthorDAO {

    private EntityManager entityManager;

    public AuthorDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void registerAuthor(Author author) {
        entityManager.persist(author);
    }

    public void removeAuthor(Author author) {
        this.entityManager.remove(author);
    }

    public Author findByAuthorName(String authorName) {
        String jpql = "SELECT a FROM Author a WHERE a.name = ?1";
        return entityManager.createQuery(jpql, Author.class)
                .setParameter(1, authorName)
                .getSingleResult();
    }

    public Author findByAuthorId(Long id) {
        return entityManager.find(Author.class, id);
    }

    public List<Author> findAllAuthors() {
        String jpql = "SELECT a FROM Author a";
        return entityManager.createQuery(jpql, Author.class)
                .getResultList();
    }

    public List<Author> findAuthorByBookTitle(String bookTitle) {
        String jpql = "SELECT b.author FROM Book b WHERE b.title = ?1";
        return entityManager.createQuery(jpql, Author.class)
                .setParameter(1, bookTitle)
                .getResultList();
    }
}
