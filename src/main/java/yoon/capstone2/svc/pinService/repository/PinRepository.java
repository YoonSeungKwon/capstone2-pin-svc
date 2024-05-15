package yoon.capstone2.svc.pinService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yoon.capstone2.svc.pinService.entity.Maps;
import yoon.capstone2.svc.pinService.entity.Pin;

import java.util.List;

public interface PinRepository extends JpaRepository<Pin, Long> {

    Pin findPinByPinIdx(long idx);

    List<Pin> findPinsByMaps(Maps maps);

}
