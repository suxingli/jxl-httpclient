package jxl.httpclient.request;

import org.apache.commons.httpclient.Header;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * XML请求体
 * @author 苏行利
 * @date 2019-06-12 15:50:46
 */
public class HttpXMLRequest extends HttpRequest {
	private static final String method = "POST"; // 传输方式
	private static final String content_type = "application/xml"; // 内容类型
	private Document doc = DocumentHelper.createDocument(); // XML文档
	private Element root = doc.addElement("xml"); // 根节点

	/**
	 * 构造方法
	 * @author 苏行利
	 * @param url 请求地址
	 * @date 2019-06-12 15:50:52
	 */
	public HttpXMLRequest(String url) {
		super(url);
	}

	/**
	 * 构造方法
	 * @author 苏行利
	 * @param url 请求地址
	 * @param root 根节点
	 * @date 2019-06-12 15:51:00
	 */
	public HttpXMLRequest(String url, String root) {
		this(url);
		this.root.setName(root);
	}

	/**
	 * 设置根节点
	 * @author 苏行利
	 * @param root 根节点
	 * @return 请求体
	 * @date 2019-06-12 15:51:24
	 */
	public HttpXMLRequest setRoot(String root) {
		this.root.setName(root);
		return this;
	}

	/**
	 * 添加元素
	 * @author 苏行利
	 * @param name 名称
	 * @param value 值
	 * @return 请求体
	 * @date 2019-06-12 15:51:45
	 */
	public HttpXMLRequest put(String name, Object value) {
		this.root.addElement(name).setText(value == null ? "" : value.toString());
		return this;
	}

	/**
	 * 获取传输方式
	 * @author 苏行利
	 * @return 传输方式
	 * @date 2019-06-12 15:52:04
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * 获取内容类型
	 * @author 苏行利
	 * @return 内容类型(application/xml)
	 * @date 2019-06-12 15:52:14
	 */
	public String getContentType() {
		return content_type;
	}

	/**
	 * 获取请求内容
	 * @author 苏行利
	 * @return 请求内容
	 * @date 2019-06-12 15:52:27
	 */
	public String getRequestContent() {
		String requestContent = doc.asXML();
		if (requestContent.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")) {
			return requestContent.replaceAll("<[?]xml version=\"1.0\" encoding=\"UTF-8\"[?]>", "").trim();
		}
		return requestContent;
	}

	/**
	 * 设置请求内容
	 * @author 苏行利
	 * @param requestContent 请求内容
	 * @date 2019-06-12 15:52:35
	 */
	public void setRequestContent(String requestContent) {
		try {
			this.doc = DocumentHelper.parseText(requestContent);
		} catch (DocumentException e) {
			throw new RuntimeException("XML格式不正确：" + e.getMessage());
		}
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
			buffer.append("\n").append("请求内容：").append(getRequestContent()).append("\"");
		}
		return buffer.toString();
	}
}
