package app.utils;

import app.entities.Instructor;
import app.entities.SkiLesson;
import app.enums.Levels;
import app.security.entities.Role;
import app.security.entities.User;
import dk.bugelhartmann.UserDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalTime;

public class Populator {

    private EntityManagerFactory emf;

    public Populator(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void populateDatabase() {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Instructor instructor1 = new Instructor("John", "Doe", "john.doe@example.com", "123456789", 5);
            Instructor instructor2 = new Instructor("Jane", "Smith", "jane.smith@example.com", "987654321", 8);

            em.persist(instructor1);
            em.persist(instructor2);

            SkiLesson lesson1 = new SkiLesson(
                    "Beginner Skiing",
                    LocalTime.of(10, 0),
                    LocalTime.of(12, 0),
                    50.0,
                    Levels.BEGINNER,   // Use enum here
                    new SkiLesson.Location(45.764, 4.8357),
                    instructor1
            );

            SkiLesson lesson2 = new SkiLesson(
                    "Advanced Skiing",
                    LocalTime.of(13, 0),
                    LocalTime.of(15, 0),
                    75.0,
                    Levels.ADVANCED,  // Use enum here
                    new SkiLesson.Location(45.764, 4.8357),
                    instructor2
            );

            SkiLesson lesson3 = new SkiLesson(
                    "Intermediate Skiing",
                    LocalTime.of(9, 0),
                    LocalTime.of(11, 0),
                    60.0,
                    Levels.INTERMEDIATE,  // Use enum here
                    new SkiLesson.Location(45.764, 4.8357),
                    instructor1
            );
            em.persist(lesson1);
            em.persist(lesson2);
            em.persist(lesson3);

            em.getTransaction().commit();

        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
    public static UserDTO[] populateUsers(EntityManagerFactory emf) {
        User user, admin;
        Role userRole, adminRole;

        user = new User("usertest", "user123");
        admin = new User("admintest", "admin123");
        userRole = new Role("USER");
        adminRole = new Role("ADMIN");
        user.addRole(userRole);
        admin.addRole(adminRole);

        try (var em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            em.persist(userRole);
            em.persist(adminRole);
            em.persist(user);
            em.persist(admin);
            em.getTransaction().commit();
        }
        UserDTO userDTO = new UserDTO(user.getUsername(), "user123");
        UserDTO adminDTO = new UserDTO(admin.getUsername(), "admin123");
        return new UserDTO[]{userDTO, adminDTO};
    }
}