package edu.ncsu.csc316.compression.manager;

import java.io.FileNotFoundException;
import java.util.Iterator;

import edu.ncsu.csc316.compression.dsa.Algorithm;
import edu.ncsu.csc316.compression.dsa.DSAFactory;
import edu.ncsu.csc316.compression.dsa.DataStructure;
import edu.ncsu.csc316.compression.io.InputReader;
import edu.ncsu.csc316.dsa.data.Identifiable;
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
        DSAFactory.setComparisonSorterType(Algorithm.MERGESORT);
        DSAFactory.setNonComparisonSorterType(Algorithm.COUNTING_SORT);
       
        unprocessedMap = InputReader.readFile(pathToInputFile);
        
        
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
    	//Edge case needed when nothing was read
    	//Need to make sure the lists contain nothing
    	
    	if(unprocessedMap.size() == 0) {
    		return null;
    	}
    	
    	//We need to sort the unprocessedMap first
    	EntryIdentifier<Integer, List<String>>[] unprocessedSortedMap = nonCompSort(unprocessedMap);
        //Go over each entry and each element in the List
    	if(unprocessedSortedMap[0].getEntry().getValue().size() == 0) {
    		return null;
    	}
    	
    	
    	
    	int order = 1;
    	int lineNum = 1;
    	//Algorithm will not use another Map.
    	//Instead it will just set map values in the unprocessedSortedMap
    	Map<String, Integer> uniqueWords = DSAFactory.getMap(null);
    	List<String>[] entries = new List[unprocessedSortedMap.length];
    	
    	for(EntryIdentifier<Integer, List<String>> e : unprocessedSortedMap) {
    		List<String> newLine = DSAFactory.getIndexedList();
    		List<String> currentLine = e.getEntry().getValue();
    		Iterator<String> it = currentLine.iterator();
    		while(it.hasNext()) {
    			//If the word is unique increase the order count and add the word as a new entry
    			String currentWord = it.next();
    			Integer mapVal = uniqueWords.put(currentWord, order);
    			if(mapVal == null) {
    				
    				order++;
        			newLine.addLast(currentWord);
    			}
    			else {
    				//Otherwise reset the value on the line
    				uniqueWords.put(currentWord, mapVal);
    				newLine.addLast(new StringBuilder("" + mapVal).toString());
    			
    			}
    			//Need to make a hard copy of the original Line so we can compress again
    			
    				
    		}
    		
    		//Now add the compressed Line to the array to later get put into a Map
    		entries[lineNum - 1] = newLine;
    		//Replace the Map entry value 
    		
    		lineNum++;
    	}
    	
    	
    	
    	
    	
//    	sorter.sort(entries);
    	//Now add back to front to help the heuristic maps that add at the front
    	Map<Integer, List<String>> retMap = DSAFactory.getMap(null);
        for(int j = entries.length - 1; j >= 0; j--) {
        	retMap.put(j + 1, entries[j]);
        }
        
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
    @SuppressWarnings("unchecked")
	public Map<Integer, List<String>> getDecompressed() {
    	//Edge case needed when nothing was read
    	
    	if(unprocessedMap.size() == 0 ) {
    		return null;
    	}
    	//We need to sort the unprocessedMap first
    	EntryIdentifier<Integer, List<String>>[] unprocessedSortedMap = nonCompSort(unprocessedMap);
    	if( unprocessedSortedMap[0].getEntry().getValue().size() == 0) {
    		return null;
    	}
    	
        //Go over each entry in the unprocessedMap
    	int order = 1;
    	int lineNum = 1;
    	//Keys are integers as a String
    	Map<String, String> uniqueWords = DSAFactory.getMap(null);
    	
    	//These entries are the processed lines
    	List<String>[] entries = new List[unprocessedSortedMap.length];
    	for(EntryIdentifier<Integer, List<String>> e : unprocessedSortedMap) {
    		
    		List<String> newLine = DSAFactory.getIndexedList();
    		List<String> currentLine = e.getEntry().getValue();
    		Iterator<String> it = currentLine.iterator();
    		while(it.hasNext()) {
    			//If the String is an Integer then replace it with its associated String
    			String currentWord = it.next();
    			String mapVal = uniqueWords.get(currentWord);
    			if(mapVal == null) {
    				uniqueWords.put(new StringBuilder("" + order).toString(), currentWord);
    				order++;
    				newLine.addLast(currentWord);
    			}
    			else {
    				//Otherwise a number has been found so just set the String with the associated value
    				newLine.addLast(mapVal);
    			}
    			
    		}
    		
    		//Then put the line onto the decompressedMap
    		entries[lineNum - 1] = newLine;
    		//Replace the Map entry value 
    		
    		lineNum++;
    	}
    	
    	
    	
    	//Now add the last element first to help the heuristic maps that add at the front
    	Map<Integer, List<String>> retMap = DSAFactory.getMap(null);
        for(int j = entries.length - 1; j >= 0; j--) {
        	retMap.put(j + 1, entries[j]);
        }
        
        //Reset the unprocessedMap at the end so we can process again
        
        return retMap;
    }
    
//    /**
//     * Private helper that does sorting
//     * @param map the map to be sorted
//     * @return the map now in sorted order even if it is supposed to be unordered
//     */
//    @SuppressWarnings("unchecked")
//	private Entry<Integer, List<String>>[] sort(Map<Integer, List<String>> map) {
//    	//We may need to sort the Map since it could come in reverse order if it doesn't self sort
//        Entry<Integer, List<String>>[] entries = new Entry[map.size()];
//        Sorter<Entry<Integer, List<String>>> sorter = DSAFactory.getComparisonSorter(null);
//        int i = 0;
//        for(Entry<Integer, List<String>> e : map.entrySet()) {
//    		entries[i] = e;
//    		i++;
//    	}
//        
//        sorter.sort(entries);
//        
//        return entries;
//    }
    
    @SuppressWarnings("unchecked")
 	private EntryIdentifier<Integer, List<String>>[] nonCompSort(Map<Integer, List<String>> map){
     	EntryIdentifier<Integer, List<String>>[] entries = new EntryIdentifier[map.size()];
     	Sorter<Identifiable> sorter = DSAFactory.getNonComparisonSorter();
     	
     	int i = 0;
         for(Entry<Integer, List<String>> e : map.entrySet()) {
     		entries[i] = new EntryIdentifier<Integer, List<String>>(e);
     		i++;
     		
     	}
         
         sorter.sort(entries);
         
         return entries;
     	
     }
     
     private class EntryIdentifier<K, V> implements Identifiable {

     	/**The underlying private entry that this class wraps*/
     	private Entry<K, V> entry;
     	
     	public EntryIdentifier(Entry<K, V> entry) {
     		this.entry = entry;
     	}
     	
     	public Entry<K, V> getEntry() {
 			return entry;
 		}
     	
     	/**
     	 * Should be fine since this is only ever called when doing counting sort
     	 */
 		@Override
 		public int getId() {
 			// TODO Auto-generated method stub
 			return (int)entry.getKey();
 		}
 		
 		
 		
 		
     	
     }
   
}