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
                new GregorianCalendar(1965, Calendar.APRIL, 11).getTime());
        Patient patient2 = new Patient("Barbara", "Oliboni", "BRBLBIDONTKNOW329I", "Verona",
                new GregorianCalendar(1973, Calendar.OCTOBER, 8).getTime());
        Patient patient3 = new Patient("Roberto", "Posenato", "PSNRBRA373UUS88I",
                "Verona", new GregorianCalendar(1981, Calendar.FEBRUARY, 11).getTime());
        Recovery rec1 = new Recovery(new GregorianCalendar(2019, Calendar.JUNE, 22).getTime(),"Fractured Meniscus",
                patient1);
        Recovery rec2 = new Recovery(new GregorianCalendar(2019, Calendar.JULY, 16).getTime(), "Salmonella",
                patient2);
        Recovery rec3 = new Recovery(new GregorianCalendar(2019, Calendar.MAY, 3).getTime(), "Blunt Trauma",
                patient3);
        rec3.discharge("nice",new GregorianCalendar(2019, Calendar.JUNE, 30).getTime());
        Recovery rec4 = new Recovery(new GregorianCalendar(2016, Calendar.OCTOBER, 24).getTime(), "Severe vertigo"
                , patient1);
        rec4.discharge("Not dead so it was a success.");
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
}
