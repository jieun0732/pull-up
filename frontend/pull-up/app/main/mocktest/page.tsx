"use client";
import { useRouter } from "next/navigation";
import Button from "@/component/ui/Button";
import Text from "@/component/ui/Text";
import Image from "next/image";
import rankLogo from "@/assets/logo/rankLogo.png";
import questionLogo from "@/assets/logo/questionLogo.png";

export default function Page() {
  const isFinished = true;
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
          <Button size="large" color="active">
            모의고사 결과 확인하기
          </Button>
        </div>
      ) : (
        <div className="flex h-full flex-col items-center justify-around">
          <Image
            src={rankLogo}
            alt="모의고사 풀기 로고"
            className="self-center"
          />
          <div className="flex gap-10">
            <div className="text-center">
              <Text size="caption-01">제한시간</Text>
              <Text size="head-02">20분</Text>
            </div>
            <div className="text-center">
              <Text size="caption-01">총 문제 수</Text>
              <Text size="head-02">20개</Text>
            </div>
          </div>
          <Button
            size="large"
            color="active"
            className="mb-11"
            onClick={() => router.push("/main/mocktest/1")}
          >
            모의고사 풀기
          </Button>
        </div>
      )}
    </div>
  );
}
