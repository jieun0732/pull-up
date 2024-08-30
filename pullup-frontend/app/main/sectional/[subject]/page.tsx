"use client";

import Image from "next/image";
import { useParams, useRouter } from "next/navigation";
import finishedLogo from "@/assets/logo/studyFinished.png";
import notFinishedLogo from "@/assets/logo/studyNotFinished.png";
import ProgressBar from "@/component/sectional/progressbar";
import Button from "@/component/ui/Button";
import Header from "@/component/ui/Header";
import Text from "@/component/ui/Text";
import { ReplayIcon, ArrowIcon } from "@/assets/icon";
import ReplaySpeechBubble from "@/component/sectional/ReplaySpeechBubble";
import useSWR from "swr";
import { Category } from "@/types/sectionalType";
import { API, fetcher } from "@/lib/API";
import { useState } from "react";
import { entryMap } from "@/constants/constants";
import { ProblemInfo } from "@/types/problemType";

export default function Page() {
  const router = useRouter();
  const params = useParams<{ subject: string }>();

  const entry = entryMap[params.subject];
  const [category, setCategory] = useState<string>("골고루");
  const memberID = 1;

  const { data, error } = useSWR<Category[]>(
    `${API}/exams/problems/${params.subject}?memberId=1`,
    fetcher,
  );

  const isFinished = data?.some(
    (item) => item.category === "골고루" && item.answeredProblems !== 0,
  );

  if (!data) return;

  return (
    <div className="flex flex-col items-center px-5 pb-7 pt-14">
      <Header type="back" content={`${entry}영역`} link={`/main/sectional/`} />
      <Text size="head-02" className="self-start">
        {entry}영역의 대표 예제를
      </Text>
      <Text size="head-02" className="self-start">
        다양하게 만나보고 싶다면?
      </Text>

      <div className="relative mt-4 flex w-full flex-col items-center gap-4 rounded-3xl bg-blue03 p-6">
        {isFinished ? (
          <>
            <ReplayIcon
              className="absolute right-6"
              onClick={() => {
                const queryString = new URLSearchParams({
                  memberId: memberID.toString(),
                  entry,
                  category,
                }).toString();
                fetch(`${API}/exams/reset?${queryString}`, {
                  method: "POST",
                })
                  .then((response) => {
                    console.log("============");
                    router.push(`/main/sectional/${params.subject}/mix/1`);

                    // response.json();
                  })
                  .then((result) => console.log(result));
              }}
            />
            <Image
              className="w-[185px]"
              src={finishedLogo}
              alt="Profile Image"
            />
            <Button
              size="large"
              color="active"
              onClick={() => {
                const queryString = new URLSearchParams({
                  memberId: memberID.toString(),
                  entry,
                  category,
                }).toString();

                const fetchNextProblem = async () => {
                  try {
                    const response = await fetch(
                      `${API}/exams/next?${queryString}`,
                      {
                        method: "GET",
                      },
                    );

                    if (!response.ok) {
                      throw new Error("Network response was not ok");
                    }

                    const res: ProblemInfo = await response.json(); // 타입 지정
                    const nextProblemId = res.id;

                    router.push(
                      `/main/sectional/${params.subject}/mix/${nextProblemId}`,
                    );
                  } catch (error) {
                    console.error(
                      "There was a problem with the fetch operation:",
                      error,
                    );
                  }
                };
                fetchNextProblem();
              }}
            >
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
              onClick={() => {
                const queryString = new URLSearchParams({
                  memberId: memberID.toString(),
                  entry,
                  category,
                }).toString();
                fetch(`${API}/exams/reset?${queryString}`, {
                  method: "POST",
                })
                  .then((response) => {
                    console.log("============");
                    router.push(`/main/sectional/${params.subject}/mix/1`);

                    // response.json();
                  })
                  .then((result) => console.log(result));
              }}
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
      {data
        ?.filter((item) => item.category === "유형별")
        .map((item, idx) => (
          <div
            key={item.type}
            className="mb-4 w-full rounded-md border border-solid border-white03 bg-white02 px-6 py-4 shadow-[2px_2px_20px_0px_rgba(0,0,0,0.07)]"
          >
            <Text
              size="head-04"
              className="mb-1 flex items-center gap-3 self-start"
            >
              {item.type} {item.answeredProblems}/{item.totalProblems}
              <ReplayIcon
                className="relative"
                onClick={() => console.log("replay")}
              >
                {idx === 0 && <ReplaySpeechBubble />}
              </ReplayIcon>
            </Text>

            {!item.isCorrect && (
              <Text size="caption-02" color="text-red01">
                ㅇㅇ 님이 틀렸던 유형이에요!
              </Text>
            )}
            <ProgressBar
              now={item.answeredProblems}
              total={item.totalProblems}
            />
            <div className="mt-3 flex w-full gap-2">
              {item.answeredProblems ? (
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
        ))}
    </div>
  );
}
