# Data Flow Testing (DFT) with Laski-Korel-Weyuker (LKW) framework


##  DFT 

Data Flow Testing (DFT): Unlike Control Flow Testing (CFT), which focuses on the structural branches of a program, DFT centers on the lifecycle of variables. It tracks how data moves through a system by identifying three fundamental elements:

- Definition (def): When a value is assigned to a variable (e.g., x = sensor_read()).
- Use (use): When that value is accessed. This is split into c-use (computational, like math) and p-use (predicate, like an if statement) .
- Definition-Clear Path: A path between a definition and a use where the variable is not redefined.
  
  
## The LKW Framework (Laski, Korel, and Weyuker)

The LKW framework provides formal criteria and axioms to measure the "adequacy" of these tests.

Laski-Korel Criteria: These strategies ensure the relationship between data production and consumption is thoroughly covered:

- All-Definitions (All-Defs): Every definition of a variable must reach at least one use. It prevents "Dead Definitions" where data is overwritten before it's used.
- All-Uses: Every definition must reach every possible reachable use. In ADS, this ensures a sensor value influences all relevant actuators, not just one.
- All-DU-Paths: The most rigorous level, requiring every simple path between a definition and a use to be tested. This identifies "state-drift" or corruption in loops.
  
Weyuker’s Seventh Axiom:
- Antidecomposition: This axiom warns that testing a system ($P$) as a whole does not mean its individual components ($Q$) are adequately tested. For example, a high-level "Overspeeding" test might pass, while the underlying "SpeedSensor" component not tested for physical constraints like negative velocity

    The Formal Definition: Weyuker’s Seventh Axiom states that there exist programs $P$ and $Q$, where $Q$ is a component of $P$, such that a test set $T$ is adequate for $P$, but the restriction of $T$ to $Q$ is not adequate for $Q$

    --> Testing the whole does not guarantee you have tested the parts with enough rigor

# Workshop Tutorial: AI-Supported DFT with LKW Criteria

Goal: Use AI to automate the identification of DU-pairs and ensure compliance with LKW.


### Phase 1: AI-Driven Identifying the Data Flow Context
Objective: Use an LLM to map the Data Flow Context.

    "Act as a software verification expert. Analyze the calculateTorque method. Identify all Definitions (def) and Uses (c-use and p-use) for the variable clampedSpeed. Map the Data Flow Context for the final return statement using the Laski-Korel definition: $Context(S) = \{(v,d) \mid d \text{ is a definition of } v \text{ that reaches } S_i\}$."

### Phase 2: Testing the "All-Uses" Criterion
Objective: Generate test cases that go beyond simple "All-Defs" coverage.

    Prompt:

    "Based on the identified DU-pairs, explain why a test suite that only provides sensorInput = 105.0 satisfies the All-Definitions criterion but fails the All-Uses criterion. Identify the specific 'Testing Gap' regarding the safety guard at Use 1."

    Response:

    Failed: if the test suite doesn't provide a case $>110.0$ to exercise the path where clampedSpeed is redefined by the safety guard.



### Phase 3: Weyuker’s Axiom Guardrail
Objective: Decouple component tests from system tests.

Warning: Even if calculateTorque passes, the sensorInput itself must be unit-tested for physical limits (e.g., negative values) to satisfy Weyuker’s Seventh Axiom. 


- Program $P$ (The System): The VelocityGovernor.calculateTorque method, which handles the logic of clamping speed and calculating torque .
- Component $Q$ (The Part): The source of the sensorInput. In a real ADS, this would be a hardware driver or a Sensor class.1. 
  
The "Adequate" Test Set $T$ for System $P$Imagine you create a test suite $T$ to get 100% coverage of the VelocityGovernor logic:

- Test 1: sensorInput = 90.0 (Checks normal torque calculation).
- Test 2: sensorInput = 120.0 (Checks the MAX_SAFE_SPEED clamping logic).
 
This test set $T$ is adequate for $P$ because it exercises every branch and every data flow path (All-Uses) within the governor .

Why $T$ is "Inadequate" for Component $Q$: While $T$ makes the governor look perfect, it fails to stress the underlying sensor's requirements. If we restrict our testing to only what $P$ needs (values like 90 and 120), we miss critical sensor faults:
- Negative Velocity: What if the sensor malfunctions and sends -50.0? The governor $P$ would simply treat this as "not overspeeding" and return a negative torque, potentially causing a mechanical failure.
- Out-of-Range Data: What if the sensor sends 500.0? $P$ will safely clamp it to 110.0, but it completely misses the fact that the sensor is reporting a physically impossible speed for a car, which indicates a hardware fault.
  
Significance for ADS SafetyIn Autonomous Driving Systems (ADS), "Top-Down" testing alone is dangerous. Under the LKW framework, you must prove adequacy for the component $Q$ individually to ensure that sensor faults are caught at the source before they reach high-level logic that might treat a "faulty" value as a "valid" input 