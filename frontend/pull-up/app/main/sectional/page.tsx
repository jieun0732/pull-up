"use client";

import Link from "next/link";

export default function Page() {
  const sections = [
    {
      name: "언어영역",
      link: "/main/sectional/language",
      isClosed: false,
    },
    {
      name: "추리영역",
      link: "/main/sectional/reasoning",
      isClosed: false,
    },
    {
      name: "수리영역",
      link: "/main/sectional/math",
      isClosed: false,
    },
    {
      name: "공간지각영역",
      link: "/main/sectional/spatial",
      isClosed: true,
    },
  ];
  return (
    <div className="bg-white pb-[91px]">
      <p className="text-[19px] font-bold text-black01">내가 필요한 부분만</p>
      <p className="text-[19px] font-bold text-black01">
        골라서 풀어볼 수 있어요!
      </p>
      <p className="mb-[26px] text-base text-gray01">
        나의 취약한 영역을 공략해 효율적으로 학습해요
      </p>

      {sections.map((item) => {
        return (
          <Link
            href={item.link}
            className="relative mb-4 flex h-[138px] w-full items-center justify-center rounded-md border border-solid border-white03 bg-red-100"
          >
            <p className="absolute bottom-4 left-5 rounded-sm bg-blue03 px-2 py-1 text-blue01">
              {item.name}
            </p>
            {item.isClosed && <div>coming soon</div>}
          </Link>
        );
      })}
    </div>
  );
}
