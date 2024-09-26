"use client";

import Header from "@/component/ui/Header";
import Button from "@/component/ui/Button";
import Text from "@/component/ui/Text";
import useSWR from "swr";
import { IncorrectAnswers } from "@/types/problemType";
import { API, fetcher } from "@/lib/API";
import { useRouter } from "next/navigation";
import { formatDate } from "@/utils/formatDate";
import LocalStorage from "@/utils/localStorage";

export default function Page() {
  const { data , error } = useSWR<IncorrectAnswers[]>(`${API}/exams/incorrect-answers?memberId=${LocalStorage.getItem("memberId")}`, fetcher);
  const router = useRouter()
  
  if (!data) return
  console.log(data)
  return (
    <div className="flex h-full w-full flex-col items-center bg-[#F4F3F8] px-5 pb-[91px] pt-14">
      <Header type="back" content="내가 틀린 문제" link="/main/profile" />
      <div className="flex w-full h-full flex-col overflow-x-scroll">
        {data.length === 0 ? (
          <p className="w-full text-center my-auto">아직 푼 문제가 없어요!</p>
        ) : (
          data.map((item) => {
            return (
              <div
                key={item.id}
                className="mb-4 w-full rounded-lg bg-white px-4 py-5"
                onClick={() => router.push(`/main/profile/wrong/${item.id}`)}
              >
                <div className="mb-4 flex w-full items-center gap-[7px]">
                  <Button size="small" color="activeLight">
                    {item.problem.entry}영역
                  </Button>
                  <Button size="small" color="nonactive">
                    {item.problem.category} 학습
                  </Button>
                  <Button size="small" color="activeLight">
                    {item.id}번
                  </Button>
                  <Text
                    size="caption-01"
                    color="text-gray02"
                    className="ml-auto"
                  >
                    {formatDate(item.incorrectTime)}
                  </Text>
                </div>
                <div className="line-clamp-2 w-full overflow-hidden text-ellipsis whitespace-normal">
                  {item.problem.question}
                </div>
              </div>
            );
          })
        )}
      </div>
    </div>
  );
}
