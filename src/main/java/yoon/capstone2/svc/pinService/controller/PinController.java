package yoon.capstone2.svc.pinService.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yoon.capstone2.svc.pinService.service.PinService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pins")
public class PinController {

    private final PinService pinService;

    //해당 핀 불러오기 GET(/{idx})
    @GetMapping("/{pinIdx}")
    public ResponseEntity<?> getPin(@PathVariable long pinIdx){
        return null;
    }

    //전체 핀 불러오기 GET(/list)
    @GetMapping("/list")
    public ResponseEntity<?> getPinList(){
        return null;
    }

    //핀 자세히 보기  GET(/detail/{idx})
    @GetMapping("/detail/{pinIdx}")
    public ResponseEntity<?> getPinDetail(@PathVariable long pinIdx){
        return null;
    }

    //핀 만들기      POST()
    @PostMapping
    public ResponseEntity<?> createPin(){
        return null;
    }

    //핀 수정하기    PUT(/{idx})
    @PutMapping("/{pinIdx}")
    public ResponseEntity<?> updatePin(@PathVariable long pinIdx){
        return null;
    }

    //핀 삭제하기    DELETE(/{idx})
    @DeleteMapping("/{pinIdx}")
    public ResponseEntity<?> deletePin(@PathVariable long pinIdx){
        return null;
    }

}
