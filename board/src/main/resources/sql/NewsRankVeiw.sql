CREATE OR REPLACE VIEW news_rank_view AS
SELECT 
    n.id AS news_id,
    GREATEST(
    (
        (n.view_count * 1) +
        (SELECT COUNT(*)
         FROM news_comment c 
         WHERE news_id = n.id) * 4 +
        (SELECT COUNT(*)
         FROM news_recommend r 
         WHERE 1+1 
         AND r.news_id = n.id
         AND r.type = 'RECOMMEND') * 2 +
         (SELECT COUNT(*)
         FROM news_recommend r 
         WHERE 1+1 
         AND r.news_id = n.id
         AND r.type = 'UN_RECOMMEND') * -2
    ) - LOG2(2 + TIMESTAMPDIFF(HOUR, n.created_at, NOW())),-10) AS rank_score
FROM news n
LIMIT 10;
-- 뷰카운트 가중치 1 <= 단순 조회 이므로 노력이 제일 적음
-- 덧글 가중치 4 <= 사용자가 투자 노력이 더많음
-- 뉴스 추천 가중치 2 <= 덧글달기보다 쉬움
-- 뉴스 비 추천 가중치 -2 ``
-- 시간은 LOG 스케일로 감소 초기 감쇄는 많지만 시간이 흐를수록 감쇄 효과 줄어듬
-- 시간이 흐를 수록 SCORE 는 무한이 음수 <= 로그는 수렴 함수가 아니기 때문에 이후 너무 오래된 글일 경우 오버플로 될꺼같아 GREATEST 사용