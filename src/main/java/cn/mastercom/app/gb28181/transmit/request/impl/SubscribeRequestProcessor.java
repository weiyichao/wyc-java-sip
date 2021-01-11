package cn.mastercom.app.gb28181.transmit.request.impl;

import cn.mastercom.app.gb28181.SipLayer;
import cn.mastercom.app.gb28181.transmit.request.ISIPRequestProcessor;
import org.springframework.stereotype.Component;

import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.header.ExpiresHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;
import java.text.ParseException;

/**    
 * @Description:SUBSCRIBE请求处理器
 * @author: songww
 * @date:   2020年5月3日 下午5:31:20     
 */
@Component
public class SubscribeRequestProcessor implements ISIPRequestProcessor {

	/**   
	 * 处理SUBSCRIBE请求  
	 * 
	 * @param evt
	 * @param layer
	 * @param transaction
	 * @param config    
	 */
	@Override
	public void process(RequestEvent evt, SipLayer layer) {
		Request request = evt.getRequest();

		try {
			Response response = null;
			response = layer.getMessageFactory().createResponse(200, request);
			if (response != null) {
				ExpiresHeader expireHeader = layer.getHeaderFactory().createExpiresHeader(30);
				response.setExpires(expireHeader);
			}
			System.out.println("response : " + response.toString());
			ServerTransaction transaction = layer.getServerTransaction(evt);
			if (transaction != null) {
				transaction.sendResponse(response);
				transaction.terminate();
			} else {
				System.out.println("processRequest serverTransactionId is null.");
			}

		} catch (ParseException e) {
			e.printStackTrace();
		} catch (SipException e) {
			e.printStackTrace();
		} catch (InvalidArgumentException e) {
			e.printStackTrace();
		}
		
	}

}
