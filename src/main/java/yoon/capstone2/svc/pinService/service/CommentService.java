package yoon.capstone2.svc.pinService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yoon.capstone2.svc.pinService.dto.request.CommentRequest;
import yoon.capstone2.svc.pinService.dto.response.CommentResponse;
import yoon.capstone2.svc.pinService.entity.Comments;
import yoon.capstone2.svc.pinService.entity.Members;
import yoon.capstone2.svc.pinService.entity.Pin;
import yoon.capstone2.svc.pinService.enums.ExceptionCode;
import yoon.capstone2.svc.pinService.exception.PinException;
import yoon.capstone2.svc.pinService.exception.UnAuthorizedException;
import yoon.capstone2.svc.pinService.repository.CommentRepository;
import yoon.capstone2.svc.pinService.repository.PinRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PinRepository pinRepository;

    private CommentResponse toResponse(Comments comments){
        return new CommentResponse(comments.getMembers().getUsername(), comments.getMembers().getProfile(), comments.getContent(),
                String.valueOf(comments.getCreatedAt()));
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getList(long pinIdx){
        Pin pin = pinRepository.findPinByPinIdx(pinIdx);

        if(pin == null)
            throw new PinException(ExceptionCode.PIN_NOT_FOUND.getMessage(), ExceptionCode.PIN_NOT_FOUND.getStatus());

        List<Comments> list = commentRepository.findAllByPin(pin);
        List<CommentResponse> result = new ArrayList<>();

        for(Comments c: list){
            result.add(toResponse(c));
        }

        return result;
    }


    @Transactional
    public CommentResponse postComment(long pinIdx, CommentRequest dto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            throw new UnAuthorizedException(ExceptionCode.UNAUTHORIZED_ACCESS.getMessage(), ExceptionCode.UNAUTHORIZED_ACCESS.getStatus()); //로그인 되지 않았거나 만료됨
        }

        Members currentMember = (Members) authentication.getPrincipal();
        Pin pin = pinRepository.findPinByPinIdx(pinIdx);

        if(pin == null)
            throw new PinException(ExceptionCode.PIN_NOT_FOUND.getMessage(), ExceptionCode.PIN_NOT_FOUND.getStatus());

        Comments comments = Comments.builder()
                .members(currentMember)
                .content(dto.getContent())
                .pin(pin)
                .build();

        return toResponse(commentRepository.save(comments));
    }

}
