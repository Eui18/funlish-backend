package com.example;

import java.sql.Connection;
import com.example.controllers.AuthController;
import com.example.controllers.GroupController;
import com.example.controllers.StudentController;
import com.example.repository.auth.AuthRepositoryImpl;
import com.example.repository.group.GroupRepositoryImpl;
import com.example.repository.student.StudentRepositoryImpl;
import com.example.routes.AuthRoutes;
import com.example.routes.GroupRoutes;
import com.example.routes.StudentRoutes;
import com.example.services.AuthService;
import com.example.services.GroupService;
import com.example.services.StudentService;

public class DependencyContainer {

    public final AuthRoutes authRoutes;
    public final GroupRoutes groupRoutes;
    public final StudentRoutes studentRoutes;

    public DependencyContainer(Connection connection) {

        // Módulo Auth
        var authRepository = new AuthRepositoryImpl(connection);
        var authService = new AuthService(authRepository);
        var authController = new AuthController(authService);
        this.authRoutes = new AuthRoutes(authController);

        //Modulo grupo
        var groupRepository = new GroupRepositoryImpl(connection);
        var groupService = new GroupService(groupRepository, authRepository);
        var groupController = new GroupController(groupService);
        this.groupRoutes = new GroupRoutes(groupController);
    
        //student
        var studentRepository = new StudentRepositoryImpl(connection);
        var studentService = new StudentService(studentRepository, groupRepository);
        var studentController = new StudentController(studentService);
        this.studentRoutes = new StudentRoutes(studentController);

    }

}