package yoon.capstone2.svc.pinService.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yoon.capstone2.svc.pinService.service.PinService;

@Tag(name="핀 서비스 관련 API", description = "version1")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pins")
public class PinController {

    private final PinService pinService;

    //해당 핀 불러오기 GET(/{idx})
    @Operation(summary = "지도에 있는 특정 핀 정보 불러오기")
    @GetMapping("/{pinIdx}")
    public ResponseEntity<?> getPin(@PathVariable long pinIdx){
        return null;
    }

    //전체 핀 불러오기 GET(/list)
    @Operation(summary = "지도의 전체 핀 정보 불러오기")
    @GetMapping("/list")
    public ResponseEntity<?> getPinList(){
        return null;
    }

    //핀 자세히 보기  GET(/detail/{idx})

    @Operation(summary = "지도에 있는 특정 핀의 자세한 정보 불러오기")
    @GetMapping("/detail/{pinIdx}")
    public ResponseEntity<?> getPinDetail(@PathVariable long pinIdx){
        return null;
    }

    //핀 만들기      POST()
    @Operation(summary = "지도에 핀 만들기")
    @PostMapping
    public ResponseEntity<?> createPin(){
        return null;
    }

    //핀 수정하기    PUT(/{idx})
    @Operation(summary = "지도에 있는 특정 핀 수정하기")
    @PutMapping("/{pinIdx}")
    public ResponseEntity<?> updatePin(@PathVariable long pinIdx){
        return null;
    }

    //핀 삭제하기    DELETE(/{idx})
    @Operation(summary = "지도에 있는 특정 핀 삭제하기")
    @DeleteMapping("/{pinIdx}")
    public ResponseEntity<?> deletePin(@PathVariable long pinIdx){
        return null;
    }

}
