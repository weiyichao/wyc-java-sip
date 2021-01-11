package cn.mastercom.app.gb28181.transmit.request.impl;


import cn.mastercom.app.gb28181.SipLayer;
import cn.mastercom.app.gb28181.transmit.request.ISIPRequestProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sip.RequestEvent;

/**
 * @Description:ACK请求处理器  
 * @author: songww
 * @date:   2020年5月3日 下午5:31:45     
 */
@Component
public class AckRequestProcessor implements ISIPRequestProcessor {

	@Autowired
	private SipLayer sipLayer;

	private final static Logger logger = LoggerFactory.getLogger(AckRequestProcessor.class);

	/**   
	 * 处理  ACK请求
	 * 
	 * @param evt
	 * @param layer
	 */  
	@Override
	public void process(RequestEvent evt, SipLayer layer) {
		/*Request request = evt.getRequest();
		Dialog dialog = evt.getDialog();
		try {
			Request ackRequest = null;
			CSeq csReq = (CSeq) request.getHeader(CSeq.NAME);
			*//*CSeqHeader csReq = (CSeqHeader) request.getHeader(CSeqHeader.NAME);*//*
			ackRequest = dialog.createAck(csReq.getSeqNumber());
			dialog.sendAck(ackRequest);
			System.out.println("send ack to callee:" + ackRequest.toString());
		} catch (Exception e) {
			System.out.println(e);
		}*/
		
	}

}
