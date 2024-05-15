package com.ssd.sthub.dto.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MessageListDTO {
    private List<MessageDTO.Response> result;
    private Long totalPage;
}
