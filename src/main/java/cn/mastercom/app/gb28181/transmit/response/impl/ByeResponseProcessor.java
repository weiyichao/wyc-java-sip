package cn.mastercom.app.gb28181.transmit.response.impl;


import cn.mastercom.app.conf.SipConfig;
import cn.mastercom.app.gb28181.SipLayer;
import cn.mastercom.app.gb28181.transmit.response.ISIPResponseProcessor;
import org.springframework.stereotype.Component;

import javax.sip.ResponseEvent;

/**
 * @Description: BYE请求响应器
 * @author: songww
 * @date:   2020年5月3日 下午5:32:05     
 */
@Component
public class ByeResponseProcessor implements ISIPResponseProcessor {

	/**   
	 * 处理BYE响应
	 * 
	 * @param evt
	 * @param layer
	 * @param config    
	 */  
	@Override
	public void process(ResponseEvent evt, SipLayer layer, SipConfig config) {
		// TODO Auto-generated method stub
		
	}

}
