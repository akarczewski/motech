package org.motechproject.security.ex;

/**
 * Exception that signalizes that given user was not found
 */
public class UserNotFoundException extends Exception {

    public UserNotFoundException() {
        super();
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
