package com.kmercoders.balancedApp.response;

import java.util.Map;

public class GroupResponse {
	private boolean validated;
    private Map<String, String> errorMessages;
 
    public boolean isValidated() {
        return validated;
    }
 
    public void setValidated(boolean validated) {
        this.validated = validated;
    }
 
    public void setErrorMessages(Map<String, String> errorMessages) {
        this.errorMessages = errorMessages;
    }
 
    public Map<String, String> getErrorMessages() {
        return errorMessages;
    }
}
