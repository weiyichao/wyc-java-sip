package cn.mastercom.app.storager;


import cn.mastercom.app.gb28181.bean.Device;

import java.util.List;

/**
 * @Description:视频设备数据存储接口
 * @author: songww
 * @date:   2020年5月6日 下午2:14:31     
 */
public interface IVideoManagerStorager {
	
	/**   
	 * 根据设备ID判断设备是否存在
	 * 
	 * @param deviceId 设备ID
	 * @return true:存在  false：不存在
	 */
	 boolean exists(String deviceId);
	
	/**   
	 * 视频设备创建
	 * 
	 * @param device 设备对象
	 * @return true：创建成功  false：创建失败
	 */
	 boolean create(Device device);
	
	/**   
	 * 视频设备更新
	 * 
	 * @param device 设备对象
	 * @return true：创建成功  false：创建失败
	 */
	 boolean update(Device device);
	
	/**   
	 * 获取设备
	 * 
	 * @param deviceId 设备ID
	 * @return DShadow 设备对象
	 */
	 Device queryVideoDevice(String deviceId);
	
	/**   
	 * 获取多个设备
	 * 
	 * @param deviceIds 设备ID数组
	 * @return List<Device> 设备对象数组
	 */
	 List<Device> queryVideoDeviceList(String[] deviceIds);
	
	/**   
	 * 删除设备
	 * 
	 * @param deviceId 设备ID
	 * @return true：删除成功  false：删除失败
	 */
	 boolean delete(String deviceId);
	
	/**   
	 * 更新设备在线
	 * 
	 * @param deviceId 设备ID
	 * @return true：更新成功  false：更新失败
	 */
	 boolean online(String deviceId);
	
	/**   
	 * 更新设备离线
	 * 
	 * @param deviceId 设备ID
	 * @return true：更新成功  false：更新失败
	 */
	 boolean outline(String deviceId);
}
