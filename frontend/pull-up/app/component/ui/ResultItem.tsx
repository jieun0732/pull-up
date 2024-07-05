"use client";

import { useRouter } from "next/navigation";
import Text from "./Text";
import Button from "./Button";
import WrongIcon from "@/assets/icon/problem/wrongIcon";
import CorrectIcon from "@/assets/icon/problem/correctIcon";
import ActiveQIcon from "@/assets/icon/problem/activeQIcon";
import NonActiveQIcon from "@/assets/icon/problem/nonActiveQIcon";
import formatNumber from "@/utils/formatNumber";

type ItemType = {
  id: number;
  type: string;
  qtype: "nonactive" | "active" | "wrong" | "right";
};

interface ResultItemPropType {
  item: ItemType;
}

const ItemStyle: {
  [key: string]: {
    icon: React.FC;
    buttontype: "red" | "green" | "active" | "nonactive";
    link: string;
    text: string;
  };
} = {
  wrong: {
    icon: WrongIcon,
    buttontype: "red",
    link: "/main/mocktest/solution",
    text: "해설 확인하기",
  },

  right: {
    icon: CorrectIcon,
    buttontype: "green",
    link: "/main/mocktest/solution",
    text: "해설 확인하기",
  },

  active: {
    icon: ActiveQIcon,
    buttontype: "active",
    link: "/main/mocktest/solution",
    text: "이어서 풀기",
  },
  nonactive: {
    icon: NonActiveQIcon,
    buttontype: "nonactive",
    link: "/main/mocktest/report",
    text: "이어서 풀기",
  },
};

export default function ResultItem({ item }: ResultItemPropType) {
  const router = useRouter();

  const nowItemStyle = ItemStyle[item.qtype];

  return (
    <div className="mb-4 w-full rounded-md border-2 border-solid border-white03 bg-white px-6 py-4 shadow-[2px_2px_20px_0px_rgba(0,0,0,0.07)]">
      <div className="flex gap-2">
        <nowItemStyle.icon />
        <div className="mb-2">
          <Text size="head-04">문제 {formatNumber(item.id)} </Text>
          <Text size="caption-01" color="text-gray02">
            문제 {formatNumber(item.id)}{" "}
          </Text>
        </div>
      </div>
      <Button size="large" color={nowItemStyle.buttontype}>
        {nowItemStyle.text}
      </Button>
    </div>
  );
}
