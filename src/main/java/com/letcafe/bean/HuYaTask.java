package com.letcafe.bean;


import javax.persistence.*;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTask_id() {
        return task_id;
    }

    public void setTask_id(Integer task_id) {
        this.task_id = task_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public Integer getExper() {
        return exper;
    }

    public void setExper(Integer exper) {
        this.exper = exper;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getAwardPrize() {
        return awardPrize;
    }

    public void setAwardPrize(String awardPrize) {
        this.awardPrize = awardPrize;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public Integer getProgressMode() {
        return progressMode;
    }

    public void setProgressMode(Integer progressMode) {
        this.progressMode = progressMode;
    }

    public Integer gettPrizeId() {
        return tPrizeId;
    }

    public void settPrizeId(Integer tPrizeId) {
        this.tPrizeId = tPrizeId;
    }

    public Integer getTargetLevel() {
        return targetLevel;
    }

    public void setTargetLevel(Integer targetLevel) {
        this.targetLevel = targetLevel;
    }

    public Integer getSubTaskTargetLevel() {
        return subTaskTargetLevel;
    }

    public void setSubTaskTargetLevel(Integer subTaskTargetLevel) {
        this.subTaskTargetLevel = subTaskTargetLevel;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public int compareTo(HuYaTask o) {
        return this.task_id > o.task_id ? 1 : this.task_id.equals(o.task_id)? 0 : -1;
    }

    @Override
    public String toString() {
        return "HuYaTask{" +
                "id=" + id +
                ", task_id=" + task_id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", enable='" + enable + '\'' +
                ", exper=" + exper +
                ", icon='" + icon + '\'' +
                ", className='" + className + '\'' +
                ", awardPrize='" + awardPrize + '\'' +
                ", progress=" + progress +
                ", progressMode=" + progressMode +
                ", tPrizeId=" + tPrizeId +
                ", targetLevel=" + targetLevel +
                ", subTaskTargetLevel=" + subTaskTargetLevel +
                ", type=" + type +
                '}';
    }
}
