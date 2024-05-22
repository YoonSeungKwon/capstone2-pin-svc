package yoon.capstone2.svc.pinService.dto.request;

import lombok.Getter;

@Getter
public class PinRequest {

    private long mapIndex;

    private String title;

    private String content;

    private String category;

    private int cost;

    private int lat;

    private int lon;

    private String file;

}
