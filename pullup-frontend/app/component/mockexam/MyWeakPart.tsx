"use client";

import Text from "../ui/Text";
import { MockExamReportPropType } from "@/types/mockexam/mockexamReport";
import useSWR from "swr";
import { API, fetcher } from "@/lib/API";
import LocalStorage from "@/utils/LocalStorage";
import { useState, useEffect } from "react";

function MyWeakPart({ recentReportInfo }: MockExamReportPropType) {
  const memberID = LocalStorage.getItem("memberId") || "";

  const sections = recentReportInfo.problemTypeResults;

  let weakest = "수리";
  let maxIncorrect = -1;

  sections.forEach((item, index) => {
    const incorrectCount = item.totalProblems - item.correctProblems;

    if (incorrectCount > maxIncorrect) {
      maxIncorrect = incorrectCount;
      weakest = item.entry;
    }
  });

  return (
    <div className="my-5 flex w-full flex-col rounded-2xl bg-white p-6">
      <Text size="head-04">가장 취약한 파트는</Text>
      <Text size="head-02" className="inline">
        {weakest} 영역 이에요!
      </Text>
      <Text size="caption-02" color="text-gray01" className="mt-1">
        * 오답 개수 기준이에요.
      </Text>
      <div className="flex w-full justify-around">
        {sections.map((item) => {
          const height = `${(item.correctProblems / item.totalProblems) * 100}%`;
          return (
            <div
              key={item.entry}
              className="flex w-14 flex-col items-center justify-end gap-1"
            >
              {item.entry === weakest && (
                <div className="relative mb-1 mt-6 flex h-7 w-14 items-center justify-center rounded-[.4em] bg-[#3d4150] text-center text-[11px] text-white">
                  취약파트
                </div>
              )}
              <Text size="caption-02" color="text-gray01">
                {item.correctProblems}/{item.totalProblems}
              </Text>
              <div className="relative h-32 w-8 rounded-md bg-red02">
                <div
                  className="absolute bottom-0 left-0 z-20 w-8 rounded-md bg-red01"
                  style={{ height }}
                ></div>
              </div>
              <Text size="caption-02" color="text-gray01">
                {item.entry}
              </Text>
            </div>
          );
        })}
      </div>
    </div>
  );
}

export default MyWeakPart;
