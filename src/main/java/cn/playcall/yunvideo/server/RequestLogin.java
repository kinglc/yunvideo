package cn.playcall.yunvideo.server;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

public class RequestLogin {

    private static String appId = "wxf33c27bb4516dba5";
    private static String secret = "c0b5fd7775b8f5576c10390777b91361";

    public static JSONObject getLoginOpenId(String code){
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+appId+"&secret="+secret+"&js_code="+code+"&grant_type=authorization_code";
        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();
        String getJson = restTemplate.getForEntity(url,String.class).getBody();
        JSONObject resultJson = JSONObject.parseObject(getJson);
        return resultJson;
    }

}
