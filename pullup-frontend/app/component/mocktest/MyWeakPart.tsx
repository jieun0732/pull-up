import Text from "../ui/Text";

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
  return (
    <div className="mt-5 flex w-full flex-col rounded-2xl bg-white p-6">
      <Text size="head-04">가장 취약한 파트는</Text>
      <Text size="head-02" className="inline">
        <span className="text-red01"> {weakest}</span>이에요!
      </Text>
      <Text size="caption-02" color="text-gray01" className="mt-1">
        * 오답 개수 기준이에요.
      </Text>
      <div className="flex w-full justify-around">
        {dummyWeak.map((item) => {
          const height = `${(item.correct / item.total) * 100}%`;
          return (
            <>
              <div
                key={item.name}
                className="flex w-14 flex-col items-center justify-end gap-1"
              >
                {item.weakest && (
                  <div className="relative mb-1 mt-6 flex h-7 w-14 items-center justify-center rounded-[.4em] bg-[#3d4150] text-center text-[11px] text-white after:absolute after:bottom-0 after:left-1/2 after:mb-[-6px] after:ml-[-7px] after:h-0 after:w-0 after:border-[7px] after:border-b-0 after:border-transparent after:border-t-[#3d4150] after:content-['']">
                    취약파트
                  </div>
                )}
                <Text size="caption-02" color="text-gray01">
                  {item.correct}/{item.total}
                </Text>
                <div className="relative h-32 w-8 rounded-md bg-red02">
                  <div
                    className={`absolute bottom-0 left-0 z-20 w-8 rounded-md bg-red01`}
                    style={{ height }}
                  ></div>
                </div>
                <Text size="caption-02" color="text-gray01">
                  {item.name}
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
