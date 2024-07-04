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

function MyScoreAverage() {
  const progress = 90;
  const averageProgress = 80;
  const status = compareScores(progress, averageProgress);
  const [componentRef, size] = useComponentSize();

  const scoreStatus = {
    lower: {
      title: "우수한 합격권이에요!",
      subtitle: "님의 점수는 평균보다 10점 높아요.",
      logo: higherlogo,
    },
    same: {
      title: "조금만 더 노력하면 합격권이에요!",
      subtitle: "님의 점수는 평균보다 10점 높아요.",
      logo: samelogo,
    },
    higher: {
      title: "더 많이 노력해야 해요!",
      subtitle: "님의 점수는 평균보다 10점 높아요.",
      logo: lowerlogo,
    },
  };

  return (
    <div className="flex aspect-square h-auto w-full flex-col rounded-2xl bg-white p-6">
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
              radius={size.width / 2 - 15}
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
                className="mt-3 w-40"
              />
              <div className="mx-auto mt-5 flex w-full items-center justify-center gap-3">
                <Button size="small" color="activeLight">
                  상위 12%
                </Button>
                <Text size="head-02">85점</Text>
              </div>
            </div>
          </div>
          ㄴ ㄴ
        </div>
      </div>
    </div>
  );
}

export default MyScoreAverage;
