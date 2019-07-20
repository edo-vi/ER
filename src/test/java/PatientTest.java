import Entities.Administration;
import Entities.Patient;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class PatientTest {

    @Test
    public void getAdministrations() {
        Patient patient = new Patient();
        ArrayList<Administration> l = new ArrayList<>();
        assertEquals(patient.getAdministrations(), l);
    }

    @Test
    public void setAdministrations() {
    }

    @Test
    public void getRecoveries() {
    }

    @Test
    public void setRecoveries() {
    }
}