import Text from "../ui/Text";
import { MockExamWeakPartType } from "@/types/mockexam/mockexamReport";
import useSWR from "swr";
import { API, fetcher } from "@/lib/API";

const dummyWeak = [
  {
    name: "언어영역",
    total: 10,
    correct: 5,
    weakest: true,
  },
  {
    name: "추리영역",
    total: 10,
    correct: 3,
    weakest: false,
  },
  {
    name: "수리영역",
    total: 10,
    correct: 2,
    weakest: false,
  },
];

function MyWeakPart() {
  const weakest = "수리영역";
  const memberID = localStorage.getItem("memberId") || "";

  const { data: averageScore } = useSWR<MockExamWeakPartType>(
    `${API}/exams/mock-exam/recent/${memberID}`,
    fetcher,
  );

  if (!averageScore) {
    return <div>데이터를 불러오는 중입니다...</div>;
  }

  if (averageScore) {
    console.log("모든", averageScore);
    console.log("모든", averageScore.problemTypeResults);

  } else {
    console.log("데이터를 불러오지 못했습니다.");
  }

  return (
    <div className="my-5 flex w-full flex-col rounded-2xl bg-white p-6">
      <Text size="head-04">가장 취약한 파트는</Text>
      <Text size="head-02" className="inline">
        <span className="text-red01"> {weakest}</span>이에요!
      </Text>
      <Text size="caption-02" color="text-gray01" className="mt-1">
        * 오답 개수 기준이에요.
      </Text>
      <div className="flex w-full justify-around">
        {averageScore.problemTypeResults.map((item) => {
          const height = `${(item.correctProblems / item.totalProblems) * 100}%`;
          return (
            <>
              <div
                key={item.entry}
                className="flex w-14 flex-col items-center justify-end gap-1"
              >
                {/* {item.weakest && (
                  <div className="relative mb-1 mt-6 flex h-7 w-14 items-center justify-center rounded-[.4em] bg-[#3d4150] text-center text-[11px] text-white after:absolute after:bottom-0 after:left-1/2 after:mb-[-6px] after:ml-[-7px] after:h-0 after:w-0 after:border-[7px] after:border-b-0 after:border-transparent after:border-t-[#3d4150] after:content-['']">
                    취약파트
                  </div>
                )} */}
                <Text size="caption-02" color="text-gray01">
                  {item.correctProblems}/{item.totalProblems}
                </Text>
                <div className="relative h-32 w-8 rounded-md bg-red02">
                  <div
                    className={`absolute bottom-0 left-0 z-20 w-8 rounded-md bg-red01`}
                    style={{ height }}
                  ></div>
                </div>
                <Text size="caption-02" color="text-gray01">
                  {item.entry}
                </Text>
              </div>
            </>
          );
        })}
      </div>
    </div>
  );
}

export default MyWeakPart;