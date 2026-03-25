import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VelocityGovernorTest {
    VelocityGovernor governor = new VelocityGovernor();

    @Test
    void testAllUsesAdequacy() {
        // Test Case 1: Normal Operation (80.0)
        // Exercises: Def 1 -> Use 2 (Torque) [cite: 80, 114]
        // Confirms data reaches the actuator command.
        assertEquals(64.0, governor.calculateTorque(80.0), 0.01);

        // Test Case 2: High Speed Warning (105.0)
        // Exercises: Def 1 -> Use 2 (Torque) AND Use 3 (Dashboard P-Use)
        // Satisfies All-Uses for the warning threshold.
        assertEquals(84.0, governor.calculateTorque(105.0), 0.01);

        // Test Case 3: Safety Guard Clamping (120.0)
        // Exercises: Def 1 -> Use 1 (Guard P-Use) 
        // AND Def 2 (Clamped Value) -> Use 2 and Use 3.
        // This closes the "Testing Gap" where high-speed logic is ignored
        assertEquals(88.0, governor.calculateTorque(120.0), 0.01);
    }
}