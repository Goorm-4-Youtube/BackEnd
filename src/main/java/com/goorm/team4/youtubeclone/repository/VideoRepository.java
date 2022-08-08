package com.goorm.team4.youtubeclone.repository;

import com.goorm.team4.youtubeclone.model.Video;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Set;

public interface VideoRepository extends MongoRepository<Video,String> {
    List<Video> findById_(String id);

    List<Video> findByUserId(String userId);
    List<Video> findByIdIn(Set<String> videoList);
    List<Video> findByVideoStatus(String status);

    List<Video> findByTitleContainingIgnoreCase(String query);
}
