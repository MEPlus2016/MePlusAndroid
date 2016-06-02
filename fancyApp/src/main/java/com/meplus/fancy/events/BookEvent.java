package com.meplus.fancy.events;

/**
 * Created by dandanba on 3/3/16.
 */
public class BookEvent {
    public final static String ACTION_BORROW = "borrow";
    public final static String ACTION_RETURN = "return";

    public String getISBN() {
        return isbn;
    }

    public void setISBN(String isbn) {
        this.isbn = isbn;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public BookEvent(String action, String isbn) {
        this.action = action;
        this.isbn = isbn;
    }

    private String isbn;

    private String action;

}
