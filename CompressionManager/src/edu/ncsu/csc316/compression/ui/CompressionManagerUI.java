package edu.ncsu.csc316.compression.ui;

import java.io.FileNotFoundException;
import java.util.Scanner;

import edu.ncsu.csc316.compression.manager.ReportManager;

/**
 * The UI component of the CompressionManager.
 * Just a simple command line interface that allows the user to type in a file
 * by specifying the file path. Then choose to compress or decompress.
 * Connects the model with this controller.
 * @author Sean Hinton (sahinto2)
 *
 */
public class CompressionManagerUI {
	
	/**The ReportManager that does the compressing and decompressing*/
	private static ReportManager reportManager;
	
	/**
	 * The main method for the UI that uses a Scanner to read commands.
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		//Ask for a file path from standard input
		Scanner input = new Scanner(System.in);
		System.out.println("Enter a file path: ");
		String filePath = input.nextLine();
		
		/* Begin the timer */
		long start = System.currentTimeMillis();
		
		//Create the reportManager object if able
		try {
			reportManager = new ReportManager(filePath);
		} catch (FileNotFoundException e) {
			//If the file isn't found print out the error message
			System.out.println("The provided input file is empty.");
			System.exit(1);
		}
		
		/* Run the compress method */
		//System.out.println(reportManager.compress());
		
		
		/*
		 * 
		 * THIS IS THE TESTING PART OF THE PROJECT
		 * IF YOU WANT TO SEE HOW FAST THIS GOES UNCOMMENT THIS PART
		 * 
		 * 
		 */
//		reportManager.compress();
//		/* Stop the clock*/
//		long end = System.currentTimeMillis();
//		System.out.println(end - start);
//		System.out.print(" Milliseconds elapsed from compression to decompression");
		
		
		
		//Ask to decompress or compress
		
		/*
		 * 
		 * 
		 * UNCOMMENT THE LINES BELOW TO SEE THE OUTPUT IF YOU FEEL LIKE IT
		 * 
		 * 
		 * 
		 */
		
		String command = "";
		if(reportManager != null) {
			System.out.println("Compress or Decompress the file? Type \"quit\" to quit.");
			command = input.nextLine();
		}
		//Repeat until told to quit
		while(!"quit".equals(command.toLowerCase()) && reportManager != null) {
			//compress
			if("compress".equals(command.toLowerCase())) {
				//long start = System.currentTimeMillis();
				System.out.println(reportManager.compress());
				//long end = System.currentTimeMillis();
				//System.out.println(end - start);
				//Used in part 3 for testing runt times
				
				System.out.println("\nCompress or Decompress the file? Type \"quit\" to quit.");
				
				command = input.nextLine();
			}
			
			//decompress
			else if("decompress".equals(command.toLowerCase())) {
				System.out.println(reportManager.decompress());
				System.out.println("\nCompress or Decompress the file? Type \"quit\" to quit.");
				command = input.nextLine();
			}
			
			//Tell the user what needs to be input if they didn't do it right
			else {
				System.out.println("usage: \"compress\" or \"decompress\"");
				System.out.println("\nCompress or Decompress the file? Type \"quit\" to quit.");
				command = input.nextLine();
			}
			
		}
		
		
		input.close();
		
	}
}
