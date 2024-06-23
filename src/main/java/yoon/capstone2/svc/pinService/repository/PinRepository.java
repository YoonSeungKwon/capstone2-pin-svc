package yoon.capstone2.svc.pinService.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import yoon.capstone2.svc.pinService.entity.Maps;
import yoon.capstone2.svc.pinService.entity.Pin;

import java.util.List;

public interface PinRepository extends JpaRepository<Pin, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Pin findPinByPinIdx(long idx);

    List<Pin> findPinsByMaps(Maps maps);

}
