"use client";

import Link from "next/link";
import Button from "@/component/ui/Button";
import Text from "@/component/ui/Text";

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
      <Text size="head-02">내가 필요한 부분만</Text>
      <Text size="head-02"> 골라서 풀어볼 수 있어요!</Text>
      <Text size="head-05" color="text-gray01" className="mb-[26px]">
        나의 취약한 영역을 공략해 효율적으로 학습해요
      </Text>
      {sections.map((item) => {
        return (
          <Link
            key={item.name}
            href={item.link}
            className="relative mb-4 flex h-[138px] w-full items-center justify-center rounded-md border border-solid border-white03 bg-white02"
          >
            <Button
              size="small"
              color="activeLight"
              className="absolute bottom-4 left-5"
            >
              {item.name}
            </Button>
            {item.isClosed && <div>coming soon</div>}
          </Link>
        );
      })}
    </div>
  );
}
