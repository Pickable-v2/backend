package com.example.picktable.member.domain.dto.update;

import java.util.Optional;

public record MemberUpdateRequestDTO(Optional<String> nickname, Optional<String> gender,
                                    Optional<Integer> age) {
}
