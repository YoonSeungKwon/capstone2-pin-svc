package yoon.capstone2.svc.pinService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yoon.capstone2.svc.pinService.repository.PinRepository;

@Service
@RequiredArgsConstructor
public class PinService {

    private final PinRepository pinRepository;

}
