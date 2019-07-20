import Entities.Administration;
import Entities.Patient;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class PatientTest {

    @Test
    public void getAdministrations() {
        Patient patient = new Patient();
        assertEquals(patient.getAdministrations(), new ArrayList<Administration>().add(new Administration()));
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