package com.producer.controller;

import com.producer.send.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fyw on 2020/5/31.
 */
@Controller
@RequestMapping("/send")
public class SendController {
    @Autowired
    SendMessage sendMessage;


    @RequestMapping("/msg")
    @ResponseBody
    public Map msg(){
        sendMessage.sendMessage();
        Map returnMap = new HashMap<>();
        returnMap.put("code","0");
        return returnMap;
    }

    @RequestMapping("/msg1")
    @ResponseBody
    public Map msg11(){
        sendMessage.sendMessage11();
        Map returnMap = new HashMap<>();
        returnMap.put("code","0");
        return returnMap;
    }
}
