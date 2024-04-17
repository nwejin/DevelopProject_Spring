package com.weatherable.weatherable.Service;

import com.weatherable.weatherable.DTO.ClosetDTO;
import com.weatherable.weatherable.DTO.UserDTO;
import com.weatherable.weatherable.DTO.UserForMyPageDTO;
import com.weatherable.weatherable.DTO.UserSizeDTO;
import com.weatherable.weatherable.Entity.AuthEntity;
import com.weatherable.weatherable.Entity.UserEntity;
import com.weatherable.weatherable.Entity.UserSizeEntity;
import com.weatherable.weatherable.Repository.AuthRepository;
import com.weatherable.weatherable.Repository.UserRepository;
import com.weatherable.weatherable.Repository.UserSizeRepository;
import com.weatherable.weatherable.enums.Style;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthRepository authRepository;

    @Autowired
    UserSizeRepository userSizeRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public String retrieveExistingPasswordById(Long id) {
        Optional<UserEntity> userEntityOptional = userRepository.findById(id);
        if(userEntityOptional.isEmpty()) {
            throw new RuntimeException("유저 정보 없음");
        }
        String hashedPassword = userEntityOptional.get().getPassword();
        return hashedPassword;
    }

    public UserService(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public boolean matchPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public boolean checkIdValidation(UserDTO userDTO) {
        return userRepository.findByUserid(userDTO.getUserid()).isEmpty();
    }

    public boolean checkPasswordsAreEqual(UserDTO userDTO) {
        return userDTO.getPassword().equals(userDTO.getPasswordConfirm());
    }

    public Long retrieveUserIndexByUserid(String userid) throws Exception {
        var userEntityOptional = userRepository.findByUseridAndActive(userid, true);
        if (userEntityOptional.isEmpty()) {
            throw new Exception("유저 정보 없음");
        }

        return userEntityOptional.get().getId();
    }

    @Value("${cloud.aws.default.imgPath}")
    private String defaultImgPath;
    @Transactional
    public String insertUser(UserDTO userDTO) throws Exception {
        if (!checkIdValidation(userDTO)) {
            throw new RuntimeException("이미 존재하는 사용자입니다.");
        }

        boolean lengthVali = userDTO.getUserid().length() >= 4;
        boolean passwordVali1 = userDTO.getPassword().length()>=6;
        if(!lengthVali) {
            throw new Exception("아이디는 최소 4글자 이상입니다.");
        }
        if(!passwordVali1) {
            throw new Exception("비밀번호는 최소 6글자 이상입니다.");
        }

        if (!checkPasswordsAreEqual(userDTO)) {
            throw new Exception("비밀번호와 비밀번호 확인이 다릅니다.");
        }

        String encodedPassword = encodePassword(userDTO.getPassword());
        userDTO.setPassword(encodedPassword);

        UserEntity userEntity = UserEntity.builder()
                .userid(userDTO.getUserid())
                .password(userDTO.getPassword())
                .nickname(userDTO.getNickname())
                .imagePath(defaultImgPath)
                .active(true)
                .build();


        userRepository.save(userEntity);
        authRepository.save(AuthEntity.builder()
                .usersEntity(userEntity)
                .build());
        userSizeRepository.save(UserSizeEntity.builder()
                        .userEntity(userEntity)
                .build());
        return userEntity.getUserid() + "회원가입 완료";
    }

    public boolean isLoginInfoEqual(String userid, String password) {

        Optional<UserEntity> userEntityOptional = userRepository.findByUseridAndActive(userid, true);
        if (userEntityOptional.isEmpty()) {
            return false;
        }
        UserEntity userEntity = userEntityOptional.get();
        String existingPassword = userEntity.getPassword();
        boolean isPasswordEqual = matchPassword(password, existingPassword);
        return isPasswordEqual;
    }

    @Transactional
    public String changeUserNickname(String nickname, Long id) throws Exception {
        if (nickname.trim().isBlank()) {
            throw new Exception("공백 문자는 닉네임으로 설정할 수 없습니다.");
        }
        userRepository.updateNickname(nickname, id);
        return "닉네임 변경 완료";
    }

    @Transactional
    public String changeUserHeightAndWeight(Double height, Double weight, Long id) throws Exception {
        if (userRepository.existsById(id)) {
            userRepository.updateHeightAndWeight(height, weight, id);
            return "정보 변경 완료";
        }
        throw new Exception("일치하는 유저 정보가 없습니다.");
    }

    @Transactional
    public String changeUserImagePath(String imagePath, Long id) {
        if (userRepository.existsById(id)) {
            userRepository.updateImagePath(imagePath, id);
            return "프로필 변경 완료";
        }
        return "일치하는 유저 정보가 없습니다.";
    }

    @Transactional
    public String changeUserIntroduction(String introduction, Long id) {
        if (userRepository.existsById(id)) {
            userRepository.updateIntroduction(introduction, id);
            return "프로필 변경 완료";
        }
        return "일치하는 유저 정보가 없습니다.";
    }

    @Transactional
    public String changeUserPassword(String password, Long id) {
        if (userRepository.existsById(id)) {
            String encodedPassword = encodePassword(password);
            userRepository.updatePassword(encodedPassword, id);
            return "프로필 변경 완료";
        }
        return "일치하는 유저 정보가 없습니다.";
    }

    @Transactional
    public String changeUserStyle(Style style, Long id) {
        if (userRepository.existsById(id)) {
            userRepository.updateStyle(style, id);
            return "프로필 변경 완료";
        }
        return "일치하는 유저 정보가 없습니다.";
    }

    @Transactional
    public String deleteUserAccount(Long id) {
        if(!userRepository.existsById(id)) {
            throw new UsernameNotFoundException("User Not Founded");
        }
        String random = "del_"+UUID.randomUUID().toString().substring(1,11);
        userRepository.deleteUser(id, random);
        return "회원탈퇴 완료";
    }


    public UserForMyPageDTO getUserInfoForMyPage(String userid) throws Exception {
        Optional<UserEntity> userEntityOptional = userRepository.findByUseridAndActive(userid, true);
       if (userEntityOptional.isEmpty()) {
           throw new UsernameNotFoundException("유저이름이 없습니다");
       }
        UserForMyPageDTO result;

            UserEntity userEntity = userEntityOptional.get();
            UserSizeDTO userSizeDTO = getUserSize(userEntity.getId());
            result = UserForMyPageDTO.builder()
                    .id(userEntity.getId())
                    .userid(userEntity.getUserid())
                    .nickname(userEntity.getNickname())
                    .favoriteStyle(userEntity.getFavoriteStyle())
                    .height(userEntity.getHeight())
                    .image_path(userEntity.getImagePath())
                    .weight(userEntity.getWeight())
                    .userSizeDTO(userSizeDTO)
                    .build();
            return result;

    }
        public UserSizeDTO getUserSize(Long userIndex) throws Exception {
            Optional<UserSizeEntity> userSizeEntityOptional = userSizeRepository.retrieveUserSizeByUserIndex(userIndex);
            if (userSizeEntityOptional.isEmpty()) {
                throw new Exception("불러올 유저 정보가 없습니다.");
            }
            UserSizeEntity userSizeEntity = userSizeEntityOptional.get();
            UserSizeDTO userSizeDTO = UserSizeDTO.builder()
                    .id(userSizeEntity.getId())
                    .b1(userSizeEntity.getB1())
                    .b2(userSizeEntity.getB2())
                    .b3(userSizeEntity.getB3())
                    .b4(userSizeEntity.getB4())
                    .b5(userSizeEntity.getB5())
                    .b6(userSizeEntity.getB6())
                    .o1(userSizeEntity.getO1())
                    .o2(userSizeEntity.getO2())
                    .o3(userSizeEntity.getO3())
                    .o4(userSizeEntity.getO4())
                    .s1(userSizeEntity.getS1())
                    .s2(userSizeEntity.getS2())
                    .t1(userSizeEntity.getT1())
                    .t2(userSizeEntity.getT2())
                    .t3(userSizeEntity.getT3())
                    .t4(userSizeEntity.getT4())
                    .user_id(userSizeEntity.getUserEntity().getId())
                    .build();
            return userSizeDTO;
    }

    public void insertUserSize(Long userIndex, UserSizeDTO userSizeDTO) throws Exception {
        Optional<UserSizeEntity> userSizeEntityOptional = userSizeRepository.retrieveUserSizeByUserIndex(userIndex);
        if (userSizeEntityOptional.isEmpty()) {
            throw new Exception("불러올 유저 정보가 없습니다.");
        }

        var existingUserEntity = userSizeEntityOptional.get().getUserEntity();
        long id = existingUserEntity.getUserSizeEntity().getId();

        UserSizeEntity userSizeEntity = UserSizeEntity.builder()
                .id(id)
                .b1(userSizeDTO.getB1())
                .b2(userSizeDTO.getB2())
                .b3(userSizeDTO.getB3())
                .b4(userSizeDTO.getB4())
                .b5(userSizeDTO.getB5())
                .b6(userSizeDTO.getB6())
                .o1(userSizeDTO.getO1())
                .o2(userSizeDTO.getO2())
                .o3(userSizeDTO.getO3())
                .o4(userSizeDTO.getO4())
                .s1(userSizeDTO.getS1())
                .s2(userSizeDTO.getS2())
                .t1(userSizeDTO.getT1())
                .t2(userSizeDTO.getT2())
                .t3(userSizeDTO.getT3())
                .t4(userSizeDTO.getT4())
                .userEntity(existingUserEntity)
                .build();
        userSizeRepository.save(userSizeEntity);
    }


}
