"use client";

import { useParams, useRouter } from "next/navigation";
import CloseIcon from "@/assets/icon/closeIcon";
import Text from "@/component/ui/Text";
import Button from "@/component/ui/Button";
import { useState } from "react";
import { dummyQ } from "@/constants/dummyq";
import useModal from "@/hooks/useModal";
import formatNumber from "@/utils/formatNumber";
import { ConfirmModal } from "@/component/ui/ConfirmModal";
import ChoiceItem from "@/component/choiceItem";

export default function Page() {
  const router = useRouter();
  const params = useParams<{ subject: string; type: string; id: string }>();
  const [selectedId, setSelectedId] = useState<string>("");

  const { openModal, closeModal, Modal } = useModal({ initialOpen: false });

  return (
    <>
      <div className="bg-whtie relative flex flex-col items-center pb-7 pt-14">
        <div className="relative flex w-full flex-col px-5">
          <div className="relative mb-8 w-full">
            <CloseIcon onClick={openModal} />
            <span className="ml-9 text-[13px] font-normal text-gray01">
              8문제 남았어요!
            </span>
          </div>

          <div className="flex w-full items-center justify-between">
            <Text size="head-03" className="mb-4">
              문제 {formatNumber(Number(params.id))}
            </Text>
            <Button size="small" color="nonactive">
              유의어
            </Button>
          </div>

          <Text size="body-03" className="relative mb-4">
            {dummyQ.question}
          </Text>
          <div className="relative mb-12 flex items-center justify-center rounded-md border border-solid border-gray02 py-5">
            <Text size="body-03">{dummyQ.questionD}</Text>
          </div>
        </div>

        {dummyQ.choice.map((item) => (
          <ChoiceItem
            key={item.id}
            item={item}
            isSelected={selectedId === String(item.id)}
            selectedId={selectedId}
            setSelectedId={setSelectedId}
          />
        ))}

        <div className="fixed bottom-0 mb-11 flex w-full flex-col px-5 py-4">
          {(() => {
            if (dummyQ.id === 1) {
              return (
                <Button size="large" color="active" className="mt-4">
                  채점하기
                </Button>
              );
            } else if (dummyQ.id > 1 && dummyQ.id < 20) {
              return (
                <div className="mt-4 flex gap-2">
                  <Button size="medium" color="activeBorder">
                    이전 문제
                  </Button>
                  <Button size="medium" color="active">
                    채점하기
                  </Button>
                </div>
              );
            } else if (dummyQ.id === 20) {
              return (
                <Button size="large" color="activeBorder" className="mt-4">
                  이전 문제
                </Button>
              );
            }
          })()}
        </div>
        <Modal>
          <ConfirmModal
            onLeft={() =>
              router.push(
                `/main/sectional/${params.subject}/${params.type}/result`,
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
