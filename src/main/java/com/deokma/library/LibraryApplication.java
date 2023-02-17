package com.deokma.library;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.*;

@SpringBootApplication
public class LibraryApplication {
    @Value("${spring.datasource.url}")
    private String databaseUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(DataSource dataSource) {
        return args -> {
            try (Connection connection = dataSource.getConnection()) {
                DatabaseMetaData metaData = connection.getMetaData();
                System.out.println("Connected to " + metaData.getDatabaseProductName() + " " + metaData.getDatabaseProductVersion());
            } catch (Exception e) {
                System.out.println("Failed to connect to the database: " + e.getMessage());
                createDatabase();
            }
        };
    }

    private void createDatabase() {
        try (Connection connection = DriverManager.getConnection(databaseUrl, username, password)) {
            System.out.println("Successfully connected to the database: " + databaseUrl);

            // code to create the database goes here
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database: " + e.getMessage());
        }
    }
}
