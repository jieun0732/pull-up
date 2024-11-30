"use client";

import Image from "next/image";
import { useParams, useRouter } from "next/navigation";
import finishedLogo from "@/assets/logo/studyFinished.png";
import notFinishedLogo from "@/assets/logo/studyNotFinished.png";
import ProgressBar from "@/component/sectional/progressbar";
import Button from "@/component/ui/Button";
import Header from "@/component/ui/Header";
import Text from "@/component/ui/Text";
import { ReplayIcon, ArrowIcon } from "@/assets/icon/Icons";
import ReplaySpeechBubble from "@/component/sectional/ReplaySpeechBubble";
import useSWR from "swr";
import { API, fetcher } from "@/lib/API";
import { entryMap } from "@/constants/constants";
import { SolvedProblems, Entry } from "@/types/problemType";
import useMutation from "@/hooks/useMutation";
import useProblemStore from "@/stores/useProblemStore";
import {
  postStartSectionalExam,
  resetSelectedAnswers,
} from "@/lib/sectionalAPI";
import Spinner from "@/component/ui/Spinner";
import useUserStore from "@/stores/useUserStore";
import { SectionalStartResponseType } from "@/types/sectionalType";
export default function Page() {
  const router = useRouter();
  const params = useParams<{
    entry: Entry;
  }>();
  const entry = entryMap[params.entry];
  const { user } = useUserStore();
  const { setExamId } = useProblemStore();

  const { data, error: solvedError } = useSWR<SolvedProblems>(
    `${API}/members/entry-exam/${params.entry}/solved?memberId=${user.memberId}`,
    fetcher,
  );

  const handleStart = async (
    type: "EVENLY" | "BY_PROBLEM_TYPE",
    problemType?: string,
  ) => {
    const res = await postStartSectionalExam(
      user.memberId,
      type,
      params.entry,
      problemType,
    );
    if (res?.examId) {
      setExamId(res.examId);
      router.push(`/main/sectional/${params.entry}/${type}/1`);
    }
  };

  const handleNext = async (
    type: "EVENLY" | "BY_PROBLEM_TYPE",
    ExamId: number,
    problemNumber: number,
  ) => {
    setExamId(ExamId);
    router.push(
      `/main/sectional/${params.entry}/${type}/${problemNumber + 1}/`,
    );
  };

  return (
    <>
      {data ? (
        <div className="flex flex-col items-center px-5 pb-7 pt-20">
          <Header
            type="back"
            content={`${entry}영역`}
            link={`/main/sectional/`}
          />
          <Text size="head-02" className="self-start">
            {entry}영역의 대표 예제를
          </Text>
          <Text size="head-02" className="self-start">
            다양하게 만나보고 싶다면?
          </Text>

          <div className="relative mt-4 flex w-full flex-col items-center gap-4 rounded-3xl bg-blue03 p-6">
            {data.evenlyExamInfo.isStarted ? (
              <>
                <ReplayIcon
                  className="absolute right-6"
                  onClick={async () => {
                    try {
                      await resetSelectedAnswers(data.evenlyExamInfo.examId);
                      await handleStart("EVENLY");
                    } catch (error) {
                      console.error("Error starting the exam:", error);
                    }
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
                  onClick={() =>
                    handleNext(
                      "EVENLY",
                      data.evenlyExamInfo.examId,
                      data.evenlyExamInfo.lastSolvedProblemNumber,
                    )
                  }
                >
                  남은 문제 이어서 풀기
                </Button>
                <button
                  className="flex w-full items-center justify-center gap-2 font-semibold text-blue01"
                  onClick={() => {
                    setExamId(data.evenlyExamInfo.examId);
                    router.push(
                      `/main/sectional/${params.entry}/EVENLY/result`,
                    );
                  }}
                >
                  골고루 학습 결과 보기
                  <ArrowIcon />
                </button>
              </>
            ) : (
              <>
                <ReplayIcon
                  className="absolute right-6"
                  onClick={async () => {
                    try {
                      await resetSelectedAnswers(data.evenlyExamInfo.examId);
                      await handleStart("EVENLY");
                    } catch (error) {
                      console.error("Error starting the exam:", error);
                    }
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
                  onClick={() => handleStart("EVENLY")}
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
            문제 유형 {data.problemTypeExamInfos.length}
          </Text>
          {data.problemTypeExamInfos.map((item, idx) => (
            <div
              key={idx}
              className="mb-4 w-full rounded-md border border-solid border-white03 bg-white02 px-6 py-4 shadow-[2px_2px_20px_0px_rgba(0,0,0,0.07)]"
            >
              <Text
                size="head-04"
                className="mb-1 flex items-center gap-3 self-start"
              >
                {item.problemType} {item.solvedProblemCount}/
                {item.totalProblemCount}
                <ReplayIcon
                  className="relative"
                  onClick={async () => {
                    try {
                      await resetSelectedAnswers(item.examId);
                      await handleStart("BY_PROBLEM_TYPE", item.problemType);
                    } catch (error) {
                      console.error("Error starting the exam:", error);
                    }
                  }}
                >
                  {idx === 0 && <ReplaySpeechBubble />}
                </ReplayIcon>
              </Text>

              {item.incorrectProblemCount > 0 && (
                <Text size="caption-02" color="text-red01">
                  {user.name} 님이 틀렸던 유형이에요!
                </Text>
              )}
              <ProgressBar
                now={item.solvedProblemCount}
                total={item.totalProblemCount}
              />
              <div className="mt-3 flex w-full gap-2">
                {item.solvedProblemCount > 0 ? (
                  <>
                    <Button
                      size="medium"
                      color="activeLight"
                      onClick={() =>
                        handleNext(
                          "BY_PROBLEM_TYPE",
                          item.examId,
                          item.solvedProblemCount,
                        )
                      }
                    >
                      이어서 풀기
                    </Button>
                    <Button
                      size="medium"
                      color="activeBlack"
                      onClick={() => {
                        setExamId(item.examId);
                        router.push(
                          `/main/sectional/${params.entry}/BY_PROBLEM_TYPE/result`,
                        );
                      }}
                    >
                      채점 결과 보기
                    </Button>
                  </>
                ) : (
                  <>
                    <Button
                      size="medium"
                      color="activeLight"
                      onClick={() =>
                        handleStart("BY_PROBLEM_TYPE", item.problemType)
                      }
                    >
                      학습하기
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
      ) : (
        <Spinner />
      )}
    </>
  );
}
