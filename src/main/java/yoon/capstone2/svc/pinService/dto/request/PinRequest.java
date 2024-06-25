package yoon.capstone2.svc.pinService.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class PinRequest {

    private String place;

    private String header;

    private String title;

    private String method;

    private String category;

    private String memo;

    private int cost;

    private int day;

    private double lat;

    private double lon;

    private List<Integer> list;

}
