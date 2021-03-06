package com.npc.cmx.criminalintent;

import java.util.Date;
import java.util.UUID;

/**
 * Created by CMX on 2016/5/23.
 */

/**
 *crime模型属性类
 * */

public class Crime {
    private UUID mId;//生成一个号称全球唯一的ID
    private String mTitle;
    private Date mDate;
    private boolean mSolved;//记录crime处理状态

    public Crime(){
//        唯一识别码 (Universally Unique Identifier)
        mId = UUID.randomUUID();
        mDate = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }
}
