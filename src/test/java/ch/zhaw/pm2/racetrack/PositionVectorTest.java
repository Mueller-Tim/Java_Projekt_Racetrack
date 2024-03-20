package ch.zhaw.pm2.racetrack;

import org.junit.jupiter.api.Test;

import model.PositionVector;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PositionVectorTest {

	/**
	 * description: test if 2 logically equal PositionVectors get taken as equal
	 * equivalence class: 1
	 * initial condition: nothing
	 * type: positive test
	 * input: 2 equal PositionVectors
	 * output: should be equal
	 */
    @Test
    void testEquals() {
        PositionVector a = new PositionVector(3, 5);
        PositionVector b = new PositionVector(3, 5);
        assertEquals(a, b);
    }

	/**
	 * description: test if a map accepts 2 different but identical PositionVectors as one key
	 * equivalence class: 1
	 * initial condition: nothing
	 * type: positive test
	 * input: Map with one PositionVector
	 * output: access via identical but different PositionVector should be possible
	 */
    @Test
    void testEqualsWithHashMap() {
        Map<PositionVector, Integer> map = new HashMap<>();
        PositionVector a = new PositionVector(3, 5);
        map.put(a, 1);
        PositionVector b = new PositionVector(3, 5);
        assertTrue(map.containsKey(a), "Test with same object");
        assertTrue(map.containsKey(b), "Test with equal object");
    }
}