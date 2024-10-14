"use client";
import Text from "@/component/ui/Text";
import SectionalResultItem from "@/component/sectional/sectionalResultItem";
import SectionalResultHeader from "@/component/sectional/sectionalResultHeader";
import { useRouter } from "next/navigation";
import { CloseIcon } from "@/assets/icon/Icons";
import { entryMap, categoryMap } from "@/constants/constants";
import useSWR from "swr";
import { ProblemInfo } from "@/types/problemType";
import { API, fetcher } from "@/lib/API";

export default function Page({
  params,
}: {
  params: {
    subject: string;
    category: string;
  };
}) {
  const router = useRouter();
  const { subject, category } = params;

  const memberID = localStorage.getItem("memberId") || "";

  const entry = entryMap[params.subject];
  const categoryKor = categoryMap[params.category];

  let type = ""

  if (categoryKor !== "골고루") {
    type = localStorage.getItem('type') || ""
  }


  const queryString = new URLSearchParams({
    memberId: memberID,
    entry,
    category: categoryKor,
    type,
  }).toString();

  const { data: problems } = useSWR<ProblemInfo[]>(
    `${API}/exams/problems?${queryString}`,
    fetcher,
  );

  if (!problems) return;

  let nullCount = 0;

  return (
    <div className="h-full w-full min-w-[320px] max-w-[450px] overflow-y-auto bg-[#ffffff] px-5 pb-7 pt-14 text-black01">
      <div className="relative mb-6 h-[25px] w-full text-center text-[17px] font-bold">
        <CloseIcon onClick={() => router.push(`/main/sectional/${subject}`)} />
      </div>
      <SectionalResultHeader problems={problems} params={params} />
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
      })}
    </div>
  );
}