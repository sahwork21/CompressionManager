package edu.ncsu.csc316.compression.manager;

import java.io.FileNotFoundException;

import edu.ncsu.csc316.compression.dsa.Algorithm;
import edu.ncsu.csc316.compression.dsa.DSAFactory;
import edu.ncsu.csc316.compression.dsa.DataStructure;
import edu.ncsu.csc316.dsa.list.List;
import edu.ncsu.csc316.dsa.map.Map;

/**
 * ReportManagers is the highest level of the model for the CompressionManager system.
 * It uses a CompressionManager field to handle processing text lines from a file. Also
 * depends upon the DSAFactory to construct different types of data structures for testing.
 * @author Sean Hinton (sahinto2)
 *
 */
public class ReportManager {
	
	/**The CompressionManager that returns the compressed or decompressed forms of the maps*/
	private CompressionManager compressionManager;
	
	/**Constant for the indents when returning the compressed or decompressed Maps*/
    private static final String INDENT = "   ";

    /**
     * Constructor for ReportManager. Takes in a file and constructs the underlying CompressionManager
     * to read in the same file. It then uses that underlying CompressionManager to do all the work.
     * Also the location where the sorting algorithm and data structures are set for this class.
     * @param pathToInputFile the path to a file being read
     * @throws FileNotFoundException if the specified file path doesn't exist
     */
    public ReportManager(String pathToInputFile) throws FileNotFoundException {
    	DSAFactory.setMapType(DataStructure.UNORDEREDLINKEDMAP);
    	DSAFactory.setListType(DataStructure.SINGLYLINKEDLIST);
    	DSAFactory.setComparisonSorterType(Algorithm.MERGESORT);
    	DSAFactory.setNonComparisonSorterType(Algorithm.COUNTING_SORT);
    	
        compressionManager = new CompressionManager(pathToInputFile);
        
    }
    /**
     * This function takes the compressed text from Map form of Integer and List of Strings to a single String.
     * Should take the form of indent, line #: Strings
     * @return a String of the compressed Map taken from Map to lines of Strings
     */
    public String compress() {
    	Map<Integer, List<String>> compressed = DSAFactory.getMap(null);
        compressed = compressionManager.getCompressed();
        //Use a StringBuffer to cut down on appends
        StringBuffer buffer = new StringBuffer();
        buffer.append("Compressed Output {\n");
        for(List<String> l : compressed.values()) {
        	//Now print out each line with an indent
        	buffer.append(INDENT);
        	for(int i = 0; i < l.size(); i++) {
        		
        		buffer.append(l.get(i));
        		buffer.append("\n");
        	}
        }
        buffer.append("}");
        
        //Now return the compressed output
        return buffer.toString();
    }
    
    /**
     * This function takes the decompressed text from Map to String. Uses the underlying
     * compressionManager to get the decompressed text and then the Strings in each List value are then
     * appended to the String.
     * @return a String of the decompressed Map taken from the Lists of Strings in the Map
     */
    public String decompress() {
    	Map<Integer, List<String>> decompressed = DSAFactory.getMap(null);
        decompressed = compressionManager.getDecompressed();
        //Use a StringBuffer to cut down on appends
        StringBuffer buffer = new StringBuffer();
        buffer.append("Compressed Output {\n");
        for(List<String> l : decompressed.values()) {
        	//Now print out each line with an indent
        	buffer.append(INDENT);
        	for(int i = 0; i < l.size(); i++) {
        		
        		buffer.append(l.get(i));
        		buffer.append("\n");
        	}
        }
        buffer.append("}");
        
        //Now return the compressed output
        return buffer.toString();
    }
}