//package com.weatherable.weatherable.repository;
//
//import com.weatherable.weatherable.Entity.AuthEntity;
//import com.weatherable.weatherable.Entity.ClosetEntity;
//import com.weatherable.weatherable.Repository.AuthRepository;
//import com.weatherable.weatherable.Repository.ClosetRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest
//public class AuthRepositoryTests {
//
//
//    @Autowired
//    AuthRepository authRepository;
//
//    @Autowired
//    ClosetRepository closetRepository;
//
//
//
//    @Test
//    void oneTest() {
//        List longMock = mock(List.class);
//        when(longMock.size()).thenReturn(3);
//        System.out.println(longMock);
//        assertEquals(longMock.size(), 3);
//    }
//
//    @Test
//    void findByUserEntityIdExistTest() {
//        Optional<AuthEntity> authEntity = authRepository.findByUserEntityId(1L);
//        System.out.println(authEntity);
//        assertTrue(authEntity.isPresent());
//        assertEquals(authEntity.get().getId(), 1);
//    }
//
//    @Test
//    void findByUserEntityIdNotExistTest() {
//        Optional<AuthEntity> authEntity = authRepository.findByUserEntityId(999L);
//        assertTrue(authEntity.isEmpty());
//    }
//
//
//
//}
