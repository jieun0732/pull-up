"use client";

import { useRouter } from "next/navigation";
import BackIcon from "@/assets/icon/BackIcon";
import CloseIcon from "@/assets/icon/CloseIcon";

interface HeaderPropType {
  type: "back" | "cancel";
  content: string;
  link?: string;
}
export default function Header({ type, content, link }: HeaderPropType) {
  const router = useRouter();

  const handleIconClick = () => {
    if (type === "back") {
      router.back();
    } else if (type === "cancel" && link) {
      router.push(link);
    }
  };

  return (
    <h1 className="relative mb-6 h-[25px] w-full text-center text-[17px] font-bold">
      {type === "back" && <BackIcon onClick={handleIconClick} />}
      {type === "cancel" && link && <CloseIcon onClick={handleIconClick} />}
      {content}
    </h1>
  );
}
