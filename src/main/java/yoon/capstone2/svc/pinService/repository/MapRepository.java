package yoon.capstone2.svc.pinService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yoon.capstone2.svc.pinService.entity.Maps;
import yoon.capstone2.svc.pinService.entity.Members;

import java.util.List;

@Repository
public interface MapRepository extends JpaRepository<Maps, Long> {

    List<Maps> findAllByMapMembers_Members(Members members);

    Maps findMapsByMapIdx(long idx);

}
