package com.marketplace.marketplace.jwt;

import com.marketplace.marketplace.user.User;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principle;

    public CustomAuthenticationToken(Object principle, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principle = principle;
    }


    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.principle;
    }
}
