package app.daos;

import app.dtos.LocationDTO;
import app.dtos.SkiLessonDTO;
import app.dtos.TotalPriceDTO;
import app.entities.Instructor;
import app.entities.SkiLesson;
import app.enums.Levels;
import app.exceptions.DaoException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SkiLessonDAO implements IDAO<SkiLessonDTO, Integer>, ISkiLessonInstructorDAO {

    private static SkiLessonDAO instance;
    private static EntityManagerFactory emf;


    public static SkiLessonDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new SkiLessonDAO();
        }
        return instance;
    }
    @Override
    public SkiLessonDTO read(Integer integer) {
        try (EntityManager em = emf.createEntityManager()) {
            SkiLesson skiLesson = em.find(SkiLesson.class, integer);
            return new SkiLessonDTO(skiLesson);
        }
    }

    @Override
    public List<SkiLessonDTO> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<SkiLessonDTO> query = em.createQuery("SELECT new app.dtos.SkiLessonDTO(s) FROM SkiLesson s", SkiLessonDTO.class);
            return query.getResultList();
        }
    }

    @Override
    public SkiLessonDTO create(SkiLessonDTO skiLessonDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            SkiLesson skiLesson = new SkiLesson(skiLessonDTO);
            em.persist(skiLesson);
            em.getTransaction().commit();
            return new SkiLessonDTO(skiLesson);
        }
    }

    @Override
    public SkiLessonDTO update(Integer integer, SkiLessonDTO skiLessonDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            SkiLesson s = em.find(SkiLesson.class, integer);
            s.setName(skiLessonDTO.getName());
            s.setStartTime(LocalTime.parse(skiLessonDTO.getStartTime()));
            s.setEndTime(LocalTime.parse(skiLessonDTO.getEndTime()));
            s.setPrice(skiLessonDTO.getPrice());
            s.setLevel(skiLessonDTO.getLevel());
            LocationDTO locationDTO = skiLessonDTO.getLocation();
            if (locationDTO != null) {
                SkiLesson.Location location = new SkiLesson.Location();
                location.setLatitude(locationDTO.getLatitude());
                location.setLongitude(locationDTO.getLongitude());
                s.setLocation(location);
            }
            SkiLesson mergedLesson = em.merge(s);
            em.getTransaction().commit();
            return mergedLesson != null ? new SkiLessonDTO(mergedLesson) : null;
        }
    }

    @Override
    public void delete(Integer integer) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            // Find den SkiLesson, du vil slette, ved hjælp af dens ID
            SkiLesson skiLesson = em.find(SkiLesson.class, integer);

            // Hvis SkiLesson findes, fjernes den
            if (skiLesson != null) {
                em.remove(skiLesson);  // Slet entiteten, ikke bare ID'et
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer integer) {
        try (EntityManager em = emf.createEntityManager()) {
            SkiLesson skiLesson = em.find(SkiLesson.class, integer);
            return skiLesson != null;
        }
    }

    @Override
    public void addInstructorToSkiLesson(int lessonId, int instructorId) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            SkiLesson lesson = em.find(SkiLesson.class, lessonId);
            Instructor instructor = em.find(Instructor.class, instructorId);

            if (lesson == null || instructor == null) {
                em.getTransaction().rollback();
                return;
            }

            instructor.addLesson(lesson);

            em.merge(lesson);
            em.merge(instructor);

            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    @Override
    public Set<SkiLessonDTO> getSkiLessonsByInstructor(int instructorId) {
        EntityManager em = emf.createEntityManager();

        try {
            Instructor instructor = em.find(Instructor.class, instructorId);

            if (instructor == null || instructor.getSkiLessons() == null) {
                return Set.of();
            }

            return instructor.getSkiLessons()
                    .stream()
                    .map(SkiLessonDTO::new)
                    .collect(Collectors.toSet());
        } finally {
            em.close();
        }
    }

    public Set<SkiLessonDTO> getSkiLessonsByLevel(Levels level) {
        EntityManager em = emf.createEntityManager();
        try {
            // JPQL Query for at hente skilektioner baseret på niveau
            TypedQuery<SkiLesson> query = em.createQuery(
                    "SELECT s FROM SkiLesson s WHERE s.level = :level", SkiLesson.class);
            query.setParameter("level", level);

            // Hent resultaterne af queryen
            List<SkiLesson> lessons = query.getResultList();

            // Brug streams til at konvertere SkiLesson objekter til SkiLessonDTO objekter
            return lessons.stream()
                    .map(SkiLessonDTO::new)  // Mapper hver SkiLesson til en DTO
                    .collect(Collectors.toSet());  // Samler dem i et Set
        } finally {
            em.close();
        }
    }
}
