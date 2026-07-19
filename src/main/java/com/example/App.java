package com.example;

import java.sql.Connection;
import com.example.config.DatabaseConfig;
import com.example.exceptions.UnauthorizedException;
import com.example.utils.ActivityStatusScheduler;
import com.example.utils.AuthenticatedUser;
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

            Javalin app = Javalin.create(config -> {
                config.bundledPlugins.enableCors(cors -> {
                    cors.addRule(it -> {
                        it.allowHost("http://localhost:5173");
                    });
                });
            });

            new GlobalExceptionHandler().register(app);

            // Filtro de autenticación JWT
            // Rutas públicas: quedan explícitamente excluidas de la validación
            app.before(ctx -> {

                // Permitir preflight de CORS
                if (ctx.method().name().equals("OPTIONS")) {
                    return;
                }

                String method = ctx.method().name();
                String path = ctx.path();

                boolean isPublicRoute =
                        (method.equals("POST") && path.equals("/auth/register")) ||
                        (method.equals("POST") && path.equals("/auth/access"));

                if (isPublicRoute) {
                    return;
                }

                String header = ctx.header("Authorization");

                if (header == null || !header.startsWith("Bearer ")) {
                    throw new UnauthorizedException("Token no proporcionado.");
                }

                String token = header.substring(7);

                AuthenticatedUser authenticatedUser = container.jwtService.validateToken(token);

                ctx.attribute("userId", authenticatedUser.getUserId());
                ctx.attribute("role", authenticatedUser.getRole());
            });

            container.authRoutes.register(app);
            container.groupRoutes.register(app);
            container.studentRoutes.register(app);
            container.unitRoutes.register(app);
            container.topicRoutes.register(app);
            container.resourceRoutes.register(app);
            container.activityRoutes.register(app);
            container.triviaRoutes.register(app);
            container.scrambleRoutes.register(app);
            container.activityStudentRoutes.register(app);
            container.studentGameRoutes.register(app);
            container.profileRoutes.register(app);
            container.forumRoutes.register(app);

            app.start(7000);

            new ActivityStatusScheduler(container.activityService).start();

        } catch (Exception e) {

            if (e instanceof java.sql.SQLException) {
                System.out.println("Error de base de datos \n");
            }

            e.printStackTrace();
            System.exit(1);

        }

    }
}