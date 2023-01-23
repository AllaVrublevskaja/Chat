package com.example.chat;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements Serializable {
    private String text;
    private Date date;
    private String nickName;
    private SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

    public Message(String text,String nickName) {
        this.text = text;
        date = new Date();
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        return nickName+" "+format.format(date) + " : "+ text;
    }
}