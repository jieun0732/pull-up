interface ThinProgressBarPropsType {
  now: number;
  total: number;
}
export default function ThinProgressBar({
  now,
  total,
}: ThinProgressBarPropsType) {
  const progressWidth = `${(now / total) * 100}%`;
  return (
    <div className="relative mt-4 h-[2px] w-full rounded-3xl bg-blue02">
      <div
        className="absolute left-0 h-[2px] rounded-3xl bg-blue01"
        style={{ width: progressWidth }}
      ></div>
    </div>
  );
}
