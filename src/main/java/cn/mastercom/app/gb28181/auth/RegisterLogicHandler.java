package cn.mastercom.app.gb28181.auth;

import cn.mastercom.app.gb28181.bean.Device;
import cn.mastercom.app.gb28181.transmit.cmd.impl.SIPCommander;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**    
 * @Description:注册逻辑处理，当设备注册后触发逻辑。
 * @author: songww
 * @date:   2020年5月8日 下午9:41:46     
 */
@Component
public class RegisterLogicHandler {

	@Autowired
	private SIPCommander cmder;
	
	public void onRegister(Device device) {
		// TODO 后续处理，只有第一次注册时调用查询设备信息，如需更新调用更新API接口
		cmder.deviceInfoQuery(device);
		
		cmder.catalogQuery(device);
	}
}
