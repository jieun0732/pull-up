"use client";

import { useRouter } from "next/navigation";
import CloseIcon from "@/assets/icon/closeIcon";
import ToggleIcon from "@/assets/icon/toggleIcon";
import Button from "@/component/ui/Button";
import { useState } from "react";
import { dummyQ } from "./dummyq";
import QuestionList from "@/component/mocktest/questionList";
import useModal from "@/hooks/useModal";

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

  const { openModal, closeModal, Modal } = useModal({ initialOpen: false });

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
        <button
          onClick={openModal}
          className="ml-auto rounded-t-2xl rounded-bl-2xl bg-gray03 px-6 py-2 text-gray02 shadow-[2px_2px_20px_0px_rgba(0,0,0,0.16)]"
        >
          제출하기
        </button>
        {(() => {
          if (dummyQ.id === 1) {
            return (
              <Button size="large" color="active" customstyle="mt-4">
                다음 문제
              </Button>
            );
          } else if (dummyQ.id > 1 && dummyQ.id < 20) {
            return (
              <div className="mt-4 flex gap-2">
                <Button size="medium" color="activeBorder">
                  이전 문제
                </Button>
                <Button size="medium" color="active">
                  다음 문제
                </Button>
              </div>
            );
          } else if (dummyQ.id === 20) {
            return (
              <Button size="large" color="activeBorder" customstyle="mt-4">
                이전 문제
              </Button>
            );
          }
        })()}
      </div>
      <Modal>
        <h2>Modal Content</h2>
        <p>This is the content of the modal.</p>
        <button onClick={closeModal}>Close</button>
      </Modal>
    </div>
  );
}
