package com.costumemania.msusers.configuration.security;

import com.costumemania.msusers.model.entity.UserEntity;
import com.costumemania.msusers.repository.IUserRepository;
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
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private IUserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

//        UserEntity userEntityByUsername = userRepository.findOneByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Not found user: " + username));
//        UserEntity userEntiByEmail = userRepository.findOneByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Not found user: " + username));

        Optional<UserEntity> userEntityByUsername = userRepository.findOneByUsername(username);
        Optional<UserEntity> userEntityByEmail = userRepository.findOneByEmail(username);
        UserEntity userEntity = new UserEntity();

        if (!userEntityByUsername.isPresent() && !userEntityByEmail.isPresent())
            new UsernameNotFoundException("Not found user: " + username);
        if (userEntityByUsername.isPresent()) {
            userEntity = userEntityByUsername.get();
        } else {
            userEntity = userEntityByEmail.get();
        }

        Collection<GrantedAuthority> authorities = new HashSet<>();
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + userEntity.getRole().name());
        authorities.add(authority);

        return new User(userEntity.getUsername(), userEntity.getPassword(), true, true, true, true, authorities);
    }
}
