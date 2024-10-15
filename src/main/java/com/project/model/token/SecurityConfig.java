package com.project.model.token;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.project.model.MemberSO;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {
	
	private final JwtTokenProvider jwtTokenProvider;
	private final MemberSO memberSo;
	
	public SecurityConfig(JwtTokenProvider jwtTokenProvider, MemberSO memberSo) {
		this.jwtTokenProvider = jwtTokenProvider;
		this.memberSo = memberSo;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			// CSRF 비활성화 (REST API 등에서 주로 사용)
			.csrf(AbstractHttpConfigurer::disable)
			
            // 세션이 아님
            .sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            .authorizeHttpRequests(authorize -> authorize
            		// 인증이 필요하지 않은 페이지
                    .requestMatchers("/login").permitAll()
                    // 그 외 모든 페이지는 인증 필요
                    .anyRequest().authenticated())
            
            // 토큰 사용
            .httpBasic(AbstractHttpConfigurer::disable)
            
            // JWT 인증을 위하여 직접 구현한 필터를 UsernamePasswordAuthenticationFilter 전에 실행
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, memberSo),
                    UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
