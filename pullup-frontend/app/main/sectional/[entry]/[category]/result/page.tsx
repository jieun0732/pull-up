"use client";

import Text from "@/component/ui/Text";
import SectionalResultItem from "@/component/sectional/sectionalResultItem";
import SectionalResultHeader from "@/component/sectional/sectionalResultHeader";
import { useRouter } from "next/navigation";
import { CloseIcon } from "@/assets/icon/Icons";
import useSWR from "swr";
import { SectionalResultResponseType } from "@/types/sectionalType";
import { API, patchFetcher } from "@/lib/API";
import useProblemStore from "@/stores/useProblemStore";
import useUserStore from "@/stores/useUserStore";
import Spinner from "@/component/ui/Spinner";
import { Entry } from "@/types/problemType";
export default function Page({
  params,
}: {
  params: {
    entry: Entry;
    category: "EVENLY" | "BY_PROBLEM_TYPE";
  };
}) {
  const router = useRouter();
  const { examId } = useProblemStore();
  const { user } = useUserStore();

  const { data } = useSWR<SectionalResultResponseType>(
    examId ? `${API}/exams/end/${examId}` : null,
    patchFetcher,
  );

  console.log("result data", data);
  return (
    <>
      {data ? (
        <div className="h-full w-full min-w-[320px] max-w-[450px] overflow-y-auto bg-[#ffffff] px-5 pb-7 pt-20 text-black01">
          <div className="relative mb-6 h-[25px] w-full text-center text-[17px] font-bold">
            <CloseIcon
              onClick={() => router.push(`/main/sectional/${data.entry}`)}
            />
          </div>
          <SectionalResultHeader
            data={data}
            user={user}
            category={params.category}
          />
          {data.results.map((item, idx) => {
            return (
              <div key={item.problemNumber} className="mt-3">
                <SectionalResultItem
                  entry={data.entry}
                  category={params.category}
                  lastQuestionNumber={
                    data.totalProblemCount - data.leftProblemCount + 1
                  }
                  item={item}
                />
              </div>
            );
          })}
        </div>
      ) : (
        <Spinner />
      )}
    </>
  );
}
