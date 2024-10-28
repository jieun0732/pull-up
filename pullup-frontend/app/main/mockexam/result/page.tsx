"use client";
import Text from "@/component/ui/Text";
import SectionalResultItem from "@/component/sectional/sectionalResultItem";
import { useRouter } from "next/navigation";
import { CloseIcon } from "@/assets/icon/Icons";
import { entryMap, categoryMap } from "@/constants/constants";
import useSWR from "swr";
import { ProblemInfo } from "@/types/problemType";
import { API, fetcher } from "@/lib/API";
import LocalStorage from "@/utils/LocalStorage";
import { User } from "@/types/userType";

export default function Page() {
  // const router = useRouter();

  // const memberID = LocalStorage.getItem("memberId") || "";

  // const entry = entryMap[params.subject];
  // const categoryKor = categoryMap[params.category];

  // let type = "";

  // if (categoryKor !== "골고루") {
  //   type = localStorage.getItem("type") || "";
  // }

  // const queryString = new URLSearchParams({
  //   memberId: memberID,
  //   entry,
  //   category: categoryKor,
  //   type,
  // }).toString();

  // const { data: problems } = useSWR<ProblemInfo[]>(
  //   `${API}/exams/problems?${queryString}`,
  //   fetcher,
  // );

  // const { data: user } = useSWR<User>(`${API}/members/${memberID}`, fetcher);

  // if (!problems || !user) return;

  // let nullCount = 0;

  return (
    <div className="h-full w-full min-w-[320px] max-w-[450px] overflow-y-auto bg-[#ffffff] px-5 pb-7 pt-20 text-black01">
      {/* <div className="relative mb-6 h-[25px] w-full text-center text-[17px] font-bold">
        <CloseIcon onClick={() => router.push(`/main/mockexam/report`)} />
      </div>

      <Text size="head-02" className="self-start">
        {user.data.name}님의 모의고사 점수는
      </Text>
      <Text size="head-02" className="self-start">
        ㅇㅇ 점에요!
      </Text>
      <Text size="head-05" color="text-gray01" className="mb-6 mt-2 self-start">
        총 20문제 중 ㅇㅇ문제를 맞췄어요!
      </Text>

      {problems.map((item, idx) => {
        const isNonActive =
          item.chosenAnswer === null ? ++nullCount > 1 : nullCount === 0;

        return (
          <div key={item.id}>
            <SectionalResultItem
              problemNum={idx + 1}
              item={item}
              params={params}
              isNonActive={isNonActive}
            />
          </div>
        );
      })} */}
    </div>
  );
}
