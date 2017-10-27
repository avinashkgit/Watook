package com.watook.model;

import java.io.Serializable;

/**
 * Created by Avinash.Kumar on 26-Oct-17.
 */

public class ApplicationIdResponse implements Serializable{

    /**
     * category : Utilities
     * link : https://www.facebook.com/games/?app_id=1288795094521349
     * name : Watook
     * id : 1288795094521349
     */

    private String category;
    private String link;
    private String name;
    private String id;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
