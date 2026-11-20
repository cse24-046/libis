package bac.ac.libis.service;

import bac.ac.libis.dao.BookDao;
import bac.ac.libis.dao.LoanDao;
import bac.ac.libis.model.Book;

import java.sql.SQLException;

public class BookService {
    private final BookDao bookDao = new BookDao();
    private final LoanDao loanDao = new LoanDao();

    public Book create(Book book) throws ValidationException, SQLException {
        validate(book);
        bookDao.create(book);
        return book;
    }

    public Book update(Book book) throws ValidationException, SQLException {
        validate(book);
        bookDao.update(book);
        return book;
    }

    public void delete(Book book) throws ValidationException, SQLException {
        if (loanDao.hasOpenLoan(book.getBookId())) {
            throw new ValidationException(java.util.Map.of("delete", book.getTitle() + " cannot be deleted because it is currently on loan."));
        }
        bookDao.delete(book.getBookId());
    }

    private void validate(Book book) throws ValidationException {
        ValidationException errors = new ValidationException();
        if (book.getTitle() == null || book.getTitle().trim().isEmpty())
            errors.addError("title", "Title is required.");
        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty())
            errors.addError("author", "Author is required.");
        if (errors.hasErrors()) throw errors;
    }
}
