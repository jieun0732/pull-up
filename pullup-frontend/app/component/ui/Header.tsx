"use client";

import { useRouter } from "next/navigation";
import { BackIcon, CloseIcon } from "@/assets/icon/Icons";

interface HeaderPropType {
  type: "back" | "cancel";
  content: string;
  link: string;
}
export default function Header({ type, content, link }: HeaderPropType) {
  const router = useRouter();

  const handleIconClick = () => {
    router.push(link);
  };

  return (
    <h1 className="relative mb-6 h-[25px] w-full text-center text-[17px] font-bold">
      {type === "back" && <BackIcon onClick={handleIconClick} />}
      {type === "cancel" && link && <CloseIcon onClick={handleIconClick} />}
      {content}
    </h1>
  );
}
