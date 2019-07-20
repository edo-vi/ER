package State;

import Entities.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class DatabaseService {
    private static DatabaseService instance;
    private static SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
    private static Session session = sessionFactory.openSession();

    public static DatabaseService getInstance() {
        if (instance == null) {
            instance = new DatabaseService();
        }
        return instance;
    }

    private DatabaseService() {

    }


    public static List<Entry> getEntries(String tableName) throws IllegalArgumentException {
        if (!tableName.equals("Patient") && !tableName.equals("Administration") && !tableName.equals("Prescription")
        && !tableName.equals("User") && !tableName.equals("Recovery") && !tableName.equals("Monitoring"))
            throw new IllegalArgumentException("You must insert a valid table name: Patient, Administration, Prescription" +
                    ", User, Recovery or Monitoring");
        String q = "FROM " + tableName;
        return session.createQuery(q).list();
    }

    public static void addEntry(Entry entry) {
        session.beginTransaction();
        session.save(entry);
        session.getTransaction().commit();
    }

    public static void addEntries(Entry ...entries) {
        session.beginTransaction();
        for (Entry e : entries) {
            session.save(e);
        }
        session.getTransaction();
    }
}
