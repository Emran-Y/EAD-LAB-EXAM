package com.todo.OnlineBookstore.servlets.bookCrud;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.todo.OnlineBookstore.db.DBConnectionManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * Servlet responsible for displaying all books in the system.
 * Handles GET requests to retrieve and display book information.
 */
@WebServlet(name = "DisplayBooksServlet", urlPatterns = { "/viewBooks" })
public class DisplayBooksServlet extends HttpServlet {

    @Autowired
    private DBConnectionManager databaseManager;

    @Override
    public void init() throws ServletException { // this method is called so that Spring can inject the
                                                 // DBConnectionManager bean
        super.init();
        // Enable Spring's dependency injection in this servlet
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter responseWriter = response.getWriter();

        try {
            databaseManager.openConnection();
            Connection dbConnection = databaseManager.getConnection();

            // Prepare and execute select query
            String selectQuery = "SELECT id, title, author, price FROM Books";
            Statement sqlStatement = dbConnection.createStatement();
            ResultSet resultSet = sqlStatement.executeQuery(selectQuery);

            // Generate HTML response
            generateBookListHtml(responseWriter, resultSet);

            // Cleanup resources
            resultSet.close();
            sqlStatement.close();

        } catch (SQLException sqlException) {
            displayErrorMessage(responseWriter, sqlException);
        } finally {
            closeConnection(databaseManager);
        }
    }

    // Helper method to generate HTML for book list
    private void generateBookListHtml(PrintWriter writer, ResultSet results) throws SQLException {
        writer.println("<html><body>");
        writer.println("<h2>Available Books</h2>");
        writer.println("<table border='1'>");
        writer.println("<tr><th>ID</th><th>Title</th><th>Author</th><th>Price</th><th>Actions</th></tr>");

        while (results.next()) {
            writer.println("<tr>");
            writer.println("<td>" + results.getInt("id") + "</td>");
            writer.println("<td>" + results.getString("title") + "</td>");
            writer.println("<td>" + results.getString("author") + "</td>");
            writer.println("<td>" + results.getDouble("price") + "</td>");
            writer.println("<td><a href='deleteBook?id=" + results.getInt("id") + "'>Delete</a></td>");
            writer.println("</tr>");
        }

        writer.println("</table>");
        writer.println("<p><a href=\"index.html\">Return to Home</a></p>");
        writer.println("</body></html>");
    }

    // Helper method to display error message
    private void displayErrorMessage(PrintWriter writer, SQLException exception) {
        writer.println("<p>Error retrieving books: " + exception.getMessage() + "</p>");
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
