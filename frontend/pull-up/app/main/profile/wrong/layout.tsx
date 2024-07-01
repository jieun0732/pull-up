export default function Layout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <>
      <div className="h-full w-full min-w-[320px] max-w-[450px] overflow-y-auto bg-white px-5 pb-7 pt-14 text-black01">
        {children}
      </div>
    </>
  );
}
