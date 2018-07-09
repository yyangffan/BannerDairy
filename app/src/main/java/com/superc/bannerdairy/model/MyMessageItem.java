package com.superc.bannerdairy.model;

import com.google.gson.annotations.SerializedName;

/**
 * 创建日期：2017/11/9 on 16:31
 * 描述：
 * 作者：郭士超
 * QQ：1169380200
 */

public class MyMessageItem {

    /**
     * notice_id : 1
     * notice_title : 梵蒂冈回复
     * notice_content : 法国队额地方的三个地方个
     * user_id : 23
     * notice_read : 0
     * creator : null
     * created : 1970-01-01 08:00:00
     * modifier : null
     * modified : 1970-01-01 08:00:00
     * delete_time : null
     */

    @SerializedName("notice_id")
    private String noticeId;
    @SerializedName("notice_title")
    private String noticeTitle;
    @SerializedName("notice_content")
    private String noticeContent;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("notice_read")
    private String noticeRead;
    @SerializedName("creator")
    private Object creator;
    @SerializedName("created")
    private String created;
    @SerializedName("modifier")
    private Object modifier;
    @SerializedName("modified")
    private String modified;
    @SerializedName("delete_time")
    private Object deleteTime;

    public boolean getRead() {
        if (noticeRead.equals("1")) {/*未读*/
            return false;
        }
        return true;

    }

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNoticeRead() {
        return noticeRead;
    }

    public void setNoticeRead(String noticeRead) {
        this.noticeRead = noticeRead;
    }

    public Object getCreator() {
        return creator;
    }

    public void setCreator(Object creator) {
        this.creator = creator;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Object getModifier() {
        return modifier;
    }

    public void setModifier(Object modifier) {
        this.modifier = modifier;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public Object getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Object deleteTime) {
        this.deleteTime = deleteTime;
    }
}
