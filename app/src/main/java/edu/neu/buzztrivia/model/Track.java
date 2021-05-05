package edu.neu.buzztrivia.model;

import java.io.Serializable;

public class Track implements Serializable {
    int duration;
    String artist;
    String releaseDate;
    String rating;

    private String id;
    private String name;

    public Track(String id, String name) {
        this.name = name;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
