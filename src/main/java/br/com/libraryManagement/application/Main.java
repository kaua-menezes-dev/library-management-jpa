package br.com.libraryManagement.application;

import br.com.libraryManagement.model.entity.Author;
import br.com.libraryManagement.model.entity.Book;
import br.com.libraryManagement.service.AuthorService;
import br.com.libraryManagement.service.BookService;
import br.com.libraryManagement.util.JPAUtil;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        EntityManager entityManager = JPAUtil.getEntityManager();

        BookService bookService = new BookService(entityManager);
        AuthorService authorService = new AuthorService(entityManager);

        int option = -1;

        try {

            while (option != 0) {

                System.out.println("""
                        
                        ===== LIBRARY MANAGEMENT =====
                        
                        1 - Livros
                        2 - Autores
                        0 - Sair
                        
                        Escolha uma opção:
                        """);

                option = scanner.nextInt();
                scanner.nextLine();

                try {

                    switch (option) {

                        case 1 ->
                                bookMenu(scanner, bookService);

                        case 2 ->
                                authorMenu(scanner, authorService);

                        case 0 ->
                                System.out.println("Encerrando aplicação...");

                        default ->
                                System.out.println("Opção inválida.");
                    }

                } catch (Exception e) {
                    System.out.println("\nErro: " + e.getMessage());
                }
            }

        } finally {

            scanner.close();

            if (entityManager.isOpen()) {
                entityManager.close();
            }

            JPAUtil.getEntityManager().close();
        }
    }

    private static void bookMenu(
            Scanner scanner,
            BookService bookService
    ) {

        int option = -1;

        while (option != 0) {

            System.out.println("""
                    
                    ===== LIVROS =====
                    
                    1 - Registrar livro
                    2 - Listar todos os livros
                    3 - Buscar livro por ID
                    4 - Buscar livro por título
                    5 - Buscar livros por autor
                    6 - Atualizar título
                    7 - Atualizar descrição
                    8 - Atualizar ano de publicação
                    9 - Atualizar autor
                    10 - Marcar como "Lendo"
                    11 - Marcar como "Quero ler"
                    12 - Marcar como "Finalizado"
                    13 - Remover livro
                    0 - Voltar
                    
                    Escolha uma opção:
                    """);

            option = scanner.nextInt();
            scanner.nextLine();

            try {

                switch (option) {

                    case 1 ->
                            registerBook(scanner, bookService);

                    case 2 ->
                            listAllBooks(bookService);

                    case 3 ->
                            findBookById(scanner, bookService);

                    case 4 ->
                            findBookByTitle(scanner, bookService);

                    case 5 ->
                            findBooksByAuthor(scanner, bookService);

                    case 6 ->
                            updateBookTitle(scanner, bookService);

                    case 7 ->
                            updateBookDescription(scanner, bookService);

                    case 8 ->
                            updateBookPublicationYear(scanner, bookService);

                    case 9 ->
                            updateBookAuthor(scanner, bookService);

                    case 10 ->
                            setBookStatusReading(scanner, bookService);

                    case 11 ->
                            setBookStatusWantToRead(scanner, bookService);

                    case 12 ->
                            setBookStatusFinished(scanner, bookService);

                    case 13 ->
                            removeBook(scanner, bookService);

                    case 0 ->
                            System.out.println("Voltando ao menu principal...");

                    default ->
                            System.out.println("Opção inválida.");
                }

            } catch (Exception e) {
                System.out.println("\nErro: " + e.getMessage());
            }
        }
    }

    private static void authorMenu(
            Scanner scanner,
            AuthorService authorService
    ) {

        int option = -1;

        while (option != 0) {

            System.out.println("""
                    
                    ===== AUTORES =====
                    
                    1 - Registrar autor
                    2 - Listar todos os autores
                    3 - Buscar autor por ID
                    4 - Buscar autor por nome
                    5 - Buscar autor pelo título de um livro
                    6 - Atualizar nome do autor
                    7 - Remover autor
                    0 - Voltar
                    
                    Escolha uma opção:
                    """);

            option = scanner.nextInt();
            scanner.nextLine();

            try {

                switch (option) {

                    case 1 ->
                            registerAuthor(scanner, authorService);

                    case 2 ->
                            listAllAuthors(authorService);

                    case 3 ->
                            findAuthorById(scanner, authorService);

                    case 4 ->
                            findAuthorByName(scanner, authorService);

                    case 5 ->
                            findAuthorByBookTitle(scanner, authorService);

                    case 6 ->
                            updateAuthorName(scanner, authorService);

                    case 7 ->
                            removeAuthor(scanner, authorService);

                    case 0 ->
                            System.out.println("Voltando ao menu principal...");

                    default ->
                            System.out.println("Opção inválida.");
                }

            } catch (Exception e) {
                System.out.println("\nErro: " + e.getMessage());
            }
        }
    }

//    Book

    private static void registerBook(
            Scanner scanner,
            BookService bookService
    ) {

        System.out.print("Título: ");
        String title = scanner.nextLine();

        System.out.print("ID do autor: ");
        Long authorId = scanner.nextLong();
        scanner.nextLine();

        System.out.print("Ano de publicação: ");
        Integer publicationYear = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Descrição: ");
        String description = scanner.nextLine();

        bookService.registerBook(
                title,
                authorId,
                publicationYear,
                description
        );

        System.out.println("Livro registrado com sucesso.");
    }

    private static void listAllBooks(BookService bookService) {

        List<Book> books = bookService.findAllBooks();

        System.out.println("\n===== LIVROS CADASTRADOS =====");

        for (Book book : books) {
            printBook(book);
        }
    }

    private static void findBookById(
            Scanner scanner,
            BookService bookService
    ) {

        System.out.print("ID do livro: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        Book book = bookService.findBookById(id);

        printBook(book);
    }

    private static void findBookByTitle(
            Scanner scanner,
            BookService bookService
    ) {

        System.out.print("Título do livro: ");
        String title = scanner.nextLine();

        Book book = bookService.findBookByTitle(title);

        printBook(book);
    }

    private static void findBooksByAuthor(
            Scanner scanner,
            BookService bookService
    ) {

        System.out.print("Nome do autor: ");
        String authorName = scanner.nextLine();

        List<Book> books =
                bookService.findBookByAuthorName(authorName);

        for (Book book : books) {
            printBook(book);
        }
    }

    private static void updateBookTitle(
            Scanner scanner,
            BookService bookService
    ) {

        System.out.print("ID do livro: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        System.out.print("Novo título: ");
        String title = scanner.nextLine();

        bookService.updateBookTitle(id, title);

        System.out.println("Título atualizado com sucesso.");
    }

    private static void updateBookDescription(
            Scanner scanner,
            BookService bookService
    ) {

        System.out.print("ID do livro: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        System.out.print("Nova descrição: ");
        String description = scanner.nextLine();

        bookService.updateBookDescription(id, description);

        System.out.println("Descrição atualizada com sucesso.");
    }

    private static void updateBookPublicationYear(
            Scanner scanner,
            BookService bookService
    ) {

        System.out.print("ID do livro: ");
        Long id = scanner.nextLong();

        System.out.print("Novo ano de publicação: ");
        Integer year = scanner.nextInt();

        scanner.nextLine();

        bookService.updateBookPublicationYear(id, year);

        System.out.println("Ano atualizado com sucesso.");
    }

    private static void updateBookAuthor(
            Scanner scanner,
            BookService bookService
    ) {

        System.out.print("ID do livro: ");
        Long bookId = scanner.nextLong();

        System.out.print("ID do novo autor: ");
        Long authorId = scanner.nextLong();

        scanner.nextLine();

        bookService.updateBookAuthor(bookId, authorId);

        System.out.println("Autor atualizado com sucesso.");
    }

    private static void setBookStatusReading(
            Scanner scanner,
            BookService bookService
    ) {

        Long id = readBookId(scanner);

        bookService.setBookStatusReading(id);

        System.out.println("Status atualizado para LENDO.");
    }

    private static void setBookStatusWantToRead(
            Scanner scanner,
            BookService bookService
    ) {

        Long id = readBookId(scanner);

        bookService.setBookStatusWantToRead(id);

        System.out.println("Status atualizado para QUERO LER.");
    }

    private static void setBookStatusFinished(
            Scanner scanner,
            BookService bookService
    ) {

        Long id = readBookId(scanner);

        bookService.setBookStatusFinished(id);

        System.out.println("Status atualizado para FINALIZADO.");
    }

    private static void removeBook(
            Scanner scanner,
            BookService bookService
    ) {

        Long id = readBookId(scanner);

        bookService.removeBookById(id);

        System.out.println("Livro removido com sucesso.");
    }

//    Author

    private static void registerAuthor(
            Scanner scanner,
            AuthorService authorService
    ) {

        System.out.print("Nome do autor: ");
        String name = scanner.nextLine();

        authorService.registerAuthor(name);

        System.out.println("Autor registrado com sucesso.");
    }

    private static void listAllAuthors(
            AuthorService authorService
    ) {

        List<Author> authors =
                authorService.findAllAuthors();

        System.out.println("\n===== AUTORES CADASTRADOS =====");

        for (Author author : authors) {
            printAuthor(author);
        }
    }

    private static void findAuthorById(
            Scanner scanner,
            AuthorService authorService
    ) {

        System.out.print("ID do autor: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        Author author =
                authorService.findByAuthorId(id);

        printAuthor(author);
    }

    private static void findAuthorByName(
            Scanner scanner,
            AuthorService authorService
    ) {

        System.out.print("Nome do autor: ");
        String name = scanner.nextLine();

        Author author =
                authorService.findByAuthorName(name);

        printAuthor(author);
    }

    private static void findAuthorByBookTitle(
            Scanner scanner,
            AuthorService authorService
    ) {

        System.out.print("Título do livro: ");
        String title = scanner.nextLine();

        Author author =
                authorService.findAuthorByBookTitle(title);

        printAuthor(author);
    }

    private static void updateAuthorName(
            Scanner scanner,
            AuthorService authorService
    ) {

        System.out.print("ID do autor: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        System.out.print("Novo nome: ");
        String newName = scanner.nextLine();

        authorService.updateAuthorName(id, newName);

        System.out.println("Nome atualizado com sucesso.");
    }

    private static void removeAuthor(
            Scanner scanner,
            AuthorService authorService
    ) {

        System.out.print("ID do autor: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        authorService.removeAuthorById(id);

        System.out.println("Autor removido com sucesso.");
    }

    private static Long readBookId(Scanner scanner) {

        System.out.print("ID do livro: ");

        Long id = scanner.nextLong();
        scanner.nextLine();

        return id;
    }

    private static void printBook(Book book) {

        System.out.println("""
                
                -------------------------
                ID: %d
                Título: %s
                Autor: %s
                -------------------------
                """.formatted(
                book.getId(),
                book.getTitle(),
                book.getAuthor().getName()
        ));
    }

    private static void printAuthor(Author author) {

        System.out.println("""
                
                -------------------------
                ID: %d
                Nome: %s
                -------------------------
                """.formatted(
                author.getId(),
                author.getName()
        ));
    }
}