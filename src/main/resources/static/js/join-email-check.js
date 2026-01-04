// 이메일 중복 확인 (가입 페이지 전용)
(function(){
  const emailInput = document.querySelector('input[name="email"]');
  const submitBtn  = document.querySelector('form button[type="submit"]');
  if(!emailInput || !submitBtn) return;

  const help = document.createElement('div');
  help.id = 'emailHelp';
  help.className = 'small mt-1';
  emailInput.closest('div').appendChild(help);

  let timer = null;
  let last = '';

  async function check(value){
    if(!value || !/^[^@\s]+@[^@\s]+\.[^@\s]+$/.test(value)){
      help.textContent = '';
      help.classList.remove('text-danger','text-success');
      submitBtn.disabled = false;
      return;
    }
    try{
      const res = await fetch(`/api/members/check-email?email=${encodeURIComponent(value)}`,
        {headers:{'X-Requested-With':'fetch'}});
      const data = await res.json();
      if(data.available){
        help.textContent = '사용 가능한 이메일입니다.';
        help.classList.remove('text-danger');
        help.classList.add('text-success');
        submitBtn.disabled = false;
      }else{
        help.textContent = '이미 등록 되어 있는 이메일 입니다.';
        help.classList.remove('text-success');
        help.classList.add('text-danger');
        submitBtn.disabled = true;
      }
    }catch(e){
      // 네트워크 오류 시 폼 제출은 막지 않음
      help.textContent = '';
      help.classList.remove('text-danger','text-success');
      submitBtn.disabled = false;
    }
  }

  emailInput.addEventListener('input', function(){
    const val = this.value.trim();
    if(val === last) return;
    last = val;
    clearTimeout(timer);
    timer = setTimeout(()=>check(val), 300); // 디바운스
  });
})();