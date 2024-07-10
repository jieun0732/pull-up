"use client";

import { useRouter } from "next/navigation";
import CloseIcon from "@/assets/icon/closeIcon";
import ToggleIcon from "@/assets/icon/toggleIcon";
import Text from "@/component/ui/Text";
import Button from "@/component/ui/Button";
import { useState } from "react";
import { dummyQ } from "../../../constants/dummyq";
import QuestionList from "@/component/mocktest/questionList";
import useModal from "@/hooks/useModal";
import WarningIcon from "@/assets/icon/warningIcon";
import formatNumber from "@/utils/formatNumber";
import pop from "@/assets/defaultImages/pop.png";
import Image from "next/image";
import { ConfirmModal } from "@/component/ui/ConfirmModal";

export default function Page() {
  const router = useRouter();
  const [selectedId, setSelectedId] = useState<number | null>(null);
  const [showQuestions, setShowQuestions] = useState<boolean>(false);

  const { openModal, closeModal, Modal } = useModal({ initialOpen: false });
  const [step, setStep] = useState(0);
  console.log(step);
  return (
    <>
      <div className="bg-whtie relative flex flex-col items-center pb-7 pt-14">
        {step < 2 && (
          <div
            onClick={() => {
              setStep((prevStep) => prevStep + 1);
            }}
            className="absolute left-0 top-0 z-20 h-[100vh] w-full bg-black opacity-80"
          />
        )}
        {step == 0 && (
          <Image
            src={pop}
            alt="tutorial_moveQuestion"
            className="absolute bottom-0 z-50 w-[calc(100%-32px)]"
            priority
            onClick={() => {
              setStep((prevStep) => prevStep + 1);
            }}
          />
        )}
        <QuestionList
          showQuestions={showQuestions}
          setShowQuestions={setShowQuestions}
        />

        <div className="relative flex w-full flex-col px-5">
          <div className="relative mb-8 w-full text-center">
            <CloseIcon onClick={openModal} />
            <span
              className={`relative rounded bg-white p-3 text-[17px] font-bold ${
                step == 1 ? "z-20" : ""
              }`}
            >
              30:00
            </span>
          </div>
          <span
            onClick={step > 1 ? () => setShowQuestions(true) : undefined}
            className={`relative mb-4 flex w-fit items-center gap-1 rounded bg-white text-[17px] ${
              step == 0 ? "z-20 p-2" : ""
            }`}
          >
            문제 {formatNumber(dummyQ.id)} <ToggleIcon />
          </span>

          <Text size="body-03" className="relative mb-4">
            {dummyQ.question}
          </Text>
          <div className="relative mb-12 flex items-center justify-center rounded-md border border-solid border-gray02 py-5">
            <Text size="body-03">{dummyQ.questionD}</Text>
            {step == 0 && (
              <>
                <Text
                  size="body-02"
                  color="text-white"
                  className="absolute -top-7 left-4 z-20"
                >
                  화살표를 누르면
                </Text>
                <Text
                  size="body-02"
                  color="text-white"
                  className="absolute -top-0 left-4 z-20"
                >
                  쉽게 문제 이동을 할 수 있는 창이 나타나요!
                </Text>
              </>
            )}
          </div>
        </div>

        {dummyQ.choice.map((item) => {
          const isSelected = selectedId === item.id;
          return (
            <div
              key={item.id}
              className={`flex w-full items-center gap-4 px-5 py-4 ${
                isSelected ? "bg-[#ebebeb]" : "bg-white"
              }`}
              onClick={() => setSelectedId(item.id)}
            >
              <div
                className={`flex h-10 w-10 items-center justify-center rounded-full border border-solid border-black01 ${
                  isSelected ? "bg-black01 text-white" : "bg-white text-black01"
                }`}
              >
                {item.id}
              </div>
              <div>{item.name}</div>
            </div>
          );
        })}

        <div className="mb-11 flex w-full flex-col px-5 py-4">
          <button
            onClick={() => router.push("/main/mocktest/report")}
            className="ml-auto rounded-t-2xl rounded-bl-2xl bg-gray03 px-6 py-2 text-gray02 shadow-[2px_2px_20px_0px_rgba(0,0,0,0.16)]"
          >
            제출하기
          </button>
          {(() => {
            if (dummyQ.id === 1) {
              return (
                <Button size="large" color="active" className="mt-4">
                  다음 문제
                </Button>
              );
            } else if (dummyQ.id > 1 && dummyQ.id < 20) {
              return (
                <div className="mt-4 flex gap-2">
                  <Button size="medium" color="activeBorder">
                    이전 문제
                  </Button>
                  <Button size="medium" color="active">
                    다음 문제
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
            onLeft={() => router.push("/main/mocktest")}
            onRight={closeModal}
            title="모의고사를 그만 푸실 건가요?"
            description="나가면 현재까지 푼 문제들은 저장되지 않아요!"
            left="나갈래요"
            right="계속 풀래요"
          />
        </Modal>
      </div>
    </>
  );
}
