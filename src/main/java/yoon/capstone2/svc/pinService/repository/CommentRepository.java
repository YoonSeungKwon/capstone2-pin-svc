package yoon.capstone2.svc.pinService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yoon.capstone2.svc.pinService.entity.Comments;
import yoon.capstone2.svc.pinService.entity.Pin;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comments, Long> {

    List<Comments> findAllByPin(Pin pin);

}
