package com.scm.scm20.helper;

public class ResourceNotFoundException extends RuntimeException
{
    public ResourceNotFoundException()
    {
        super("Resource not found...");

    }
    public ResourceNotFoundException(String message)
    {
        super(message);
    }

}
