package edu.ncsu.csc316.compression.manager;

import java.io.FileNotFoundException;

import edu.ncsu.csc316.compression.dsa.Algorithm;
import edu.ncsu.csc316.compression.dsa.DSAFactory;
import edu.ncsu.csc316.compression.dsa.DataStructure;
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
    @SuppressWarnings("unchecked")
	public CompressionManager(String pathToInputFile) throws FileNotFoundException {
        DSAFactory.setMapType(DataStructure.SKIPLIST);
        DSAFactory.setListType(DataStructure.SINGLYLINKEDLIST);
        DSAFactory.setComparisonSorterType(Algorithm.MERGESORT);
        DSAFactory.setNonComparisonSorterType(Algorithm.COUNTING_SORT);
        unprocessedMap = DSAFactory.getMap(null);
        unprocessedMap = InputReader.readFile(pathToInputFile);
        
        //We may need to sort the Map since it could come in reverse order if it doesn't self sort
        Entry<Integer, List<String>>[] entries = new Entry[unprocessedMap.size()];
        Sorter<Entry<Integer, List<String>>> sorter = DSAFactory.getComparisonSorter(null);
        int i = 0;
        for(Entry<Integer, List<String>> e : unprocessedMap.entrySet()) {
    		entries[i] = e;
    		i++;
    	}
        
        sorter.sort(entries);
        Map<Integer, List<String>> retMap = DSAFactory.getMap(null);
        for(int j = i - 1; j >= 0; j--) {
        	retMap.put(entries[j].getKey(), entries[j].getValue());
        }
        unprocessedMap = retMap;
        
    }

    /**
     * Compresses the Map of unprocessed text by replacing repeats of words with an Integer.
     * If a word is repeated it is replaced with the integer of the number where the first occurrence was
     * in the original text.
     * Maps are returned with Integer as the line number and List of Strings is the line.
     * @return a Map of compressed text that has number replacements for repeat words.
     */
    @SuppressWarnings("unchecked")
	public Map<Integer, List<String>> getCompressed() {
        //Go over each entry and each element in the List
    	int order = 1;
    
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
    		compressedMap.put(e.getKey(), compressedLine);
    		
    	}
    	
    	Sorter<Entry<Integer, List<String>>> sorter = DSAFactory.getComparisonSorter(null);
    	//Pull out all the entries and put them in an array to sort
    	Entry<Integer, List<String>>[] entries = new Entry[compressedMap.size()];
    	int i = 0;
    	for(Entry<Integer, List<String>> e : compressedMap.entrySet()) {
    		entries[i] = e;
    		i++;
    	}
    	sorter.sort(entries);
    	//Now add back to front to help the heuristic maps that add at the front
    	Map<Integer, List<String>> retMap = DSAFactory.getMap(null);
        for(int j = i - 1; j >= 0; j--) {
        	retMap.put(entries[j].getKey(), entries[j].getValue());
        }
        return retMap;
    }
    
    /**
     * Decompresses the unprocessedMap by replacing numbers with their associated unique words.
     * Unique Strings are placed into a Map of Integers as the keys and Strings as the values.
     * Whenever an Integer is encountered the associated String value will replace the Integer in 
     * the decompressedMap.
     * @return a Map of decompressed text with no number replacements
     */
    @SuppressWarnings("unchecked")
	public Map<Integer, List<String>> getDecompressed() {
        //Go over each entry in the unprocessedMap
    	int order = 1;
    	int lineNum = 1;
    	//Keys are integers as a String
    	Map<String, String> uniqueWords = DSAFactory.getMap(null);
    	Map<Integer, List<String>> decompressedMap = DSAFactory.getMap(null);
    	
    	for(Entry<Integer, List<String>> e : unprocessedMap.entrySet()) {
    		List<String> decompressedLine = DSAFactory.getIndexedList();
    		List<String> currentLine = e.getValue();
    		for(int i = 0; i < currentLine.size(); i++) {
    			//If the String is an Integer then replace it with its associated String
    			//Must use regex to check that the currentWord is in fact a number to add
    			if(uniqueWords.get(currentLine.get(i)) == null) {
    				uniqueWords.put("" + order, currentLine.get(i));
    				order++;
    				decompressedLine.addLast(currentLine.get(i));
    			}
    			else {
    				//Otherwise a number has been found
    				decompressedLine.addLast(uniqueWords.get(currentLine.get(i)));
    			}
    		}
    		
    		//Then put the line onto the decompressedMap
    		decompressedMap.put(lineNum, decompressedLine);
    		lineNum++;
    	}
    	
    	//Then sort the Map since we don't know if Map orders itself
    	Sorter<Entry<Integer, List<String>>> sorter = DSAFactory.getComparisonSorter(null);
    	//Pull out all the entries and put them in an array to sort
    	Entry<Integer, List<String>>[] entries = new Entry[decompressedMap.size()];
    	int i = 0;
    	for(Entry<Integer, List<String>> e : decompressedMap.entrySet()) {
    		entries[i] = e;
    		i++;
    	}
    	sorter.sort(entries);
    	//Now add the last element first to help the heuristic maps that add at the front
    	Map<Integer, List<String>> retMap = DSAFactory.getMap(null);
        for(int j = i - 1; j >= 0; j--) {
        	retMap.put(entries[j].getKey(), entries[j].getValue());
        }
        return retMap;
    }
}