package com.project.model.token;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class JwtAuthenticationToken extends AbstractAuthenticationToken  {

	// 직렬화 버전 ID
    private static final long serialVersionUID = 1L;

    private final Object principal;
    private Object credentials;

    // 인증 전
    public JwtAuthenticationToken(Object principal) {
        super(null);
        this.principal = principal;
        this.credentials = null;
        setAuthenticated(false);
    }

    // 인증 후
    public JwtAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }
}
