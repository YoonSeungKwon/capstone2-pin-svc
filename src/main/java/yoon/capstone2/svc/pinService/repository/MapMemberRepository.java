package yoon.capstone2.svc.pinService.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import yoon.capstone2.svc.pinService.entity.MapMembers;
import yoon.capstone2.svc.pinService.entity.Maps;
import yoon.capstone2.svc.pinService.entity.Members;

import java.util.List;

@Repository
public interface MapMemberRepository extends JpaRepository<MapMembers, Long> {

    List<MapMembers> findAllByMaps(Maps maps);
    MapMembers findMapMembersByMapsAndMembers(Maps maps, Members members);

    boolean existsByMaps(Maps maps);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    boolean existsByMapsAndMembers(Maps maps, Members members);

}
