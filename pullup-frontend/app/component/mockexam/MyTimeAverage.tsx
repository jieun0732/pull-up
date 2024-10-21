"use client";

import Text from "../ui/Text";
import Button from "../ui/Button";
import ProgressBar from "react-customizable-progressbar";
import { compareTime } from "@/utils/compareFunc";
import useComponentSize from "@/hooks/useComponentSize";
import { MockExamTimeAverageType } from "@/types/mockexam/mockexamReport";
import useSWR from "swr";
import { API, fetcher } from "@/lib/API";
import LocalStorage from "@/utils/LocalStorage";

function MyTimeAverage() {
  const [componentRef, size] = useComponentSize();
  const memberID = LocalStorage.getItem("memberId") || "";

  const { data: averageScore } = useSWR<MockExamTimeAverageType>(
    `${API}/exams/mock-exam/recent/${memberID}`,
    fetcher,
  );

  if (!averageScore) {
    return (
      <div className="mt-5 flex w-full flex-col rounded-2xl bg-white p-6">
            <Text size="head-03" className="mb-2">
              소요시간
            </Text>
            <Text size="caption-02" color="text-gray01">
              제한 시간은 총 20분이였어요.
            </Text>
            <Text size="caption-02" color="text-gray01">
              이지은 님은 ??분 만에 모든 문제를 풀어냈어요!
            </Text>
            <div>
              <Button size="small" color="activeLight" className="ml-10 mt-5">
                평균보다 ??분 ??요!
              </Button>
              <div className="flex w-full justify-around" ref={componentRef}>
                <div className="relative flex flex-col items-center">
                  <ProgressBar
                    progress={0}
                    radius={0}
                    inverse={false}
                    rotate={180 + 90}
                    strokeColor="#4d70ec"
                    strokeWidth={14}
                    trackStrokeColor="#F2F3F6"
                    trackStrokeWidth={14}
                    strokeLinecap={"square"}
                    counterClockwise={true}
                  />
                  <Text
                    size="head-04"
                    className="absolute top-[37%] flex h-full w-full select-none justify-center text-center"
                  >
                    ??분
                  </Text>
                  <Text
                    size="caption-02"
                    color="text-gray01"
                    className="select-none justify-center text-center"
                  >
                    이지은 님
                  </Text>
                </div>
                <div className="relative flex flex-col items-center">
                  <ProgressBar
                    progress={0}
                    radius={0}
                    inverse={false}
                    rotate={180 + 90}
                    strokeColor="#3D4150"
                    strokeWidth={14}
                    trackStrokeColor="#F2F3F6"
                    trackStrokeWidth={14}
                    strokeLinecap={"square"}
                    counterClockwise={true}
                  />
                  <Text
                    size="head-04"
                    className="absolute top-[37%] flex h-full w-full select-none justify-center text-center"
                  >
                    ??분
                  </Text>
                  <Text
                    size="caption-02"
                    color="text-gray01"
                    className="select-none justify-center text-center"
                  >
                    평균
                  </Text>
                </div>
              </div>
            </div>
          </div>
    )
  }

  const mytime = 10;
  const averageTime = 15;
  const status = compareTime(averageTime, mytime);

  const scoreStatus = {
    higher: `평균보다 ${averageTime - mytime}분 빨라요!`,
    same: "평균이에요",
    lower: `평균보다 ${mytime - averageTime}분 느려요!`,
  };

  const PROGRESSBAR_SIZE = size.width / 4 - 35 > 0 ? size.width / 4 - 35 : 0;

  return (
    <div className="mt-5 flex w-full flex-col rounded-2xl bg-white p-6">
      <Text size="head-03" className="mb-2">
        소요시간
      </Text>
      <Text size="caption-02" color="text-gray01">
        제한 시간은 총 20분이였어요.
      </Text>
      <Text size="caption-02" color="text-gray01">
        이지은 님은 {mytime}분 만에 모든 문제를 풀어냈어요!
      </Text>
      <div>
        <Button size="small" color="activeLight" className="ml-10 mt-5">
          {scoreStatus[status]}
        </Button>
        <div className="flex w-full justify-around" ref={componentRef}>
          <div className="relative flex flex-col items-center">
            <ProgressBar
              progress={(mytime / 20) * 100}
              radius={PROGRESSBAR_SIZE}
              inverse={false}
              rotate={180 + 90}
              strokeColor="#4d70ec"
              strokeWidth={14}
              trackStrokeColor="#F2F3F6"
              trackStrokeWidth={14}
              strokeLinecap={"square"}
              counterClockwise={true}
            />
            <Text
              size="head-04"
              className="absolute top-[37%] flex h-full w-full select-none justify-center text-center"
            >
              {mytime}분
            </Text>
            <Text
              size="caption-02"
              color="text-gray01"
              className="select-none justify-center text-center"
            >
              이지은 님
            </Text>
          </div>
          <div className="relative flex flex-col items-center">
            <ProgressBar
              progress={(averageTime / 20) * 100}
              radius={PROGRESSBAR_SIZE}
              inverse={false}
              rotate={180 + 90}
              strokeColor="#3D4150"
              strokeWidth={14}
              trackStrokeColor="#F2F3F6"
              trackStrokeWidth={14}
              strokeLinecap={"square"}
              counterClockwise={true}
            />
            <Text
              size="head-04"
              className="absolute top-[37%] flex h-full w-full select-none justify-center text-center"
            >
              {averageTime}분
            </Text>
            <Text
              size="caption-02"
              color="text-gray01"
              className="select-none justify-center text-center"
            >
              평균
            </Text>
          </div>
        </div>
      </div>
    </div>
  );
}

export default MyTimeAverage;
