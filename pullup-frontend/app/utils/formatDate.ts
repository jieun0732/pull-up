export function formatDate(dateString: string): string {
  // 주어진 문자열을 Date 객체로 변환
  const date = new Date(dateString);
  
  // 월과 일을 추출
  const month = date.getMonth() + 1; // getMonth()는 0부터 시작하므로 1을 더함
  const day = date.getDate();
  
  // 형식에 맞춰 문자열 반환
  return `${month}월 ${day}일`;
}
