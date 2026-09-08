package bac.ac.libis;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class LibraryDataListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {

        event.getServletContext().setAttribute(
                "members",
                LibraryData.members
        );

        event.getServletContext().setAttribute(
                "books",
                LibraryData.books
        );
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        // No action required
    }
}