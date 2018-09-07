package com.letcafe.bean;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Data
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

    public void setBirthday(Integer birthday) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        this.birthday = new java.sql.Date(format.parse("" + birthday).getTime());
    }

}
