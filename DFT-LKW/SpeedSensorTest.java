import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SpeedSensorTest {
    // In a real ADS, this would be your Sensor Driver or Hardware Interface
    SpeedSensor sensor = new SpeedSensor(); 

    @Test
    void testSensorIntegrity() {
        Double speed = sensor.getSpeed();
        
        if (speed != null) {
            // Constraint 1: Negative Velocity [cite: 187]
            // High-level logic might ignore -50.0, but the sensor must flag it as a fault.
            assertTrue(speed >= 0, "Sensor Error: Velocity cannot be negative [cite: 193]");

            // Constraint 2: Out-of-Range/Physical Limits [cite: 187, 194]
            // Detects hardware "stuck-at" faults or impossible physics (e.g., 500 km/h).
            assertTrue(speed <= 250, "Sensor Error: Velocity exceeds physical vehicle limits [cite: 194]");
        } else {
            // Constraint 3: Data Availability [cite: 187]
            // Validates how the component handles intermittent hardware failure.
            fail("Sensor hardware failure: Data unavailable [cite: 174]");
        }
    }
}