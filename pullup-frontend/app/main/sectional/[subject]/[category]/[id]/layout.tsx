"use client";

import { useRouter } from "next/navigation";
import { CloseIcon } from "@/assets/icon/Icons";
import Text from "@/component/ui/Text";
import Button from "@/component/ui/Button";
import useModal from "@/hooks/useModal";
import formatNumber from "@/utils/formatNumber";
import { ConfirmModal } from "@/component/ui/ConfirmModal";
import ThinProgressBar from "@/component/sectional/thinProgressbar";
import { API } from "@/lib/API";
import useSWR from "swr";
import { ProblemInfo } from "@/types/problemType";
import { entryMap, categoryMap } from "@/constants/constants";

export default function Layout({
  children,
  params,
}: {
  children: React.ReactNode;
  params: {
    subject: string;
    category: string;
    id: string;
  };
}) {
  const router = useRouter();

  const { openModal, closeModal, Modal } = useModal({ initialOpen: false });

  const memberId = localStorage.getItem('memberId') || "";
  const entry = entryMap[params.subject];
  const category = categoryMap[params.category];
  
  let type = ""

  if (category !== "골고루") {
    type = localStorage.getItem('type') || ""
  }


  const queryString = new URLSearchParams({
    memberId,
    entry,
    category,
    type,
  }).toString();

  const { data } = useSWR(
    {
      url: `${API}/exams`,
      ids: [`problems?${queryString}`, `next?${queryString}`],
    },
    ({ url, ids }) =>
      Promise.all(
        ids.map((id) =>
          fetch(`${url}/${id}`).then(async (response) => {
            return response.json();
          }),
        ),
      ),
  );

  if (!data) return;

  const nowIndex = data[0].findIndex(
    (item: ProblemInfo) => item.id === Number(params.id),
  );

  const nowProblem: ProblemInfo = data[0][Number(params.id) - 1];
  // .find(
  //   (item: ProblemInfo) => item.id === Number(params.id),
  // );

  console.log(nowProblem)
  return (
    <>
      <div className="bg-whtie relative flex flex-col items-center pb-7 pt-14">
        <div className="relative flex w-full flex-col">
          <div className="relative mx-5 w-full">
            <CloseIcon onClick={openModal} />
            <span className="ml-9 text-[13px] font-normal text-gray01">
              {data[0].length - Number(params.id)}문제 남았어요!
            </span>
          </div>

          <ThinProgressBar now={nowIndex} total={data[0].length} />

          <div className="mt-11 flex w-full items-center justify-between px-5">
            <Text size="head-03" className="mb-4">
              문제 {formatNumber(params.id)}
            </Text>
            <Button size="small" color="nonactive">
              {nowProblem.problem.type}
            </Button>
          </div>

          <Text size="body-03" className="relative mb-4 px-5">
            {nowProblem.problem.question}
          </Text>

          {nowProblem.problem.explanation && (
            <div className="relative mx-5 mb-12 flex items-center justify-center rounded-md border border-solid border-gray02 py-5">
              <Text size="body-03">{nowProblem.problem.explanation}</Text>
            </div>
          )}
        </div>

        {children}
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
      </div>
    </>
  );
}
