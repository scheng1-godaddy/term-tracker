package com.shawncheng.termtracker.model;

import java.io.Serializable;

public class Assessment implements Serializable {

    private int assessmentId;
    private String title;
    private String type;
    private String dueDate;
    private int courseId;

    public Assessment() {}

    public Assessment(int assessmentId, String title, String type, String dueDate, int courseId) {
        this.assessmentId = assessmentId;
        this.title = title;
        this.type = type;
        this.dueDate = dueDate;
        this.courseId = courseId;
    }

    public int getAssessmentId() {
        return assessmentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
