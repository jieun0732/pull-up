"use client";

import Image from "next/image";
import { StaticImageData } from "next/image";
import { useParams, useRouter } from "next/navigation";
import BackIcon from "@/assets/icon/backIcon";
import dummyIcon from "@/assets/defaultImages/profile.png";
import ProgressBar from "@/component/sectional/progressbar";

const dummydata = {
  isFinished: true,
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
      type: "유의어",
      now: 0,
      total: 10,
      hasWrong: false,
    },
  ],
};
export default function Page() {
  interface SectionInfo {
    name: string;
    icon: StaticImageData;
  }

  const sections: Record<string, SectionInfo> = {
    language: {
      name: "언어영역",
      icon: dummyIcon,
    },
    reasoning: {
      name: "추리영역",
      icon: dummyIcon,
    },
    math: {
      name: "수리영역",
      icon: dummyIcon,
    },
    spatial: {
      name: "공간지각영역",
      icon: dummyIcon,
    },
  };

  const router = useRouter();
  const params = useParams<{ subject: string }>();
  const currentSection = sections[params.subject];

  return (
    <div className="flex flex-col items-center">
      <p className="relative mb-6 w-full text-center text-[17px] font-bold">
        <BackIcon onClick={() => router.back()} />
        {currentSection?.name}
      </p>
      <p className="self-start text-[19px] font-bold">
        {currentSection?.name}의 대표 예제를
      </p>
      <p className="self-start text-[19px] font-bold">
        다양하게 만나보고 싶다면?
      </p>

      <div className="flex w-full flex-col items-center gap-4 p-6">
        <Image
          className="h-28 w-28 rounded-full"
          src={currentSection?.icon}
          alt="Profile Image"
        />
        <button className="w-full rounded-md bg-blue01 py-3 text-white">
          골고루 학습하기
        </button>
        {dummydata.isFinished && (
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
                fill-rule="evenodd"
                clip-rule="evenodd"
                d="M5.03944 0.273312C5.33233 -0.019581 5.80721 -0.019581 6.1001 0.273312L9.67099 3.8442C9.7491 3.92231 9.7491 4.04894 9.67099 4.12704L8.7569 5.04113C8.75406 5.04398 8.74945 5.04398 8.7466 5.04113C8.74376 5.03829 8.73915 5.03829 8.73631 5.04113L6.05076 7.72668C5.75787 8.01957 5.28299 8.01957 4.9901 7.72668C4.69721 7.43379 4.69721 6.95891 4.9901 6.66602L7.10248 4.55364H0.570432C0.404746 4.55364 0.270432 4.41933 0.270432 4.25364V3.35364C0.270432 3.18796 0.404746 3.05364 0.570432 3.05364H6.75911L5.03944 1.33397C4.74655 1.04108 4.74655 0.566205 5.03944 0.273312Z"
                fill="#4D70EC"
              />
            </svg>
          </button>
        )}
      </div>

      <p className="mb-3 mt-14 self-start text-[17px]">
        필요한 유형만 학습할 수 있어요!
      </p>
      <p className="mb-4 self-start text-[15px] text-gray01">문제 유형 2</p>

      {dummydata.data.map((item) => {
        return (
          <div className="mb-4 w-full rounded-md border border-solid border-white03 bg-green-200 px-6 py-4 shadow-[2px_2px_20px_0px_rgba(0,0,0,0.07)]">
            <p className="mb-1 self-start text-[15px] font-semibold">
              {item.type} {item.now}/{item.total}
            </p>
            {item.hasWrong && (
              <p className="text-xs text-red01">ㅇㅇ 님이 틀렸던 유형이에요!</p>
            )}
            <ProgressBar now={item.now} total={item.total} />
            <div className="mt-3 flex w-full gap-2">
              <button className="w-[50%] whitespace-nowrap rounded-md bg-gray03 py-3 text-center text-gray02">
                다시 풀기
              </button>
              {item.now ? (
                <button className="w-[50%] whitespace-nowrap rounded-md bg-blue03 py-3 text-center text-blue01">
                  채점 결과 보기
                </button>
              ) : (
                <button className="w-[50%] whitespace-nowrap rounded-md bg-gray03 py-3 text-center text-gray02">
                  채점 결과 보기
                </button>
              )}
            </div>
          </div>
        );
      })}
    </div>
  );
}
