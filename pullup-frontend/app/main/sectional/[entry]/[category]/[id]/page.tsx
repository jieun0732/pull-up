"use client";

import { useRouter, useSearchParams, useParams } from "next/navigation";
import { CloseIcon } from "@/assets/icon/Icons";
import Text from "@/component/ui/Text";
import Button from "@/component/ui/Button";
import useModal from "@/hooks/useModal";
import formatNumber from "@/utils/formatNumber";
import { ConfirmModal } from "@/component/ui/ConfirmModal";
import ThinProgressBar from "@/component/sectional/thinProgressbar";
import { API, fetcher } from "@/lib/API";
import useSWR from "swr";
import { FormatQuestion } from "@/utils/FormatQuestion";
import { Entry } from "@/types/problemType";
import { SectionalNextResponseType, Explanation } from "@/types/sectionalType";
import Spinner from "@/component/ui/Spinner";
import ChoiceItem1 from "@/component/sectional/ChoiceItem1";
import QuestionFooterButton from "@/component/sectional/questionFooterButton";
import useProblemStore from "@/stores/useProblemStore";
import SolutionComponent from "./SolutionComponent";
import { useEffect, useState } from "react";

const initialSolution: Explanation = {
  isCorrect: false,
  submitAnswer: 0,
  correctAnswer: 0,
  correctRate: 0,
  incorrectRate: 0,
  explanation: "",
};

export default function Page() {
  const router = useRouter();
  const params = useParams<{
    entry: Entry;
    category: "EVENLY" | "BY_PROBLEM_TYPE";
    id: string;
  }>();
  const { examId } = useProblemStore();
  const searchParams = useSearchParams();
  const [isSubmitted, setIsSubmitted] = useState<boolean>(
    searchParams.get("chosenAnswer") ? true : false,
  );
  const [solution, setSolution] = useState<Explanation>(initialSolution);
  const [selectedId, setSelectedId] = useState<number>(
    searchParams.get("chosenAnswer")
      ? Number(searchParams.get("chosenAnswer")) - 1
      : -1,
  );

  const { openModal, closeModal, Modal } = useModal({ initialOpen: false });

  const { data, error: solvedError } = useSWR<SectionalNextResponseType>(
    `${API}/exams/next/${examId}?problemNumber=${params.id}`,
    fetcher,
  );

  useEffect(() => {
    if (data && data.explanation) {
      setIsSubmitted(true);
      setSolution(data.explanation);
    }
  }, [data]);

  return (
    <>
      {data ? (
        <div className="bg-whtie relative flex flex-col items-center pb-7 pt-20">
          <div className="relative flex w-full flex-col">
            <div className="relative mx-5 w-full">
              <CloseIcon onClick={openModal} />
              <span className="ml-9 text-[13px] font-normal text-gray01">
                {data.leftProblemCount}문제 남았어요!
              </span>
            </div>

            <ThinProgressBar
              now={data.totalProblemCount - data.leftProblemCount}
              total={data.totalProblemCount}
            />

            <div className="mt-11 flex w-full items-center justify-between px-5">
              <Text size="head-03" className="mb-4">
                문제 {formatNumber(params.id)}
              </Text>
              <Button size="small" color="nonactive">
                {data.problemType}
              </Button>
            </div>

            <Text size="body-03" className="relative mb-4 px-5">
              {data.question}
            </Text>

            {data.example && (
              <div className="relative mx-5 mb-7 flex items-center justify-center rounded-md border border-solid border-gray02 py-5">
                <Text size="body-03">{FormatQuestion(data.example)}</Text>
              </div>
            )}
          </div>

          {isSubmitted ? (
            <>
              <SolutionComponent
                data={data}
                solution={solution}
                openModal={openModal}
                category={params.category}
              />
            </>
          ) : (
            <>
              {data.choices.map((item, idx) => (
                <ChoiceItem1
                  key={item}
                  choice={item}
                  idx={idx}
                  isSelected={selectedId === idx}
                  selectedId={selectedId}
                  setSelectedId={setSelectedId}
                />
              ))}
              <div className="fixed bottom-10 w-full px-5">
                <QuestionFooterButton
                  data={data}
                  selectedId={selectedId}
                  category={params.category}
                  setSolution={setSolution}
                  setIsSubmitted={setIsSubmitted}
                />
              </div>
            </>
          )}

          <Modal>
            <ConfirmModal
              onLeft={() => router.push(`/main/sectional/${params.entry}`)}
              onRight={closeModal}
              title="정말로 학습을 종료하실 건가요?"
              description="나가면 현재까지 푼 문제만 저장돼요!"
              left="종료할래요"
              right="계속 풀고 싶어요."
            />
          </Modal>
        </div>
      ) : (
        <Spinner />
      )}
    </>
  );
}
