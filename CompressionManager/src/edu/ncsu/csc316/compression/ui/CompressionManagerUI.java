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
		
		//Create the reportManager object if able
		try {
			reportManager = new ReportManager(filePath);
		} catch (FileNotFoundException e) {
			//If the file isn't found print out the error message
			System.out.println("The provided input file is empty.");
			
		}
		
		//Ask to decompress or compress
		System.out.println("Compress or Decompress the file?");
		String command = "";
		if(reportManager != null) {
			command = input.nextLine();
		}
		//Repeat until told to quit
		while(!"quit".equals(command) && reportManager != null) {
			//compress
			if("compress".equals(command)) {
				System.out.println(reportManager.compress());
				System.out.println("\nCompress or Decompress the file?");
				command = input.nextLine();
			}
			
			//decompress
			else if("decompress".equals(command)) {
				System.out.println(reportManager.decompress());
				System.out.println("\nCompress or Decompress the file?");
				command = input.nextLine();
			}
			
			//Tell the user what needs to be input if they didn't do it right
			else {
				System.out.println("usage: \"compress\" or \"decompress\"");
				System.out.println("\nCompress or Decompress the file?");
				command = input.nextLine();
			}
			
		}
		
		
		input.close();
		
	}
}
