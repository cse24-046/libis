package bac.ac.libis.service;

import bac.ac.libis.dao.BookDao;
import bac.ac.libis.dao.LoanDao;
import bac.ac.libis.exception.LoanException;
import bac.ac.libis.model.Book;

import java.sql.SQLException;

public class LoanService {
    public static final int MAX_LOANS = 3;

    private final LoanDao loanDao = new LoanDao();
    private final BookDao bookDao = new BookDao();

    public void borrow(int memberId, int bookId) throws LoanException, SQLException {
        Book book = bookDao.findById(bookId);
        if (book == null) {
            throw new LoanException("That book does not exist.");
        }
        if (loanDao.hasOpenLoan(bookId)) {
            throw new LoanException(book.getTitle() + " cannot be borrowed because it is already on loan.");
        }
        int openLoans = loanDao.countOpenByMember(memberId);
        if (openLoans >= MAX_LOANS) {
            throw new LoanException("You already have 3 books on loan, the maximum allowed. Return a book before borrowing another.");
        }
        loanDao.create(memberId, bookId);
    }

    public void returnLoan(int memberId, int loanId) throws LoanException, SQLException {
        if (!loanDao.isOpenLoanForMember(loanId, memberId)) {
            throw new LoanException("That book is not currently on loan to you.");
        }
        if (!loanDao.markReturned(loanId, memberId)) {
            throw new LoanException("The loan could not be returned.");
        }
    }
}
