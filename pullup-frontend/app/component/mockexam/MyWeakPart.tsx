"use client";

import Text from "../ui/Text";
import { MockExamWeakPartType } from "@/types/mockexam/mockexamReport";
import { ProblemTypeResult } from "@/types/mockexam/mockexamReport";
import useSWR from "swr";
import { API, fetcher } from "@/lib/API";
import LocalStorage from "@/utils/LocalStorage";
import { useState, useEffect } from "react";

function MyWeakPart() {
  const weakest = "수리영역";
  const memberID = LocalStorage.getItem("memberId") || "";
  const [weakestPart, setWeakestPart] = useState<ProblemTypeResult[]>([]);

  // 에러나 로딩 상태에 관계없이 기본값을 렌더링하도록 설정
  const displayWeakPart =
    weakestPart.length > 0
      ? weakestPart
      : [
          { entry: "??", correctProblems: 0, totalProblems: 0, weakest: false },
          { entry: "??", correctProblems: 0, totalProblems: 0, weakest: false },
          { entry: "??", correctProblems: 0, totalProblems: 0, weakest: false },
        ];

  // SWR을 통해 데이터 fetch
  // const { data: averageScore } = useSWR<MockExamWeakPartType>(
  //   `${API}/exams/mock-exam/recent/${memberID}`,
  //   fetcher
  // );

  const { data: averageScore } = useSWR<MockExamWeakPartType>(
    memberID ? `${API}/exams/mock-exam/recent/${memberID}` : null,
    fetcher,
  );

  useEffect(() => {
    if (averageScore) {
      // 각 영역의 오답 비율 계산
      const updatedWeakPart = averageScore.problemTypeResults.map((item) => ({
        ...item,
        weakest: false, // 초기화
        errorRate:
          (item.totalProblems - item.correctProblems) / item.totalProblems, // 오답 비율
      }));

      // 가장 오답 비율이 높은 부분 찾기
      const maxErrorRate = Math.max(
        ...updatedWeakPart.map((item) => item.errorRate || 0),
      );

      // 가장 취약한 부분에 true 설정
      const finalWeakPart = updatedWeakPart.map((item) => ({
        ...item,
        weakest: item.errorRate === maxErrorRate,
      }));

      setWeakestPart(finalWeakPart);
    }
  }, [averageScore]);

  if (averageScore) {
    console.log("모든", averageScore);
    console.log("모든", averageScore.problemTypeResults);
  } else {
    console.log("데이터를 불러오지 못했습니다.");
    return (
      <div className="my-5 flex w-full flex-col rounded-2xl bg-white p-6">
        <Text size="head-04">가장 취약한 파트는</Text>
        {displayWeakPart.length > 0 && (
          <Text size="head-02" className="inline">
            <span className="text-red01">
              {displayWeakPart.find((part) => part.weakest)?.entry || "??"}
            </span>{" "}
            이에요!
          </Text>
        )}
        <Text size="caption-02" color="text-gray01" className="mt-1">
          * 오답 개수 기준이에요.
        </Text>
        <div className="flex w-full justify-around">
          {displayWeakPart.map((item, index) => {
            const height = `${(item.correctProblems / item.totalProblems) * 100}%`;
            return (
              <div
                key={index}
                className="flex w-14 flex-col items-center justify-end gap-1"
              >
                {item.weakest && (
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
                  {item.entry || "??"}
                </Text>
              </div>
            );
          })}
        </div>
      </div>
    );
  }

  return (
    <div className="my-5 flex w-full flex-col rounded-2xl bg-white p-6">
      <Text size="head-04">가장 취약한 파트는</Text>
      {weakestPart.length > 0 && (
        <Text size="head-02" className="inline">
          <span className="text-red01">
            {" "}
            {weakestPart.find((part) => part.weakest)?.entry}
          </span>{" "}
          이에요!
        </Text>
      )}
      <Text size="caption-02" color="text-gray01" className="mt-1">
        * 오답 개수 기준이에요.
      </Text>
      <div className="flex w-full justify-around">
        {weakestPart.map((item) => {
          const height = `${(item.correctProblems / item.totalProblems) * 100}%`;
          return (
            <div
              key={item.entry}
              className="flex w-14 flex-col items-center justify-end gap-1"
            >
              {item.weakest && (
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
