//package com.elcom.gateway.auth;
//
//import com.elcom.gateway.model.User;
//import java.util.Collection;
//import java.util.Collections;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
///**
// *
// * @author anhdv
// */
//public class CustomUserDetails implements UserDetails {
//    
//    private User user;
//
//    public CustomUserDetails(){
//    }
//    
//    public CustomUserDetails(User user){
//        this.user = user;
//    }
//    
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRoleName()));
//    }
//
//    @Override
//    public String getPassword() {
//        return getUser().getPassword();
//    }
//
//    @Override
//    public String getUsername() {
//        return getUser().getUserName();
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//
//    /**
//     * @return the user
//     */
//    public User getUser() {
//        return user;
//    }
//
//    /**
//     * @param user the user to set
//     */
//    public void setUser(User user) {
//        this.user = user;
//    }
//    
////    public static void main(String[] args) {
////        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
////        String p = bCryptPasswordEncoder.encode("123456");
////        System.out.println(bCryptPasswordEncoder.matches("123456", p));
////    }
//}
