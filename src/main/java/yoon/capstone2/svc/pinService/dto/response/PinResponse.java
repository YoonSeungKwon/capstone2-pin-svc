package yoon.capstone2.svc.pinService.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PinResponse {

    private long pinIdx;

    private int day;

    private String place;

    private String header;

    private String title;

    private String memo;

    private String category;

    private int cost;

    private double lat;

    private double lon;

    private String file;

    private String createdAt;
}
