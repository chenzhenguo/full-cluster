package com.hhcf.system.other;


import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

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
	DataSource dataSource;
	// @Autowired
	// private CustomSuccessHandler customSuccessHandler;

	// /**
	// * 启动时，加载所有用户角色，权限
	// *
	// * @param auth
	// * @throws Exception
	// * void
	// */
	// @Autowired
	// public void configureGlobal(AuthenticationManagerBuilder auth) throws
	// Exception {
	// auth.inMemoryAuthentication().withUser("yiibai").password("123456").roles("USER");
	// auth.inMemoryAuthentication().withUser("admin").password("123456").roles("ADMIN");
	// auth.inMemoryAuthentication().withUser("dba").password("123456").roles("DBA");
	// logger.info("启动时，加载所有用户角色，权限configureGlobal");
	// }

	// /**
	// * 设置页面权限
	// */
	// @Override
	// protected void configure(HttpSecurity http) throws Exception {
	// // http.authorizeRequests()//
	// // .antMatchers("/home.do").access("hasRole('ROLE_ADMIN')")// 主页
	// // .antMatchers("/dba**").access("hasAnyRole('ROLE_ADMIN','ROLE_DBA')")//
	// // 其它页
	// // .and()//
	// // .formLogin().loginPage("/login").defaultSuccessUrl("/welcome.do")
	// //
	// .loginProcessingUrl("/welcome.do").successForwardUrl("/welcome.do").failureUrl("/login.do?error=error")//
	// // 登录页
	// // .and().exceptionHandling().accessDeniedPage("/basic/exception.do");
	// logger.info("设置页面权限configure");
	// }

	/**
	 * 有xml配置文件时，JAV代码不起作用
	 * 
	 * @param auth
	 * @throws Exception
	 *             void
	 */
	@Autowired
	public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
		auth.authenticationProvider(authenticationProvider());
		// logger.info("configureGlobalSecurity");
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl tokenRepositoryImpl = new JdbcTokenRepositoryImpl();
		tokenRepositoryImpl.setDataSource(dataSource);
		return tokenRepositoryImpl;
	}

	// public static void main(String[] args) {
	// BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	//// String hashedPassword = passwordEncoder.encode(password);
	// System.out.println("aaa:"+passwordEncoder.matches("123456",
	// "$2a$10$QLCzyQanmeIws6jmuGanV.mXigJaWq3RWLEB3FNMCFixIC.YTNiDG"));
	// System.out.println("aaa:"+passwordEncoder.matches("123456",
	// "$2a$10$wGwpKpbbgHZaNLlNvsBJBO5w6z7/IXeBGzMBk3nR2SnlUPaChSjNm"));
	// System.out.println("aaa:"+passwordEncoder.matches("123456",
	// "$2a$10$wGwpKpbbgHZaNLlNvsBJBO5w6z7/IXeBGzMBk3nR2SnlUPaaaaaNm"));
	//// int t = 0;
	//// String password = "123456";
	//// System.out.println(password + " -> ");
	//// for (t = 1; t <= 10; t++) {
	//// BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	//// String hashedPassword = passwordEncoder.encode(password);
	//// System.out.println("aaa:"+passwordEncoder.matches(password,
	// hashedPassword));
	//// System.out.println(hashedPassword);
	//// }
	////
	//// password = "MIKE123";
	//// System.out.println(password + " -> ");
	//// for (t = 1; t <= 10; t++) {
	//// BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	//// String hashedPassword = passwordEncoder.encode(password);
	//// System.out.println(hashedPassword);
	//// }
	// }

}
