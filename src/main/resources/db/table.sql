/* 회원 */
CREATE TABLE tbl_user
(
    `id`                            BIGINT           NOT NULL    AUTO_INCREMENT         COMMENT '회원 식별자',
    `email`                         VARCHAR(100)     NOT NULL                           COMMENT '이메일',
    `nickname`                      VARCHAR(30)      NOT NULL                           COMMENT '닉네임',
    `reg_date`                      DATETIME         NOT NULL                           COMMENT '등록일시',
    `mod_date`                      DATETIME         NOT NULL                           COMMENT '수정일시',
    PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8mb4 COMMENT '회원';
