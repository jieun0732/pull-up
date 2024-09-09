"use client";

import { useState } from "react";
import { dummyQ } from "@/constants/dummyq";
import ChoiceItem from "@/component/choiceItem";
import QuestionFooterButton from "@/component/sectional/questionFooterButton";
import useSWR from "swr";
import { API, fetcher } from "@/lib/API";
import { ProblemInfo, Problem } from "@/types/problemType";
import { entryMap, categoryMap } from "@/constants/constants";

export default function Page({
  params,
}: {
  params: {
    subject: string;
    type: string;
    id: string;
  };
}) {
  const memberID = 1;
  const [selectedId, setSelectedId] = useState<number>(-1);
  const entry = entryMap[params.subject];
  const category = categoryMap[params.type];

  const queryString = new URLSearchParams({
    memberId: memberID.toString(),
    entry,
    category,
  }).toString();

  const { data, error } = useSWR<ProblemInfo>(
    `${API}/exams/next?${queryString}`,
    fetcher,
  );

  const { data: problems } = useSWR<ProblemInfo[]>(
    `${API}/exams/problems?${queryString}`,
    fetcher,
  );

  if (!data || !problems) return;

  const nowProblem: Problem = problems[Number(params.id) - 1].problem;

  return (
    <>
      {nowProblem.choices.map((item, idx) => (
        <ChoiceItem
          key={item}
          choice={item}
          idx={idx}
          isSelected={selectedId === idx}
          selectedId={selectedId}
          setSelectedId={setSelectedId}
        />
      ))}

      <div className="fixed bottom-0 mb-11 flex w-full flex-col px-5 py-4">
        <QuestionFooterButton
          problemInfo={data}
          selectedId={selectedId}
          params={params}
          problemsCnt={problems.length}
        />
      </div>
    </>
  );
}
