"use client";
import Text from "@/component/ui/Text";
import { dummyresult } from "@/constants/dummyresult";
import ResultItem from "@/component/ui/ResultItem";
import BackIcon from "@/assets/icon/backIcon";
import { useParams, useRouter } from "next/navigation";

export default function Page() {
  const router = useRouter();
  const params = useParams<{ subject: string }>();

  return (
    <div className="h-full w-full min-w-[320px] max-w-[450px] overflow-y-auto bg-[#ffffff] px-5 pb-7 pt-14 text-black01">
      <div className="relative mb-6 h-[25px] w-full text-center text-[17px] font-bold">
        <BackIcon
          onClick={() => router.push(`/main/sectional/${params.subject}`)}
        />
      </div>
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
