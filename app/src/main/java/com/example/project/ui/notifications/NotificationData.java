package com.example.project.ui.notifications;

public class NotificationData {

    // Store the id of the  movie poster
    private int nIcon;
    // Store the name of the movie
    private String nName;
    // Store the release date of the movie
    private String nDetails;
    private long nKey;
    // Constructor that is used to create an instance of the NotificationData object
    public NotificationData(int cnIcon, String cnName, String cnDetails ) {
        this.nIcon = cnIcon;
        this.nName = cnName;
        this.nDetails = cnDetails;
        this.nKey=0;
    }

    public int getnIcon() {
        return nIcon;
    }

    public void setnIcon(int anIcon) {
        this.nIcon = anIcon;
    }

    public String getnName() {
        return nName;
    }

    public void setnName(String anName) {
        this.nName = anName;
    }

    public String getnDetails() {
        return nDetails;
    }

    public void setnDetails(String anDetails) {
        this.nDetails = anDetails;
    }
    public void setnKey(long k){ this.nKey =k;}
    public long getnKey(){ return this.nKey; }

}
