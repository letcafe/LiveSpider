package com.letcafe.bean;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "huya_game_type")
public class HuYaGameType {
    @Id
    private String gid;
    private String name;

    public HuYaGameType() {
    }

    public HuYaGameType(String gid, String name) {
        this.gid = gid;
        this.name = name;
    }

}
