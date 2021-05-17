package com.chpok.logiweb.dao.impl;

import com.chpok.logiweb.dao.EmployeeDao;
import com.chpok.logiweb.dao.exception.DatabaseRuntimeException;
import com.chpok.logiweb.model.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Repository
public class EmployeeDaoImpl implements EmployeeDao {
    private final SessionFactory sessionFactory;

    public EmployeeDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public void save(Employee entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.save(entity);

            session.getTransaction().commit();
        } catch (PersistenceException pe) {
            throw new DatabaseRuntimeException("DB saving employee exception", pe);
        }
    }

    @Override
    public Optional<Employee> findById(Long id) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();

            final Employee employee = session.get(Employee.class, id);

            session.getTransaction().commit();

            return Optional.of(employee);
        } catch (PersistenceException pe) {
            throw new DatabaseRuntimeException("DB find employee by id exception", pe);
        }
    }

    @Override
    public List<Employee> findAll() {
        return null;
    }

    @Override
    public void update(Employee entity) {

    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void deleteAllByIds(Set<Long> ids) {

    }
}
