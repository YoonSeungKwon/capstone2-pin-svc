package yoon.capstone2.svc.pinService.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class PinDetailResponse {

    private long pinIdx;

    private int day;

    private String place;

    private String writer;

    private String header;

    private String title;

    private String memo;

    private String category;

    private String method;

    private int cost;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String file;

    private List<?> list;

}
