package com.shawncheng.termtracker.model;

public class GoalDate {

    //TODO might need to remove goal date object
    private int goalDateId;
    private String date;
    private int assessmentId;

    public GoalDate(String date) {
        this.date = date;
    }

    public GoalDate(int goalDateId, String goalDate, int assessmentId) {
        this.goalDateId = goalDateId;
        this.date = goalDate;
        this.assessmentId = assessmentId;
    }

    public int getGoalDateId() {
        return goalDateId;
    }

    public int getAssessmentId() {
        return assessmentId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}