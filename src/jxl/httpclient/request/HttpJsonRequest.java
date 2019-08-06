package jxl.httpclient.request;

import org.apache.commons.httpclient.Header;
import org.apache.commons.lang.StringUtils;

import net.sf.json.JSONObject;

/**
 * Json请求体
 * @author 苏行利
 * @date 2019-06-12 15:49:01
 */
public class HttpJsonRequest extends HttpRequest {
	private static final String method = "POST"; // 传输方式
	private static final String content_type = "application/json"; // 内容类型
	private JSONObject requestContent = new JSONObject(); // 请求内容

	/**
	 * 构造方法
	 * @author 苏行利
	 * @param url 请求地址
	 * @date 2019-06-12 15:49:06
	 */
	public HttpJsonRequest(String url) {
		super(url);
	}

	/**
	 * 构造方法
	 * @author 苏行利
	 * @param url 请求地址
	 * @param requestContent 请求内容
	 * @date 2019-06-12 15:49:13
	 */
	public HttpJsonRequest(String url, JSONObject requestContent) {
		this(url);
		this.requestContent = requestContent;
	}

	/**
	 * 添加元素
	 * @author 苏行利
	 * @param name 名称
	 * @param value 值
	 * @return 请求体
	 * @date 2019-06-12 15:49:26
	 */
	public HttpJsonRequest put(String name, Object value) {
		requestContent.put(name, value);
		return this;
	}

	/**
	 * 获取传输方式
	 * @author 苏行利
	 * @return 传输方式
	 * @date 2019-06-12 15:49:49
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * 获取内容类型
	 * @author 苏行利
	 * @return 内容类型(application/json)
	 * @date 2019-06-12 15:49:58
	 */
	public String getContentType() {
		return content_type;
	}

	/**
	 * 获取请求内容
	 * @author 苏行利
	 * @return 请求内容
	 * @date 2019-06-12 15:50:16
	 */
	public String getRequestContent() {
		return requestContent == null ? "{}" : requestContent.toString();
	}

	/**
	 * 设置请求内容
	 * @author 苏行利
	 * @param requestContent 请求内容
	 * @date 2019-06-12 15:50:23
	 */
	public void setRequestContent(JSONObject requestContent) {
		this.requestContent = requestContent;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("请求地址：").append(getUrl()).append("\n");
		buffer.append("传输方式：").append(getMethod()).append("\n");
		buffer.append("内容类型：").append(getContentType()).append("\n");
		buffer.append("编码方式：").append(getEncode());
		if (getHeaders() != null && getHeaders().size() > 0) {
			buffer.append("\n").append("请求头：").append("[").append("\n");
			for (Header header : getHeaders()) {
				buffer.append("\t").append(header.getName()).append(" = ").append(header.getValue()).append("\n");
			}
			buffer.append("]");
		}
		if (StringUtils.isNotEmpty(getRequestContent())) {
			buffer.append("\n").append("请求内容：").append(getRequestContent());
		}
		return buffer.toString();
	}
}
