package app.daos;

import app.dtos.SkiLessonDTO;

import java.util.List;
import java.util.Set;

public interface IDAO<T, I> {

    T read(I i);
    List<T> readAll();
    T create(T t);
    T update(I i, T t);
    void delete(I i);
    boolean validatePrimaryKey(I i);

}
