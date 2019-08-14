package pageRankImplementation;

import java.io.Serializable;

/**
 * 
 * This class represents a WebPage. This class can be used to describe a WebPage, by holding as attributes the name of 
 * the URL that a Web Page has and also the page rank of the Web Page . Although a Web Page can have many more attributes
 * but in this version of the Web Page ,we find the completion of the WebPage at only two attributes ,as described before.
 * 
 * 
 * @author Valentinos Pariza
 *
 */
public class WebPage implements Serializable{

	private String url; 		// The path of the webPage 
	
	private double pageRank; 			// the pageRank of the current WebPage
	
	
	/**
	 * This constructor creates an object of this class WebPage, that has a URL name as indicated
	 *  by the argument WebPageURLname and an initial page rank value as indicated by the argument 
	 *  initialPageRank.
	 * 
	 * @param WebPageURLname the name of the URL of the Web Page to create
	 */
	public WebPage(String WebPageURLname,double initialPageRank)
	{
		// Throw an exception if the argument which represents the name of the URL is null as an effort to prevent the creation of
		// the WebPage object with wrong data
		if(WebPageURLname==null) 
			throw new NullPointerException("Null webpage path given in constructor of the class WebPagePath.");
		
		else if(initialPageRank<=0)
		{
			
			// incorrect values of page Rank
			if(initialPageRank<0)
				throw new NegativeNumberException("Negative number of initial Page Rank given at constructor of class WebPage");
			else 
				throw new ZeroNumberException("Zero number of initial Page Rank given at constructor of class WebPage");
			
		}
	
		// Initializing the page rank and the URL name of this WebPage object
		this.pageRank=initialPageRank;
		this.url=WebPageURLname;
		
	}
	
	
	/**
	 * This constructor creates an object of this class WebPage, that has a URL name as indicated by the argument and
	 * an initial page rank value 1.
	 * 
	 * @param WebPagePath the name of the URL of the Web Page to create
	 */
	public WebPage(String WebPagePath)
	{
		this(WebPagePath,1);
	}
	
	
	/**
	 * This method returns the URL name of this WebPage object
	 * 
	 * @return the URL name of this WebPage object
	 */
	public String getNameOfURL()
	{
		return this.url;
	}
	
	
	/**
	 * This method returns the page rank value of this object of type WebPage that is corresponded to it
	 * 
	 * @return the page rank value of this object of type WebPage
	 */
	public double getPageRank()
	{
		return this.pageRank;
	}
	
	
	/**
	 * This method sets the new page rank of the object of type WebPage that invokes the method and returns 
	 * true if it has been changed or false otherwise
	 * 
	 * @param newPageRank the new page Rank to set
	 * @return true if the page rank of this WebPage object has been changed successfully or false otherwise
	 */
	public boolean setPageRank(double newPageRank)
	{
		if(newPageRank<=0)
			return false;
		
		this.pageRank=newPageRank;
		return true;
	}
	
	
	/**
	 * This method sets the URL name of a Web Page and returns true if it has been set successfully or false otherwise
	 * 
	 * @param newWebPageURLname a String which represents the new name of the URL 
	 * @return true if the URL name has been changed or false if it hasn't been changed
	 */
	public boolean setWebPageURLName(String newWebPageURLname)
	{
		if(newWebPageURLname==null || newWebPageURLname.length()<=0)
			return false;
		
		this.url=newWebPageURLname;
		
		return true;
	}
	
	
	/**
	 * Checks if the object of type WebPage , which is given as argument is equal with the object type of WebPage 
	 * which invokes the method. We define that two objects of type WebPage are equal if and only if they have the 
	 * same URL name.
	 * 
	 * @param otherObject an object of the class Object
	 * @return true if the object of type WebPage that invokes the method is equal to the object of type Object that 
	 * is passed as an argument
	 */
	public boolean equals(Object otherObject)
	{
		if(otherObject==null || getClass()!=otherObject.getClass())
				return false;
		
		WebPage webpage=(WebPage)otherObject;
		
		return this.url.equals(webpage.url) ;	
	}
	
	
	
	/**
	 * Returns a String representation of the object type of WebPage which invokes the method
	 * 
	 * @return a String representation of the object invoking the method
	 * 
	 */
	public String toString()
	{	
		return "URL : "+this.url+"  |  Page Rank : "+this.pageRank;
	}
	
	
	/**
	 * 
	 * This method checks whether the object of type WebPage that invokes the method is sibling to the object of type WebPage that is passed as an argument
	 * Two objects of type WebPage are said to be siblings if and only if come from the same domain. More simpler if they have the same 
	 * starting URL name.
	 * 
	 * @param b an object of type WebPage
	 * @return true if the object of type WebPage that invokes the method is sibling to the object of type WebPage that is passed as an argument ,otherwise false
	 */
	public boolean webPagesAreSiblings(WebPage b)
	{
		String urlA=this.getNameOfURL();		// take the name of the URL of the first webPage
		
		String urlB=b.getNameOfURL();			// take the name of the URL of the second webPage
		
		// check whether the two URL names start from the same name
		return urlA.substring(0, urlA.indexOf('/')).equals(urlB.substring(0, urlB.indexOf('/')));
		
	}
	
	
}
