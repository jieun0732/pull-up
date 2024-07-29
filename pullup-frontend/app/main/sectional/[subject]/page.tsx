"use client";

import Image from "next/image";
import { useParams, useRouter } from "next/navigation";
import finishedLogo from "@/assets/logo/studyFinished.png";
import notFinishedLogo from "@/assets/logo/studyNotFinished.png";
import ProgressBar from "@/component/sectional/progressbar";
import Button from "@/component/ui/Button";
import Header from "@/component/ui/Header";
import Text from "@/component/ui/Text";
import { sections } from "@/constants/constants";
import { ReplayIcon, ArrowIcon } from "@/assets/icon";
import ReplaySpeechBubble from "@/component/sectional/ReplaySpeechBubble";

const dummydata = {
  isFinished: false,
  data: [
    {
      id: 0,
      type: "유의어",
      now: 4,
      total: 10,
      hasWrong: true,
    },
    {
      id: 1,
      type: "반의어",
      now: 2,
      total: 10,
      hasWrong: false,
    },
    {
      id: 2,
      type: "유의어1",
      now: 0,
      total: 10,
      hasWrong: false,
    },
  ],
};
export default function Page() {
  const router = useRouter();
  const params = useParams<{ subject: string }>();
  const currentSection = sections[params.subject];

  return (
    <div className="flex flex-col items-center px-5 pb-7 pt-14">
      <Header type="back" content={currentSection} link={`/main/sectional/`} />
      <Text size="head-02" className="self-start">
        {currentSection}의 대표 예제를
      </Text>
      <Text size="head-02" className="self-start">
        다양하게 만나보고 싶다면?
      </Text>

      <div className="relative mt-4 flex w-full flex-col items-center gap-4 rounded-3xl bg-blue03 p-6">
        {dummydata.isFinished ? (
          <>
            <ReplayIcon
              className="absolute right-6"
              onClick={() => console.log("replay")}
            />
            <Image
              className="w-[185px]"
              src={finishedLogo}
              alt="Profile Image"
            />
            <Button size="large" color="active">
              남은 문제 이어서 풀기
            </Button>
            <button className="flex w-full items-center justify-center gap-2 font-semibold text-blue01">
              골고루 학습 결과 보기
              <ArrowIcon />
            </button>
          </>
        ) : (
          <>
            <ReplayIcon
              className="absolute right-6"
              onClick={() => console.log("replay")}
            />
            <Image
              className="w-44 rounded-full"
              src={notFinishedLogo}
              alt="icon"
            />
            <Button
              size="large"
              color="active"
              onClick={() =>
                router.push(`/main/sectional/${params.subject}/mix/1`)
              }
            >
              골고루 학습하기
            </Button>
          </>
        )}
      </div>

      <Text size="head-03" className="mb-3 mt-14 self-start">
        필요한 유형만 학습할 수 있어요!
      </Text>
      <Text size="head-04" color="text-gray01" className="mb-2 self-start">
        문제 유형 2
      </Text>
      {dummydata.data.map((item) => {
        return (
          <div
            key={item.type}
            className="mb-4 w-full rounded-md border border-solid border-white03 bg-white02 px-6 py-4 shadow-[2px_2px_20px_0px_rgba(0,0,0,0.07)]"
          >
            <Text
              size="head-04"
              className="mb-1 flex items-center gap-3 self-start"
            >
              {item.type} {item.now}/{item.total}
              <ReplayIcon
                className="relative"
                onClick={() => console.log("replay")}
              >
                {item.id === 0 && <ReplaySpeechBubble />}
              </ReplayIcon>
            </Text>

            {item.hasWrong && (
              <Text size="caption-02" color="text-red01">
                ㅇㅇ 님이 틀렸던 유형이에요!
              </Text>
            )}
            <ProgressBar now={item.now} total={item.total} />
            <div className="mt-3 flex w-full gap-2">
              {item.now ? (
                <>
                  <Button size="medium" color="activeLight">
                    학습하기
                  </Button>
                  <Button size="medium" color="activeBlack">
                    채점 결과 보기
                  </Button>
                </>
              ) : (
                <>
                  <Button size="medium" color="activeLight">
                    이어서 풀기
                  </Button>
                  <Button size="medium" color="nonactive">
                    채점 결과 보기
                  </Button>
                </>
              )}
            </div>
          </div>
        );
      })}
    </div>
  );
}
