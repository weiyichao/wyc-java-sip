package cn.mastercom.app.utils.http;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * http请求工具类、完善版本；
 * 
 * @author Administrator
 *
 */
public class HttpClientUtil {
	private HttpClientUtil() {

	}

	private static final String CHARSET = "UTF-8";
	private static Logger log = LoggerFactory.getLogger(HttpClientUtil.class);

	/**
	 * Post表单方式请求
	 * 
	 * @param reqUrl 请求url
	 * @param mapData 请求表单数据map
	 * @param headerMap 请求头
	 * @return
	 */
	public static String doPost(String reqUrl, Map<String, Object> mapData, Map<String, String> headerMap) {
		log.info("post请求地址：{}", reqUrl);
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			List<NameValuePair> formparams = new ArrayList<>();
			for (Map.Entry<String, Object> entry : mapData.entrySet()) {
				formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
			}
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
			log.info("post请求参数：{}", formparams);
			HttpPost httpPost = new HttpPost(reqUrl);
			httpPost.setEntity(entity);
			// 设置请求头信息
			if (headerMap != null && !headerMap.isEmpty()) {
				for (Map.Entry<String, String> entry : headerMap.entrySet()) {
					httpPost.setHeader(entry.getKey(), entry.getValue());
				}
			}
			CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
			StatusLine statusLine = httpResponse.getStatusLine();
			int code = statusLine.getStatusCode();
			String reasonPhrase = statusLine.getReasonPhrase();
			log.info(" 响应状态码：{};原因或结果 ：{}", code, reasonPhrase);
			HttpEntity httpEntity = httpResponse.getEntity();
			return EntityUtils.toString(httpEntity);
		} catch (IOException e) {
			log.info(e.getMessage());
		}
		return null;
	}

	/**
	 * json post请求
	 * */
	public static String jsonPost(String url, JSONObject jsonObject, String encoding) {
		String body = "";

		//创建httpclient对象
		try (CloseableHttpClient httpClient = HttpClients.createDefault()){
			//创建post方式请求对象
			HttpPost httpPost = new HttpPost(url);

			//装填参数
			StringEntity s = new StringEntity(jsonObject.toString(), "utf-8");
			s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));
			//设置参数到请求对象中
			httpPost.setEntity(s);
			System.out.println("请求地址："+url);
			httpPost.setHeader("Content-type", "application/json");
			httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

			//执行请求操作，并拿到结果（同步阻塞）
			CloseableHttpResponse response = httpClient.execute(httpPost);
			//获取结果实体
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				//按指定编码转换结果实体为String类型
				body = EntityUtils.toString(entity, encoding);
			}
			EntityUtils.consume(entity);
			//释放链接
			response.close();
			return body;
		}
		catch (Exception ex){
			log.info(ex.getMessage());
		}
		return null;

	}
}
