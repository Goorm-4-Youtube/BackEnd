package com.goorm.team4.youtubeclone.Controller;


import com.goorm.team4.youtubeclone.Service.VideoService;
import com.goorm.team4.youtubeclone.dto.CommentDto;
import com.goorm.team4.youtubeclone.dto.UploadVideoResponse;
import com.goorm.team4.youtubeclone.dto.VideoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UploadVideoResponse uploadVideo(@RequestParam("file") MultipartFile file) {
        return videoService.uploadVideo(file);
    }

    @PostMapping("/thumbnail")
    @ResponseStatus(HttpStatus.CREATED)
    public String uploadThumbnail(@RequestParam("file") MultipartFile file, @RequestParam("videoId") String videoId) {
        return videoService.uploadThumbnail(file, videoId);
    }


    @PostMapping("/{videoId}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable String videoId)
    {
        videoService.deleteVideo(videoId);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public VideoDto editVideoMetadata(@RequestBody VideoDto videoDto) {
        return videoService.editVideo(videoDto);
    }

    @GetMapping("/{videoId}")
    @ResponseStatus(HttpStatus.OK)
    public VideoDto getVideoDetails(@PathVariable String videoId) {
        return videoService.getVideoDetails(videoId);
    }

    @PostMapping("/{videoId}/like")
    @ResponseStatus(HttpStatus.OK)
    public VideoDto likeVideo(@PathVariable String videoId) {
        return videoService.likeVideo(videoId);
    }

    @PostMapping("/{videoId}/disLike")
    @ResponseStatus(HttpStatus.OK)
    public VideoDto disLikeVideo(@PathVariable String videoId) {
        return videoService.disLikeVideo(videoId);
    }

    @PostMapping("/{videoId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public void addComment(@PathVariable String videoId, @RequestBody CommentDto commentDto) {
        videoService.addComment(videoId, commentDto);
    }

    @PostMapping("/{videoId}/comment/delete")
    @ResponseStatus(HttpStatus.OK)
    public void DeleteComment(@PathVariable String videoId) {
        videoService.deleteComment(videoId);
    }

    @GetMapping("/{videoId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getAllComments(@PathVariable String videoId) {
        return videoService.getAllComments(videoId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<VideoDto> getAllVideos() {
        return videoService.getAllVideos();
    }


    @GetMapping("/public")
    @ResponseStatus(HttpStatus.OK)
    public List<VideoDto> getPublicVideos(){return videoService.getPublicVideos();}


    @GetMapping("/{query}/search")
    @ResponseStatus(HttpStatus.OK)
    public List<VideoDto> searchVideo(@PathVariable String query) {

        System.out.println(query);
        return videoService.searchVideoList(query);
    }

    @GetMapping("/{userId}/videos")
    @ResponseStatus(HttpStatus.OK)
    public List<VideoDto> getMyVideos(@PathVariable String userId) {
        return videoService.getMyVideos(userId);
    }
}
