package pageRankImplementation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * This class represents the functionallity of a graph that holds the web Pages and their connections-links to other WebPages.
 * The objects of this class can be used to simulate the calculations of the page ranks for all the WebPages which will be 
 * placed in the graph with different parameters(Sibling factor , Lower bound for spams ,different connections between WebPages
 *  ...). 
 * 
 * @author Valentinos Pariza
 *
 */
public class WebPageGraph implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7967452411097421103L;

	// The maximum number of edges that a vertex can have pointing to other vertices
	public static final int MAX_NUMBER_OF_OUT_DIRECTED_EDGES_IN_VERTEX=30;
	
	// The minimum number of edges that a vertex can have pointing to other vertices
	public static final int MIN_NUMBER_OF_OUT_DIRECTED_EDGES_IN_VERTEX=1;
	
	// The initial page rank that a vertex as a WebPage can have
	public static final double INITIAL_PAGE_RANK=1;
	
	// The d constant which is used in the calculations of the page rank of a WebPage
	public static final double D_FACTOR=0.85;
	
	// All the vertices of the Graph
	private ArrayList<WebPageVertex> webPageVertices;
	
	// The number of the edges of the graph
	private int edges;
	
	// The lower bound which distrbutes the spam webapages
	private double lowerBound;
	
	// The sibling factor that is applied when a pageRank of a WebPage is used to calculate the PageRank of another WebPage
	// which last WebPage is a sibling of the source-first WebPage
	private double siblingFactor;
	
	
	/**
	 * Creates a new WebPageGraph object which is a graph for web pages. It initializes all the attributes to
	 * default values(for the program).SiblingFactor to 1 , edges to 0 , lowerBound to 0.
	 * 
	 * @param void
	 * @return void
	 * 
	 */
	public WebPageGraph()
	{
		webPageVertices=new ArrayList<WebPageVertex>();
		siblingFactor=1;
		edges=0;
		lowerBound=0;
	}
	
	
	/**
	 * This method returns the number of Vertices that this graph-object has
	 * 
	 * @param void
	 * @return the number of vertices that the object invoking the method have
	 */
	public int numberOfVertices()
	{
		return this.webPageVertices.size();
	}
	
	
	/**
	 * This method clears the graph.Also sets all the values of the attributes of this object to default.
	 * 
	 * @return void
	 */
	public void clear()
	{
		this.webPageVertices.clear();
		this.siblingFactor=1;
		this.lowerBound=0;
		this.edges=0;
		
	}
	
	/**
	 * Returns whether a graph is empty or not
	 * 
	 * @return true if the graph is empty or false otherwise
	 */
	public boolean isEmpty()
	{
		return this.webPageVertices.isEmpty();
	}
	
	
	/**
	 *This method returns the Lower bound number that distributes the spam Web Pages
	 * 
	 * @return the Lower bound number
	 */
	public double getLoweBound()
	{
		return this.lowerBound;
	}
	
	/**
	 *This method tries to change the Lower bound of the graph-object which invokes the method and returns 
	 *true if it has been changed or false otherwise.
	 * 
	 * @param lowerBound the new lower bound to replace the previous one
	 * @return true if the Lower bound has been set succesfully or false otherwise
	 */
	public boolean setLowerBound(double lowerBound)
	{
		if(lowerBound<0)
			return false;
		this.lowerBound=lowerBound;
		
		return true;
	}
	
	/**
	 * This method returns the Sibling factor of the graph-object which invokes the method
	 * @return the Sibling factor of the graph-object which invokes the method
	 */
	public double getSiblingFactor()
	{
		return this.siblingFactor;
	}
	
	
	/**
	 *This method tries to change the Sibling factor of the graph-object which invokes the method and returns 
	 *true if it has been changed or false otherwise.
	 * 
	 * @param newSiblingFactor the new sibling factor to replace the previous one
	 * @return true if the sinling factor has been set succesfully or false otherwise
	 */
	public boolean setSiblingFactor(double newSiblingFactor)
	{
		if(newSiblingFactor<=0 || newSiblingFactor>1)
			return false;
		
		this.siblingFactor=newSiblingFactor;
		return true;
		
	}
	
	/**
	 * This method takes two objects of type WebPage as argumetns tests whether the two WebPages are siblings and returns
	 * the sibling factor that is encapsulated in this object of type WebPageGraph if the two WebPages objects are siblings
	 * otherwise returns 1 indicating that the two WebPages aren't siblings. 
	 * 
	 * @param a an object of type WebPage that will be used to test whether it is a sibling web Page with the second argument object
	 * @param b an object of type WebPage that will be used to test whether it is a sibling web Page with the first argument object
	 * @return The sibling factor that is encapsulated in this object of type WebPageGraph if the two WebPages(which are passed as 
	 * arguments) objects are siblings otherwise returns 1 indicating that the two WebPages aren't siblings. 
	 */
	public double calculateSiblingFactor(WebPage a,WebPage b)
	{
		return (a.webPagesAreSiblings(b))? this.siblingFactor : 1;
		
	}
	
	
	/**
	 * Collect all the vertices of type WebPageVertex that point to a vertex ,in an object of type ArrayList<WebPageVertex>.
	 * 
	 * @param vertex an objcect of type WebPageVertex which reprsents a vertex in the graph
	 * @param inPointingVertices an object of type ArrayList<WebPageVertex> which will be filled with
	 * all the WebPageVertex objects that point to the specified (by parameter vertex WebPageVertex)
	 * WebPageVertex object
	 */
	private void getInPointingVertices(WebPageVertex vertex,ArrayList<WebPageVertex> inPointingVertices)
	{
		if(vertex==null || inPointingVertices==null)
			return ;
		
		inPointingVertices.clear();
		
		for(WebPageVertex graphVertex : this.webPageVertices)
		{
			// Check first whether the examined vertex has an out-pointing reference to the desired vertex and also 
			// check whether the examined WebPage from the graph isn't the vertex that we want to find its in-pointing vertices
			
				if(graphVertex.containsAtOutLinkedWebPages(vertex.webPage.getNameOfURL()) &&
				!graphVertex.webPage.getNameOfURL().equals(vertex.webPage.getNameOfURL()))
					inPointingVertices.add(graphVertex);
		
		}
		
	}
	
	
	/**
	 * This method returns a WebPageVertex object which is located in the object of type WebPageGraph
	 * which invokes the method ,which corresponds to the name of the URL that is passed as an  
	 * argument to parameter nameURL .If the URL name doesn't correpsond to an actual WebPage, null 
	 * is returned.
	 * 
	 * @param nameURL The name of a URL to find its WebPage in the graph
	 * @return a WebPageVertex object in the object of type WebPageGraph which invokes the method which
	 *  corresponds to the name of the URL that is passed as an argument to parameter nameURL .If the 
	 *  URL name doesn't correpsond to an actual WebPage, null is returned.
	 */
	private WebPageVertex findWebPageVertex(String nameURL)
	{
		if(nameURL==null)
			return null;
		
		for(WebPageVertex vertex : this.webPageVertices)
			if(vertex.webPage.getNameOfURL().equals(nameURL))
				return vertex;
		
		return null;
		
	}
	
	
	/**
	 * This method takes the length of the longest name of URL from the list with the WebPageVertex objects and returns it.
	 * 
	 * @param list an object of type ArrayList<WebPageVertex> which inside has some objects of type WebPageVertex
	 * @return an integer number which is the maximum length of a String .The Strings from them ,the method takes the maximum
	 * are the URL names of some WebPages
	 */
	private int findMaxLengthOfURLnames(ArrayList<WebPageVertex> list)
	{
		int max=0;		// The max length of a URL name 
		
		int index=0;	// The index of the current WebPageVertex object examined
						// in the object of type  ArrayList<WebPageVertex>
			
		int length=0;	// The current length of a URL name
		
		for(WebPageVertex vertex : list)
		{
			// If it's the first time to get in the loop,choose the
			// length of the first URL name to be the maximum
			if(index==0)
			{
				max=vertex.webPage.getNameOfURL().length();
				index++;
				continue;
			}
			
			length=vertex.webPage.getNameOfURL().length();
			
			if(max<length)
				max=length;
			
			index++;
		}
		
		return max;
		
	}
	
	
	/**
	 * This method simulates the calculations of the page ranks for all the WebPages in a WebPageGraph object
	 * and there is an option to print the progress of a WebPage specified by its URL name ,and/or print 
	 * the calculations for the WebPages that affect the specified WebPage. 
	 * 
	 * @param outputStreamForSpecificWebPage an object of type PrintStream which is used to 
	 * print the progress of the Page Rank of a focused WebPage
	 * @param outputStreamForFactorsOfSpecificWebPage  an object of type PrintStream which is 
	 * used to printthe calculations of the  pageRanks of the WebPages that affect the PageRank
	 *  of the focused WebPage
	 * @param iterations The number of iterations to run the simulation
	 * @param focusedURLname A name of the URl of a WebPage in the graph which will be focused 
	 */
	public void runPageRankCalculationProcessSimulation(PrintStream outputStreamForSpecificWebPage,PrintStream outputStreamForFactorsOfSpecificWebPage,int iterations,String focusedURLname)
	{
		if(this.isEmpty() || iterations<0)
			return ;
		
		
		WebPageVertex focusedVertex=findWebPageVertex(focusedURLname);
		
		// Condition that has to be valid in order to print the progress of calculation of a web page
		boolean printCalculationPRofSpecificWebPage= focusedVertex!=null && outputStreamForSpecificWebPage!=null;
		
		// Condition that has to be valid in order to print the calculations of the web pages that affect a specific web page
		boolean printFactorsOfSpecificWebPage=outputStreamForFactorsOfSpecificWebPage!=null && focusedVertex!=null;
		
		// an array which corresponds to the vertices in the arraylist by their position in the arraylist
		double newPageRanks[]=new double[this.webPageVertices.size()];
			
		// The number of vertices in the graph
		int vertices=this.numberOfVertices();
		
		
		int index=0;
		
		// The current calculate page Rank of each vertex
		double calculatedPageRank=0;
		
		// The current vertex to use for calculating its pageRank
		WebPageVertex currentVertex=null;
		
		// A list which contains all the vertices that point to a vertex
		ArrayList<WebPageVertex> inPointingVertices=new ArrayList<WebPageVertex>();
		
		// Initialization of the pageRanks of the vertices in order to test the simulation
		for(WebPageVertex vertex : this.webPageVertices)
			vertex.webPage.setPageRank(INITIAL_PAGE_RANK);
		
		// Object for decimal formatting
		DecimalFormat formatter=new DecimalFormat("0.00");
		
		// The arrayList which will hold the web pages that affect the page rank of the selected web page
		ArrayList<WebPageVertex> focusedWebPageFactors=null;
		
		// The arrayList which will hold all the calculations of the page ranks of the web pages that affect the page rank of the selected webpage
		ArrayList< ArrayList<String>> calculationsOfTheFactors=null;
		
		if(printCalculationPRofSpecificWebPage)
		{
			outputStreamForSpecificWebPage.println("++++++++++++++++++++  Focused webPage is "+focusedURLname+" ++++++++++++++++++++ ");
			outputStreamForSpecificWebPage.println("Initial Page Rank : "+focusedVertex.webPage.getPageRank());
								
		}
		
		
		if(printFactorsOfSpecificWebPage)
		{
					
				focusedWebPageFactors=new ArrayList<WebPageVertex>();
				
				getInPointingVertices(focusedVertex, focusedWebPageFactors);
				
				focusedWebPageFactors.trimToSize();
				
				calculationsOfTheFactors=new ArrayList< ArrayList<String>> (focusedWebPageFactors.size());
							
				
				// Initialization of the calculation of the factors statistics with the name of the factors
				for(index=0;index<focusedWebPageFactors.size(); ++index)
				{
					calculationsOfTheFactors.add(index,new ArrayList<String>());
					calculationsOfTheFactors.get(index).add(focusedWebPageFactors.get(index).webPage.getNameOfURL());
					
					// The first page rank of each web page is its initial
					int isSpam=(this.isSpam(focusedWebPageFactors.get(index))? 1 : 0);
					calculationsOfTheFactors.get(index).add(+isSpam+"/ "+String.valueOf(focusedWebPageFactors.get(index).webPage.getPageRank()));
					
				}
				
		}
		
		
		
		
		for(int i=0;i<iterations;i++)
		{
			// In every loop the next sequence of pageRanks for all the vertices of the graph is calculated
		
			
			for(index=0;index<vertices;index++)
			{
				calculatedPageRank=0;
				
				currentVertex=this.webPageVertices.get(index);
				
				// Get all the vertices that point to the current vertex
				this.getInPointingVertices(currentVertex, inPointingVertices);
				 
				int inDegree=inPointingVertices.size();
				
				boolean itsTime=printCalculationPRofSpecificWebPage && currentVertex.equals(focusedVertex);
				
				if(itsTime)
				{
					outputStreamForSpecificWebPage.print("I : "+i+" --> ");
					outputStreamForSpecificWebPage.printf("(1 - %.2f) + %.2f*(",D_FACTOR,D_FACTOR);
				}
				
				WebPageVertex inVertex=null;
				
				for(int k=0;k<inDegree;k++)
				{
					
					
					inVertex=inPointingVertices.get(k);
					
					if(!this.isSpam(inVertex))
					{
						// Calculate final sibling factor for this pointed vertex in association with the current vertex
						double S=calculateSiblingFactor(currentVertex.webPage,inVertex.webPage);
						
						double previousPageRank=inVertex.webPage.getPageRank();
						
						int outDegree=inVertex.outLinkingWebPages.size();
						
						calculatedPageRank+=S*previousPageRank/outDegree;
					
						if(itsTime)
						{
							if(k>0)
								outputStreamForSpecificWebPage.print("+");
							
							outputStreamForSpecificWebPage.printf(" (%.2f *(%.2f / %d)) ",S,previousPageRank,outDegree);
															
						}				
																							
					}
					else
					{
						// Doesn't affect the page Rank of the current vertex
						
						if(itsTime)
						{
							if(k>0)
								outputStreamForSpecificWebPage.print("+");
							
							// 0 indicates zero affection and indicates that this vertex was a spam 	
							outputStreamForSpecificWebPage.printf(" 0 ");
															
						}	
					}
										
										
				}
				
				calculatedPageRank*=D_FACTOR;
				calculatedPageRank+=(1-D_FACTOR);
				
				newPageRanks[index]=calculatedPageRank;
				
				if(itsTime)
				{
					outputStreamForSpecificWebPage.printf(") = %.2f",calculatedPageRank);
					outputStreamForSpecificWebPage.println();
				}
				
			}
			
			index=0;
			
			for(WebPageVertex vertex : this.webPageVertices)
				vertex.webPage.setPageRank(newPageRanks[index++]);
			
			// avoid to print the last iteration because if so the profram will print iterations+1 page ranks
			if(printFactorsOfSpecificWebPage && i!=iterations-1)  
			{													 
				int isSpam=0;
				
								
				for(index=0;index<calculationsOfTheFactors.size();index++)
				{
										
					isSpam=(this.isSpam(focusedWebPageFactors.get(index))? 1 : 0);
					calculationsOfTheFactors.get(index).add(isSpam+"/ "+
					formatter.format(focusedWebPageFactors.get(index).webPage.getPageRank()));
				}
				
				
			}
			
		}
		
		if(printFactorsOfSpecificWebPage)
		{
			
			int maxSizeOfURL=findMaxLengthOfURLnames(focusedWebPageFactors);
			
			outputStreamForFactorsOfSpecificWebPage.println("\n-------------------------- Page Ranks calculations of the factors that affect web page with URL : "+focusedURLname+" --------------------------\n");
			
			int size=calculationsOfTheFactors.size();
			
			for(index=0;index<size;index++)
			{
				outputStreamForFactorsOfSpecificWebPage.printf("%-"+(maxSizeOfURL)+"s : ", calculationsOfTheFactors.get(index).get(0));
				
				for(int i=1;i<calculationsOfTheFactors.get(index).size();i++)
				{
					outputStreamForFactorsOfSpecificWebPage.printf("%-10s ",calculationsOfTheFactors.get(index).get(i));
				}
								
				
				outputStreamForFactorsOfSpecificWebPage.println();
			}
		
			outputStreamForFactorsOfSpecificWebPage.println("<< Note >>  ( isSample/ pageRank of each iteration )");
		}
		
		
	}
	
	
	
	
	
	
	/**
	 * This method takes an object of type WebPageVertex which will be examined to see if its WebPage is spam.
	 * It returns the result of the examination.
	 * 
	 * @param vertex an object of type WebPageVertex which will be examined to see if its WebPage is spam
	 * @return true if its WebPage is spam ,or false if it's not
	 */
	private boolean isSpam(WebPageVertex vertex)
	{
		if(vertex.webPage==null)
			return false;
		
		if(vertex.webPage.getPageRank()<this.lowerBound)
			return true;
			
		return false;
	}
	
	
	/**
	 * This method takes the name of a file as a String and uses the data in the 
	 * file for creating the vertices of the graph.
	 * 
	 * @param filename the name of a File which contain data to build the graph
	 * @return true if the data has been used succesfuly to create vertices in the graph 
	 * @throws FileNotFoundException The Exception is thrown when there occur problems while reading
	 * the data from the file
	 */
	public boolean createWebPageGraphFromFileData(String filename) throws FileNotFoundException
	{
		if(filename==null)
			return false;
		
		Scanner inputStream=new Scanner(new FileInputStream(filename));
		
		while(inputStream.hasNext())
		{
			this.addWebPage(new WebPage(inputStream.next(),INITIAL_PAGE_RANK));
		}
		
		inputStream.close();
		
		return true;
		
	}
	
	
	/**
	 * This methos creates random associations-references between the vertices of the graph.This method
	 * presupposes that the graph isn't empty.
	 * 
	 * @return true if the random associations between the vertices of the graph have been created succesfully
	 */
	public boolean createAssociationsBetweenVertices()
	{
		if(this.webPageVertices.isEmpty())
			return false;
		
		// An array which will be filled with random numbers which numbers represent indexes in the object of 
		// type ArrayList<WebPageVertex>. 
		int randomNumbers[]=new int[MAX_NUMBER_OF_OUT_DIRECTED_EDGES_IN_VERTEX-MIN_NUMBER_OF_OUT_DIRECTED_EDGES_IN_VERTEX+1];
		int randomNumberOfOutVertices=0;
		int index=0;
		
		for(WebPageVertex vertex : this.webPageVertices)
		{
			
			randomNumberOfOutVertices=(int)((MAX_NUMBER_OF_OUT_DIRECTED_EDGES_IN_VERTEX-MIN_NUMBER_OF_OUT_DIRECTED_EDGES_IN_VERTEX+1)*(Math.random()));
			
			randomNumberOfOutVertices+=MIN_NUMBER_OF_OUT_DIRECTED_EDGES_IN_VERTEX;
			// The random number of out-pointing-referencing vertices from the current vertex ,is now placed at randomNumberOfOutVertices
			
			// The index represents the number of the index in the ArrayList<WebPageVertex> that the current Vertex is located
			// Beacuse we don't accept references to from a vertex to itself ,we promise that something like this won't happen
			// by forcing the random numbers not to be equal to that number
			updateRandomNumbers(randomNumbers,randomNumberOfOutVertices,index);
			
			
			// For all the vertices that randomly were collected , add a reference from the current verex examined to them 
			for(int i=0;i<randomNumberOfOutVertices;i++)
			{
				this.addDirectedWeightedEdge(vertex.webPage.getNameOfURL(), this.webPageVertices.get(randomNumbers[i]).webPage.getNameOfURL(), 1.0/randomNumberOfOutVertices);
			}
			
			index++;
		}
		
		return true;
		
	}
	
	
	/**
	 * This method takes an array and fills it with random numbers which the values of random numbers
	 * are between zero and of the size of the the graph(which the object invoking the method represents)
	 *  minus 1.The number of random numbers to produce is specified by parameter  numberOfRandomNumers.Also
	 *  the parameter numberToExclude is filled with a number which the method has to exlude in the random
	 *  numbers that will be produced.
	 * 
	 * @param numbers an array of integers which inside therr random numbers will be placed
	 * @param numberOfRandomNumers 	 The number of the random numbers to produce
	 * @param numberToExclude     The number which we don't want to include in the random numbers
	 * @return true if the creation and store in the array of the random numbers was succesful ,otherwise false 
	 */
	private boolean updateRandomNumbers(int[] numbers,int numberOfRandomNumers,int numberToExclude)
	{
		if(numbers==null || numberOfRandomNumers<=0 || numberOfRandomNumers>numbers.length)
			return false;
		
		int index=0;
		int randomNumber=0;
		int sizeOfGraph=this.webPageVertices.size();
		
		OuterLoop :
		while(index<numberOfRandomNumers)
		{
			randomNumber=(int)(Math.random()*sizeOfGraph);
			
			// Now at randomNumber we have an integer number between  MIN_NUMBER_OF_OUT_DIRECTED_EDGES_IN_VERTEX and
			// MAX_NUMBER_OF_OUT_DIRECTED_EDGES_IN_VERTEX , inclusive
			
			if(numberToExclude==randomNumber)
				continue;
			
			// check if the number already exists at the array .This means that the same number was calculated before
			for(int i=0;i<index;i++)
				if(randomNumber==numbers[i])
					continue OuterLoop;
			
			numbers[index]=randomNumber;
			index++;
		}
		
		// Before returning the array has at the first numberOfRandomNumers positions discrete integer numbers
				
		return true;
	}
	
	
	/**
	 * This method takes an object of type WebPage and places this object in the object of
	 *  type WebPageGraph which invokes the method 
	 * 
	 * @param newWebPage an object of type WebPage which will be added in the Graph
	 * @return true if the WebPage was added o the graph or false otherwise
	 */
	public boolean addWebPage(WebPage newWebPage)
	{
		if(newWebPage==null)
			return false;
		
		
		for(WebPageVertex vertex : this.webPageVertices)
			if(vertex.webPage.getNameOfURL().equals(newWebPage.getNameOfURL()))
				return false;
		
		
		this.webPageVertices.add(new WebPageVertex(newWebPage));
		
		return true;
		
	}
	
	/**
	 * This method takes a URL name and returns the correpsonding object of type WebPage in the graph.
	 * If a corresponding object of type WebPage doen't exist ,then null is returned.
	 * 
	 * @param nameURL the name of a URL that might correpsond to a WebPage
	 * @return the object of type WebPage,which is located inside the object of 
	 * type WebPageGraph, and correpsonds to the name of URL passed as an argument 
	 */
	public WebPage getWebPageWithSpecificNameURL(String nameURL)
	{
		if(nameURL==null)
			return null;
		
		for(WebPageVertex vertex : this.webPageVertices)
		{
			if(vertex.webPage.getNameOfURL().equals(nameURL))
				return vertex.webPage;
		}
		
		return null;
	}
	
	/**
	 * This method returns an array with all the WebPage objects that that are pointed to by 
	 * an object of type WebPage with URL name as indicated by the parameter.If the name of
	 * the URL doesn't correpsnod to an actual WbPage in the graph ,null is returned.
	 * 
	 * @param nameURL a name of a URL
	 * @return array with all the WebPage objects that that are pointed to by an object of type
	 *  WebPage with URL name as indicated by the parameteror or null if the name of the URL 
	 *  doen't correpsond to any actual WebPage in the graph
	 */
	public WebPage[] getOutputLinkedWebPagesOfWebPageWithURL(String nameURL)
	{
		if(nameURL==null)
			return null;
		
		for(WebPageVertex vertex : this.webPageVertices)
		{
			if(vertex.webPage.getNameOfURL().equals(nameURL))
				return vertex.getOutLinkedWebPages();
		}
		
		return null;
		
	}
	

	
	/**
	 * This method adds a directed edge from a vertex,which its URL name is specified by the first parameter
	 *  to a vertex which its URL name is specified by the second parameter with weight as indicated by the 
	 *  parameter weight.
	 * 
	 * @param fromURLname The name of the URL of the source vertex in the graph
	 * @param toURLname The name of the URL of the destination vertex in the graph
	 * @param weight The weight of the edge that connects the source vertex to the destination vertex
	 * @return true if the directed edge was inserted succesfully or faslse otherwise
	 */
	public boolean addDirectedWeightedEdge(String fromURLname,String toURLname,double weight)
	{
		
		
		if(fromURLname==null || toURLname==null || weight<0 || fromURLname.equals(toURLname))
			return false;
		
		WebPageVertex fromVertex=null;
		WebPageVertex toVertex=null;
		
		for(WebPageVertex vertex : this.webPageVertices)
		{
			if(vertex.webPage.getNameOfURL().equals(fromURLname))
			{
				fromVertex=vertex;
				
				if(toVertex!=null)			// if the destination of the directed edge has been already found skip 
					break;					// the rest elements by breaking the loop
				
			}
			
			if(vertex.webPage.getNameOfURL().equals(toURLname))
			{
				
				toVertex=vertex;
				
				if(fromVertex!=null)			// if the source of the directed edge has been already found skip 
					break;					// the rest elements by breaking the loop
				
			}
			
		}
		
		//System.out.println(fromVertex.webPage.getNameOfURL()+" | "+toVertex.webPage.getNameOfURL()+" | "+fromVertex.containsAtOutLinkedWebPages(toURLname));
		
		if(fromVertex==null || toVertex==null || fromVertex.containsAtOutLinkedWebPages(toURLname))
			return false;
		
		
		// Connect the the fromVertex with the toVertix , with a directed edge with weight as indicated by the parameter
		fromVertex.outLinkingWebPages.add(new OutLinkedWebPage(toVertex,weight));
		
		this.edges++;
		
		return true;
		
	}
	
	
	/**
	 * 
	 * @param urlName
	 * @return an object of type WebPage which correpsonds to the name of URL given as parameter
	 *  that is deleted from the graph, or null if a WebPage with the URL name specified doesn't exist.
	 */
	public WebPage deleteWebPageVertex(String urlName)
	{
				
		int index=0;
		
		for(WebPageVertex vertex : this.webPageVertices)
		{
						
			if(vertex.webPage.getNameOfURL().equals(urlName))
			{
				// Deletes the record of the WebPage with URL name as specified by parameter 
				this.webPageVertices.remove(index);
			}
			else
			{
				// For all the webPages that point to the focused WebPage delete
				// the record that points to the focused WebPage
				vertex.deleteWebPageAtLinkedOutWebPages(urlName);
			}
			
			index++;
		}
			
		
		return null;
		
	}
	
	
	
	
	private class WebPageVertex  implements Serializable
	{
					
		/**
		 * 
		 */
		private static final long serialVersionUID = 8011038800385089223L;

		private ArrayList<OutLinkedWebPage> outLinkingWebPages;		// The out-pointing webPages where this webPage points to

		private WebPage webPage;
		
		/**
		 * Constructor of an object of type WebPageVertex which represents a vertex in a graph and holds a reference
		 * to a WebPage object .Also this object holds the references to some other vertices, as an object 
		 * ArrayList<OutLinkedWebPage>
		 * 
		 * @param webPage an object of type WebPage whcih will be encapsulated in a vertex (object of type WebPageVertex)
		 */
		public WebPageVertex(WebPage webPage)
		{
			if(webPage==null)
				throw new NullPointerException("Null pointer was given as argument for webpage at constructor in WebPageVertex class");
				
			this.webPage=webPage;
			outLinkingWebPages=new ArrayList<OutLinkedWebPage>();
		}
				
		/**
		 * Returns a String representation of the object invoking this method
		 * 
		 * @return a String representation of the object invoking th emethod
		 */
		public String toString()
		{
			StringBuilder builder=new StringBuilder(this.webPage.toString()+"\n");
			builder.append("Out linked Web Pages :\n");
			for(OutLinkedWebPage el : this.outLinkingWebPages)
			{
				builder.append("-------------------------------------------------------\n");
				builder.append(el.toString());
				builder.append("\n");
			}
			
			return this.getClass().getName();
		}
		
		
		/**
		 * This method returns an array of object of type WebPage which represents 
		 * the pointing WebPages of the object of type WebPageVertex which invokes the method
		 * 
		 * @param void
		 * @return an array of object of type WebPage which represents 
		 * the pointing WebPages of the object of type WebPageVertex which invokes the method
		 * 
		 */
		public WebPage[] getOutLinkedWebPages()
		{
			WebPage[] outWebPages=new WebPage[this.outLinkingWebPages.size()];
			
			int index=0;
			
			for(OutLinkedWebPage outLinkedWebPage : this.outLinkingWebPages)
			{
				outWebPages[index++]=outLinkedWebPage.pointedWebPage.webPage;
			}
			
			return outWebPages;
			
		}
		
		
		/**
		 * This method examines whether a  URL name corresponds to a WebPage which exists in the list with
		 *  the out-pointed vertices ,or not and returns the result of the examination.
		 * 
		 * @param urlName the name of a URL 
		 * @return true if the URL name corresponds to a WebPage  which exists in the list with
		 *  the out-pointed vertices ,or false if not
		 */
		public boolean containsAtOutLinkedWebPages(String urlName)
		{
			for(OutLinkedWebPage element : outLinkingWebPages)
			{
				if(element.pointedWebPage.webPage.getNameOfURL().equals(urlName))
					return true;
				
			}
			
			return false;
			
		}
		
		
		/**
		 * This method deletes a record of a WebPage of type OutLinkedWebPage which correpsonds 
		 * to the name of the URL which is passed as an argument in the list with the OutLinkedWebPage
		 *  objects of an object of type WebPageVertex
		 * 
		 * @param urlName the name of a URL 
		 * @return true if the deletion was made succesfully or false if not
		 */
		public boolean deleteWebPageAtLinkedOutWebPages(String urlName)
		{
			int index=0;
			
			for(OutLinkedWebPage element : outLinkingWebPages)
			{
				
				if(element.pointedWebPage.webPage.getNameOfURL().equals(urlName))
				{
					this.outLinkingWebPages.remove(index);
					return true;
				}
				
				index++;
				
			}
			
			return false;
			
		}
		
		
		
		/**
		 * Checks if the object of type WebPageVertex , which is given as argument is equal with the object which invokes
		 * the method. We define that two objects of type WebPageVertex are equal if and only if they have the same WebPage
		 * objects as attributes and also their linking-out webPages objects of type OutLinkedWebPage are the same.
		 * 
		 * @param otherObject an object of type Object
		 * @return true if the oject invoking the methodis equal with the object passed as an argument
		 * 
		 */
		public boolean equals(Object otherObject)
		{
			if(otherObject==null || otherObject.getClass()!=getClass())
				return false;
			
			WebPageVertex vertex=(WebPageVertex)otherObject;
			
			if(! vertex.webPage.equals(this.webPage))
				return false;
			
			// If the size of their OutLinkedWebPage objects isn't the same,then they aren't the same
			if(this.outLinkingWebPages.size()!=vertex.outLinkingWebPages.size())
				return false;
			
			
OuterLoop : for(OutLinkedWebPage element : this.outLinkingWebPages)
			{
	
				// For every object of type OutLinkedWebPage of the object invoking the method examine
				// whether the object passed as an argument has it
				for(OutLinkedWebPage element2 : vertex.outLinkingWebPages)
					if(element.pointedWebPage.webPage.equals(element2.pointedWebPage.webPage))
						continue OuterLoop;
					
			
			    return false;
			}
			
			return true;
						
		}
		
			
	}
	
	
	/**
	 * This class represents the weighted connection of a WebPageVertex with another WebPageVertex. 
	 * 
	 * @author Valentinios Pariza
	 *
	 */
	private class OutLinkedWebPage implements Serializable
	{
		
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -2128849198773528948L;

		private WebPageVertex pointedWebPage;   // the WebPage where another webPage points to  (in a graph)

		private double weightOfPossibility;   // The weight of an edge which edge points to the webPageVertex with id
											  // as indicated by attribute pointedWebPageID

		
		/**
		 * This method constructs a new object of type OutLinkedWebPage specifying the 
		 * connection of an object of type WebPageVertex with an object of type WebPageVertex
		 * that is passed as an argument and with zero weight.
		 * 
		 * @param pointedWebPage an object of type WebPageVertex which another WebPageVertex object points to
		 */
		public OutLinkedWebPage(WebPageVertex pointedWebPage)
		{
			
			if(pointedWebPage==null)
				throw new NullPointerException("Null pointer was given as argument for pointedWebPage at constructor in OutLinkedWebPage class");
			
			this.pointedWebPage=pointedWebPage;
			weightOfPossibility=0;
		}
		
		
		/**
		 * This method constructs a new object of type OutLinkedWebPage specifying the 
		 * connection of an object of type WebPageVertex with an object of type WebPageVertex
		 * that is passed as an argument and with  weight as indicated by theargument passed 
		 * in parameter weightOfPossibility.
		 * 
		 * @param pointedWebPage an object of type WebPageVertex which another WebPageVertex object points to
		 * @param weightOfPossibility The weight of the directed edge that has been created, by the connection of the vertices
		 */
		public OutLinkedWebPage(WebPageVertex pointedWebPage,double weightOfPossibility) throws NegativeNumberException
		{
			
			 if(weightOfPossibility<0)
				throw new NegativeNumberException("Negative number given in constructor of OutLinkedWebPage class ,for the weight of the edge in the graph -> weightOfPossibility");	
			
			 if(pointedWebPage==null)
					throw new NullPointerException("Null popinter was given as argument for pointedWebPage at constructor in OutLinkedWebPage class");
			
			 this.pointedWebPage=pointedWebPage;
			
			this.weightOfPossibility=weightOfPossibility;
		}
		
		
		
		

		/**
		 * Checks if the object of type OutLinkedWebPage , which is given as argument is equal with the object which invokes
		 * the method. We define that two objects of type OutLinkedWebPage are equal if and only if they have equal 
		 * pointedWebPage objects as attributes and same weight.
		 * 
		 * @return true if the object invoking the method is equal to the object passed as an argument
		 * 
		 */
		public boolean equals(Object anObject)
		{
			if(anObject==null || this.getClass()!=anObject.getClass())
				return false;
			
			OutLinkedWebPage outLinkedPage=(OutLinkedWebPage)anObject;
			
			return outLinkedPage.pointedWebPage.equals(this.pointedWebPage) &&
					this.weightOfPossibility==outLinkedPage.weightOfPossibility;
			
		}
		
		
		/**
		 * Returns a String representation of the object type of OutLinkedWebPage which  invokes this method.
		 * 
		 * @return String representation of the object invoking this method
		 */
		public String toString()
		{
			return "Pointed WebPage : "+this.pointedWebPage.webPage+"  and the "
			+ "possibility weight of the connction is "+this.weightOfPossibility;
		}
		
	}	
	
}
