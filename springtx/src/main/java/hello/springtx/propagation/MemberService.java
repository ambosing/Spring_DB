package hello.springtx.propagation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final LogRepository logRepository;

    // 1.각각 리포지토리에서 각각 트랜잭션을 사용하는 예제
    // 2.서비스에서만 @Transactional 걸어서 하나의 물리 트랜잭션으로 써보기
    @Transactional
    public void joinV1(String username) {
        Member member = new Member(username);
        Log logMessage = new Log(username);

        log.info("== memberRepository 호출 시작 ==");
        memberRepository.save(member);
        log.info("== memberRepository 호출 종료 ==");
        log.info("== logRepository 호출 시작 ==");
        logRepository.save(logMessage);
        log.info("== logRepository 호출 종료 ==");
    }

    // logRepository.save만 실패할 경우는 Catch에서 처리하고 메시지만 남기고 정상 로직 처리
    @Transactional
    public void joinV2(String username) {
        Member member = new Member(username);
        Log logMessage = new Log(username);

        log.info("== memberRepository 호출 시작 ==");
        memberRepository.save(member);
        log.info("== memberRepository 호출 종료 ==");
        log.info("== logRepository 호출 시작 ==");
        try {
            logRepository.save(logMessage);

        } catch (RuntimeException e) {
            log.info("log 저장에 실패했습니다. logMessage={}", logMessage);
            log.info("정상 흐름 반환");
        }
        log.info("== logRepository 호출 종료 ==");
    }
}
