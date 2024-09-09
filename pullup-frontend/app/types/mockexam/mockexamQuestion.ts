export interface MockExamResponseType {
  id: number;
  member: Member;
  entry: null;
  category: string;
  type: null;
  problemIds: ProblemId[];
}

export interface MockExamProblemsType {
  id: number;
  member: Member;
  entry: null;
  category: string;
  type: null;
  problemIds: ProblemId[];
}


export interface ProblemBeingSolved {
  id: number;
  problem: Problem;
  chosenAnswer: string;
  isCorrect: boolean;
}

export interface ProblemId {
  id: number;
  examInformation: ExamInformation;
  problem: Problem;
  problemNumber: number;
  chosenAnswer: null;
  isCorrect: null;
}

export interface Problem {
  id: number;
  entry: string;
  category: string;
  type: string;
  question: string;
  explanation: string;
  choices: string[];
  answer: string;
  answerExplain: string;
  totalAttempts: null;
  incorrectAttempts: null;
  incorrectRate: null;
}


export interface MockExamProblemType {
  id: number;
  problemNumber: number;
  entry: string;
  category: string;
  type: string;
  question: string;
  explanation: string;
  choices: string[];
  answer: string;
  answerExplain: string;
  totalAttempts: number;
  incorrectAttempts: number;
  incorrectRate: number;
  createdDate: string;
}


interface ExamInformation {
  id: number;
  member: Member;
  entry: null;
  category: string;
  type: null;
  createdDate: string;
  solvedDate: null;
  requiredTime: null;
  score: number;
}

interface Member {
  id: number;
  name: string;
  email: string;
  accessCheck: boolean;
  role: string;
}


