package com.todo.OnlineBookstore.servlets;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class DispatcherServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        dispatch(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        dispatch(request, response);
    }

    private void dispatch(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String uri = request.getRequestURI();
        if (uri.contains("/createBook")) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/createBook");
            dispatcher.forward(request, response);
        } else if (uri.contains("/displayBooks")) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/displayBooks");
            dispatcher.forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}