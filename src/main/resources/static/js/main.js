// 카페사이트 메인 JavaScript

document.addEventListener('DOMContentLoaded', function() {
    
    // 폼 검증
    const forms = document.querySelectorAll('.needs-validation');
    Array.from(forms).forEach(form => {
        form.addEventListener('submit', event => {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        });
    });
    
    // 댓글 토글
    const replyButtons = document.querySelectorAll('.reply-btn');
    replyButtons.forEach(button => {
        button.addEventListener('click', function() {
            const commentId = this.dataset.commentId;
            const replyForm = document.getElementById('reply-form-' + commentId);
            if (replyForm.style.display === 'none' || replyForm.style.display === '') {
                replyForm.style.display = 'block';
            } else {
                replyForm.style.display = 'none';
            }
        });
    });
    
    // 검색어 하이라이트
    const keyword = document.querySelector('input[name="keyword"]')?.value;
    if (keyword && keyword.trim() !== '') {
        highlightSearchTerm(keyword.trim());
    }
    
    // 자동 저장 기능 (글쓰기)
    const titleInput = document.getElementById('title');
    const contentTextarea = document.getElementById('content');
    
    if (titleInput && contentTextarea) {
        // 자동 저장
        setInterval(function() {
            const title = titleInput.value;
            const content = contentTextarea.value;
            
            if (title.trim() !== '' || content.trim() !== '') {
                localStorage.setItem('draft_title', title);
                localStorage.setItem('draft_content', content);
                localStorage.setItem('draft_time', new Date().toISOString());
            }
        }, 30000); // 30초마다 자동 저장
        
        // 페이지 로드시 임시 저장된 내용 복원
        const draftTitle = localStorage.getItem('draft_title');
        const draftContent = localStorage.getItem('draft_content');
        const draftTime = localStorage.getItem('draft_time');
        
        if (draftTitle && draftContent && draftTime) {
            const draftDate = new Date(draftTime);
            const now = new Date();
            const diffHours = (now - draftDate) / (1000 * 60 * 60);
            
            // 24시간 이내의 임시 저장 내용만 복원
            if (diffHours < 24) {
                if (confirm('임시 저장된 내용이 있습니다. 복원하시겠습니까?')) {
                    titleInput.value = draftTitle;
                    contentTextarea.value = draftContent;
                }
            }
        }
    }
    
    // 글 작성 완료시 임시 저장 내용 삭제
    const writeForm = document.querySelector('form[action*="/write"]');
    if (writeForm) {
        writeForm.addEventListener('submit', function() {
            localStorage.removeItem('draft_title');
            localStorage.removeItem('draft_content');
            localStorage.removeItem('draft_time');
        });
    }
});

// 검색어 하이라이트 함수
function highlightSearchTerm(keyword) {
    const elements = document.querySelectorAll('.board-title, .board-content');
    elements.forEach(element => {
        const text = element.textContent;
        const highlightedText = text.replace(
            new RegExp(`(${keyword})`, 'gi'),
            '<mark>$1</mark>'
        );
        if (text !== highlightedText) {
            element.innerHTML = highlightedText;
        }
    });
}

// 페이지 상단으로 스크롤
function scrollToTop() {
    window.scrollTo({
        top: 0,
        behavior: 'smooth'
    });
}

// 확인 대화상자
function confirmDelete(message) {
    return confirm(message || '정말 삭제하시겠습니까?');
}

// 텍스트 에리어 자동 높이 조절
function autoResize(textarea) {
    textarea.style.height = 'auto';
    textarea.style.height = textarea.scrollHeight + 'px';
}

// 이미지 미리보기
function previewImage(input, previewId) {
    const file = input.files[0];
    const preview = document.getElementById(previewId);
    
    if (file) {
        const reader = new FileReader();
        reader.onload = function(e) {
            preview.src = e.target.result;
            preview.style.display = 'block';
        };
        reader.readAsDataURL(file);
    } else {
        preview.style.display = 'none';
    }
}