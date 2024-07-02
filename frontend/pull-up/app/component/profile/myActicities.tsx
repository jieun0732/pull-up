import Link from "next/link";
import Image from "next/image";
import wrongQuestionIcon from "@/assets/icon/wrong-questions.png";
import DepthIcon from "@/assets/icon/depthIcon";
import Text from "../ui/Text";

interface MyActivitiesProp {
  testScore: String;
}

export default function MyActivities({ testScore }: MyActivitiesProp) {
  return (
    <div>
      <Text size="body-01" customstyle="mb-2">
        나의 활동
      </Text>
      <div className="flex w-full gap-3">
        <Link
          href="/main/profile/wrong"
          className="flex w-3/5 flex-col gap-3 rounded-lg bg-white px-3 py-4 shadow-[1px_1px_15px_0px_rgba(0,0,0,0.03)]"
        >
          <Image src={wrongQuestionIcon} alt="틀린 문제 확인하기" />
          <div className="flex items-center justify-between text-base">
            <Text size="head-04" customstyle="whitespace-nowrap">
              내가 틀린 문제 확인
            </Text>
            <DepthIcon />
          </div>
        </Link>

        <Link
          href="#"
          className="flex w-2/5 flex-col justify-between gap-2 rounded-lg bg-white px-3 py-4 shadow-[1px_1px_15px_0px_rgba(0,0,0,0.03)]"
        >
          <div className="flex flex-col gap-2">
            <div>
              <Text size="head-04">내 최근</Text>
              <Text size="head-04">모의고사 점수</Text>
            </div>
            <Text size="head-02" color="text-blue01">
              {testScore}점
            </Text>
          </div>
        </Link>
      </div>
    </div>
  );
}
