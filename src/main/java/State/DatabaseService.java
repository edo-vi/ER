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

    public static void initialize() {

        Patient patient1 = new Patient("Carlo", "Combi", "CMBCBMWHATEVER291Z", "Verona",
                new GregorianCalendar(1965, Calendar.APRIL, 11).getTime());
        Patient patient2 = new Patient("Barbara", "Oliboni", "BRBLBIDONTKNOW329I", "Verona",
                new GregorianCalendar(1973, Calendar.OCTOBER, 8).getTime());
        Patient patient3 = new Patient("Roberto", "Posenato", "PSNRBRA373UUS88I",
                "Verona", new GregorianCalendar(1981, Calendar.FEBRUARY, 11).getTime());
        Recovery rec1 = new Recovery(new GregorianCalendar(2019, Calendar.JUNE, 22).getTime(),"Fractured Meniscus",
                patient1);
        Recovery rec2 = new Recovery(new GregorianCalendar(2019, Calendar.JULY, 16).getTime(), "Salmonella",
                patient2);
        Recovery rec3 = new Recovery(new GregorianCalendar(2019, Calendar.MAY, 3).getTime(),
                new GregorianCalendar(2019, Calendar.JUNE, 30).getTime(), "Blunt Trauma",
                false, patient3);
        Recovery rec4 = new Recovery(new GregorianCalendar(2016, Calendar.OCTOBER, 24).getTime(),
                new GregorianCalendar(2016, Calendar.DECEMBER, 2).getTime(), "Severe vertigo",
                false, patient1);
        try {
            Prescription pr1 = new Prescription("Aspirin", new GregorianCalendar(2019, Calendar.JULY,10).getTime(),
                    20, 2389, 3, "Dr. Mario", rec1);
            Prescription pr2 = new Prescription("Oki", new GregorianCalendar(2016, Calendar.JULY, 9).getTime(),
                    4, 20, 3, "Dr. Warian", rec3);
            Administration adm1 = new Administration(new GregorianCalendar(2019, Calendar.JULY, 11).getTime(),
                    14, 75.76, "All good", patient1, pr1);

            Administration adm2 = new Administration(new GregorianCalendar(2019, Calendar.JULY, 11).getTime(),
                    2, 2, "Fine", patient1, pr2);


            session.beginTransaction();
            /*adm1.setPrescription(pr1);
            patient1.addToAddministrations(adm1);*/
            session.save(rec1);
            session.save(rec2);
            session.save(rec3);
            session.save(rec4);
            session.save(adm1);
            session.save(pr1);
            session.save(adm2);
            session.save(pr2);
            session.save(patient1);
            session.save(patient2);
            session.save(patient3);
            session.save(adm1);

        } catch (Exception e) {System.out.println(e);}

        session.getTransaction().commit();
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
