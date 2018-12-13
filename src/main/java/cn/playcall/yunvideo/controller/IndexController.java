package cn.playcall.yunvideo.controller;

import cn.playcall.yunvideo.dao.UserDao;
import cn.playcall.yunvideo.entity.UserInfo;
import cn.playcall.yunvideo.server.RequestLogin;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@EnableRedisHttpSession
@CrossOrigin
@Controller
@RequestMapping
public class IndexController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private StringRedisTemplate redisClient;

    @RequestMapping(value = "/login")
    public ResponseEntity<JSONObject> login(HttpServletRequest request, @RequestBody JSONObject codeJson){
        JSONObject resultJson = new JSONObject();
        System.out.println(codeJson);

        JSONObject requestJson = RequestLogin.getLoginOpenId(codeJson.getString("code"));
        System.out.println(requestJson);

        if (requestJson.getString("openid") == null){
            resultJson.put("code","001");
            resultJson.put("desc","授权失败");
            return new ResponseEntity<JSONObject>(resultJson, HttpStatus.OK);
        }

        UserInfo userInfo = userDao.findByOpenId(requestJson.getString("openid"));
        if (userInfo == null){
            userInfo = new UserInfo();
            userInfo.setOpenId(requestJson.getString("openid"));
            userDao.save(userInfo);
        }

        String sessionKey = requestJson.getString("session_key");

        redisClient.opsForValue().set(sessionKey,userInfo.getOpenId(),60*10, TimeUnit.SECONDS);
        resultJson.put("sessionId",sessionKey);
        resultJson.put("code","000");
        resultJson.put("desc","登录成功");

        return new ResponseEntity<JSONObject>(resultJson, HttpStatus.OK);
    }
}
