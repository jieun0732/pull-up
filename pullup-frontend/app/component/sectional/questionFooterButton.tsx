"use client";

import Button from "../ui/Button";
import { useRouter } from "next/navigation";
import { API } from "@/lib/API";
import { ProblemInfo } from "@/types/problemType";

interface QuestionProps {
  problemInfo: ProblemInfo;
  selectedId: number | null;
  params: {
    subject: string;
    category: string;
    id: string;
  };
  problemsCnt: number;
}

const QuestionFooterButton: React.FC<QuestionProps> = ({
  problemInfo,
  selectedId,
  params,
  problemsCnt,
}) => {
  const router = useRouter();
  const { subject, category, id } = params;

  const handleNextProblem = async () => {
    try {
      if (selectedId === null) return;

      const selectedIdString = String(selectedId + 1);
      let chosenAnswer;

      while (true) {
        const response = await fetch(`${API}/exams/answer`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            chosenAnswer: selectedIdString,
            id: problemInfo.id,
          }),
        });

        if (!response.ok) {
          throw new Error("Network response was not ok");
        }

        const result = await response.json();
        chosenAnswer = result.chosenAnswer;

        if (chosenAnswer) break;
      }
      router.push(
        `/main/sectional/${subject}/${category}/${id}/solution?chosenAnswer=${chosenAnswer}`,
      );
    } catch (error) {
      console.error("Fetch operation error:", error);
      // 에러가 발생해도 router.push를 호출
      // router.push(`/main/sectional/${subject}/${category}/${id}/solution`);
    }
  };

  const handleSubmit = async () => {
    try {
      let chosenAnswer;
      let selectedIdString;

      if (selectedId !== null) {
        selectedIdString = String(selectedId + 1);
      }

      do {
        const response = await fetch(`${API}/exams/answer`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            chosenAnswer: selectedIdString,
            id: problemInfo.id,
          }),
        });

        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }

        const result = await response.json();
        chosenAnswer = result.chosenAnswer;
        console.log("Response:", result);
      } while (chosenAnswer === undefined);

      router.push(`/main/sectional/${subject}/${category}/result`);
    } catch (error) {
      console.error("fetch operation:", error);
    }
  };

  if (params.id === "1") {
    if (selectedId !== null) {
      return (
        <Button
          size="large"
          color="active"
          className="mt-4"
          onClick={problemsCnt === 1 ? handleSubmit : handleNextProblem}
        >
          채점하기
        </Button>
      );
    }
    if (selectedId === null) {
      return (
        <Button size="large" color="nonactive" className="mt-4">
          채점하기
        </Button>
      );
    }
  }
  if (params.id !== "1" && Number(params.id) !== problemsCnt) {
    if (selectedId === null) {
      return (
        <div className="mt-4 flex gap-2">
          <Button
            size="medium"
            color="activeBorder"
            onClick={() =>
              router.push(
                `/main/sectional/${subject}/${category}/${Number(id) - 1}/solution`,
              )
            }
          >
            이전 문제
          </Button>
          <Button size="medium" color="nonactive">
            채점하기
          </Button>
        </div>
      );
    }
    if (selectedId !== null) {
      return (
        <div className="mt-4 flex gap-2">
          <Button
            size="medium"
            color="activeBorder"
            onClick={() =>
              router.push(
                `/main/sectional/${subject}/${category}/${Number(id) - 1}/solution`,
              )
            }
          >
            이전 문제
          </Button>
          <Button size="medium" color="active" onClick={handleNextProblem}>
            채점하기
          </Button>
        </div>
      );
    }
  }

  if (Number(params.id) === problemsCnt) {
    if (selectedId === null) {
      return (
        <div className="mt-4 flex gap-2">
          <Button
            size="medium"
            color="activeBorder"
            onClick={() =>
              router.push(
                `/main/sectional/${subject}/${category}/${Number(id) - 1}/solution`,
              )
            }
          >
            이전 문제
          </Button>
          <Button size="medium" color="nonactive">
            채점하기
          </Button>
        </div>
      );
    }
    if (selectedId !== null) {
      return (
        <div className="mt-4 flex gap-2">
          <Button
            size="medium"
            color="activeBorder"
            onClick={() =>
              router.push(
                `/main/sectional/${subject}/${category}/${Number(id) - 1}/solution`,
              )
            }
          >
            이전 문제
          </Button>
          <Button size="medium" color="active" onClick={handleSubmit}>
            채점하기
          </Button>
        </div>
      );
    }
  }
};

export default QuestionFooterButton;
