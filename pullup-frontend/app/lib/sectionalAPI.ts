import { API } from "@/lib/API";
import { SectionalStartResponseType, Explanation } from "@/types/sectionalType";

export const postStartSectionalExam = async (
  memberId: number,
  type: "EVENLY" | "BY_PROBLEM_TYPE",
  entry: "LANGUAGE" | "REASONING" | "MATH" | "SPATIAL",
  problemType?: string,
) => {
  const typeMap = {
    EVENLY: "evenly",
    BY_PROBLEM_TYPE: "by-problem-type",
  };

  let body: Record<string, any> = {
    memberId: memberId,
    entry: entry,
  };

  if (type === "BY_PROBLEM_TYPE" && problemType) {
    body.problemType = problemType;
  }

  try {
    const response = await fetch(`${API}/exams/${typeMap[type]}/start`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(body),
      // credentials: "include",
    });

    if (!response.ok) {
      throw new Error("Network response was not ok");
    }

    const result: SectionalStartResponseType = await response.json();
    return result;
  } catch (error) {
    console.error("There was a problem with the fetch operation:", error);
  }
};

export const resetSelectedAnswers = async (examId: number | null) => {
  if (!examId) return;
  type resetSelectedAnswersResponseType = {
    message: string;
  };
  try {
    const response = await fetch(`${API}/exams/reset/${examId}`, {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
      },
      // credentials: "include",
    });

    if (!response.ok) {
      throw new Error("Network response was not ok");
    }
    const result: resetSelectedAnswersResponseType = await response.json();
    return result;
  } catch (error) {
    console.error("There was a problem with the fetch operation:", error);
  }
};

export const postSubmitAnswer = async (
  examId: number,
  problemNumber: number,
  submitAnswer: number,
) => {
  try {
    const response = await fetch(`${API}/exams/submit`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        examId: examId,
        problemNumber: problemNumber,
        submitAnswer: submitAnswer,
      }),
      // credentials: "include",
    });

    if (!response.ok) {
      throw new Error("Network response was not ok");
    }
    const result: Explanation = await response.json();
    return result;
  } catch (error) {
    console.error("There was a problem with the fetch operation:", error);
  }
};
