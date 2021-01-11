package cn.mastercom.app.vmanager.record;

import cn.mastercom.app.gb28181.bean.Device;
import cn.mastercom.app.gb28181.bean.RecordInfo;
import cn.mastercom.app.gb28181.transmit.callback.DeferredResultHolder;
import cn.mastercom.app.gb28181.transmit.cmd.impl.SIPCommander;
import cn.mastercom.app.storager.IVideoManagerStorager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class RecordController {
	
	private final static Logger logger = LoggerFactory.getLogger(RecordController.class);
	
	@Autowired
	private SIPCommander cmder;
	
	@Autowired
	private IVideoManagerStorager storager;
	
	@Autowired
	private DeferredResultHolder resultHolder;
	
	@GetMapping("/record/{deviceId}/{channelId}")
	public DeferredResult<ResponseEntity<RecordInfo>> recordinfo(@PathVariable String deviceId, @PathVariable String channelId, String startTime, String endTime){
		
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("录像信息查询 API调用，deviceId：%s ，startTime：%s， startTime：%s",deviceId, startTime, endTime));
		}
		
		Device device = storager.queryVideoDevice(deviceId);
		cmder.recordInfoQuery(device, channelId, startTime, endTime);
		DeferredResult<ResponseEntity<RecordInfo>> result = new DeferredResult<>();
		// 录像查询以channelId作为deviceId查询
		resultHolder.put(DeferredResultHolder.CALLBACK_CMD_RECORDINFO+channelId, result);
        return result;
	}
}
