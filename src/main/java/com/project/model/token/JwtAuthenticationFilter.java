package com.project.model.token;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.project.model.MemberDO;
import com.project.model.MemberSO;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberSO memberSo;
    
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, MemberSO memberSo) {
    	this.jwtTokenProvider = jwtTokenProvider;
    	this.memberSo = memberSo;
    }
    
    @Override
    protected void doFilterInternal(
    		HttpServletRequest request, 
    		HttpServletResponse response, 
    		FilterChain chain) throws ServletException, IOException {
    	
    	String token = resolveToken(request);
    	
    	if(token != null && jwtTokenProvider.validateToken(token)) {
    		int member_id = jwtTokenProvider.getMember_id(token);
    		MemberDO member = memberSo.findMember_idByM_acctid(token);
    		
    		if (member != null) {
    		    JwtAuthenticationToken authentication = new JwtAuthenticationToken(
    		            member, null, memberSo.getAuthorities(member));  // m_role에 따른 권한 부여
    		    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // SecurityContext에 인증 정보 설정
                SecurityContextHolder.getContext().setAuthentication(authentication);
    		}
    	}
    	
    	chain.doFilter(request, response);
    }
    
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);  // "Bearer " 이후의 토큰 부분만 반환
        }
        return null;
    }
    
}
