package com.letcafe.bean;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "huya_tasks")
public class HuYaTask implements Comparable<HuYaTask>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer task_id;
    private String name;
    private String description;
    private String enable;
    private Integer exper;
    private String icon;
    @Column(name = "class_name")
    private String className;
    @Column(name = "award_prize")
    private String awardPrize;
    private Integer progress;
    @Column(name = "progress_mode")
    private Integer progressMode;
    @Column(name = "t_prize_id")
    private Integer tPrizeId;
    @Column(name = "target_level")
    private Integer targetLevel;
    @Column(name = "sub_task_target_level")
    private Integer subTaskTargetLevel;
    private Integer type;

    public HuYaTask(Integer task_id, String name, String description, String enable, Integer exper, String icon, String className, String awardPrize, Integer progress, Integer progressMode, Integer targetLevel, Integer type) {
        this.task_id = task_id;
        this.name = name;
        this.description = description;
        this.enable = enable;
        this.exper = exper;
        this.icon = icon;
        this.className = className;
        this.awardPrize = awardPrize;
        this.progress = progress;
        this.progressMode = progressMode;
        this.targetLevel = targetLevel;
        this.type = type;
    }


    @Override
    public int compareTo(HuYaTask o) {
        return this.task_id > o.task_id ? 1 : this.task_id.equals(o.task_id)? 0 : -1;
    }
}
