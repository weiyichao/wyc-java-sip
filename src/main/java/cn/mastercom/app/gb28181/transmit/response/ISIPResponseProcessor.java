package cn.mastercom.app.gb28181.transmit.response;


import cn.mastercom.app.conf.SipConfig;
import cn.mastercom.app.gb28181.SipLayer;

import javax.sip.ResponseEvent;
import java.text.ParseException;

/**
 * @Description:处理接收IPCamera发来的SIP协议响应消息
 * @author: songww
 * @date:   2020年5月3日 下午4:42:22     
 */
public interface ISIPResponseProcessor {

	void process(ResponseEvent evt, SipLayer layer, SipConfig config) throws ParseException;

}
