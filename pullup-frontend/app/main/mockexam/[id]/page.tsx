"use client";

import { useParams, useRouter } from "next/navigation";
import CloseIcon from "@/assets/icon/CloseIcon";
import ToggleIcon from "@/assets/icon/ToggleIcon";
import Text from "@/component/ui/Text";
import Button from "@/component/ui/Button";
import { useState, useEffect } from "react";
import { dummyQ } from "../../../constants/dummyq";
import QuestionList from "@/component/mockexam/questionList";
import useModal from "@/hooks/useModal";
import formatNumber from "@/utils/formatNumber";
import { ConfirmModal } from "@/component/ui/ConfirmModal";
import { TutorialOverlay } from "@/component/mockexam/tutorial";
import ChoiceItem from "@/component/choiceItem";
import {
  TutorialStep0,
  TutorialStep0Text,
} from "@/component/mockexam/tutorial";
import {
  TutorialStep1,
  TutorialStep1SpeechBubble,
} from "@/component/mockexam/tutorial";
import useSWR from "swr";
import { API } from "@/lib/API";
import { mockexamQuestionType } from "@/types/mockexam/mockexamQuestion";

export default function Page() {
  const router = useRouter();
  const params = useParams<{ id: string }>();
  const [selectedId, setSelectedId] = useState<number | null>();
  const [showQuestions, setShowQuestions] = useState<boolean>(false);

  const { openModal, closeModal, Modal } = useModal({ initialOpen: false });
  const [step, setStep] = useState(0);

  const { data, error } = useSWR<mockexamQuestionType>(
    `${API}/exams/mock-exam/1`,
  );

  const handleNextQuestion = () => {
    router.push(`/main/mockexam/${Number(params.id) + 1}`);
  };

  console.log(error);
  if (error) return <div>/exams/problem/ error</div>;

  console.log(data);

  return (
    <>
      <div className="bg-whtie relative flex h-full flex-col items-center overflow-x-auto">
        <TutorialOverlay problemId={params.id} step={step} setStep={setStep} />
        <TutorialStep0 problemId={params.id} step={step} setStep={setStep} />
        <QuestionList
          showQuestions={showQuestions}
          setShowQuestions={setShowQuestions}
        />

        <div className="relative flex w-full flex-col overflow-x-auto px-5 pt-14">
          <div className="relative mb-8 w-full text-center">
            <CloseIcon onClick={openModal} />
            <span
              className={`relative rounded bg-white p-3 text-[17px] font-bold ${
                params.id === "1" && step == 1 ? "z-20" : ""
              }`}
            >
              30:00
            </span>
            <TutorialStep1 problemId={params.id} step={step} />
          </div>
          <span
            onClick={() => setShowQuestions(true)}
            className={`relative mb-4 flex w-fit items-center gap-1 rounded bg-white text-[17px] ${
              params.id === "1" && step == 0 ? "z-20 p-2" : ""
            }`}
          >
            문제 {formatNumber(params.id)} <ToggleIcon />
          </span>

          <Text size="body-03" className="relative mb-4">
            {data?.question}
          </Text>

          {data?.explanation && data.explanation.length > 0 && (
            <div className="relative mb-12 flex items-center justify-center rounded-md border border-solid border-gray02 py-5">
              <Text size="body-03">{data.explanation}</Text>
              <TutorialStep0Text step={step} />
            </div>
          )}
        </div>

        {data?.choices.map((item, idx) => (
          <ChoiceItem
            key={item}
            item={item}
            idx={idx}
            isSelected={selectedId === idx}
            selectedId={selectedId}
            setSelectedId={setSelectedId}
          />
        ))}

        <div className="absolute bottom-0 mb-11 flex w-full flex-col px-5 py-4">
          <TutorialStep1SpeechBubble step={step} problemId={params.id} />
          <button
            onClick={() => router.push("/main/mockexam/report")}
            className="ml-auto rounded-t-2xl rounded-bl-2xl bg-gray03 px-6 py-2 text-gray02 shadow-[2px_2px_20px_0px_rgba(0,0,0,0.16)]"
          >
            제출하기
          </button>
          {(() => {
            if (params.id === "1") {
              return (
                <Button
                  size="large"
                  color="active"
                  className="mt-4"
                  onClick={handleNextQuestion}
                >
                  다음 문제
                </Button>
              );
            } else if (params.id !== "1" && params.id !== "20") {
              return (
                <div className="mt-4 flex gap-2">
                  <Button size="medium" color="activeBorder">
                    이전 문제
                  </Button>
                  <Button
                    size="medium"
                    color="active"
                    onClick={handleNextQuestion}
                  >
                    다음 문제
                  </Button>
                </div>
              );
            } else if (params.id === "20") {
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
            onLeft={() => router.push("/main/mockexam")}
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