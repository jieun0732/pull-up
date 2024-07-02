"use client";

import Header from "@/component/ui/Header";

export default function Page() {
  return (
    <div className="h-full w-full min-w-[320px] max-w-[450px] overflow-y-auto bg-yellow-200 px-5 pb-7 pt-14 text-black01">
      <Header
        type="cancel"
        content="이지은 님의 모의고사"
        link="/main/mocktest"
      />

      <div className="flex w-full flex-col rounded-2xl bg-pink-100 p-6">
        <p>우수한 합격권이예요!</p>
      </div>
    </div>
  );
}
