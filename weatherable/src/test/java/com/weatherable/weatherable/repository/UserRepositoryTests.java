//package com.weatherable.weatherable.repository;
//
//import com.weatherable.weatherable.Entity.AuthEntity;
//import com.weatherable.weatherable.Repository.AuthRepository;
//import com.weatherable.weatherable.Repository.ClosetRepository;
//import com.weatherable.weatherable.Repository.UserRepository;
//import com.weatherable.weatherable.Service.UserService;
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//import java.util.Optional;
//
//import static com.weatherable.weatherable.enums.Style.Sporty;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest
//public class UserRepositoryTests {
//
//
//    @Autowired
//    AuthRepository authRepository;
//
//    @Autowired
//    ClosetRepository closetRepository;
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    UserService userService;
//
//
//
//
//    @Transactional
//    @Test
//    void changeUserProfileImage() {
//        userRepository.updateImagePath("aaaaaa", 1L);
//        var userEntityOptional = userRepository.findByUserid("aaaa");
//        assertEquals(userEntityOptional.get().getImagePath(), "aaaaaa");
//    }
//
//    @Test
//    void findByUserIdAndActiveTest() {
//        var userEntityOptional = userRepository.findByUseridAndActive("aaaa", true);
//        assertEquals(1, userEntityOptional.get().getId());
//    }
//
//    @Test
//    void findByUserIdAndActiveWithNotExistTest() {
//        var userEntityOptional = userRepository.findByUseridAndActive("wrwvsvsf", true);
//        assertTrue(userEntityOptional.isEmpty());
//    }
//
//    @Test
//    void loginTest() {
//        boolean isUserExist = userRepository.existsByUseridAndPassword("aaaa", "$2a$12$4c3HQ096h1hRdw8klLJ.Fe7.ftCchO27cZ3xw1JfUNK6.ji.FsqYe");
//        assertTrue(isUserExist);
//    }
//
//
//    @Transactional
//    @Test
//    void deleteUserTest() {
//        userService.deleteUserAccount(1L);
//        var userEntityOptional = userRepository.findByUseridAndActive("aaaa", true);
//        assertTrue(userEntityOptional.isEmpty());
//    }
//
//  @Transactional
//    @Test
//    void updateStyleUserTest() {
//        userRepository.updateStyle(Sporty, 1L);
//        var userEntityOptional = userRepository.findByUseridAndActive("test", true);
//        assertEquals(userEntityOptional.get().getFavoriteStyle(), Sporty);
//    }
//
//}
