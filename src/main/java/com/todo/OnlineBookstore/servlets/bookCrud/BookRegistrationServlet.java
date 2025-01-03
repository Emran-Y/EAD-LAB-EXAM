package com.todo.OnlineBookstore.servlets.bookCrud;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.todo.OnlineBookstore.db.DBConnectionManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * Servlet handling book registration operations.
 * Processes POST requests to create new book entries in the database.
 */
@WebServlet(name = "BookRegistrationServlet", urlPatterns = { "/createBook" })
public class BookRegistrationServlet extends HttpServlet {

    @Autowired
    private DBConnectionManager databaseManager;    // Database connection manager

    @Override
    public void init() throws ServletException {
        super.init();
        // Enable Spring's dependency injection in this servlet
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Extract book details from request parameters
        String bookTitle = request.getParameter("title");
        String authorName = request.getParameter("author");
        String priceString = request.getParameter("price");
        
        // Parse and validate price
        Double bookPrice = null;
        if (priceString != null && !priceString.isEmpty()) {
            try {
                bookPrice = Double.parseDouble(priceString);
            } catch (IllegalArgumentException exception) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid price format provided");
                return;
            }
        }

        // Set response content type
        response.setContentType("text/html");
        PrintWriter responseWriter = response.getWriter();

        try {
            // Database operations
            databaseManager.openConnection();
            Connection dbConnection = databaseManager.getConnection();
            
            // Prepare and execute insert query
            String insertQuery = "INSERT INTO Books (title, author, price) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = dbConnection.prepareStatement(insertQuery);
            preparedStatement.setString(1, bookTitle);
            preparedStatement.setString(2, authorName);
            preparedStatement.setDouble(3, bookPrice);

            // Process insert operation
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                displaySuccessMessage(responseWriter);
            }

        } catch (SQLException sqlException) {
            displayErrorMessage(responseWriter, sqlException);
        } finally {
            closeConnection(databaseManager);
        }
    }

    // Helper method to display success message
    private void displaySuccessMessage(PrintWriter writer) {
        writer.println("<html><body>");
        writer.println("<h3>Book created successfully!</h3>");
        writer.println("<p><a href=\"index.html\">Return to Home</a></p>");
        writer.println("</body></html>");
    }

    // Helper method to display error message
    private void displayErrorMessage(PrintWriter writer, SQLException exception) {
        exception.printStackTrace();
        writer.println("<html><body>");
        writer.println("<h3>Error creating Book: " + exception.getMessage() + "</h3>");
        writer.println("<p><a href=\"index.html\">Return to Home</a></p>");
        writer.println("</body></html>");
    }

    // Helper method to close database connection
    private void closeConnection(DBConnectionManager dbManager) {
        try {
            dbManager.closeConnection();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
