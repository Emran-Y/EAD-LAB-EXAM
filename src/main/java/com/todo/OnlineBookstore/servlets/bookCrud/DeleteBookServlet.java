package com.todo.OnlineBookstore.servlets.bookCrud;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
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

@WebServlet(name = "DeleteBookServlet", urlPatterns = { "/deleteBook" })
public class DeleteBookServlet extends HttpServlet {

    @Autowired
    private DBConnectionManager dbManager;

    @Override
    public void init() throws ServletException {
        super.init();
        // Enable Spring's dependency injection in this servlet
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        if (idStr == null || idStr.isEmpty()) {
            out.println("<html><body>");
            out.println("<h3>Error: 'id' parameter is missing.</h3>");
            out.println("<p><a href=\"index.html\">Go Back</a></p>");
            out.println("</body></html>");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);

            dbManager.openConnection();
            Connection conn = dbManager.getConnection();

            String sql = "DELETE FROM Books WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);

            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0) {
                out.println("<html><body>");
                out.println("<h2>Book with ID " + id + " was deleted successfully</h2>");
                out.println("<p><a href=\"viewBooks\">View All Books</a></p>");
                out.println("</body></html>");
            } else {
                out.println("<html><body>");
                out.println("<h2>Book with ID " + id + " was not found</h2>");
                out.println("<p><a href=\"viewBooks\">View All Books</a></p>");
                out.println("</body></html>");
            }

            statement.close();

        } catch (NumberFormatException e) {
            out.println("<html><body>");
            out.println("<h3>Error: 'id' parameter must be a valid integer.</h3>");
            out.println("<p><a href=\"index.html\">Go Back</a></p>");
            out.println("</body></html>");
        } catch (SQLException e) {
            throw new ServletException("Error deleting Book", e);
        } finally {
            try {
                dbManager.closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
