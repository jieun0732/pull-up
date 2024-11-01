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

export default function Page() {
  const memberID = LocalStorage.getItem("memberId") || "";

  const { data: recentReportInfo, isLoading } = useSWR<MockExamReportType>(
    `${API}/exams/mock-exam/recent?memberId=${memberID}`,
    fetcher,
  );

  if (isLoading) return <Spinner />;

  return (
    <div className="h-full w-full min-w-[320px] max-w-[450px] overflow-y-auto bg-[#f3f4f6] px-5 pb-7 pt-20 text-black01">
      <Header
        type="cancel"
        content="이지은 님의 모의고사"
        link="/main/mockexam"
      />
      {recentReportInfo && (
        <>
          <MyScoreAverage recentReportInfo={recentReportInfo} />
          <MyTimeAverage recentReportInfo={recentReportInfo} />
          <MyWeakPart recentReportInfo={recentReportInfo} />
        </>
      )}

      <Link href="/main/mockexam/result" className="mt-6">
        <Button size="large" color="active">
          해설 확인하기
        </Button>
      </Link>
    </div>
  );
}
