package bac.ac.libis.util;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DBConnection {
    private static final DataSource DATA_SOURCE;

    static {
        try {
            DATA_SOURCE = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/libis");
        } catch (NamingException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DATA_SOURCE.getConnection();
    }
}