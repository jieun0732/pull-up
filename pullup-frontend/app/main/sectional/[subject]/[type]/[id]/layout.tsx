"use client";

import { useParams, useRouter } from "next/navigation";
import CloseIcon from "@/assets/icon/closeIcon";
import Text from "@/component/ui/Text";
import Button from "@/component/ui/Button";
import { dummyQ } from "@/constants/dummyq";
import useModal from "@/hooks/useModal";
import formatNumber from "@/utils/formatNumber";
import { ConfirmModal } from "@/component/ui/ConfirmModal";
import ThinProgressBar from "@/component/sectional/thinProgressbar";

export default function Layout({
  children,
  params,
}: {
  children: React.ReactNode;
  params: {
    subject: string;
    type: string;
    id: string;
  };
}) {
  const router = useRouter();

  const { openModal, closeModal, Modal } = useModal({ initialOpen: false });

  return (
    <>
      <div className="bg-whtie relative flex flex-col items-center pb-7 pt-14">
        <div className="relative flex w-full flex-col">
          <div className="relative mx-5 w-full">
            <CloseIcon onClick={openModal} />
            <span className="ml-9 text-[13px] font-normal text-gray01">
              8문제 남았어요!
            </span>
          </div>

          <ThinProgressBar now={2} total={20} />

          <div className="mt-11 flex w-full items-center justify-between px-5">
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

        {children}
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
