package com.letcafe.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "huya_live_info")
public class HuYaLiveInfo {

    @Id
    private Long uid;
    private Integer sex;
    @Column(name = "game_full_name")
    private String gameFullName;
    @Column(name = "game_host_name")
    private Integer gameHostName;
    @Column(name = "start_time")
    private Long startTime;
    @Column(name = "activity_id")
    private Integer activityId;
    private Integer level;
    @Column(name = "total_count")
    private Integer totalCount;
    @Column(name = "room_name")
    private String roomName;
    @Column(name = "is_secret")
    private Boolean isSecret;
    @Column(name = "camera_open")
    private Boolean cameraOpen;
    @Column(name = "live_channel")
    private Long liveChannel;
    @Column(name = "buss_type")
    private Integer bussType;
    private String yyid;
    private String screenshot;
    @Column(name = "activity_count")
    private Integer activityCount;
    @Column(name = "private_host")
    private String privateHost;
    @Column(name = "recommend_status")
    private Integer recommendStatus;
    private String nick;
    @Column(name = "short_channel")
    private Integer shortChannel;
    private String avatar180;
    private Integer gid;
    private Long channel;
    private String introduction;
    @Column(name = "profile_home_host")
    private String profileHomeHost;
    @Column(name = "live_source_type")
    private Integer liveSourceType;
    @Column(name = "screen_type")
    private Integer screenType;
    @Column(name = "bit_rate")
    private Integer bitRate;
    @Column(name = "game_type")
    private Integer gameType;
    @Column(name = "attendee_count")
    private Integer attendeeCount;
    @Column(name = "multi_stream_flag")
    private String multiStreamFlag;
    @Column(name = "codec_type")
    private Integer codecType;
    @Column(name = "live_compatible_flag")
    private String liveCompatibleFlag;
    @Column(name = "profile_room")
    private Integer profileRoom;
    @Column(name = "live_id")
    private Long liveId;
    @Column(name = "rank_label")
    private String rankLabel;
    @Column(name = "live_host")
    private String liveHost;
    private String avatar;
    private String game;
    private Integer user_count;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Long live_time;

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

    public String getGameFullName() {
        return gameFullName;
    }

    public void setGameFullName(String gameFullName) {
        this.gameFullName = gameFullName;
    }

    public Integer getGameHostName() {
        return gameHostName;
    }

    public void setGameHostName(Integer gameHostName) {
        this.gameHostName = gameHostName;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Boolean getSecret() {
        return isSecret;
    }

    public void setSecret(Boolean secret) {
        isSecret = secret;
    }

    public Boolean getCameraOpen() {
        return cameraOpen;
    }

    public void setCameraOpen(Boolean cameraOpen) {
        this.cameraOpen = cameraOpen;
    }

    public Long getLiveChannel() {
        return liveChannel;
    }

    public void setLiveChannel(Long liveChannel) {
        this.liveChannel = liveChannel;
    }

    public Integer getBussType() {
        return bussType;
    }

    public void setBussType(Integer bussType) {
        this.bussType = bussType;
    }

    public String getYyid() {
        return yyid;
    }

    public void setYyid(String yyid) {
        this.yyid = yyid;
    }

    public String getScreenshot() {
        return screenshot;
    }

    public void setScreenshot(String screenshot) {
        this.screenshot = screenshot;
    }

    public Integer getActivityCount() {
        return activityCount;
    }

    public void setActivityCount(Integer activityCount) {
        this.activityCount = activityCount;
    }

    public String getPrivateHost() {
        return privateHost;
    }

    public void setPrivateHost(String privateHost) {
        this.privateHost = privateHost;
    }

    public Integer getRecommendStatus() {
        return recommendStatus;
    }

    public void setRecommendStatus(Integer recommendStatus) {
        this.recommendStatus = recommendStatus;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Integer getShortChannel() {
        return shortChannel;
    }

    public void setShortChannel(Integer shortChannel) {
        this.shortChannel = shortChannel;
    }

    public String getAvatar180() {
        return avatar180;
    }

    public void setAvatar180(String avatar180) {
        this.avatar180 = avatar180;
    }

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public Long getChannel() {
        return channel;
    }

    public void setChannel(Long channel) {
        this.channel = channel;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getProfileHomeHost() {
        return profileHomeHost;
    }

    public void setProfileHomeHost(String profileHomeHost) {
        this.profileHomeHost = profileHomeHost;
    }

    public Integer getLiveSourceType() {
        return liveSourceType;
    }

    public void setLiveSourceType(Integer liveSourceType) {
        this.liveSourceType = liveSourceType;
    }

    public Integer getScreenType() {
        return screenType;
    }

    public void setScreenType(Integer screenType) {
        this.screenType = screenType;
    }

    public Integer getBitRate() {
        return bitRate;
    }

    public void setBitRate(Integer bitRate) {
        this.bitRate = bitRate;
    }

    public Integer getGameType() {
        return gameType;
    }

    public void setGameType(Integer gameType) {
        this.gameType = gameType;
    }

    public Integer getAttendeeCount() {
        return attendeeCount;
    }

    public void setAttendeeCount(Integer attendeeCount) {
        this.attendeeCount = attendeeCount;
    }

    public String getMultiStreamFlag() {
        return multiStreamFlag;
    }

    public void setMultiStreamFlag(String multiStreamFlag) {
        this.multiStreamFlag = multiStreamFlag;
    }

    public Integer getCodecType() {
        return codecType;
    }

    public void setCodecType(Integer codecType) {
        this.codecType = codecType;
    }

    public String getLiveCompatibleFlag() {
        return liveCompatibleFlag;
    }

    public void setLiveCompatibleFlag(String liveCompatibleFlag) {
        this.liveCompatibleFlag = liveCompatibleFlag;
    }

    public Integer getProfileRoom() {
        return profileRoom;
    }

    public void setProfileRoom(Integer profileRoom) {
        this.profileRoom = profileRoom;
    }

    public Long getLiveId() {
        return liveId;
    }

    public void setLiveId(Long liveId) {
        this.liveId = liveId;
    }

    public String getRankLabel() {
        return rankLabel;
    }

    public void setRankLabel(String rankLabel) {
        this.rankLabel = rankLabel;
    }

    public String getLiveHost() {
        return liveHost;
    }

    public void setLiveHost(String liveHost) {
        this.liveHost = liveHost;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public Integer getUser_count() {
        return user_count;
    }

    public void setUser_count(Integer user_count) {
        this.user_count = user_count;
    }

    public Long getLive_time() {
        return live_time;
    }

    public void setLive_time(Long live_time) {
        this.live_time = live_time;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }


    @Override
    public String toString() {
        return "HuYaLiveInfo{" +
                "uid=" + uid +
                ", sex=" + sex +
                ", gameFullName='" + gameFullName + '\'' +
                ", gameHostName=" + gameHostName +
                ", startTime=" + startTime +
                ", activityId=" + activityId +
                ", level=" + level +
                ", totalCount=" + totalCount +
                ", roomName='" + roomName + '\'' +
                ", isSecret=" + isSecret +
                ", cameraOpen=" + cameraOpen +
                ", liveChannel=" + liveChannel +
                ", bussType=" + bussType +
                ", yyid='" + yyid + '\'' +
                ", screenshot='" + screenshot + '\'' +
                ", activityCount=" + activityCount +
                ", privateHost='" + privateHost + '\'' +
                ", recommendStatus=" + recommendStatus +
                ", nick='" + nick + '\'' +
                ", shortChannel=" + shortChannel +
                ", avatar180='" + avatar180 + '\'' +
                ", gid=" + gid +
                ", channel=" + channel +
                ", introduction='" + introduction + '\'' +
                ", profileHomeHost='" + profileHomeHost + '\'' +
                ", liveSourceType=" + liveSourceType +
                ", screenType=" + screenType +
                ", bitRate=" + bitRate +
                ", gameType=" + gameType +
                ", attendeeCount=" + attendeeCount +
                ", multiStreamFlag='" + multiStreamFlag + '\'' +
                ", codecType=" + codecType +
                ", liveCompatibleFlag='" + liveCompatibleFlag + '\'' +
                ", profileRoom=" + profileRoom +
                ", liveId=" + liveId +
                ", rankLabel='" + rankLabel + '\'' +
                ", liveHost='" + liveHost + '\'' +
                ", avatar='" + avatar + '\'' +
                ", game='" + game + '\'' +
                ", user_count=" + user_count +
                ", live_time=" + live_time +
                '}';
    }
}
