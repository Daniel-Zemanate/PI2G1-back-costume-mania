package com.costumemania.msbills.configuration.security;

import com.costumemania.msbills.configuration.feign.UserFeign;
import com.costumemania.msbills.repository.UsersRepositoryFeign;
import com.costumemania.msbills.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userFeignService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserFeign userFeign = userFeignService.getByUsername(username);

        Collection<GrantedAuthority> authorities = new HashSet<>();
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + userFeign.getRole().name());
        authorities.add(authority);

        return new User(userFeign.getUsername(), userFeign.getPassword(), true, true, true, true, authorities);
    }
}
