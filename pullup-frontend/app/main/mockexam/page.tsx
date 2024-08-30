"use client";
import { useRouter } from "next/navigation";
import Button from "@/component/ui/Button";
import Text from "@/component/ui/Text";
import Image from "next/image";
import rankLogo from "@/assets/logo/rankLogo.png";
import questionLogo from "@/assets/logo/questionLogo.png";
import { TotalQuestionNumberIcon, LimitTimeIcon } from "@/assets/icon";

export default function Page() {
  const isFinished = false;
  const router = useRouter();

  return (
    <div className="flex h-full flex-col bg-white px-5 pb-[91px] pt-14">
      <div className="">
        <Text size="head-02">문제를 풀고</Text>
        <Text size="head-02">내 실력을 확인해볼 수 있다면?</Text>
        <Text size="head-05" color="text-gray01" className="mb-[26px]">
          모의고사 진단으로 내 위치를 알아봐요!
        </Text>
      </div>

      {isFinished ? (
        <div className="flex h-full flex-col items-center justify-around">
          <Image
            src={questionLogo}
            alt="모의고사 결과 로고"
            className="self-center"
          />

          <Button size="large" color="active" className="relative">
            모의고사 결과 확인하기
            <div className="absolute top-[-60px] whitespace-nowrap rounded-[0.4em] bg-black01 px-4 py-2 text-white after:absolute after:bottom-0 after:left-1/2 after:mb-[-9px] after:h-0 after:w-0 after:-translate-x-1/2 after:border-[9px] after:border-b-0 after:border-transparent after:border-t-black01 after:content-['']">
              <Text size="caption-02">문제를 다시풀 수 있어요!</Text>
            </div>
          </Button>
        </div>
      ) : (
        <div className="flex h-full flex-col items-center justify-around">
          <Image
            src={rankLogo}
            alt="모의고사 풀기 로고"
            className="self-center"
          />
          <div className="flex w-full items-center justify-around rounded-xl bg-gray03 px-7 py-3">
            <div className="flex items-center gap-2">
              <TotalQuestionNumberIcon />
              <Text size="caption-01">총 문제 수</Text>
              <Text size="head-04">20개</Text>
            </div>
            <div className="h-3 w-[2px] rounded-xl bg-[#d9d9d9]"></div>
            <div className="flex items-center gap-2">
              <LimitTimeIcon />
              <Text size="caption-01">제한시간</Text>
              <Text size="head-04">20분</Text>
            </div>
          </div>

          <Button
            size="large"
            color="active"
            className="mb-11"
            onClick={() => router.push("/main/mockexam/1")}
          >
            모의고사 풀기
          </Button>
        </div>
      )}
    </div>
  );
}
