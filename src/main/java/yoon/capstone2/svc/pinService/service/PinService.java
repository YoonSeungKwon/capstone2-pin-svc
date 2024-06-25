package yoon.capstone2.svc.pinService.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import yoon.capstone2.svc.pinService.dto.request.PinRequest;
import yoon.capstone2.svc.pinService.dto.response.MemberResponse;
import yoon.capstone2.svc.pinService.dto.response.PinDetailResponse;
import yoon.capstone2.svc.pinService.dto.response.PinResponse;
import yoon.capstone2.svc.pinService.entity.*;
import yoon.capstone2.svc.pinService.enums.Category;
import yoon.capstone2.svc.pinService.enums.ExceptionCode;
import yoon.capstone2.svc.pinService.enums.Method;
import yoon.capstone2.svc.pinService.exception.MapException;
import yoon.capstone2.svc.pinService.exception.PinException;
import yoon.capstone2.svc.pinService.exception.UnAuthorizedException;
import yoon.capstone2.svc.pinService.exception.UtilException;
import yoon.capstone2.svc.pinService.repository.MapMemberRepository;
import yoon.capstone2.svc.pinService.repository.MapRepository;
import yoon.capstone2.svc.pinService.repository.MemberRepository;
import yoon.capstone2.svc.pinService.repository.PinRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PinService {

    private final MemberRepository memberRepository;

    private final MapRepository mapRepository;

    private final PinRepository pinRepository;

    private final MapMemberRepository mapMemberRepository;

    private final AmazonS3Client amazonS3Client;

    private final String bucket = "pinkok-storage";
    private final String region = "ap-northeast-2";

    //toResponse
    private PinResponse toResponse(Pin pin){
        return new PinResponse(pin.getPinIdx(), pin.getDay(), pin.getPlace(), pin.getHeader(), pin.getTitle(), pin.getMemo(), pin.getCategory().getCategory()
                , pin.getCost(), pin.getLatitude(), pin.getLongitude(), pin.getFile(), String.valueOf(pin.getCreatedAt()));
    }

    //toDetail
    private PinDetailResponse toDetail(Pin pin){

        List<MemberResponse> list = new ArrayList<>();
        for(PinMembers pinMembers: pin.getPinMembers()){
            list.add(toMemberResponse(pinMembers.getMembers()));
        }

        return new PinDetailResponse(pin.getPinIdx(), pin.getDay(), pin.getPlace(), pin.getMembers().getUsername(), pin.getHeader(), pin.getTitle(), pin.getMemo(),
                pin.getCategory().getCategory(), pin.getMethod().getMethod(), pin.getCost(), pin.getCreatedAt(), pin.getUpdatedAt(), pin.getFile(), list);
    }

    private MemberResponse toMemberResponse(Members members){
        return new MemberResponse(members.getMemberIdx(), members.getEmail(), members.getUsername()
                , members.getProfile(), members.getCreatedAt(), members.getUpdatedAt());
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

        if(!mapMemberRepository.existsByMapsAndMembers(currentMap, currentMember))      //핀에 권한이 없음
            throw new PinException(ExceptionCode.UNAUTHORIZED_ACCESS.getMessage(), ExceptionCode.UNAUTHORIZED_ACCESS.getStatus());


        List<Pin> list = currentMap.getPins();
        List<PinResponse> result = new ArrayList<>();

        for(Pin p : list){
            result.add(toResponse(p));
        }

        return result;
    }

    //해당 날짜의 핀 불러오기
    @Transactional(readOnly = true)
    public List<PinResponse> getDatePin(long mapIndex, int day){
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

        if(!mapMemberRepository.existsByMapsAndMembers(currentMap, currentMember))      //핀에 권한이 없음
            throw new PinException(ExceptionCode.UNAUTHORIZED_ACCESS.getMessage(), ExceptionCode.UNAUTHORIZED_ACCESS.getStatus());


        List<Pin> list = pinRepository.findPinsByDay(day);
        List<PinResponse> result = new ArrayList<>();

        for(Pin p:list){
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
    public PinResponse createPin(long mapIdx, MultipartFile file, PinRequest dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
            throw new UnAuthorizedException(ExceptionCode.UNAUTHORIZED_ACCESS.getMessage(), ExceptionCode.UNAUTHORIZED_ACCESS.getStatus()); //로그인 되지 않았거나 만료됨

        Members currentMember = (Members) authentication.getPrincipal();
        Maps currentMap = mapRepository.findMapsByMapIdx(mapIdx);

        if (currentMap == null)
            throw new MapException(ExceptionCode.MAP_NOT_FOUND.getMessage(), ExceptionCode.MAP_NOT_FOUND.getStatus());
        if (!mapMemberRepository.existsByMapsAndMembers(currentMap, currentMember))      //핀에 권한이 없음
            throw new PinException(ExceptionCode.UNAUTHORIZED_ACCESS.getMessage(), ExceptionCode.UNAUTHORIZED_ACCESS.getStatus());

        String url = null;
        if (file!=null && !Objects.requireNonNull(file.getContentType()).startsWith("image"))
            throw new UtilException(ExceptionCode.NOT_IMAGE_FORMAT.getMessage(), ExceptionCode.NOT_IMAGE_FORMAT.getStatus());

        UUID uuid = UUID.randomUUID();
        try {
            if(file!=null) {
                String fileName = uuid + file.getOriginalFilename();
                String fileUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/projects/" + currentMap.getMapIdx() + "/" + fileName;
                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentType(file.getContentType());
                objectMetadata.setContentLength(file.getSize());
                System.out.println(file.getContentType());
                url = fileUrl;
                amazonS3Client.putObject(bucket + "/projects/" + currentMap.getMapIdx(), fileName, file.getInputStream(), objectMetadata);
            }
            Pin pin = Pin.builder()
                    .maps(currentMap)
                    .members(currentMember)
                    .place(dto.getPlace())
                    .header(dto.getHeader())
                    .title(dto.getTitle())
                    .category(Category.fromString(dto.getCategory()))
                    .method(Method.fromString(dto.getMethod()))
                    .memo(dto.getMemo())
                    .cost(dto.getCost())
                    .day(dto.getDay())
                    .lat(dto.getLat())
                    .lon(dto.getLon())
                    .file(url)
                    .build();

            currentMap.getPins().add(pin);

            if(!currentMap.isPrivate()) {
                List<PinMembers> pinMembers = pin.getPinMembers();
                List<Members> memberList = memberRepository.findMembersByMemberIdxIn(dto.getList());


                for (Members m : memberList) {
                    if (m == null) continue;
                    PinMembers tempPinMember = PinMembers.builder().pin(pin).members(m).build();
                    pinMembers.add(tempPinMember);
                }
            }
            mapRepository.save(currentMap);

            return toResponse(pin);

        } catch (Exception e) {
            throw new UtilException(ExceptionCode.INTERNAL_SERVER_ERROR.getMessage(), ExceptionCode.INTERNAL_SERVER_ERROR.getStatus());
        }
    }


    //핀 수정하기
    @Transactional
    public PinResponse updatePin(long mapIdx, long pinIdx, MultipartFile file, PinRequest dto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
            throw new UnAuthorizedException(ExceptionCode.UNAUTHORIZED_ACCESS.getMessage(), ExceptionCode.UNAUTHORIZED_ACCESS.getStatus()); //로그인 되지 않았거나 만료됨

        Members currentMember = (Members) authentication.getPrincipal();
        Maps currentMap = mapRepository.findMapsByMapIdx(mapIdx);
        Pin pin = pinRepository.findPinByPinIdx(pinIdx);

        if(pin == null)
            throw new PinException(ExceptionCode.PIN_NOT_FOUND.getMessage(), ExceptionCode.PIN_NOT_FOUND.getStatus()); // 핀이 존재하지 않음
        if(currentMap == null)
            throw new MapException(ExceptionCode.MAP_NOT_FOUND.getMessage(), ExceptionCode.MAP_NOT_FOUND.getStatus());
        if(!pin.getMaps().equals(currentMap))
            throw new PinException(ExceptionCode.PIN_NOT_VALID.getMessage(), ExceptionCode.PIN_NOT_VALID.getStatus());
        if(!currentMember.equals(pin.getMembers()))       //////// 만약 멤버 모두가 수정 가능하게 하려면 주석처리
            throw new PinException(ExceptionCode.FORBIDDEN_ACCESS.getMessage(), ExceptionCode.FORBIDDEN_ACCESS.getStatus()); //멤버가 일치하지 않음
        if(!mapMemberRepository.existsByMapsAndMembers(currentMap, currentMember))      //핀에 권한이 없음
            throw new PinException(ExceptionCode.UNAUTHORIZED_ACCESS.getMessage(), ExceptionCode.UNAUTHORIZED_ACCESS.getStatus());

        if(dto.getHeader() != null && !dto.getHeader().equals(pin.getHeader()))
            pin.setHeader(dto.getHeader());
        if(dto.getTitle() != null && !dto.getTitle().equals(pin.getTitle()))
            pin.setTitle(dto.getTitle());
        if(dto.getMemo() != null && !dto.getMemo().equals(pin.getMemo()))
            pin.setMemo(dto.getMemo());
        if(dto.getCategory() != null && !dto.getCategory().equals(pin.getCategory().getCategory()))
            pin.setCategory(Category.fromString(dto.getCategory()));
        if(dto.getMethod() != null && !dto.getMethod().equals(pin.getMethod().getMethod()))
            pin.setMethod(Method.fromString(dto.getMethod()));
        if(dto.getCost() != 0 && dto.getCost() != pin.getCost()) {
            pin.setCost(dto.getCost());
        }
        if(dto.getDay() != 0 && dto.getDay() != pin.getDay()) {
            pin.setDay(dto.getDay());
        }

        String url = null;

        if (file!=null && !Objects.requireNonNull(file.getContentType()).startsWith("image"))
            throw new UtilException(ExceptionCode.NOT_IMAGE_FORMAT.getMessage(), ExceptionCode.NOT_IMAGE_FORMAT.getStatus());

        UUID uuid = UUID.randomUUID();

        try {
            if(file!=null) {
                String fileName = uuid + file.getOriginalFilename();
                String fileUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/projects/" + currentMap.getMapIdx() + "/" + fileName;
                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentType(file.getContentType());
                objectMetadata.setContentLength(file.getSize());
                System.out.println(file.getContentType());
                url = fileUrl;
                amazonS3Client.putObject(bucket + "/projects/" + currentMap.getMapIdx(), fileName, file.getInputStream(), objectMetadata);

                String prevUrl = pin.getFile();

                if (prevUrl != null && !prevUrl.isEmpty()) {
                    String existingFileKey = prevUrl.substring(prevUrl.indexOf(".com/") + 5);
                    amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, existingFileKey));
                }
            }

            pin.setFile(url);
            return toResponse(pinRepository.save(pin));

        } catch (Exception e) {
            throw new UtilException(ExceptionCode.INTERNAL_SERVER_ERROR.getMessage(), ExceptionCode.INTERNAL_SERVER_ERROR.getStatus());
        }
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
        if(!currentMember.equals(pin.getMembers()))
            throw new PinException(ExceptionCode.FORBIDDEN_ACCESS.getMessage(), ExceptionCode.FORBIDDEN_ACCESS.getStatus()); //멤버가 일치하지 않음

        try{
            String url = pin.getFile();

            if (url != null && !url.isEmpty()) {
                String existingFileKey = url.substring(url.indexOf(".com/") + 5);
                amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, existingFileKey));
            }
        } catch (Exception e) {
            throw new UtilException(ExceptionCode.INTERNAL_SERVER_ERROR.getMessage(), ExceptionCode.INTERNAL_SERVER_ERROR.getStatus());
        }

        Maps maps = pin.getMaps();

        if(maps!=null){
            List<Comments> list1 = new ArrayList<>(pin.getComments());
            List<PinMembers> list2 = new ArrayList<>(pin.getPinMembers());
            pin.getComments().removeAll(list1);
            pin.getPinMembers().removeAll(list2);
            maps.getPins().remove(pin);
            pin.setMaps(null);
            mapRepository.save(maps);
        }
    }
}
