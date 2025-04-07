package app.routes;

import app.controllers.SkiLessonController;
import io.javalin.apibuilder.EndpointGroup;
import app.security.enums.Role;

import static io.javalin.apibuilder.ApiBuilder.*;

public class SkiRoutes {
    private final SkiLessonController skiLessonController = new SkiLessonController();


    public EndpointGroup getRoutes()
    {
        return () -> {
            get("/populate", skiLessonController::populate);
            post("/", skiLessonController::create, Role.USER);
            get("/", skiLessonController::readAll, Role.USER);
            get("/{id}", skiLessonController::read);
            put("/{id}", skiLessonController::update);
            delete("/{id}", skiLessonController::delete);
            put("/{lessonId}/instructors/{instructorId}", skiLessonController::addInstructorToSkiLesson);
            get("/level/{level}", skiLessonController::filterByLevel);
        };
        };
    }

