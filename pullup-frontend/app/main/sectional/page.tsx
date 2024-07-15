"use client";

import Link from "next/link";
import Button from "@/component/ui/Button";
import Text from "@/component/ui/Text";
import Image from "next/image";
import {
  language,
  reasoning,
  math,
  spatial,
} from "@/assets/defaultImages/sectional";

export default function Page() {
  const sections = [
    {
      name: "언어영역",
      link: "/main/sectional/language",
      isClosed: false,
      img: language,
    },
    {
      name: "추리영역",
      link: "/main/sectional/reasoning",
      isClosed: false,
      img: reasoning,
    },
    {
      name: "수리영역",
      link: "/main/sectional/math",
      isClosed: false,
      img: math,
    },
    {
      name: "공간지각영역",
      link: "/main/sectional/spatial",
      isClosed: true,
      img: spatial,
    },
  ];
  return (
    <div className="bg-white px-5 pb-[91px] pt-14">
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
            <div className="absolute inset-0 overflow-hidden rounded-md">
              <Image
                src={item.img}
                alt={item.name}
                className="h-full w-full object-cover"
              />
            </div>
            <Button
              size="small"
              color="activeLight"
              className="absolute bottom-4 left-5 z-10"
            >
              {item.name}
            </Button>
          </Link>
        );
      })}
    </div>
  );
}
