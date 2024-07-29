"use client";

import Button from "@/component/ui/Button";
import { useRouter } from "next/navigation";
import { dummysolution } from "@/constants/dummyq";
import Text from "@/component/ui/Text";
import formatNumber from "@/utils/formatNumber";
import { dummyQ } from "@/constants/dummyq";
import BackIcon from "@/assets/icon/BackIcon";
import Header from "@/component/ui/Header";

export default function Page({
  params,
}: {
  params: {
    subject: string;
    type: string;
    id: string;
  };
}) {
  const router = useRouter();
  const { subject, type, id } = params;

  return (
    <>
      <div className="bg-whtie relative flex flex-col items-center pb-7 pt-14">
        <div className="h-11 w-full px-5">
          <div className="relative">
            <BackIcon onClick={() => router.back()} />
          </div>
        </div>
        <div className="relative flex w-full flex-col">
          <div className="mt-4 flex w-full items-center justify-between px-5">
            <Text size="head-03" className="mb-4">
              문제 {formatNumber(Number(params.id))}
            </Text>
            <Button size="small" color="nonactive">
              유의어
            </Button>
          </div>

          <Text size="body-03" className="relative mb-4 px-5">
            {dummyQ.question}
          </Text>
          <div className="relative mx-5 mb-12 flex items-center justify-center rounded-md border border-solid border-gray02 py-5">
            <Text size="body-03">{dummyQ.questionD}</Text>
          </div>
        </div>
        {dummysolution.choice.map((choice) => {
          let choiceStyle;
          let choiceNumStyle;
          if (choice.id === dummysolution.isAnswer) {
            choiceStyle = "bg-green02 text-green01";
            choiceNumStyle = "bg-green01 text-white";
          } else if (choice.id === dummysolution.isSelected) {
            choiceStyle = "bg-red02 text-red01";
            choiceNumStyle = "bg-red01 text-white";
          } else {
            choiceStyle = "";
            choiceNumStyle = "border border-solid border-black01";
          }
          return (
            <div
              key={choice.id}
              className={`flex w-full items-center gap-4 px-5 py-4 ${choiceStyle}`}
            >
              <div
                className={`flex h-10 w-10 items-center justify-center rounded-full ${choiceNumStyle}`}
              >
                {choice.id}
              </div>
              <div> {choice.name}</div>
            </div>
          );
        })}

        <div className="relative px-5">
          <Text size="caption-01" className="mb-3 text-end">
            정답률 15%
          </Text>
          <div className="w-full rounded-lg bg-[#f2f3f6] px-5 py-7">
            <Text size="body-03" className="mb-3">
              해설
            </Text>
            <Text size="body-04" className="whitespace mb-3">
              해설해설해설해설해설해설해설해설해설해설해설해설해설해설해설해설
              해설해설해설해설해설해설해설해설해설해설해설해설해설해설해설해설
            </Text>
          </div>
        </div>
      </div>
    </>
  );
}
