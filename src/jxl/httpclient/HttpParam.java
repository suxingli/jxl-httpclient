package jxl.httpclient;

/**
 * Http请求参数
 * @author 苏行利
 * @date 2019-06-12 16:04:00
 */
public class HttpParam {
	private String name; // 参数名
	private String value; // 参数值

	/**
	 * 构造方法
	 * @author 苏行利
	 * @param name 参数名
	 * @param value 参数值
	 * @date 2019-06-12 16:04:04
	 */
	public HttpParam(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}

	/**
	 * 获取参数名
	 * @author 苏行利
	 * @return 参数名
	 * @date 2019-06-12 16:04:14
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置参数名
	 * @author 苏行利
	 * @param name 参数名
	 * @date 2019-06-12 16:04:20
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取参数值
	 * @author 苏行利
	 * @return 参数值
	 * @date 2019-06-12 16:04:28
	 */
	public String getValue() {
		return value;
	}

	/**
	 * 设置参数值
	 * @author 苏行利
	 * @param value 参数值
	 * @date 2019-06-12 16:04:34
	 */
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer(this.getClass().getSimpleName());
		buffer.append("[");
		buffer.append("name").append(" = ").append(name).append(", ");
		buffer.append("value").append(" = ").append(value);
		buffer.append("]");
		return buffer.toString();
	}
}
