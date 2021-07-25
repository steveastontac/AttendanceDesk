package com.example.project.ui.home;

import com.google.firebase.database.Exclude;

public class AttendanceData {


    // Store the id of the  movie poster
    private String studusn;
    // Store the name of the movie
    private String name;
    // Store the release date of the movie
    private int attendance;
    private int counter;
    private String year ;
    private String daye ;
    private String section;
//    private long nKey;
    // Constructor that is used to create an instance of the NotificationData object
    public AttendanceData( String cstudname, int cattend ) {
        //this.studusn= cstudusn;
        this.counter = 0;
        this.name = cstudname;
        this.attendance = cattend;
        System.out.println(" cattend for "+studusn +" is  "+cattend);
//        this.nKey=0;
    }
    public AttendanceData(){}

    @Exclude
    public String getstudusn() {
        return studusn;
    }

    public void setYr(String yr) {
        this.year=yr;
    }

    public void setSx(String sx) {
       this.section=sx;
    }
    public void setDaye(String dy) {
        this.daye=dy;
    }


    public String getYr() {
        return year;
    }
    public String getSx() {
        return section;
    }
    public String getDaye() {
        return daye;
    }



    public void setStudusn(String usn) {
        this.studusn = usn;
    }

    public String getName() {
        return name;
    }

    public void setName(String anName) {
        this.name = anName;
    }

    public int getAttendance() {
        return attendance;
    }

    public void setAttendance(int attended) {
        this.attendance = attended;   }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int count){this.counter = count;}
//    public void setnKey(long k){ this.nKey =k;}
//    public long getnKey(){ return this.nKey; }

}
