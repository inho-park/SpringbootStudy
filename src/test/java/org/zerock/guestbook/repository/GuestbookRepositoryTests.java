package org.zerock.guestbook.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.entity.QGuestbook;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class GuestbookRepositoryTests {

    @Autowired
    private GuestbookRepository guestbookRepository;

    @Test
    public void insertDummies() {
        IntStream.rangeClosed(1,300).forEach( i -> {
            Guestbook guestbook = Guestbook.builder()
                    .title("Title......" + i)
                    .content("Content......" + i)
                    .writer("user" + (i%10))
                    .build();
            System.out.println(guestbookRepository.save(guestbook));
        });
    }

    @Test
    public void updateTest() {
        Optional<Guestbook> result = guestbookRepository.findById(1501l);
        if (result.isPresent()) {
            Guestbook guestbook = result.get();
            guestbook.changeTitle("Changed Title.....");
            guestbook.changeContent("Changed Content......");
            guestbookRepository.save(guestbook);
        }
    }

    @Test
    public void testQuery1() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno"));
        // 1. Q도메인 클래스 생성 => 엔티티 클래스에 선언된 title, content 같은 필드를 변수로 활용
        QGuestbook qGuestbook = QGuestbook.guestbook;
        String keyword = "15";

        // 2. BooleanBuilder 는 where문에 들어가는 조건들을 넣어주는 컨테이너
        BooleanBuilder builder = new BooleanBuilder();

        // 3. 원하는 조건은 필드 값과 같이 결합해서 생성
        // (BooleanBuilder 에 들어가는 값은 com.querydsl.core.types.predicate )
        BooleanExpression expression = qGuestbook.title.contains(keyword);

        // 4. 만들어진 조건을 where문(BooleanBuilder) 에 and 나 or 로 키워드 결합
        builder.and(expression);

        // 5. BooleanBuilder 는 GuestbookRepository 에 추가된
        // QuerydslPredicateExcutor 인터페이스의 findAll() 사용 가능
        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable); // 5

        result.forEach(guestbook -> {
            System.out.println(guestbook);
        });
    }

    @Test
    public void testQuery2() {
        Pageable pageable = PageRequest.of(2, 5, Sort.by("gno"));
        QGuestbook qGuestbook = QGuestbook.guestbook;
        String keyword = "1";
        BooleanBuilder builder = new BooleanBuilder();
        BooleanExpression exTitle = qGuestbook.title.contains(keyword);
        BooleanExpression exContent = qGuestbook.content.contains(keyword);
        BooleanExpression exWriter = qGuestbook.writer.contains(keyword);
        BooleanExpression exAll = exTitle.or(exContent).or(exWriter);
        builder.and(exAll);
        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable);
        result.forEach(guestbook -> {
            System.out.println(guestbook);
        });
    }
}
