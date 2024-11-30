"use client";

import Header from "@/component/ui/Header";
import Button from "@/component/ui/Button";
import Text from "@/component/ui/Text";
import useSWR from "swr";
import { API, fetcher } from "@/lib/API";
import { useRouter } from "next/navigation";
import { entryMap, categoryMap } from "@/constants/constants";
import { IncorrectProblems } from "@/types/problemType";
import useUserStore from "@/stores/useUserStore";
import useProblemStore from "@/stores/useProblemStore";
import Spinner from "@/component/ui/Spinner";

export default function Page() {
  const { user } = useUserStore();
  const { setExamId } = useProblemStore();
  const { data, error } = useSWR<IncorrectProblems>(
    `${API}/members/incorrect/${user.memberId}`,
    fetcher,
  );
  const router = useRouter();

  return (
    <>
      {data ? (
        <div className="flex h-full w-full flex-col items-center bg-[#F4F3F8] px-5 pb-[91px] pt-20">
          <Header type="back" content="내가 틀린 문제" link="/main/profile" />
          <div className="flex h-full w-full flex-col overflow-x-scroll">
            {data?.list.length === 0 ? (
              <p className="my-auto w-full text-center">
                아직 틀린 문제가 없어요!
              </p>
            ) : (
              data.list.map((item, idx) => {
                return (
                  <div
                    key={idx}
                    className="mb-4 w-full rounded-lg bg-white px-4 py-5"
                    onClick={() => {
                      setExamId(item.examId);
                      router.push(`/main/profile/wrong/${item.problemNumber}`);
                    }}
                  >
                    <div className="mb-4 flex w-full items-center gap-[7px]">
                      <Button size="small" color="activeLight">
                        {entryMap[item.entry]}영역
                      </Button>
                      <Button size="small" color="nonactive">
                        {categoryMap[item.examType]} 학습
                      </Button>
                      <Button size="small" color="activeLight">
                        {item.problemNumber}번
                      </Button>
                      <Text
                        size="caption-01"
                        color="text-gray02"
                        className="ml-auto"
                      >
                        {item.solvedDate}
                      </Text>
                    </div>
                    <div className="line-clamp-2 w-full overflow-hidden text-ellipsis whitespace-normal">
                      {item.questionSubstring}
                    </div>
                  </div>
                );
              })
            )}
          </div>
        </div>
      ) : (
        <Spinner />
      )}
    </>
  );
}
