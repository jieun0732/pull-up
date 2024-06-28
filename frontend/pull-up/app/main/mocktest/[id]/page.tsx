"use client";

import { useRouter } from "next/navigation";
import CloseIcon from "@/assets/icon/closeIcon";
import ToggleIcon from "@/assets/icon/toggleIcon";
import { useState } from "react";
import { dummyQ } from "./dummyq";
import QuestionList from "@/component/mocktest/questionList";

function formatNumber(num: number) {
  const numToString = String(num);
  if (numToString.length === 1) {
    return `0${numToString}`;
  }
  return String(num);
}

export default function Page() {
  const router = useRouter();
  const [selectedId, setSelectedId] = useState<number | null>(null);
  const [showQuestions, setShowQuestions] = useState<boolean>(false);

  return (
    <div className="bg-whtie relative flex flex-col pb-7 pt-14">
      <QuestionList
        showQuestions={showQuestions}
        setShowQuestions={setShowQuestions}
      />

      <div className="px-5">
        <p className="relative mb-6 w-full text-center text-[17px] font-bold">
          <CloseIcon onClick={() => router.push("/main/mocktest")} />
          30:00
        </p>
        <button
          onClick={() => setShowQuestions(true)}
          className="mb-4 flex items-center gap-1"
        >
          문제 {formatNumber(dummyQ.id)} <ToggleIcon />
        </button>

        <p className="mb-4 text-[15px] font-medium">{dummyQ.question}</p>
        <div className="mb-12 flex items-center justify-center rounded-md border border-solid border-gray02 py-5">
          <p className="text-[15px] font-medium">{dummyQ.questionD}</p>
        </div>
      </div>

      {dummyQ.choice.map((item) => {
        const isSelected = selectedId === item.id;
        return (
          <div
            key={item.id}
            className={`flex w-full items-center gap-4 px-5 py-4 ${
              isSelected ? "bg-[#ebebeb]" : "bg-white"
            }`}
            onClick={() => setSelectedId(item.id)}
          >
            <div
              className={`flex h-10 w-10 items-center justify-center rounded-full border border-solid border-black01 ${
                isSelected ? "bg-black01 text-white" : "bg-white text-black01"
              }`}
            >
              {item.id}
            </div>
            <div>{item.name}</div>
          </div>
        );
      })}

      <div className="mb-11 flex w-full flex-col px-5 py-4">
        <button className="ml-auto rounded-t-2xl rounded-bl-2xl bg-gray03 px-6 py-2 text-gray02 shadow-[2px_2px_20px_0px_rgba(0,0,0,0.16)]">
          제출하기
        </button>
        {dummyQ.id > 1 ? (
          <div className="mt-4 flex gap-2">
            <button className="w-1/2 rounded-lg border-2 border-solid border-blue01 bg-white py-3 text-center text-base font-semibold text-blue01">
              이전 문제
            </button>
            <button className="w-1/2 rounded-lg border-2 border-solid border-blue01 bg-blue01 py-3 text-center text-base font-semibold text-white">
              다음 문제
            </button>
          </div>
        ) : (
          <button className="mt-4 w-full rounded-lg border-2 border-solid border-blue01 bg-blue01 py-3 text-center text-base font-semibold text-white">
            다음 문제
          </button>
        )}
      </div>
    </div>
  );
}
