// 비밀번호/확인 일치 체크
(function(){
  const form = document.querySelector('form');
  if(!form) return;
  const pw = form.querySelector('input[name="password"], input[id$="password"]');
  // confirmPassword는 th:field="*{confirmPassword}"일 때 name="confirmPassword"가 됩니다.
  const pw2 = form.querySelector('input[name="confirmPassword"], input[id$="confirmPassword"]');
  const minLen = 6;
  if(!pw || !pw2) return;

  let help = document.createElement('div');
  help.className = 'small mt-1';
  pw2.closest('div').appendChild(help);

  function update(){
    const a = (pw.value||'').trim();
    const b = (pw2.value||'').trim();
    if(a.length < minLen){
      help.textContent = '비밀번호는 최소 ' + minLen + '자 이상이어야 합니다.';
      help.classList.add('text-danger'); help.classList.remove('text-success');
      return;
    }
    if(a === b){
      help.textContent = '비밀번호가 일치합니다.';
      help.classList.remove('text-danger'); help.classList.add('text-success');
    }else{
      help.textContent = '비밀번호가 서로 일치하지 않습니다.';
      help.classList.add('text-danger'); help.classList.remove('text-success');
    }
  }
  pw.addEventListener('input', update);
  pw2.addEventListener('input', update);

  form.addEventListener('submit', (e)=>{
    const a=(pw.value||'').trim(), b=(pw2.value||'').trim();
    if(a.length<minLen || a!==b){ e.preventDefault(); e.stopPropagation(); update(); pw2.focus(); }
  });
})();