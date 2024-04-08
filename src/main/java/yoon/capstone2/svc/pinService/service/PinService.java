package yoon.capstone2.svc.pinService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import yoon.capstone2.svc.pinService.dto.request.PinRequest;
import yoon.capstone2.svc.pinService.dto.response.PinDetailResponse;
import yoon.capstone2.svc.pinService.dto.response.PinResponse;
import yoon.capstone2.svc.pinService.entity.Members;
import yoon.capstone2.svc.pinService.entity.Pin;
import yoon.capstone2.svc.pinService.enums.Category;
import yoon.capstone2.svc.pinService.enums.ErrorCode;
import yoon.capstone2.svc.pinService.exception.PinException;
import yoon.capstone2.svc.pinService.exception.UnauthorizedException;
import yoon.capstone2.svc.pinService.repository.PinRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PinService {

    private final PinRepository pinRepository;

    //toResponse
    private PinResponse toResponse(Pin pin){
        return new PinResponse(pin.getPinIdx(), pin.getTitle(), pin.getCategory().getCategory(), pin.getCost(), pin.getLatitude(), pin.getLongitude());
    }

    //toDetail
    private PinDetailResponse toDetail(Pin pin){
        return new PinDetailResponse(pin.getPinIdx(), pin.getMembers().getUsername(), pin.getTitle(), pin.getContent(),
                pin.getCategory().getCategory(), pin.getCost(), pin.getCreatedAt(), pin.getUpdatedAt(), pin.getFile(), pin.getMemo());
    }

    //핀 불러오기
    public PinResponse getPin(long pinIdx){
        //해당 지도의 멤버인지 확인하는 절차 필요

        Pin pin = pinRepository.findPinByPinIdx(pinIdx);
        if(pin == null)
            throw new PinException(ErrorCode.PIN_NOT_FOUND.getMessage(), ErrorCode.PIN_NOT_FOUND.getStatus());

        return toResponse(pin);
    }


    //전체 핀 불러오기
    public List<PinResponse> getPinList(){
        //해당 지도의 멤버인지 확인하는 절차 필요

        List<Pin> list = pinRepository.findAll();
        List<PinResponse> result = new ArrayList<>();

        for(Pin p : list){
            result.add(toResponse(p));
        }

        return result;
    }

    //핀 자세히 보기
    public PinDetailResponse getDetail(long pinIdx){
        //해당 지도의 멤버인지 확인하는 절차 필요

        Pin pin = pinRepository.findPinByPinIdx(pinIdx);

        return toDetail(pin);
    }

    //핀 만들기
    public PinResponse createPin(PinRequest dto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_ACCESS.getMessage(), ErrorCode.UNAUTHORIZED_ACCESS.getStatus()); //로그인 되지 않았거나 만료됨

        Members currentMember = (Members) authentication.getPrincipal();

        Pin pin = Pin.builder()
                .category(Category.valueOf(dto.getCategory()))
                .title(dto.getTitle())
                .content(dto.getContent())
                .cost(dto.getCost())
                .file(dto.getFile())
                .memo(dto.getMemo())
                .lat(dto.getLat())
                .lon(dto.getLon())
                .members(currentMember)
                .build();

        return toResponse(pinRepository.save(pin));
    }

    //핀 수정하기
    public PinResponse updatePin(long pinIdx, PinRequest dto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_ACCESS.getMessage(), ErrorCode.UNAUTHORIZED_ACCESS.getStatus()); //로그인 되지 않았거나 만료됨

        Members currentMember = (Members) authentication.getPrincipal();
        Pin pin = pinRepository.findPinByPinIdx(pinIdx);
        if(pin == null)
            throw new PinException(ErrorCode.PIN_NOT_FOUND.getMessage(), ErrorCode.PIN_NOT_FOUND.getStatus()); // 핀이 존재하지 않음
        if(currentMember != pin.getMembers())
            throw new PinException(ErrorCode.FORBIDDEN_ACCESS.getMessage(), ErrorCode.FORBIDDEN_ACCESS.getStatus()); //멤버가 일치하지 않음


        if(dto.getTitle() != null && !dto.getTitle().equals(pin.getTitle()))
            pin.setTitle(dto.getTitle());
        if(dto.getContent() != null && !dto.getContent().equals(pin.getContent()))
            pin.setContent(dto.getContent());
        if(dto.getCategory() != null && !dto.getCategory().equals(pin.getCategory().getCategory()))
            pin.setCategory(Category.valueOf(dto.getCategory()));
        if(dto.getCost() != 0 && dto.getCost() != pin.getCost())
            pin.setCost(dto.getCost());
        if(dto.getFile() != null && !dto.getFile().equals(pin.getFile()))
            pin.setFile(dto.getFile());
        if(dto.getMemo() != null && !dto.getMemo().equals(pin.getMemo()))
            pin.setMemo(dto.getMemo());

        return toResponse(pinRepository.save(pin));
    }

    //핀 삭제하기
    public void deletePin(long pinIdx){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_ACCESS.getMessage(), ErrorCode.UNAUTHORIZED_ACCESS.getStatus()); //로그인 되지 않았거나 만료됨

        Members currentMember = (Members) authentication.getPrincipal();
        Pin pin = pinRepository.findPinByPinIdx(pinIdx);

        if(pin == null)
            throw new PinException(ErrorCode.PIN_NOT_FOUND.getMessage(), ErrorCode.PIN_NOT_FOUND.getStatus());  // 핀이 존재하지 않음
        if(currentMember != pin.getMembers())
            throw new PinException(ErrorCode.FORBIDDEN_ACCESS.getMessage(), ErrorCode.FORBIDDEN_ACCESS.getStatus()); //멤버가 일치하지 않음

        pinRepository.delete(pin);
    }
}
