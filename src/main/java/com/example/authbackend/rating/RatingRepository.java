package com.example.authbackend.rating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Query("SELECT r FROM Rating r WHERE r.activity.id = :activityId ORDER BY r.createdAt DESC")
    List<Rating> findByActivityId(@Param("activityId") Long activityId);

    @Query("SELECT r FROM Rating r WHERE r.user.id = :userId ORDER BY r.createdAt DESC")
    List<Rating> findByUserId(@Param("userId") Long userId);

    Optional<Rating> findByUserIdAndActivityId(Long userId, Long activityId);

    @Query("SELECT AVG(r.activityScore) FROM Rating r WHERE r.activity.id = :activityId")
    Double getAverageActivityScore(@Param("activityId") Long activityId);

    @Query("SELECT AVG(r.guideScore) FROM Rating r WHERE r.activity.id = :activityId")
    Double getAverageGuideScore(@Param("activityId") Long activityId);

    Long countByActivityId(Long activityId);
}
