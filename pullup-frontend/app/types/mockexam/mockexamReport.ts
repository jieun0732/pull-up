export interface MockExamReportType {
  examId: number;
  createdDate: string;
  solvedDate: string;
  requiredTime: number;
  score: number;
  problemTypeResults: ProblemTypeResult[];
  averageScore: number;
  averageTime: number;
  totalCorrectAnswers: number;
  rankPercent: string;
}

export interface ProblemTypeResult {
  entry: string;
  totalProblems: number;
  correctProblems: number;
}

export interface MockExamReportPropType {
  recentReportInfo: MockExamReportType;
}
