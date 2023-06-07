package nextstep.courses.domain;

import nextstep.users.domain.NsUserTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.LocalDateTime;

import static nextstep.fixtures.SessionFixtures.*;
import static org.assertj.core.api.Assertions.*;

public class SessionTest {

    @DisplayName("강의 기간")
    @Test
    void period() {
        LocalDateTime startedAt = LocalDateTime.now();
        LocalDateTime endedAt = startedAt.plusDays(7);

        Session session = testSession1();

        assertThat(session.getSessionPeriod()).isEqualTo(new SessionPeriod(startedAt, endedAt));
    }

    @Test
    @DisplayName("강의 커버 이미지 정보")
    void coverImage() {
        String coverImageUrl = "https://nextstep.tdd";
        Session session = testSession1();

        assertThat(session.getSessionCoverImage()).isEqualTo(coverImageUrl);
    }

    @ParameterizedTest(name = "강의결제 유형 {0}")
    @EnumSource(value = SessionBilling.class ,names = {"FREE"})
    void sessionBillingType(SessionBilling sessionBillType) {
        Session session = testSession1();

        assertThat(session.getSessionBilling()).isEqualTo(sessionBillType);
    }

    @ParameterizedTest(name = "강의결제 유형 {0}")
    @EnumSource(value = SessionBilling.class ,names = {"PAID"})
    void sessionBillingType2(SessionBilling sessionBillType) {
        Session session = testSession2();

        assertThat(session.getSessionBilling()).isEqualTo(sessionBillType);
    }

    @ParameterizedTest(name = "강의 상태 {0}")
    @EnumSource(value = SessionStatus.class ,names = {"READY"})
    void sessionStatusType(SessionStatus sessionStatusType) {
        Session session = testSession2();

        assertThat(session.getSessionStatus()).isEqualTo(sessionStatusType);
    }

    @ParameterizedTest(name = "강의 상태 {0}")
    @EnumSource(value = SessionStatus.class ,names = {"OPEN"})
    void sessionStatusType2(SessionStatus sessionStatusType) {
        Session session = testSession1();

        assertThat(session.getSessionStatus()).isEqualTo(sessionStatusType);
    }

    @ParameterizedTest(name = "강의 상태 {0}")
    @EnumSource(value = SessionStatus.class ,names = {"CLOSED"})
    void sessionStatusType3(SessionStatus sessionStatusType) {
        Session session = testSession3();

        assertThat(session.getSessionStatus()).isEqualTo(sessionStatusType);
    }

    @DisplayName("모집중일시 수강신청 테스트")
    @Test
    void openStatus() {
        Session session = testSession1();

        session.register(NsUserTest.JAVAJIGI);

        assertThat(session.getSessionJoins()).hasSize(1).extracting("session.id", "nsUser.id")
                .containsExactly(tuple(1L, 1L));
    }

    @DisplayName("준비중일 때 수강신청 시 오류")
    @Test
    void readyStatus() {
        Session session = testSession2();

        assertThatThrownBy(() -> session.register(NsUserTest.JAVAJIGI)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("종료일 때 수강신청 시 오류")
    @Test
    void closedStatus() {
        Session session = testSession3();

        assertThatThrownBy(() -> session.register(NsUserTest.JAVAJIGI)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("강의 최대 수강인원 달성 후 신청 시 오류")
    @Test
    void maximumUser() {
        Session session = testSession5();
        assertThatThrownBy(() -> session.register(NsUserTest.JAVAJIGI)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("중복ID 신청 시 오류")
    @Test
    void duplicatedUser() {
        Session session = testSession1();
        session.register(NsUserTest.JAVAJIGI);
        assertThatThrownBy(() -> session.register(NsUserTest.JAVAJIGI)).isInstanceOf(IllegalArgumentException.class);
    }
}
