package cn.mastercom.app.quartz;

import cn.mastercom.app.gb28181.bean.Device;
import cn.mastercom.app.gb28181.bean.DeviceChannel;
import cn.mastercom.app.gb28181.transmit.cmd.impl.SIPCommander;
import cn.mastercom.app.storager.IVideoManagerStorager;
import cn.mastercom.app.utils.http.HttpClientUtil;
import cn.mastercom.app.utils.redis.RedisUtil;
import cn.mastercom.app.vmanager.play.PlayController;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class QuartzJobs {

    private final static Logger logger = LoggerFactory.getLogger(PlayController.class);

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy/MM/DD/HH");

    @Autowired
    private SIPCommander cmder;

    @Autowired
    private IVideoManagerStorager storager;


    @Autowired
    private RedisUtil redisUtil;


    @Value("#{${sip.auto-storage}}")
    private boolean autoStorage;


    public void execute() throws IOException {

        if (autoStorage){
            try {
                List<Device> deviceList = storager.queryVideoDeviceList(null);
                if (deviceList != null && (!deviceList.isEmpty())){
                    for (Device device : deviceList){
                        String deviceId = device.getDeviceId();
                        int online = device.getOnline();
                        if (online == 1){
                            execStartTask(device, deviceId);
                        }
                    }
                }
            }
            catch (Exception ex){
                logger.info("执行失败，",ex);
            }

        }
    }

    private void execStartTask(Device device, String deviceId) throws IOException {
        Map<String, DeviceChannel> channelMap =  device.getChannelMap();
        for (Map.Entry<String,DeviceChannel> entry : channelMap.entrySet()){
            String channelId = entry.getKey();
            String ssrc = initSsrc(deviceId, channelId);
            recordVideo(ssrc);
        }
    }

    /**
     * 初始化数据源
     * */
    private String initSsrc(String deviceId, String channelId) {
        String ssrc = "";
        String key = deviceId + "_" + channelId + "_" +"ssrc";
        if (redisUtil.hasKey(key)) {
            ssrc = redisUtil.get(key) == null ? "" : redisUtil.get(key).toString();
            redisUtil.set(key, ssrc, 60);
        } else {
            ssrc = getSSrc(deviceId, channelId);
        }
        return ssrc;
    }

    /**
     * 录制视频
     * */
    private void recordVideo(String ssrc) throws IOException {
        String strHex = ("0" + Integer.toHexString(Integer.parseInt(ssrc))).toUpperCase();
        if (PlayController.getSSrcIsOnline(strHex)){
            requestVideoUrl(strHex);
            if (!isRecording(strHex)){
                startRecording(strHex);
            }
            logger.info("{}正在录制",strHex);
        }
    }

    public void executeClean() throws IOException {
        try {
            if (autoStorage){
                List<Device> deviceList = storager.queryVideoDeviceList(null);
                if (deviceList != null && (!deviceList.isEmpty())){
                    for (Device device : deviceList){
                        String deviceId = device.getDeviceId();
                        int online = device.getOnline();
                        if (online == 1){
                            Map<String, DeviceChannel> channelMap =  device.getChannelMap();
                            for (Map.Entry<String,DeviceChannel> entry : channelMap.entrySet()){
                                String channelId = entry.getKey();
                                String ssrc = initSsrc(deviceId, channelId);
                                String strHex = ("0" + Integer.toHexString(Integer.parseInt(ssrc))).toUpperCase();
                                if (PlayController.getSSrcIsOnline(strHex)){
                                    requestVideoUrl(strHex);
                                    if (isRecording(strHex)){
                                        stopRecording(strHex);
                                        logger.info("{}按整点切割文件{}",strHex,SIMPLE_DATE_FORMAT.format(new Date()));
                                    }

                                }
                            }
                        }
                    }
                }
            }

        }
        catch (Exception ex){
            logger.info("切割文件失败,",ex);
        }

    }



    /**
     * 开始录制
     * */
    public void startRecording(String ssrc){
        try(CloseableHttpClient client = HttpClients.createDefault()){
            HttpGet get = new HttpGet("http://127.0.0.1/index/api/startRecord?secret=035c73f7-bb6b-4889-a715-d9eb2d1925cc&type=1&vhost=__defaultVhost__&app=rtp&stream=" + ssrc);
            HttpResponse rese = client.execute(get);
            String ret = EntityUtils.toString(rese.getEntity());
            JSONObject jsonObject = JSON.parseObject(ret);
            String status = jsonObject.getString("result");
            logger.info("录制结果：{}",status);
        }
        catch (Exception ex){
            logger.info("请求失败," + ex);
        }
    }

    public void stopRecording(String ssrc){
        try(CloseableHttpClient client = HttpClients.createDefault()){
            HttpGet get = new HttpGet("http://127.0.0.1/index/api/stopRecord?secret=035c73f7-bb6b-4889-a715-d9eb2d1925cc&type=1&vhost=__defaultVhost__&app=rtp&stream=" + ssrc);
            HttpResponse rese = client.execute(get);
            String ret = EntityUtils.toString(rese.getEntity());
            JSONObject jsonObject = JSON.parseObject(ret);
            String status = jsonObject.getString("result");
            logger.info("停止录制：{}",status);
        }
        catch (Exception ex){
            logger.info("请求失败," + ex);
        }
    }

    /**
     * 判断是否在录制
     * */
    public boolean isRecording(String ssrc) throws IOException {
        try(CloseableHttpClient client = HttpClients.createDefault()){
            HttpGet get = new HttpGet("http://127.0.0.1/index/api/isRecording?secret=035c73f7-bb6b-4889-a715-d9eb2d1925cc&type=1&vhost=__defaultVhost__&app=rtp&stream=" + ssrc);
            HttpResponse rese = client.execute(get);
            String ret = EntityUtils.toString(rese.getEntity());
            JSONObject jsonObject = JSON.parseObject(ret);
            String status = jsonObject.getString("status");
            return "true".equals(status);
        }
        catch (Exception ex){
            logger.info("请求失败," + ex);
            throw ex;
        }
    }

    /**
     * 请求视频URL
     * */
    public void requestVideoUrl(String ssrc){
        try(CloseableHttpClient client = HttpClients.createDefault()){
            HttpGet get = new HttpGet("http://127.0.0.1/rtp/" + ssrc + ".flv");
            client.execute(get);
        }
        catch (Exception ex){
            logger.info("请求失败," + ex);
        }
    }





    /**
     * 获取播放数据源
     * */
    public String getSSrc(String deviceId,String channelId){
        Device device = storager.queryVideoDevice(deviceId);
        String ssrc = cmder.playStreamCmd(device,deviceId,channelId);
        logger.info(String.format("设备预览 API调用，deviceId：%s ，channelId：%s",deviceId, channelId));
        logger.info("设备预览 API调用，ssrc："+ssrc+",ZLMedia streamId:0"+Integer.toHexString(Integer.parseInt(ssrc)));
        String mainKey = deviceId + "_" + channelId;
        if (redisUtil.hasKey(mainKey)){
             String value =   redisUtil.get(mainKey) == null ? "" : (String) redisUtil.get(mainKey);
             if ("".equals(value)){
                 value = ssrc;
             }
             else {
                 value = value + "," + ssrc;
             }
             redisUtil.set(mainKey,value);
        }
        else {
            redisUtil.set(mainKey,ssrc);
        }
        redisUtil.set(deviceId + "_" + channelId + "_" +"ssrc",ssrc,60);
        return ssrc;
    }

}
