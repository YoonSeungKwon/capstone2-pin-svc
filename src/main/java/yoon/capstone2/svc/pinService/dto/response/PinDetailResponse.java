package yoon.capstone2.svc.pinService.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PinDetailResponse {

    private long pinIdx;

    private String writer;

    private String title;

    private String content;

    private String category;

    private int cost;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String file;

}
