"use client";

interface SubmitButtonProps {
  handleExamResult: () => Promise<void>;
  isFinished: boolean;
}
export default function SubmitButton({
  isFinished,
  handleExamResult,
}: SubmitButtonProps) {
  return (
    <>
      {isFinished ? (
        <button
          onClick={() => handleExamResult()}
          className="ml-auto rounded-t-2xl rounded-bl-2xl bg-blue03 px-6 py-2 text-blue01 shadow-[2px_2px_20px_0px_rgba(0,0,0,0.16)]"
        >
          제출하기
        </button>
      ) : (
        <button className="ml-auto rounded-t-2xl rounded-bl-2xl bg-gray03 px-6 py-2 text-gray02 shadow-[2px_2px_20px_0px_rgba(0,0,0,0.16)]">
          제출하기
        </button>
      )}
    </>
  );
}
