package com.letcafe.generator;

import com.letcafe.bean.HuYaLiveInfo;
import com.letcafe.controller.huya.LiveInfoGetter;

import java.util.List;

public class HuYaLiveInfoGen implements Generator<HuYaLiveInfo>{

    // 通过游戏ID以及每次检索的list索引唯一标记直播间
    private int roomIndexInGame;
    private LiveInfoGetter liveInfoGetter;
    private List<HuYaLiveInfo> liveInfoList;
    private GameIdGen gameIdGen;

    public HuYaLiveInfoGen(GameIdGen gameIdGen) {
        liveInfoGetter = new LiveInfoGetter();
        liveInfoList = liveInfoGetter.listHuYaLiveByGid(gameIdGen.next());
        this.gameIdGen = gameIdGen;
    }

    @Override
    public HuYaLiveInfo next() {
        if (roomIndexInGame >= liveInfoList.size()) {
            liveInfoList = liveInfoGetter.listHuYaLiveByGid(gameIdGen.next());
            roomIndexInGame = 0;
        }
        // take LOL for example, get LOL live list, if come across exception,log then recursive
        HuYaLiveInfo live = liveInfoList.get(roomIndexInGame);
        roomIndexInGame ++;
        return live;
    }
}
