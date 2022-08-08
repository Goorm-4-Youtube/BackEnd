package com.goorm.team4.youtubeclone.dto;


import com.goorm.team4.youtubeclone.model.Comment;
import com.goorm.team4.youtubeclone.model.VideoStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoDto {


    private String id;
    private String title;
    private String description;
    private String userId;
    private Integer likeCount;
    private Integer dislikeCount;
    private Set<String> tags;
    private String videoUrl;
    private VideoStatus videoStatus;
    private Integer viewCount;
    private String thumbnailUrl;
    private List<Comment> commentList;

}
