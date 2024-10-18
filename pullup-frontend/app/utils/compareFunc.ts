export function compareScores(average: number, myscore: number) {
  if (myscore > average) {
    return "higher";
  } else if (myscore === average) {
    return "same";
  } else {
    return "lower";
  }
}

export function compareTime(average: number, mytime: number) {
  if (mytime > average) {
    return "lower";
  } else if (mytime === average) {
    return "same";
  } else {
    return "higher";
  }
}