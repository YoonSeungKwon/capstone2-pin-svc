package yoon.capstone2.svc.pinService.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import yoon.capstone2.svc.pinService.enums.Category;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pin")
public class Pin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pinIdx;

    private long mapIdx;

    @ManyToOne
    @JoinColumn(name = "pin_member")
    private Members members;

    @Column(nullable = false, length = 30)
    private String title;

    @Column(nullable = false, length = 20)
    private String content;

    @Enumerated(EnumType.STRING)
    private Category category;

    private int cost;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private int latitude;

    private int longitude;

    private String file;

    private String memo;


    @Builder
    public Pin(Members members, String title, String content, Category category, int cost, int lat, int lon, String file, String memo){
        this.members = members;
        this.title = title;
        this.content = content;
        this.category = category;
        this.cost = cost;
        this.latitude = lat;
        this.longitude = lon;
        this.file = file;
        this.memo = memo;
    }

}
