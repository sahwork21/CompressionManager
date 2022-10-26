package edu.ncsu.csc316.compression.manager;

import java.io.FileNotFoundException;

import edu.ncsu.csc316.compression.dsa.DSAFactory;
import edu.ncsu.csc316.compression.io.InputReader;
import edu.ncsu.csc316.dsa.list.List;
import edu.ncsu.csc316.dsa.map.Map;
import edu.ncsu.csc316.dsa.map.Map.Entry;
import edu.ncsu.csc316.dsa.sorter.Sorter;

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
        unprocessedMap = DSAFactory.getMap(null);
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
        //Go over each entry and each element in the List
    	int order = 1;
    	int lineNum = 1;
    	Map<String, Integer> uniqueWords = DSAFactory.getMap(null);
    	Map<Integer, List<String>> compressedMap = DSAFactory.getMap(null);
    	
    	for(Entry<Integer, List<String>> e : unprocessedMap.entrySet()) {
    		List<String> compressedLine = DSAFactory.getIndexedList();
    		List<String> currentLine = e.getValue();
    		for(int i = 0; i < currentLine.size(); i++) {
    			//If the word is unique increase the order count and add the word as a new entry
    			
    			if(uniqueWords.get(currentLine.get(i)) == null) {
    				uniqueWords.put(currentLine.get(i), order);
    				order++;
        			compressedLine.addLast(currentLine.get(i));
    			}
    			else {
    				String num = "" + uniqueWords.get(currentLine.get(i));
    				
    				compressedLine.addLast(num);
    			}
    				
    		}
    		
    		//Now add the compressed Line to the map
    		compressedMap.put(lineNum, compressedLine);
    		lineNum++;
    	}
    	
    	Sorter<Entry<Integer, List<String>>> sorter = DSAFactory.getComparisonSorter(null);
    	//Pull out all the entries and put them in an array
    	Entry[] entries = new Entry<Integer, List<String>>[compressedMap.size()];
    	
        
    }

    public Map<Integer, List<String>> getDecompressed() {
        // TODO Complete this method
    }
}