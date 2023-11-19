//package com.costumemania.msusers.configuration.security;
//
//import com.costumemania.msusers.model.entity.UserEntity;
//import com.costumemania.msusers.repository.IUserRepository;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//public class UserDetailServiceImpl implements UserDetailsService {
//
//    private IUserRepository userRepository;
//
//    public UserDetailServiceImpl(IUserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        UserEntity userEntity = userRepository.findOneByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Username: " + email + " Not found"));
//
//
//        return new UserDetailsImpl(userEntity);
//    }
//}
