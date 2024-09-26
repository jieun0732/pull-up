"use client";
import SectionalResultItem from "@/component/sectional/sectionalResultItem";
import SectionalResultHeader from "@/component/sectional/sectionalResultHeader";
import { useRouter } from "next/navigation";
import CloseIcon from "@/assets/icon/CloseIcon";

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

  return (
    <div className="h-full w-full min-w-[320px] max-w-[450px] overflow-y-auto bg-[#ffffff] px-5 pb-7 pt-14 text-black01">
      <div className="relative mb-6 h-[25px] w-full text-center text-[17px] font-bold">
        <CloseIcon onClick={() => router.push(`/main/sectional/${subject}`)} />
      </div>
      {/* <SectionalResultHeader isFinished={false} params={params} /> */}
      {/* {dummyresult.map((item) => {
        return (
          <div key={item.id}>
            <SectionalResultItem item={item} params={params} />
          </div>
        );
      })} */}
    </div>
  );
}
