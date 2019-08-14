package pageRankImplementation;

/**
 * This class is an Exception class for representing the Exceptions caused by
 * using negative numbers in a specific situation in the program.This 
 * Exception type is a derived class from {@link ArithmeticException}
 * because this Exception (unchecked Exception) gives the opportunity
 * to be used in some cases as an unhandled Exception.But anyone  
 * who wants to catch it can handle this Exception explicit.
 * 
 * @author vpariz01
 * @version 1.0
 */
public class NegativeNumberException extends ArithmeticException{

	public static final String DEFAULT_MESSAGE="NegativeNumberException was thrown.";
	
	
	/**
	 * This constructor is for creating an Exception object type of  
	 * @see NegativeNumberException using the constructor of the 
	 * ancestor class {@link ArithmeticException} and using a default message
	 * {@link NegativeNumberException#DEFAULT_MESSAGE} for error message.
	 * 
	 * @param void
	 * @return void
	 */
	public NegativeNumberException()
	{
		super(DEFAULT_MESSAGE);
	}
	
	
	/**
	 * This constructor is for creating an Exception object type of by 
	 * @see NegativeNumberException using the constructor of the 
	 * ancestor class {@link ArithmeticException}.It takes a String 
	 * as an error message.If that message is null then the default
	 * message is used for an error message
	 * {@link NegativeNumberException#DEFAULT_MESSAGE}.
	 * 
	 * @param message a String representing an error message
	 * @return void
	 */
	public NegativeNumberException(String message)
	{
		super( (message==null) ? DEFAULT_MESSAGE : message );
	}
	
}
