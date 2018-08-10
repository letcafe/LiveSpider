package com.letcafe.bean;

import javax.persistence.*;

@Entity
@Table(name = "huya_game_type")
public class HuYaGameType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String gid;
    String name;

    public HuYaGameType() {
    }

    public HuYaGameType(String gid, String name) {
        this.gid = gid;
        this.name = name;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "HuYaGameType{" +
                "id=" + id +
                ", gid='" + gid + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
