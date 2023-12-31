package com.example.mentoringproject.mentoring.mentoring.model;

import com.example.mentoringproject.mentoring.mentoring.entity.Mentoring;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Data
@Builder
public class MentoringList {
    private long mentoringId;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private int numberOfPeople;
    private int amount;
    private String category;
    private Long userId;
    private String name;
    private String uploadUrl;
    private Double grade;
    public static Page<MentoringList> from(Page<Mentoring> page) {
        List<MentoringList> mentoringLists = page.getContent().stream()
                .map(mentoring -> MentoringList.builder()
                        .mentoringId(mentoring.getId())
                        .title(mentoring.getTitle())
                        .startDate(mentoring.getStartDate())
                        .endDate(mentoring.getEndDate())
                        .amount(mentoring.getAmount())
                        .category(mentoring.getCategory())
                        .userId(mentoring.getUser().getId())
                        .name(mentoring.getUser().getName())
                        .uploadUrl(mentoring.getUploadUrl())
                        .numberOfPeople(mentoring.getNumberOfPeople())
                        .grade(mentoring.getUser().getRating())
                        .build()

                ).collect(Collectors.toList());

        return new PageImpl<>(mentoringLists, page.getPageable(), page.getTotalElements());
    }
}
