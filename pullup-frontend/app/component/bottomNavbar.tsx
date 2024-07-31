"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import { MockTestIcon, ProfileIcon, SectionalIcon } from "@/assets/icon";

const navbarItem = [
  {
    name: "모의고사",
    Icon: MockTestIcon,
    link: "/main/mocktest",
  },
  {
    name: "영역별",
    Icon: SectionalIcon,
    link: "/main/sectional",
  },
  {
    name: "내 정보",
    Icon: ProfileIcon,
    link: "/main/profile",
  },
];

export default function BottomNavbar() {
  const pathname = usePathname();

  const pathDepth = pathname.split("/").length - 1;
  if (pathDepth !== 2) return;

  return (
    <nav className="z-90 fixed bottom-0 flex h-[91px] w-full min-w-[320px] max-w-[450px] items-center justify-around pb-[30px] pt-[17px]">
      {navbarItem.map(({ name, Icon, link }) => {
        const isClicked = pathname.includes(link);
        return (
          <Link
            href={link}
            key={name}
            className="flex flex-col items-center gap-1"
          >
            <Icon isClicked={isClicked} />
            <p
              className={`text-[13px] font-semibold ${isClicked ? "text-blue01" : "text-[#D9D9D9]"} `}
            >
              {name}
            </p>
          </Link>
        );
      })}
    </nav>
  );
}
