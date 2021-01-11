package cn.mastercom.app.gb28181.transmit.request;


import cn.mastercom.app.gb28181.SipLayer;

import javax.sip.RequestEvent;

/**    
 * @Description:处理接收IPCamera发来的SIP协议请求消息
 * @author: songww
 * @date:   2020年5月3日 下午4:42:22     
 */
public interface ISIPRequestProcessor {

	void process(RequestEvent evt, SipLayer layer);

}
