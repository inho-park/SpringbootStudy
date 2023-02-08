package org.zerock.guestbook.dto;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
public class PageResultDTO<DTO, Entity> {

    private List<DTO> dtoList;

    public PageResultDTO(Page<Entity> result, Function<Entity, DTO> fn) {
        // Function<T,R> 을 통해 map 안에서 T 를 R 로 변환 ( 반복문이 실행 중이지만 코드에 드러나지 않음 )
        dtoList = result.stream().map(fn).collect(Collectors.toList());
    }
}
