"use client";

import Button from "../ui/Button";
import { useRouter } from "next/navigation";
import { API } from "@/lib/API";
import { ProblemInfo } from "@/types/problemType";

interface QuestionProps {
  problemInfo: ProblemInfo;
  selectedId: number;
  params: {
    subject: string;
    type: string;
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
  const { subject, type, id } = params;

  console.log(problemInfo.id, problemsCnt);

  const handleNextProblem = async () => {
    try {
      const response = await fetch(`${API}/exams/answer`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          chosenAnswer: String(selectedId + 1),
          id: problemInfo.problem.id,
        }),
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const result = await response.json(); // 응답 JSON 파싱
      console.log("Response:", result);

      // 성공적으로 응답을 받으면 라우팅
      router.push(`/main/sectional/${subject}/${type}/${id}/solution`);
    } catch (error) {
      console.error("There was a problem with the fetch operation:", error);
    }
  };

  // console.log(problemInfo);
  // console.log(selectedId);
  if (problemInfo.id === 1) {
    if (selectedId !== -1) {
      return (
        <Button
          size="large"
          color="active"
          className="mt-4"
          onClick={handleNextProblem}
        >
          채점하기
        </Button>
      );
    }
    if (selectedId === -1) {
      return (
        <Button size="large" color="nonactive" className="mt-4">
          채점하기
        </Button>
      );
    }
  }
  if (problemInfo.id != 1 && problemInfo.id != problemsCnt) {
    if (selectedId === -1) {
      return (
        <div className="mt-4 flex gap-2">
          <Button
            size="medium"
            color="activeBorder"
            onClick={() =>
              router.push(
                `/main/sectional/${subject}/${type}/${Number(id) - 1}/solution`,
              )
            }
          >
            이전 문제 중간
          </Button>
          <Button size="medium" color="nonactive">
            채점하기
          </Button>
        </div>
      );
    }
    if (selectedId !== -1) {
      return (
        <div className="mt-4 flex gap-2">
          <Button
            size="medium"
            color="activeBorder"
            onClick={() =>
              router.push(
                `/main/sectional/${subject}/${type}/${Number(id) - 1}/solution`,
              )
            }
          >
            이전 문제 중간
          </Button>
          <Button size="medium" color="active" onClick={handleNextProblem}>
            채점하기 중간
          </Button>
        </div>
      );
    }
  }

  if (problemInfo.id === problemsCnt) {
    if (selectedId === -1) {
      return (
        <div className="mt-4 flex gap-2">
          <Button
            size="medium"
            color="activeBorder"
            onClick={() =>
              router.push(
                `/main/sectional/${subject}/${type}/${Number(id) - 1}/solution`,
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
    if (selectedId !== -1) {
      return (
        <div className="mt-4 flex gap-2">
          <Button
            size="medium"
            color="activeBorder"
            onClick={() =>
              router.push(
                `/main/sectional/${subject}/${type}/${Number(id) - 1}/solution`,
              )
            }
          >
            이전 문제
          </Button>
          <Button
            size="medium"
            color="active"
            onClick={() =>
              router.push(`/main/sectional/${subject}/${type}/result`)
            }
          >
            채점하기 마지막
          </Button>
        </div>
      );
    }
  }
};

export default QuestionFooterButton;
