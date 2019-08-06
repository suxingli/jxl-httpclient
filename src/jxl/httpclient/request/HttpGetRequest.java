package jxl.httpclient.request;

import org.apache.commons.httpclient.Header;

/**
 * Get请求
 * @author 苏行利
 * @date 2019-06-12 15:47:50
 */
public class HttpGetRequest extends HttpRequest {
	private static final String method = "GET"; // 传输方式

	/**
	 * 构造方法
	 * @author 苏行利
	 * @date 2019-06-12 15:47:54
	 */
	public HttpGetRequest() {
		super();
	}

	/**
	 * 构造方法
	 * @author 苏行利
	 * @param url 请求地址
	 * @date 2019-06-12 15:48:02
	 */
	public HttpGetRequest(String url) {
		super(url);
	}

	/**
	 * 构造方法
	 * @author 苏行利
	 * @param url 请求地址
	 * @param encode 编码方式
	 * @date 2019-06-12 15:48:08
	 */
	public HttpGetRequest(String url, String encode) {
		this(url);
		setEncode(encode);
	}

	/**
	 * 构造方法
	 * @author 苏行利
	 * @param url 请求地址
	 * @param headers 请求头列表
	 * @date 2019-06-12 15:48:19
	 */
	public HttpGetRequest(String url, Header... headers) {
		this(url);
		setHeaders(headers);
	}

	/**
	 * 构造方法
	 * @author 苏行利
	 * @param url 请求地址
	 * @param encode 编码方式
	 * @param headers 请求头列表
	 * @date 2019-06-12 15:48:32
	 */
	public HttpGetRequest(String url, String encode, Header... headers) {
		this(url, encode);
		setHeaders(headers);
	}

	/**
	 * 获取传输方式
	 * @author 苏行利
	 * @return 传输方式
	 * @date 2019-06-12 15:48:45
	 */
	public String getMethod() {
		return method;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("请求地址：").append(getUrl()).append("\n");
		buffer.append("传输方式：").append(getMethod()).append("\n");
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
