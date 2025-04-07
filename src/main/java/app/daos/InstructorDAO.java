package app.daos;

import app.dtos.InstructorDTO;
import app.dtos.SkiLessonDTO;
import app.entities.Instructor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Set;

public class InstructorDAO implements IDAO<InstructorDTO, Integer>{

    private static InstructorDAO instance;
    private static EntityManagerFactory emf;

    public static InstructorDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new InstructorDAO();
        }
        return instance;
    }

    @Override
    public InstructorDTO read(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            Instructor instructor = em.find(Instructor.class, id);
            return instructor != null ? new InstructorDTO(instructor) : null;
        }
    }

    @Override
    public List<InstructorDTO> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<InstructorDTO> query = em.createQuery(
                    "SELECT new app.dtos.InstructorDTO(i) FROM Instructor i", InstructorDTO.class);
            return query.getResultList();
        }
    }

    @Override
    public InstructorDTO create(InstructorDTO instructorDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Instructor instructor = new Instructor(instructorDTO); // Du skal have denne constructor i din entity
            em.persist(instructor);
            em.getTransaction().commit();
            return new InstructorDTO(instructor);
        }
    }

    @Override
    public InstructorDTO update(Integer id, InstructorDTO instructorDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Instructor instructor = em.find(Instructor.class, id);
            if (instructor == null) {
                em.getTransaction().rollback();
                return null;
            }

            instructor.setFirstName(instructorDTO.getFirstName());
            instructor.setLastName(instructorDTO.getLastName());
            instructor.setEmail(instructorDTO.getEmail());
            instructor.setPhone(instructorDTO.getPhone());
            instructor.setYearsOfExperience(instructorDTO.getYearsOfExperience());

            Instructor mergedInstructor = em.merge(instructor);
            em.getTransaction().commit();
            return new InstructorDTO(mergedInstructor);
        }
    }

    @Override
    public void delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Instructor instructor = em.find(Instructor.class, id);
            if (instructor != null) {
                em.remove(instructor);
            }
            em.getTransaction().commit();
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            Instructor instructor = em.find(Instructor.class, id);
            return instructor != null;
        }
    }


    // Fjernes fordi dette DAO er for Instructor
    // addInstructorToSkiLesson og getSkiLessonsByInstructor hører hjemme i SkiLessonDAO
}