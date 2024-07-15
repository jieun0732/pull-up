"use client";
import Text from "@/component/ui/Text";
import Header from "@/component/ui/Header";
import { dummyresult } from "@/constants/dummyresult";
import ResultItem from "@/component/ui/ResultItem";

export default function Page() {
  return (
    <div className="h-full w-full min-w-[320px] max-w-[450px] overflow-y-auto bg-[#ffffff] px-5 pb-7 pt-14 text-black01">
      <Header type="back" content="" />
      <Text size="head-02" className="self-start">
        이지은님의 모의고사 점수는
      </Text>
      <Text size="head-02" className="self-start">
        85점이예요!
      </Text>
      <Text size="head-05" color="text-gray01" className="mb-6 mt-2 self-start">
        총 20문제 중 17문제를 맞췄어요!
      </Text>
      {dummyresult.map((item) => {
        return (
          <div key={item.id}>
            <ResultItem item={item} />
          </div>
        );
      })}
    </div>
  );
}
