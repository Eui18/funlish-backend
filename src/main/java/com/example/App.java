package com.example;

import java.sql.Connection;
import com.example.config.DatabaseConfig;
import com.example.utils.GlobalExceptionHandler;
import io.github.cdimascio.dotenv.Dotenv;
import io.javalin.Javalin;

public class App {
    public static void main(String[] args) {

        try {

            Dotenv dotenv = Dotenv.load();
            DatabaseConfig databaseConfig = new DatabaseConfig(dotenv);
            Connection connection = databaseConfig.getDataSource().getConnection();
            DependencyContainer container = new DependencyContainer(connection);

            Javalin app = Javalin.create();

            new GlobalExceptionHandler().register(app);
            container.authRoutes.register(app);
            container.groupRoutes.register(app);
            container.studentRoutes.register(app);

            app.start(7000);

        } catch (Exception e) {

            if (e instanceof java.sql.SQLException) {
                System.out.println("Error de base de datos \n");
            }

            e.printStackTrace();
            System.exit(1);

        }

    }
}
