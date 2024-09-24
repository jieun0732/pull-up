"use client";

import Button from "@/component/ui/Button";
import { useRouter } from "next/navigation";
import useSWR, { mutate } from "swr";
import Text from "@/component/ui/Text";
import { API, fetcher } from "@/lib/API";
import { ProblemInfo, Problem } from "@/types/problemType";
import { roundUpNumber } from "@/utils/roundUpNumber";
import { categoryMap, entryMap } from "@/constants/constants";

export default function Page({
  params,
}: {
  params: {
    subject: string;
    category: string;
    id: string;
  };
}) {
  const router = useRouter();
  const { subject, category, id } = params;

  const memberID = localStorage.getItem("memberId") || "";

  const entry = entryMap[params.subject];
  const categoryKor = categoryMap[params.category];

  let type = ""

  if (categoryKor !== "골고루") {
    type = localStorage.getItem('type') || ""
  }


  const queryString = new URLSearchParams({
    memberId: memberID.toString(),
    entry,
    category: categoryKor,
    type,
  }).toString();

  const { data, error } = useSWR<ProblemInfo[]>(
    `${API}/exams/problems?${queryString}`,
    fetcher,
  );

  if (!data) return;

  const nowProblem: ProblemInfo = data[Number(params.id) - 1];

  return (
    <>
      {nowProblem?.problem.choices.map((choice, idx) => {
        let choiceStyle;
        let choiceNumStyle;
        if (String(idx + 1) === nowProblem.problem.answer) {
          choiceStyle = "bg-green02 text-green01";
          choiceNumStyle = "bg-green01 text-white";
        } else if (String(idx + 1) === nowProblem.chosenAnswer) {
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
        );
      })}

      <div className="relative w-full px-5 pb-[91px]">
        <Text size="caption-01" className="mb-3 text-end">
          정답률 {roundUpNumber(nowProblem?.problem.incorrectRate || 0)}%
        </Text>
        <div className="w-full rounded-lg bg-[#f2f3f6] px-5 py-7">
          <Text size="body-03" className="mb-3">
            해설
          </Text>
          <Text size="body-04" className="whitespace mb-3">
            {nowProblem?.problem.answerExplain}
          </Text>
        </div>
      </div>

      <div className="fixed bottom-0 mb-11 flex w-full flex-col px-5">
        {params.id == "1" ? (
          <Button
            size="large"
            color="active"
            className="backdrop-blur-sm"
            onClick={() =>
              router.push(
                `/main/sectional/${subject}/${category}/${Number(id) + 1}/`,
              )
            }
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
                  `/main/sectional/${params.subject}/${params.category}/${Number(id) - 1}/solution`,
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

                if (data.length == Number(id)) {
                  console.log("여기1")
                  router.push(`/main/sectional/${subject}/${category}/result`);
                } else if (data[Number(id)].chosenAnswer) {
                  router.push(
                    `/main/sectional/${subject}/${category}/${Number(id) + 1}/solution`,
                  )
                } else {
                  router.push(
                    `/main/sectional/${subject}/${category}/${Number(id) + 1}/`,
                  )
                } 
                
                const previousPath = document.referrer;
                console.log(previousPath)
                console.log(window.history)
                console.log(router.forward())
                // if (!previousPath.includes("solution")) {
                //   router.push(
                //     `/main/sectional/${params.subject}/${params.category}/${Number(id) - 1}/solution`,
                //   );
                // } else {
                //   router.push(
                //     `/main/sectional/${subject}/${category}/${Number(id) + 1}/`,
                //   );
                // }
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
