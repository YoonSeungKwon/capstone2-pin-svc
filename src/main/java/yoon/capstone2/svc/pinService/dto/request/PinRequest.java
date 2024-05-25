package yoon.capstone2.svc.pinService.dto.request;

import lombok.Getter;

@Getter
public class PinRequest {

    private String header;

    private String title;

    private String method;

    private String category;

    private String memo;

    private int cost;

    private int lat;

    private int lon;

}
