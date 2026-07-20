package com.example;

import java.sql.Connection;

import com.example.services.ResourceService;
import com.example.services.ScrambleService;
import com.example.services.StudentGameService;
import com.example.controllers.ActivityController;
import com.example.controllers.ActivityStudentController;
import com.example.controllers.AuthController;
import com.example.controllers.ForumController;
import com.example.controllers.GroupController;
import com.example.controllers.ProfileController;
import com.example.controllers.ResourceController;
import com.example.controllers.ScrambleController;
import com.example.controllers.StudentController;
import com.example.controllers.StudentGameController;
import com.example.controllers.TopicController;
import com.example.controllers.TriviaController;
import com.example.controllers.UnitController;
import com.example.repository.activity.ActivityRepositoryImpl;
import com.example.repository.activityStudent.ActivityStudentRepositoryImpl;
import com.example.repository.activityStudent.StudentGameRepositoryImpl;
import com.example.repository.auth.AuthRepositoryImpl;
import com.example.repository.forum.CommentRepositoryImpl;
import com.example.repository.forum.ForumRepositoryImpl;
import com.example.repository.group.GroupRepositoryImpl;
import com.example.repository.profile.ProfileRepositoryImpl;
import com.example.repository.resource.ResourceRepositoryImpl;
import com.example.repository.scramble.ScrambleRepositoryImpl;
import com.example.repository.student.StudentRepositoryImpl;
import com.example.repository.topic.TopicRepositoryImpl;
import com.example.repository.trivia.TriviaRepositoryImpl;
import com.example.repository.unit.UnitRepositoryImpl;
import com.example.routes.ActivityRoutes;
import com.example.routes.ActivityStudentRoutes;
import com.example.routes.AuthRoutes;
import com.example.routes.ForumRoutes;
import com.example.routes.GroupRoutes;
import com.example.routes.ResourceRoutes;
import com.example.routes.ScrambleRoutes;
import com.example.routes.StudentGameRoutes;
import com.example.routes.StudentRoutes;
import com.example.routes.TopicRoutes;
import com.example.routes.TriviaRoutes;
import com.example.routes.UnitRoutes;
import com.example.routes.ProfileRoutes;
import com.example.services.ActivityService;
import com.example.services.ActivityStudentService;
import com.example.services.AuthService;
import com.example.services.ForumService;
import com.example.services.GroupService;
import com.example.services.JwtService;
import com.example.services.ProfileService;
import com.example.services.StudentService;
import com.example.services.TopicService;
import com.example.services.TriviaService;
import com.example.services.UnitService;
import com.example.utils.OwnershipValidator;

public class DependencyContainer {

    public final AuthRoutes authRoutes;
    public final GroupRoutes groupRoutes;
    public final StudentRoutes studentRoutes;
    public final UnitRoutes unitRoutes;
    public final TopicRoutes topicRoutes;
    public final ResourceRoutes resourceRoutes;
    public final ActivityRoutes activityRoutes;
    public final TriviaRoutes triviaRoutes;
    public final ScrambleRoutes scrambleRoutes;
    public final ActivityStudentRoutes activityStudentRoutes;
    public final StudentGameRoutes studentGameRoutes;
    public final ProfileRoutes profileRoutes;
    public final ForumRoutes forumRoutes;
    public final JwtService jwtService;
    public final ActivityService activityService;

    public DependencyContainer(Connection connection) {

        // ==========================================================
        // FASE 1: Repositories. Se construyen todos primero porque
        // OwnershipValidator y varios Services (Group, Profile...)
        // necesitan repositorios de otros módulos.
        // ==========================================================
        var authRepository = new AuthRepositoryImpl(connection);
        var groupRepository = new GroupRepositoryImpl(connection);
        var studentRepository = new StudentRepositoryImpl(connection);
        var unitRepository = new UnitRepositoryImpl(connection);
        var topicRepository = new TopicRepositoryImpl(connection);
        var resourceRepository = new ResourceRepositoryImpl(connection);
        var activityRepository = new ActivityRepositoryImpl(connection);
        var triviaRepository = new TriviaRepositoryImpl(connection);
        var scrambleRepository = new ScrambleRepositoryImpl(connection);
        var activityStudentRepository = new ActivityStudentRepositoryImpl(connection);
        var studentGameRepository = new StudentGameRepositoryImpl(connection);
        var profileRepository = new ProfileRepositoryImpl(connection);
        var forumRepository = new ForumRepositoryImpl(connection);
        var commentRepository = new CommentRepositoryImpl(connection);

        // Validador de propiedad (docente dueño del grupo, en última instancia,
        // del recurso sobre el que está operando). Reutilizado por todos los
        // módulos que restringen edición/eliminación al docente propietario.
        var ownershipValidator = new OwnershipValidator(groupRepository, unitRepository, topicRepository);

        // ==========================================================
        // FASE 2: Services, Controllers y Routes.
        // ==========================================================

        // Módulo Auth
        this.jwtService = new JwtService();
        var jwtService = this.jwtService;
        var authService = new AuthService(authRepository, jwtService);
        var authController = new AuthController(authService);
        this.authRoutes = new AuthRoutes(authController);

        //Modulo grupo
        var groupService = new GroupService(groupRepository, authRepository, studentRepository, profileRepository, ownershipValidator);
        var groupController = new GroupController(groupService);
        this.groupRoutes = new GroupRoutes(groupController);

        //student
        var studentService = new StudentService(studentRepository, groupRepository);
        var studentController = new StudentController(studentService);
        this.studentRoutes = new StudentRoutes(studentController);

        //unit
        var unitService = new UnitService(unitRepository, groupRepository, authRepository, ownershipValidator);
        var unitController = new UnitController (unitService);
        this.unitRoutes = new UnitRoutes(unitController);

        // Topic
        var topicService = new TopicService(topicRepository, unitRepository, authRepository, ownershipValidator);
        var topicController = new TopicController(topicService);
        this.topicRoutes = new TopicRoutes(topicController);

        // Resource
        var resourceService = new ResourceService(resourceRepository, topicRepository, authRepository, ownershipValidator);
        var resourceController = new ResourceController(resourceService);
        this.resourceRoutes = new ResourceRoutes(resourceController);

        //activity
        this.activityService = new ActivityService(activityRepository, topicRepository, unitRepository, authRepository, triviaRepository, scrambleRepository, ownershipValidator);
        var activityService = this.activityService;
        var activityController = new ActivityController(activityService);
        this.activityRoutes = new ActivityRoutes(activityController);

        //trivia
        var triviaService = new TriviaService(triviaRepository, activityRepository, authRepository, ownershipValidator);
        var triviaController = new TriviaController(triviaService);
        this.triviaRoutes = new TriviaRoutes(triviaController);

        //scramble
        var scrambleService = new ScrambleService(scrambleRepository, activityRepository, authRepository, ownershipValidator);
        var scrambleController = new ScrambleController(scrambleService);
        this.scrambleRoutes = new ScrambleRoutes(scrambleController);

        //actividad estudiante
        var activityStudentService = new ActivityStudentService(activityStudentRepository, activityRepository, authRepository);
        var activityStudentController = new ActivityStudentController(activityStudentService);
        this.activityStudentRoutes = new ActivityStudentRoutes(activityStudentController);

        // Student Game
        var studentGameService = new StudentGameService(studentGameRepository,activityStudentRepository,activityRepository);
        var studentGameController = new StudentGameController(studentGameService);
        this.studentGameRoutes = new StudentGameRoutes(studentGameController);


        // Perfil y estadísticas
        var profileService = new ProfileService(profileRepository, authRepository);
        var profileController = new ProfileController(profileService);
        this.profileRoutes = new ProfileRoutes(profileController);

        // Foro
        var forumService = new ForumService(forumRepository, commentRepository, groupRepository, authRepository);
        var forumController = new ForumController(forumService);
        this.forumRoutes = new ForumRoutes(forumController);
    }

}
