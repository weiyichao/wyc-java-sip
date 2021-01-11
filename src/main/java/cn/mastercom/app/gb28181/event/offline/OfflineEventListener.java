package cn.mastercom.app.gb28181.event.offline;


import cn.mastercom.app.common.VideoManagerConstants;
import cn.mastercom.app.gb28181.transmit.request.impl.RegisterRequestProcessor;
import cn.mastercom.app.storager.IVideoManagerStorager;
import cn.mastercom.app.utils.redis.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @Description: 离线事件监听器，监听到离线后，修改设备离在线状态。 设备离线有两个来源：
 *               1、设备主动注销，发送注销指令，{@link RegisterRequestProcessor}
 *               2、设备未知原因离线，心跳超时,{@link OfflineEventListener}
 * @author: songww
 * @date: 2020年5月6日 下午1:51:23
 */
@Component
public class OfflineEventListener implements ApplicationListener<OfflineEvent> {

	private final static Logger logger = LoggerFactory.getLogger(OfflineEventListener.class);
	
	@Autowired
	private IVideoManagerStorager storager;
	
	@Autowired
    private RedisUtil redis;

	@Override
	public void onApplicationEvent(OfflineEvent event) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("设备离线事件触发，deviceId：" + event.getDeviceId() + ",from:" + event.getFrom());
		}

		String key = VideoManagerConstants.KEEPLIVEKEY_PREFIX + event.getDeviceId();

		switch (event.getFrom()) {
		// 心跳超时触发的离线事件，说明redis中已删除，无需处理
		case VideoManagerConstants.EVENT_OUTLINE_TIMEOUT:
			break;
		// 设备主动注销触发的离线事件，需要删除redis中的超时监听
		case VideoManagerConstants.EVENT_OUTLINE_UNREGISTER:
			redis.del(key);
			break;
		default:
			boolean exist = redis.hasKey(key);
			if (exist) {
				redis.del(key);
			}
		}

		// 处理离线监听
		storager.outline(event.getDeviceId());
	}
}
