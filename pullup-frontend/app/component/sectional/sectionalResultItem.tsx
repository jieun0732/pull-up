"use client";

import { useRouter } from "next/navigation";
import Text from "../ui/Text";
import Button from "../ui/Button";
import WrongIcon from "@/assets/icon/problem/wrongIcon";
import CorrectIcon from "@/assets/icon/problem/correctIcon";
import ActiveQIcon from "@/assets/icon/problem/activeQIcon";
import NonActiveQIcon from "@/assets/icon/problem/nonActiveQIcon";
import formatNumber from "@/utils/formatNumber";

type ButtonType = "red" | "green" | "active" | "nonactive";
type QuestionType = "nonactive" | "active" | "wrong" | "right";
interface ItemProps {
  icon: React.FC;
  buttontype: ButtonType;
  link: string;
  text: string;
}

const getItemStyle = (
  id: number,
  qtype: QuestionType,
  subject: string,
  type: string,
) => {
  const itemStyles: { [key: string]: ItemProps } = {
    wrong: {
      icon: WrongIcon,
      buttontype: "red",
      link: `/main/sectional/${subject}/${type}/result/${id}`,
      text: "해설 확인하기",
    },
    right: {
      icon: CorrectIcon,
      buttontype: "green",
      link: `/main/sectional/${subject}/${type}/result/${id}`,
      text: "해설 확인하기",
    },
    active: {
      icon: ActiveQIcon,
      buttontype: "active",
      link: `/main/sectional/${subject}/${type}/${id}`,
      text: "이어서 풀기",
    },
    nonactive: {
      icon: NonActiveQIcon,
      buttontype: "nonactive",
      link: `/main/sectional/${subject}/${type}/result`,
      text: "이어서 풀기",
    },
  };

  return itemStyles[qtype];
};

interface QItemType {
  id: number;
  type: string;
  qtype: "nonactive" | "active" | "wrong" | "right";
}

interface ItemPropType1 {
  item: QItemType;
  params: {
    subject: string;
    type: string;
  };
}

export default function SectionalResultItem({ item, params }: ItemPropType1) {
  const router = useRouter();
  const { subject, type } = params;
  const nowItemStyle = getItemStyle(item.id, item.qtype, subject, type);

  return (
    <div className="mb-4 w-full rounded-md border-2 border-solid border-white03 bg-white px-6 py-4 shadow-[2px_2px_20px_0px_rgba(0,0,0,0.07)]">
      <div className="flex gap-2">
        <nowItemStyle.icon />
        <div className="mb-2">
          <Text size="head-04">문제 {formatNumber(item.id)} </Text>
          <Text size="caption-01" color="text-gray02">
            {item.type}
          </Text>
        </div>
      </div>
      <Button
        size="large"
        color={nowItemStyle.buttontype}
        onClick={() => router.push(nowItemStyle.link)}
      >
        {nowItemStyle.text}
      </Button>
    </div>
  );
}
