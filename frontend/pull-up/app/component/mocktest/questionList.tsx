"use client";

import { dummyQlist } from "./dummyQlist";
import Button from "../ui/Button";

interface QuestionListPropType {
  showQuestions: boolean;
  setShowQuestions: React.Dispatch<React.SetStateAction<boolean>>;
}

function QuestionList({
  showQuestions,
  setShowQuestions,
}: QuestionListPropType) {
  const handleClick = () => {
    setShowQuestions(false);
  };

  console.log(showQuestions);

  return (
    <>
      {showQuestions && (
        <>
          <div
            className="absolute left-0 top-0 h-full w-full bg-black opacity-55"
            onClick={handleClick}
          ></div>
        </>
      )}

      <div
        className={`absolute bottom-9 ml-[16px] flex w-[calc(100%-32px)] flex-col items-center justify-between rounded-3xl bg-white px-5 pb-9 pt-3 duration-500 ease-out ${
          showQuestions ? "animate-fadeInUp" : "invisible animate-fadeOutDown"
        }`}
      >
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="27"
          height="27"
          viewBox="0 0 27 27"
          fill="none"
          className="mb-9"
          onClick={handleClick}
        >
          <path
            fillRule="evenodd"
            clipRule="evenodd"
            d="M13.1373 19.2645C13.3985 19.2687 13.661 19.1711 13.8603 18.9718L15.2745 17.5575L15.2586 17.5416L22.2394 10.5608C22.8252 9.97497 22.8252 9.02522 22.2394 8.43943C21.6536 7.85365 20.7039 7.85365 20.1181 8.43943L13.1373 15.4203L6.56066 8.84367C5.97487 8.25788 5.02513 8.25788 4.43934 8.84367C3.85355 9.42946 3.85355 10.3792 4.43934 10.965L11.0159 17.5416L11 17.5575L12.4142 18.9718C12.6135 19.1711 12.8761 19.2687 13.1373 19.2645Z"
            fill="#ACACAC"
          />
        </svg>
        <div className="grid w-full grid-cols-5 justify-items-center gap-y-4">
          {dummyQlist.qlist.map(({ isDone, id }) => {
            const boxStyle = isDone
              ? "border-blue01 bg-blue03 text-blue01"
              : "border-gray02 bg-gray03 text-gray02";

            return (
              <div
                key={id}
                className={`flex h-11 w-11 items-center justify-center space-x-4 rounded-md border border-solid text-center text-[17px] font-semibold ${
                  boxStyle
                }`}
              >
                <p>{id}</p>
              </div>
            );
          })}
        </div>

        {dummyQlist.isFinished ? (
          <Button size="large" color="active" customstyle="mt-9">
            제출하기
          </Button>
        ) : (
          <Button size="large" color="nonactive" customstyle="mt-9">
            제출하기
          </Button>
        )}
      </div>
    </>
  );
}

export default QuestionList;
