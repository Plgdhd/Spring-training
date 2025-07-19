package remind.remember.fix.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
// import remind.remember.fix.controllers.BatchController;
import remind.remember.fix.models.Person;
import remind.remember.fix.utils.PersonValidator;

import java.beans.Transient;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class PersonDAO {

    private final PersonValidator personValidator;

    private final SessionFactory sessionFactory;

    @Autowired
    public PersonDAO(SessionFactory sessionFactory, PersonValidator personValidator){
        this.sessionFactory = sessionFactory;
        this.personValidator = personValidator;
    }

    @Transactional//все что в этом методе происходит внутри Hibernate транзакции
    public List<Person> index() {
        Session session = sessionFactory.getCurrentSession();

        List<Person> people = session.createQuery("SELECT p FROM Person p", Person.class).getResultList();
        return people;
    }

    @Transactional
    public Person show(int id) {
        Session session = sessionFactory.getCurrentSession();

        return session.get(Person.class, id);
    }

    @Transactional
    public Optional<Person> show(String email){
        Session session  = sessionFactory.getCurrentSession();
        return session.createQuery("SELECT p FROM Person WHERE p.email = :email", Person.class).getResultList().stream().findFirst();
    }

    @Transactional
    public void save(Person person) {
        Session session = sessionFactory.getCurrentSession();
        session.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson) {
        Session session = sessionFactory.getCurrentSession();
        Person personToUpdate = session.get(Person.class, id);
        
        personToUpdate.setAddress(updatedPerson.getAddress());
        personToUpdate.setEmail(updatedPerson.getEmail());
        personToUpdate.setName(updatedPerson.getName());
        personToUpdate.setAge(updatedPerson.getAge());
    }

    public void delete(int id) {
        Session session = sessionFactory.getCurrentSession();
        Person person = session.get(Person.class, id);
        if(person != null){ 
            session.delete(person);
        }
    }


    //тестирование

    // public void testMultipleUpdate(){
    //     List<Person> people = create1000People();

    //     long before = System.currentTimeMillis();

    //     for(Person data : people){
    //         jdbcTemplate.update("INSERT INTO Person VALUES(?,?,?,?)", data.getId(), data.getName(), data.getAge(), data.getEmail());
    //     }
    //     long after = System.currentTimeMillis() ;
    //     System.out.println("Добавление 1000 пользователей без batch: " + (after-before));
    // }

    // public void testBathcUpdate(){
    //     List<Person> people = create1000People();

    //     long before = System.currentTimeMillis();
    //     jdbcTemplate.batchUpdate("INSERT INTO Person VALUES(?,?,?,?)", 
    //     new BatchPreparedStatementSetter() {
    //         @Override
    //         public void setValues(PreparedStatement preparedStatement, int i) throws SQLException{
    //             preparedStatement.setInt(1, people.get(i).getId());
    //             preparedStatement.setString(2, people.get(i).getName());
    //             preparedStatement.setInt(3, people.get(i).getAge());
    //             preparedStatement.setString(4, people.get(i).getEmail());

    //         }
    //         @Override
    //         public int getBatchSize(){
    //             return people.size();
    //         }
    //     });
    //     long after = System.currentTimeMillis();

    //     System.out.println("Time with batch: " + (after-before));
    // }

    // private List<Person> create1000People(){
    //     List<Person> people = new ArrayList<>();

    //     for(int i = 0; i <1000;++i){
    //         people.add(new Person(i, "Name" + i,  30, "test " + i + "mail.ru" , "Holo 3"));
    //     }
    //     return people;
    // }
}
