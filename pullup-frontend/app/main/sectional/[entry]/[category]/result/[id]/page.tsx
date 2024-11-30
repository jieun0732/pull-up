"use client";

import Button from "@/component/ui/Button";
import { useRouter } from "next/navigation";
import Text from "@/component/ui/Text";
import formatNumber from "@/utils/formatNumber";
import { BackIcon } from "@/assets/icon/Icons";
import useSWR from "swr";
import { API, fetcher } from "@/lib/API";
import { roundUpNumber } from "@/utils/roundUpNumber";
import { FormatQuestion } from "@/utils/FormatQuestion";
import useProblemStore from "@/stores/useProblemStore";
import { SectionalNextResponseType } from "@/types/sectionalType";
import { Entry } from "@/types/problemType";
import Spinner from "@/component/ui/Spinner";

export default function Page({
  params,
}: {
  params: {
    entry: Entry;
    category: "EVENLY" | "BY_PROBLEM_TYPE";
    id: string;
  };
}) {
  const router = useRouter();
  const { examId } = useProblemStore();

  const { data, error } = useSWR<SectionalNextResponseType>(
    `${API}/exams/next/${examId}?problemNumber=${params.id}`,
    fetcher,
  );

  return (
    <>
      {data ? (
        <div className="bg-whtie relative flex flex-col items-center pb-7 pt-20">
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
                {data.problemType}
              </Button>
            </div>

            <Text size="body-03" className="relative mb-4 px-5">
              {data.question}
            </Text>

            {data.example && (
              <div className="relative mx-5 mb-12 flex items-center justify-center rounded-md border border-solid border-gray02 py-5">
                <Text size="body-03">{data.example}</Text>
              </div>
            )}
          </div>
          {data.choices.map((choice, idx) => {
            let choiceStyle;
            let choiceNumStyle;
            if (
              data.explanation.submitAnswer &&
              idx + 1 === data.explanation.submitAnswer
            ) {
              choiceStyle = "bg-green02 text-green01";
              choiceNumStyle = "bg-green01 text-white";
            } else if (idx + 1 === data.explanation.correctAnswer) {
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
                <div className="min-h-[40px] min-w-0 flex-1">
                  {FormatQuestion(choice)}
                </div>
              </div>
            );
          })}

          <div className="relative px-5">
            <Text size="caption-01" className="mb-3 text-end">
              정답률 {roundUpNumber(data.explanation.incorrectRate || 0)}%
            </Text>
            <div className="w-full rounded-lg bg-[#f2f3f6] px-5 py-7">
              <Text size="body-03" className="mb-3">
                해설
              </Text>
              <Text size="body-04" className="whitespace mb-3">
                {data.explanation.explanation}
              </Text>
            </div>
          </div>
        </div>
      ) : (
        <Spinner />
      )}
    </>
  );
}
