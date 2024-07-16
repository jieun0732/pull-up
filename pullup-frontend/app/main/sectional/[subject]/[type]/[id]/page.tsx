"use client";

import { useState } from "react";
import { dummyQ } from "@/constants/dummyq";
import ChoiceItem from "@/component/choiceItem";
import QuestionFooterButton from "@/component/sectional/questionFooterButton";

export default function Page({
  params,
}: {
  params: {
    subject: string;
    type: string;
    id: string;
  };
}) {
  const [selectedId, setSelectedId] = useState<string>("");

  return (
    <>
      {dummyQ.choice.map((item) => (
        <ChoiceItem
          key={item.id}
          item={item}
          isSelected={selectedId === String(item.id)}
          selectedId={selectedId}
          setSelectedId={setSelectedId}
        />
      ))}

      <div className="fixed bottom-0 mb-11 flex w-full flex-col px-5 py-4">
        <QuestionFooterButton
          questionId={dummyQ.id}
          selectedId={selectedId}
          params={params}
        />
      </div>
    </>
  );
}
