package Main;

import Entities.Patient;
import Entities.Recovery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class ClearDatabase {
    private static SessionFactory sessionFactory = new Configuration().configure("hibernate.overwrite.cfg.xml").buildSessionFactory();

    public static void main(String ...args) {
        Session session = sessionFactory.openSession();

        session.beginTransaction();
        session.getTransaction().commit();

    }

}
