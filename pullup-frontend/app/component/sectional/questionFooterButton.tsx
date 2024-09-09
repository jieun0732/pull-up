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

  const handleNextProblem = async () => {
    try {
      let chosenAnswer;

      do {
        const response = await fetch(`${API}/exams/answer`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            chosenAnswer: String(selectedId + 1),
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
      router.push(`/main/sectional/${subject}/${type}/${id}/solution`);
    } catch (error) {
      console.error("fetch operation:", error);
    }
  };
    

  if (params.id === "1") {
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
  if (params.id !== "1" && Number(params.id) !== problemsCnt) {
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

  if (Number(params.id) === problemsCnt) {
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
            onClick={async () => {
              try {
                let chosenAnswer;
          
                do {
                  const response = await fetch(`${API}/exams/answer`, {
                    method: "POST",
                    headers: {
                      "Content-Type": "application/json",
                    },
                    body: JSON.stringify({
                      chosenAnswer: String(selectedId + 1),
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
                router.push(`/main/sectional/${subject}/${type}/result`);
              } catch (error) {
                console.error("fetch operation:", error);
              }
            }}
          >
            채점하기 마지막
          </Button>
        </div>
      );
    }
  }
};

export default QuestionFooterButton;
