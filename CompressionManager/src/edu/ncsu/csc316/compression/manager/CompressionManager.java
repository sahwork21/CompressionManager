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
 * Warnings are suppressed to allow the Sorter to sort an Array of Entry objects
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
        DSAFactory.setMapType(DataStructure.SKIPLIST);
        DSAFactory.setListType(DataStructure.ARRAYBASEDLIST);
        DSAFactory.setComparisonSorterType(Algorithm.QUICKSORT);
        DSAFactory.setNonComparisonSorterType(Algorithm.RADIX_SORT);
       
        unprocessedMap = InputReader.readFile(pathToInputFile);
        
        
    }
	
    /**
     * Compresses the Map of unprocessed text by replacing repeats of words with an Integer.
     * If a word is repeated it is replaced with the integer of the number where the first occurrence was
     * in the original text.
     * Maps are returned with Integer as the line number and List of Strings is the line.
     * @return a Map of compressed text that has number replacements for repeat words.
     */
    public Map<Integer, List<String>> getCompressed() {
    	//Edge case needed when nothing was read
    	//Need to make sure the lists contain nothing
    	if(unprocessedMap.size() == 0) {
    		return null;
    	}
    	
    	
    	//We need to sort the unprocessedMap first
    	Map<Integer, List<String>> unprocessedSortedMap = sort(unprocessedMap);
        //Go over each entry and each element in the List
    	int order = 1;
    	int lineNum = 1;
    	//Algorithm will not use another Map.
    	//Instead it will just set map values in the unprocessedSortedMap
    	Map<String, Integer> uniqueWords = DSAFactory.getMap(null);
    	//Entry<Integer, List<String>>[] entries = new Entry[unprocessedSortedMap.size()];
    	Map<Integer, List<String>> retMap = DSAFactory.getMap(null);
    	for(Entry<Integer, List<String>> e : unprocessedSortedMap.entrySet()) {
    		List<String> originalLine = DSAFactory.getIndexedList();
    		List<String> currentLine = e.getValue();
    		
    		for(int i = 0; i < currentLine.size(); i++) {
    			//If the word is unique increase the order count and add the word as a new entry
    			String currentWord = currentLine.get(i);
    			Integer mapVal = uniqueWords.get(currentWord);
    			if(mapVal == null) {
    				uniqueWords.put(currentWord, order);
    				order++;
        			
    			}
    			else {
    				//Otherwise reset the value on the line
    				
    				currentLine.set(i, new StringBuilder("" + mapVal).toString());
    			
    			}
    			//Need to make a hard copy of the original Line so we can compress again
    			originalLine.addLast(currentWord);
    				
    		}
    		
    		//Now add the compressed Line to the array to later get put into a Map
    		retMap.put(lineNum, currentLine);
    		//Replace the Map entry value 
    		unprocessedMap.put(lineNum, originalLine);
    		lineNum++;
    	}
    	
    	
    	
    	
    	
//    	sorter.sort(entries);
    	//Now add back to front to help the heuristic maps that add at the front
    	
        
        
        //Change
        return retMap;
    }
    
    /**
     * Decompresses the unprocessedMap by replacing numbers with their associated unique words.
     * Unique Strings are placed into a Map of Integers as the keys and Strings as the values.
     * Whenever an Integer is encountered the associated String value will replace the Integer in 
     * the decompressedMap.
     * @return a Map of decompressed text with no number replacements
     */
    public Map<Integer, List<String>> getDecompressed() {
    	//Edge case needed when nothing was read
    	if(unprocessedMap.size() == 0) {
    		return null;
    	}
    	
    	
    	//We need to sort the unprocessedMap first
    	Map<Integer, List<String>> unprocessedSortedMap = sort(unprocessedMap);
        //Go over each entry in the unprocessedMap
    	int order = 1;
    	int lineNum = 1;
    	//Keys are integers as a String
    	Map<String, String> uniqueWords = DSAFactory.getMap(null);
    	Map<Integer, List<String>> retMap = DSAFactory.getMap(null);
    	//These entries are the processed lines
    	//Entry<Integer, List<String>>[] entries = new Entry[unprocessedSortedMap.size()];
    	for(Entry<Integer, List<String>> e : unprocessedSortedMap.entrySet()) {
    		
    		List<String> originalLine = DSAFactory.getIndexedList();
    		List<String> currentLine = e.getValue();
    		
    		for(int i = 0; i < currentLine.size(); i++) {
    			//If the String is an Integer then replace it with its associated String
    			String currentWord = currentLine.get(i);
    			String mapVal = uniqueWords.get(currentWord);
    			if(mapVal == null) {
    				uniqueWords.put(new StringBuilder("" + order).toString(), currentWord);
    				order++;
    				
    			}
    			else {
    				//Otherwise a number has been found so just set the String with the associated value
    				currentLine.set(i, mapVal);
    			}
    			originalLine.addLast(currentWord);
    		}
    		
    		
    		//Replace the Map entry value 
    		retMap.put(lineNum, currentLine);
    		unprocessedMap.put(lineNum, originalLine);
    		
    		lineNum++;
    	}
    	
    	
    	
    	//Now add the last element first to help the heuristic maps that add at the front
    	
//        for(int j = entries.length - 1; j >= 0; j--) {
//        	retMap.put(j + 1, entries[j].getValue());
//        }
        
        //Reset the unprocessedMap at the end so we can process again
        
        return retMap;
    }
    
    /**
     * Private helper that does sorting
     * @param map the map to be sorted
     * @return the map now in sorted order even if it is supposed to be unordered
     */
    @SuppressWarnings("unchecked")
	private Map<Integer, List<String>> sort(Map<Integer, List<String>> map) {
    	//We may need to sort the Map since it could come in reverse order if it doesn't self sort
        Entry<Integer, List<String>>[] entries = new Entry[map.size()];
        Sorter<Entry<Integer, List<String>>> sorter = DSAFactory.getComparisonSorter(null);
        int i = 0;
        for(Entry<Integer, List<String>> e : map.entrySet()) {
    		entries[i] = e;
    		i++;
    	}
        
        sorter.sort(entries);
        Map<Integer, List<String>> retMap = DSAFactory.getMap(null);
        for(int j = i - 1; j >= 0; j--) {
        	retMap.put(j, entries[j].getValue());
        }
        return retMap;
    }
    
    
//    @SuppressWarnings("unchecked")
//	private Map<Integer, List<String>> nonCompSort(Map<Integer, List<String>> map){
//    	Identifiable[] entries = new Identifiable[map.size()];
//    	Sorter<Identifiable> sorter = DSAFactory.getNonComparisonSorter();
//    	
//    	int i = 0;
//        for(Entry<Integer, List<String>> e : map.entrySet()) {
//    		entries[i] = new EntryIdentifier<Integer, List<String>>(e);
//    		i++;
//    		
//    	}
//        
//        sorter.sort(entries);
//        Map<Integer, List<String>> retMap = DSAFactory.getMap(null);
//        for(int j = i - 1; j >= 0; j--) {
//        	retMap.put(j, ((EntryIdentifier<Integer, List<String>>) entries[j]).getEntry().getValue());
//        	
//        }
//        return retMap;
//    	
//    }
//    
//    private class EntryIdentifier<K, V> implements Identifiable {
//
//    	/**The underlying private entry that this class wraps*/
//    	private Entry<K, V> entry;
//    	
//    	public EntryIdentifier(Entry<K, V> entry) {
//    		this.entry = entry;
//    	}
//    	
//    	public Entry<K, V> getEntry() {
//			return entry;
//		}
//    	
//    	/**
//    	 * Should be fine since this is only ever called when doing counting sort
//    	 */
//		@Override
//		public int getId() {
//			// TODO Auto-generated method stub
//			return (int)entry.getKey();
//		}
//		
//		
//		
//		
//    	
//    }
   
}