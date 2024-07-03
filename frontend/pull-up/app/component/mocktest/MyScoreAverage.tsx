import SemiCircularProgressBar from "../ui/SemiCircularProgressBar";
function MyScoreAverage() {
  return (
    <div className="flex w-full flex-col rounded-2xl bg-pink-100 p-6">
      <p>우수한 합격권이예요!</p>
      <SemiCircularProgressBar />
    </div>
  );
}

export default MyScoreAverage;
