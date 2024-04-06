package yoon.capstone2.svc.pinService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yoon.capstone2.svc.pinService.service.PinService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pins")
public class PinController {

    private final PinService pinService;

}
