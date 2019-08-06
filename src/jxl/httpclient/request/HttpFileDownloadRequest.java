package jxl.httpclient.request;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.httpclient.Header;
import org.apache.commons.lang.StringUtils;

import jxl.httpclient.HttpParam;

/**
 * 文件下载请求体
 * @author 苏行利
 * @date 2019-06-12 15:55:32
 */
public class HttpFileDownloadRequest extends HttpRequest {
	private static final String method = "POST"; // 传输方式
	private List<HttpParam> params; // 请求参数列表
	private String requestContent; // 请求内容
	private OutputStream out; // 接收文件的输出流

	/**
	 * 构造方法
	 * @author 苏行利
	 * @date 2019-06-12 15:55:37
	 */
	public HttpFileDownloadRequest() {
		super();
	}

	/**
	 * 构造方法
	 * @author 苏行利
	 * @param url 请求地址
	 * @date 2019-06-12 15:55:40
	 */
	public HttpFileDownloadRequest(String url) {
		super(url);
	}

	/**
	 * 构造方法
	 * @author 苏行利
	 * @param url 请求地址
	 * @param out 接收文件的输出流
	 * @date 2019-06-12 15:55:49
	 */
	public HttpFileDownloadRequest(String url, OutputStream out) {
		this(url);
		this.out = out;
	}

	/**
	 * 构造方法
	 * @author 苏行利
	 * @param url 请求地址
	 * @param params 参数列表
	 * @param out 接收文件的输出流
	 * @date 2019-06-12 15:56:00
	 */
	public HttpFileDownloadRequest(String url, HttpParam[] params, OutputStream out) {
		this(url, params, null, out);
	}

	/**
	 * 构造方法
	 * @author 苏行利
	 * @param url 请求地址
	 * @param requestContent 请求内容
	 * @param out 接收文件的输出流
	 * @date 2019-06-12 15:56:12
	 */
	public HttpFileDownloadRequest(String url, String requestContent, OutputStream out) {
		this(url, null, requestContent, out);
	}

	/**
	 * 构造方法
	 * @author 苏行利
	 * @param url 请求地址
	 * @param params 参数列表
	 * @param requestContent 请求内容
	 * @param out 接收文件的输出流
	 * @date 2019-06-12 15:56:27
	 */
	public HttpFileDownloadRequest(String url, HttpParam[] params, String requestContent, OutputStream out) {
		this(url, params, requestContent, out, "UTF-8");
	}

	/**
	 * 构造方法
	 * @author 苏行利
	 * @param url 请求地址
	 * @param params 参数列表
	 * @param requestContent 请求内容
	 * @param out 接收文件的输出流
	 * @param encode 字符编码
	 * @date 2019-06-12 15:56:45
	 */
	public HttpFileDownloadRequest(String url, HttpParam[] params, String requestContent, OutputStream out, String encode) {
		super(url, encode);
		this.params = params == null ? null : new ArrayList<HttpParam>(Arrays.asList(params));
		this.requestContent = requestContent;
		this.out = out;
	}

	/**
	 * 添加参数
	 * @author 苏行利
	 * @param name 名称
	 * @param value 值
	 * @date 2019-06-12 15:57:11
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
	 * @date 2019-06-12 15:57:24
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * 获取参数列表
	 * @author 苏行利
	 * @return 参数列表
	 * @date 2019-06-12 15:57:33
	 */
	public List<HttpParam> getParams() {
		return params;
	}

	/**
	 * 设置参数列表
	 * @author 苏行利
	 * @param params 参数列表
	 * @date 2019-06-12 15:57:40
	 */
	public void setParams(HttpParam... params) {
		this.params = (params == null ? null : new ArrayList<HttpParam>(Arrays.asList(params)));
	}

	/**
	 * 获取请求内容
	 * @author 苏行利
	 * @return 请求内容
	 * @date 2019-06-12 15:57:47
	 */
	public String getRequestContent() {
		return requestContent;
	}

	/**
	 * 设置请求内容
	 * @author 苏行利
	 * @param requestContent 请求内容
	 * @date 2019-06-12 15:57:53
	 */
	public void setRequestContent(String requestContent) {
		this.requestContent = requestContent;
	}

	/**
	 * 获取接收文件的输出流
	 * @author 苏行利
	 * @return 接收文件的输出流
	 * @date 2019-06-12 15:58:01
	 */
	public OutputStream getOut() {
		return out;
	}

	/**
	 * 设置接收文件的输出流
	 * @author 苏行利
	 * @param out 接收文件的输出流
	 * @date 2019-06-12 15:58:06
	 */
	public void setOut(OutputStream out) {
		this.out = out;
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
		if (params != null && params.size() > 0) {
			buffer.append("\n").append("参数：").append("[").append("\n");
			for (HttpParam param : params) {
				buffer.append("\t").append(param.getName()).append(" = ").append(param.getValue()).append("\n");
			}
			buffer.append("]");
		}
		if (StringUtils.isNotEmpty(getRequestContent())) {
			buffer.append("\n").append("请求内容：").append(getRequestContent()).append("\"");
		}
		if (out != null) {
			buffer.append("\n").append("接收文件的输出流：").append(out.toString()).append("\"");
		}
		return buffer.toString();
	}
}
