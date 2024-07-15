"use client";

import { useParams, useRouter } from "next/navigation";
import { sections } from "@/constants/constants";
import Button from "@/component/ui/Button";

export default function Page() {
  const router = useRouter();
  const params = useParams<{ id: string }>();
  const currentSection = params.id;

  return (
    <div className="m-9 w-full bg-yellow-400 text-2xl">
      <p>{currentSection} 번</p>
      <Button
        onClick={() => router.push(`/main/check/${Number(currentSection) + 1}`)}
        size="large"
      >
        move ++
      </Button>
      <Button
        onClick={() => router.push(`/main/check/${Number(currentSection) - 1}`)}
        size="large"
      >
        move __
      </Button>
    </div>
  );
}
