type Entry = "LANGUAGE" | "REASONING" | "MATH" | "SPATIAL";

export interface SectionalNextResponseType {
  examId: number;
  isSubmitted: boolean;
  explanation: Explanation;
  totalProblemCount: number;
  leftProblemCount: number;
  problemNumber: number;
  entry: string;
  problemType: string;
  question: string;
  example: string;
  choices: string[];
}

export interface EntryStartRequestBody {
  memberId: number;
  examType: "EVENLY" | "BY_PROBLEM_TYPE";
  entry: Entry;
}

export interface Explanation {
  isCorrect: boolean;
  submitAnswer: number;
  correctAnswer: number;
  correctRate: number;
  incorrectRate: number;
  explanation: string;
}

export interface SectionalStartResponseType {
  examId: number;
  totalProblemCount: number;
  leftProblemCount: number;
  problemNumber: number;
  entry: string;
  problemType: string;
  question: string;
  example: string;
  choices: string[];
}

export interface SectionalResultResponseType {
  entry: Entry;
  isFinished: boolean;
  memberName: string;
  totalProblemCount: number;
  leftProblemCount: number;
  correctProblemCount: number;
  score: number;
  results: ProblemResult[];
}

export interface ProblemResult {
  problemNumber: number;
  problemType: string;
  isSubmitted: boolean;
  isCorrect: boolean;
}
// Explanation

// export interface EntryStartRequestBody {
//   examId: number;
//   problemNumber: number;
//   submitAnswer: number;
// }

// export interface EntryStartRequestBody {
//   examId: number;
//   totalProblemCount: number;
//   leftProblemCount: number;
//   problemNumber: number;
//   entry: string;
//   problemType: string;
//   question: string;
//   example: string;
//   choices: string[];
// }
