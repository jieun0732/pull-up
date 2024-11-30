"use client";

import { useParams, useRouter } from "next/navigation";
import { CloseIcon, ToggleIcon } from "@/assets/icon/Icons";
import Text from "@/component/ui/Text";
import Button from "@/component/ui/Button";
import { useState, useEffect } from "react";
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
import { API, fetcher } from "@/lib/API";
import useTimer from "@/hooks/useTimer";
import LocalStorage from "@/utils/LocalStorage";
import Spinner from "@/component/ui/Spinner";
import SubmitButton from "@/component/mockexam/tutorial/SubmitButton";
import useExamStore from "@/stores/useExamStore";
import { MockExamResponseType } from "@/types/mockexam/mockexamQuestion";

export default function Page() {
  const router = useRouter();
  const params = useParams<{ id: string }>();
  const [selectedId, setSelectedId] = useState<number | null>(null);
  const [showQuestions, setShowQuestions] = useState<boolean>(false);
  const { openModal, closeModal, Modal } = useModal({ initialOpen: false });
  const [step, setStep] = useState(4);
  const timeLeft = useTimer(params.id, 20, step);
  const {
    examId,
    isFinished,
    selectedAnswers,
    tutorialFinished,
    setTutorialFinished,
  } = useExamStore();

  const { data: nowProblem, error } = useSWR<MockExamResponseType>(
    `${API}/exams/next/${examId}?problemNumber=${params.id}`,
  );

  useEffect(() => {
    if (params.id === "1") {
      if (!tutorialFinished) {
        setStep(0);
      }
    }
  }, []);

  // SELECTED ANSWER
  useEffect(() => {
    const getSelectedId = selectedAnswers[Number(params.id) - 1].submitAnswer;
    if (getSelectedId !== null) {
      setSelectedId(getSelectedId - 1);
    } else {
      setSelectedId(null);
    }
  }, [params.id]);

  const handleExamResult = async () => {
    try {
      const response = await fetch(`${API}/exams/mock-exam/grade`, {
        method: "POST",
        // credentials: "include",
        headers: {
          Accept: "*/*",
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          examId: examId,
          answerSheets: selectedAnswers,
        }),
      });
      console.log({
        examId: examId,
        answerSheets: selectedAnswers,
      });
      console.log(response);
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      const result = await response.json();
      router.push("/main/mockexam/report");
    } catch (error) {
      console.error(error);
    } finally {
      // router.push("/main/mockexam/report");
    }
  };

  return (
    <>
      {nowProblem ? (
        <>
          <div className="bg-whtie relative flex h-full flex-col items-center overflow-x-auto">
            <TutorialOverlay
              problemId={params.id}
              step={step}
              setStep={setStep}
              setTutorialFinished={setTutorialFinished}
            />
            <TutorialStep0
              problemId={params.id}
              step={step}
              setStep={setStep}
            />
            <QuestionList
              showQuestions={showQuestions}
              setShowQuestions={setShowQuestions}
              handleExamResult={handleExamResult}
              isFinished={isFinished}
            />

            <div className="relative flex w-full flex-col overflow-x-auto px-5 pt-20">
              <div className="relative mb-8 w-full text-center">
                <CloseIcon onClick={openModal} />
                <span
                  className={`relative rounded bg-white p-3 text-[17px] font-bold ${
                    params.id === "1" && step == 1 ? "z-20" : ""
                  }`}
                >
                  {params.id === "1" && step == 1 ? "20:00" : timeLeft}
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
                {nowProblem.question}
                <TutorialStep0Text step={step} />
              </Text>

              {nowProblem.example && nowProblem.example.length > 0 && (
                <div className="relative mb-12 flex items-center justify-center rounded-md border border-solid border-gray02 px-5 py-5">
                  <Text size="body-03">{nowProblem.example}</Text>
                </div>
              )}
            </div>

            {nowProblem.choices.map((item, idx) => (
              <ChoiceItem
                key={item}
                choice={item}
                idx={idx}
                isSelected={selectedId === idx}
                selectedId={selectedId}
                setSelectedId={setSelectedId}
                problemNumber={Number(params.id)}
              />
            ))}

            <div className="absolute bottom-0 mb-11 flex w-full flex-col px-5 py-4">
              <TutorialStep1SpeechBubble step={step} problemId={params.id} />

              <SubmitButton
                handleExamResult={handleExamResult}
                isFinished={isFinished}
              />
              {(() => {
                if (params.id === "1") {
                  return (
                    <Button
                      size="large"
                      color="active"
                      className="mt-4"
                      onClick={() =>
                        router.push(`/main/mockexam/${Number(params.id) + 1}`)
                      }
                    >
                      다음 문제
                    </Button>
                  );
                } else if (params.id !== "1" && params.id !== "20") {
                  return (
                    <div className="mt-4 flex gap-2">
                      <Button
                        size="medium"
                        color="activeBorder"
                        onClick={() =>
                          router.push(`/main/mockexam/${Number(params.id) - 1}`)
                        }
                      >
                        이전 문제
                      </Button>
                      <Button
                        size="medium"
                        color="active"
                        onClick={() =>
                          router.push(`/main/mockexam/${Number(params.id) + 1}`)
                        }
                      >
                        다음 문제
                      </Button>
                    </div>
                  );
                } else if (params.id === "20") {
                  return (
                    <Button
                      size="large"
                      color="activeBorder"
                      className="mt-4"
                      onClick={() =>
                        router.push(`/main/mockexam/${Number(params.id) - 1}`)
                      }
                    >
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
      ) : (
        <Spinner />
      )}
    </>
  );
}
