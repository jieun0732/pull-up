"use client";
import Text from "@/component/ui/Text";
import { dummyresult } from "@/constants/dummyresult";
import SectionalResultItem from "@/component/sectional/sectionalResultItem";
import SectionalResultHeader from "@/component/sectional/sectionalResultHeader";
import { useRouter } from "next/navigation";
import CloseIcon from "@/assets/icon/CloseIcon";
import { entryMap, categoryMap } from "@/constants/constants";
import useSWR from "swr";
import { ProblemInfo } from "@/types/problemType";
import { API, fetcher } from "@/lib/API";

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

  const memberID = 1;
  const entry = entryMap[params.subject];
  const category = categoryMap[params.type];

  const queryString = new URLSearchParams({
    memberId: memberID.toString(),
    entry,
    category,
  }).toString();

  const { data: problems } = useSWR<ProblemInfo[]>(
    `${API}/exams/problems?${queryString}`,
    fetcher,
  );

  if (!problems) return;

  console.log(problems);

  return (
    <div className="h-full w-full min-w-[320px] max-w-[450px] overflow-y-auto bg-[#ffffff] px-5 pb-7 pt-14 text-black01">
      <div className="relative mb-6 h-[25px] w-full text-center text-[17px] font-bold">
        <CloseIcon onClick={() => router.push(`/main/sectional/${subject}`)} />
      </div>
      <SectionalResultHeader isFinished={false} params={params} />
      {problems.map((item) => {
        return (
          <div key={item.id}>
            <SectionalResultItem item={item} params={params} />
          </div>
        );
      })}
    </div>
  );
}
