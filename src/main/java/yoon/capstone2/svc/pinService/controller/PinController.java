package yoon.capstone2.svc.pinService.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yoon.capstone2.svc.pinService.dto.request.PinRequest;
import yoon.capstone2.svc.pinService.dto.response.PinDetailResponse;
import yoon.capstone2.svc.pinService.dto.response.PinResponse;
import yoon.capstone2.svc.pinService.service.PinService;

import java.util.List;

@Tag(name="핀 서비스 관련 API", description = "version1")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pins")
public class PinController {

    private final PinService pinService;

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

    //핀 자세히 보기  GET(/detail/{idx})

    @Operation(summary = "지도에 있는 특정 핀의 자세한 정보 불러오기")
    @GetMapping("/detail/{pinIdx}")
    public ResponseEntity<?> getPinDetail(@PathVariable long pinIdx){

        PinDetailResponse result = pinService.getDetail(pinIdx);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //핀 만들기      POST()
    @Operation(summary = "지도에 핀 만들기")
    @PostMapping()
    public ResponseEntity<PinResponse> createPin(@RequestBody PinRequest dto){

        PinResponse result = pinService.createPin(dto);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    //핀 수정하기    PUT(/{idx})
    @Operation(summary = "지도에 있는 특정 핀 수정하기")
    @PutMapping("/{pinIdx}")
    public ResponseEntity<PinResponse> updatePin(@PathVariable long pinIdx, @RequestBody PinRequest dto){

        PinResponse result = pinService.updatePin(pinIdx, dto);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //핀 삭제하기    DELETE(/{idx})
    @Operation(summary = "지도에 있는 특정 핀 삭제하기")
    @DeleteMapping("/{pinIdx}")
    public ResponseEntity<?> deletePin(@PathVariable long pinIdx){

        pinService.deletePin(pinIdx);

        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

}
