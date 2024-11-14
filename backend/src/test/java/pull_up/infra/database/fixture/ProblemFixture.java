package pull_up.infra.database.fixture;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import pull_up.infra.database.entity.Problem;

@RequiredArgsConstructor
public enum ProblemFixture {
    ONE(1L,"수리", "골고루", "속력", "철수가 출발 지점에서 10km 떨어진 지점에서부터 시계 방향으로 원형 트랙을 자전거로 돌기 시작했다. 이 원형 트랙의 총 길이는 8km이다. 철수는 처음 2시간 동안 5바퀴를 돌았고, 다음 1시간 동안 3바퀴를 돌았다면, 철수의 3시간 동안의 자전거 평균 속력은 몇 km/h인가? (소수점 둘째자리에서 반올림하세요.)", "", "18.2 km/h", "20.5 km/h", "21.3 km/h", "28.7 km/h", "30.1 km/h", "3", "자전거로 총 달린 거리를 구합니다.\n" +
            " 첫 2시간 동안: 8km X 5바퀴 = 40km\n" +
            " 다음 1시간 동안: 8km X 3바퀴 = 24km\n" +
            " 따라서, 총 3시간 동안 달린 거리는 40 + 24 = 64km입니다.\n" +
            " 평균 속력은 총 이동 거리(64km)를 총 시간(3시간)으로 나눈 값입니다.\n" +
            " 즉, 64 / 3 = 약 21.33 km/h입니다.\n" +
            " 하지만 이 값은 소수점 첫째 자리를 올림하여 21.3km/h가 됩니다.", 70D),
    TWO(2L, "수리", "골고루", "속력", "민수는 원형 트랙을 자전거로 시계 방향으로 돌면서 첫 30분 동안 4바퀴, 그 다음 30분 동안 5바퀴, 마지막 1시간 동안 10바퀴를 돌았다. 원형 트랙의 총 길이가 6km라면, 민수가 2시간 동안 자전거를 탄 평균 속력은 몇 km/h인가?", "", "48 km/h", "57 km/h", "60 km/h", "66 km/h", "78 km/h", "2", "민수가 자전거로 총 달린 거리를 구합니다.\n" +
            " 첫 30분 동안: 6km X 4바퀴 = 24km\n" +
            " 다음 30분 동안: 6km X 5바퀴 = 30km\n" +
            " 마지막 1시간 동안: 6km X 10바퀴 = 60km\n" +
            " 따라서 총 이동 거리는 24 + 30 + 60 = 114km입니다.\n" +
            " 평균 속력은 총 이동 거리(114km)를 총 시간(2시간)으로 나눈 값입니다.\n" +
            " 즉, 114 / 2 = 57 km/h 입니다.", 72D),
    THREE(3L,"수리", "골고루", "용액의 농도", "어떤 소금물 500g에 100g 물을 증발시켰더니 15%의 소금물이 되었다. 처음 소금물의 농도는 몇 %인가?", "", "10%", "12%", "13%", "14%", "16%", "2", "초기 소금물의 농도는 미지수 x로 두고, 최종 농도는 15%이며, 초기 무게는 500g이고 최종 무게는 400g입니다. 이를 대입하여 초기 농도를 구해봅시다.\n\n" +
            " 초기 농도 x 초기 무게 = 최종 농도 x 최종 무게\n" +
            " x * 500g = 15% * 400g\n" +
            " 500x = 0.15 * 400\n" +
            " 500x = 60\n" +
            " x = 60 / 500\n" +
            " x = 0.12 또는 12%\n\n" +
            " 따라서 처음 소금물의 농도는 12%입니다.", 68D),
    FOUR(4L,"수리", "골고루", "용액의 농도", "어떤 소금물 600g에 300g 물을 증발시켰더니 20%의 소금물이 나왔다. 처음 소금물의 농도는 몇 %인가?", "", "10%", "16%", "17%", "18%", "20%", "1", "초기 소금물의 농도는 미지수 x로 두고, 최종 농도는 20%이며, 초기 무게는 600g이고 최종 무게는 300g입니다. 이를 대입하여 초기 농도를 구해봅시다.\n\n" +
            " 초기 농도 x 초기 무게 = 최종 농도 x 최종 무게\n" +
            " x * 600g = 20% * 300g\n" +
            " 600x = 0.20 * 300\n" +
            " 600x = 60\n" +
            " x = 60 / 600\n" +
            " x = 0.10 또는 10%\n\n" +
            " 따라서 처음 소금물의 농도는 10%입니다.", 83D),
    FIVE(5L,"수리", "골고루", "원가/정가", "어떤 물건을 정가에서 20% 할인하여 팔아도 원가에 대해서는 10%의 이익을 얻고자 한다. 처음 원가에 몇 %의 이익을 붙여서 정가를 매겨야 하는가?", "", "20.4%", "25%", "37.5%", "39%", "41.5%", "3", "원가를 C, 정가를 P라 할 때,\n" +
            "정가에서 20% 할인된 가격은 0.8 * P이며, 이 가격이 원가의 10% 이익인 1.1 * C가 되어야 합니다.\n" +
            "0.8 * P = 1.1 * C\n" +
            "P = (1.1 * C) / 0.8 = 1.375 * C\n" +
            "원가에 37.5%의 이익을 붙여 정가를 매겨야 합니다.", 88D);

    private final Long id;
    private final String entry;
    private final String category;
    private final String type;
    private final String question;
    private final String explanation;
    private final String choice1;
    private final String choice2;
    private final String choice3;
    private final String choice4;
    private final String choice5;
    private final String answer;
    private final String answerExplain;
    private final Double incorrectRate;

    public Problem get() {
        Problem problem = Problem.of(entry,
                category,
                type,
                question,
                explanation,
                choice1,
                choice2,
                choice3,
                choice4,
                choice5,
                answer,
                answerExplain,
                0,
                0,
                incorrectRate);
//        problem.setId(id);
        return problem;
    }
}
