'use client';
import { useRouter } from 'next/navigation';
import { CloseIcon } from "@/assets/icon/Icons";

export default function TermsOfUse() {
  const router = useRouter();


  return (
    <div className="text-[#000000] mb-6 mt-16 mx-2">
      <div className="absolute top-14 right-8 text-white">
      <CloseIcon 
        onClick={() => router.back()} 
      />
      </div>
      <div className="font-['WantedSans'] p-4">
        <h1 className="text-[26px] font-bold mb-4">개인정보처리방침</h1>

        <p>(주)풀업(이하 “회사”)는 통신비밀보호법, 전기통신사업법, 정보통신망 이용촉진 및 정보보호 등에 관한 법률 등 정보통신서비스제공자가 준수하여야 할 관련 법령상의 개인정보보호 규정을 준수하며, 관련 법령에 의거한 개인정보처리방침을 정하여 이용자 권익 보호에 최선을 다하고 있습니다.</p>
        
        <p className="mt-8 mb-2">이에 개인정보보호법 제 30조 따라 정보주체의 개인정보를 보호하고 이와 관련한 고충을 신속하고 원활하게 처리할 수 있도록 위하여 다음과 같이 개인정보처리방침을 수립, 공개합니다.</p>

        <h2 className="text-[20px] font-bold mt-8 mb-2">1. 개인정보의 수집 및 이용목적</h2>
        <p className="mt-8 mb-2">회사는 다음 목적을 위하여 개인정보를 수집하고 있으며 다음 목적 이외의 용도로는 수집한 개인정보를 이용하지 않으며, 이용 목적이 변경되는 경우에는 {'개인정보보호법'} 제 18조에 따라 별도의 동의를 받는 등 필요한 조치를 이행할 예정입니다.</p>

        <ol className="list-decimal ml-5 space-y-2">
          <li>서비스 이용에 따른 가입의사 확인, 회원 식별, 원활한 의사소통 경로의 확보, 새로운 정보의 소개 및 고지사항 전달, 서비스 방문 및 이용기록 분석 등을 위해 이용합니다.</li>
          <li>회원이 등록한 정보에 기반한 풀업 서비스를 위해 이용합니다.</li>
          <li>서비스 이용과 접속 빈도 분석, 서비스 이용에 대한 통계, 서비스 분석 및 통계에 따른 맞춤 서비스 제공 및 광고 게재 등을 위해 이용합니다.</li>
          <li>서비스 제공에 더하여, 인구통계학적 분석, 맞춤형 서비스 제공 등 신규 서비스 요소의 발굴 및 기존 서비스 개선 등을 위해 이용합니다.</li>
          <li>법령 및 풀업 이용약관을 위반하는 회원에 대한 이용 제한 조치, 부정 이용 행위를 포함하여 서비스의 원활한 운영에 지장을 주는 행위에 대한 방지 및 제제, 계정도용 방지, 약관 개정 등의 고지사항 전달, 분쟁조정을 위한 기록 보존, 고객 문의 처리 등 회원 보호 및 서비스 운영을 위해 이용합니다.</li>
          <li>민원인의 신원 확인, 민원사항 확인, 사실조사를 위한 연락, 통치, 처리 결과 통보 등의 목적을 위해 이용합니다.</li>
          <li>이벤트 정보 및 참여기회 제공과 경품 배송, 광고성 정보 제공 등 마케팅 및 프로모션 목적을 위해 이용합니다.</li>
        </ol>

        <h2 className="text-[20px] font-bold mt-8 mb-2">2. 수집하는 개인정보 항목 및 수집방법</h2>
        <p className="mt-8 mb-2">회사는 법령에 따른 개인정보 보유 이용기간 또는 정보 주체로부터 개인정보를 수집 시에 동의 받은 개인정보 보유 이용기간 내에서 개인 정보를 처리 보유합니다.</p>

        <ol className="list-decimal ml-5 space-y-2">
          <div>1. 수집하는 개인정보의 항목</div>
          <ul className="list-disc ml-5">
            <div>가. 소셜 회원가입</div>
            <ul className="list-disc ml-5">
              <div>1) 카카오</div>
              <ul className="list-disc ml-5">
                <div>필수항목: 카카오계정(이메일), 닉네임</div>
              </ul>
              <div>2) 애플</div>
              <ul className="list-disc ml-5">
                <div>필수항목: 애플계정(이메일), 이름</div>
              </ul>
            </ul>
            <div>나. 서비스 이용과정이나 사업처리 과정에서 자동으로 수집되는 항목</div>
            <ul className="list-disc ml-5">
              <div>IP Address, 쿠키, 방문 일시, 서비스 이용 기록, 불량 이용 기록, 광고 ID, 접속 환경</div>
            </ul>
            <div>다. 민원 처리 과정에서 수집되는 항목</div>
            <ul className="list-disc ml-5">
              <div>1) 이메일 접수</div>
              <ul className="list-disc ml-5">
                <div>이메일 주소, 이름, 풀업 계정 정보</div>
              </ul>
              <div>2) 전화 접수</div>
              <ul className="list-disc ml-5">
                <div>이메일 주소, 이름, 풀업 계정 정보</div>
              </ul>
              <div>3) 카카오톡 채널을 통한 접수</div>
              <ul className="list-disc ml-5">
                <div>이름, 풀업 계정 정보</div>
              </ul>
            </ul>
          </ul>
        </ol>



        <h2 className="text-[20px] font-bold mt-8 mb-2">3. 개인정보의 보유 및 이용기간</h2>
        <p>회사는 이용자의 개인정보를 고지 및 동의 받은 사항에 따라 수집, 이용 목적이 달성되기 전 또는 이용자의 탈퇴 요청이 있기 전까지 해당 정보를 보유합니다. 개인정보의 수집 및 이용에 대한 동의를 철회하는 경우, 수집 및 이용목적이 달성되거나 이용기간이 종료한 경우 개인정보를 지체 없이 파기합니다.</p>
        <p className="mt-8 mb-2">단, 상법 등 관계 법령의 규정에 의하여 보존할 필요가 있는 경우 법령에서 규정한 보존기간 동안 거래내역과 최소한의 기본정보를 보유합니다. 이 경우 회사는 보관하는 정보를 그 보관의 목적으로만 이용합니다.</p>
        
        <ol className="list-decimal ml-5 space-y-2">
          <li>통신비밀보호법</li>
            <ul className="list-disc ml-5">
                <li>서비스 이용기록, 접속 로그, 접속 IP 정보 : 3개월</li>
            </ul>
          <li>전자상거래 등에서의 소비자보호에 관한 법률
            <ul className="list-disc ml-5">
              <li>표시, 광고에 관한 기록 : 6개월</li>
              <li>계약 또는 청약철회 등에 관한 기록 : 5년</li>
              <li>대금결제 및 재화 등의 공급에 관한 기록 : 5년</li>
              <li>소비자의 불만 또는 분쟁처리에 관한 기록 : 3년</li>
            </ul>
          </li>
          <li>부가가치세법</li>
            <ul className="list-disc ml-5">
                  <li>세금계산서, 영수증 등 거래내역 관련 정보 : 5년</li>
            </ul>
          <li>전자금융거래법</li>
            <ul className="list-disc ml-5">
                  <li>전자금융 거래에 관한 기록 : 1년 또는 5년</li>
            </ul>
        </ol>

        <h2 className="text-[20px] font-bold mt-8 mb-2">4. 개인정보의 제공 및 위탁</h2>
        <p>회사는 서비스의 원활한 제공을 위해 필요한 때에는 개인정보의 처리를 일부 위탁하고 있으며, 수탁 받은 업체가 관계 법령을 준수하도록 관리, 감독하고 있습니다.</p>

        <table className="min-w-full border-collapse border border-gray-300 mt-8 mb-2">
          <thead>
            <tr>
              <th className="border border-gray-300 px-4 py-2 bg-gray-100">수탁자</th>
              <th className="border border-gray-300 px-4 py-2 bg-gray-100">위탁 업무 내용</th>
              <th className="border border-gray-300 px-4 py-2 bg-gray-100">개인정보의 보유 및 이용기간</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td className="border border-gray-300 px-4 py-2">Amazon Web Services (AWS)</td>
              <td className="border border-gray-300 px-4 py-2">회원 정보 보관, 서비스 이용 내역 보관</td>
              <td className="border border-gray-300 px-4 py-2">회원 탈퇴 시 또는 위탁 계약 종료 시까지</td>
            </tr>
          </tbody>
        </table>

        <h2 className="text-[20px] font-bold mt-8 mb-2">5. 개인정보 파기절차 및 파기방법</h2>
        <p>이용자의 개인정보는 원칙적으로 개인정보의 수집 및 이용목적이 달성되면 지체없이 파기합니다. 회사의 개인정보 파기절차 및 방법은 다음과 같습니다.</p>
        
        <ol className="list-decimal ml-5 space-y-2">
          <li>파기절차</li>
            <ul className="list-disc ml-5">
                  <li>이용자의 개인정보는 수집 및 이용목적이 달성되면 지체없이 파기합니다. (여기서 ㄴ{'이용목적이 달성된 때'}란 철회요청, 서비스 계약 만료, 탈퇴 시를 의미) 다만, 회사 내부 방침 또는 관계 법령에서 정한 보관기간이 있을 경우 일정 기간동안 보관 후 파기됩니다.</li>
            </ul>
          <li>파기방법</li>
            <ul className="list-disc ml-5">
                  <li>종이에 출력된 개인정보는 분쇄기로 분쇄하거나 소각을 통해 파기합니다. 전자적 파일 형태로 저장된 개인정보는 기록을 재생할 수 없는 기술적 방법을 사용하여 삭제합니다.</li>
            </ul>
        </ol>

        <h2 className="text-[20px] font-bold mt-8 mb-2">6. 이용자의 권리</h2>
        <ol className="list-decimal ml-5 space-y-2">
          <li>회원은 언제든지 자신의 개인정보를 조회하거나 수정할 수 있습니다.</li>
          <li>회원은 언제든지 “탈퇴하기” 기능을 통해 개인정보의 수집 및 이용 동의를 철회할 수 있습니다.</li>
          <li>회원이 개인정보의 오류에 대한 정정을 요청한 경우, 정정을 완료하기 전까지 해당 개인정보를 이용 또는 제공하지 않습니다. 또한 잘못된 개인정보를 제 3자에게 이미 제공한 경우에는 정정 처리결과를 제 3자에게 지체없이 통지하여 정정이 이루어지도록 하겠습니다.</li>
        </ol>

        <h2 className="text-[20px] font-bold mt-8 mb-2">7. 개인정보 자동 수집 장치의 설치, 운영 및 거부에 관한 사항</h2>
        <p> - 쿠키란 웹사이트를 운영하는데 이용되는 서버가 이용자의 브라우저에 보내는 작은 텍스트 파일로 이용자의 하드디스크에 저장됩니다. 세션이란 웹사이트를 운영하는 데 이용되는 서버가 이용자 로그인 시간 동안에 이용자의 정보를 서버에 저장하는 것을 의미합니다. 회사는 다음과 같은 목적을 위해 쿠키와 세션을 사용합니다.</p>
        
        <ol className="list-decimal ml-5 space-y-2 mt-8 mb-2">
          <li> 쿠키의 사용 목적 </li>
          <p>회사가 쿠키를 통해 수집하는 정보는 {'수집하는 개인정보의 항목'}과 같으며 ㄴㄴ{'개인정보의 수집 및 이용목적'} 외의 용도로는 이용되지 않습니다.</p>
          <li>쿠키 설정 거부</li>
          <p>이용자는 쿠키에 대한 선택권을 가지고 있습니다. 웹 브라우저 옵션을 선택함으로써 모든 쿠키의 허용, 동의를 통한 쿠키의 허용, 모든 쿠키의 차단을 스스로 결정할 수 있습니다. 단, 쿠키 저장을 거부할 경우에는 로그인이 필요한 일부 서비스를 이용하지 못할 수 있습니다.</p>
        </ol>

        <h2 className="text-[20px] font-bold mt-8 mb-2">8. 개인정보의 기술적, 관리적 보호대책</h2>
        <ol className="list-decimal ml-5 space-y-2">
          <li>개인정보 암호화 이용자의 개인정보는 비밀번호에 의해 보호되며, 파일 및 각종 데이터는 암호화되거나 파일 잠금 기능을 통해 별도의 보안기능을 통해 보호하고 있습니다.</li>
          <li>개인정보에 대한 접근 제한개인정보를 처리하는 데이터베이스시스템에 대한 접근권한의 부여, 변경, 말소를 통하여 개인정보에 대한 접근통제를 위하여 필요한 조치를 하고 있으며 침입차단시스템을 이용하여 외부로부터의 무단 접근을 통제하고 있습니다. </li>
          <li>개인 아이디와 비밀번호 관리 등에 대하여 회사는 이용자의 개인정보를 보호하기 위하여 최선의 노력을 다하고 있습니다. 단, 이용자의 개인적인 부주의로 이메일(또는 페이스북 등 외부 서비스와의 연동을 통해 이용자가 설정한 계정 정보), 비밀번호 등 개인정보가 유출되어 발생한 문제와 기본적인 인터넷 위험성 때문에 일어나는 일들에 대해 책임을 지지 않습니다. </li>
        </ol>

        <h2 className="text-[20px] font-bold mt-8 mb-2">링크</h2>
        <ol className="list-decimal ml-5 space-y-2">
          <li>사이트는 다양한 배너와 링크를 포함하고 있습니다. 많은 경우 타 사이트의 페이지와 연결되어 있으며 이는 광고주와의 계약관계에 의하거나 제공받은 컨텐츠의 출처를 밝히기 위한 조치입니다.</li>
          <li>사이트가 포함하고 있는 링크를 클릭하여 타 사이트의 페이지로 옮겨갈 경우 해당 사이트의 개인정보처리방침은 회사와 무관하므로 새로 방문한 사이트의 정책을 검토해 보시기 바랍니다.</li>
        </ol>

        <h2 className="text-[20px] font-bold mt-8 mb-2">10. 이용자의 권리와 그 행사방법</h2>
        <ol className="list-decimal ml-5 space-y-2">
            <ul className="list-disc ml-5">
                <li>이용자 및 법정대리인은 언제든지 등록되어 있는 자신 혹은 당해 미성년자의 정보를 열람, 공개 및 비공개 처리, 수정, 삭제할 수 있습니다. 이용자 및 법정대리인은 개인정보 조회, 수정, 가입해지(동의철회)가 전자우편 등을 통해 가능하며, 개인정보보호 책임자에게 이메일로 연락하시는 경우에는 본인 확인 절차를 거친 후 조치하겠습니다. 이용자가 개인정보의 오류에 대한 정정을 요청하신 경우에는 정정을 완료하기 전까지 당해 개인 정보를 이용 또는 제공하지 않습니다. 또한 잘못된 개인정보를 제 3자에게 이미 제공한 경우에는 정정 처리결과를 제 3자에게 지체 없이 통지하여 저정이 이루어지도록 하겠습니다. 회사는 이용자 요청에 의해 해지 또는 삭제된 개인정보는 {'3. 개인정보의 보유 및 이용기간'}에 명시된 바에 따라 처리하고 그 외의 용도로 열람 또는 이용할 수 없도록 처리하고 있습니다. </li>
            </ul>
        </ol>

        <h2 className="text-[20px] font-bold mt-8 mb-2">11. 개인정보에 관한 민원서비스</h2>
        <p>회사는 개인정보 처리에 관한 업무를 총괄해서 책임지고, 개인정보 처리와 관련한 정보주체의 불만처리 및 피해구제 등을 위하여 아래와 같이 개인정보 보호책임자를 지정하고 있습니다.</p>
          <ol className="list-decimal ml-5 space-y-2 mt-8 mb-2">
            <ul className="list-disc ml-5">
              <li>개인정보 보호 책임자</li>
              <li>성명 : 이지은</li>
              <li>직책 : 개인정보관리담당자</li>
              <li>이메일 : pullup.product@gmail.com</li>
            </ul>
          </ol>

        <p className="mt-2 text-gray-600">* 개인정보 보호 담당부서로 연결됩니다.</p>

        <p className="mt-8 mb-2">또한 개인정보가 침해되어 이에 대한 신고나 상담이 필요하신 경우에는 아래 기관에 문의하셔서 도움을 받으실 수 있습니다.</p>
          <ol className="list-decimal ml-5 space-y-2 mt-8 mb-2">
            <ul className="list-disc ml-5">
              <li>개인정보침해신고센터 (privacy.kisa.or.kr / 국번없이 118)</li>
              <li>대검찰청 사이버수사과 (www.spo.go.kr / 국번없이 1301)</li>
              <li>경찰청 사이버안전지킴이 (www.police.go.kr/www/security/cyver.jsp / 국번없이 182)</li>
            </ul>
          </ol>

        <h2 className="text-[20px] font-bold mt-8 mb-2">12. 개인정보 처리 방침 변경</h2>
        <ol className="list-decimal ml-5 space-y-2">
          <li>본 개인정보 처리방침은 2024년 8월 1일부터 적용됩니다.</li>
          <li>이전의 개인정보 처리방침은 아래에서 확인하실 수 있습니다. </li>
        </ol>
      </div>
    </div>
  );
}
