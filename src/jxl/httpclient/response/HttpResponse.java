package jxl.httpclient.response;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpStatus;

/**
 * Http响应体
 * @author 苏行利
 * @date 2019-06-12 15:44:14
 */
public class HttpResponse {
	private String version; // Http版本
	private Integer statusCode; // 状态码
	private String reasonPhrase; // 状态信息
	private long consumeTime; // 耗时
	private Header[] headers; // 响应头列表
	private String encode; // 字符编码
	private String contentType; // 内容类型
	private long contentLength; // 内容长度
	private String content; // 响应内容

	/**
	 * 判断是否正常状态
	 * @author 苏行利
	 * @return 是否正常状态
	 * @date 2019-06-12 15:44:37
	 */
	public boolean isOKStatus() {
		return statusCode != null && statusCode.equals(HttpStatus.SC_OK);
	}

	/**
	 * 获取Http版本
	 * @author 苏行利
	 * @return Http版本
	 * @date 2019-06-12 15:45:02
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * 设置Http版本
	 * @author 苏行利
	 * @param version Http版本
	 * @date 2019-06-12 15:45:14
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * 获取状态码
	 * @author 苏行利
	 * @return 状态码
	 * @date 2019-06-12 15:45:25
	 */
	public Integer getStatusCode() {
		return statusCode;
	}

	/**
	 * 设置状态码
	 * @author 苏行利
	 * @param statusCode 状态码
	 * @date 2019-06-12 15:45:30
	 */
	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * 获取状态信息
	 * @author 苏行利
	 * @return 状态信息
	 * @date 2019-06-12 15:45:41
	 */
	public String getReasonPhrase() {
		return reasonPhrase;
	}

	/**
	 * 设置状态信息
	 * @author 苏行利
	 * @param reasonPhrase 状态信息
	 * @date 2019-06-12 15:45:46
	 */
	public void setReasonPhrase(String reasonPhrase) {
		this.reasonPhrase = reasonPhrase;
	}

	/**
	 * 获取接口请求完成的消耗时间
	 * @author 苏行利
	 * @return 接口请求完成的消耗时间(单位/毫秒)
	 * @date 2019-06-12 15:45:58
	 */
	public long getConsumeTime() {
		return consumeTime;
	}

	/**
	 * 设置接口请求完成的消耗时间
	 * @author 苏行利
	 * @param consumeTime 接口请求完成的消耗时间(单位/毫秒)
	 * @date 2019-06-12 15:46:07
	 */
	public void setConsumeTime(long consumeTime) {
		this.consumeTime = consumeTime;
	}

	/**
	 * 获取响应头列表
	 * @author 苏行利
	 * @return 响应头列表
	 * @date 2019-06-12 15:46:19
	 */
	public Header[] getHeaders() {
		return headers;
	}

	/**
	 * 设置响应头列表
	 * @author 苏行利
	 * @param headers 响应头列表
	 * @date 2019-06-12 15:46:24
	 */
	public void setHeaders(Header[] headers) {
		this.headers = headers;
	}

	/**
	 * 获取编码方式
	 * @author 苏行利
	 * @return 编码方式
	 * @date 2019-06-12 15:46:30
	 */
	public String getEncode() {
		return encode;
	}

	/**
	 * 设置编码方式
	 * @author 苏行利
	 * @param encode 编码方式
	 * @date 2019-06-12 15:46:36
	 */
	public void setEncode(String encode) {
		this.encode = encode;
	}

	/**
	 * 获取内容类型
	 * @author 苏行利
	 * @return 内容类型
	 * @date 2019-06-12 15:46:45
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * 设置内容类型
	 * @author 苏行利
	 * @param contentType 内容类型
	 * @date 2019-06-12 15:46:54
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * 获取内容长度
	 * @author 苏行利
	 * @return 内容长度
	 * @date 2019-06-12 15:47:08
	 */
	public long getContentLength() {
		return contentLength;
	}

	/**
	 * 设置内容长度
	 * @author 苏行利
	 * @param contentLength 内容长度
	 * @date 2019-06-12 15:47:14
	 */
	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}

	/**
	 * 获取响应内容
	 * @author 苏行利
	 * @return 响应内容
	 * @date 2019-06-12 15:47:22
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 设置响应内容
	 * @author 苏行利
	 * @param content 响应内容
	 * @date 2019-06-12 15:47:29
	 */
	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("版本：").append(version);
		buffer.append("\n").append("状态码：").append(statusCode);
		buffer.append("\n").append("状态信息：").append(reasonPhrase);
		buffer.append("\n").append("耗时：").append(consumeTime).append("ms");
		if (headers != null && headers.length > 0) {
			buffer.append("\n").append("响应头：").append("[").append("\n");
			for (Header header : getHeaders()) {
				buffer.append("\t").append(header.getName()).append(" = ").append(header.getValue()).append("\n");
			}
			buffer.append("]");
		}
		buffer.append("\n").append("响应内容：").append(content);
		return buffer.toString();
	}
}
