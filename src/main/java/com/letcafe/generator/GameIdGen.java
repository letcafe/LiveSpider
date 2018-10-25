package com.letcafe.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class GameIdGen implements Generator<Integer> {
    private static final Logger logger = LoggerFactory.getLogger(GameIdGen.class);

    private int currentGameId = -1;

    /**
     * 1-英雄联盟, 2793-绝地求生PC版， 3203-刺激战场手游， 2-地下城与勇士
     * 4-穿越火线， 6-DOTA1, 7-DOTA2, 8-魔兽世界， 9-QQ飞车， 5-星际争霸
     */
    private int[] gameIdArray = {1, 2793, 3203, 2, 4, 6, 7, 8, 9, 5};

    @Override
    public Integer next() {
        logger.info("[Game Id Switch : Next Id] = " + gameIdArray[++ currentGameId % gameIdArray.length]);
        return gameIdArray[currentGameId % gameIdArray.length];
    }

}
