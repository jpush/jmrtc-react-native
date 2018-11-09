package io.jmrtc.android.event;

public class AddSurfaceViewEvent {

    private String username ;

    public AddSurfaceViewEvent(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
