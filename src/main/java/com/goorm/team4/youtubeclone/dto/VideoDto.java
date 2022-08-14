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
    private String userName;
    private String videoUrl;
    private String thumbnailUrl;

    private Set<String> tags;

    private Integer likeCount;
    private Integer dislikeCount;
    private Integer viewCount;

    private VideoStatus videoStatus;

    private List<Comment> commentList;

}
