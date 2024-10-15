"use client";

// 밑줄 적용을 위함
export const FormatQuestion = (question: string) => {
  const parts = question.split(/(@@.*?@@)/g);

  return (
    <span>
      {parts.map((part, index) =>
        part.startsWith("@@") && part.endsWith("@@") ? (
          <span key={index} className="underline underline-offset-4">
            {part.slice(2, -2)}
          </span>
        ) : (
          part
        ),
      )}
    </span>
  );
};
