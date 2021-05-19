package com.mseabraham.finalyearapp.Models;

import com.google.firebase.firestore.DocumentId;

import java.util.ArrayList;
import java.util.List;

public class LogDocument {
    @DocumentId String id;
    List<Log> data;

    public LogDocument(String id, List<Log> data) {
        this.id = id;
        this.data = data;
    }

    public LogDocument(List<Log> data) {
        this.data = data;
    }

    public LogDocument() {
        this.data = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Log> getData() {
        return data;
    }

    public void setData(List<Log> data) {
        this.data = data;
    }
}
