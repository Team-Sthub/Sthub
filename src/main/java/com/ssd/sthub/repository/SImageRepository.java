package com.ssd.sthub.repository;

import com.ssd.sthub.domain.SImage;
import com.ssd.sthub.domain.Secondhand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SImageRepository extends JpaRepository<SImage, Long> {
    List<SImage> findAllBySecondhand(Secondhand secondhand);
    List<SImage> findAllBySecondhandId(Long secondhandId);
    void deleteByPath(String path);
}
