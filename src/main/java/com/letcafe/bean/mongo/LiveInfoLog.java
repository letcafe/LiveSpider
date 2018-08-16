package com.letcafe.bean.mongo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;

@Document
public class LiveInfoLog {

    private Long uid;
    private Integer sex;
    private Integer activityCount;
    private String nick;
    private String gid;
    private Integer attendeeCount;
    private String liveHost;
    private String game;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Timestamp liveTime;

    public LiveInfoLog() {
    }

    public LiveInfoLog(Long uid, Integer sex, Integer activityCount, String nick, String gid, Integer attendeeCount, String liveHost, String game, Timestamp liveTime) {
        this.uid = uid;
        this.sex = sex;
        this.activityCount = activityCount;
        this.nick = nick;
        this.gid = gid;
        this.attendeeCount = attendeeCount;
        this.liveHost = liveHost;
        this.game = game;
        this.liveTime = liveTime;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getActivityCount() {
        return activityCount;
    }

    public void setActivityCount(Integer activityCount) {
        this.activityCount = activityCount;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public Integer getAttendeeCount() {
        return attendeeCount;
    }

    public void setAttendeeCount(Integer attendeeCount) {
        this.attendeeCount = attendeeCount;
    }

    public String getLiveHost() {
        return liveHost;
    }

    public void setLiveHost(String liveHost) {
        this.liveHost = liveHost;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public Timestamp getLiveTime() {
        return liveTime;
    }

    public void setLiveTime(Timestamp liveTime) {
        this.liveTime = liveTime;
    }

    @Override
    public String toString() {
        return "LiveInfoLog{" +
                "uid=" + uid +
                ", sex=" + sex +
                ", activityCount=" + activityCount +
                ", nick='" + nick + '\'' +
                ", gid='" + gid + '\'' +
                ", attendeeCount=" + attendeeCount +
                ", liveHost='" + liveHost + '\'' +
                ", game='" + game + '\'' +
                ", liveTime=" + liveTime +
                '}';
    }
}
