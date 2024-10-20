"use client";

import Button from "@/component/ui/Button";
import { useRouter } from "next/navigation";
import useSWR, { mutate } from "swr";
import Text from "@/component/ui/Text";
import { API, fetcher } from "@/lib/API";
import { ProblemInfo, Problem } from "@/types/problemType";
import { roundUpNumber } from "@/utils/roundUpNumber";
import { categoryMap, entryMap } from "@/constants/constants";
import { FormatQuestion } from "@/utils/FormatQuestion";
import useModal from "@/hooks/useModal";
import { ConfirmModal } from "@/component/ui/ConfirmModal";

export default function Page({
  params,
}: {
  params: {
    subject: string;
    category: string;
    id: string;
  };
}) {
  const { openModal, closeModal, Modal } = useModal({ initialOpen: false });

  const router = useRouter();
  const { subject, category, id } = params;

  const memberID = localStorage.getItem("memberId") || "";

  const entry = entryMap[params.subject];
  const categoryKor = categoryMap[params.category];

  let type = "";

  if (categoryKor !== "골고루") {
    type = localStorage.getItem("type") || "";
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
  const nextProblem: ProblemInfo = data[Number(params.id)];

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
            <div className="min-h-[40px] min-w-0 flex-1">
              {FormatQuestion(choice)}
            </div>
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
        <button
          onClick={openModal}
          className="ml-auto rounded-t-2xl rounded-bl-2xl bg-blue03 px-6 py-2 text-[13px] text-blue01 shadow-[2px_2px_20px_0px_rgba(0,0,0,0.16)]"
        >
          학습 종료하기
        </button>

        {params.id == "1" ? (
          <Button
            size="large"
            color="active"
            className="mt-4 backdrop-blur-sm"
            onClick={() => {
              if (data.length == Number(id)) {
                // case 1) 지금 문제가 마지막 문제인 경우
                router.push(`/main/sectional/${subject}/${category}/result`);
              } else if (nextProblem.chosenAnswer) {
                // case 2) 다음 문제가 이미 풀어져 있는 상태라면 solution페이지로 가야함
                router.push(
                  `/main/sectional/${subject}/${category}/${Number(id) + 1}/solution`,
                );
              } else {
                // case 3) 만약 다음 문제가 풀어져 있지 않다면 다음 문제 푸는 페이지로 가야 함
                router.push(
                  `/main/sectional/${subject}/${category}/${Number(id) + 1}/`,
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
                  `/main/sectional/${subject}/${category}/${Number(id) - 1}/solution`,
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
                  // case 1) 지금 문제가 마지막 문제인 경우
                  router.push(`/main/sectional/${subject}/${category}/result`);
                } else if (nextProblem.chosenAnswer) {
                  // case 2) 다음 문제가 이미 풀어져 있는 상태라면 solution페이지로 가야함
                  router.push(
                    `/main/sectional/${subject}/${category}/${Number(id) + 1}/solution`,
                  );
                } else {
                  // case 3) 만약 다음 문제가 풀어져 있지 않다면 다음 문제 푸는 페이지로 가야 함
                  router.push(
                    `/main/sectional/${subject}/${category}/${Number(id) + 1}/`,
                  );
                }
              }}
            >
              다음 문제
            </Button>
          </div>
        )}
      </div>
      <Modal>
        <ConfirmModal
          onLeft={() =>
            router.push(
              `/main/sectional/${params.subject}/${params.category}/result`,
            )
          }
          onRight={closeModal}
          title="정말로 학습을 종료하실 건가요?"
          description="나가면 현재까지 푼 문제만 저장돼요!"
          left="종료할래요"
          right="계속 풀고 싶어요."
        />
      </Modal>
    </>
  );
}
