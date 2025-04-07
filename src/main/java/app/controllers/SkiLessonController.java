package app.controllers;

import app.config.HibernateConfig;
import app.daos.SkiLessonDAO;
import app.dtos.SkiLessonDTO;
import app.dtos.TotalPriceDTO;
import app.entities.Instructor;
import app.entities.SkiLesson;
import app.enums.Levels;
import app.exceptions.ApiException;
import app.utils.Populator;
import io.javalin.http.Context;
import io.javalin.validation.Validator;
import jakarta.persistence.EntityManager;
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

    // Read a specific SkiLesson by ID
    @Override
    public void read(Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class)
                .check(this::validatePrimaryKey, "Not a valid id")
                .get();
        SkiLessonDTO skiLessonDTO = dao.read(id);
        ctx.status(200).json(skiLessonDTO, SkiLessonDTO.class);
    }

    // Read all SkiLessons
    @Override
    public void readAll(Context ctx) {
        List<SkiLessonDTO> skiLessonDTOS = dao.readAll();
        ctx.status(200).json(skiLessonDTOS, SkiLessonDTO.class);
    }

    // Create a new SkiLesson
    @Override
    public void create(Context ctx) {
        SkiLessonDTO skiLessonDTO = ctx.bodyAsClass(SkiLessonDTO.class);
        SkiLessonDTO createdSkiLessonDTO = dao.create(skiLessonDTO);
        ctx.status(201).json(createdSkiLessonDTO, SkiLessonDTO.class);
    }

    // Update an existing SkiLesson
    @Override
    public void update(Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class)
                .check(this::validatePrimaryKey, "Not a valid id")
                .get();
        SkiLessonDTO updatedSkiLessonDTO = dao.update(id, validateEntity(ctx));
        ctx.status(200).json(updatedSkiLessonDTO, SkiLessonDTO.class);
    }

    // Delete a SkiLesson by ID
    @Override
    public void delete(Context ctx) {
        Integer id = ctx.pathParamAsClass("id", Integer.class)
                .check(this::validatePrimaryKey, "Not a valid id")
                .get();
        dao.delete(id);
        ctx.status(204);
    }

    // Validate if the primary key (ID) exists
    @Override
    public boolean validatePrimaryKey(Integer id) {
        return dao.validatePrimaryKey(id);
    }

    // Validate the incoming SkiLesson entity
    @Override
    public SkiLessonDTO validateEntity(Context ctx) {
        return ctx.bodyValidator(SkiLessonDTO.class)
                .check(sl -> sl.getName() != null && !sl.getName().isEmpty(), "Ski Lesson name must be set")
                .check(sl -> sl.getStartTime() != null, "Start time must be set")
                .check(sl -> sl.getEndTime() != null, "End time must be set")
                .check(sl -> sl.getPrice() != null && sl.getPrice() > 0, "Price must be a positive number")
                .get();
    }

    // Populate the database with sample data (SkiLessons and instructors)
    public void populate(Context ctx) {
        try {
            // Initializer Populator and call the method to populate SkiLessons and Instructors
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

            // Kald DAO-metode for at tilføje instruktør til skilektion
            dao.addInstructorToSkiLesson(lessonId, instructorId);

            ctx.status(204);  // OK, men ingen indhold
        } catch (Exception e) {
            ctx.status(500).json("{ \"error\": \"Failed to add instructor to SkiLesson\" }");
        }
    }

    public void filterByLevel(Context ctx) {
        try {
            // Hent niveau som en path parameter
            String levelParam = ctx.pathParam("level").toUpperCase();

            // Valider om niveauet findes i enum
            Levels level = Levels.valueOf(levelParam);

            // Brug DAO til at hente skilektioner baseret på niveau
            Set<SkiLessonDTO> skiLessons = dao.getSkiLessonsByLevel(level);

            // Hvis ingen lektioner blev fundet, returner 404
            if (skiLessons.isEmpty()) {
                ctx.status(404).json("{ \"message\": \"No Ski Lessons found for this level.\" }");
                return;
            }

            // Returner de filtrerede ski lessons
            ctx.status(200).json(skiLessons, SkiLessonDTO.class);
        } catch (IllegalArgumentException e) {
            // Hvis niveauet ikke findes i enum, returner en 400
            ctx.status(400).json("{ \"error\": \"Invalid level provided\" }");
        } catch (Exception e) {
            // Fejlbehandling ved uventede problemer
            e.printStackTrace();
            ctx.status(500).json("{ \"error\": \"Internal server error\" }");
        }
    }
}

