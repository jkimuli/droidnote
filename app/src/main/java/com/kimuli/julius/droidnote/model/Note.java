package com.kimuli.julius.droidnote.model;


/** This POJO represents a single note that can be saved by a given
 * user in the database
 */

public class Note {

    private String mUserId;
    private String mTitle;
    private String mContent;
    private String mCreatedAt;

    public Note(){}

    public Note(String userId,String title,String content,String my_date){
        this.mUserId = userId;
        this.mTitle = title;
        this.mContent=content;
        this.mCreatedAt = my_date;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        this.mUserId = userId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        this.mContent = content;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String myDate) {
        this.mCreatedAt = myDate;
    }
}
