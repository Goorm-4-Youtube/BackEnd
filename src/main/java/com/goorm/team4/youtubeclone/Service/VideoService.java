package com.goorm.team4.youtubeclone.Service;


import com.goorm.team4.youtubeclone.dto.CommentDto;
import com.goorm.team4.youtubeclone.dto.UploadVideoResponse;
import com.goorm.team4.youtubeclone.dto.VideoDto;
import com.goorm.team4.youtubeclone.model.Comment;
import com.goorm.team4.youtubeclone.model.Video;
import com.goorm.team4.youtubeclone.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final S3Service s3service;
    private final VideoRepository videoRepository;
    private final UserService userService;
    public UploadVideoResponse uploadVideo(MultipartFile multipartFile) {
        String videoUrl = s3service.uploadFile(multipartFile);
        var video = new Video();
        video.setVideoUrl(videoUrl);

        var savedVideo = videoRepository.save(video);
        return new UploadVideoResponse(savedVideo.getId(), savedVideo.getVideoUrl());

    }
    public VideoDto editVideo(VideoDto videoDto) {
    var savedVideo = getVideoById((videoDto.getId()));
    savedVideo.setTitle(videoDto.getTitle());
    savedVideo.setDescription(videoDto.getDescription());
    savedVideo.setTags(videoDto.getTags());
    savedVideo.setVideoStatus(videoDto.getVideoStatus());
    savedVideo.setUserId(videoDto.getUserId());

    videoRepository.save(savedVideo);
    return videoDto;

    }

    public void deleteVideo(String id) {
        System.out.println(("delete"));
        var temp=videoRepository.findById_(id);
        var v=temp.get(0);
        String videoUrl=v.getVideoUrl();
        String thumbnailUrl=v.getThumbnailUrl();
        s3service.deleteFile(thumbnailUrl); //S3 thumbnail 삭제
        s3service.deleteFile(videoUrl); //S3 video 삭제
        videoRepository.deleteById(id); //몽고DB 삭제

    }

    public String uploadThumbnail(MultipartFile file, String videoId) {
        var savedVideo = getVideoById(videoId);

        String thumbnailUrl =  s3service.uploadFile(file);

        savedVideo.setThumbnailUrl(thumbnailUrl);
        videoRepository.save(savedVideo);
        return thumbnailUrl;
    }

    Video getVideoById(String videoId){
        return videoRepository.findById(videoId).orElseThrow( () -> new IllegalArgumentException("Cannot Find video by id - " + videoId));
    }


    public VideoDto getVideoDetails(String videoId) {
        Video savedVideo = getVideoById(videoId);

        increaseVideoCount(savedVideo);
        userService.addVideoToHistory(videoId);

        return mapToVideoDto(savedVideo);
    }

    private void increaseVideoCount(Video savedVideo) {
        savedVideo.incrementViewCount();
        videoRepository.save(savedVideo);
    }

    public VideoDto likeVideo(String videoId) {
        Video videoById = getVideoById(videoId);

        if (userService.ifLikedVideo(videoId)) {
            videoById.decrementLikes();
            userService.removeFromLikedVideos(videoId);
        } else if (userService.ifDisLikedVideo(videoId)) {
            videoById.decrementDisLikes();
            userService.removeFromDislikedVideos(videoId);
            videoById.incrementLikes();
            userService.addToLikedVideos(videoId);
        } else {
            videoById.incrementLikes();
            userService.addToLikedVideos(videoId);
        }

        videoRepository.save(videoById);

        return mapToVideoDto(videoById);
    }

    public VideoDto disLikeVideo(String videoId) {
        Video videoById = getVideoById(videoId);

        if (userService.ifDisLikedVideo(videoId)) {
            videoById.decrementDisLikes();
            userService.removeFromDislikedVideos(videoId);
        } else if (userService.ifLikedVideo(videoId)) {
            videoById.decrementLikes();
            userService.removeFromLikedVideos(videoId);
            videoById.incrementDisLikes();
            userService.addToDisLikedVideos(videoId);
        } else {
            videoById.incrementDisLikes();
            userService.addToDisLikedVideos(videoId);
        }

        videoRepository.save(videoById);

        return mapToVideoDto(videoById);
    }

    private VideoDto mapToVideoDto(Video videoById) {
        VideoDto videoDto = new VideoDto();
        videoDto.setVideoUrl(videoById.getVideoUrl());
        videoDto.setThumbnailUrl(videoById.getThumbnailUrl());
        videoDto.setId(videoById.getId());
        videoDto.setTitle(videoById.getTitle());
        videoDto.setDescription(videoById.getDescription());
        videoDto.setTags(videoById.getTags());
        videoDto.setVideoStatus(videoById.getVideoStatus());
        videoDto.setLikeCount(videoById.getLikes().get());
        videoDto.setDislikeCount(videoById.getDisLikes().get());
        videoDto.setViewCount(videoById.getViewCount().get());
        return videoDto;
    }

    public void addComment(String videoId, CommentDto commentDto) {
        Video video = getVideoById(videoId);
        Comment comment = new Comment();
        comment.setText(commentDto.getCommentText());
        comment.setAuthorId(commentDto.getAuthorId());
        video.addComment(comment);

        videoRepository.save(video);
    }
    public void deleteComment(String videoId){
        Video video = getVideoById(videoId);
        List<Comment> commentList = video.getCommentList();
        Comment comment = commentList.get(commentList.size()-1);
        String author = comment.getAuthorId();
        System.out.println(comment);
        System.out.println(author);
        if (author.equals(author)){
            commentList.remove(commentList.size()-1);
        } else {
            System.out.println("false");
        }
        System.out.println(commentList);
        videoRepository.save(video);
    }
    public void deleteComment2(String videoId,int num){
        Video video = getVideoById(videoId);
        List<Comment> commentList = video.getCommentList();
        Comment comment = commentList.get(commentList.size()-1);
        String author = comment.getAuthorId();
        if (author.equals(author)){
            commentList.remove(num);
        } else {
            System.out.println("false");
        }
        System.out.println(commentList);
        videoRepository.save(video);
    }

    public List<CommentDto> getAllComments(String videoId) {
        Video video = getVideoById(videoId);
        List<Comment> commentList = video.getCommentList();

        return commentList.stream().map(this::mapToCommentDto).collect(Collectors.toList());
    }

    private CommentDto mapToCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setCommentText(comment.getText());
        commentDto.setAuthorId(comment.getAuthorId());
        return commentDto;
    }

    public List<VideoDto> getAllVideos() {
        return videoRepository.findAll().stream().map(this::mapToVideoDto).collect(Collectors.toList());
    }

    public List<VideoDto> getPublicVideos() {
        return videoRepository.findByVideoStatus("PUBLIC").stream().map(this::mapToVideoDto).collect(Collectors.toList());
    }

    public List<VideoDto> getMyVideos(String userId) {
        return videoRepository.findByUserId(userId).stream().map(this::mapToVideoDto).collect(Collectors.toList());
    }

    public List<VideoDto> getVideoList(Set<String> videoList){
        return videoRepository.findByIdIn(videoList).stream().map(this::mapToVideoDto).collect(Collectors.toList());
    }

    public List<VideoDto> getdisLikeVideoList(Set<String> videoList){
        return videoRepository.findByIdIn(videoList).stream().map(this::mapToVideoDto).collect(Collectors.toList());
    }

    public List<VideoDto> searchVideoList(String query){
        String[] queryList=query.split(" ");
        Set<Video> ret = new HashSet<Video>();
        List<Video> temp = new ArrayList<>();
        for(String qt: queryList){
            temp = videoRepository.findByTagsIgnoreCaseAndVideoStatus(qt,"PUBLIC");
            for(Video vt: temp) {
                ret.add(vt);
            }
        }

        for(String qt:queryList) {
            System.out.println(qt);
            temp = videoRepository.findByTitleContainingIgnoreCaseAndVideoStatus(qt, "PUBLIC");
            for (Video vt : temp) {
                ret.add(vt);
            }
        }



        return ret.stream().map(this::mapToVideoDto).collect(Collectors.toList());
        //return videoRepository.findByTitleContainingIgnoreCaseAndVideoStatus(query,"PUBLIC").stream().map(this::mapToVideoDto).collect(Collectors.toList());

    }

}
