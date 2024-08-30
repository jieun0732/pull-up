"use client";

import { useRouter } from "next/navigation";
import Text from "../ui/Text";
import Button from "../ui/Button";
import WrongIcon from "@/assets/icon/problem/wrongIcon";
import CorrectIcon from "@/assets/icon/problem/correctIcon";
import ActiveQIcon from "@/assets/icon/problem/activeQIcon";
import NonActiveQIcon from "@/assets/icon/problem/nonActiveQIcon";
import formatNumber from "@/utils/formatNumber";
import { ProblemInfo } from "@/types/problemType";
import { useRef } from "react";

type ButtonType = "red" | "green" | "active" | "nonactive";
type QuestionType = "nonactive" | "active" | "wrong" | "right";
interface ItemPropType1 {
  item: ProblemInfo;
  params: {
    subject: string;
    type: string;
  };
}

export default function SectionalResultItem({ item, params }: ItemPropType1) {
  const router = useRouter();
  const { subject, type } = params;
  const isFirst = true;

  if (item.chosenAnswer && item.isCorrect) {
    return (
      <div className="mb-4 w-full rounded-md border-2 border-solid border-white03 bg-white px-6 py-4 shadow-[2px_2px_20px_0px_rgba(0,0,0,0.07)]">
        <div className="flex gap-2">
          <CorrectIcon />
          <div className="mb-2">
            <Text size="head-04">문제 {formatNumber(item.id)} </Text>
            <Text size="caption-01" color="text-gray02">
              {item.problem.type}
            </Text>
          </div>
        </div>
        <Button
          size="large"
          color="green"
          onClick={() =>
            router.push(`/main/sectional/${subject}/${type}/result/${item.id}`)
          }
        >
          해설 확인하기
        </Button>
      </div>
    );
  } else if (item.chosenAnswer && !item.isCorrect) {
    return (
      <div className="mb-4 w-full rounded-md border-2 border-solid border-white03 bg-white px-6 py-4 shadow-[2px_2px_20px_0px_rgba(0,0,0,0.07)]">
        <div className="flex gap-2">
          <WrongIcon />
          <div className="mb-2">
            <Text size="head-04">문제 {formatNumber(item.id)} </Text>
            <Text size="caption-01" color="text-gray02">
              {item.problem.type}
            </Text>
          </div>
        </div>
        <Button
          size="large"
          color="red"
          onClick={() =>
            router.push(`/main/sectional/${subject}/${type}/result/${item.id}`)
          }
        >
          해설 확인하기
        </Button>
      </div>
    );
  } else if (item.chosenAnswer && !item.isCorrect) {
    return (
      <div className="mb-4 w-full rounded-md border-2 border-solid border-white03 bg-white px-6 py-4 shadow-[2px_2px_20px_0px_rgba(0,0,0,0.07)]">
        <div className="flex gap-2">
          <WrongIcon />
          <div className="mb-2">
            <Text size="head-04">문제 {formatNumber(item.id)} </Text>
            <Text size="caption-01" color="text-gray02">
              {item.problem.type}
            </Text>
          </div>
        </div>
        <Button
          size="large"
          color="red"
          onClick={() =>
            router.push(`/main/sectional/${subject}/${type}/result/${item.id}`)
          }
        >
          해설 확인하기
        </Button>
      </div>
    );
  } else if (isFirst == true) {
    return (
      <div className="mb-4 w-full rounded-md border-2 border-solid border-white03 bg-white px-6 py-4 shadow-[2px_2px_20px_0px_rgba(0,0,0,0.07)]">
        <div className="flex gap-2">
          <ActiveQIcon />
          <div className="mb-2">
            <Text size="head-04">문제 {formatNumber(item.id)} </Text>
            <Text size="caption-01" color="text-gray02">
              {item.problem.type}
            </Text>
          </div>
        </div>
        <Button
          size="large"
          color="active"
          onClick={() =>
            router.push(`/main/sectional/${subject}/${type}/${item.id}`)
          }
        >
          이어서 풀기
        </Button>
      </div>
    );
  } else if (isFirst == false) {
    return (
      <div className="mb-4 w-full rounded-md border-2 border-solid border-white03 bg-white px-6 py-4 shadow-[2px_2px_20px_0px_rgba(0,0,0,0.07)]">
        <div className="flex gap-2">
          <NonActiveQIcon />
          <div className="mb-2">
            <Text size="head-04">문제 {formatNumber(item.id)} </Text>
            <Text size="caption-01" color="text-gray02">
              {item.problem.type}
            </Text>
          </div>
        </div>
        <Button size="large" color="nonactive">
          이어서 풀기
        </Button>
      </div>
    );
  }
}
