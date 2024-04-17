package com.weatherable.weatherable.Controller;

import com.weatherable.weatherable.DTO.CommentDTO;
import com.weatherable.weatherable.Service.CommentService;
import com.weatherable.weatherable.Service.JwtUtilsService;
import com.weatherable.weatherable.enums.DefaultRes;
import com.weatherable.weatherable.enums.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    CommentService commentService;

    @Autowired
    JwtUtilsService jwtUtilsService;

    @DeleteMapping("")
    public ResponseEntity<DefaultRes<String>> deleteSingleComment(@RequestBody CommentDTO commentDTO) {
        try {
            Long id = commentDTO.getId();
            String result = commentService.deleteComment(id);
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.OK, result),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.BAD_REQUEST, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DefaultRes<List<CommentDTO>>> retrieveAllComment(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.OK, "댓글 fetch 완료", commentService.retrieveAllComment(id)),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.BAD_REQUEST, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/single/{id}")
    public ResponseEntity<DefaultRes<CommentDTO>> retrieveSingleComment(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.OK, "댓글 fetch 완료", commentService.retrieveSingleComment(id)),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.BAD_REQUEST, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("")
    public ResponseEntity<DefaultRes<String>> insertComment(@RequestBody CommentDTO commentDTO, @RequestHeader("Authorization") String accessToken) {
        try {
            String userid = jwtUtilsService.retrieveUserid(accessToken);
            commentDTO.setUserid(userid);
            commentService.insertComment(commentDTO);
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.CREATED, "댓글 작성 완료"),
                    HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.BAD_REQUEST, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("")
    public ResponseEntity<DefaultRes<String>> updateComment(@RequestBody CommentDTO commentDTO, @RequestHeader("Authorization") String accessToken) throws Exception {
        try {
            String userid = jwtUtilsService.retrieveUserid(accessToken);
            commentDTO.setUserid(userid);
            commentService.updateComment(commentDTO);
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.CREATED, "댓글 수정 완료"),
                    HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.BAD_REQUEST, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }


}
