package jxl.httpclient.request;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.httpclient.Header;
import org.apache.commons.lang.StringUtils;

import jxl.httpclient.HttpParam;

/**
 * Post请求体
 * @author 苏行利
 * @date 2019-06-12 15:58:22
 */
public class HttpPostRequest extends HttpRequest {
	private static final String method = "POST"; // 传输方式
	private String contentType = "application/x-www-form-urlencoded"; // 内容类型，默认表单提交
	private List<HttpParam> params; // 请求参数列表
	private String requestContent; // 请求内容

	/**
	 * 构造方法
	 * @author 苏行利
	 * @date 2019-06-12 15:58:26
	 */
	public HttpPostRequest() {
		super();
	}

	/**
	 * 构造方法
	 * @author 苏行利
	 * @param url 请求地址
	 * @date 2019-06-12 15:58:32
	 */
	public HttpPostRequest(String url) {
		super(url);
	}

	/**
	 * 构造方法
	 * @author 苏行利
	 * @param url 请求地址
	 * @param headers 请求头列表
	 * @param params 请求参数列表
	 * @date 2019-06-12 15:58:37
	 */
	public HttpPostRequest(String url, Header[] headers, HttpParam... params) {
		this(url);
		setHeaders(headers);
		this.params = (params == null ? null : new ArrayList<HttpParam>(Arrays.asList(params)));
	}

	/**
	 * 构造方法
	 * @author 苏行利
	 * @param url 请求地址
	 * @param params 请求参数列表
	 * @date 2019-06-12 15:58:50
	 */
	public HttpPostRequest(String url, HttpParam... params) {
		this(url);
		this.params = (params == null ? null : new ArrayList<HttpParam>(Arrays.asList(params)));
	}

	/**
	 * 构造方法
	 * @author 苏行利
	 * @param url 请求地址
	 * @param requestContent 请求内容
	 * @date 2019-06-12 15:59:14
	 */
	public HttpPostRequest(String url, String requestContent) {
		this(url);
		this.requestContent = requestContent;
	}

	/**
	 * 构造方法
	 * @author 苏行利
	 * @param url 请求地址
	 * @param headers 请求头列表
	 * @param requestContent 请求内容
	 * @date 2019-06-12 15:59:24
	 */
	public HttpPostRequest(String url, Header[] headers, String requestContent) {
		this(url);
		setHeaders(headers);
		this.requestContent = requestContent;
	}

	/**
	 * 添加参数
	 * @author 苏行利
	 * @param name 参数名称
	 * @param value 参数值
	 * @date 2019-06-12 15:59:39
	 */
	public void addParam(String name, String value) {
		if (params == null) {
			params = new ArrayList<HttpParam>(0);
		}
		params.add(new HttpParam(name, value));
	}

	/**
	 * 获取传输方式
	 * @author 苏行利
	 * @return 传输方式
	 * @date 2019-06-12 15:59:53
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * 获取内容类型
	 * @author 苏行利
	 * @return 内容类型
	 * @date 2019-06-12 16:00:00
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * 设置内容类型
	 * @author 苏行利
	 * @param contentType 内容类型
	 * @date 2019-06-12 16:00:08
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * 获取请求参数列表
	 * @author 苏行利
	 * @return 请求参数列表
	 * @date 2019-06-12 16:00:16
	 */
	public List<HttpParam> getParams() {
		return params;
	}

	/**
	 * 设置请求参数列表
	 * @author 苏行利
	 * @param params 请求参数列表
	 * @date 2019-06-12 16:00:22
	 */
	public void setParams(HttpParam... params) {
		this.params = (params == null ? null : new ArrayList<HttpParam>(Arrays.asList(params)));
	}

	/**
	 * 获取请求内容
	 * @author 苏行利
	 * @return 请求内容
	 * @date 2019-06-12 16:00:29
	 */
	public String getRequestContent() {
		return requestContent;
	}

	/**
	 * 设置请求内容
	 * @author 苏行利
	 * @param requestContent 请求内容
	 * @date 2019-06-12 16:00:36
	 */
	public void setRequestContent(String requestContent) {
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
		if (params != null && params.size() > 0) {
			buffer.append("\n").append("参数：").append("[").append("\n");
			for (HttpParam param : params) {
				buffer.append("\t").append(param.getName()).append(" = ").append(param.getValue()).append("\n");
			}
			buffer.append("]");
		}
		if (StringUtils.isNotEmpty(getRequestContent())) {
			buffer.append("\n").append("请求内容：").append(getRequestContent());
		}
		return buffer.toString();
	}
}
