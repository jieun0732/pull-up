"use client";

import MyScoreAverage from "@/component/mockexam/MyScoreAverage";
import MyTimeAverage from "@/component/mockexam/MyTimeAverage";
import MyWeakPart from "@/component/mockexam/MyWeakPart";
import { useRouter } from "next/navigation";
import { CloseIcon } from "@/assets/icon/Icons";
import Button from "@/component/ui/Button";
 
export default function Page({
  params,
}: {
  params: {
    subject: string;
    type: string;
  };
}) {
  const router = useRouter();
  const { subject, type } = params;

  let name = "이지은";

  const handleExplanation = async () => {
    router.push('/해설확인하기');
  }

  return (
    <div className="h-full w-full min-w-[375px] min-h-[1297px] overflow-y-auto bg-[#F3F4F6] px-5 pb-7 pt-14">
      {/** 결과 창 닫기 + 사용자 모의고사 결과 문구 */}
      <div className="relative mb-6 h-[25px] w-full text-center text-[17px] font-bold text-black01">
        <CloseIcon onClick={() => router.push(`/main/sectional/${subject}`)} />
          {name} 님의 모의고사 결과
      </div>
          {/** 사용자 평균 점수 결과 */}
          <MyScoreAverage />
          {/** 사용자 평균 시간 결과 */}
          <MyTimeAverage />
          {/** 사용자 약점 결과 */}
          <MyWeakPart />
          {/** 해설 확인하기 버튼 */}
          <Button
            size="large"
            color="active"
            className="mt-4"
            onClick={handleExplanation}
          >
            해설 확인하기 
          </Button>
    </div>
  );
}
