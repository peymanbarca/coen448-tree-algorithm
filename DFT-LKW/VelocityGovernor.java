public class VelocityGovernor {
    private static final double MAX_SAFE_SPEED = 110.0;
    private static final double TORQUE_FACTOR = 0.8;

    public double calculateTorque(double sensorInput) {
        // Definition (Def 1): clampedSpeed
        double clampedSpeed = sensorInput; 

        // Use 1 (Predicate/p-use): Safety Guard
        if (clampedSpeed > MAX_SAFE_SPEED) { 
            clampedSpeed = MAX_SAFE_SPEED; // Def 2: Redefinition
        }

        // Use 2 (Computation/c-use): Torque Calculation
        double torque = clampedSpeed * TORQUE_FACTOR;

        // Use 3 (Predicate/p-use): Dashboard Alert
        if (clampedSpeed > 100.0) { 
            displayHighSpeedWarning();
        }

        return torque;
    }

    private void displayHighSpeedWarning() {
        System.out.println("Warning: Operating at high speed.");
    }
}