package com.gg.mafia.domain.achievement.application;


import static org.mockito.BDDMockito.given;

import com.gg.mafia.domain.achievement.dao.AchievementDao;
import com.gg.mafia.domain.achievement.dao.AchievementStepDao;
import com.gg.mafia.domain.achievement.dao.UserAchievementDao;
import com.gg.mafia.domain.achievement.domain.Achievement;
import com.gg.mafia.domain.achievement.domain.AchievementEnum;
import com.gg.mafia.domain.achievement.domain.AchievementStep;
import com.gg.mafia.domain.achievement.domain.UserAchievement;
import com.gg.mafia.domain.achievement.dto.AchievementDto;
import com.gg.mafia.domain.member.dao.UserDao;
import com.gg.mafia.domain.member.domain.User;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AchievementServiceTest {
    @InjectMocks
    AchievementService service;
    @Mock
    AchievementStepDao achievementStepDao;
    @Mock
    AchievementDao achievementDao;
    @Mock
    UserAchievementDao userAchievementDao;
    @Mock
    UserDao userDao;
    User user;
    AchievementDto dto;

    @BeforeEach
    public void setUp() {
        user = User.builder().email("TEST@naver.com").password("123").build();
    }

    @Test
    @DisplayName("회원의 업적 단계를 조회한다.")
    public void getStepsByUser_id_Test() {
        AchievementStep expect = AchievementStep.builder().user(user).build();

        given(achievementStepDao.findByUser_id(user.getId())).willReturn(Optional.of(expect));
        AchievementStep result = service.getStepsByUser_id(user.getId());

        Assertions.assertThat(expect).isEqualTo(result);
    }

    @Test
    @DisplayName("회원의 보유 업적을 조회한다.")
    public void getOwnAchievementByUser_id_Test() {
        List<UserAchievement> expect = createAchievement(
                new AchievementEnum[]{AchievementEnum.궁예, AchievementEnum.순경, AchievementEnum.멍때리기}
        ).stream().map(e -> UserAchievement.relate(user, e)).toList();

        given(userAchievementDao.findByUser_id(user.getId())).willReturn(expect);

        List<AchievementEnum> result = service.getOwnAchievementByUser_id(user.getId());
        expect.stream().forEach(
                e -> Assertions.assertThat(result.contains(e.getAchievement().getAchievementName())).isTrue());
    }

    @Test
    @DisplayName("회원의 단계 별 미보유 업적을 조회한다.")
    public void getAchievementToComplete_Test() {
        List<AchievementEnum> ownAchievement = createAchievement(
                new AchievementEnum[]{AchievementEnum.궁예, AchievementEnum.순경, AchievementEnum.멍때리기}
        ).stream().map(e -> e.getAchievementName()).toList();

        AchievementStep steps = AchievementStep.builder().user(user).build();

        List<AchievementEnum> expect = Arrays.asList(AchievementEnum.직무유기, AchievementEnum.KILLER);
        List<AchievementEnum> result = service.getAchievementToComplete(ownAchievement, steps);
        expect.stream().forEach(e -> Assertions.assertThat(result.contains(e)).isTrue());
    }

    @Test
    @DisplayName("완료한 업적을 조회한다.")
    public void getCompleteAchievement_Test() {
        List<AchievementEnum> completeTo = createAchievement(
                new AchievementEnum[]{AchievementEnum.궁예, AchievementEnum.순경, AchievementEnum.멍때리기}
        ).stream().map(e -> e.getAchievementName()).toList();
        List<AchievementEnum> result = service.getCompleteAchievement(completeTo);
        result.stream().forEach(e -> Assertions.assertThat(completeTo.contains(e)).isTrue());
    }


    @Test
    @DisplayName("AchievementDto를 생성한다.")
    public void getAchievementDto_Test() {
        AchievementStep expect1 = AchievementStep.builder().user(user).build();

        List<UserAchievement> expect2 = createAchievement(
                new AchievementEnum[]{AchievementEnum.궁예, AchievementEnum.순경, AchievementEnum.멍때리기}
        ).stream().map(e -> UserAchievement.relate(user, e)).toList();
        List<AchievementEnum> expect3 = AchievementEnum.getAchievementToComplete(
                expect2.stream().map(e -> e.getAchievement().getAchievementName()).toList(), expect1);
        List<AchievementEnum> expect4 = Arrays.asList(AchievementEnum.직무유기, AchievementEnum.KILLER);

        given(achievementStepDao.findByUser_id(user.getId())).willReturn(Optional.of(expect1));
        given(userAchievementDao.findByUser_id(user.getId())).willReturn(expect2);

        dto = service.getAchievementDto(user.getId());
        Assertions.assertThat(expect1).isEqualTo(dto.getSteps());
        expect2.stream().forEach(e -> Assertions.assertThat(
                dto.getUserAchievementList().contains(e.getAchievement().getAchievementName())).isTrue());
        expect3.stream().forEach(e -> Assertions.assertThat(
                dto.getAchievemenToComplete().contains(e)).isTrue());
        expect4.stream().forEach(e -> Assertions.assertThat(
                dto.getCompletedAchievement().contains(e)).isTrue());
//        Assertions.assertThat(dto.getSteps()).isNotEqualTo(expect1);
    }

    @Test
    @DisplayName("업데이트 된 업적 단계를 조회한다.")
    public void updateSteps_Test() {
        getAchievementDto_Test();
        service.updateSteps(dto);
        Assertions.assertThat(dto.getSteps().getCommonAchieveStep()).isEqualTo(2);
        Assertions.assertThat(dto.getSteps().getMafiaAchieveStep()).isEqualTo(2);
        Assertions.assertThat(dto.getSteps().getCitizenAchieveStep()).isEqualTo(2);
        Assertions.assertThat(dto.getSteps().getPoliceAchieveStep()).isEqualTo(2);
        Assertions.assertThat(dto.getSteps().getDoctorAchieveStep()).isEqualTo(2);
    }

    public List<Achievement> createAchievement(AchievementEnum[] data) {
        AchievementEnum[] arr = data;
        List<Achievement> result = new ArrayList<>();
        Arrays.stream(arr).forEach(e -> {
            result.add(Achievement.builder().achievementName(e).jobName(e.getJobEnum()).build());

        });
        return result;
    }


}
