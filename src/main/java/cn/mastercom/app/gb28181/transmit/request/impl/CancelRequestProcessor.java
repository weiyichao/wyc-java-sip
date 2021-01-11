package cn.mastercom.app.gb28181.transmit.request.impl;


import cn.mastercom.app.gb28181.SipLayer;
import cn.mastercom.app.gb28181.transmit.request.ISIPRequestProcessor;
import org.springframework.stereotype.Component;

import javax.sip.RequestEvent;

/**
 * @Description:CANCEL请求处理器
 * @author: songww
 * @date:   2020年5月3日 下午5:32:23     
 */
@Component
public class CancelRequestProcessor implements ISIPRequestProcessor {

	/**   
	 * 处理CANCEL请求
	 *  
	 * @param evt
	 * @param layer
	 * @param transaction
	 * @param config    
	 */  
	@Override
	public void process(RequestEvent evt, SipLayer layer) {
		// TODO Auto-generated method stub
		
	}

}
