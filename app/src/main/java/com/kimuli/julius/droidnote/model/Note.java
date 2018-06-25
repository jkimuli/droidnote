package com.kimuli.julius.droidnote.model;

import java.util.Date;


/** This POJO represents a single note that can be saved by a given
 * user in the database
 */

public class Note {

    private String userId;
    private String title;
    private String content;
    private Date timestamp;


    public Note(){}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
