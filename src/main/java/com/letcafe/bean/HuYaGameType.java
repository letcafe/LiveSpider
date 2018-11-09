package com.letcafe.bean;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "huya_game_type")
public class HuYaGameType {
    @Id
    private Integer gid;
    private String name;

    public HuYaGameType(Integer gid, String name) {
        this.gid = gid;
        this.name = name;
    }

}
