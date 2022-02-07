package com.home.onlinelibrary.controllers.params;

public class RegistrationState {
    private boolean userAlreadyExist = false;

    public boolean isUserAlreadyExist() {
        return userAlreadyExist;
    }

    public void setRegistrationState(boolean isUserAlreadyExist) {
        this.userAlreadyExist = isUserAlreadyExist;
    }
}
