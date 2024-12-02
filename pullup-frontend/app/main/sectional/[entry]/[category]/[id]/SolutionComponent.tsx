"use client";

import Button from "@/component/ui/Button";
import Text from "@/component/ui/Text";
import { useRouter } from "next/navigation";
import { roundUpNumber } from "@/utils/roundUpNumber";
import { FormatQuestion } from "@/utils/FormatQuestion";
import { SectionalNextResponseType, Explanation } from "@/types/sectionalType";

export default function SolutionComponent({
  data,
  solution,
  openModal,
  category,
}: {
  data: SectionalNextResponseType;
  solution: Explanation;
  openModal: () => void;
  category: "EVENLY" | "BY_PROBLEM_TYPE";
}) {
  const router = useRouter();

  if (solution?.submitAnswer === 0) return <></>;

  return (
    <>
      {data.choices.map((choice, idx) => {
        let choiceStyle;
        let choiceNumStyle;
        if (idx + 1 === solution.correctAnswer) {
          choiceStyle = "bg-green02 text-green01";
          choiceNumStyle = "bg-green01 text-white";
        } else if (
          solution?.submitAnswer &&
          idx + 1 === solution.submitAnswer
        ) {
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
            <div className="">{FormatQuestion(choice)}</div>
          </div>
        );
      })}

      <div className="relative w-full px-5 pb-[91px]">
        <Text size="caption-01" className="mb-3 text-end">
          정답률 {roundUpNumber(solution.incorrectRate || 0)}%
        </Text>
        <div className="w-full rounded-lg bg-[#f2f3f6] px-5 py-7">
          <Text size="body-03" className="mb-3">
            해설
          </Text>
          <Text size="body-04" className="whitespace mb-3">
            {solution.explanation}
          </Text>
        </div>
      </div>

      <div className="fixed bottom-0 mb-11 flex w-full flex-col px-5">
        <button
          onClick={openModal}
          className="ml-auto rounded-t-2xl rounded-bl-2xl bg-blue03 px-6 py-2 text-[13px] text-blue01 shadow-[2px_2px_20px_0px_rgba(0,0,0,0.16)]"
        >
          학습 종료하기
        </button>

        {data.problemNumber == 1 ? (
          <Button
            size="large"
            color="active"
            className="mt-4 backdrop-blur-sm"
            onClick={() => {
              if (data.problemNumber == data.totalProblemCount) {
                // case 1) 지금 문제가 마지막 문제인 경우
                router.push(
                  `/main/sectional/${data.entry}/${data.problemType}/result`,
                );
              } else if (
                data.problemNumber <
                data.totalProblemCount - data.leftProblemCount
              ) {
                // case 2) 다음 문제가 이미 풀어져 있는 상태라면 solution페이지로 가야함
                router.push(`/main/sectional/${data.entry}/${category}/result`);
              } else {
                // case 3) 만약 다음 문제가 풀어져 있지 않다면 다음 문제 푸는 페이지로 가야 함
                router.push(
                  `/main/sectional/${data.entry}/${category}/${data.problemNumber + 1}`,
                );
              }
            }}
          >
            다음 문제
          </Button>
        ) : (
          <div className="mt-4 flex gap-2">
            <Button
              size="medium"
              color="activeBorder"
              className="backdrop-blur-sm"
              onClick={() =>
                router.push(
                  `/main/sectional/${data.entry}/${category}/${data.problemNumber - 1}`,
                )
              }
            >
              이전 문제
            </Button>
            <Button
              size="medium"
              color="active"
              className="backdrop-blur-sm"
              onClick={() => {
                if (data.problemNumber === data.totalProblemCount) {
                  // case 1) 지금 문제가 마지막 문제인 경우
                  router.push(
                    `/main/sectional/${data.entry}/${category}/result`,
                  );
                } else {
                  // case 3) 만약 다음 문제가 풀어져 있지 않다면 다음 문제 푸는 페이지로 가야 함
                  router.push(
                    `/main/sectional/${data.entry}/${category}/${data.problemNumber + 1}`,
                  );
                }
              }}
            >
              다음 문제
            </Button>
          </div>
        )}
      </div>
    </>
  );
}
