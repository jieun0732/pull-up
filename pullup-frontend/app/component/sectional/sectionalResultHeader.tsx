"use client";

import Text from "../ui/Text";
import { entryMap } from "@/constants/constants";
import { ProblemInfo } from "@/types/problemType";
interface QuestionProps {
  problems: ProblemInfo[];
  params: {
    subject: string;
    type: string;
  };
}

const SectionalResultHeader: React.FC<QuestionProps> = ({
  problems,
  params,
}) => {
  const { subject, type } = params;
  const totalProblemsCount = problems.length;
  const isFinished = problems.every((item) => item.chosenAnswer !== null);
  const solvedCount = problems.filter(
    (item) => item.chosenAnswer !== null,
  ).length;
  const isCorrectCount = problems.filter(
    (item) => item.isCorrect == true,
  ).length;

  if (isFinished == false) {
    return (
      <>
        <Text size="head-02" className="self-start">
          {type === "mix"
            ? `    이지은님은 ${entryMap[subject]}영역 ${totalProblemsCount}문제 중`
            : `    이지은님은 ㅇㅇㅇ유형 ${totalProblemsCount}문제 중`}
        </Text>
        <Text size="head-02" className="self-start">
          {solvedCount}개의 학습을 완료했어요
        </Text>
        <Text
          size="head-05"
          color="text-gray01"
          className="mb-6 mt-2 self-start"
        >
          {type === "mix"
            ? `남은 문제를 풀고 ${entryMap[subject]}영역을 정복해보세요`
            : `남은 문제를 풀고  ㅇㅇ유형을 정복해보세요`}
        </Text>
      </>
    );
  }
  if (isFinished == true) {
    return (
      <>
        <Text size="head-02" className="self-start">
          {type === "mix"
            ? `ㅇㅇ님은 ${entryMap[subject]}영역`
            : `ㅇㅇ님은 ㅇㅇㅇ유형`}
        </Text>
        <Text size="head-02" className="self-start">
          총 {totalProblemsCount}제 중 {isCorrectCount}문제를 맞췄어요!
        </Text>
      </>
    );
  }
};

export default SectionalResultHeader;
