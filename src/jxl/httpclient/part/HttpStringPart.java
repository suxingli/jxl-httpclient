package jxl.httpclient.part;

/**
 * 字符片段
 * @author 苏行利
 * @date 2019-06-12 16:00:53
 */
public class HttpStringPart extends HttpPart {
	private String name; // 名称
	private String value; // 值

	/**
	 * 构造方法
	 * @author 苏行利
	 * @param name 名称
	 * @param value 值
	 * @date 2019-06-12 16:00:57
	 */
	public HttpStringPart(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}

	/**
	 * 构造方法
	 * @author 苏行利
	 * @param name 名称
	 * @param value 值
	 * @date 2019-06-12 16:01:06
	 */
	public HttpStringPart(String name, Number value) {
		this(name, value == null ? null : value.toString());
	}

	/**
	 * 获取名称
	 * @author 苏行利
	 * @return 名称
	 * @date 2019-06-12 16:01:17
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置名称
	 * @author 苏行利
	 * @param name 名称
	 * @date 2019-06-12 16:01:40
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取值
	 * @author 苏行利
	 * @return 值
	 * @date 2019-06-12 16:01:48
	 */
	public String getValue() {
		return value;
	}

	/**
	 * 设置值
	 * @author 苏行利
	 * @param value 值
	 * @date 2019-06-12 16:01:57
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
