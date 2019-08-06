package jxl.httpclient.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.Header;

/**
 * Http请求体
 * @author 苏行利
 * @date 2019-06-12 15:39:48
 */
public class HttpRequest {
	private String url; // 请求地址
	private String encode = "UTF-8"; // 编码方式，默认UTF-8
	private Map<String, Header> hm; // 请求头映射表

	/**
	 * 构造方法
	 * @author 苏行利
	 * @date 2019-06-12 15:39:53
	 */
	public HttpRequest() {
		super();
	}

	/**
	 * 构造方法
	 * @author 苏行利
	 * @param url 请求地址
	 * @date 2019-06-12 15:39:59
	 */
	public HttpRequest(String url) {
		super();
		this.url = url;
	}

	/**
	 * 构造方法
	 * @author 苏行利
	 * @param url 请求地址
	 * @param encode 编码方式
	 * @date 2019-06-12 15:40:08
	 */
	public HttpRequest(String url, String encode) {
		super();
		this.url = url;
		this.encode = encode;
	}

	/**
	 * 添加请求头
	 * @author 苏行利
	 * @param name 请求头名称
	 * @param value 请求头值
	 * @date 2019-06-12 15:40:23
	 */
	public void addHeader(String name, String value) {
		if (hm == null) {
			hm = new HashMap<String, Header>();
		}
		hm.put(name, new Header(name, value));
	}

	/**
	 * 设置请求头
	 * @author 苏行利
	 * @param name 请求头名称
	 * @param value 请求头值
	 * @date 2019-06-12 15:40:40
	 */
	public void setHeader(String name, String value) {
		if (hm == null) {
			hm = new HashMap<String, Header>();
		}
		Header header = hm.get(name);
		if (header == null) {
			hm.put(name, new Header(name, value));
		} else {
			header.setValue(value);
		}
	}

	/**
	 * 设置客户端缓存信息
	 * @author 苏行利
	 * @param cookie 客户端缓存信息
	 * @date 2019-06-12 15:40:57
	 */
	public void setCookie(String cookie) {
		setHeader("Cookie", cookie);
	}

	/**
	 * 添加链接参数
	 * @author 苏行利
	 * @param name 参数名称
	 * @param value 参数值
	 * @date 2019-06-12 15:41:13
	 */
	public void addLinkParam(String name, Object value) {
		if (url.indexOf("?") == -1) {
			url += "?" + name + "=" + value;
			return;
		}
		url += "&" + name + "=" + value;
	}

	/**
	 * 获取请求地址
	 * @author 苏行利
	 * @return 请求地址
	 * @date 2019-06-12 15:41:27
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 设置请求地址
	 * @author 苏行利
	 * @param url 请求地址
	 * @date 2019-06-12 15:41:40
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 获取编码方式
	 * @author 苏行利
	 * @return 编码方式
	 * @date 2019-06-12 15:41:50
	 */
	public String getEncode() {
		return encode;
	}

	/**
	 * 设置编码方式
	 * @author 苏行利
	 * @param encode 编码方式
	 * @date 2019-06-12 15:42:02
	 */
	public void setEncode(String encode) {
		this.encode = encode;
	}

	/**
	 * 获取请求头列表
	 * @author 苏行利
	 * @return 请求头列表
	 * @date 2019-06-12 15:42:14
	 */
	public List<Header> getHeaders() {
		if (hm == null) {
			hm = new HashMap<String, Header>();
		}
		return new ArrayList<Header>(hm.values());
	}

	/**
	 * 设置请求头列表
	 * @author 苏行利
	 * @param headers 请求头列表
	 * @date 2019-06-12 15:42:25
	 */
	public void setHeaders(Header... headers) {
		if (hm == null) {
			hm = new HashMap<String, Header>();
		}
		hm.clear();
		if (headers == null || headers.length <= 0) {
			return;
		}
		for (Header header : headers) {
			hm.put(header.getName(), header);
		}
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("请求地址：").append(getUrl()).append("\n");
		buffer.append("编码方式：").append(getEncode());
		if (getHeaders() != null && getHeaders().size() > 0) {
			buffer.append("\n").append("请求头：").append("[").append("\n");
			for (Header header : getHeaders()) {
				buffer.append("\t").append(header.getName()).append(" = ").append(header.getValue()).append("\n");
			}
			buffer.append("]");
		}
		return buffer.toString();
	}
}
