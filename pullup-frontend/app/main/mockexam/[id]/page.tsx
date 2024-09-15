"use client";

import { useParams, useRouter } from "next/navigation";
import CloseIcon from "@/assets/icon/CloseIcon";
import ToggleIcon from "@/assets/icon/ToggleIcon";
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
import { API } from "@/lib/API";
import { MockExamProblemType } from "@/types/mockexam/mockexamQuestion";
import useTimer from "@/hooks/useTimer";

export default function Page() {
  const router = useRouter();
  const params = useParams<{ id: string }>();
  const [selectedId, setSelectedId] = useState<number>(-1);
  const [showQuestions, setShowQuestions] = useState<boolean>(false);
  const { openModal, closeModal, Modal } = useModal({ initialOpen: false });
  const [step, setStep] = useState(4);
  
  
  const timeLeft = useTimer(params.id, 30); 
  const examId = localStorage.getItem('examId')


  const { data : nowProblem , error } = useSWR<MockExamProblemType>(
    `${API}/exams/mock-exam/problem?examInformationId=${examId}&problemNumber=${params.id}`,
  );

  useEffect(() => {
    const checkAccess = async () => {
      if (params.id !== '1') return
      if (params.id === '1') { 
        try {
          const response = await fetch(`${API}/members/${localStorage.getItem('memberId')}/access-check`, {
            method: 'PUT',
            headers: {
              'Content-Type': 'application/json',
            },
          });

          if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
          }

          const result = await response.json();
          if (result.data.accessCheck) {
            setStep(4); 
          } else {
            setStep(0); 
          }
        } catch (error) {
          console.error('Error fetching access check:', error);
          setStep(0); 
        }
      }
    };
    checkAccess();
  }, [params.id]); // id가 변경될 때마다 useEffect가 실행됩니다.



  const handleNextQuestion = async () => {
    if (selectedId !== -1) {
      try {
        const response = await fetch(`${API}/exams/mock-exam/answer`, {
          method: 'POST',
          headers: {
            'Accept': '*/*',
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            examInformationId: Number(localStorage.getItem('examId')),
            problemNumber: Number(nowProblem?.problemNumber),
            chosenAnswer: String(selectedId + 1),
          })
        });

        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }

        const result = await response.json();
        console.log(result)
        localStorage.setItem('createdDate', result.examInformation.createdDate)
        console.log(result.examInformation.createdDate)
      } catch (error) {
        console.error(error);
      } finally {
        router.push(`/main/mockexam/${Number(params.id) + 1}`);
      }

    } else {
      router.push(`/main/mockexam/${Number(params.id) + 1}`);

    }
  };

  const handleExamResult = async () => {
    console.log("handleExamResult")
    try {
      const response = await fetch(`${API}/exams/mock-exam/complete`, {
        method: 'POST',
      });
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      const result = await response.json();
      router.push("/main/mockexam/report")

    } catch (error) {
      console.error('Error fetching access check:', error);
    }

  }
  if (!nowProblem) return;
  
  return (
    <>
      <div className="bg-whtie relative flex h-full flex-col items-center overflow-x-auto">
        <TutorialOverlay problemId={params.id} step={step} setStep={setStep} />
        <TutorialStep0 problemId={params.id} step={step} setStep={setStep} />
        <QuestionList
          showQuestions={showQuestions}
          setShowQuestions={setShowQuestions}
          handleExamResult={handleExamResult}
        />

        <div className="relative flex w-full flex-col overflow-x-auto px-5 pt-14">
          <div className="relative mb-8 w-full text-center">
            <CloseIcon onClick={openModal} />
            <span
              className={`relative rounded bg-white p-3 text-[17px] font-bold ${
                params.id === "1" && step == 1 ? "z-20" : ""
              }`}
            >
              {timeLeft}
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
          </Text>

          {nowProblem.explanation && nowProblem.explanation.length > 0 && (
            <div className="relative mb-12 flex items-center justify-center rounded-md border border-solid border-gray02 py-5">
              <Text size="body-03">{nowProblem.explanation}</Text>
              <TutorialStep0Text step={step} />
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
            />
        ))}

        <div className="absolute bottom-0 mb-11 flex w-full flex-col px-5 py-4">
          <TutorialStep1SpeechBubble step={step} problemId={params.id} />
          <button
            onClick={handleExamResult}
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
                  <Button size="medium" color="activeBorder"
                onClick={(() => router.push(`/main/mockexam/${Number(params.id) - 1}`))}
                >
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
                <Button size="large" color="activeBorder" className="mt-4"
                onClick={(() => router.push(`/main/mockexam/${Number(params.id) - 1}`))}
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
  );
}
