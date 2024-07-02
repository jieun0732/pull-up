"use client";
import { useRouter } from "next/navigation";
import Button from "@/component/ui/Button";
import Text from "@/component/ui/Text";

export default function Page() {
  const isFinished = false;
  const router = useRouter();

  return (
    <div className="flex h-full flex-col justify-between bg-white px-5 pb-[91px] pt-14">
      {isFinished ? (
        <div>
          <Text size="head-02">모의고사 진단이 완료되었어요!</Text>
          <Text size="head-05" color="text-gray01" customstyle="mb-[26px]">
            리포트를 확인해보세요!
          </Text>
        </div>
      ) : (
        <div>
          <Text size="head-02">문제를 풀고</Text>
          <Text size="head-02">골라서 풀어볼 수 있어요!</Text>
          <Text size="head-05" color="text-gray01" customstyle="mb-[26px]">
            나의 취약한 영역을 공략해 효율적으로 학습해요
          </Text>
        </div>
      )}
      <div className="self-center">아이콘</div>
      {isFinished ? (
        <div>
          <p className="mb-3 w-full text-center text-[15px] leading-4 text-blue01">
            ㅇㅇ님이 취약한 파트를 <br></br>알 수 있어요
          </p>
          <Button size="large" color="active">
            모의고사 결과 확인하기
          </Button>
        </div>
      ) : (
        <div>
          <p className="mb-3 w-full text-center text-[15px] leading-4 text-blue01">
            총 20문제로<br></br>30분 동안 풀 수 있어요
          </p>
          <Button
            size="large"
            color="active"
            customstyle="mb-11"
            onClick={() => router.push("/main/mocktest/1")}
          >
            모의고사 풀기
          </Button>
        </div>
      )}
    </div>
  );
}
