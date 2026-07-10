package com.example;

import java.sql.Connection;

import com.example.services.ResourceService;
import com.example.controllers.ActivityController;
import com.example.controllers.AuthController;
import com.example.controllers.GroupController;
import com.example.controllers.ResourceController;
import com.example.controllers.StudentController;
import com.example.controllers.TopicController;
import com.example.controllers.UnitController;
import com.example.repository.activity.ActivityRepositoryImpl;
import com.example.repository.auth.AuthRepositoryImpl;
import com.example.repository.group.GroupRepositoryImpl;
import com.example.repository.resource.ResourceRepositoryImpl;
import com.example.repository.student.StudentRepositoryImpl;
import com.example.repository.topic.TopicRepositoryImpl;
import com.example.repository.unit.UnitRepositoryImpl;
import com.example.routes.ActivityRoutes;
import com.example.routes.AuthRoutes;
import com.example.routes.GroupRoutes;
import com.example.routes.ResourceRoutes;
import com.example.routes.StudentRoutes;
import com.example.routes.TopicRoutes;
import com.example.routes.UnitRoutes;
import com.example.services.ActivityService;
import com.example.services.AuthService;
import com.example.services.GroupService;
import com.example.services.StudentService;
import com.example.services.TopicService;
import com.example.services.UnitService;

public class DependencyContainer {

    public final AuthRoutes authRoutes;
    public final GroupRoutes groupRoutes;
    public final StudentRoutes studentRoutes;
    public final UnitRoutes unitRoutes;
    public final TopicRoutes topicRoutes;
    public final ResourceRoutes resourceRoutes;
    public final ActivityRoutes activityRoutes;

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

        //unit
        var unitRepository = new UnitRepositoryImpl(connection);
        var unitService = new UnitService(unitRepository, groupRepository);
        var unitController = new UnitController (unitService);
        this.unitRoutes = new UnitRoutes(unitController);

        // Topic
        var topicRepository = new TopicRepositoryImpl(connection);
        var topicService = new TopicService(topicRepository, unitRepository);
        var topicController = new TopicController(topicService);
        this.topicRoutes = new TopicRoutes(topicController);

        // Resource
        var resourceRepository = new ResourceRepositoryImpl(connection);
        var resourceService = new ResourceService(resourceRepository, topicRepository);
        var resourceController = new ResourceController(resourceService);
        this.resourceRoutes = new ResourceRoutes(resourceController);

        //activity
        var activityRepository = new ActivityRepositoryImpl(connection);
        var activityService = new ActivityService(activityRepository, topicRepository);
        var activityController = new ActivityController(activityService);
        this.activityRoutes = new ActivityRoutes(activityController);

    }

}