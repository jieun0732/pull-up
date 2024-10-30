export interface MockExamReportType {
  examId: number;
  createdDate: string;
  solvedDate: string;
  requiredTime: RequiredTime;
  score: number;
  problemTypeResults: ProblemTypeResult[];
  averageScore: number;
  averageTime: RequiredTime;
  totalCorrectAnswers: number;
  rankPercent: string;
}

interface ProblemTypeResult {
  entry: string;
  totalProblems: number;
  correctProblems: number;
}

interface RequiredTime {
  seconds: number;
  zero: boolean;
  nano: number;
  negative: boolean;
  units: Unit[];
}

interface Unit {
  durationEstimated: boolean;
  timeBased: boolean;
  dateBased: boolean;
}

export interface MockExamReportPropType {
  recentReportInfo: MockExamReportType;
}
