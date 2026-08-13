package br.com.libraryManagement.model.entity;

import br.com.libraryManagement.util.Validator;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "autores")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "author")
    private List<Book> books = new ArrayList<>();

    public Author(String name) {
        validateAuthorData(name);
        this.name = name;
    }

    public Author() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void updateAuthorName(String newName) {
        Validator.requireNameLength(newName, "Novo nome do autor");
        this.name = newName;
    }

    public void addBook(Book book) {
        this.books.add(book);
        book.updateBookAuthor(this);
    }

    private void validateAuthorData(String name) {
        Validator.requireNameLength(name, "Nome do autor");
    }

}
