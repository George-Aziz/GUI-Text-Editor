package COMP3003.Assignment02;

// Thrown in the event of a parsing error while reading the text area file
public class FileLoadingException extends Exception
{
    public FileLoadingException(String msg, Throwable cause)
    {
        super(msg, cause);
    }
}
