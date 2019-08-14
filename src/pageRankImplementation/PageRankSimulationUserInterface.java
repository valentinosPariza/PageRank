package pageRankImplementation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.InputMismatchException;
import java.util.Scanner;


/**
 * This class represents the interface of the program with the user.Objects of this class
 * can be used to offer a variety of functions that are associated with calculating the 
 * page ranks of WebPages in different situations and with different parameters that 
 * affect differently the calculations of the program.
 * The exercise has requested three basic methods.These methods are all implemented inside
 * of this class but also this class gives a variety of more combinations in order to 
 * experiment more precise and accurately the correctness and the efficiency of the 
 * algorithm(Algorithm which calculates the page rank).For exapmle if we want to test
 * the method A (The classic page rank) the user can set the sibling factor to 1 and 
 * also to set the lower bound to 0. Testing method B (The application of a lower bound 
 * factor) , can be maintained changing only the lower bounfd factor to the desired number
 * and letting the sibling factor to 1. The test of the method C (The application of a 
 * sibling factor ) can be made by changing the sibling factor to the desired number and
 * setting the lower bound number to 0. Although more powerful experiments can be maintained
 * by combinations of these methods and also the correctness of this program can be examined
 * by these combinations. 
 * 
 * @author Valentinos Pariza
 *
 */
public class PageRankSimulationUserInterface 
{
	
	public static final Scanner keyboard=new Scanner(System.in);	// an object of type Scanner which will be used for input from keyboard
	
	private WebPageGraph graph;							// an object of type WebPageGraph which will be used for page rank calculations experiments
	
	public static final int NUMBER_OF_ITERATIONS=15;	// The number of iterations for calculating the PageRanks
	
	
	/**
	 * 
	 * This method creates an object of type PageRankSimulationUserInterface by initializing the attribute object of type WebPageGraph
	 * of this object by creating a new objevt using the data from the file which name is specified by the argument fileName 
	 * (loadFromBinaryFileOrBuildFromText=false) or by loading an existing object of type WebPageGraph by a binary file specified by
	 * the argument fileName(loadFromBinaryFileOrBuildFromText=true).
	 * 
	 * @param fileName a String which represents the name of a file which will be used for reading data or o an object of type WebPageGraph
	 * @param loadFromBinaryFileOrBuildFromText False if the fileName argument will specify a fileName for reading data and creating a new 
	 * object of type WebPageGraph and true if the fileName argument will be specifying a file from which the an object of type WebPageGraph 
	 * will be loaded.
	 *
	 * @throws FileNotFoundException This Exception is thrown when problems , during opening the file or because of not finding it occur.
	 * @throws IOException This Exception is thrown when problems during reading of the object or data occur
	 * @throws ClassNotFoundException This Exception is thrown if the object that is trying to be read isn't implementing Serializable interface 
	 * -> Which means not to be Serializable object
	 */
	public PageRankSimulationUserInterface(String fileName,boolean loadFromBinaryFileOrBuildFromText) throws FileNotFoundException, ClassNotFoundException, IOException
	{
		if(fileName==null)
			throw new NullPointerException("Null filename given in constructor of class PageRankSimulationUserInterface ");
		
		if(loadFromBinaryFileOrBuildFromText)
		{
			this.loadWebPageGraph(fileName);
		}
		else
		{
			this.graph=new WebPageGraph();
			this.graph.createWebPageGraphFromFileData(fileName);
			
			// Randomizw the associations between the vertices of the graph
			this.graph.createAssociationsBetweenVertices();
			
		}
		
	}
	
	
	
	/**
	 * This method reads an object graph of type WebPageGraph from a file which the name of file is specified by the argument fileName.
	 * The object of type WebPageGraph that is read from the file is stored inside of the object of type PageRankSimulationUserInterface
	 * which invokes the method.
	 * 
	 * @param fileName a String which represents the name of a file (binary file )
	 * @throws FileNotFoundException This Exception is thrown if problems , during opening the file or because of not finding it occur.
	 * @throws IOException This Exception is thrown when problems during reading of the object occur.
	 */
	public void loadWebPageGraph(String fileName) throws FileNotFoundException, IOException, ClassNotFoundException
	{
		if(fileName==null)
			throw new NullPointerException("Null file name was given at method loadWebPageGraph");
		
		ObjectInputStream inputStream=new ObjectInputStream(new FileInputStream(fileName));
		
		this.graph=(WebPageGraph)inputStream.readObject();
		
		inputStream.close();
		
	}
	
	
	/**
	 * This method writes an object graph of type WebPageGraph to a file which the name of file is specified by the argument fileName.
	 * The object of type WebPageGraph that is written to the file is encapsulated inside of the object of type PageRankSimulationUserInterface
	 * which invokes the method.
	 * 
	 * @param fileName a String which represents the name of a file (binary file more specific)
	 * @throws FileNotFoundException This Exception is thrown if problems , during opening the file or because of not finding it occur.
	 * @throws IOException This Exception is thrown when problems during writing of the object occur
	 */
	public void writeWebPageGraph(String fileName) throws FileNotFoundException, IOException
	{
		if(fileName==null)
			throw new NullPointerException("Null file name was given at method writeWebPageGraph");
		
		ObjectOutputStream outpuStream=new ObjectOutputStream(new FileOutputStream(fileName));
		
		outpuStream.writeObject(this.graph);
		
		outpuStream.close();
	}
	
	
	/**
	 * This method prints the menu of the interface of the simulation of page rank program.
	 * 
	 * @param void
	 * @return void
	 * 
	 */
	public static void printMenu()
	{
		System.out.println("--------------------------------------- Page Rank Simulation Menu --------------------------------------- \n\n");
		System.out.println("1) Set a focused Web Page to calculate its progress : "); 
		System.out.println("2) Print progress of a web page at the calculation of its pageRank."); 
		System.out.println("3) Print the page ranks' calculations of the factors of the focused Web Page."); 
		System.out.println("4) Print both the progress of the focused Web Page(on the calculation of its page rank) and the Page Ranks calculations its factors.");
		System.out.println("5) Load Other web-page graph."); 
		System.out.println("6) Write the the current graph to a file.");
		System.out.println("7) Specify a new sibling factor .");
		System.out.println("8) Specify a new lower bound for spam Web pages.");
		System.out.println("9) Print sibling factor and Lower bound for spams web pages ."); 
		System.out.println("10) Exit\n");
		
	}
	
	public boolean printStatisticsBasedOnFocusedWebPage(String urlName,int option)
	{
		if( option<2 || option>4)
			return false;
		
		if(this.graph.getWebPageWithSpecificNameURL(urlName)==null)
		{
			System.out.println("The focused URL name doesn't exist in the graph.You have to change it into a valid URL before i can do this function.\n");
			return false;
		}
		
		
		String inputLine=null;		
		
		boolean hasUsedFileForStream1=false;
		boolean hasUsedFileForStream2=false;
		PrintStream outputStream1=null;
		PrintStream outputStream2=null;
		
		if(option==2 || option==4)
		{
			
			System.out.println("Do you want to use the default screen output(System.out stream) for printing the results of the focused web page, or do you want to specify a stream to a file ?");
			System.out.println("Enter \"yes\" for choosing the default output stream  or \"no\" to specify a different.");
			inputLine=keyboard.next();
			keyboard.nextLine();
			
			while(!inputLine.equalsIgnoreCase("yes") && ! inputLine.equalsIgnoreCase("no"))
			{
				System.out.println("I can't understant your answer. Please enter only yes or no .Try again.\n");
				inputLine=keyboard.next();
				keyboard.nextLine();
			}
			
			
			if(inputLine.equalsIgnoreCase("no"))
			{
				
				hasUsedFileForStream1=true;
				
				System.out.println("Give the name of the file you want to use for the output stream :");
				inputLine=keyboard.nextLine().trim();
				
				try
				{
					outputStream1=new PrintStream(new FileOutputStream(inputLine));
				} 
				catch (FileNotFoundException e) 
				{	
					System.out.println("The file can't be opened or doesn't exist.Try again later.\n");
					return false;
				}
				
			}
			else outputStream1=System.out;
			
		}
		
		
		if(option==3 || option==4)
		{
			
			System.out.println("Do you want to use the default screen output(System.out stream) for printing the results of the factors of the focused web page, or do you want to specify a stream to a file ?");
			System.out.println("Enter \"yes\" for choosing the default output stream  or \"no\" to specify a different.");
			inputLine=keyboard.next();
			keyboard.nextLine();
			
			while(!inputLine.equalsIgnoreCase("yes") && ! inputLine.equalsIgnoreCase("no"))
			{
				System.out.println("I can't understant your answer. Please enter only yes or no .Try again.\n");
				inputLine=keyboard.next();
				keyboard.nextLine();
			}
			
			
			if(inputLine.equalsIgnoreCase("no"))
			{
				
				hasUsedFileForStream2=true;
				
				System.out.println("Give the name of the file you want to use for the output stream :");
				inputLine=keyboard.nextLine().trim();
				
				try 
				{
					outputStream2=new PrintStream(new FileOutputStream(inputLine));
				} 
				catch (FileNotFoundException e) 
				{	
					System.out.println("The file can't be opened or doesn't exist.Try again later.\n");
					return false;
				}
				
			}
			else outputStream2=System.out;
			
		}
		
		
		
		this.graph.runPageRankCalculationProcessSimulation(outputStream1, outputStream2, NUMBER_OF_ITERATIONS, urlName);
		
		if(hasUsedFileForStream1)
		 {
			System.out.println("The output of the progress of the web page with URL "+urlName+" ,has been written to the specified file .");
			outputStream1.close();
		 }
		
		if(hasUsedFileForStream2)
		 {
			System.out.println("The output of the page ranks' results, of the factors that affect the web page with URL "+urlName+" has been written to the specified file :");
			outputStream2.close();
		 }
		
		return true;
		
	}
	
	/**
	 * This method is a method which builds a conversation-interface in which interface the computer guides the user to change 
	 * the sibling factor of the graph.
	 * 
	 * @param void
	 * @retutn void
	 */
	public void setSiblingFactor()
	{
		System.out.println("Give a new floating point number for sibling factor :");
		
		
		try
		{
			if(!this.graph.setSiblingFactor(keyboard.nextDouble()))
			{
				System.out.println("Th sibling factor should be between 0 and 1 inclusive");
			}
			else System.out.println("The sibling factor has been updated .");
			
		}
		catch(InputMismatchException io)
		{
			System.out.println("Not a correct floating point number");
		}
		keyboard.nextLine();
	}
	
	
	/**
	 * This method is a method which builds a conversation-interface in which interface ,the computer guides the user to change 
	 * the lowest bound for distributing all the spam web pages(spam webpages are the webpages that have page rank less than
	 * the value that is represented by the lower bound number) from the graph.
	 * 
	 * @param void
	 * @retutn void
	 */
	public void setLowerBoundForSpams()
	{
		
		System.out.println("Give a new floating point number for lower bound , for distributing which web pages are spams  :");
		
		try
		{
						
			if(!this.graph.setLowerBound(keyboard.nextDouble()))
			{
				System.out.println("The lower bound should be greater or equal than zero (>=0)");
			}
			else System.out.println("The lower bound has been updated .");
			
		}
		catch(InputMismatchException io)
		{
			System.out.println("Not a correct floating point number");
		}
		keyboard.nextLine();
	}
		
	
	
	/**
	 * 
	 * This method runs the interface of the simulation of the page rank program and gives a variety of choices to
	 * the user ,to choose.In these choices are included the loading or the creation of a WebPageGraph object ,
	 * the running of the simulation of the calculation of pageRank for a focused-specific vertex which represents a
	 * WebPage and can be chosen before the calculation of the pageRank of the different WebPages , the calculation
	 * of all the progress of the page ranks of the WebPages.
	 * 
	 * @param void
	 * @return void
	 * 
	 */
	public static void runUserInterfaceForPageRankSimulation()
	{
		
		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< Page Rank Simulation Program >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n");
		
		
		
		int choice=0;					// The choice of the user from the menu
		
		String focusedUrlName=null;		// The name of the URL that will be used to find the WebPage .(focused WebPage for calculating its progress)
		
		PageRankSimulationUserInterface simulationPageRank=null;
		
		System.out.println("Do you want to load an existing web page graph or to create a new one?");
		System.out.println("Enter \"new\" for creating a new graph from a data text file or \"load\" for loading an existing web page graph from a binary file");
		String inputLine=keyboard.next();
		keyboard.nextLine();
		
		while(!inputLine.equalsIgnoreCase("new") && !inputLine.equalsIgnoreCase("load"))
		{
			System.out.println("\nI don't understant your answer.Please try again.");
			inputLine=keyboard.next();
			keyboard.nextLine();
		}
		
		{
			boolean loadExistingGraphFromBinaryFile=inputLine.equalsIgnoreCase("load");
			
			System.out.println("Give the name of the file :");
			inputLine=keyboard.nextLine().trim();
			
			// Creates an object of this class which contains the graph created or loaded from a file
			
			try {
				simulationPageRank=new PageRankSimulationUserInterface(inputLine,loadExistingGraphFromBinaryFile);
				System.out.println("The graph was succesfully placed in the program.\n");
			} 
			catch (ClassNotFoundException classException) {
				System.out.println("Cannot recognise the object graph in the file .\n");
				return ;
			}
			catch ( IOException e)
			{
				System.out.println("The file can't be opened or couldn't be found.\n");
				return ;
			}
			
			
		}
		
		
		
		do
		{
			
			System.out.println("\n");
			printMenu();
			
			System.out.print("\nGive you choice : ");
			
			try
			{
				// Read the choice of the user
				choice=keyboard.nextInt();
				
				if(choice<1 || choice>10)
					{
						System.out.println("Undefined option . Try again .\n");
						choice=0;
					}
				
			}
			catch(InputMismatchException inputException)
			{
				System.out.println("The choice you have gave isn't a correct representation of a number.Try again .\n");
				choice=0;
			}
			
			keyboard.nextLine();
			
			
			switch (choice)
			{
				case 1:		// The choice of setting the focused URL name
					
						System.out.println("Give the name of the URL that corresponds to the web page , to be focused.");
						
						focusedUrlName=keyboard.nextLine();	
						
						System.out.println("You have set the new focused web page URL to be -> "+focusedUrlName);
					
					break;
					
					
				case 2:
						// The choice of running the simulation of calculating the page ranks of the Web Pages by printing
						// only the progress of the focused URL
						
						simulationPageRank.printStatisticsBasedOnFocusedWebPage(focusedUrlName, 2);
					
					break;
					
				case 3 :
					// The choice of running the simulation of calculating the page ranks of the Web Pages by printing
					// only the calculations of the web Pages that affect the focused WebPage that correpsonds to the focused URL
					
						simulationPageRank.printStatisticsBasedOnFocusedWebPage(focusedUrlName, 3);
					break;
					
					
				case 4 :
					// The choice of running the simulation of calculating the page ranks of the Web Pages by printing
					// the progress of the focused URL and the calculations of the web Pages that affect the focused
					// WebPage that correpsonds to the focused URL
					
						simulationPageRank.printStatisticsBasedOnFocusedWebPage(focusedUrlName, 4);
					
					break;
					
				case 5 :
							// Load a new object of type WebPage graph in the program
					
							System.out.println("Give the name of the file , from which the new web page graph will be loaded .\n");
							inputLine=keyboard.nextLine();
						
							try 
							{
									simulationPageRank.loadWebPageGraph(inputLine);
									
									System.out.println("The graph hase been read succesfully.\n\n");
									
							} 
							catch (FileNotFoundException e) 
							{
									System.out.println("File couldn't be opened.Try again later.\n");
					
							} 
							catch (ClassNotFoundException e) 
							{
									System.out.println("The object trying to be read isn't Serializable .Try again later.\n");
							} 
							catch (IOException e)
							{
									System.out.println("Proble trying to read the graph object from the file.\n");
							}
					
						break;
					
				case 6: 
							// Write the encapsulated object of type WebPageGraph in a file
							
							System.out.println("Give the name of the file , at which the graph will be written .\n");
							inputLine=keyboard.nextLine();
					
							try 
							{
								simulationPageRank.writeWebPageGraph(inputLine);
								
								System.out.println("The graph has been written succesfully.\n\n");
								
							} 
							catch (FileNotFoundException e) 
							{
								System.out.println("File couldn't be opened.Try again later.\n");
				
							} 
							
							catch (IOException e)
							{
								System.out.println("Proble trying to write the graph object to the file.\n");
							}
					
					break;
					
				case 7 :
						// Set a new Sibling factor	
						
					simulationPageRank.setSiblingFactor();
							
					break;
					
				case 8 :
					// Set a new lower bound for distributing the spam web pages
					
					simulationPageRank.setLowerBoundForSpams();
					
					break;
					
				case 9 : 
						// Prints the current sibling factor and the the lowerr bouynd for spams
					
					simulationPageRank.printSiblingFactorAndLowerBoundForSpams();
					
					break;
					
				default :
						
				   break;
 			
			}
			
			
			
		}while(choice!=10);
		
		System.out.println("Exiting the Page Rank Simulation.\n");
		
		
	}
	
	
	/**
	 * This method prints the Lower bound and the sibling factor of the current graph loaded
	 * 
	 * @param void
	 * @return void
	 */
	public void printSiblingFactorAndLowerBoundForSpams()
	{
		
		System.out.println("\n>> Lower Bound for spams is : "+this.graph.getLoweBound());
		System.out.println(">> Sibling factor is : "+this.graph.getSiblingFactor());
	}
	
	
	/**
	 * This methd is the main method of the program from which the control flow of the program is moved to the method 
	 * {@link PageRankSimulationUserInterface#runUserInterfaceForPageRankSimulation()}
	 * 
	 * @param args arguments taken from the command line
	 * @return void
	 */
	public static void main(String[] args)
	{
		runUserInterfaceForPageRankSimulation();
		
		System.out.println("--------------------------------------------- Exiting the program"
				+ " - Simulation of Page Ranks ---------------------------------------------");
				
	}
	
	
}
