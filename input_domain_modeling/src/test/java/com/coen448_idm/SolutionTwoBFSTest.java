package coen448.assignment1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Bipartite Graph Detection SolutionTwo BFS - IDM Test Suite")
public class SolutionTwoBFSTest {

    private SolutionTwoBFS solution = new SolutionTwoBFS();

    // ============================================================
    // Each Choice Coverage (ECC) Tests
    // ============================================================

    // @ParameterizedTest(name = "ECC-{0}: {8}")
    // @CsvFileSource(resources = "/test_cases_ecc.csv", numLinesToSkip = 1)
    // @DisplayName("ECC Tests - Each Choice Coverage")
    // void testEachChoiceCoverage(
    //         String testID,
    //         String graphSize,
    //         String edgePresence,
    //         String graphStructure,
    //         String cyclePresence,
    //         String selfLoops,
    //         String nodeIndexValidity,
    //         String nullInput,
    //         String expectedStr,
    //         String description,
    //         String graphJSON) {
        
    //     boolean expected = parseExpected(expectedStr);
    //     int[][] graph = parseGraph(graphSize, cyclePresence, graphStructure, nullInput);

    //     if (expected == false && isExceptionCase(expectedStr)) {
    //         assertThrows(Exception.class, () -> solution.isBipartite(graph),
    //                 "Test " + testID + ": " + description + " should throw exception");
    //     } else {
    //         boolean result = solution.isBipartite(graph);
    //         assertEquals(expected, result,
    //                 "Test " + testID + ": " + description + " expected " + expected + " but got " + result);
    //     }
    // }

    // // ============================================================
    // // Basic Choice Coverage (BCC) Tests
    // // ============================================================

    // @ParameterizedTest(name = "BCC-{0}: {8}")
    // @CsvFileSource(resources = "/test_cases_bcc.csv", numLinesToSkip = 1)
    // @DisplayName("BCC Tests - Basic Choice Coverage")
    // void testBasicChoiceCoverage(
    //         String testID,
    //         String graphSize,
    //         String edgePresence,
    //         String graphStructure,
    //         String cyclePresence,
    //         String selfLoops,
    //         String nodeIndexValidity,
    //         String nullInput,
    //         String expectedStr,
    //         String description,
    //         String graphJSON) {
        
    //     boolean expected = parseExpected(expectedStr);
    //     int[][] graph = parseGraph(graphSize, cyclePresence, graphStructure, nullInput);

    //     if (isExceptionCase(expectedStr)) {
    //         assertThrows(Exception.class, () -> solution.isBipartite(graph),
    //                 "Test " + testID + ": " + description + " should throw exception");
    //     } else {
    //         boolean result = solution.isBipartite(graph);
    //         assertEquals(expected, result,
    //                 "Test " + testID + ": " + description);
    //     }
    // }

    // ============================================================
    // Unit Tests for Specific Scenarios
    // ============================================================

    @Test
    @DisplayName("Empty graph is bipartite")
    void testEmptyGraph() {
        int[][] graph = {};
        assertTrue(solution.isBipartite(graph));
    }

    @Test
    @DisplayName("Single node is bipartite")
    void testSingleNode() {
        int[][] graph = {{}};
        assertTrue(solution.isBipartite(graph));
    }

    @Test
    @DisplayName("Tree structure is bipartite")
    void testTreeStructure() {
        int[][] graph = {{1}, {0, 2}, {1}};
        assertTrue(solution.isBipartite(graph));
    }

    @Test
    @DisplayName("4-cycle (even) is bipartite")
    void testEvenCycle() {
        int[][] graph = {{1, 3}, {0, 2}, {1, 3}, {0, 2}};
        assertTrue(solution.isBipartite(graph));
    }

    @Test
    @DisplayName("3-cycle (odd/triangle) is NOT bipartite")
    void testOddCycle() {
        int[][] graph = {{1, 2}, {0, 2}, {0, 1}};
        assertFalse(solution.isBipartite(graph));
    }

    @Test
    @DisplayName("Self-loop is NOT bipartite")
    void testSelfLoop() {
        int[][] graph = {{0}};
        assertFalse(solution.isBipartite(graph));
    }

    @Test
    @DisplayName("Disconnected bipartite components")
    void testDisconnectedBipartite() {
        int[][] graph = {{1}, {0}, {3}, {2}};
        assertTrue(solution.isBipartite(graph));
    }

    @Test
    @DisplayName("Null graph throws NullPointerException")
    void testNullGraph() {
        int[][] graph = null;
        assertThrows(NullPointerException.class, () -> solution.isBipartite(graph));
    }

    @Test
    @DisplayName("Null adjacency list throws NullPointerException")
    void testNullAdjacencyList() {
        int[][] graph = {{1}, null, {0}};
        assertThrows(NullPointerException.class, () -> solution.isBipartite(graph));
    }

    @Test
    @DisplayName("Invalid node index throws IndexOutOfBoundsException")
    void testInvalidNodeIndex() {
        int[][] graph = {{5}, {0}};
        assertThrows(IndexOutOfBoundsException.class, () -> solution.isBipartite(graph));
    }

    // ============================================================
    // Helper Methods
    // ============================================================

    private boolean parseExpected(String expectedStr) {
        if ("Exception".equalsIgnoreCase(expectedStr)) {
            return false; // placeholder
        }
        return Boolean.parseBoolean(expectedStr);
    }

    private boolean isExceptionCase(String expectedStr) {
        return "Exception".equalsIgnoreCase(expectedStr);
    }

    private int[][] parseGraph(String graphSize, String cyclePresence, String graphStructure, String nullInput) {
        if ("Null".equalsIgnoreCase(nullInput)) {
            return null;
        }
        if ("NullEdgeList".equalsIgnoreCase(nullInput)) {
            return new int[][]{{1}, null, {0}};
        }

        switch (cyclePresence) {
            case "NoCycles":
                return new int[][]{{1}, {0, 2}, {1}};
            case "EvenCycle":
                return new int[][]{{1, 3}, {0, 2}, {1, 3}, {0, 2}};
            case "OddCycle":
                return new int[][]{{1, 2}, {0, 2}, {0, 1}};
            default:
                return new int[][]{};
        }
    }
}