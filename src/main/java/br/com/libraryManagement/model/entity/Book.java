package br.com.libraryManagement.model.entity;

import br.com.libraryManagement.model.enums.BookStatus;
import br.com.libraryManagement.model.exceptions.StatusChangeException;
import br.com.libraryManagement.util.Validator;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "livros")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    private Author author;
    private Integer publicationYear;
    private String description;
    private String isbn;

    @Enumerated(EnumType.STRING)
    private BookStatus bookStatus;

    public Book(String title, Author author, Integer publicationYear, String description) {
        validateBookData(title, author, publicationYear, description);
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.description = description;
        this.isbn = UUID.randomUUID().toString().substring(0, 13);
        this.bookStatus = BookStatus.WANT_TO_READ;
    }

    public Book() {
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Author getAuthor() {
        return author;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public String getDescription() {
        return description;
    }

    public String getIsbn() {
        return isbn;
    }

    public BookStatus getBookStatus() {
        return bookStatus;
    }

    public boolean isFinished() {
        return this.bookStatus == BookStatus.FINISHED;
    }

    public boolean isReading() {
        return this.bookStatus == BookStatus.READING;
    }

    public boolean isWantToRead() {
        return this.bookStatus == BookStatus.WANT_TO_READ;
    }

    public void updateBookTitle(String newBookTitle) {
        Validator.requireTitleLength(newBookTitle, "Novo titulo do livro");
        this.title = newBookTitle;
    }

    public void updateBookDescription(String newBookDescription) {
        Validator.requireDescriptionLength(newBookDescription, "Nova descrição do livro");
        this.description = newBookDescription;
    }

    public void updateBookAuthor(Author newAuthor) {
        Validator.requireNotNull(newAuthor, "Novo autor do livro");
        this.author = newAuthor;
    }

    public void updatePublicationYear(Integer newBookYear) {
        Validator.requirePublicationYearValid(newBookYear, "Novo ano do livro");
        this.publicationYear = newBookYear;
    }

    public void setBookStatusFinished() {
        if (isFinished()) {
            throw new StatusChangeException("O livro já está finalizado.");
        }
        this.bookStatus = BookStatus.FINISHED;
    }

    public void setBookStatusWantToRead() {
        if(isWantToRead()) {
            throw new StatusChangeException("O livro já está requerido para leitura.");
        }
        this.bookStatus = BookStatus.WANT_TO_READ;
    }

    public void setBookStatusReading() {
        if (isReading()) {
            throw new StatusChangeException("O livro já está sendo lido.");
        }
        this.bookStatus = BookStatus.READING;
    }

    private void validateBookData(String title, Author author, Integer publicationYear, String description) {
        Validator.requireTitleLength(title, "Titulo do livro");
        Validator.requireNotNull(author, "Autor do livro");
        Validator.requirePublicationYearValid(publicationYear, "Ano de publicação do livro");
        Validator.requireDescriptionLength(description, "Descrição do livro");
    }

}

