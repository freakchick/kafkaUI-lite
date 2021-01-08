package com.jq.kafkaui.conf;

import com.alibaba.fastjson.JSON;
import com.jq.kafkaui.util.IPUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 日志拦截器
 * 
 * @author jiangqiang
 * @date 2019年3月19日下午4:30:56
 */
@Component
@Slf4j
public class LogInterceptor implements HandlerInterceptor {

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3) throws Exception {
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3) throws Exception {

	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse arg1, Object arg2) throws Exception {
		String servletPath = request.getServletPath();
		Map<String, String[]> map = request.getParameterMap();

		log.info("url={};params={}",  servletPath, JSON.toJSONString(map));

		return true;
	}

}