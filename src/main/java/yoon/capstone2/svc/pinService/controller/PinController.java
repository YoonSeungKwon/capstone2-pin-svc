package yoon.capstone2.svc.pinService.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import yoon.capstone2.svc.pinService.dto.request.CommentRequest;
import yoon.capstone2.svc.pinService.dto.request.PinRequest;
import yoon.capstone2.svc.pinService.dto.response.CommentResponse;
import yoon.capstone2.svc.pinService.dto.response.PinDetailResponse;
import yoon.capstone2.svc.pinService.dto.response.PinResponse;
import yoon.capstone2.svc.pinService.service.CommentService;
import yoon.capstone2.svc.pinService.service.PinService;

import java.util.List;

@Tag(name="핀 서비스 관련 API", description = "version1")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pins")
public class PinController {

    private final PinService pinService;

    private final CommentService commentService;

    //해당 핀 불러오기 GET(/{idx})
    @Operation(summary = "지도에 있는 특정 핀 정보 불러오기")
    @GetMapping("/{pinIdx}")
    public ResponseEntity<PinResponse> getPin(@PathVariable long pinIdx){

        PinResponse result = pinService.getPin(pinIdx);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //전체 핀 불러오기 GET(/list)
    @Operation(summary = "지도의 전체 핀 정보 불러오기")
    @GetMapping("/list/{mapIdx}")
    public ResponseEntity<List<PinResponse>> getPinList(@PathVariable long mapIdx){

        List<PinResponse> result = pinService.getPinList(mapIdx);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(summary = "지도 해당 날짜의 핀 정보 불러오기")
    @GetMapping("/list/{mapIdx}/{day}")
    public ResponseEntity<List<PinResponse>> getDatePinList(@PathVariable long mapIdx, @PathVariable int day){

        List<PinResponse> result = pinService.getDatePin(mapIdx, day);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //핀 자세히 보기  GET(/detail/{idx})

    @Operation(summary = "지도에 있는 특정 핀의 자세한 정보 불러오기")
    @GetMapping("/detail/{pinIdx}")
    public ResponseEntity<PinDetailResponse> getPinDetail(@PathVariable long pinIdx){

        PinDetailResponse result = pinService.getDetail(pinIdx);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //핀 만들기      POST()
    @Operation(summary = "지도에 핀 만들기")
    @PostMapping("/{mapIdx}")
    public ResponseEntity<PinResponse> createPin(@PathVariable long mapIdx, @RequestPart(required = false) MultipartFile file, @RequestPart PinRequest dto){

        PinResponse result = pinService.createPin(mapIdx, file, dto);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    //핀 수정하기    PUT(/{idx})
    @Operation(summary = "지도에 있는 특정 핀 수정하기")
    @PutMapping("/{mapIdx}/{pinIdx}")
    public ResponseEntity<PinResponse> updatePin(@PathVariable long mapIdx, @PathVariable long pinIdx, @RequestPart(required = false) MultipartFile file, @RequestPart PinRequest dto){

        PinResponse result = pinService.updatePin(mapIdx, pinIdx, file, dto);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //핀 삭제하기    DELETE(/{idx})
    @Operation(summary = "지도에 있는 특정 핀 삭제하기")
    @DeleteMapping("/{pinIdx}")
    public ResponseEntity<?> deletePin(@PathVariable long pinIdx){

        pinService.deletePin(pinIdx);

        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "댓글 불러오기")
    @GetMapping("/{pinIdx}/comments")
    public ResponseEntity<List<CommentResponse>> getCommentList(@PathVariable long pinIdx){

        List<CommentResponse> result = commentService.getList(pinIdx);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(summary = "댓글 쓰기")
    @PostMapping("/{pinIdx}/post")
    public ResponseEntity<CommentResponse> createComment(@PathVariable long pinIdx, @RequestBody CommentRequest dto){

        CommentResponse result = commentService.postComment(pinIdx, dto);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

}
