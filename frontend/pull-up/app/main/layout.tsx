import BottomNavbar from "@/component/bottomNavbar";

export default function Layout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <>
      <main className="h-full w-full min-w-[320px] max-w-[450px] overflow-y-auto bg-white text-black01">
        {children}
      </main>
      <BottomNavbar />
    </>
  );
}
