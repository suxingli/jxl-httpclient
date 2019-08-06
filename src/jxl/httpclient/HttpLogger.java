package jxl.httpclient;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 * Http日志
 * @author 苏行利
 * @date 2019-06-12 15:54:55
 */
public class HttpLogger {
	private static Logger logger; // 日志记录器

	/**
	 * 获取日志记录器
	 * @author 苏行利
	 * @return 日志记录器
	 * @date 2019-06-12 15:54:59
	 */
	public static final Logger getLogger() {
		if (logger == null) {
			logger = LogManager.getLogger("jxl.httpclient");
			logger.setLevel(Level.DEBUG);
			logger.setAdditivity(false);
			PatternLayout console_layout = new PatternLayout();
			console_layout.setConversionPattern("[jxl-httpclient] %d{yyyy-MM-dd HH:mm:ss,SSS} %p %l - %m%n");
			ConsoleAppender console_appender = new ConsoleAppender(console_layout);
			console_appender.activateOptions();
			PatternLayout file_layout = new PatternLayout();
			file_layout.setConversionPattern("[jxl-httpclient] %d{yyyy-MM-dd HH:mm:ss,SSS} %p %l - %m%n");
			DailyRollingFileAppender appender = new DailyRollingFileAppender();
			appender.setLayout(file_layout);
			appender.setFile(System.getProperty("user.dir") + "/../logs/jxl-httpclient/jxl-httpclient.log");
			appender.setDatePattern("'.'yyyy-MM-dd'.log'");
			appender.setEncoding("UTF-8");
			appender.activateOptions();
			logger.addAppender(console_appender);
			logger.addAppender(appender);
		}
		return logger;
	}

}
