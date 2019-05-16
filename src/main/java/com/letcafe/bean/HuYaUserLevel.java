package com.letcafe.bean;

import javax.persistence.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Entity
@Table(name = "huya_user_level")
public class HuYaUserLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "yy_id")
    private String yyId;
    @Column(name = "curr_level_exp")
    private String currLevelExp;
    @Column(name = "next_level_exp")
    private String nextLevelExp;
    @Column(name = "next_2_level_exp")
    private String next2LevelExp;
    @Column(name = "user_exp")
    private String userExp;
    @Column(name = "user_level")
    private Integer userLevel;
    private Date birthday;
    private Integer gender;
    private String sign;
    private String area;
    private String location;
    private String nick;
    @Column(name = "level_progress")
    private Integer levelProgress;
    @Column(name = "daily_inc_exp")
    private Integer dailyIncExp;

    public HuYaUserLevel() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getYyId() {
        return yyId;
    }

    public void setYyId(String yyId) {
        this.yyId = yyId;
    }

    public String getCurrLevelExp() {
        return currLevelExp;
    }

    public void setCurrLevelExp(String currLevelExp) {
        this.currLevelExp = currLevelExp;
    }

    public String getNextLevelExp() {
        return nextLevelExp;
    }

    public void setNextLevelExp(String nextLevelExp) {
        this.nextLevelExp = nextLevelExp;
    }

    public String getNext2LevelExp() {
        return next2LevelExp;
    }

    public void setNext2LevelExp(String next2LevelExp) {
        this.next2LevelExp = next2LevelExp;
    }

    public String getUserExp() {
        return userExp;
    }

    public void setUserExp(String userExp) {
        this.userExp = userExp;
    }

    public Integer getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Integer getLevelProgress() {
        return levelProgress;
    }

    public void setLevelProgress(Integer levelProgress) {
        this.levelProgress = levelProgress;
    }

    public Integer getDailyIncExp() {
        return dailyIncExp;
    }

    public void setDailyIncExp(Integer dailyIncExp) {
        this.dailyIncExp = dailyIncExp;
    }

    public void setBirthday(Integer birthday) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        this.birthday = new java.sql.Date(format.parse("" + birthday).getTime());
    }

    @Override
    public String toString() {
        return "HuYaUserLevel{" +
                "id=" + id +
                ", yyId='" + yyId + '\'' +
                ", currLevelExp='" + currLevelExp + '\'' +
                ", nextLevelExp='" + nextLevelExp + '\'' +
                ", next2LevelExp='" + next2LevelExp + '\'' +
                ", userExp='" + userExp + '\'' +
                ", userLevel=" + userLevel +
                ", birthday=" + birthday +
                ", gender=" + gender +
                ", sign='" + sign + '\'' +
                ", area='" + area + '\'' +
                ", location='" + location + '\'' +
                ", nick='" + nick + '\'' +
                ", levelProgress=" + levelProgress +
                ", dailyIncExp=" + dailyIncExp +
                '}';
    }
}
