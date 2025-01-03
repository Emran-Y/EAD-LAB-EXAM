package com.todo.OnlineBookstore.servlets.bookCrud;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
 * Servlet handling book search operations.
 * Processes GET requests to search books by title or author.
 */
@WebServlet(name = "SearchBooksServlet", urlPatterns = { "/searchBooks" })
public class SearchBooksServlet extends HttpServlet {

    @Autowired
    private DBConnectionManager databaseManager;

    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String searchQuery = request.getParameter("searchQuery");
        String searchType = request.getParameter("searchType"); // "title" or "author"
        
        response.setContentType("text/html");
        PrintWriter responseWriter = response.getWriter();

        try {
            databaseManager.openConnection();
            Connection dbConnection = databaseManager.getConnection();
            
            // Prepare search query based on search type
            String sqlQuery = "SELECT * FROM Books WHERE " + searchType + " LIKE ?";
            PreparedStatement preparedStatement = dbConnection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, "%" + searchQuery + "%");
            
            ResultSet resultSet = preparedStatement.executeQuery();
            
            // Generate HTML response
            generateSearchResultsHtml(responseWriter, resultSet, searchQuery, searchType);
            
            resultSet.close();
            preparedStatement.close();
            
        } catch (SQLException sqlException) {
            displayErrorMessage(responseWriter, sqlException);
        } finally {
            closeConnection(databaseManager);
        }
    }

    private void generateSearchResultsHtml(PrintWriter writer, ResultSet results, 
                                         String searchQuery, String searchType) throws SQLException {
        writer.println("<html>");
        writer.println("<head>");
        writer.println("<title>Search Results</title>");
        writer.println("<style>");
        writer.println("body { font-family: Arial, sans-serif; margin: 20px; }");
        writer.println("table { border-collapse: collapse; width: 100%; }");
        writer.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
        writer.println("th { background-color: #f2f2f2; }");
        writer.println("</style>");
        writer.println("</head>");
        writer.println("<body>");
        
        writer.println("<h2>Search Results</h2>");
        writer.println("<p>Searching for " + searchType + ": '" + searchQuery + "'</p>");
        
        writer.println("<table>");
        writer.println("<tr><th>ID</th><th>Title</th><th>Author</th><th>Price</th><th>Actions</th></tr>");

        boolean hasResults = false;
        while (results.next()) {
            hasResults = true;
            writer.println("<tr>");
            writer.println("<td>" + results.getInt("id") + "</td>");
            writer.println("<td>" + results.getString("title") + "</td>");
            writer.println("<td>" + results.getString("author") + "</td>");
            writer.println("<td>" + results.getDouble("price") + "</td>");
            writer.println("<td><a href='deleteBook?id=" + results.getInt("id") + "'>Delete</a></td>");
            writer.println("</tr>");
        }

        if (!hasResults) {
            writer.println("<tr><td colspan='5'>No results found</td></tr>");
        }

        writer.println("</table>");
        writer.println("<p><a href=\"index.html\">Return to Home</a></p>");
        writer.println("</body></html>");
    }

    private void displayErrorMessage(PrintWriter writer, SQLException exception) {
        writer.println("<html><body>");
        writer.println("<h3>Error searching books: " + exception.getMessage() + "</h3>");
        writer.println("<p><a href=\"index.html\">Return to Home</a></p>");
        writer.println("</body></html>");
    }

    private void closeConnection(DBConnectionManager dbManager) {
        try {
            dbManager.closeConnection();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
} 