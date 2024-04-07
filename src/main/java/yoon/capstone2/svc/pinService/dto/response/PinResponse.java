package yoon.capstone2.svc.pinService.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PinResponse {

    private long pinIdx;

    private String title;

    private String category;

    private int cost;

    private int lat;

    private int lon;

}
