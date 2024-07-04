import Text from "../ui/Text";
import Button from "../ui/Button";
import ProgressBar from "react-customizable-progressbar";
import { compareTime } from "@/utils/compareFunc";
import useComponentSize from "@/hooks/useComponentSize";

function MyTimeAverage() {
  const mytime = 15;
  const averageTime = 15;
  const status = compareTime(mytime, averageTime);
  const [componentRef, size] = useComponentSize();

  const scoreStatus = {
    higher: "평균보다 3분 느려요",
    same: "평균이에요",
    lower: "평균보다 3분 빨라요!",
  };

  return (
    <div className="mt-5 flex w-full flex-col rounded-2xl bg-white p-6">
      <Text size="head-03" className="mb-2">
        소요시간
      </Text>
      <Text size="caption-02" color="text-gray01">
        제한 시간은 총 20분이였어요.
      </Text>
      <Text size="caption-02" color="text-gray01">
        이지은 님은 15분 만에 모든 문제를 풀어냈어요!
      </Text>
      <div>
        <Button size="small" color="activeLight" className="ml-10 mt-5">
          {scoreStatus[status]}
        </Button>
        <div className="flex w-full justify-around" ref={componentRef}>
          <div className="relative flex flex-col items-center">
            <ProgressBar
              progress={(mytime / 20) * 100}
              radius={size.width / 4 - 35}
              inverse={false}
              rotate={180 + 90}
              strokeColor="#4d70ec"
              strokeWidth={14}
              trackStrokeColor="#F2F3F6"
              trackStrokeWidth={14}
              strokeLinecap={"square"}
              counterClockwise={true}
            />
            <Text
              size="head-04"
              className="absolute top-[37%] flex h-full w-full select-none justify-center text-center"
            >
              15분
            </Text>
            <Text
              size="caption-02"
              color="text-gray01"
              className="select-none justify-center text-center"
            >
              이지은 님
            </Text>
          </div>
          <div className="relative flex flex-col items-center">
            <ProgressBar
              progress={(averageTime / 20) * 100}
              radius={size.width / 4 - 35}
              inverse={false}
              rotate={180 + 90}
              strokeColor="#3D4150"
              strokeWidth={14}
              trackStrokeColor="#F2F3F6"
              trackStrokeWidth={14}
              strokeLinecap={"square"}
              counterClockwise={true}
            />
            <Text
              size="head-04"
              className="absolute top-[37%] flex h-full w-full select-none justify-center text-center"
            >
              15분
            </Text>
            <Text
              size="caption-02"
              color="text-gray01"
              className="select-none justify-center text-center"
            >
              평균
            </Text>
          </div>
        </div>
      </div>
    </div>
  );
}

export default MyTimeAverage;
