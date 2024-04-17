package com.weatherable.weatherable.Repository;

import com.weatherable.weatherable.Model.ClothInfoEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClothInfoRepository extends MongoRepository<ClothInfoEntity, String> {
    List<ClothInfoEntity> findByProductNameContainingIgnoreCase(String keyword);
    List<ClothInfoEntity> findByBrandIgnoreCase(String keyword);
}
