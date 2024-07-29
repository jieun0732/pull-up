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

const dummydata = {
  isFinished: false,
  data: [
    {
      type: "유의어",
      now: 4,
      total: 10,
      hasWrong: true,
    },
    {
      type: "반의어",
      now: 2,
      total: 10,
      hasWrong: false,
    },
    {
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
      <Header type="back" content={currentSection} />
      <Text size="head-02" className="self-start">
        {currentSection}의 대표 예제를
      </Text>
      <Text size="head-02" className="self-start">
        다양하게 만나보고 싶다면?
      </Text>

      <div className="relative flex w-full flex-col items-center gap-4 bg-green-400 p-6">
        {dummydata.isFinished ? (
          <>
            <Image
              className="w-[185px]"
              src={finishedLogo}
              alt="Profile Image"
            />
            <Button size="large" color="active">
              골고루 학습 결과 보기
            </Button>
            <button className="flex w-full items-center justify-center gap-2 text-blue01">
              남은 문제 이어서 풀기
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="10"
                height="8"
                viewBox="0 0 10 8"
                fill="none"
              >
                <path
                  fillRule="evenodd"
                  clipRule="evenodd"
                  d="M5.03944 0.273312C5.33233 -0.019581 5.80721 -0.019581 6.1001 0.273312L9.67099 3.8442C9.7491 3.92231 9.7491 4.04894 9.67099 4.12704L8.7569 5.04113C8.75406 5.04398 8.74945 5.04398 8.7466 5.04113C8.74376 5.03829 8.73915 5.03829 8.73631 5.04113L6.05076 7.72668C5.75787 8.01957 5.28299 8.01957 4.9901 7.72668C4.69721 7.43379 4.69721 6.95891 4.9901 6.66602L7.10248 4.55364H0.570432C0.404746 4.55364 0.270432 4.41933 0.270432 4.25364V3.35364C0.270432 3.18796 0.404746 3.05364 0.570432 3.05364H6.75911L5.03944 1.33397C4.74655 1.04108 4.74655 0.566205 5.03944 0.273312Z"
                  fill="#4D70EC"
                />
              </svg>
            </button>
          </>
        ) : (
          <>
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
            <Text size="head-04" className="mb-1 self-start">
              {item.type} {item.now}/{item.total}
            </Text>
            {item.hasWrong && (
              <Text size="caption-02" color="text-red01">
                ㅇㅇ 님이 틀렸던 유형이에요!
              </Text>
            )}
            <ProgressBar now={item.now} total={item.total} />
            <div className="mt-3 flex w-full gap-2">
              <Button size="medium" color="nonactive">
                다시 풀기
              </Button>

              {item.now ? (
                <Button size="medium" color="activeLight">
                  채점 결과 보기
                </Button>
              ) : (
                <Button size="medium" color="nonactive">
                  채점 결과 보기
                </Button>
              )}
            </div>
          </div>
        );
      })}
    </div>
  );
}
