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
public class CompressionManagerTest {

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
		
		//Check that a lot of stuff was compressed correctly
		assertEquals("This", map.get(3).get(0));
		assertEquals("9", map.get(4).get(0));
		assertEquals("one", map.get(3).get(1));
		assertEquals("10", map.get(4).get(1));
		assertEquals("has", map.get(3).get(2));
		assertEquals("11", map.get(4).get(2));
		assertEquals("a", map.get(3).get(3));
		assertEquals("12", map.get(4).get(3));
		assertEquals("little", map.get(3).get(4));
		assertEquals("13", map.get(4).get(4));
		assertEquals("car", map.get(3).get(5));
		assertEquals("star", map.get(4).get(5));
		
		try {
			cm = new CompressionManager("input/sample.txt");
		} catch (FileNotFoundException e) {
			fail("Unable to read");
			e.printStackTrace();
		}
		
		map = cm.getCompressed();
		//Now assert everything is right
		
		assertEquals("the", map.get(1).get(0));
		assertEquals("dog", map.get(1).get(1));
		assertEquals("and", map.get(1).get(2));
		assertEquals("1", map.get(1).get(3));
		assertEquals("cat", map.get(1).get(4));
		assertEquals("3", map.get(1).get(5));
		assertEquals("1", map.get(1).get(6));
		assertEquals("fox", map.get(1).get(7));
		assertEquals("3", map.get(1).get(8));
		assertEquals("1", map.get(1).get(9));
		assertEquals("snake", map.get(1).get(10));
		
		assertEquals("3", map.get(2).get(0));
		assertEquals("1", map.get(2).get(1));
		assertEquals("fish", map.get(2).get(2));
		assertEquals("3", map.get(2).get(3));
		assertEquals("1", map.get(2).get(4));
		assertEquals("horse", map.get(2).get(5));
		assertEquals("3", map.get(2).get(6));
		assertEquals("1", map.get(2).get(7));
		assertEquals("2", map.get(2).get(8));
		assertEquals("3", map.get(2).get(9));
		assertEquals("1", map.get(2).get(10));
		assertEquals("4", map.get(2).get(11));
	}
	
	
	/**
	 * Tests the getDecompressedMethod
	 */
	@Test
	public void testGetDecompress() {
		CompressionManager cm = null;
		try {
			cm = new CompressionManager("input/compressed.txt");
		} catch (FileNotFoundException e) {
			fail("No path found");
			e.printStackTrace();
		}
		
		Map<Integer, List<String>> map = cm.getDecompressed();
		
		assertEquals(5, map.size());
		assertEquals("One", map.get(1).get(0));
		assertEquals("fish", map.get(1).get(1));
		assertEquals("Two", map.get(1).get(2));
		assertEquals("fish", map.get(1).get(3));
		
		//Check that a lot of stuff was decompressed correctly
		assertEquals("This", map.get(3).get(0));
		assertEquals("This", map.get(4).get(0));
		assertEquals("one", map.get(3).get(1));
		assertEquals("one", map.get(4).get(1));
		assertEquals("has", map.get(3).get(2));
		assertEquals("has", map.get(4).get(2));
		assertEquals("a", map.get(3).get(3));
		assertEquals("a", map.get(4).get(3));
		assertEquals("little", map.get(3).get(4));
		assertEquals("little", map.get(4).get(4));
		assertEquals("car", map.get(3).get(5));
		assertEquals("star", map.get(4).get(5));
	}

}
