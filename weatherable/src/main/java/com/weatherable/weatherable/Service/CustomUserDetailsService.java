package com.weatherable.weatherable.Service;

import com.weatherable.weatherable.DTO.UserDTO;
import com.weatherable.weatherable.utils.CustomUserDetails;
import com.weatherable.weatherable.Entity.UserEntity;
import com.weatherable.weatherable.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUserid(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        UserDTO user = UserDTO.builder()
                .id(userEntity.getId())
                .userid(userEntity.getUserid())
                .password(userEntity.getPassword())
                .nickname(userEntity.getNickname())
                .imagePath(userEntity.getImagePath())
                .height(userEntity.getHeight())
                .weight(userEntity.getWeight())
                .introduction(userEntity.getIntroduction())
                .favoriteStyle(userEntity.getFavoriteStyle())
                .build();

         CustomUserDetails customUserDetails = new CustomUserDetails(user);
        return customUserDetails;


    }



}
