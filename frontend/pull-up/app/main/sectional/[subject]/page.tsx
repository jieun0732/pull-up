"use client";

import Link from "next/link";
import { useParams } from "next/navigation";

export default function Page() {
  const params = useParams<{ subject: string }>();

  return (
    <div className="">
      <p>{params.subject}</p>
    </div>
  );
}
