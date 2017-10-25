package com.palusers.email.logger;

import com.palusers.email.common.AppConstants;
import com.palusers.email.utils.PropertyFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoggerFactory {

	@Autowired
	private PropertyFileReader propertyFileReader;
	@Autowired
	private ILogger fileAppenderLog;

	public ILogger getLoggerInstance() {		
		String loggerMode = propertyFileReader.getMessage(AppConstants
				.LOGGER_SELECT);
		if (loggerMode.equalsIgnoreCase(AppConstants.FILE)) {
			return fileAppenderLog;
		} 
		//TODO
		return fileAppenderLog;
	}
}
