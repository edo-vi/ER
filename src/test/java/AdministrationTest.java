import Entities.Administration;
import Entities.Prescription;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class AdministrationTest {

    @Test
    public void getPrescription() {
        try {
            Prescription prescription = new Prescription("Aspirin", new Date(), 4, 5, 20, "Marian");
            Administration ad = new Administration(new Date(), 3, 4, "NN");
            prescription.addToAdministrations(ad);
            assertTrue(prescription.getAdministrations().contains(ad));
        } catch (Exception er) {

        }

    }

    @Test
    public void setPrescription() {

    }
}