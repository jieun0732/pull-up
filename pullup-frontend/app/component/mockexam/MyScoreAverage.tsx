"use client";
import Image from "next/image";
import ProgressBar from "react-customizable-progressbar";
import Text from "../ui/Text";
import Button from "../ui/Button";
import { compareScores } from "@/utils/compareFunc";
import higherlogo from "@/assets/logo/higherLogo.png";
import samelogo from "@/assets/logo/sameLogo.png";
import lowerlogo from "@/assets/logo/lowerLogo.png";
import useComponentSize from "@/hooks/useComponentSize";
import { API, fetcher } from "@/lib/API";
import useSWR from "swr";
import { MockExamAverageType } from "@/types/mockexam/mockexamReport";

function MyScoreAverage() {

  
  const [componentRef, size] = useComponentSize();
  const memberID = localStorage.getItem("memberId") || "";

  const { data: averageScore } = useSWR<MockExamAverageType>(
    `${API}/exams/mock-exam/recent/${memberID}`,
    fetcher,
  );

  if (!averageScore) {
    return (
    <div className="flex h-[370px] w-full flex-col rounded-2xl bg-white p-6">
    <div className="w-full">
      <Text size="head-02" color="text-blue01">
        잠시만 기다려주세요!
      </Text>
      <Text size="caption-02" color="text-gray01">
        잠시만 기다려주세요!
      </Text>
      <div
        className="relative mt-3 flex w-full flex-col items-center justify-center"
        ref={componentRef}
      >
        <div className="absolute left-0 top-0">
          <ProgressBar
            progress={0}
            radius={0}
            cut={200}
            rotate={190}
            initialAnimation={false}
            strokeColor="#4d70ec"
            strokeWidth={18}
            trackStrokeColor="#F2F3F6"
            trackStrokeWidth={18}
            strokeLinecap={"round"}
            pointerRadius={1}
            pointerStrokeWidth={10}
            pointerStrokeColor={"#4d70ec"}
          />
          <div className="absolute top-0 flex h-full w-full select-none flex-col items-center justify-center text-center">
            <div className="flex items-center gap-2">
              <div className="h-2 w-2 rounded-full bg-blue01"></div>
              <Text size="caption-02" color="text-gray01" className="mr-2">
                내 점수
              </Text>
              <div className="h-2 w-2 rounded-full bg-black01"></div>
              <Text size="caption-02" color="text-gray01">
                평균
              </Text>
            </div>
            <Image
              src={""}
              alt="statusIcon"
              className="mt-3 h-32 w-auto"
              width={40}
            />
            <div className="mx-auto mt-5 flex w-full items-center justify-center gap-3">
              <Button size="small" color="activeLight">
              상위 ??%
              </Button>
              <Text size="head-02">??점</Text>
            </div>
          </div>
        </div>

        <div className="absolute left-0 top-0">
          <ProgressBar
            progress={0}
            radius={0}
            cut={200}
            rotate={190}
            initialAnimation={false}
            strokeColor="#4d6fec0"
            strokeWidth={18}
            trackStrokeColor="#f2f3f60"
            trackStrokeWidth={18}
            strokeLinecap={"round"}
            pointerRadius={1}
            pointerStrokeWidth={10}
            pointerStrokeColor={"#3d4150"}
          />
        </div>
      </div>
    </div>
  </div>
  )}

  const progress = averageScore.score;
  const averageProgress = averageScore.averageScore;
  const status = compareScores(averageProgress, progress);

  

  const scoreStatus = {
    lower: {
      title: "더 많이 노력해야 해요!",
      subtitle: `님의 점수는 평균보다 ${(averageProgress)-(progress)}점 낮아요.`,
      logo: lowerlogo,
    },
    same: {
      title: "조금만 더 노력하면 합격권이에요!",
      subtitle: "님의 점수는 평균이랑 같아요.",
      logo: samelogo,
    },
    higher: {
      title: "우수한 합격권이에요!",
      subtitle: `님의 점수는 평균보다 ${(progress)-(averageProgress)}점 높아요.`,
      logo: higherlogo,
    },
  };

  const PROGRESSBAR_SIZE = size.width > 0 ? size.width / 2 - 15 : 0;

  return (
    <div className="flex h-[370px] w-full flex-col rounded-2xl bg-white p-6">
      <div className="w-full">
        <Text size="head-02" color="text-blue01">
          {scoreStatus[status].title}
        </Text>
        <Text size="caption-02" color="text-gray01">
          {scoreStatus[status].subtitle}
        </Text>
        <div
          className="relative mt-3 flex w-full flex-col items-center justify-center"
          ref={componentRef}
        >
          <div className="absolute left-0 top-0">
            <ProgressBar
              progress={progress}
              radius={PROGRESSBAR_SIZE}
              cut={200}
              rotate={190}
              initialAnimation={false}
              strokeColor="#4d70ec"
              strokeWidth={18}
              trackStrokeColor="#F2F3F6"
              trackStrokeWidth={18}
              strokeLinecap={"round"}
              pointerRadius={1}
              pointerStrokeWidth={10}
              pointerStrokeColor={"#4d70ec"}
            />
            <div className="absolute top-0 flex h-full w-full select-none flex-col items-center justify-center text-center">
              <div className="flex items-center gap-2">
                <div className="h-2 w-2 rounded-full bg-blue01"></div>
                <Text size="caption-02" color="text-gray01" className="mr-2">
                  내 점수
                </Text>
                <div className="h-2 w-2 rounded-full bg-black01"></div>
                <Text size="caption-02" color="text-gray01">
                  평균
                </Text>
              </div>
              <Image
                src={scoreStatus[status].logo}
                alt="statusIcon"
                className="mt-3 h-32 w-auto"
                width={40}
              />
              <div className="mx-auto mt-5 flex w-full items-center justify-center gap-3">
                <Button size="small" color="activeLight">
                {averageScore.rankPercent}
                </Button>
                <Text size="head-02">{averageScore.score}점</Text>
              </div>
            </div>
          </div>

          <div className="absolute left-0 top-0">
            <ProgressBar
              progress={averageProgress}
              radius={PROGRESSBAR_SIZE}
              cut={200}
              rotate={190}
              initialAnimation={false}
              strokeColor="#4d6fec0"
              strokeWidth={18}
              trackStrokeColor="#f2f3f60"
              trackStrokeWidth={18}
              strokeLinecap={"round"}
              pointerRadius={1}
              pointerStrokeWidth={10}
              pointerStrokeColor={"#3d4150"}
            />
          </div>
        </div>
      </div>
    </div>
  );
}

export default MyScoreAverage;