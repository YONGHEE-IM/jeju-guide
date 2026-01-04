// src/main/resources/static/js/attractions.js
(() => {
  const $ = (s, c=document)=>c.querySelector(s);
  const $$ = (s, c=document)=>Array.from(c.querySelectorAll(s));

  const searchInput = $('#searchInput');
  const categoryGroup = $('#categoryGroup');
  const grid = $('#grid');
  const emptyState = $('#emptyState');
  const detailBody = $('#detailBody');
  const modal = new bootstrap.Modal($('#attractionModal'));

  const norm = s => (s||'').toLowerCase().trim();

  function applyFilter(){
    const q = norm(searchInput?.value);
    const active = $('#categoryGroup .btn.btn-dark');
    const cat = active ? active.dataset.cat : '전체';
    let visible = 0;

    $$('.attraction-card').forEach(card => {
      const title = norm(card.querySelector('.h6')?.textContent);
      const desc  = norm(card.querySelector('.card-text')?.textContent);
      const loc   = norm(card.querySelector('.text-secondary span')?.textContent);
      const okSearch = [title, desc, loc].some(v => (v||'').includes(q));
      const okCat = (cat === '전체') || (card.dataset.category === cat);
      const show = okSearch && okCat;
      card.parentElement.classList.toggle('d-none', !show);
      if (show) visible++;
    });

    emptyState.classList.toggle('d-none', visible !== 0);
  }

  categoryGroup?.addEventListener('click', e => {
    const btn = e.target.closest('button[data-cat]'); if(!btn) return;
    $$('#categoryGroup .btn').forEach(b => { b.classList.add('btn-outline-secondary'); b.classList.remove('btn-dark'); });
    btn.classList.remove('btn-outline-secondary'); btn.classList.add('btn-dark');
    applyFilter();
  });

  searchInput?.addEventListener('input', applyFilter);

  // 상세 가져와서 모달에 주입
  async function openDetailByTitle(title){
    try{
      const res = await fetch(`/spots/detail?title=${encodeURIComponent(title)}`,
                              { headers: { 'X-Requested-With':'fetch' }});
      const html = await res.text();
      detailBody.innerHTML = html;
      modal.show();
    }catch(e){
      detailBody.innerHTML = `<div class="p-4 text-center text-danger">상세 정보를 불러오지 못했습니다.</div>`;
      modal.show();
    }
  }
  grid?.addEventListener('click', e => {
    const btn = e.target.closest('.btn-detail'); if(!btn) return;
    const card = btn.closest('.attraction-card');
    const title = card?.dataset.title;
    if (title) openDetailByTitle(title);
  });
  
  try {
      const sp = new URLSearchParams(location.search);
      const t = sp.get('title');
      if (t) {
        openDetailByTitle(t);   // 이미 정의된 함수: 서버에서 상세 HTML 받아 모달 표시
      }
    } catch (e) { /* noop */ }

  applyFilter();
})();
