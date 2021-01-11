package cn.mastercom.app.gb28181.transmit.request.impl;


import cn.mastercom.app.gb28181.SipLayer;
import cn.mastercom.app.gb28181.transmit.request.ISIPRequestProcessor;
import org.springframework.stereotype.Component;

import javax.sip.RequestEvent;

/**
 * @Description:暂不支持的消息请求处理器
 * @author: songww
 * @date:   2020年5月3日 下午5:32:59     
 */
@Component
public class OtherRequestProcessor implements ISIPRequestProcessor {

	/**   
	 * <p>Title: process</p>   
	 * <p>Description: </p>   
	 * @param evt
	 * @param layer
	 * @param transaction
	 * @param config    
	 */  
	@Override
	public void process(RequestEvent evt, SipLayer layer) {
		System.out.println("no support the method! Method:" + evt.getRequest().getMethod());
	}

}
