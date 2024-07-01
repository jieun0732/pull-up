"use client";

import Header from "@/component/ui/Header";
import Button from "@/component/ui/Button";
import { useState } from "react";

export default function Page() {
  const [activeCancel, setActiveCancel] = useState<boolean>(false);

  const userName = "홍길동";

  const term = [
    {
      title: `${userName}님의 모의고사 점수는 삭제되지 않아요!`,
      content:
        "모의고사 점수는 풀업에서 모의고사 전체 통계를 위해 사용되기 때문이에요.",
    },
    {
      title: `이외에 ${userName}님의 모든 정보는 바로 삭제되요!`,
      content: "다시 가입해도 이용내역은 복구되지 않아요.",
    },
  ];
  return (
    <div className="flex h-full w-full flex-col items-center bg-white px-5 pb-[91px] pt-14">
      <Header type="back" content="회원탈퇴" />
      <div className="w-full">
        <p className="mb-2 text-[19px] font-bold">
          정말 풀업을 탈퇴하실 건가요?
        </p>
        <p className="mb-9 text-[15px] font-medium text-gray01">
          탈퇴 전 oo님께서 확인하실 정보가 있어요!
        </p>
      </div>

      {term.map((item) => {
        return (
          <div
            key={item.title}
            className="mb-4 w-full rounded-md bg-gray03 px-9 py-4"
          >
            <li className="mb-1 text-[15px] font-medium"> {item.title}</li>
            <p className="pl-5 text-[13px] font-normal text-gray01">
              {item.content}
            </p>
          </div>
        );
      })}

      <div
        onClick={() => setActiveCancel(!activeCancel)}
        className="mt-11 flex w-full items-center justify-center gap-3"
      >
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="25"
          height="25"
          viewBox="0 0 25 25"
          fill="none"
        >
          <rect
            width="25"
            height="25"
            rx="5"
            fill={activeCancel ? "#4D70EC" : "#F2F3F6"}
          />
          <path
            d="M7 11L12.1333 17L18 8"
            stroke={activeCancel ? "white" : "#ACACAC"}
            stroke-width="3.2"
            stroke-linecap="round"
          />
        </svg>

        <p>안내 사항을 모두 확인하였고, 이에 동의해요.</p>
      </div>

      <Button
        size="large"
        color={activeCancel ? "active" : "nonactive"}
        customstyle="mt-auto"
      >
        탈퇴하기
      </Button>
    </div>
  );
}
