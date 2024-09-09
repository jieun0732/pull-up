"use client";

import Button from "@/component/ui/Button";
import { useRouter } from "next/navigation";
import Text from "@/component/ui/Text";
import formatNumber from "@/utils/formatNumber";
import BackIcon from "@/assets/icon/BackIcon";
import { categoryMap, entryMap } from "@/constants/constants";
import useSWR from "swr";
import { ProblemInfo } from "@/types/problemType";
import { API, fetcher } from "@/lib/API";
import { roundUpNumber } from "@/utils/roundUpNumber";

export default function Page({
  params,
}: {
  params: {
    id: string;
  };
}) {
  const router = useRouter();
  const { id } = params;

  const { data, error } = useSWR<ProblemInfo>(
    `${API}/exams/incorrect-answers/${id}`,
    fetcher,
  );

  if (!data) return;

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
            {data.problem.type}
            </Button>
          </div>

          <Text size="body-03" className="relative mb-4 px-5">
            {data.problem.question}
          </Text>
        
          {data.problem.explanation && (
            <div className="relative mx-5 mb-12 flex items-center justify-center rounded-md border border-solid border-gray02 py-5">
              <Text size="body-03">{data.problem.explanation}</Text>
            </div>
          )}

        </div>
        {data.problem.choices.map((choice, idx) => {
          let choiceStyle;
          let choiceNumStyle;
          if (String(idx + 1) === data.problem.answer) {
            choiceStyle = "bg-green02 text-green01";
            choiceNumStyle = "bg-green01 text-white";
          } else if (String(idx + 1) === data.chosenAnswer) {
            choiceStyle = "bg-red02 text-red01";
            choiceNumStyle = "bg-red01 text-white";
          } else {
            choiceStyle = "";
            choiceNumStyle = "border border-solid border-black01";
          }
          return (
            <div
              key={idx}
              className={`flex w-full items-center gap-4 px-5 py-4 ${choiceStyle}`}
            >
              <div
                className={`flex h-10 w-10 items-center justify-center rounded-full ${choiceNumStyle}`}
              >
                {idx + 1}
              </div>
              <div> {choice}</div>
            </div>
          )
        })}

        <div className="relative px-5">
          <Text size="caption-01" className="mb-3 text-end">
            정답률 {roundUpNumber(data?.problem.incorrectRate || 0)}%
          </Text>
          <div className="w-full rounded-lg bg-[#f2f3f6] px-5 py-7">
            <Text size="body-03" className="mb-3">
              해설
            </Text>
            <Text size="body-04" className="whitespace mb-3">
              {data.problem.answerExplain}
            </Text>
          </div>
        </div>
      </div>
    </>
  );
}
