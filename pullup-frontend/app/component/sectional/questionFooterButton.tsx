"use client";

import Button from "../ui/Button";
import { useRouter } from "next/navigation";
import { API, fetcher } from "@/lib/API";
import { ProblemInfo } from "@/types/problemType";
import useSWR from "swr";
import { useEffect, useState } from "react";

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

  const { data, mutate } = useSWR(`${API}/exams/answer`, fetcher, {
    refreshInterval: (data) => {
      if (!data?.chosenAnswer) return 100;
      return 10000;
    },
  });
  useEffect(() => {
    console.log(data);
    if (data?.data?.chosenAnswer)
      router.push(`/main/sectional/${subject}/${category}/${id}/solution`);
  }, [data]);

  const handleNextProblem = async () => {
    try {
      mutate({
        data: {
          chosenAnswer: selectedId! + 1,
          id: problemInfo.id,
        },
      });
      // let chosenAnswer;
      // let selectedIdString;

      // if (selectedId !== null) {
      //   selectedIdString = String(selectedId + 1);
      // }

      // do {
      //   const response = await fetch(`${API}/exams/answer`, {
      //     method: "POST",
      //     headers: {
      //       "Content-Type": "application/json",
      //     },
      //     body: JSON.stringify({
      //       chosenAnswer: selectedIdString,
      //       id: problemInfo.id,
      //     }),
      //   });
      // const res = await (await mutate())?.json();
      // console.log(res);
      // const result = await response.json();
      // chosenAnswer = result.chosenAnswer;
      // router.push(`/main/sectional/${subject}/${category}/${id}/solution`);
      // } while (chosenAnswer === undefined);
      // router.push(`/main/sectional/${subject}/${category}/${id}/solution`);
    } catch (error) {
      // router.push(`/main/sectional/${subject}/${category}/${id}/solution`);
      // console.error("fetch operation:", error);
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
