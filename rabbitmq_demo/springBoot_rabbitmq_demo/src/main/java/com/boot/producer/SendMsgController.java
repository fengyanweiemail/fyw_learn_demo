package com.boot.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fyw on 2020/6/2.
 */
@RestController
@RequestMapping("send")
public class SendMsgController {
    @Autowired
    SendMsg sendMessage;

    @RequestMapping("/msg")
    public Map msg(){
        String msg = "消息1111";
        Map m = new HashMap<String, Object>();
        m.put("abc","1");
        sendMessage.send(msg,m);
        Map returnMap = new HashMap<>();
        returnMap.put("code","0");
        return returnMap;
    };
}
