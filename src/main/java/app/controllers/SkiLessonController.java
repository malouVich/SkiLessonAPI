package app.controllers;

import app.config.HibernateConfig;
import app.daos.SkiLessonDAO;
import app.dtos.SkiLessonDTO;
import app.enums.Levels;
import app.utils.Populator;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Set;

public class SkiLessonController implements IController<SkiLessonDTO, Integer>{
    private final EntityManagerFactory emf;
    private final SkiLessonDAO dao;

    public SkiLessonController() {
        this.emf = HibernateConfig.getEntityManagerFactory();
        this.dao = SkiLessonDAO.getInstance(emf);
    }

    @Override
    public void read(Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class)
                .check(this::validatePrimaryKey, "Not a valid id")
                .get();
        SkiLessonDTO skiLessonDTO = dao.read(id);
        ctx.status(200).json(skiLessonDTO, SkiLessonDTO.class);
    }

    @Override
    public void readAll(Context ctx) {
        List<SkiLessonDTO> skiLessonDTOS = dao.readAll();
        ctx.status(200).json(skiLessonDTOS, SkiLessonDTO.class);
    }


    @Override
    public void create(Context ctx) {
        SkiLessonDTO skiLessonDTO = ctx.bodyAsClass(SkiLessonDTO.class);
        SkiLessonDTO createdSkiLessonDTO = dao.create(skiLessonDTO);
        ctx.status(201).json(createdSkiLessonDTO, SkiLessonDTO.class);
    }


    @Override
    public void update(Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class)
                .check(this::validatePrimaryKey, "Not a valid id")
                .get();
        SkiLessonDTO updatedSkiLessonDTO = dao.update(id, validateEntity(ctx));
        ctx.status(200).json(updatedSkiLessonDTO, SkiLessonDTO.class);
    }


    @Override
    public void delete(Context ctx) {
        Integer id = ctx.pathParamAsClass("id", Integer.class)
                .check(this::validatePrimaryKey, "Not a valid id")
                .get();
        dao.delete(id);
        ctx.status(204);
    }


    @Override
    public boolean validatePrimaryKey(Integer id) {
        return dao.validatePrimaryKey(id);
    }

    @Override
    public SkiLessonDTO validateEntity(Context ctx) {
        return ctx.bodyValidator(SkiLessonDTO.class)
                .check(sl -> sl.getName() != null && !sl.getName().isEmpty(), "Ski Lesson name must be set")
                .check(sl -> sl.getStartTime() != null, "Start time must be set")
                .check(sl -> sl.getEndTime() != null, "End time must be set")
                .check(sl -> sl.getPrice() != null && sl.getPrice() > 0, "Price must be a positive number")
                .get();
    }

    public void populate(Context ctx) {
        try {
            Populator populator = new Populator(emf);
            populator.populateDatabase();
            ctx.status(200).json("{ \"message\": \"Ski Lessons and Instructors populated\" }");
        } catch (Exception e) {
            // Error handling
            ctx.status(500).json("{ \"error\": \"Failed to populate Ski Lessons and Instructors\" }");
        }
    }

    public void addInstructorToSkiLesson(Context ctx) {
        try {

            int lessonId = ctx.pathParamAsClass("lessonId", Integer.class).get();
            int instructorId = ctx.pathParamAsClass("instructorId", Integer.class).get();

            dao.addInstructorToSkiLesson(lessonId, instructorId);

            ctx.status(204);
        } catch (Exception e) {
            ctx.status(500).json("{ \"error\": \"Failed to add instructor to SkiLesson\" }");
        }
    }

    public void filterByLevel(Context ctx) {
        try {
            String levelParam = ctx.pathParam("level").toUpperCase();

            Levels level = Levels.valueOf(levelParam);

            Set<SkiLessonDTO> skiLessons = dao.getSkiLessonsByLevel(level);

            if (skiLessons.isEmpty()) {
                ctx.status(404).json("{ \"message\": \"No Ski Lessons found for this level.\" }");
                return;
            }

            ctx.status(200).json(skiLessons, SkiLessonDTO.class);
        } catch (IllegalArgumentException e) {
            ctx.status(400).json("{ \"error\": \"Invalid level provided\" }");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json("{ \"error\": \"Internal server error\" }");
        }
    }
}