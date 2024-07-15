interface ProgressBarPropsType {
  now: number;
  total: number;
}
export default function ProgressBar({ now, total }: ProgressBarPropsType) {
  const progressWidth = `${(now / total) * 100}%`;

  return (
    <div className="relative mt-4 h-[5px] w-full rounded-3xl bg-gray03">
      <div
        className="absolute left-0 h-[5px] rounded-3xl bg-blue01"
        style={{ width: progressWidth }}
      ></div>
    </div>
  );
}
