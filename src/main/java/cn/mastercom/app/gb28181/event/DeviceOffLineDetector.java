package cn.mastercom.app.gb28181.event;


import cn.mastercom.app.common.VideoManagerConstants;
import cn.mastercom.app.utils.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description:设备离在线状态检测器，用于检测设备状态
 * @author: songww
 * @date:   2020年5月13日 下午2:40:29     
 */
@Component
public class DeviceOffLineDetector {

	@Autowired
    private RedisUtil redis;
	
	public boolean isOnline(String deviceId) {
		String key = VideoManagerConstants.KEEPLIVEKEY_PREFIX + deviceId;
		return redis.hasKey(key);
	}
}
