INSERT INTO account (email, password, nickname, introduce, refresh_token)
VALUES ('이메일 1',
        '비밀번호 1',
        '닉네임 1',
        '소개 1',
        '리프레쉬 토큰 1');

INSERT INTO category (name)
VALUES ('카테고리 1');

INSERT INTO study_image (filename)
VALUES ('파일명 1');

INSERT INTO study (name, image_id, category_id, type, location, recruitment_number, visibility, join_type, introduce,
                   access_url, creator_id)
VALUES ('스터디명 1',
        1,
        1,
        'OFFLINE',
        '장소 1',
        '10',
        'PUBLIC',
        'FREE',
        '소개 1',
        'URL 1',
        (SELECT LAST_INSERT_ID() FROM account));

INSERT INTO participant (user_id, study_id)
VALUES (1, 1);

INSERT INTO study (name, image_id, category_id, type, location, recruitment_number, visibility, join_type, introduce,
                   access_url, creator_id)
VALUES ('스터디명 2',
        1,
        1,
        'OFFLINE',
        '장소 2',
        '10',
        'PRIVATE',
        'FREE',
        '소개 2',
        'URL 2',
        (SELECT LAST_INSERT_ID() FROM account));

INSERT INTO account (email, password, nickname, introduce, refresh_token)
VALUES ('이메일 2',
        '비밀번호 2',
        '닉네임 2',
        '소개 2',
        '리프레쉬 토큰 2');

INSERT INTO applicant (study_id, user_id, message)
VALUES (2, 2, '메시지 2');