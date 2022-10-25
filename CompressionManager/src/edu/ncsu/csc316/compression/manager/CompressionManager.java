package edu.ncsu.csc316.compression.manager;

import java.io.FileNotFoundException;

import edu.ncsu.csc316.compression.dsa.DSAFactory;
import edu.ncsu.csc316.compression.io.InputReader;
import edu.ncsu.csc316.dsa.list.List;
import edu.ncsu.csc316.dsa.map.Map;

/**
 * CompressionManager is the part of the model that takes a Map from the InputReader and processes it.
 * The CompressionManager will use the DSAFactory to test an assortment of Map, List, and sorting implementations.
 * CompressionManager is used by ReportManager to be the encapsulated compressor and decompressor.
 * @author Sean Hinton (sahinto2)
 *
 */
public class CompressionManager {

	/**The underlying map of raw text straight from a file*/
	private Map<Integer, List<String>> unprocessedMap;
	/**
	 * Constructor for the CompressionManager.
	 * Takes in a file path and creates a file of the unprocessed text.
	 * It also sets the different Map, List, and Sorting implementations for testing.
	 * @param pathToInputFile the file path to the File to be read
	 * @throws FileNotFoundException occurs if the path does not exist
	 */
    public CompressionManager(String pathToInputFile) throws FileNotFoundException {
        DSAFactory.setMapType(null);
        DSAFactory.setListType(null);
        DSAFactory.setComparisonSorterType(null);
        DSAFactory.setNonComparisonSorterType(null);
        unprocessedMap = InputReader.readFile(pathToInputFile);
    }

    /**
     * Compresses the Map of unprocessed text by replacing repeats of words with an Integer.
     * If a word is repeated it is replaced with the integer of the number where the first occurence was
     * in the original text.
     * Maps are returned with Integer as the line number and List of Strings is the line.
     * @return a Map of compressed text that
     */
    public Map<Integer, List<String>> getCompressed() {
        unprocessedMap = DSAFactory.getMap(null);
        
    }

    public Map<Integer, List<String>> getDecompressed() {
        // TODO Complete this method
    }
}