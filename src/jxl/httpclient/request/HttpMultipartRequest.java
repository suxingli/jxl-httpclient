package jxl.httpclient.request;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.httpclient.Header;

import jxl.httpclient.HttpParam;
import jxl.httpclient.part.HttpFilePart;
import jxl.httpclient.part.HttpPart;
import jxl.httpclient.part.HttpStringPart;

/**
 * 多片段请求体
 * @author 苏行利
 * @date 2019-06-12 16:04:52
 */
public class HttpMultipartRequest extends HttpRequest {
	private static final String method = "POST"; // 传输方式
	private static final String content_type = "multipart/form-data"; // 内容类型
	private List<HttpParam> params; // 请求参数列表
	private List<HttpPart> parts = new ArrayList<HttpPart>(0); // 片断列表

	/**
	 * 构造方法
	 * @author 苏行利
	 * @date 2019-06-12 16:04:56
	 */
	public HttpMultipartRequest() {
		super();
	}

	/**
	 * 构造方法
	 * @author 苏行利
	 * @param url 请求地址
	 * @date 2019-06-12 16:05:00
	 */
	public HttpMultipartRequest(String url) {
		super(url);
	}

	/**
	 * 构造方法
	 * @author 苏行利
	 * @param url 请求地址
	 * @param input 件片段的文件
	 * @date 2019-06-12 16:05:05
	 */
	public HttpMultipartRequest(String url, File input) {
		this(url, "", input);
	}

	/**
	 * 构造方法
	 * @author 苏行利
	 * @param url 请求地址
	 * @param name 文件片段的名称
	 * @param input 文件片段的文件
	 * @date 2019-06-12 16:05:18
	 */
	public HttpMultipartRequest(String url, String name, File input) {
		this(url, name, input, "UTF-8");
	}

	/**
	 * 构造方法
	 * @author 苏行利
	 * @param url 请求地址
	 * @param name 文件片段的名称
	 * @param input 文件片段的文件
	 * @param encode 编码方式
	 * @date 2019-06-12 16:05:35
	 */
	public HttpMultipartRequest(String url, String name, File input, String encode) {
		super(url, encode);
		addFilePart(name, input);
	}

	/**
	 * 添加参数
	 * @author 苏行利
	 * @param name 参数名称
	 * @param value 参数值
	 * @date 2019-06-12 16:05:49
	 */
	public void addParam(String name, String value) {
		if (params == null) {
			params = new ArrayList<HttpParam>(0);
		}
		params.add(new HttpParam(name, value));
	}

	/**
	 * 添加字符片段
	 * @author 苏行利
	 * @param name 名称
	 * @param value 值
	 * @date 2019-06-12 16:06:00
	 */
	public void addStringPart(String name, String value) {
		parts.add(new HttpStringPart(name, value));
	}

	/**
	 * 添加字符片段
	 * @author 苏行利
	 * @param name 名称
	 * @param value 值
	 * @date 2019-06-12 16:06:11
	 */
	public void addStringPart(String name, Number value) {
		parts.add(new HttpStringPart(name, value));
	}

	/**
	 * 添加文件片段
	 * @author 苏行利
	 * @param input 文件
	 * @date 2019-06-12 16:06:23
	 */
	public void addFilePart(File input) {
		addFilePart("", input);
	}

	/**
	 * 添加文件片段
	 * @author 苏行利
	 * @param name 名称
	 * @param input 文件
	 * @date 2019-06-12 16:06:32
	 */
	public void addFilePart(String name, File input) {
		parts.add(new HttpFilePart(name, input));
	}

	/**
	 * 获取传输方式
	 * @author 苏行利
	 * @return 传输方式
	 * @date 2019-06-12 16:06:53
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * 获取内容类型
	 * @author 苏行利
	 * @return 内容类型(multipart/form-data)
	 * @date 2019-06-12 16:07:04
	 */
	public String getContentType() {
		return content_type;
	}

	/**
	 * 获取参数列表
	 * @author 苏行利
	 * @return 参数列表
	 * @date 2019-06-12 16:07:15
	 */
	public List<HttpParam> getParams() {
		return params;
	}

	/**
	 * 设置参数列表
	 * @author 苏行利
	 * @param params 参数列表
	 * @date 2019-06-12 16:07:22
	 */
	public void setParams(HttpParam... params) {
		this.params = (params == null ? null : new ArrayList<HttpParam>(Arrays.asList(params)));
	}

	/**
	 * 获取片段列表
	 * @author 苏行利
	 * @return 片段列表
	 * @date 2019-06-12 16:07:33
	 */
	public List<HttpPart> getParts() {
		return parts;
	}

	/**
	 * 设置片段列表
	 * @author 苏行利
	 * @param parts 片段列表
	 * @date 2019-06-12 16:07:41
	 */
	public void setParts(HttpPart... parts) {
		this.parts = (parts == null ? this.parts : new ArrayList<HttpPart>(Arrays.asList(parts)));
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("请求地址：").append(getUrl()).append("\n");
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
			buffer.append("]").append("\n");
		}
		if (parts != null && parts.size() > 0) {
			buffer.append("\n").append("片段：").append("[").append("\n");
			for (HttpPart part : parts) {
				if (part instanceof HttpStringPart) {
					buffer.append("\t").append("name = ").append(((HttpStringPart) part).getName()).append(", ");
					buffer.append("value = ").append(((HttpStringPart) part).getValue()).append("\n");
					continue;
				}
				if (part instanceof HttpFilePart) {
					buffer.append("\t").append("name = ").append(((HttpFilePart) part).getName()).append(", ");
					buffer.append("file = ").append(((HttpFilePart) part).getInput()).append("\n");
					continue;
				}
			}
			buffer.append("]");
		}
		return buffer.toString();
	}

}
