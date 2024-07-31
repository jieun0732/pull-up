import Link from "next/link";
import DepthIcon from "@/assets/icon/DepthIcon";
import Text from "../ui/Text";
import {
  ServiceUsageIcon,
  PrivacyPolicyIcon,
  LogoutIcon,
  SignOutIcon,
  VersionIcon,
} from "@/assets/icon";

const etcItems = [
  {
    name: "서비스 이용약관",
    link: "www.google.co.kr",
    hasNextPage: true,
    icon: ServiceUsageIcon,
  },
  {
    name: "개인정보처리방침",
    link: "#",
    hasNextPage: true,
    icon: PrivacyPolicyIcon,
  },
  {
    name: "로그아웃",
    link: "#",
    hasNextPage: true,
    icon: LogoutIcon,
  },
  {
    name: "회원탈퇴",
    link: "#",
    hasNextPage: true,
    icon: SignOutIcon,
  },
];

export default function ProfileMenus() {
  return (
    <div className="mb-4">
      <Text size="body-01" className="mb-2">
        기타
      </Text>
      <div className="w-full rounded-lg bg-white shadow-[1px_1px_15px_0px_rgba(0,0,0,0.03)]">
        {etcItems.map((item) => {
          return (
            <Link
              href={`https://${item.link}`} // 프로토콜을 추가
              key={item.name}
              target="_blank"
              rel="noopener noreferrer"
              className="flex items-center border border-b border-solid border-[#F4F3F8] px-6 py-5"
            >
              <item.icon />
              <Text size="body-03" className="w-[80%]">
                {item.name}
              </Text>
              {item.hasNextPage && <DepthIcon />}
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
