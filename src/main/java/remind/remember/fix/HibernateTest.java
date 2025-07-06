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

            PersonH first = session.get(PersonH.class, 1);

            System.out.println(first.getName());
            System.out.println(first.getAge());

            session.getTransaction().commit();
        } finally {
            sessionFactory.close();
        }

    }
}
