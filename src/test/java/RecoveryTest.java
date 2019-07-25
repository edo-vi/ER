import DataGenerator.Sickness;
import DataGenerator.Value;
import Entities.Monitoring;
import Entities.Patient;
import Entities.Recovery;
import javafx.application.Platform;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class RecoveryTest {

    @Test
    public void getLastMonitoring() {
        Patient p = new Patient("Edoardo", "Zorzi", "Verona", "VRIOIO(", new Date());
        Recovery r = new Recovery("Ciao");

        try {
            p.admit(r);
        } catch(Patient.MoreThanOneActiveRecoveryException err) {

        }
        Monitoring mon = r.getLastMonitoring();
        //assertEquals(mon.getTemperature(), new Monitoring().defaultMonitoring().getTemperature());
        assertEquals(mon.getDiastolicPressure(), new Monitoring().defaultMonitoring().getDiastolicPressure());
        assertEquals(mon.getSystolicPressure(), new Monitoring().defaultMonitoring().getSystolicPressure());
        assertEquals(mon.getHeartRate(), new Monitoring().defaultMonitoring().getHeartRate());
    }


    @Test
    public void discharge() {
        Patient p = new Patient("Edoardo", "Zorzi", "Verona", "VRIOIO(", new Date());
        Recovery r = new Recovery("Ciao");

        try {
            p.admit(r);
        } catch (Patient.MoreThanOneActiveRecoveryException err) {

        }

        p.discharge(r, "ciao");
        assertTrue(r.isDischarged());
        assertTrue(p.isDischarged());
    }
}