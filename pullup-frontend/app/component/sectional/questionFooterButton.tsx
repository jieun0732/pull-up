"use client";

import Button from "../ui/Button";
import { useRouter } from "next/navigation";
import { postSubmitAnswer } from "@/lib/sectionalAPI";
import { SectionalNextResponseType, Explanation } from "@/types/sectionalType";
import { Dispatch } from "react";
import { SetStateAction } from "react";

interface QuestionProps {
  data: SectionalNextResponseType;
  selectedId: number;
  category: "EVENLY" | "BY_PROBLEM_TYPE";
  setSolution: Dispatch<SetStateAction<Explanation>>;
  setIsSubmitted: Dispatch<SetStateAction<boolean>>;
}

const QuestionFooterButton: React.FC<QuestionProps> = ({
  data,
  selectedId,
  category,
  setSolution,
  setIsSubmitted,
}: QuestionProps) => {
  const router = useRouter();

  const handleSubmit = async () => {
    const response = await postSubmitAnswer(
      data.examId,
      data.problemNumber,
      selectedId + 1,
    );

    const explanation = await response;
    if (explanation) {
      setSolution(explanation);
      router.push(`/main/sectional/${data.entry}/${category}/result`);
    }
  };

  const handleNextProblem = async () => {
    const response = await postSubmitAnswer(
      data.examId,
      data.problemNumber,
      selectedId + 1,
    );
    const explanation = await response;
    if (explanation) {
      setSolution(explanation);
      setIsSubmitted(true);
    }
  };

  const ButtonGroupComponent = ({
    onClickPrev,
    onClickNext,
    isActive,
  }: {
    onClickPrev: () => void;
    onClickNext: () => void;
    isActive: boolean;
  }) => (
    <div className="mt-4 flex gap-2">
      <Button size="medium" color="activeBorder" onClick={onClickPrev}>
        이전 문제
      </Button>
      <Button
        size="medium"
        color={isActive ? "active" : "nonactive"}
        onClick={onClickNext}
      >
        채점하기
      </Button>
    </div>
  );

  const handleStateBranching = () => {
    switch (true) {
      // 1) problemNumber가 1인 경우
      case data.problemNumber === 1:
        // 1-1) selectedId가 -1인 경우
        if (selectedId === -1) {
          return (
            <Button size="large" color="nonactive">
              채점하기
            </Button>
          );
        }
        // 1-2) selectedId가 -1이 아닌 경우
        return (
          <Button
            size="large"
            color="active"
            onClick={
              data.totalProblemCount === 1 ? handleSubmit : handleNextProblem
            }
          >
            채점하기
          </Button>
        );

      // 2) data.totalProblemCount가 problemNumber와 같은 경우
      case data.totalProblemCount === data.problemNumber:
        // 2-1) selectedId가 -1인 경우
        if (selectedId === -1) {
          return (
            <Button size="large" color="nonactive">
              전체 결과 보러가기
            </Button>
          );
        }
        // 1-2) selectedId가 -1이 아닌 경우
        return (
          <Button size="large" color="active" onClick={handleSubmit}>
            전체 결과 보러가기
          </Button>
        );

      // 3) 그 외의 경우
      default:
        // 3-1) selectedId가 -1인 경우
        if (selectedId === -1) {
          return (
            <ButtonGroupComponent
              onClickPrev={() =>
                router.push(
                  `/main/sectional/${data.entry}/${category}/${data.problemNumber - 1}`,
                )
              }
              onClickNext={() => {}}
              isActive={false}
            />
          );
        }
        // 3-2) selectedId가 -1이 아닌 경우
        return (
          <ButtonGroupComponent
            onClickPrev={() =>
              router.push(
                `/main/sectional/${data.entry}/${category}/${data.problemNumber - 1}`,
              )
            }
            onClickNext={handleNextProblem}
            isActive={true}
          />
        );
    }
  };

  return handleStateBranching();
};

export default QuestionFooterButton;
