import Link from "next/link";
import Image from "next/image";
import wrongQuestionIcon from "@/assets/icon/wrong-questions.png";
import DepthIcon from "@/assets/icon/depthIcon";

interface MyActivitiesProp {
  testScore: String;
}

export default function MyActivities({ testScore }: MyActivitiesProp) {
  return (
    <div>
      <h2 className="font-lg mb-2 font-semibold">나의 활동</h2>
      <div className="flex w-full gap-3">
        <Link
          href="/main/profile/wrong"
          className="flex w-3/5 flex-col gap-2 rounded-lg bg-white px-3 py-4 shadow-[1px_1px_15px_0px_rgba(0,0,0,0.03)]"
        >
          <Image src={wrongQuestionIcon} alt="틀린 문제 확인하기" />
          <div className="flex items-center justify-between text-base">
            <p className="whitespace-nowrap">내가 틀린 문제 확인하기</p>
            <DepthIcon />
          </div>
        </Link>

        <Link
          href="#"
          className="flex w-2/5 flex-col justify-between gap-2 rounded-lg bg-white px-3 py-4 shadow-[1px_1px_15px_0px_rgba(0,0,0,0.03)]"
        >
          <div className="flex flex-col gap-1">
            <div>
              <p>내 최근</p>
              <p>모의고사 점수</p>
            </div>
            <p className="text-lg font-bold text-blue01">{testScore}점</p>
          </div>
        </Link>
      </div>
    </div>
  );
}
