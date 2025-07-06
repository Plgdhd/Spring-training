package remind.remember.fix;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import remind.remember.fix.models.PersonH;

public class HibernateTest {
    public static void main(String[] args) {

        Configuration configuration = new Configuration().addAnnotatedClass(PersonH.class);

        SessionFactory sessionFactory = configuration.buildSessionFactory();

        Session session = sessionFactory.getCurrentSession();
        try {
            session.beginTransaction();
            PersonH first = new PersonH("Oleg", 2);
            PersonH second = new PersonH("Dimas", 24);
            PersonH third = new PersonH("Bob", 12);

            session.save(first);
            session.save(second);
            session.save(third);
            
            session.getTransaction().commit();
        } finally {
            sessionFactory.close();
        }

    }
}
