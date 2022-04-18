package com.example.demo.service.board.photoBoard;

import com.example.demo.dto.request.CommentRequest;
import com.example.demo.dto.response.PhotoBoardCommentResponse;
import com.example.demo.entity.board.photoBoard.PhotoBoard;
import com.example.demo.entity.board.photoBoard.PhotoBoardComments;
import com.example.demo.repository.board.photoBoard.PhotoBoardCommentsRepository;
import com.example.demo.repository.board.photoBoard.PhotoBoardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
public class PhotoBoardCommentsServiceImpl implements PhotoBoardCommentsService {

    @Autowired
    private PhotoBoardCommentsRepository repository;

    @Autowired
    private PhotoBoardRepository boardRepository;

    @Override
    public void register(Integer boardNo, CommentRequest commentRequest) {

        Optional<PhotoBoard> maybeBoard = boardRepository.findById(Long.valueOf(boardNo));
        PhotoBoard board = maybeBoard.get();

        PhotoBoardComments comment = PhotoBoardComments.builder()
                .comment(commentRequest.getComment())
                .boardPhoto(board)
                .writer(commentRequest.getWriter())
                .build();

        repository.save(comment);
    }

    @Override
    public List<PhotoBoardCommentResponse> list(Integer boardNo) {
        List<PhotoBoardComments> checkComments = repository.findAllPhotoBoardCommentsByBoardId(Long.valueOf(boardNo));
        List<PhotoBoardCommentResponse> response = new ArrayList<>();
        for(PhotoBoardComments comment : checkComments){
            response.add(new PhotoBoardCommentResponse(comment.getWriter(), comment.getComment(), comment.getBoardPhoto().getBoardNo(),
                    comment.getRegDate(),comment.getCommentNo()));

        }

        return response;
    }

    @Override
    public PhotoBoardCommentResponse modify(Integer commentNo, CommentRequest commentRequest) {

        Optional<PhotoBoard> maybeBoard = boardRepository.findById(Long.valueOf(commentRequest.getBoardNo()));
        PhotoBoard board = maybeBoard.get();

        PhotoBoardComments commentModify = PhotoBoardComments.builder()
                .commentNo(Long.valueOf(commentNo))
                .boardPhoto(board)
                .comment(commentRequest.getComment())
                .writer(commentRequest.getWriter())
                .regDate(commentRequest.getRegDate())
                .build();
        repository.save(commentModify);

        PhotoBoardCommentResponse response = new PhotoBoardCommentResponse(commentModify.getWriter(), commentModify.getComment(),
                commentModify.getBoardPhoto().getBoardNo(), commentModify.getRegDate(), commentModify.getCommentNo());

        return response;

    }

    @Override
    public void remove(Integer commentNo) {
        repository.deleteById(Long.valueOf(commentNo));
    }
}
