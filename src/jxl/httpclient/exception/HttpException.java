package jxl.httpclient.exception;

/**
 * Http异常
 * @author 苏行利
 * @date 2019-06-12 15:52:51
 */
public class HttpException extends Exception {
	private static final long serialVersionUID = 8814591639595467074L;
	private String code; // 异常编码
	private long consumeTime; // 耗时

	/**
	 * 构造方法
	 * @author 苏行利
	 * @param code 异常编码
	 * @param message 异常信息
	 * @date 2019-06-12 15:52:55
	 */
	public HttpException(Integer code, String message) {
		this(code, message, 0);
	}

	/**
	 * 构造方法
	 * @author 苏行利
	 * @param code 异常编码
	 * @param message 异常信息
	 * @date 2019-06-12 15:53:07
	 */
	public HttpException(String code, String message) {
		this(code, message, 0);
	}

	/**
	 * 构造方法
	 * @author 苏行利
	 * @param code 异常编码
	 * @param message 异常信息
	 * @param consumeTime 接口请求的消耗时间(毫秒)
	 * @date 2019-06-12 15:53:21
	 */
	public HttpException(Integer code, String message, long consumeTime) {
		this(code, message, consumeTime, null);
	}

	/**
	 * 构造方法
	 * @author 苏行利
	 * @param code 异常编码
	 * @param message 异常信息
	 * @param consumeTime 接口请求的消耗时间(毫秒)
	 * @date 2019-06-12 15:53:34
	 */
	public HttpException(String code, String message, long consumeTime) {
		this(code, message, consumeTime, null);
	}

	/**
	 * 构造方法
	 * @author 苏行利
	 * @param code 异常编码
	 * @param message 异常信息
	 * @param consumeTime 接口请求的消耗时间(毫秒)
	 * @param cause 异常源
	 * @date 2019-06-12 15:53:50
	 */
	public HttpException(Integer code, String message, long consumeTime, Throwable cause) {
		super(message, cause);
		this.code = code == null ? null : code.toString();
		this.consumeTime = consumeTime;
	}

	/**
	 * 构造方法
	 * @author 苏行利
	 * @param code 异常编码
	 * @param message 异常信息
	 * @param consumeTime 接口请求的消耗时间(毫秒)
	 * @param cause 异常源
	 * @date 2019-06-12 15:54:11
	 */
	public HttpException(String code, String message, long consumeTime, Throwable cause) {
		super(message, cause);
		this.code = code;
		this.consumeTime = consumeTime;
	}

	/**
	 * 获取异常编码
	 * @author 苏行利
	 * @return 异常编码
	 * @date 2019-06-12 15:54:29
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 获取接口请求的消耗时间
	 * @author 苏行利
	 * @return 接口请求的消耗时间(单位/毫秒)
	 * @date 2019-06-12 15:54:39
	 */
	public long getConsumeTime() {
		return consumeTime;
	}

	@Override
	public String toString() {
		return super.toString() + "(" + this.code + ")";
	}

}
