package yoon.capstone2.svc.pinService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yoon.capstone2.svc.pinService.dto.request.PinRequest;
import yoon.capstone2.svc.pinService.dto.response.PinDetailResponse;
import yoon.capstone2.svc.pinService.dto.response.PinResponse;
import yoon.capstone2.svc.pinService.entity.MapMembers;
import yoon.capstone2.svc.pinService.entity.Maps;
import yoon.capstone2.svc.pinService.entity.Members;
import yoon.capstone2.svc.pinService.entity.Pin;
import yoon.capstone2.svc.pinService.enums.Category;
import yoon.capstone2.svc.pinService.enums.ExceptionCode;
import yoon.capstone2.svc.pinService.exception.MapException;
import yoon.capstone2.svc.pinService.exception.PinException;
import yoon.capstone2.svc.pinService.exception.UnAuthorizedException;
import yoon.capstone2.svc.pinService.repository.MapMemberRepository;
import yoon.capstone2.svc.pinService.repository.MapRepository;
import yoon.capstone2.svc.pinService.repository.PinRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PinService {

    private final MapRepository mapRepository;

    private final PinRepository pinRepository;

    private final MapMemberRepository mapMemberRepository;

    //toResponse
    private PinResponse toResponse(Pin pin){
        return new PinResponse(pin.getPinIdx(), pin.getTitle(), pin.getCategory().getCategory(), pin.getCost(), pin.getLatitude(), pin.getLongitude());
    }

    //toDetail
    private PinDetailResponse toDetail(Pin pin){
        return new PinDetailResponse(pin.getPinIdx(), pin.getMembers().getUsername(), pin.getTitle(), pin.getContent(),
                pin.getCategory().getCategory(), pin.getCost(), pin.getCreatedAt(), pin.getUpdatedAt(), pin.getFile());
    }

    //핀 불러오기
    @Transactional(readOnly = true)
    public PinResponse getPin(long pinIdx){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            throw new UnAuthorizedException(ExceptionCode.UNAUTHORIZED_ACCESS.getMessage(), ExceptionCode.UNAUTHORIZED_ACCESS.getStatus()); //로그인 되지 않았거나 만료됨
        }
        Members currentMember = (Members) authentication.getPrincipal();

        Pin pin = pinRepository.findPinByPinIdx(pinIdx);

        if(pin == null)         //핀이 존재하지 않음
        {
            throw new PinException(ExceptionCode.PIN_NOT_FOUND.getMessage(), ExceptionCode.PIN_NOT_FOUND.getStatus());
        }

        Maps currentMap = pin.getMaps();

        if(currentMap == null){
            throw new MapException(ExceptionCode.MAP_NOT_FOUND.getMessage(), ExceptionCode.MAP_NOT_FOUND.getStatus());
        }

        if(!mapMemberRepository.existsByMapsAndMembers(currentMap, currentMember))      //핀에 권한이 없음
            throw new PinException(ExceptionCode.UNAUTHORIZED_ACCESS.getMessage(), ExceptionCode.UNAUTHORIZED_ACCESS.getStatus());



        return toResponse(pin);
    }


    //전체 핀 불러오기
    @Transactional(readOnly = true)
    public List<PinResponse> getPinList(long mapIndex){
        //해당 지도의 멤버인지 확인하는 절차 필요
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            throw new UnAuthorizedException(ExceptionCode.UNAUTHORIZED_ACCESS.getMessage(), ExceptionCode.UNAUTHORIZED_ACCESS.getStatus()); //로그인 되지 않았거나 만료됨
        }

        Members currentMember = (Members) authentication.getPrincipal();
        Maps currentMap = mapRepository.findMapsByMapIdx(mapIndex);

        if(currentMap == null){
            throw new MapException(ExceptionCode.MAP_NOT_FOUND.getMessage(), ExceptionCode.MAP_NOT_FOUND.getStatus());
        }

        MapMembers mapMembers = mapMemberRepository.findMapMembersByMapsAndMembers(currentMap, currentMember);

        if(!mapMemberRepository.existsByMapsAndMembers(currentMap, currentMember))      //핀에 권한이 없음
            throw new PinException(ExceptionCode.UNAUTHORIZED_ACCESS.getMessage(), ExceptionCode.UNAUTHORIZED_ACCESS.getStatus());


        List<Pin> list = pinRepository.findPinsByMaps(currentMap);
        List<PinResponse> result = new ArrayList<>();

        for(Pin p : list){
            result.add(toResponse(p));
        }

        return result;
    }

    //핀 자세히 보기
    @Transactional(readOnly = true)
    public PinDetailResponse getDetail(long pinIdx){
        //해당 지도의 멤버인지 확인하는 절차 필요
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            throw new UnAuthorizedException(ExceptionCode.UNAUTHORIZED_ACCESS.getMessage(), ExceptionCode.UNAUTHORIZED_ACCESS.getStatus()); //로그인 되지 않았거나 만료됨
        }

        Members currentMember = (Members) authentication.getPrincipal();

        Pin pin = pinRepository.findPinByPinIdx(pinIdx);
        if(pin == null)     //핀이 존재하지 않음
            throw new PinException(ExceptionCode.PIN_NOT_FOUND.getMessage(), ExceptionCode.PIN_NOT_FOUND.getStatus());

        Maps currentMap = pin.getMaps();
        if(!mapMemberRepository.existsByMapsAndMembers(currentMap, currentMember))      //핀에 권한이 없음
            throw new PinException(ExceptionCode.UNAUTHORIZED_ACCESS.getMessage(), ExceptionCode.UNAUTHORIZED_ACCESS.getStatus());

        return toDetail(pin);
    }

    //핀 만들기
    @Transactional
    public PinResponse createPin(PinRequest dto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
            throw new UnAuthorizedException(ExceptionCode.UNAUTHORIZED_ACCESS.getMessage(), ExceptionCode.UNAUTHORIZED_ACCESS.getStatus()); //로그인 되지 않았거나 만료됨

        Members currentMember = (Members) authentication.getPrincipal();

        Maps currentMap = mapRepository.findMapsByMapIdx(dto.getMapIndex());

        if(currentMap == null)
            throw new MapException(ExceptionCode.MAP_NOT_FOUND.getMessage(), ExceptionCode.MAP_NOT_FOUND.getStatus());

        if(!mapMemberRepository.existsByMapsAndMembers(currentMap, currentMember))      //핀에 권한이 없음
            throw new PinException(ExceptionCode.UNAUTHORIZED_ACCESS.getMessage(), ExceptionCode.UNAUTHORIZED_ACCESS.getStatus());



        Pin pin = Pin.builder()
                .maps(currentMap)
                .category(Category.valueOf(dto.getCategory()))
                .title(dto.getTitle())
                .content(dto.getContent())
                .cost(dto.getCost())
                .file(dto.getFile())
                .lat(dto.getLat())
                .lon(dto.getLon())
                .members(currentMember)
                .build();

        return toResponse(pinRepository.save(pin));
    }

    //핀 수정하기
    @Transactional
    public PinResponse updatePin(long pinIdx, PinRequest dto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
            throw new UnAuthorizedException(ExceptionCode.UNAUTHORIZED_ACCESS.getMessage(), ExceptionCode.UNAUTHORIZED_ACCESS.getStatus()); //로그인 되지 않았거나 만료됨

        Members currentMember = (Members) authentication.getPrincipal();

        Pin pin = pinRepository.findPinByPinIdx(pinIdx);
        if(pin == null)
            throw new PinException(ExceptionCode.PIN_NOT_FOUND.getMessage(), ExceptionCode.PIN_NOT_FOUND.getStatus()); // 핀이 존재하지 않음
        if(currentMember != pin.getMembers())
            throw new PinException(ExceptionCode.FORBIDDEN_ACCESS.getMessage(), ExceptionCode.FORBIDDEN_ACCESS.getStatus()); //멤버가 일치하지 않음

        Maps currentMap = mapRepository.findMapsByMapIdx(dto.getMapIndex());

        if(currentMap == null)
            throw new MapException(ExceptionCode.MAP_NOT_FOUND.getMessage(), ExceptionCode.MAP_NOT_FOUND.getStatus());

        if(!mapMemberRepository.existsByMapsAndMembers(currentMap, currentMember))      //핀에 권한이 없음
            throw new PinException(ExceptionCode.UNAUTHORIZED_ACCESS.getMessage(), ExceptionCode.UNAUTHORIZED_ACCESS.getStatus());


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

        return toResponse(pinRepository.save(pin));
    }

    //핀 삭제하기
    @Transactional
    public void deletePin(long pinIdx){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
            throw new UnAuthorizedException(ExceptionCode.UNAUTHORIZED_ACCESS.getMessage(), ExceptionCode.UNAUTHORIZED_ACCESS.getStatus()); //로그인 되지 않았거나 만료됨

        Members currentMember = (Members) authentication.getPrincipal();
        Pin pin = pinRepository.findPinByPinIdx(pinIdx);

        if(pin == null)
            throw new PinException(ExceptionCode.PIN_NOT_FOUND.getMessage(), ExceptionCode.PIN_NOT_FOUND.getStatus());  // 핀이 존재하지 않음
        if(currentMember != pin.getMembers())
            throw new PinException(ExceptionCode.FORBIDDEN_ACCESS.getMessage(), ExceptionCode.FORBIDDEN_ACCESS.getStatus()); //멤버가 일치하지 않음

        pinRepository.delete(pin);
    }
}
