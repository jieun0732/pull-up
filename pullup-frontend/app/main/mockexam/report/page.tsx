"use client";

import Link from "next/link";
import Header from "@/component/ui/Header";
import MyScoreAverage from "@/component/mockexam/MyScoreAverage";
import MyTimeAverage from "@/component/mockexam/MyTimeAverage";
import MyWeakPart from "@/component/mockexam/MyWeakPart";
import Button from "@/component/ui/Button";

export default function Page() {
  return (
    <div className="h-full w-full min-w-[320px] max-w-[450px] overflow-y-auto bg-[#f3f4f6] px-5 pb-7 pt-14 text-black01">
      <Header
        type="cancel"
        content="이지은 님의 모의고사"
        link="/main/mockexam"
      />
      <MyScoreAverage />

      <MyTimeAverage />

      <MyWeakPart />

      <Link href="/main/mockexam/result" className="mt-6">
        <Button size="large" color="active">
          해설 확인하기
        </Button>
      </Link>
    </div>
  );
}
