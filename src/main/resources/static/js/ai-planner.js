(()=> {
  const el = id => document.getElementById(id);
  const btn = el('generate');
  const result = el('result');

  // ⬇️ CSRF 토큰/헤더명 읽기
  const csrfToken  = document.querySelector('meta[name="_csrf"]')?.content || '';
  const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.content || 'X-CSRF-TOKEN';

  function showError(msg){
    result.innerHTML = `<div class="alert alert-danger mt-3">${msg}</div>`;
  }

  btn.addEventListener('click', async () => {
    btn.disabled = true; 
    btn.textContent = 'AI가 일정을 생성하고 있습니다...';
    result.innerHTML = '';

    try {
      const body = {
        days: parseInt(el('days').value, 10),
        style: el('style').value,
        companion: el('companion').value,
        budget: el('budget').value
      };

      const res = await fetch('/api/ai/plan', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          [csrfHeader]: csrfToken,     // ⬅️ 여기 중요!
        },
        body: JSON.stringify(body)
      });

      if (!res.ok) {
        const text = await res.text();
        throw new Error(`요청 실패(${res.status}) : ${text}`);
      }

      const data = await res.json();
      renderPlans(data);

    } catch (e) {
      console.error(e);
      showError('일정 생성 중 문제가 발생했습니다. 잠시 후 다시 시도해 주세요.');
    } finally {
      btn.disabled = false;
      btn.textContent = '맞춤 여행 일정 생성하기';
    }
  });

  function badgeClass(cat){
    const map = {
      '자연':'bg-success-subtle text-success-emphasis',
      '문화':'bg-primary-subtle text-primary-emphasis',
      '맛집':'bg-warning-subtle text-warning-emphasis',
      '액티비티':'bg-info-subtle text-info-emphasis',
      '식사':'bg-danger-subtle text-danger-emphasis',
      '휴식':'bg-secondary-subtle text-secondary-emphasis',
      '숙박':'bg-dark-subtle text-dark-emphasis',
      '이동':'bg-light text-dark'
    };
    return 'badge rounded-pill ' + (map[cat]||'bg-light text-dark');
  }

  function renderPlans(data){
    if (!data || !data.plans) {
      showError('생성된 일정이 없습니다.');
      return;
    }
    const wrap = document.createElement('div');
    data.plans.forEach(day => {
      const card = document.createElement('div');
      card.className = 'card rounded-4 shadow-sm mb-3';
      card.innerHTML = `
        <div class="card-header bg-body-tertiary fw-bold">${day.title}</div>
        <div class="card-body">
          <div class="vstack gap-3"></div>
        </div>`;
      const list = card.querySelector('.vstack');
      day.activities.forEach(act => {
        const row = document.createElement('div');
        row.className = 'd-flex gap-3 align-items-start border-bottom pb-2';
        row.innerHTML = `
          <div class="text-muted small" style="width:70px">${act.time}</div>
          <div class="flex-grow-1">
            <div class="fw-semibold">${act.place}</div>
            <div class="text-muted small">${act.description}</div>
          </div>
          <span class="${badgeClass(act.category)}">${act.category}</span>`;
        list.appendChild(row);
      });
      wrap.appendChild(card);
    });
    result.appendChild(wrap);
    window.scrollTo({top: result.offsetTop - 80, behavior: 'smooth'});
  }
})();
