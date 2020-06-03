package com.boot.delayQueue;

import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fyw on 2020/6/3.
 */
@RestController
@RequestMapping("/delaySend")
public class DelayController {

    @Autowired
    DelayP delayP;
    @RequestMapping("/msg")
    public Map msg(){
        for(int i=0;i<5;i++){
            String msg = "第一个延迟消息。。。"+i;
            Map m = new HashMap<String, Object>();
            m.put("x-delay",1000*i);
            delayP.send(msg,m);
        }



        Map returnMap = new HashMap<>();
        returnMap.put("code","0");
        return returnMap;
    };
}
