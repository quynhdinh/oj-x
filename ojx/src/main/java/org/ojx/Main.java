package org.ojx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import org.ojx.connection.ConnectionManager;
import org.ojx.gui.login.LoginScreen;

public class Main {

    private static final Logger log = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws IOException, SQLException {
        initDatabase();
        log.info("Starting oj-x application...");
        // UserRepository userRepository = new UserRepository();
        // Optional<User> byId = userRepository.getByUserName("admin");
        // if (byId.isPresent()) {
        //     log.info("User found: " + byId.get());
        // } else {
        //     log.info("User not found.");
        // }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginScreen().setVisible(true);
            }
        });
    }

    private static void initDatabase() throws IOException {
        System.out.println("Initializing database...");

        ClassLoader classLoader = Main.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("data.sql");

        if (inputStream == null) {
            log.info("Database initialization script not found.");
            return;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sqlScript = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            sqlScript.append(line).append("\n");
        }
        try (Connection conn = ConnectionManager.getConnection()) {
            String[] sqlStatements = sqlScript.toString().split(";");
            System.out.println(sqlStatements.length + " SQL statements found.");
            try (Statement stmt = conn.createStatement()) {
                for (String sql : sqlStatements) {
                    sql = sql.trim();
                    if (!sql.isEmpty()) {
                        log.info("Executing: " + sql);
                        stmt.execute(sql);
                    }
                }
            }
            log.info("Database initialized successfully.");
        } catch (Exception e) {
            log.severe("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}