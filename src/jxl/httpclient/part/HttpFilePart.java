package jxl.httpclient.part;

import java.io.File;

/**
 * 文件片段
 * @author 苏行利
 * @date 2019-06-12 16:02:19
 */
public class HttpFilePart extends HttpPart {
	private String name; // 名称
	private File input; // 文件

	/**
	 * 构造方法
	 * @author 苏行利
	 * @param input 文件
	 * @date 2019-06-12 16:02:23
	 */
	public HttpFilePart(File input) {
		super();
		this.input = input;
	}

	/**
	 * 构造方法
	 * @author 苏行利
	 * @param name 名称
	 * @param input 文件
	 * @date 2019-06-12 16:02:31
	 */
	public HttpFilePart(String name, File input) {
		super();
		this.name = name;
		this.input = input;
	}

	/**
	 * 获取名称
	 * @author 苏行利
	 * @return 名称
	 * @date 2019-06-12 16:02:42
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置名称
	 * @author 苏行利
	 * @param name 名称
	 * @date 2019-06-12 16:02:56
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取文件
	 * @author 苏行利
	 * @return 文件
	 * @date 2019-06-12 16:03:10
	 */
	public File getInput() {
		return input;
	}

	/**
	 * 设置文件
	 * @author 苏行利
	 * @param input 文件
	 * @date 2019-06-12 16:03:19
	 */
	public void setInput(File input) {
		this.input = input;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer(this.getClass().getSimpleName());
		buffer.append("[");
		buffer.append("name").append(" = ").append(name).append(", ");
		buffer.append("input").append(" = ").append(input);
		buffer.append("]");
		return buffer.toString();
	}
}
