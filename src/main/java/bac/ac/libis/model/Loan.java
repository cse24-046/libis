package bac.ac.libis.model;

import java.sql.Timestamp;

public class Loan {
    private int loanId;
    private int memberId;
    private int bookId;
    private Timestamp borrowedOn;
    private Timestamp returnedOn;   // it will be null when the book is still out
    private String bookTitle;       // joined from books

    public Loan() {
    }

    public int getLoanId() { return loanId; }
    public void setLoanId(int loanId) { this.loanId = loanId; }

    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }

    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    public Timestamp getBorrowedOn() { return borrowedOn; }
    public void setBorrowedOn(Timestamp borrowedOn) { this.borrowedOn = borrowedOn; }

    public Timestamp getReturnedOn() { return returnedOn; }
    public void setReturnedOn(Timestamp returnedOn) { this.returnedOn = returnedOn; }

    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }

    public boolean isReturned() { return returnedOn != null; }
}