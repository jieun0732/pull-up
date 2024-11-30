"use client";

import Link from "next/link";
import Header from "@/component/ui/Header";
import MyScoreAverage from "@/component/mockexam/MyScoreAverage";
import MyTimeAverage from "@/component/mockexam/MyTimeAverage";
import MyWeakPart from "@/component/mockexam/MyWeakPart";
import Button from "@/component/ui/Button";
import LocalStorage from "@/utils/LocalStorage";
import { MockExamReportType } from "@/types/mockexam/mockexamReport";
import { API, fetcher } from "@/lib/API";
import useSWR from "swr";
import Spinner from "@/component/ui/Spinner";
import useExamStore from "@/stores/useExamStore";
import useUserStore from "@/stores/useUserStore";

export default function Page() {
  const { examId } = useExamStore();
  const { user } = useUserStore();

  const { data, isLoading } = useSWR<MockExamReportType>(
    `${API}/exams/mock-exam/report/${examId}`,
    fetcher,
  );

  console.log(data);

  return (
    <>
      {data ? (
        <div className="h-full w-full min-w-[320px] max-w-[450px] overflow-y-auto bg-[#f3f4f6] px-5 pb-7 pt-20 text-black01">
          <Header
            type="cancel"
            content={`${user.name} 님의 모의고사`}
            link="/main/mockexam"
          />
          <MyScoreAverage data={data} />
          <MyTimeAverage data={data} />
          {/* <MyWeakPart data={data} /> */}
          <Link href="/main/mockexam/result" className="mt-6">
            <Button size="large" color="active">
              해설 확인하기
            </Button>
          </Link>
        </div>
      ) : (
        <Spinner />
      )}
    </>
  );
}
