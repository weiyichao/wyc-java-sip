package cn.mastercom.app.vmanager.play;

import cn.mastercom.app.gb28181.bean.Device;
import cn.mastercom.app.gb28181.transmit.cmd.impl.SIPCommander;
import cn.mastercom.app.storager.IVideoManagerStorager;
import cn.mastercom.app.utils.http.HttpClientUtil;
import cn.mastercom.app.utils.redis.RedisUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class PlayController {
	
	private final static Logger logger = LoggerFactory.getLogger(PlayController.class);

	private static final String request_url = "http://127.0.0.1/index/api/getMediaList?secret=035c73f7-bb6b-4889-a715-d9eb2d1925cc&schema=rtmp";

	@Autowired
	private SIPCommander cmder;
	
	@Autowired
	private IVideoManagerStorager storager;

	@Autowired
	private RedisUtil redisUtil;
	
	@GetMapping("/play/{deviceId}/{channelId}")
	public ResponseEntity<String> play(@PathVariable String deviceId, @PathVariable String channelId){
		
		Device device = storager.queryVideoDevice(deviceId);
		String ssrc = cmder.playStreamCmd(device,deviceId,channelId);
		
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("设备预览 API调用，deviceId：%s ，channelId：%s",deviceId, channelId));
			logger.debug("设备预览 API调用，ssrc："+ssrc+",ZLMedia streamId:"+Integer.toHexString(Integer.parseInt(ssrc)));
		}
		
		if(ssrc!=null) {
			JSONObject json = new JSONObject();
			json.put("ssrc", ssrc);
			return new ResponseEntity<String>(json.toString(), HttpStatus.OK);
		} else {
			logger.warn("设备预览API调用失败！");
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/play/{ssrc}/stop")
	public ResponseEntity<String> playStop(@PathVariable String ssrc){
		
		cmder.streamByeCmd(ssrc);
		
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("设备预览停止API调用，ssrc：%s", ssrc));
		}
		
		if(ssrc!=null) {
			JSONObject json = new JSONObject();
			json.put("ssrc", ssrc);
			return new ResponseEntity<String>(json.toString(), HttpStatus.OK);
		} else {
			logger.warn("设备预览停止API调用失败！");
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/play/getSSrc/{mac}/{deviceId}/{channelId}")
	public ResponseEntity<String> getSSrc(@PathVariable String mac, @PathVariable String deviceId,@PathVariable String channelId){
		String key = mac + "_" + deviceId + "_" + channelId + "_" + "current_ssrc";
		String ssrc = "";
		if (redisUtil.hasKey(key)){
			ssrc = redisUtil.get(key) == null ? "" : (String) redisUtil.get(key);

			if ("".equals(ssrc)){
				Device device = storager.queryVideoDevice(deviceId);
				ssrc = cmder.playStreamCmd(device,deviceId,channelId);
				redisUtil.set(key,ssrc);
			}
			else {
				String hexString = "0" + Integer.toHexString(Integer.parseInt(ssrc));
				hexString = hexString.toUpperCase();
				logger.info("hex:{}",hexString);
				if (!getSSrcIsOnline(hexString)){
					Device device = storager.queryVideoDevice(deviceId);
					ssrc = cmder.playStreamCmd(device,deviceId,channelId);
					redisUtil.set(key,ssrc);
				}
			}
		}
		else {
			Device device = storager.queryVideoDevice(deviceId);
			ssrc = cmder.playStreamCmd(device,deviceId,channelId);
			redisUtil.set(key,ssrc);
		}
		if(ssrc!=null) {
			JSONObject json = new JSONObject();
			json.put("ssrc", ssrc);
			return new ResponseEntity<>(json.toString(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * 判断数据源是否在线
	 * */
	public static boolean getSSrcIsOnline(String ssrc){
		String ret = HttpClientUtil.doPost(request_url,new HashMap<>(),new HashMap<>());
		JSONObject jsonObject = JSON.parseObject(ret);
		String code = jsonObject.getString("code");
		if ("0".equals(code)){
			String data = jsonObject.getString("data");
			JSONArray jsonArray = JSONArray.parseArray(data);
			if (jsonArray == null){
				return false;
			}
			for (int i = 0; i<jsonArray.size(); i ++){
				if (ssrc.equals(jsonArray.getJSONObject(i).getString("stream"))){
					return true;
				}
			}
			return false;
		}
		return false;
	}

}
