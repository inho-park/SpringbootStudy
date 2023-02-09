package org.zerock.guestbook.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.repository.GuestbookRepository;

import java.util.function.Function;

@Log4j2
@Service
@RequiredArgsConstructor
public class GuestbookServiceImpl implements GuestbookService{

    private final GuestbookRepository guestbookRepository;


    @Override
    public Long register(GuestbookDTO dto) {
        log.info("Register.............................DTO");
        log.info(dto);
        Guestbook entity = dtoToEntity(dto);
        log.info(entity);
        guestbookRepository.save(entity);
        return entity.getGno();
    }

    // PageRequestDTO 로 PageRequest 를 이용해 Pageable 을 리턴하여
    // repository 의 findAll 로 Page 생성 후
    // Function 객체를 통해 entity 를 DTO 로 교체
    @Override
    public PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO) {
        // 최신 순으로 보기 위한 descending 반영
        Pageable pageable = requestDTO.getPageable(Sort.by("gno").descending());
        // Pageable 을 이용하여 repository 의 JpaRepository 의 findAll 사용
        Page<Guestbook> result = guestbookRepository.findAll(pageable);
        // 람다식을 통해 Guestbook 의 type 을 이용해 GuestbookDTO 로 바꾸는 기능 추가 ( map() 에 사용 )
        Function<Guestbook, GuestbookDTO> fn = (entity -> entityToDTO(entity));

        return new PageResultDTO<>(result, fn);
    }


}
