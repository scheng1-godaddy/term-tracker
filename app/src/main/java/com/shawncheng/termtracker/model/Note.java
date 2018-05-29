package com.shawncheng.termtracker.model;

import java.io.Serializable;

public class Note implements Serializable {

    private int noteId;
    private String note;
    private int courseId;

    public Note() { }

    public Note(String note, int courseId) {
        this.note = note;
        this.courseId = courseId;
    }

    public Note(int noteId, String note, int courseId) {
        this.noteId = noteId;
        this.note = note;
        this.courseId = courseId;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int id) { this.noteId = id; }

    public int getCourseId() {
        return courseId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
