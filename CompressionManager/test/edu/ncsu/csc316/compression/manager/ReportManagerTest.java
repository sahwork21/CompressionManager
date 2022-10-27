package edu.ncsu.csc316.compression.manager;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;

/**
 * Tests the functionality of ReportManager. Makes sure that it can compress and decompress a file
 * @author Sean Hinton (sahinto2)
 *
 */
class ReportManagerTest {
	/**Constant for the indents when returning the compressed or decompressed Maps*/
    private static final String INDENT = "   ";
			
	/**
	 * Tests the compress method which uses an underlying compressionManager
	 */
	@Test
	void testCompress() {
		ReportManager rm = null;
		try {
			rm = new ReportManager("input/decompressed.txt");
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
			fail("Could not read");
		}
		
		assertEquals("Compressed Output {\n"
				+ INDENT + "Line 1: One fish Two 2 Red 2 Blue 2\n"
				+ INDENT + "Line 2: Black 2 5 2 Old 2 New 2\n"
				+ INDENT + "Line 3: This one has a little car\n"
				+ INDENT + "Line 4: 9 10 11 12 13 star\n"
				+ INDENT + "Line 5: Say What 12 lot of 2 there are\n"
				+ "}", rm.compress());
		
		
		//Make sure no changes are made for a unique file
		try {
			rm = new ReportManager("input/unique.txt");
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
			fail("Could not read");
		}
		assertEquals("Compressed Output {\n"
				+ INDENT + "Line 1: This has only unique words\n"
				+ INDENT + "Line 2: No repeats here\n"
				+ INDENT + "Line 3: so no changes will be made\n"
				+ "}", rm.compress());
	}

}
