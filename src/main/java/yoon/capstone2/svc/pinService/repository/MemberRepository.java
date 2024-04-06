package yoon.capstone2.svc.pinService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yoon.capstone2.svc.pinService.entity.Members;

@Repository
public interface MemberRepository extends JpaRepository<Members, Long> {

    Members findMembersByMemberIdx(long idx);

    Members findMembersByEmail(String email);

    boolean existsMembersByEmail(String email);

    boolean existsMembersByMemberIdx(long idx);

    void deleteByMemberIdx(long idx);

    Members findMembersByRefresh(String token);

}
