package bac.ac.libis;

import java.util.ArrayList;
import java.util.List;

public class LibraryData {
    // Two ArrayLists storing Member and Book objects
    public static List<Member> members = new ArrayList<>();
    public static List<Book> books = new ArrayList<>();

    static {
        // 1. Seed 5 Member objects
        members.add(new Member(1, "Admin User", "admin@bac.ac.bw", "admin123", "ADMIN"));
        members.add(new Member(2, "Jane Doe", "jane@bac.ac.bw", "password123", "STUDENT"));
        members.add(new Member(3, "John Smith", "john@bac.ac.bw", "pass123", "STUDENT"));
        members.add(new Member(4, "Alice Johnson", "alice@bac.ac.bw", "alice2024", "LECTURER"));
        members.add(new Member(5, "Bob Williams", "bob@bac.ac.bw", "bobsecure", "STUDENT"));

        // 2. Seed 10 Book objects
        books.add(new Book(101, "To Kill a Mockingbird", "Harper Lee", true));
        books.add(new Book(102, "1984", "George Orwell", true));
        books.add(new Book(103, "The Great Gatsby", "F. Scott Fitzgerald", false));
        books.add(new Book(104, "Pride and Prejudice", "Jane Austen", true));
        books.add(new Book(105, "The Catcher in the Rye", "J.D. Salinger", false));
        books.add(new Book(106, "Clean Code", "Robert C. Martin", true));
        books.add(new Book(107, "The Hobbit", "J.R.R. Tolkien", true));
        books.add(new Book(108, "Brave New World", "Aldous Huxley", false));
        books.add(new Book(109, "Design Patterns", "Erich Gamma et al.", true));
        books.add(new Book(110, "Fahrenheit 451", "Ray Bradbury", true));
    }
}