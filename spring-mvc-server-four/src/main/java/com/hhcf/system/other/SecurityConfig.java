package com.hhcf.system.other;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.alibaba.fastjson.JSON;

/**
 * @Title: SecurityConfig
 * @Description:
 * @Author: zhaotf
 * @Since:2018年6月12日 下午3:13:54
 * @see {@linkplain https://www.yiibai.com/spring-security/spsecurity-hello-world-annotation-example.html#article-start}
 * @see {@linkplain https://www.yiibai.com/spring-security/spring-security-4-hibernate-annotation-example.html}
 * @see {@linkplain http://www.tianshouzhi.com/api/tutorials/spring_security_4/265}
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private static Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

	@Autowired
	@Qualifier("userDetailService")
	private UserDetailsService userDetailsService;
	@Autowired
	private CustomSuccessHandler customSuccessHandler;

//	/**
//	 * 启动时，加载所有用户角色，权限
//	 * 
//	 * @param auth
//	 * @throws Exception
//	 *             void
//	 */
//	@Autowired
//	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//		auth.inMemoryAuthentication().withUser("yiibai").password("123456").roles("USER");
//		auth.inMemoryAuthentication().withUser("admin").password("123456").roles("ADMIN");
//		auth.inMemoryAuthentication().withUser("dba").password("123456").roles("DBA");
//		logger.info("启动时，加载所有用户角色，权限configureGlobal");
//	}
//
//	/**
//	 * 设置页面权限
//	 */
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http.authorizeRequests()//
//				.antMatchers("/home.do").access("hasRole('ROLE_ADMIN')")// 主页
//				// .antMatchers("/hello.do").access("hasRole('ROLE_ADMIN')")//
//				// 其它页
//				.antMatchers("/dba**").access("hasAnyRole('ROLE_ADMIN','ROLE_DBA')")// 其它页
//				.and()//
//				.formLogin().loginPage("/login.do").defaultSuccessUrl("/welcome.do").loginProcessingUrl("/welcome.do")
//				.successForwardUrl("/welcome.do").failureUrl("/login.do?error=error")// 登录页
//		;
//		logger.info("设置页面权限configure");
//	}

	@Autowired
	public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
		logger.info("configureGlobalSecurity");
	}

}
