package com.weatherable.weatherable.Service;

import com.weatherable.weatherable.DTO.AuthDTO;
import com.weatherable.weatherable.Entity.AuthEntity;
import com.weatherable.weatherable.Repository.AuthRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    AuthRepository authRepository;

    @Transactional
    public void updateUserRefreshToken(String refreshToken, Long id) {
        authRepository.updateRefreshToken(refreshToken, id);
    }





}
