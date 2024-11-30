"use client";

import Text from "@/component/ui/Text";
import Button from "@/component/ui/Button";
import { useRouter } from "next/navigation";
import { CloseIcon } from "@/assets/icon/Icons";
import useSWR from "swr";
import { API, fetcher } from "@/lib/API";
import { MockExamResultType } from "@/types/mockexam/mockexamQuestion";
import LocalStorage from "@/utils/LocalStorage";
import Spinner from "@/component/ui/Spinner";
import CorrectIcon from "@/assets/icon/problem/correctIcon";
import formatNumber from "@/utils/formatNumber";
import WrongIcon from "@/assets/icon/problem/wrongIcon";
import useExamStore from "@/stores/useExamStore";

export default function Page() {
  const router = useRouter();
  const { examId } = useExamStore();

  const { data, isLoading } = useSWR<MockExamResultType>(
    `${API}/exams/mock-exam/result/${examId}`,
    fetcher,
  );

  return (
    <>
      {data ? (
        <div className="h-full w-full min-w-[320px] max-w-[450px] overflow-y-auto bg-[#ffffff] px-5 pb-7 pt-20 text-black01">
          <div className="relative mb-6 h-[25px] w-full text-center text-[17px] font-bold">
            <CloseIcon onClick={() => router.push(`/main/mockexam/report`)} />
          </div>
          <Text size="head-02" className="self-start">
            {data.memberName}님의 모의고사 점수는
          </Text>
          <Text size="head-02" className="self-start">
            {data.score}점이예요!
          </Text>
          <Text
            size="head-05"
            color="text-gray01"
            className="mb-6 mt-2 self-start"
          >
            총 20문제 중에 {data.correctProblemCount}문제를 맞혔어요!
          </Text>

          {data.results.map((item, idx) => {
            if (item.isCorrect) {
              // Use === for comparison
              return (
                <div
                  key={idx}
                  className="mb-4 w-full rounded-md border-2 border-solid border-white03 bg-white px-6 py-4 shadow-[2px_2px_20px_0px_rgba(0,0,0,0.07)]"
                >
                  <div className="flex gap-2">
                    <CorrectIcon />
                    <div className="mb-2">
                      <Text size="head-04">문제 {formatNumber(idx + 1)} </Text>
                      <Text size="caption-01" color="text-gray02">
                        {item.problemType}
                      </Text>
                    </div>
                  </div>
                  <Button
                    size="large"
                    color="green"
                    onClick={() =>
                      router.push(`/main/mockexam/result/${item.problemNumber}`)
                    }
                  >
                    해설 확인하기
                  </Button>
                </div>
              );
            } else {
              return (
                <div
                  key={idx}
                  className="mb-4 w-full rounded-md border-2 border-solid border-white03 bg-white px-6 py-4 shadow-[2px_2px_20px_0px_rgba(0,0,0,0.07)]"
                >
                  <div className="flex gap-2">
                    <WrongIcon />
                    <div className="mb-2">
                      <Text size="head-04">문제 {formatNumber(idx + 1)} </Text>
                      <Text size="caption-01" color="text-gray02">
                        {item.problemType}
                      </Text>
                    </div>
                  </div>
                  <Button
                    size="large"
                    color="red"
                    onClick={() =>
                      router.push(`/main/mockexam/result/${item.problemNumber}`)
                    }
                  >
                    해설 확인하기
                  </Button>
                </div>
              );
            }
          })}
        </div>
      ) : (
        <Spinner />
      )}
    </>
  );
}
