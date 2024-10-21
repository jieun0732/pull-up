"use client";

import Text from "../ui/Text";
import { entryMap } from "@/constants/constants";
import { ProblemInfo } from "@/types/problemType";
import useSWR from "swr";
import { API, fetcher } from "@/lib/API";
import { User } from "@/types/userType";
import LocalStorage from "@/utils/LocalStorage";

interface QuestionProps {
  problems: ProblemInfo[];
  params: {
    subject: string;
    category: string;
  };
}

const SectionalResultHeader: React.FC<QuestionProps> = ({
  problems,
  params,
}) => {
  const { subject, category } = params;
  const totalProblemsCount = problems.length;
  const isFinished = problems.every((item) => item.chosenAnswer !== null);
  const solvedCount = problems.filter(
    (item) => item.chosenAnswer !== null,
  ).length;
  const isCorrectCount = problems.filter(
    (item) => item.isCorrect == true,
  ).length;

  const type = LocalStorage.getItem("type");

  const { data: user } = useSWR<User>(
    `${API}/members/${LocalStorage.getItem("memberId")}`,
    fetcher,
  );

  if (isFinished == false && user) {
    return (
      <>
        <Text size="head-02" className="self-start">
          {category === "mix"
            ? `    ${user.data.name}님은 ${entryMap[subject]}영역 ${totalProblemsCount}문제 중`
            : `    ${user.data.name}님은 ${type} 유형 ${totalProblemsCount}문제 중`}
        </Text>
        <Text size="head-02" className="self-start">
          {solvedCount}개의 학습을 완료했어요
        </Text>
        <Text
          size="head-05"
          color="text-gray01"
          className="mb-6 mt-2 self-start"
        >
          {category === "mix"
            ? `남은 문제를 풀고 ${entryMap[subject]}영역을 정복해보세요`
            : `남은 문제를 풀고  ${type} 유형을 정복해보세요`}
        </Text>
      </>
    );
  }
  if (isFinished == true && user) {
    return (
      <>
        <Text size="head-02" className="self-start">
          {category === "mix"
            ? `${user.data.name}님은 ${entryMap[subject]}영역`
            : `${user.data.name}님은 ${type} 유형`}
        </Text>
        <Text size="head-02" className="self-start">
          총 {totalProblemsCount}제 중 {isCorrectCount}문제를 맞췄어요!
        </Text>
      </>
    );
  }
};

export default SectionalResultHeader;
