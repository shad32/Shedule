package com.chernov.niko.timetable.customList;


public class ItemOfSchedule {
    private String time, subject, audience, teacher;

    public ItemOfSchedule(){

    }
    public ItemOfSchedule(String time, String subject, String audience, String teacher){
        this.time     = time;
        this.audience = audience;
        this.subject  = subject;
        this.teacher  = teacher;
    }

    public String getSubject() {
        return subject;
    }

    public String getAudience() {
        return audience;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
}
