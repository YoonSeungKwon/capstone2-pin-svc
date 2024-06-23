package yoon.capstone2.svc.pinService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yoon.capstone2.svc.pinService.entity.Maps;

@Repository
public interface MapRepository extends JpaRepository<Maps, Long> {

    Maps findMapsByMapIdx(long idx);

}
