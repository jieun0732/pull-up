"use client";

import Text from "../ui/Text";
import { MockExamReportType } from "@/types/mockexam/mockexamReport";
import { entryMap } from "@/constants/constants";

type MyWeakPartProps = {
  data: MockExamReportType;
};

function MyWeakPart({ data }: MyWeakPartProps) {
  const sections = ["Language", "Reasoning", "Math"] as const;

  return (
    <div className="my-5 flex w-full flex-col rounded-2xl bg-white p-6">
      <Text size="head-04">가장 취약한 파트는</Text>
      <Text size="head-02" className="inline">
        {data.vulnerableEntryInfo.vulnerableEntry.map((item) => {
          return item + " 영역, ";
        })}
        이에요!
      </Text>
      <Text size="caption-02" color="text-gray01" className="mt-1">
        * 오답 개수 기준이에요.
      </Text>
      <div className="flex w-full justify-around">
        {sections.map((item) => {
          const incorrectCount =
            data.vulnerableEntryInfo[`incorrect${item}Count`];
          const totalCount = data.vulnerableEntryInfo[`total${item}Count`];
          // 0으로 나누는 오류 방지
          const height =
            totalCount > 0 ? `${(incorrectCount / totalCount) * 100}%` : "0%";
          return (
            <div
              key={item}
              className="flex w-14 flex-col items-center justify-end gap-1"
            >
              {data.vulnerableEntryInfo.vulnerableEntry.includes(item) && (
                <div className="relative mb-1 mt-6 flex h-7 w-14 items-center justify-center rounded-[.4em] bg-[#3d4150] text-center text-[11px] text-white">
                  취약파트
                </div>
              )}
              <Text size="caption-02" color="text-gray01">
                {incorrectCount}/{totalCount}
              </Text>
              <div className="relative h-32 w-8 rounded-md bg-red02">
                <div
                  className="absolute bottom-0 left-0 z-20 w-8 rounded-md bg-red01"
                  style={{ height }}
                ></div>
              </div>
              <Text size="caption-02" color="text-gray01">
                {entryMap[item.toUpperCase()]}
              </Text>
            </div>
          );
        })}
      </div>
    </div>
  );
}

export default MyWeakPart;
