package com.example.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;

import javax.sql.DataSource;

public class DatabaseConfig {

    private final String host;
    private final String port;
    private final String name;
    private final String user;
    private final String password;

    private final HikariDataSource dataSource;

    public DatabaseConfig(Dotenv dotenv) {

        this.host = dotenv.get("DB_HOST");
        this.port = dotenv.get("DB_PORT");
        this.name = dotenv.get("DB_NAME");
        this.user = dotenv.get("DB_USER");
        this.password = dotenv.get("DB_PASSWORD");

        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(getUrl());
        config.setUsername(user);
        config.setPassword(password);

        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);

        this.dataSource = new HikariDataSource(config);
    }

    public String getUrl() {
        return "jdbc:mysql://" + host + ":" + port + "/" + name;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

}