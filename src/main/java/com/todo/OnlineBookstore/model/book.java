package com.todo.OnlineBookstore.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a Book entity in the bookstore system.
 * Contains essential book information for inventory management.
 */
@NoArgsConstructor
@Getter
@Setter
public class book {
    private int bookId;           // Unique identifier for the book
    private String bookTitle;     // Title of the book
    private String authorName;    // Name of the book's author
    private double bookPrice;     // Price of the book in default currency
}
