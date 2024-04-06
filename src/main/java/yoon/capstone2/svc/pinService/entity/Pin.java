package yoon.capstone2.svc.pinService.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pin")
public class Pin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pinIdx;

    private String content;


    @Builder
    public Pin(String content){
        this.content = content;
    }
}
