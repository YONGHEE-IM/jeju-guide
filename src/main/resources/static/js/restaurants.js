// restaurants.js — 카테고리/검색/상세 (안정화 버전)
(() => {
  const $ = (s, c=document)=>c.querySelector(s);
  const $$ = (s, c=document)=>Array.from(c.querySelectorAll(s));

  const searchInput   = $('#searchInput');
  const categoryGroup = $('#categoryGroup');
  const grid          = $('#grid');
  const emptyState    = $('#emptyState');
  const detailBody    = $('#detailBody');
  const modal         = $('#restaurantModal') ? new bootstrap.Modal($('#restaurantModal')) : null;

  const norm = s => (s||'').toLowerCase().trim();

  function applyFilter(){
    const q   = norm(searchInput?.value);
    const act = $('#categoryGroup .btn.btn-dark');
    const cat = act ? act.dataset.cat : '전체';
    let visible = 0;

    $$('.restaurant-card').forEach(card => {
      const title = norm(card.querySelector('.h6')?.textContent);
      const desc  = norm(card.querySelector('.card-text')?.textContent);
      const loc   = norm(card.querySelector('.text-secondary span')?.textContent);
      const okSearch = [title, desc, loc].some(v => (v||'').includes(q));
      const okCat    = (cat === '전체') || (card.dataset.category === cat);
      const show     = okSearch && okCat;
      card.parentElement.classList.toggle('d-none', !show);
      if (show) visible++;
    });

    emptyState?.classList.toggle('d-none', visible !== 0);
  }

  // ✅ 카테고리 클릭(이벤트 위임)
  categoryGroup?.addEventListener('click', e => {
    const btn = e.target.closest('button[data-cat]');
    if(!btn) return;

    $$('#categoryGroup .btn').forEach(b => {
      b.classList.add('btn-outline-secondary');
      b.classList.remove('btn-dark');
    });
    btn.classList.remove('btn-outline-secondary');
    btn.classList.add('btn-dark');

    applyFilter();
  });

  // ✅ 검색
  searchInput?.addEventListener('input', applyFilter);

  // ✅ 상세 모달
  async function openDetailByTitle(title){
    try{
      const res  = await fetch(`/foods/detail?title=${encodeURIComponent(title)}`,
                               { headers: { 'X-Requested-With':'fetch' }});
      const html = await res.text();
      if (detailBody) detailBody.innerHTML = html;
      modal?.show();
    }catch(e){
      if (detailBody) detailBody.innerHTML = `<div class="p-4 text-center text-danger">상세 정보를 불러오지 못했습니다.</div>`;
      modal?.show();
    }
  }
  grid?.addEventListener('click', e => {
    const btn = e.target.closest('.btn-detail'); if(!btn) return;
    const card = btn.closest('.restaurant-card');
    const title = card?.dataset.title;
    if (title) openDetailByTitle(title);
  });

  // ✅ URL 파라미터로 초기 상태 세팅 (cat, q, title)
  try {
    const sp = new URLSearchParams(location.search);

    const cat = sp.get('cat');   // 예: 흑돼지/갈치조림/고기국수/전복죽/전체
    if (cat && categoryGroup) {
      $$('#categoryGroup .btn').forEach(b => {
        b.classList.add('btn-outline-secondary');
        b.classList.remove('btn-dark');
      });
      const btn = $$('#categoryGroup [data-cat]').find(b => b.dataset.cat === cat);
      if (btn) {
        btn.classList.remove('btn-outline-secondary');
        btn.classList.add('btn-dark');
      }
    }

    const q = sp.get('q');
    if (q && searchInput) searchInput.value = q;

    const t = sp.get('title');
    if (t) openDetailByTitle(t);
  } catch (e) {}

  // 첫 렌더 필터
  applyFilter();
})();
