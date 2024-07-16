"use client";

import Button from "../ui/Button";
import { useRouter } from "next/navigation";

interface QuestionProps {
  questionId: number;
  selectedId: string;
  params: {
    subject: string;
    type: string;
    id: string;
  };
}

const QuestionFooterButton: React.FC<QuestionProps> = ({
  questionId,
  selectedId,
  params,
}) => {
  const router = useRouter();
  const { subject, type, id } = params;

  if (id === "1") {
    if (selectedId !== "") {
      return (
        <Button
          size="large"
          color="active"
          className="mt-4"
          onClick={() =>
            router.push(`/main/sectional/${subject}/${type}/${id}/solution`)
          }
        >
          채점하기
        </Button>
      );
    }
    if (selectedId === "") {
      return (
        <Button size="large" color="nonactive" className="mt-4">
          채점하기
        </Button>
      );
    }
  }
  if (id != "1" && id != "20") {
    if (selectedId === "") {
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
            color="nonactive"
            onClick={() =>
              router.push(`/main/sectional/${subject}/${type}/${id}/solution`)
            }
          >
            채점하기
          </Button>
        </div>
      );
    }
    if (selectedId !== "") {
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
            채점하기
          </Button>
        </div>
      );
    }
  }

  if (id == "20") {
    if (selectedId === "") {
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
    if (selectedId !== "") {
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
            채점하기
          </Button>
        </div>
      );
    }
  }
};

export default QuestionFooterButton;
