package com.ssd.sthub.dto.complaint;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ComplaintRepoDTO {

    private Long memberId;
    private Set<Integer> trueIndexes;

    public ComplaintRepoDTO(Long memberId) {
        this.memberId = memberId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Set<Integer> getTrueIndexes() {
        return trueIndexes;
    }

    public void setTrueIndexes(Set<Integer> trueIndexes) {
        this.trueIndexes = trueIndexes;
    }
}
