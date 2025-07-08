package remind.remember.fix;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.lock.PessimisticEntityLockException;

import remind.remember.fix.models.Person;
import remind.remember.fix.models.PersonH;

public class HibernateTest {
    public static void main(String[] args) {

        Configuration configuration = new Configuration().addAnnotatedClass(PersonH.class);

        SessionFactory sessionFactory = configuration.buildSessionFactory();

        Session session = sessionFactory.getCurrentSession();
        try {
            session.beginTransaction();

            List<PersonH> people = session.createQuery("FROM Person WHERE age > 30 AND name LIKE 'T%'").getResultList();

            session.createQuery("UPDATE Person SET name = 'Oleg WHERE age<20").executeUpdate();

            for(PersonH data : people){
                System.out.println(data);
            }
            session.getTransaction().commit();
        } finally {
            sessionFactory.close();
        }

    }
}
