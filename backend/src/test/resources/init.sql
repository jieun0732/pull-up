-- drop all entity
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE member;
TRUNCATE TABLE problem;
TRUNCATE TABLE exam;
TRUNCATE TABLE answer;
SET FOREIGN_KEY_CHECKS = 1;

--  insert member fixture
INSERT INTO member(id, tutorial_finished, name, email, sns_id, sns_provider, role) VALUES
('1', false, 'apple test user', 'test@apple.com', 'test1234', 'apple', 'user'),
('2', false, 'apple test user', 'test@kakao.com', 'test1234', 'apple', 'user'),
('3', true, 'tutorial finished user', 'test@apple.com', 'test1234', 'apple', 'user'),
('4', false, 'conceal email user', 'test@privaterelay.appleid.com', 'test1234', 'apple', 'user');

-- insert problem fixture
INSERT INTO problem (id, total_attempts, incorrect_attempts, incorrect_rate, entry, problem_type, question, example, choice1, choice2, choice3, choice4, choice5, correct_answer, explanation) VALUES
(1, 0, 0, 0.0, 'MATH', '속력', CAST('철수가 출발 지점에서 10km 떨어진 지점에서부터 시계 방향으로 원형 트랙을 자전거로 돌기 시작했다. 이 원형 트랙의 총 길이는 8km이다. 철수는 처음 2시간 동안 5바퀴를 돌았고, 다음 1시간 동안 3바퀴를 돌았다면, 철수의 3시간 동안의 자전거 평균 속력은 몇 km/h인가? (소수점 둘째자리에서 반올림하세요.)' AS BINARY),CAST('' AS BINARY),'18.2 km/h', '20.5 km/h', '21.3 km/h', '28.7 km/h', '30.1 km/h','3',CAST('자전거로 총 달린 거리를 구합니다. 첫 2시간 동안: 8km X 5바퀴 = 40km 다음 1시간 동안: 8km X 3바퀴 = 24km 따라서, 총 3시간 동안 달린 거리는 40 + 24 = 64km입니다. 평균 속력은 총 이동 거리(64km)를 총 시간(3시간)으로 나눈 값입니다. 즉, 64 / 3 = 약 21.33 km/h입니다. 하지만 이 값은 소수점 첫째 자리를 올림하여 21.3km/h가 됩니다.' AS BINARY )),
(2, 0, 0, 0.0, 'MATH', '속력',CAST('민수는 원형 트랙을 자전거로 시계 방향으로 돌면서 첫 30분 동안 4바퀴, 그 다음 30분 동안 5바퀴, 마지막 1시간 동안 10바퀴를 돌았다. 원형 트랙의 총 길이가 6km라면, 민수가 2시간 동안 자전거를 탄 평균 속력은 몇 km/h인가?' AS BINARY),CAST('' AS BINARY),'48 km/h', '57 km/h', '60 km/h', '66 km/h', '78 km/h','2',CAST('민수가 자전거로 총 달린 거리를 구합니다. 첫 30분 동안: 6km X 4바퀴 = 24km 다음 30분 동안: 6km X 5바퀴 = 30km 마지막 1시간 동안: 6km X 10바퀴 = 60km 따라서 총 이동 거리는 24 + 30 + 60 = 114km입니다. 평균 속력은 총 이동 거리(114km)를 총 시간(2시간)으로 나눈 값입니다. 즉, 114 / 2 = 57 km/h 입니다.' AS BINARY)),
(3, 0, 0, 0.0, 'MATH', '용액의 농도',    CAST('어떤 소금물 500g에 100g 물을 증발시켰더니 15%의 소금물이 되었다. 처음 소금물의 농도는 몇 %인가?' AS BINARY),    CAST('' AS BINARY),    '10%', '12%', '13%', '14%', '16%',    '2',    CAST('초기 소금물의 농도는 미지수 x로 두고, 최종 농도는 15%이며, 초기 무게는 500g이고 최종 무게는 400g입니다. 이를 대입하여 초기 농도를 구해봅시다. 초기 농도 x 초기 무게 = 최종 농도 x 최종 무게 x * 500g = 15% * 400g 500x = 0.15 * 400 500x = 60 x = 60 / 500 x = 0.12 또는 12% 따라서 처음 소금물의 농도는 12%입니다.' AS BINARY)),
(4, 0, 0, 0.0, 'MATH', '용액의 농도',CAST('어떤 소금물 600g에 300g 물을 증발시켰더니 20%의 소금물이 나왔다. 처음 소금물의 농도는 몇 %인가?' AS BINARY),CAST('' AS BINARY),'10%', '16%', '17%', '18%', '20%','1',CAST('초기 소금물의 농도는 미지수 x로 두고, 최종 농도는 20%이며, 초기 무게는 600g이고 최종 무게는 300g입니다. 이를 대입하여 초기 농도를 구해봅시다. 초기 농도 x 초기 무게 = 최종 농도 x 최종 무게 x * 600g = 20% * 300g 600x = 0.20 * 300 600x = 60 x = 60 / 600 x = 0.10 또는 10% 따라서 처음 소금물의 농도는 10%입니다.' AS BINARY)),
(5, 0, 0, 0.0, 'REASONING', '문자추리',CAST('일정한 규칙으로 나열된 문자를 통해 빈칸에 들어갈 알맞은 문자를 고르시오' AS BINARY),CAST('A, C, F, J, ( )' AS BINARY),'O', 'K', 'F', 'X', 'S','1',CAST('각 문자의 차이를 보면: A → C: 2칸 이동 C → F: 3칸 이동 F → J: 4칸 이동 이러한 규칙에 따라 J에서 5칸을 이동하면 O가 됩니다. 따라서 정답은 3) O입니다.' AS BINARY)),
(6, 0, 0, 0.0, 'REASONING', '문자추리',CAST('일정한 규칙으로 나열된 문자를 통해 빈칸에 들어갈 알맞은 문자를 고르시오' AS BINARY),CAST('Z, X, V, T, ( )' AS BINARY),'K', 'F', 'X', 'R', 'P','4',CAST('각 문자 간의 차이를 보면: Z → X: -2칸 X → V: -2칸 V → T: -2칸 따라서 T에서 -2칸 이동하면 R이 됩니다. 따라서 정답은 2) R입니다.' AS BINARY)),
(7, 0, 0, 0.0, 'REASONING', '숫자추리',CAST('다음에 제시되는 수들이 일정한 규칙을 가지고 있다고 할 때, ( ) 안에 알맞은 숫자는?' AS BINARY),CAST('8, 4, 12, 6, 18, 9, ( )' AS BINARY),'12', '15', '22', '27', '31','4',CAST('두 연속되는 수 간의 규칙이 번갈아 나누기 2, 곱하기 3으로 진행되는 패턴을 따릅니다.' AS BINARY)),
(8, 0, 0, 0.0, 'REASONING', '숫자추리',CAST('다음에 제시되는 수들이 일정한 규칙을 가지고 있다고 할 때, ( ) 안에 알맞은 숫자는?' AS BINARY),CAST('2, 6, 12, 20, 30, ( )' AS BINARY),'30', '32', '42', '46', '48','3',CAST('주어진 수열의 규칙을 찾아서 다음에 오는 숫자를 찾아내야 합니다. 6 - 2 = 4 12 - 6 = 6 20 - 12 = 8 30 - 20 = 10 각 숫자 사이의 차이가 4, 6, 8, 10과 같이 증가하는 것을 확인할 수 있습니다. 이 패턴을 이어가면, 다음 차이는 12가 될 것입니다. 수열의 마지막 숫자 30에 다음 차이를 더합니다. 30 + 12 = 42' AS BINARY)),
(9, 0, 0, 0.0, 'LANGUAGE', '관계비교',CAST('다음 중 관계가 다른 하나는?' AS BINARY),CAST('' AS BINARY),'희망(希望) - 기대(期待)', '위기(危機) - 기회(機會)', '절망(絶望) - 낙담(落膽)', '승리(勝利) - 성공(成功)', '도전(挑戰) - 시도(試圖)','2',CAST('희망- 기대: 유의관계 위기 - 기회: 반의관계 절망 - 낙담: 유의관계 승리 - 성공: 유의관계 도전 - 시도: 유의관계 따라서, 관계가 다른 것은 ''2번 위기 - 기회''입니다. 다른 항목들은 서로 유사한 의미를 가지지만, 위기와 기회는 반대되는 의미를 가지고 있습니다.' AS BINARY)),
(10, 0, 0, 0.0, 'LANGUAGE', '관계비교',CAST('다음 중 관계가 다른 하나는?' AS BINARY),CAST('' AS BINARY),'숙고 - 심려', '속박 - 구속', '채근 - 독촉', '추위 - 동상', '보호 - 보전','4',CAST('숙고 - 심려 : 유의관계 속박 - 구속 : 유의관계 채근 - 독촉 : 유의관계 추위 - 동상 : 원인- 결과 관계 보호 - 보전 : 유의관계 따라서, 관계가 다른 것은 4번 ''추위 - 동상 ''입니다. 다른 항목들은 서로 유사한 의미를 가지지만, 추위와 동상은 원인과 결과 관계를 가지고 있습니다.' AS BINARY)),
(11, 0, 0, 0.0, 'LANGUAGE', '어휘추론',CAST('다음 중 괄호 안에 상응하는 단어로 적합한 것은?' AS BINARY),CAST('응고하다 : 굳어지다 = 방자하다 : ( )' AS BINARY),'공손하다', '누설하다', '교만하다', '겸양하다', '정중하다','3',CAST('제시된 단어 응고하다와 굳어지다는 모두 액체 따위가 엉겨서 뭉쳐 딱딱하게 굳음을 뜻하므로 유의관계입니다. 따라서 어려워하거나 조심스러워하는 태도가 없이 무례하고 건방지다는 의미의 ''방자하다''와 유의관계인 ''교만하다''가 적절합니다.' AS BINARY)),
(12, 0, 0, 0.0, 'LANGUAGE', '어휘추론',CAST('다음 중 괄호 안에 상응하는 단어로 적합한 것은?' AS BINARY),CAST('감성 : 자극 = 화석 : ( )' AS BINARY),'증명', '발명', '발굴', '퇴적', '생산','3',CAST('제시된 단어 공감대와 형성은 ''공감대를 형성하다''로 쓸 수 있으므로 목적어와 서술어의 관계입니다. 따라서 ''체력''을 목적어로 쓸 수 있는 ''''발굴''이 적절합니다.' AS BINARY));

-- insert exam fixture
INSERT INTO exam(id, is_finished, score, exam_type, start_time, end_time, duration, member_id) VALUES
(1, false, 0, 'EVENLY', '2024-11-16 12:25:00', NULL, NULL, 1),
(2, true, 50, 'EVENLY', '2024-11-16 12:25:00', '2024-11-16 12:35:00', '10.0', 1),
(3, false, 0, 'BY_PROBLEM_TYPE', '2024-11-16 12:25:00', NULL, NULL, 1),
(4, true, 50, 'BY_PROBLEM_TYPE', '2024-11-16 12:25:00', '2024-11-16 12:40:00', '15.0', 1),
(5, true, 0, 'MOCK_EXAM', '2024-11-16 12:25:00', NULL, NULL, 1),
(6, true, 70, 'MOCK_EXAM', '2024-11-16 12:25:00', '2024-11-16 12:45:00', '20.0', 1);

-- insert answer fixture
INSERT INTO answer(id, is_correct, is_submitted, problem_number, submit_count, submit_answer, submit_time, exam_id, problem_id) VALUES
(1, false, true, 1, 1, '3', '2024-11-16 12:26:00', 1,  9),
(2, true, true, 1, 1, '3', '2024-11-16 12:25:30', 4,  1),
(3, false, true, 2, 1, '3', '2024-11-16 12:26:00', 4,  2);

-- insert examsheet fixture
INSERT INTO examsheet(id, exam_title, exam_count, average_score, average_duration) VALUES
(1, "MOCK_EXAM", 0, 0, 0);

-- insert problemsheet fixture
INSERT INTO problemsheet(examsheet_id, problem_number, problem_id) VALUES
(1, 1, 5),
(1, 2, 6),
(1, 3, 7),
(1, 4, 8);


