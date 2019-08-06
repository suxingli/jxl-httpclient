package jxl.httpclient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NoHttpResponseException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tools.ant.filters.StringInputStream;

import jxl.httpclient.exception.HttpException;
import jxl.httpclient.part.HttpFilePart;
import jxl.httpclient.part.HttpPart;
import jxl.httpclient.part.HttpStringPart;
import jxl.httpclient.request.HttpFileDownloadRequest;
import jxl.httpclient.request.HttpGetRequest;
import jxl.httpclient.request.HttpJsonRequest;
import jxl.httpclient.request.HttpMultipartRequest;
import jxl.httpclient.request.HttpPostRequest;
import jxl.httpclient.request.HttpXMLRequest;
import jxl.httpclient.response.HttpResponse;

/**
 * Http浏览器
 * @author 苏行利
 * @date 2019-06-12 16:08:12
 */
public class HttpExplorer {
	private static final Logger logger = HttpLogger.getLogger();
	private static final int defaultConnectionTimeout = 15; // 默认连接超时时间
	private static final int defaultSoTimeout = 30; // 默认读取数据超时时间
	private int connectionTimeout = defaultConnectionTimeout; // 连接超时时间，默认15秒
	private int soTimeout = defaultSoTimeout; // 读取数据超时时间，默认30秒
	private HttpClient client; // Http客户端对象

	/**
	 * 构造方法
	 * @author 苏行利
	 * @date 2019-06-12 16:08:16
	 */
	public HttpExplorer() {
		this(defaultConnectionTimeout, defaultSoTimeout);
	}

	/**
	 * 构造方法
	 * @author 苏行利
	 * @param connectionTimeout 连接超时时间(单位/秒)
	 * @param soTimeout 读取数据超时时间(单位/秒)
	 * @date 2019-06-12 16:08:20
	 */
	public HttpExplorer(int connectionTimeout, int soTimeout) {
		super();
		client = new HttpClient(new MultiThreadedHttpConnectionManager()); // Http客户端对象
		setConnectionTimeout(connectionTimeout);
		setSoTimeout(soTimeout);
	}

	/**
	 * Get请求
	 * @author 苏行利
	 * @param url 请求地址
	 * @return 响应体
	 * @throws HttpException
	 * @date 2019-06-12 16:08:33
	 */
	public HttpResponse doGet(String url) throws HttpException {
		return doGet(new HttpGetRequest(url));
	}

	/**
	 * Get请求
	 * @author 苏行利
	 * @param url 请求地址
	 * @param headers 请求头列表
	 * @return 响应体
	 * @throws HttpException
	 * @date 2019-06-12 16:08:49
	 */
	public HttpResponse doGet(String url, Header... headers) throws HttpException {
		return doGet(new HttpGetRequest(url, headers));
	}

	/**
	 * Get请求
	 * @author 苏行利
	 * @param request 请求体
	 * @return 响应体
	 * @throws HttpException
	 * @date 2019-06-12 16:09:10
	 */
	public HttpResponse doGet(HttpGetRequest request) throws HttpException {
		long start = System.currentTimeMillis();
		HttpResponse response = new HttpResponse();
		BufferedReader reader = null;
		GetMethod getMethod = new GetMethod(request.getUrl());
		getMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, request.getEncode()); // 设置字符编码
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler()); // 使用系统提供的默认的恢复策略
		if (request.getHeaders() != null && request.getHeaders().size() > 0) {
			for (Header header : request.getHeaders()) {
				getMethod.addRequestHeader(header); // 添加请求头
			}
		}
		try {
			int status = client.executeMethod(getMethod);
			if (status == HttpStatus.SC_MOVED_PERMANENTLY || status == HttpStatus.SC_MOVED_TEMPORARILY) { // 重定向
				request.setUrl(getMethod.getResponseHeader("Location").getValue());
				response = doGet(request);
				response.setConsumeTime(System.currentTimeMillis() - start);
				if (logger.isDebugEnabled()) {
					logger.debug(response.getVersion() + " GET " + response.getStatusCode() + " " + response.getReasonPhrase() + " " + response.getConsumeTime() + "ms\n" + request.toString() + "\n" + response.toString());
				}
				return response;
			}
			if (getMethod.getResponseBodyAsStream() != null) {
				reader = new BufferedReader(new InputStreamReader(getMethod.getResponseBodyAsStream(), getMethod.getResponseCharSet()));
				StringBuffer buffer = new StringBuffer();
				String line = null;
				while ((line = reader.readLine()) != null) {
					buffer.append(line).append("\n");
				}
				if (buffer.length() > 0) {
					buffer.deleteCharAt(buffer.length() - 1);
				}
				response.setContent(buffer.toString());
			}
			Header contentType = getMethod.getResponseHeader("Content-Type");
			response.setContentType(contentType == null ? null : (contentType.getValue().indexOf(";") == -1 ? contentType.getValue() : contentType.getValue().substring(0, contentType.getValue().indexOf(";"))));
			response.setVersion(getMethod.getStatusLine().getHttpVersion()); // 设置版本
			response.setStatusCode(getMethod.getStatusLine().getStatusCode()); // 设置状态码
			response.setReasonPhrase(getMethod.getStatusLine().getReasonPhrase()); // 设置状态信息
			response.setEncode(getMethod.getResponseCharSet()); // 设置编码方式
			response.setContentLength(getMethod.getResponseContentLength()); // 设置内容长度
		} catch (ConnectException e) {
			HttpException _e = new HttpException(800, "服务器拒绝连接", System.currentTimeMillis() - start, e);
			if (logger.isDebugEnabled()) {
				logger.debug("HTTP GET " + _e.getCode() + " " + _e.getMessage() + " " + _e.getConsumeTime() + "ms\n" + request.toString() + "\n异常信息：" + _e.getCause().getClass().getName() + "(" + _e.getCause().getMessage() + ")");
			}
			throw _e;
		} catch (SocketTimeoutException e) {
			HttpException _e = new HttpException(801, "读取数据超时", System.currentTimeMillis() - start, e);
			if (logger.isDebugEnabled()) {
				logger.debug("HTTP GET " + _e.getCode() + " " + _e.getMessage() + " " + _e.getConsumeTime() + "ms\n" + request.toString() + "\n异常信息：" + _e.getCause().getClass().getName() + "(" + _e.getCause().getMessage() + ")");
			}
			throw _e;
		} catch (ConnectTimeoutException e) {
			HttpException _e = new HttpException(802, "连接服务器超时", System.currentTimeMillis() - start, e);
			if (logger.isDebugEnabled()) {
				logger.debug("HTTP GET " + _e.getCode() + " " + _e.getMessage() + " " + _e.getConsumeTime() + "ms\n" + request.toString() + "\n异常信息：" + _e.getCause().getClass().getName() + "(" + _e.getCause().getMessage() + ")");
			}
			throw _e;
		} catch (UnknownHostException e) {
			HttpException _e = new HttpException(803, "找不着主机", System.currentTimeMillis() - start, e);
			if (logger.isDebugEnabled()) {
				logger.debug("HTTP GET " + _e.getCode() + " " + _e.getMessage() + " " + _e.getConsumeTime() + "ms\n" + request.toString() + "\n异常信息：" + _e.getCause().getClass().getName() + "(" + _e.getCause().getMessage() + ")");
			}
			throw _e;
		} catch (NoHttpResponseException e) {
			HttpException _e = new HttpException(804, "服务器没有回应", System.currentTimeMillis() - start, e);
			if (logger.isDebugEnabled()) {
				logger.debug("HTTP GET " + _e.getCode() + " " + _e.getMessage() + " " + _e.getConsumeTime() + "ms\n" + request.toString() + "\n异常信息：" + _e.getCause().getClass().getName() + "(" + _e.getCause().getMessage() + ")");
			}
			throw _e;
		} catch (IOException e) {
			if ("Connection reset".equals(e.getMessage())) {
				HttpException _e = new HttpException(807, "连接被重置，可以尝试使用更高版本的JDK进行请求", System.currentTimeMillis() - start, e);
				if (logger.isDebugEnabled()) {
					logger.debug("HTTP GET " + _e.getCode() + " " + _e.getMessage() + " " + _e.getConsumeTime() + "ms\n" + request.toString() + "\n异常信息：" + _e.getCause().getClass().getName() + "(" + _e.getCause().getMessage() + ")");
				}
				throw _e;
			}
			if ("Network is unreachable: connect".equals(e.getMessage())) {
				HttpException _e = new HttpException(808, "网络不可达", System.currentTimeMillis() - start, e);
				if (logger.isDebugEnabled()) {
					logger.debug("HTTP GET " + _e.getCode() + " " + _e.getMessage() + " " + _e.getConsumeTime() + "ms\n" + request.toString() + "\n异常信息：" + _e.getCause().getClass().getName() + "(" + _e.getCause().getMessage() + ")");
				}
				throw _e;
			}
			HttpException _e = new HttpException(806, "数据读写异常：" + e.getMessage(), System.currentTimeMillis() - start, e);
			if (logger.isDebugEnabled()) {
				logger.debug("HTTP GET " + _e.getCode() + " " + _e.getMessage() + " " + _e.getConsumeTime() + "ms\n" + request.toString() + "\n异常信息：" + _e.getCause().getClass().getName() + "(" + _e.getCause().getMessage() + ")");
			}
			throw _e;
		} catch (Exception e) {
			HttpException _e = new HttpException(805, "请求异常：" + e.getMessage(), System.currentTimeMillis() - start, e);
			if (logger.isDebugEnabled()) {
				logger.debug("HTTP GET " + _e.getCode() + " " + _e.getMessage() + " " + _e.getConsumeTime() + "ms\n" + request.toString() + "\n异常信息：" + _e.getCause().getClass().getName() + "(" + _e.getCause().getMessage() + ")");
			}
			throw _e;
		} finally {
			if (reader != null) {
				try {
					reader.close();
					reader = null;
				} catch (IOException e) {
					logger.error("关闭资源出现异常：", e);
				}
			}
			if (getMethod != null) {
				request.setHeaders(getMethod.getRequestHeaders()); // 设置请求头列表
				response.setHeaders(getMethod.getResponseHeaders()); // 设置响应头列表
				getMethod.releaseConnection(); // 关闭连接
				getMethod = null;
			}
			response.setConsumeTime(System.currentTimeMillis() - start);
		}
		if (logger.isDebugEnabled()) {
			logger.debug(response.getVersion() + " GET " + response.getStatusCode() + " " + response.getReasonPhrase() + " " + response.getConsumeTime() + "ms\n" + request.toString() + "\n" + response.toString());
		}
		return response;
	}

	/**
	 * Post请求
	 * @author 苏行利
	 * @param url 请求地址
	 * @param params 请求参数列表
	 * @return 响应体
	 * @throws HttpException
	 * @date 2019-06-12 16:09:25
	 */
	public HttpResponse doPost(String url, HttpParam... params) throws HttpException {
		return doPost(url, new Header[0], params);
	}

	/**
	 * Post请求
	 * @author 苏行利
	 * @param url 请求地址
	 * @param headers 请求头列表
	 * @param params 请求参数列表
	 * @return 响应体
	 * @throws HttpException
	 * @date 2019-06-12 16:09:42
	 */
	public HttpResponse doPost(String url, Header[] headers, HttpParam... params) throws HttpException {
		return doPost(new HttpPostRequest(url, headers, params));
	}

	/**
	 * Post请求
	 * @author 苏行利
	 * @param url 请求地址
	 * @param requestContent 请求内容
	 * @return 响应体
	 * @throws HttpException
	 * @date 2019-06-12 16:10:09
	 */
	public HttpResponse doPost(String url, String requestContent) throws HttpException {
		return doPost(url, new Header[0], requestContent);
	}

	/**
	 * Post请求
	 * @author 苏行利
	 * @param url 请求地址
	 * @param headers 请求头列表
	 * @param requestContent 请求内容
	 * @return 响应体
	 * @throws HttpException
	 * @date 2019-06-12 16:10:42
	 */
	public HttpResponse doPost(String url, Header[] headers, String requestContent) throws HttpException {
		return doPost(new HttpPostRequest(url, headers, requestContent));
	}

	/**
	 * Post请求
	 * @author 苏行利
	 * @param request json内容请求体
	 * @return 响应体
	 * @throws HttpException
	 * @date 2019-06-12 16:11:02
	 */
	public HttpResponse doPost(HttpJsonRequest request) throws HttpException {
		HttpPostRequest _request = new HttpPostRequest(request.getUrl());
		_request.setContentType(request.getContentType());
		_request.setEncode(request.getEncode());
		_request.setHeaders(request.getHeaders() == null ? null : request.getHeaders().toArray(new Header[request.getHeaders().size()]));
		_request.setRequestContent(request.getRequestContent() == null ? "" : request.getRequestContent().toString());
		HttpResponse response = doPost(_request);
		if (_request.getHeaders() != null) {
			request.setHeaders(_request.getHeaders().toArray(new Header[_request.getHeaders().size()]));
		}
		return response;
	}

	/**
	 * Post请求
	 * @author 苏行利
	 * @param request xml内容请求体
	 * @return 响应体
	 * @throws HttpException
	 * @date 2019-06-12 16:11:18
	 */
	public HttpResponse doPost(HttpXMLRequest request) throws HttpException {
		HttpPostRequest _request = new HttpPostRequest(request.getUrl());
		_request.setContentType(request.getContentType());
		_request.setEncode(request.getEncode());
		_request.setHeaders(request.getHeaders() == null ? null : request.getHeaders().toArray(new Header[request.getHeaders().size()]));
		_request.setRequestContent(request.getRequestContent());
		HttpResponse response = doPost(_request);
		if (_request.getHeaders() != null) {
			request.setHeaders(_request.getHeaders().toArray(new Header[_request.getHeaders().size()]));
		}
		return response;
	}

	/**
	 * Post请求
	 * @author 苏行利
	 * @param request 请求体
	 * @return 响应体
	 * @throws HttpException
	 * @date 2019-06-12 16:11:35
	 */
	public HttpResponse doPost(HttpPostRequest request) throws HttpException {
		long start = System.currentTimeMillis();
		HttpResponse response = new HttpResponse();
		BufferedReader reader = null;
		PostMethod postMethod = new PostMethod(request.getUrl());
		try {
			postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, request.getEncode()); // 设置字符编码
			postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler()); // 使用系统提供的默认的恢复策略
			postMethod.setRequestHeader("Connection", "keep-alive"); // 表示需要持久连接。（HTTP 1.1默认进行持久连接）
			if (request.getHeaders() != null && request.getHeaders().size() > 0) {
				for (Header header : request.getHeaders()) {
					postMethod.addRequestHeader(header); // 添加请求头
				}
			}
			if (StringUtils.isNotEmpty(request.getContentType()) && StringUtils.isNotEmpty(request.getEncode())) {
				postMethod.setRequestHeader("Content-Type", request.getContentType() + "; charset=" + request.getEncode());
			}
			if (request.getRequestContent() != null && !"".equals(request.getRequestContent())) {
				RequestEntity requestEntity = new InputStreamRequestEntity(new StringInputStream(request.getRequestContent(), request.getEncode()));
				postMethod.setRequestEntity(requestEntity);
			}
			if (request.getParams() != null && request.getParams().size() > 0) {
				for (HttpParam httpParam : request.getParams()) {
					postMethod.setParameter(httpParam.getName(), httpParam.getValue());
				}
			}
			int status = client.executeMethod(postMethod);
			if (status == HttpStatus.SC_MOVED_PERMANENTLY || status == HttpStatus.SC_MOVED_TEMPORARILY) { // 重定向
				request.setUrl(postMethod.getResponseHeader("Location").getValue());
				response = doPost(request);
				response.setConsumeTime(System.currentTimeMillis() - start);
				if (logger.isDebugEnabled()) {
					logger.debug(response.getVersion() + " POST " + response.getStatusCode() + " " + response.getReasonPhrase() + " " + response.getConsumeTime() + "ms\n" + request.toString() + "\n" + response.toString());
				}
				return response;
			}
			if (postMethod.getResponseBodyAsStream() != null) {
				reader = new BufferedReader(new InputStreamReader(postMethod.getResponseBodyAsStream(), postMethod.getResponseCharSet()));
				StringBuffer buffer = new StringBuffer();
				String line = null;
				while ((line = reader.readLine()) != null) {
					buffer.append(line).append("\n");
				}
				if (buffer.length() > 0) {
					buffer.deleteCharAt(buffer.length() - 1);
				}
				response.setContent(buffer.toString());
			}
			Header contentType = postMethod.getResponseHeader("Content-Type");
			response.setContentType(contentType == null ? null : (contentType.getValue().indexOf(";") == -1 ? contentType.getValue() : contentType.getValue().substring(0, contentType.getValue().indexOf(";"))));
			response.setVersion(postMethod.getStatusLine().getHttpVersion()); // 设置版本
			response.setStatusCode(postMethod.getStatusLine().getStatusCode()); // 设置状态码
			response.setReasonPhrase(postMethod.getStatusLine().getReasonPhrase()); // 设置状态信息
			response.setEncode(postMethod.getResponseCharSet()); // 设置编码方式
			response.setContentLength(postMethod.getResponseContentLength()); // 设置内容长度
		} catch (ConnectException e) {
			HttpException _e = new HttpException(800, "服务器拒绝连接", System.currentTimeMillis() - start, e);
			if (logger.isDebugEnabled()) {
				logger.debug("HTTP POST " + _e.getCode() + " " + _e.getMessage() + " " + _e.getConsumeTime() + "ms\n" + request.toString() + "\n异常信息：" + _e.getCause().getClass().getName() + "(" + _e.getCause().getMessage() + ")");
			}
			throw _e;
		} catch (SocketTimeoutException e) {
			HttpException _e = new HttpException(801, "读取数据超时", System.currentTimeMillis() - start, e);
			if (logger.isDebugEnabled()) {
				logger.debug("HTTP POST " + _e.getCode() + " " + _e.getMessage() + " " + _e.getConsumeTime() + "ms\n" + request.toString() + "\n异常信息：" + _e.getCause().getClass().getName() + "(" + _e.getCause().getMessage() + ")");
			}
			throw _e;
		} catch (ConnectTimeoutException e) {
			HttpException _e = new HttpException(802, "连接服务器超时", System.currentTimeMillis() - start, e);
			if (logger.isDebugEnabled()) {
				logger.debug("HTTP POST " + _e.getCode() + " " + _e.getMessage() + " " + _e.getConsumeTime() + "ms\n" + request.toString() + "\n异常信息：" + _e.getCause().getClass().getName() + "(" + _e.getCause().getMessage() + ")");
			}
			throw _e;
		} catch (UnknownHostException e) {
			HttpException _e = new HttpException(803, "找不着主机", System.currentTimeMillis() - start, e);
			if (logger.isDebugEnabled()) {
				logger.debug("HTTP POST " + _e.getCode() + " " + _e.getMessage() + " " + _e.getConsumeTime() + "ms\n" + request.toString() + "\n异常信息：" + _e.getCause().getClass().getName() + "(" + _e.getCause().getMessage() + ")");
			}
			throw _e;
		} catch (NoHttpResponseException e) {
			HttpException _e = new HttpException(804, "服务器没有回应", System.currentTimeMillis() - start, e);
			if (logger.isDebugEnabled()) {
				logger.debug("HTTP POST " + _e.getCode() + " " + _e.getMessage() + " " + _e.getConsumeTime() + "ms\n" + request.toString() + "\n异常信息：" + _e.getCause().getClass().getName() + "(" + _e.getCause().getMessage() + ")");
			}
			throw _e;
		} catch (IOException e) {
			if ("Connection reset".equals(e.getMessage())) {
				HttpException _e = new HttpException(807, "连接被重置，可以尝试使用更高版本的JDK进行请求", System.currentTimeMillis() - start, e);
				if (logger.isDebugEnabled()) {
					logger.debug("HTTP POST " + _e.getCode() + " " + _e.getMessage() + " " + _e.getConsumeTime() + "ms\n" + request.toString() + "\n异常信息：" + _e.getCause().getClass().getName() + "(" + _e.getCause().getMessage() + ")");
				}
				throw _e;
			}
			if ("Network is unreachable: connect".equals(e.getMessage())) {
				HttpException _e = new HttpException(808, "网络不可达", System.currentTimeMillis() - start, e);
				if (logger.isDebugEnabled()) {
					logger.debug("HTTP POST " + _e.getCode() + " " + _e.getMessage() + " " + _e.getConsumeTime() + "ms\n" + request.toString() + "\n异常信息：" + _e.getCause().getClass().getName() + "(" + _e.getCause().getMessage() + ")");
				}
				throw _e;
			}
			HttpException _e = new HttpException(806, "数据读写异常：" + e.getMessage(), System.currentTimeMillis() - start, e);
			if (logger.isDebugEnabled()) {
				logger.debug("HTTP POST " + _e.getCode() + " " + _e.getMessage() + " " + _e.getConsumeTime() + "ms\n" + request.toString() + "\n异常信息：" + _e.getCause().getClass().getName() + "(" + _e.getCause().getMessage() + ")");
			}
			throw _e;
		} catch (Exception e) {
			HttpException _e = new HttpException(805, "请求异常：" + e.getMessage(), System.currentTimeMillis() - start, e);
			if (logger.isDebugEnabled()) {
				logger.debug("HTTP POST " + _e.getCode() + " " + _e.getMessage() + " " + _e.getConsumeTime() + "ms\n" + request.toString() + "\n异常信息：" + _e.getCause().getClass().getName() + "(" + _e.getCause().getMessage() + ")");
			}
			throw _e;
		} finally {
			if (reader != null) {
				try {
					reader.close();
					reader = null;
				} catch (IOException e) {
					logger.error("关闭资源出现异常：", e);
				}
			}
			if (postMethod != null) {
				request.setHeaders(postMethod.getRequestHeaders()); // 设置请求头列表
				response.setHeaders(postMethod.getResponseHeaders()); // 设置响应头列表
				postMethod.releaseConnection(); // 关闭连接
				postMethod = null;
			}
			response.setConsumeTime(System.currentTimeMillis() - start);
		}
		if (logger.isDebugEnabled()) {
			logger.debug(response.getVersion() + " POST " + response.getStatusCode() + " " + response.getReasonPhrase() + " " + response.getConsumeTime() + "ms\n" + request.toString() + "\n" + response.toString());
		}
		return response;
	}

	/**
	 * Post请求
	 * @author 苏行利
	 * @param url 上传地址
	 * @param input 上传的文件
	 * @return 响应体
	 * @throws HttpException
	 * @date 2019-06-12 16:16:48
	 */
	public HttpResponse doPost(String url, File input) throws HttpException {
		return doPost(new HttpMultipartRequest(url, input));
	}

	/**
	 * Post请求
	 * @author 苏行利
	 * @param url 上传地址
	 * @param name 接收文件的参数名称
	 * @param input 上传的文件对象
	 * @return 响应体
	 * @throws HttpException
	 * @date 2019-06-12 16:17:10
	 */
	public HttpResponse doPost(String url, String name, File input) throws HttpException {
		return doPost(new HttpMultipartRequest(url, name, input));
	}

	/**
	 * Post请求
	 * @author 苏行利
	 * @param request 多片段请求体
	 * @return 响应体
	 * @throws HttpException
	 * @date 2019-06-12 16:17:34
	 */
	public HttpResponse doPost(HttpMultipartRequest request) throws HttpException {
		long start = System.currentTimeMillis();
		HttpResponse response = new HttpResponse();
		BufferedReader reader = null;
		PostMethod postMethod = new PostMethod(request.getUrl());
		try {
			postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, request.getEncode()); // 设置字符编码
			postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler()); // 使用系统提供的默认的恢复策略
			postMethod.setRequestHeader("Connection", "keep-alive"); // 表示需要持久连接。（HTTP 1.1默认进行持久连接）
			if (request.getHeaders() != null && request.getHeaders().size() > 0) {
				for (Header header : request.getHeaders()) {
					postMethod.addRequestHeader(header); // 添加请求头
				}
			}
			if (request.getParams() != null && request.getParams().size() > 0) {
				for (HttpParam param : request.getParams()) {
					postMethod.setParameter(param.getName(), param.getValue());
				}
			}
			List<Part> parts = new ArrayList<Part>(0);
			List<HttpPart> httpParts = request.getParts();
			for (HttpPart httpPart : httpParts) {
				if (httpPart instanceof HttpFilePart) { // 文件片段
					HttpFilePart _part = (HttpFilePart) httpPart;
					parts.add(new FilePart(_part.getName() == null ? "" : _part.getName(), _part.getInput().getName(), _part.getInput(), new MimetypesFileTypeMap().getContentType(_part.getInput()),
							request.getEncode()));
					continue;
				}
				if (httpPart instanceof HttpStringPart) { // 字符片段
					HttpStringPart _part = (HttpStringPart) httpPart;
					parts.add(new StringPart(_part.getName(), _part.getValue(), request.getEncode()));
				}
			}
			postMethod.setRequestEntity(new MultipartRequestEntity(parts.toArray(new Part[parts.size()]), postMethod.getParams()));
			int status = client.executeMethod(postMethod);
			if (status == HttpStatus.SC_MOVED_PERMANENTLY || status == HttpStatus.SC_MOVED_TEMPORARILY) { // 重定向
				request.setUrl(postMethod.getResponseHeader("Location").getValue());
				response = doPost(request);
				response.setConsumeTime(System.currentTimeMillis() - start);
				return response;
			}
			if (postMethod.getResponseBodyAsStream() != null) {
				reader = new BufferedReader(new InputStreamReader(postMethod.getResponseBodyAsStream(), postMethod.getResponseCharSet()));
				StringBuffer buffer = new StringBuffer();
				String line = null;
				while ((line = reader.readLine()) != null) {
					buffer.append(line).append("\n");
				}
				if (buffer.length() > 0) {
					buffer.deleteCharAt(buffer.length() - 1);
				}
				response.setContent(buffer.toString()); // 设置响应内容
			}
			Header contentType = postMethod.getResponseHeader("Content-Type");
			response.setContentType(contentType == null ? null : (contentType.getValue().indexOf(";") == -1 ? contentType.getValue() : contentType.getValue().substring(0, contentType.getValue().indexOf(";"))));
			response.setVersion(postMethod.getStatusLine().getHttpVersion()); // 设置版本
			response.setStatusCode(postMethod.getStatusLine().getStatusCode()); // 设置状态码
			response.setReasonPhrase(postMethod.getStatusLine().getReasonPhrase()); // 设置状态信息
			response.setEncode(postMethod.getResponseCharSet()); // 设置编码方式
			response.setContentLength(postMethod.getResponseContentLength()); // 设置内容长度
		} catch (ConnectException e) {
			throw new HttpException(800, "服务器拒绝连接", System.currentTimeMillis() - start, e);
		} catch (SocketTimeoutException e) {
			throw new HttpException(801, "读取数据超时", System.currentTimeMillis() - start, e);
		} catch (ConnectTimeoutException e) {
			throw new HttpException(802, "连接服务器超时", System.currentTimeMillis() - start, e);
		} catch (UnknownHostException e) {
			throw new HttpException(803, "找不着主机", System.currentTimeMillis() - start, e);
		} catch (NoHttpResponseException e) {
			throw new HttpException(804, "服务器没有回应", System.currentTimeMillis() - start, e);
		} catch (IOException e) {
			if ("Connection reset".equals(e.getMessage())) {
				throw new HttpException(807, "连接被重置，可以尝试使用更高版本的JDK进行请求", System.currentTimeMillis() - start, e);
			}
			if ("Network is unreachable: connect".equals(e.getMessage())) {
				throw new HttpException(808, "网络不可达", System.currentTimeMillis() - start, e);
			}
			if ("File is not a normal file.".equals(e.getMessage())) {
				throw new HttpException(809, "文件不是普通文件或文件不存在", System.currentTimeMillis() - start, e);
			}
			throw new HttpException(806, "数据读写异常：" + e.getMessage(), System.currentTimeMillis() - start, e);
		} catch (Exception e) {
			throw new HttpException(805, "请求异常：" + e.getMessage(), System.currentTimeMillis() - start, e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
					reader = null;
				} catch (IOException e) {
					logger.error("关闭资源出现异常：", e);
				}
			}
			if (postMethod != null) {
				request.setHeaders(postMethod.getRequestHeaders()); // 设置请求头列表
				response.setHeaders(postMethod.getResponseHeaders()); // 设置响应头列表
				postMethod.releaseConnection(); // 关闭连接
				postMethod = null;
			}
			response.setConsumeTime(System.currentTimeMillis() - start);
		}
		if (logger.isDebugEnabled()) {
			logger.debug(response.getVersion() + " POST " + response.getStatusCode() + " " + response.getReasonPhrase() + " " + response.getConsumeTime() + "ms\n" + request.toString() + "\n" + response.toString());
		}
		return response;
	}

	/**
	 * Get请求
	 * @author 苏行利
	 * @param url 请求地址
	 * @param out 接收文件的输出流
	 * @throws HttpException
	 * @date 2019-06-12 16:18:04
	 */
	public void doGet(String url, OutputStream out) throws HttpException {
		long start = System.currentTimeMillis();
		GetMethod getMethod = new GetMethod(url);
		getMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8"); // 设置字符编码
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler()); // 使用系统提供的默认的恢复策略
		try {
			int status = client.executeMethod(getMethod);
			if (status == HttpStatus.SC_OK) { // 状态正常
				out.write(getMethod.getResponseBody());
				return;
			}
			if (status == HttpStatus.SC_MOVED_PERMANENTLY || status == HttpStatus.SC_MOVED_TEMPORARILY) { // 重定向
				doGet(getMethod.getResponseHeader("Location").getValue(), out);
				return;
			}
			throw new HttpException(getMethod.getStatusLine().getStatusCode(), "请求异常：" + getMethod.getStatusLine().getReasonPhrase(), System.currentTimeMillis() - start);
		} catch (ConnectException e) {
			throw new HttpException(800, "服务器拒绝连接", System.currentTimeMillis() - start, e);
		} catch (SocketTimeoutException e) {
			throw new HttpException(801, "读取数据超时", System.currentTimeMillis() - start, e);
		} catch (ConnectTimeoutException e) {
			throw new HttpException(802, "连接服务器超时", System.currentTimeMillis() - start, e);
		} catch (UnknownHostException e) {
			throw new HttpException(803, "找不着主机", System.currentTimeMillis() - start, e);
		} catch (NoHttpResponseException e) {
			throw new HttpException(804, "服务器没有回应", System.currentTimeMillis() - start, e);
		} catch (IOException e) {
			if ("Connection reset".equals(e.getMessage())) {
				throw new HttpException(807, "连接被重置，可以尝试使用更高版本的JDK进行请求", System.currentTimeMillis() - start, e);
			}
			if ("Network is unreachable: connect".equals(e.getMessage())) {
				throw new HttpException(808, "网络不可达", System.currentTimeMillis() - start, e);
			}
			throw new HttpException(806, "数据读写异常：" + e.getMessage(), System.currentTimeMillis() - start, e);
		} catch (Exception e) {
			throw new HttpException(805, "请求异常：" + e.getMessage(), System.currentTimeMillis() - start, e);
		} finally {
			if (getMethod != null) {
				getMethod.releaseConnection(); // 关闭连接
				getMethod = null;
			}
			if (out != null) {
				try {
					out.close();
					out = null;
				} catch (IOException e) {
					logger.error("关闭资源出现异常：", e);
				}
			}
		}
	}

	/**
	 * Get请求
	 * @author 苏行利
	 * @param url 下载地址
	 * @param output 保存的文件
	 * @throws HttpException
	 * @date 2019-06-12 16:18:22
	 */
	public void doGet(String url, File output) throws HttpException {
		if (!output.getParentFile().exists()) {
			output.getParentFile().mkdirs();
		}
		try {
			doGet(url, new FileOutputStream(output));
		} catch (FileNotFoundException e) {
			logger.error("文件不存在：", e);
		}
	}

	/**
	 * Post请求
	 * @author 苏行利
	 * @param request 请求体
	 * @throws HttpException
	 * @date 2019-06-12 16:18:34
	 */
	public void doPost(HttpFileDownloadRequest request) throws HttpException {
		long start = System.currentTimeMillis();
		PostMethod postMethod = new PostMethod(request.getUrl());
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, request.getEncode()); // 设置字符编码
		postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler()); // 使用系统提供的默认的恢复策略
		if (request.getHeaders() != null && request.getHeaders().size() > 0) {
			for (Header header : request.getHeaders()) {
				postMethod.addRequestHeader(header); // 添加请求头
			}
		}
		if (request.getParams() != null && request.getParams().size() > 0) {
			for (HttpParam param : request.getParams()) {
				postMethod.setParameter(param.getName(), param.getValue());
			}
		}
		if (request.getRequestContent() != null && !"".equals(request.getRequestContent())) {
			RequestEntity requestEntity = new InputStreamRequestEntity(new StringInputStream(request.getRequestContent(), request.getEncode()));
			postMethod.setRequestEntity(requestEntity);
		}
		try {
			int status = client.executeMethod(postMethod);
			if (status == HttpStatus.SC_OK) { // 成功
				request.getOut().write(postMethod.getResponseBody());
				return;
			}
			if (status == HttpStatus.SC_MOVED_PERMANENTLY || status == HttpStatus.SC_MOVED_TEMPORARILY) { // 重定向
				request.setUrl(postMethod.getResponseHeader("Location").getValue());
				doPost(request);
				return;
			}
			throw new HttpException(postMethod.getStatusLine().getStatusCode(), "请求异常：" + postMethod.getStatusLine().getReasonPhrase(), System.currentTimeMillis() - start);
		} catch (ConnectException e) {
			throw new HttpException(800, "服务器拒绝连接", System.currentTimeMillis() - start, e);
		} catch (SocketTimeoutException e) {
			throw new HttpException(801, "读取数据超时", System.currentTimeMillis() - start, e);
		} catch (ConnectTimeoutException e) {
			throw new HttpException(802, "连接服务器超时", System.currentTimeMillis() - start, e);
		} catch (UnknownHostException e) {
			throw new HttpException(803, "找不着主机", System.currentTimeMillis() - start, e);
		} catch (NoHttpResponseException e) {
			throw new HttpException(804, "服务器没有回应", System.currentTimeMillis() - start, e);
		} catch (IOException e) {
			if ("Connection reset".equals(e.getMessage())) {
				throw new HttpException(807, "连接被重置，可以尝试使用更高版本的JDK进行请求", System.currentTimeMillis() - start, e);
			}
			if ("Network is unreachable: connect".equals(e.getMessage())) {
				throw new HttpException(808, "网络不可达", System.currentTimeMillis() - start, e);
			}
			throw new HttpException(806, "数据读写异常：" + e.getMessage(), System.currentTimeMillis() - start, e);
		} catch (Exception e) {
			throw new HttpException(805, "请求异常：" + e.getMessage(), System.currentTimeMillis() - start, e);
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection(); // 关闭连接
				postMethod = null;
			}
		}
	}

	/**
	 * 获取连接超时时间
	 * @author 苏行利
	 * @return 连接超时时间
	 * @date 2019-06-12 16:18:46
	 */
	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	/**
	 * 设置连接超时时间
	 * @author 苏行利
	 * @param connectionTimeout 连接超时时间
	 * @date 2019-06-12 16:18:56
	 */
	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
		client.getHttpConnectionManager().getParams().setConnectionTimeout(connectionTimeout * 1000); // 设置连接超时时间
	}

	/**
	 * 获取读取数据超时时间
	 * @author 苏行利
	 * @return 读取数据超时时间(单位/秒)
	 * @date 2019-06-12 16:19:05
	 */
	public int getSoTimeout() {
		return soTimeout;
	}

	/**
	 * 设置读取数据超时时间
	 * @author 苏行利
	 * @param soTimeout 读取数据超时时间(单位/秒)
	 * @date 2019-06-12 16:19:28
	 */
	public void setSoTimeout(int soTimeout) {
		this.soTimeout = soTimeout;
		client.getHttpConnectionManager().getParams().setSoTimeout(soTimeout * 1000); // 设置读取数据超时时间
	}

	/**
	 * 获取cookie
	 * @author 苏行利
	 * @return cookie(使用;进行分割)
	 * @date 2019-06-12 16:19:58
	 */
	public String getCookie() {
		StringBuffer cookie = new StringBuffer();
		Cookie[] cookies = client.getState().getCookies();
		if (cookies == null || cookies.length == 0) {
			return cookie.toString();
		}
		for (Cookie c : cookies) {
			cookie.append(c.getName()).append("=").append(c.getValue()).append(";");
		}
		return cookie.substring(0, cookie.length() - 1);
	}

	/**
	 * 清空cookie
	 * @author 苏行利
	 * @date 2019-06-12 16:20:11
	 */
	public void clearCookie() {
		client.getState().clearCookies();
	}
}
