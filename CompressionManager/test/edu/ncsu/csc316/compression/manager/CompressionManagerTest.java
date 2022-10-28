package edu.ncsu.csc316.compression.manager;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;

import edu.ncsu.csc316.dsa.list.List;
import edu.ncsu.csc316.dsa.map.Map;

/**
 * Tests the methods for the CompressionManager
 * @author Sean Hinton (sahinto2)
 *
 */
class CompressionManagerTest {

	/**
	 * Tests the getCompressedMethod
	 */
	@Test
	public void testGetCompressed() {
		CompressionManager cm = null;
		try {
			cm = new CompressionManager("input/decompressed.txt");
		} catch (FileNotFoundException e) {
			fail("No path found");
			e.printStackTrace();
		}
		
		Map<Integer, List<String>> map = cm.getCompressed();
		assertEquals(5, map.size());
		assertEquals("One", map.get(1).get(0));
		assertEquals("fish", map.get(1).get(1));
		assertEquals("Two", map.get(1).get(2));
		assertEquals("2", map.get(1).get(3));
	}

}
