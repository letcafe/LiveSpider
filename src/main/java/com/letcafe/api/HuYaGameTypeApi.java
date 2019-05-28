package com.letcafe.api;

import com.letcafe.bean.HuYaGameType;
import com.letcafe.bean.RestMessage;
import com.letcafe.service.HuYaGameTypeService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Api(value = "物联网设备的相关信息API")
@RestController
@RequestMapping("/api/v1/huYaGameType")
public class HuYaGameTypeApi {

    private static Logger logger = LoggerFactory.getLogger(HuYaGameTypeApi.class);

    private HuYaGameTypeService huYaGameTypeService;

    @Autowired
    public HuYaGameTypeApi(HuYaGameTypeService huYaGameTypeService) {
        this.huYaGameTypeService = huYaGameTypeService;
    }


    @ApiOperation(value = "获取所有游戏Id", httpMethod = "GET", produces = "application/json")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success: get successfully"),
            @ApiResponse(code = 400, message = "error: your id don`t exist any device"),
            @ApiResponse(code = 401, message = "error: db error")
    })
    @RequestMapping(value = "queryDeviceById", method = RequestMethod.GET, produces = "application/json")
    public RestMessage<List<Integer>> listGid() {
        RestMessage<List<Integer>> restMessage = new RestMessage<>();
        return restMessage.setCode(200).setMsg("success: get successfully").setData(huYaGameTypeService.listAllGid());
    }

    @ApiOperation(value = "获得游戏数量", httpMethod = "GET", produces = "application/json")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success: get successfully"),
            @ApiResponse(code = 400, message = "error: your id don`t exist any device"),
            @ApiResponse(code = 401, message = "error: db error")
    })
    @RequestMapping(value = "count", method = RequestMethod.GET, produces = "application/json")
    public RestMessage<Long> count() {
        RestMessage<Long> restMessage = new RestMessage<>();
        return restMessage.setCode(200).setMsg("success: get successfully").setData(huYaGameTypeService.count());
    }

    @ApiOperation(value = "插入一个游戏", httpMethod = "GET", produces = "application/json")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success: get successfully"),
            @ApiResponse(code = 400, message = "error: your id don`t exist any device"),
            @ApiResponse(code = 401, message = "error: db error")
    })
    @RequestMapping(value = "saveTest", method = RequestMethod.GET, produces = "application/json")
    public RestMessage<Long> saveTest() {
        RestMessage<Long> restMessage = new RestMessage<>();
        List<HuYaGameType> huYaGameTypes = new ArrayList<>();
        huYaGameTypes.add(new HuYaGameType(new Random().nextInt(5), "haha"));
        huYaGameTypeService.saveOrUpdateList(huYaGameTypes);
        return restMessage.setCode(200).setMsg("success: get successfully").setData(null);
    }

}
