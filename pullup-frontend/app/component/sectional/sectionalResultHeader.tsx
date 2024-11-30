"use client";

import Text from "../ui/Text";
import { entryMap } from "@/constants/constants";
import { UserLogin } from "@/types/userType";
import { SectionalResultResponseType } from "@/types/sectionalType";

interface SectionalResultHeaderProps {
  data: SectionalResultResponseType;
  user: UserLogin;
  category: "EVENLY" | "BY_PROBLEM_TYPE";
}

export default function SectionalResultHeader({
  data,
  user,
  category,
}: SectionalResultHeaderProps) {
  if (!data.isFinished) {
    return (
      <>
        <Text size="head-02" className="self-start">
          {category === "EVENLY"
            ? `    ${user.name}님은 ${entryMap[data.entry]}영역 ${data.totalProblemCount}문제 중`
            : `    ${user.name}님은 ${entryMap[data.entry]} 유형 ${data.totalProblemCount}문제 중`}
        </Text>
        <Text size="head-02" className="self-start">
          {data.totalProblemCount - data.leftProblemCount}개의 학습을 완료했어요
        </Text>
        <Text
          size="head-05"
          color="text-gray01"
          className="mb-6 mt-2 self-start"
        >
          {category === "BY_PROBLEM_TYPE"
            ? `남은 문제를 풀고 ${data.results[0].problemType}영역을 정복해보세요`
            : `남은 문제를 풀고  ${data.results[0].problemType} 유형을 정복해보세요`}
        </Text>
      </>
    );
  }
  if (data.isFinished == true) {
    return (
      <>
        <Text size="head-02" className="self-start">
          {category === "EVENLY"
            ? `${user.name}님은 ${entryMap[data.entry]}영역`
            : `${user.name}님은 ${data.results[0].problemType} 유형`}
        </Text>
        <Text size="head-02" className="self-start">
          총 {data.totalProblemCount}문제 중 {data.correctProblemCount}문제를
          맞췄어요!
        </Text>
      </>
    );
  }
}
