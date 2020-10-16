//package com.elcom.gateway.service.impl;
//
//import com.elcom.gateway.auth.CustomUserDetails;
//import com.elcom.gateway.model.User;
//import com.elcom.gateway.repository.UserCustomizeRepository;
//import com.elcom.gateway.service.AuthService;
//import javax.transaction.Transactional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
///**
// *
// * @author anhdv
// */
//@Service
//public class AuthServiceImpl implements UserDetailsService, AuthService {
//
//    @Autowired
//    private UserCustomizeRepository userRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) {
//        // Kiểm tra xem user có tồn tại trong database không?
//        User user = userRepository.findByUsername(username);
//        if (user == null)
//            throw new UsernameNotFoundException("User not found with username : " + username);
//        return new CustomUserDetails(user);
//    }
//
//    // JWTAuthenticationFilter sẽ sử dụng hàm này
//    @Transactional
//    @Override
//    public UserDetails loadUserById(Long id) {
//        User user = userRepository.findById(id);
//        if (user == null)
//            throw new UsernameNotFoundException("User not found with id : " + id);
//        return new CustomUserDetails(user);
//    }
//}