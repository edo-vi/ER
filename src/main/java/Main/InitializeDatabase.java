package Main;

import Entities.Administration;
import Entities.Patient;
import Entities.Prescription;
import Entities.Recovery;
import State.DatabaseService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class InitializeDatabase {
    private static SessionFactory sessionFactory = new Configuration().configure("hibernate.overwrite.cfg.xml").buildSessionFactory();

    public static void main(String[] args) {
        Session session = sessionFactory.openSession();

        Patient patient1 = new Patient("Carlo", "Combi", "CMBCBMWHATEVER291Z", "Verona",
                new GregorianCalendar(1967, Calendar.APRIL, 11).getTime());
        Patient patient2 = new Patient("Barbara", "Oliboni", "BRBLBIDONTKNOW329I", "Verona",
                new GregorianCalendar(1973, Calendar.OCTOBER, 8).getTime());
        Patient patient3 = new Patient("Roberto", "Posenato", "PSNRBRA373UUS88I",
                "Verona", new GregorianCalendar(1970, Calendar.FEBRUARY, 11).getTime());


        try {
            Recovery active = new Recovery("Gomito del tennista");
            patient1.admit(active);
            session.beginTransaction();
            /*adm1.setPrescription(pr1);
            patient1.addToAddministrations(adm1);*/
            session.save(patient1);
            session.save(patient2);
            session.save(patient3);
            session.save(active);
        } catch (Exception e) {System.out.println(e);}

        session.getTransaction().commit();

    }
}
