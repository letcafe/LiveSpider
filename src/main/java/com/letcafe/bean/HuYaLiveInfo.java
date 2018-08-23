package com.letcafe.bean;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
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
    private LocalDateTime startTime;
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
    private LocalDateTime live_time;
    public void setUid(Long uid) {
        this.uid = uid;
    }
    public Long getUid() {
        return uid;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }
    public Integer getSex() {
        return sex;
    }

    public void setGameFullName(String gameFullName) {
        this.gameFullName = gameFullName;
    }
    public String getGameFullName() {
        return gameFullName;
    }

    public void setGameHostName(Integer gameHostName) {
        this.gameHostName = gameHostName;
    }
    public Integer getGameHostName() {
        return gameHostName;
    }

    public void setStartTime(Long startTime) {
        this.startTime = new Timestamp(startTime * 1000).toLocalDateTime();
    }
    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }
    public Integer getActivityId() {
        return activityId;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
    public Integer getLevel() {
        return level;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
    public Integer getTotalCount() {
        return totalCount;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
    public String getRoomName() {
        return roomName;
    }

    public void setIsSecret(Boolean isSecret) {
        this.isSecret = isSecret;
    }
    public Boolean getIsSecret() {
        return isSecret;
    }

    public void setCameraOpen(Boolean cameraOpen) {
        this.cameraOpen = cameraOpen;
    }
    public Boolean getCameraOpen() {
        return cameraOpen;
    }

    public void setLiveChannel(Long liveChannel) {
        this.liveChannel = liveChannel;
    }
    public Long getLiveChannel() {
        return liveChannel;
    }

    public void setBussType(Integer bussType) {
        this.bussType = bussType;
    }
    public Integer getBussType() {
        return bussType;
    }

    public void setYyid(String yyid) {
        this.yyid = yyid;
    }
    public String getYyid() {
        return yyid;
    }

    public void setScreenshot(String screenshot) {
        this.screenshot = screenshot;
    }
    public String getScreenshot() {
        return screenshot;
    }

    public void setActivityCount(Integer activityCount) {
        this.activityCount = activityCount;
    }
    public Integer getActivityCount() {
        return activityCount;
    }

    public void setPrivateHost(String privateHost) {
        this.privateHost = privateHost;
    }
    public String getPrivateHost() {
        return privateHost;
    }

    public void setRecommendStatus(Integer recommendStatus) {
        this.recommendStatus = recommendStatus;
    }
    public Integer getRecommendStatus() {
        return recommendStatus;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
    public String getNick() {
        return nick;
    }

    public void setShortChannel(Integer shortChannel) {
        this.shortChannel = shortChannel;
    }
    public Integer getShortChannel() {
        return shortChannel;
    }

    public void setAvatar180(String avatar180) {
        this.avatar180 = avatar180;
    }
    public String getAvatar180() {
        return avatar180;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }
    public Integer getGid() {
        return gid;
    }

    public void setChannel(Long channel) {
        this.channel = channel;
    }
    public Long getChannel() {
        return channel;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
    public String getIntroduction() {
        return introduction;
    }

    public void setProfileHomeHost(String profileHomeHost) {
        this.profileHomeHost = profileHomeHost;
    }
    public String getProfileHomeHost() {
        return profileHomeHost;
    }

    public void setLiveSourceType(Integer liveSourceType) {
        this.liveSourceType = liveSourceType;
    }
    public Integer getLiveSourceType() {
        return liveSourceType;
    }

    public void setScreenType(Integer screenType) {
        this.screenType = screenType;
    }
    public Integer getScreenType() {
        return screenType;
    }

    public void setBitRate(Integer bitRate) {
        this.bitRate = bitRate;
    }
    public Integer getBitRate() {
        return bitRate;
    }

    public void setGameType(Integer gameType) {
        this.gameType = gameType;
    }
    public Integer getGameType() {
        return gameType;
    }

    public void setAttendeeCount(Integer attendeeCount) {
        this.attendeeCount = attendeeCount;
    }
    public Integer getAttendeeCount() {
        return attendeeCount;
    }

    public void setMultiStreamFlag(String multiStreamFlag) {
        this.multiStreamFlag = multiStreamFlag;
    }
    public String getMultiStreamFlag() {
        return multiStreamFlag;
    }

    public void setCodecType(Integer codecType) {
        this.codecType = codecType;
    }
    public Integer getCodecType() {
        return codecType;
    }

    public void setLiveCompatibleFlag(String liveCompatibleFlag) {
        this.liveCompatibleFlag = liveCompatibleFlag;
    }
    public String getLiveCompatibleFlag() {
        return liveCompatibleFlag;
    }

    public void setProfileRoom(Integer profileRoom) {
        this.profileRoom = profileRoom;
    }
    public Integer getProfileRoom() {
        return profileRoom;
    }

    public void setLiveId(Long liveId) {
        this.liveId = liveId;
    }
    public Long getLiveId() {
        return liveId;
    }

    public void setRankLabel(String rankLabel) {
        this.rankLabel = rankLabel;
    }
    public String getRankLabel() {
        return rankLabel;
    }

    public void setLiveHost(String liveHost) {
        this.liveHost = liveHost;
    }
    public String getLiveHost() {
        return liveHost;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public String getAvatar() {
        return avatar;
    }

    public void setGame(String game) {
        this.game = game;
    }
    public String getGame() {
        return game;
    }

    public void setUser_count(Integer user_count) {
        this.user_count = user_count;
    }
    public Integer getUser_count() {
        return user_count;
    }

    public void setLive_time(Long live_time) {
        this.live_time = new Timestamp(live_time * 1000).toLocalDateTime();

    }
    public LocalDateTime getLive_time() {
        return live_time;
    }

    @Override
    public String toString() {
        return "HuYaLiveInfo{" +
                "uid='" + uid + '\'' +
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
                ", channel='" + channel + '\'' +
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
