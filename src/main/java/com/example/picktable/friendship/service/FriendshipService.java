package com.example.picktable.friendship.service;

import com.example.picktable.friendship.domain.dto.FriendListDTO;
import com.example.picktable.friendship.domain.entity.Friendship;
import com.example.picktable.member.domain.entity.Member;
import com.example.picktable.notice.domain.entity.Notice;
import com.example.picktable.friendship.domain.type.FriendshipStatus;
import com.example.picktable.notice.domain.type.NoticeType;
import com.example.picktable.friendship.repository.FriendshipRepository;
import com.example.picktable.member.repository.MemberRepository;
import com.example.picktable.notice.repository.NoticeRepository;
import com.example.picktable.member.security.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FriendshipService {

    private final MemberRepository memberRepository;
    private final FriendshipRepository friendshipRepository;
    private final NoticeRepository noticeRepository;

    public void createFriendship(String toLoginId) throws BadRequestException {
        String fromLoginId = String.valueOf(SecurityUtil.getLoginId());
        if (fromLoginId == null) {
            throw new BadRequestException("에러 발생");
        }

        Member fromMember = memberRepository.findByLoginId(fromLoginId).orElseThrow(() -> new BadRequestException("회원 조회 실패"));
        Member toMember = memberRepository.findByLoginId(toLoginId).orElseThrow(() -> new BadRequestException("회원 조회 실패"));

        Friendship friendshipFrom = Friendship.builder()
                .member(fromMember)
                .memberLoginId(fromLoginId)
                .friendLoginId(toLoginId)
                .status(FriendshipStatus.WAITING)
                .isFrom(true)
                .build();

        Friendship friendshipTo = Friendship.builder()
                .member(toMember)
                .memberLoginId(toLoginId)
                .friendLoginId(fromLoginId)
                .status(FriendshipStatus.WAITING)
                .isFrom(false)
                .build();

        fromMember.getFriendshipList().add(friendshipTo);
        toMember.getFriendshipList().add(friendshipFrom);

        friendshipRepository.save(friendshipTo);
        friendshipRepository.save(friendshipFrom);

        friendshipTo.setCounterpartId(friendshipFrom.getId());
        friendshipFrom.setCounterpartId(friendshipTo.getId());

        String content = fromMember.getNickname() + "님이 친구추가 요청을 보냈습니다.";
        Notice notice = new Notice(toMember, content, NoticeType.FRIEND_INVITE);
        noticeRepository.save(notice);
    }

    public List<FriendListDTO> getFriendList() throws BadRequestException {
        Member user = memberRepository.findByLoginId(SecurityUtil.getLoginId()).orElseThrow(() -> new BadRequestException("회원 조회 실패"));
        List<Friendship> friendshipList = user.getFriendshipList();
        List<FriendListDTO> result = new ArrayList<>();

        for (Friendship request : friendshipList) {
            if(request.getStatus() == FriendshipStatus.ACCEPT) {
                Member friend = memberRepository.findByLoginId(request.getFriendLoginId()).orElseThrow(() -> new BadRequestException("회원 조회 실패"));
                FriendListDTO friendList = FriendListDTO.builder()
                        .friendshipId(request.getId())
                        .friendLoginId(friend.getLoginId())
                        .friendNickname(friend.getNickname())
                        .status(request.getStatus())
                        .build();
                result.add(friendList);
            }
        }
        return result;
    }

    public List<FriendListDTO> getWaitingFriendList() throws Exception {
        Member user = memberRepository.findByLoginId(SecurityUtil.getLoginId()).orElseThrow(() -> new BadRequestException("회원 조회 실패"));
        List<Friendship> friendshipList = user.getFriendshipList();
        List<FriendListDTO> result = new ArrayList<>();

        for (Friendship request : friendshipList) {
            if(!request.isFrom() && (request.getStatus() == FriendshipStatus.WAITING)) {
                Member friend = memberRepository.findByLoginId(request.getFriendLoginId()).orElseThrow(() -> new BadRequestException("회원 조회 실패"));
                FriendListDTO waitingFriendList = FriendListDTO.builder()
                        .friendshipId(request.getId())
                        .friendLoginId(friend.getLoginId())
                        .friendNickname(friend.getNickname())
                        .status(request.getStatus())
                        .build();
                result.add(waitingFriendList);
            }
        }
        return result;
    }

    public void acceptFriendRequest(Long friendshipId) throws Exception {
        Friendship friendship = friendshipRepository.findById(friendshipId).orElseThrow(() -> new BadRequestException("친구 요청 조회 실패"));
        Friendship counterFriendship = friendshipRepository.findById(friendship.getCounterpartId()).orElseThrow(() -> new BadRequestException("친구 요청 조회 실패"));

        friendship.acceptFriendshipRequest();
        counterFriendship.acceptFriendshipRequest();
    }

    public void cancelFriendRequest(Long friendshipId) {
        friendshipRepository.deleteById(friendshipId);
    }
}
