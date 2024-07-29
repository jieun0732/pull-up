"use client";

import { useRouter } from "next/navigation";
import CloseIcon from "@/assets/icon/CloseIcon";
import ToggleIcon from "@/assets/icon/ToggleIcon";
import Text from "@/component/ui/Text";
import Button from "@/component/ui/Button";
import { useState } from "react";
import { dummyQ } from "../../../constants/dummyq";
import QuestionList from "@/component/mocktest/questionList";
import useModal from "@/hooks/useModal";
import formatNumber from "@/utils/formatNumber";
import { ConfirmModal } from "@/component/ui/ConfirmModal";
import { TutorialOverlay } from "@/component/mocktest/tutorial";
import ChoiceItem from "@/component/choiceItem";
import {
  TutorialStep0,
  TutorialStep0Text,
} from "@/component/mocktest/tutorial";
import {
  TutorialStep1,
  TutorialStep1SpeechBubble,
} from "@/component/mocktest/tutorial";

export default function Page() {
  const router = useRouter();
  const [selectedId, setSelectedId] = useState<string>("");
  const [showQuestions, setShowQuestions] = useState<boolean>(false);

  const { openModal, closeModal, Modal } = useModal({ initialOpen: false });
  const [step, setStep] = useState(0);
  console.log(step);
  return (
    <>
      <div className="bg-whtie relative flex flex-col items-center pb-7 pt-14">
        <TutorialOverlay step={step} setStep={setStep} />
        <TutorialStep0 step={step} setStep={setStep} />
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
            <TutorialStep1 step={step} />
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
            <TutorialStep0Text step={step} />
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

        <div className="relative mb-11 flex w-full flex-col px-5 py-4">
          <TutorialStep1SpeechBubble step={step} />
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
