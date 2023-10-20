package com.myblog.myblog.service.impl;

import com.myblog.myblog.entity.Comment;
import com.myblog.myblog.entity.Post;
import com.myblog.myblog.exception.BlogAPIException;
import com.myblog.myblog.exception.ResourceNotFoundException;
import com.myblog.myblog.payload.CommentDto;
import com.myblog.myblog.repository.CommentRepository;
import com.myblog.myblog.repository.PostRepository;
import com.myblog.myblog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepository;
    private PostRepository postRepository;

    private ModelMapper mapper;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository,ModelMapper mapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;

        this.mapper=mapper;
    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        Comment comment = mapToEntity(commentDto);


        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("post", "id", postId));


        comment.setPost(post);
       Comment newComment =commentRepository.save(comment);
        return mapToDto(newComment);

    }

    @Override
    public List<CommentDto> getCommentsByPostId(long postId) {
        List<Comment> comments=commentRepository.findByPostId(postId);

        return comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());


    }

    @Override
    public CommentDto getCommentById(Long postId, Long commentId) {

       Post post=postRepository.findById(postId).orElseThrow(
                ()-> new ResourceNotFoundException("post","id",postId));

        Comment comment=commentRepository.findById(commentId).orElseThrow(
                ()-> new ResourceNotFoundException("Comment","id",commentId));

if (!Objects.equals(comment.getPost().getId(), post.getId())){

    throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment does not belog to post");
}
return mapToDto(comment);


    }

    @Override
    public CommentDto updateComment(long postId, long id, CommentDto commentDto) {


        Post post=postRepository.findById(postId).orElseThrow(
                ()-> new ResourceNotFoundException("post","id",postId));



        Comment comment=commentRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Comment","id",id));
if(Objects.equals(comment.getPost().getId(), post.getId())){
    throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment does not belong to this post");
}

comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());
        Comment updateComment= commentRepository.save(comment);

        return mapToDto(updateComment);
    }

    @Override
    public void deleteComment(long postId, long id) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("post", "id", postId));


        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Comment", "id", id));
        if (!Objects.equals(comment.getPost().getId(), post.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to this post");
        }
        commentRepository.deleteById(comment.getId());


    }

    CommentDto mapToDto(Comment newComment) {

        CommentDto commentDto=mapper.map(newComment,CommentDto.class);
     return commentDto;
    }

    Comment mapToEntity(CommentDto commentDto) {

       Comment comment=mapper.map(commentDto,Comment.class);
return comment;
    }}
