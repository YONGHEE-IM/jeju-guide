// 실시간 글자 수 카운터 + 최소 글자수 체크
(function () {
  function bindCounter(input) {
    const targetSel = input.getAttribute('data-count-target');
    const min = parseInt(input.getAttribute('data-min') || '0', 10);
    const counter = document.querySelector(targetSel);
    const update = () => {
      const len = input.value.trim().length;
      if (counter) counter.textContent = String(len);
      if (min > 0) {
        input.classList.toggle('is-invalid', len < min);
      }
    };
    input.addEventListener('input', update);
    update();
  }

  document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('[data-count-target]').forEach(bindCounter);

    // 폼 전송시 최소 길이 검증(프론트)
    document.querySelectorAll('form').forEach(form => {
      form.addEventListener('submit', (e) => {
        const mins = form.querySelectorAll('[data-min]');
        for (const el of mins) {
          const min = parseInt(el.getAttribute('data-min') || '0', 10);
          if (min > 0 && el.value.trim().length < min) {
            e.preventDefault();
            el.focus();
            alert((el.id === 'title' ? '제목' : '내용') + `은(는) 최소 ${min}자 이상이어야 합니다.`);
            return false;
          }
        }
      });
    });
  });
})();
