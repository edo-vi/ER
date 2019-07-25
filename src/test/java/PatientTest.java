import Entities.Administration;
import Entities.Patient;
import Entities.Recovery;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;

public class PatientTest {

    @Test
    public void constructor() {
        Patient p = new Patient("Edoardo", "Zorzi", "Verona", "VRIOIO(", new Date());
        assertTrue(p.isWaiting());

        try {
            p.admit(new Recovery("ciao"));
            assertTrue(p.isRecovered());
        } catch(Patient.MoreThanOneActiveRecoveryException err) {

        }

        p.discharge(p.getActiveRecoveries().stream().findFirst().orElse(null), "discharged");
        assert(p.isDischarged());

    }

    @Test
    public void getRecoveries() {
        Patient p = new Patient("Edoardo", "Zorzi", "Verona", "VRIOIO(", new Date());
        try {
            Recovery r1 = new Recovery("Mal di pancia", p);
            Recovery r2 = new Recovery("Mal di testa", p);

            p.admit(r1);
            p.discharge(r1, "discharged");
            p.admit(r2);

            assertTrue(p.getActiveRecoveries().contains(r2));
            assertTrue(p.getDischargedRecovery().contains(r1));

        } catch(Recovery.PatientNotAdmittedException | Patient.MoreThanOneActiveRecoveryException err) {

        }


    }

    @Test
    public void getAdministrations() {
        Patient patient = new Patient();
        ArrayList<Administration> l = new ArrayList<>();
        assertEquals(patient.getAdministrations(), l);
        Administration adm = new Administration();

        patient.addToAddministrations(adm);
        assertTrue(patient.getAdministrations().contains(adm));
    }


}