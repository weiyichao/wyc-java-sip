package cn.mastercom.app.storager;

import cn.mastercom.app.conf.VManagerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**    
 * @Description:视频设备数据存储工厂，根据存储策略，返回对应的存储器
 * @author: songww
 * @date:   2020年5月6日 下午2:15:16     
 */
@Component
public class VideoManagerStoragerFactory {
	
	@Autowired
	private VManagerConfig vmConfig;

	@Autowired
	private IVideoManagerStorager jdbcStorager;
	
	@Autowired
	private IVideoManagerStorager redisStorager;
	
	@Bean("storager")
	public IVideoManagerStorager getStorager() {
		if ("redis".equals(vmConfig.getDatabase().toLowerCase())) {
			return redisStorager;
		} else  if ("jdbc".equals(vmConfig.getDatabase().toLowerCase())) {
			return jdbcStorager;
		}
		return redisStorager;
	}
	
}
