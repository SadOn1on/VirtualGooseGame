package com.goosegame.backend.security;

import java.util.List;

public class AuthorityResponse {

    private String username;

    private List<String> authorities;

    public AuthorityResponse() {
    }

    public AuthorityResponse(String username, List<String> authorities) {
        this.username = username;
        this.authorities = authorities;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuthorityResponse that = (AuthorityResponse) o;
        return username.equals(that.username) && authorities.equals(that.authorities);
    }

    @Override
    public int hashCode() {
        int result = username.hashCode();
        result = 31 * result + authorities.hashCode();
        return result;
    }
}
