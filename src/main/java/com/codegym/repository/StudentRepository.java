package com.codegym.repository;

import com.codegym.model.Student;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.swing.text.html.parser.Entity;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class StudentRepository implements IStudentRepository {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<Student> findAll() {
        TypedQuery<Student> query = entityManager.createQuery("select p from Student as p", Student.class);
        return query.getResultList();
    }

    @Override
    public Student findById(Long id) {
        TypedQuery<Student> query = entityManager.createQuery("select p from Student as p where p.id = :id", Student.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public void save(Student student) {
        if (student.getId() != null) {
            entityManager.merge(student);
        } else {
            entityManager.persist(student);
        }
    }

    @Override
    public void remote(Long id) {
        Student student = findById(id);
        if (student != null) {
            entityManager.remove(student);
        }
    }

    @Override
    public List<Student> findByName(String name) {
        return null;
    }
}
