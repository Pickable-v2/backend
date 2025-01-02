package com.example.eatpick.friendship.service;

import org.apache.coyote.BadRequestException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.eatpick.auth.domain.entity.Member;
import com.example.eatpick.auth.repository.MemberRepository;
import com.example.eatpick.auth.service.MemberService;
import com.example.eatpick.common.security.util.SecurityUtil;
import com.example.eatpick.friendship.domain.dto.FriendshipResponse;
import com.example.eatpick.friendship.domain.entity.Friendship;
import com.example.eatpick.friendship.domain.status.FriendshipStatus;
import com.example.eatpick.friendship.repository.FriendshipRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FriendshipService {
    private final MemberService memberService;
    private final FriendshipRepository friendshipRepository;

    public FriendshipResponse createFriendship(String toMemberLoginId) throws BadRequestException {
        Member fromMember = memberService.getLoginMember();
        Member toMember = memberService.findByLoginId(toMemberLoginId)
            .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 회원입니다."));

        Long user1Id = Math.min(fromMember.getId(), toMember.getId());
        Long user2Id = Math.max(fromMember.getId(), toMember.getId());

        if (friendshipRepository.existsByFromMemberIdAndToMemberId(user1Id, user2Id)) {
            throw new BadRequestException("이미 친구로 등록되어 있습니다.");
        }

        Friendship friendship = Friendship.of(fromMember.getId(), toMember.getId(), FriendshipStatus.WAIT);
        friendshipRepository.save(friendship);

        return FriendshipResponse.from(friendship);
    }
}
