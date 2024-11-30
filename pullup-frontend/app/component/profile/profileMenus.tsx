import Link from "next/link";
import Text from "../ui/Text";
import { DepthIcon } from "@/assets/icon/Icons";
import {  ServiceUsageIcon,
  PrivacyPolicyIcon,
  LogoutIcon,
  SignOutIcon,
  VersionIcon, } from "@/assets/icon/ProfileMenusIcons";


const etcItems = [
  {
    name: "서비스 이용약관",
    link: "/main/profile/termsOfUse",
    hasNextPage: false,
    icon: ServiceUsageIcon,
  },
  {
    name: "개인정보처리방침",
    link: "/main/profile/privacyPolicy",
    hasNextPage: false,
    icon: PrivacyPolicyIcon,
  },
  {
    name: "로그아웃",
    link: "/",
    hasNextPage: true,
    icon: LogoutIcon,
  },
  {
    name: "회원탈퇴",
    link: "/main/profile/cancel",
    hasNextPage: true,
    icon: SignOutIcon,
  },
];

export default function ProfileMenus() {
  const handleLogout = (e: React.MouseEvent<HTMLAnchorElement>) => {
      e.preventDefault(); // 기본 링크 동작 방지
      localStorage.clear(); // 로컬 스토리지 비우기
      window.location.href = "/"; // 로그아웃 후 이동할 페이지
  };

  return (
    <div className="mb-4">
      <Text size="body-01" className="mb-2">
        기타
      </Text>
      <div className="w-full rounded-lg bg-white shadow-[1px_1px_15px_0px_rgba(0,0,0,0.03)]">
        {etcItems.map((item) => {
          return (
            <Link
              href={item.link}
              key={item.name}
              className="flex items-center border border-b border-solid border-[#F4F3F8] px-6 py-5"
              onClick={item.name === "로그아웃" ? handleLogout : undefined} 
            >
              <item.icon />
              <Text size="body-03" className="w-[80%]">
                {item.name}
              </Text>
              <DepthIcon />
            </Link>
          );
        })}
        <div className="flex items-center px-6 py-5">
          <VersionIcon />
          <Text size="body-03" className="w-[75%]">
            버전 정보
          </Text>
          <p>1.0.0</p>
        </div>
      </div>
    </div>
  );
} 

// target={item.hasNextPage ? undefined : "_blank"}
// rel={item.hasNextPage ? undefined : "noopener noreferrer"}
