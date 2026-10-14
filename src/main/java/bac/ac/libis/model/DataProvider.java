package bac.ac.libis.model;

import java.util.ArrayList;
import java.util.List;

public class DataProvider {
    private static List<Member> members = new ArrayList<>();
    private static List<Book> books = new ArrayList<>();

    static {
        members.add(new Member(1, "Seneo", "Molatlhwa", "seneo@gmail.com", "pass123"));
        members.add(new Member(2, "Bob", "Johnson", "bob@gmail.com", "pass456"));
        members.add(new Member(3, "Fally", "Ipupa", "fipupa@bac.com", "pass789"));
        members.add(new Member(4, "Raymond", "Reddington", "rayred@bac.com", "passabc"));
        members.add(new Member(5, "Monyana", "Kedukaetswe", "biancak@bac.com", "passdef"));

        books.add(new Book(1, "Java Programming", "P. Sebalatlheng", "Technology", "An introductory guide to Java.", true));
        books.add(new Book(2, "Clean Code", "R. Martin", "Technology", "A handbook of agile software craftsmanship.", true));
        books.add(new Book(3, "The Hobbit", "J.R.R. Tolkien", "Fantasy", "A fantasy novel about a journey.", false));
        books.add(new Book(4, "1984", "George Orwell", "Dystopian", "A dystopian social science fiction novel.", true));
        books.add(new Book(5, "To Kill a Mockingbird", "Harper Lee", "Classic", "A novel about racial injustice.", true));
        books.add(new Book(6, "Pride and Prejudice", "Jane Austen", "Romance", "A romantic novel of manners.", false));
        books.add(new Book(7, "The Alchemist", "Paulo Coelho", "Adventure", "A quest to find worldly treasure.", true));
        books.add(new Book(8, "The Matrix Concepts", "L. Wachowski", "Sci-Fi", "Understanding digital reality.", true));
        books.add(new Book(9, "Database Systems", "C. Date", "Education", "An introduction to database design.", false));
        books.add(new Book(10, "Web Engineering", "T. Berners-Lee", "Technology", "Building modern web apps.", true));
    }

    public static List<Member> getMembers() {
        return members;
    }

    public static List<Book> getBooks() {
        return books;
    }
}