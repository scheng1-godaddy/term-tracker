package com.shawncheng.termtracker.model;

import java.io.Serializable;

public class Course implements Serializable {

    private int courseId;
    private String title;
    private String startDate;
    private String endDate;
    private String status;
    private int termId;

    public Course() { }

    public Course(int courseId, String title, String status, String startDate, String endDate, int termId) {
        this.courseId = courseId;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.termId = termId;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTermId() {
        return termId;
    }

    @Override
    public String toString() {
        return title;
    }
}
