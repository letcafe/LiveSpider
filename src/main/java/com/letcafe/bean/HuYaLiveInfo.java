package com.letcafe.bean;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
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

    public void setStartTime(Long startTime) {
        this.startTime = new Timestamp(startTime * 1000).toLocalDateTime();
    }

    public void setLive_time(Long live_time) {
        this.live_time = new Timestamp(live_time * 1000).toLocalDateTime();

    }

}
