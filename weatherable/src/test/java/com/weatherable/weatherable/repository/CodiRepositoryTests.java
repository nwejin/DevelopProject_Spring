//package com.weatherable.weatherable.repository;
//
//import com.weatherable.weatherable.DTO.CodiLikeDTO;
//import com.weatherable.weatherable.Entity.ClosetEntity;
//import com.weatherable.weatherable.Repository.ClosetRepository;
//import com.weatherable.weatherable.Repository.CodiLikeRepository;
//import com.weatherable.weatherable.Repository.CodiRepository;
//import com.weatherable.weatherable.Service.CodiLikeService;
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.sql.Timestamp;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//public class CodiRepositoryTests {
//
//
//
//
//    @Autowired
//    ClosetRepository closetRepository;
//
//    @Autowired
//    CodiLikeRepository codiLikeRepository;
//
//    @Autowired
//    CodiRepository codiRepository;
//
//    @Autowired
//    CodiLikeService codiLikeService;
//
//    @Transactional
//    @Test
//    void deleteCodiTest() {
//        codiRepository.deleteCodi(5L);
//        var codiEntity = codiRepository.getByIdAndActive(5L, false);
//        assertTrue(codiEntity.isPresent());
//    }
//
//    @Transactional
//    @Test
//    void showCodiTest() {
//        codiRepository.showCodi(5L);
//        var codiEntity = codiRepository.findByActiveAndShowingOrderByCodiDateDesc(true, true);
//        assertEquals(codiEntity.get().size(), 1);
//    }
//
//    // 코디 좋아요
//    @Test
//    @Transactional
//    void codiLikeCountTest() throws Exception {
//        CodiLikeDTO codiLikeDTO1 = CodiLikeDTO.builder()
//                .codi_id(5L)
//                .user_id(1L)
//                .build();
//        codiLikeService.likeToggle(codiLikeDTO1);
//
//        CodiLikeDTO codiLikeDTO2 = CodiLikeDTO.builder()
//                .codi_id(5L)
//                .user_id(2L)
//                .build();
//        codiLikeService.likeToggle(codiLikeDTO1);
//        codiLikeService.likeToggle(codiLikeDTO2);
//
//        long likeCount = codiLikeService.getCodiLikeCount(5L);
//        assertEquals(likeCount, 1);
//    }
//    @Test
//    @Transactional
//    void findByUserCodiIdTest() {
//        var codiEntityList = codiRepository.findByUserCodiIdAndActiveOrderByCodiDateDesc(2L, true);
//        assertEquals(codiEntityList.get().size(), 3);
//    }
//
//    @Test
//    void findByUserCodiIdAndcodiDate() {
//        Date date = new Date(124, Calendar.MARCH, 23);
//        long milliseconds = date.getTime();
//        var codiEntityList = codiRepository.findByUserCodiIdAndActiveAndCodiDate(2L, true, new Timestamp(milliseconds));
//        for(var codiEntity : codiEntityList) {
//            System.out.println(codiEntity.getId());
//        }
//        assertEquals(codiEntityList.size(), 3);
//    }
//
//    @Test
//    void findByMyCodi() {
//        var codiEntityList = codiRepository.findByUserCodiIdAndActiveOrderByCodiDateDesc(2L, true);
//        for(var codiEntity : codiEntityList.get()) {
//            System.out.println(codiEntity.getId());
//        }
//        assertEquals(codiEntityList.get().size(), 4);
//    }
//
//
//
//
//}
