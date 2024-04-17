//package com.weatherable.weatherable.repository;
//
//import com.weatherable.weatherable.Entity.AuthEntity;
//import com.weatherable.weatherable.Entity.ClosetEntity;
//import com.weatherable.weatherable.Repository.AuthRepository;
//import com.weatherable.weatherable.Repository.ClosetRepository;
//import jakarta.transaction.Transactional;
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
//public class ClosetRepositoryTests {
//
//
//
//
//    @Autowired
//    ClosetRepository closetRepository;
//
//
//    // 있을 경우 리스트로 반환
//    @Test
//    void retrieveAllClothByUserIndexExistTest() {
//        Optional<List<ClosetEntity>> closetEntityList = closetRepository.retrieveAllClothByUserIndex(1L);
//        assertTrue(closetEntityList.isPresent());
//        for (ClosetEntity closetEntity : closetEntityList.get()) {
//            System.out.println(closetEntity.getProductName());
//        }
//    }
//
//    // 없을 경우 빈 배열로 반환
//    @Test
//    void retrieveAllClothByUserIndexNotExistTest() {
//        Optional<List<ClosetEntity>> closetEntityList = closetRepository.retrieveAllClothByUserIndex(999L);
//        assertEquals(closetEntityList.get().size(), 0);
//    }
//
//    // 옷 좋아요
//    @Test
//    @Transactional
//    void likeClosetTest() {
//        closetRepository.likeCloset(3L);
//        var closetEntity = closetRepository.getByIdAndActive(3L, true);
//        assertTrue(closetEntity.get().isLiked());
//    }
//
//    // 옷 좋아요 취소
//    @Test
//    @Transactional
//    void UnlikeClosetTest() {
//        closetRepository.unlikeCloset(3L);
//        var closetEntity = closetRepository.getByIdAndActive(3L, true);
//        assertFalse(closetEntity.get().isLiked());
//    }
//
//    // 옷 삭제
//    @Test
//    @Transactional
//    void deleteCloset() {
//        closetRepository.deleteCloset(3L);
//        var closetEntity = closetRepository.getByIdAndActive(3L, true);
//        assertTrue(closetEntity.isEmpty());
//    }
//
//    // 옷 삭제 2
//    @Test
//    @Transactional
//    void deleteCloset2() {
//        closetRepository.deleteCloset(3L);
//        var closetEntity = closetRepository.getByIdAndActive(3L, false);
//        assertTrue(closetEntity.isPresent());
//    }
//
//    // 옷 삭제 후 유저 옷 불러왔을 때 안보이는지 확인
//    @Test
//    @Transactional
//    void deleteCloset3() {
//        closetRepository.deleteCloset(3L);
//        Optional<List<ClosetEntity>> closetEntityList = closetRepository.retrieveAllClothByUserIndex(1L);
//        int lastIndex = closetEntityList.get().size()-1;
//        assertEquals(closetEntityList.get().get(lastIndex).getId(), 4);
//
//    }
//
//    @Test
//    void getClosetEntityByProductName() {
//        var closetEntityList = closetRepository.findByProductNameAndUserClosetIdAndActive("울 꽈배기 니트", 1L, true);
//        System.out.println(closetEntityList.get(0));
//        System.out.println(closetEntityList.size());
//        assertEquals(closetEntityList.get(0).getId(), 3);
//    }
//
//    @Test
//    void getByIdWithNull() {
//
//    ClosetEntity closetShoesEntity = closetRepository.getByIdAndActive(null, true).orElseGet(ClosetEntity::new);
//        System.out.println(closetShoesEntity.getId());
//        assertNull(closetShoesEntity.getId());
//    }
//
//
//}
