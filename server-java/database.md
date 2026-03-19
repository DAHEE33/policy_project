# PolicyPing Database Schema v4.2 (PostgreSQL)

## 0. Custom Types
CREATE TYPE policy_status AS ENUM ('PENDING', 'APPROVED', 'REJECTED', 'EXPIRED');
CREATE TYPE review_status AS ENUM ('PENDING', 'APPROVED', 'REJECTED');
CREATE TYPE notification_type AS ENUM ('NEW_MATCH', 'D7', 'D3', 'D1');
CREATE TYPE subscription_tier AS ENUM ('FREE', 'PRO');
CREATE TYPE admin_role AS ENUM ('SUPER_ADMIN', 'EDITOR');

---

## 1. Admins (관리자)
| 컬럼명 | 타입 | 제약 조건 / 설명 |
| :--- | :--- | :--- |
| `id` | BIGSERIAL | Primary Key |
| `email` | VARCHAR(255) | UNIQUE, 관리자 로그인 ID |
| `password_hash` | VARCHAR(255) | Bcrypt 해싱 |
| `role` | admin_role | DEFAULT 'EDITOR' |
| `created_at` | TIMESTAMPTZ | DEFAULT CURRENT_TIMESTAMP |

---

## 2. Users (일반 사용자)
| 컬럼명 | 타입 | 제약 조건 / 설명 |
| :--- | :--- | :--- |
| `id` | BIGSERIAL | Primary Key |
| `email` | VARCHAR(255) | UNIQUE, 사용자 로그인 ID (소셜 연동 시 이메일) |
| `password_hash` | VARCHAR(255) | Bcrypt 해싱 (소셜 로그인은 NULL 허용 또는 더미값) |
| `oauth_provider` | VARCHAR(50) | KAKAO, GOOGLE 등 (로컬 로그인은 NULL) |
| `oauth_id` | VARCHAR(255) | 소셜 제공자가 주는 고유 ID |
| `birth_year` | INTEGER | 나이 계산용 |
| `job_status` | VARCHAR(50) | 취업중, 구직중 등 |
| `income_bracket` | VARCHAR(255) | AES-256 암호화 저장 |
| `region` | VARCHAR(255) | AES-256 암호화 저장 |
| `housing_type` | VARCHAR(50) | 자가/전세/월세/기숙사 |
| `subscription_tier` | subscription_tier | DEFAULT 'FREE' |
| `is_banned` | BOOLEAN | DEFAULT FALSE (제재 여부) |
| `banned_by` | BIGINT | REFERENCES admins(id) ON DELETE SET NULL |
| `banned_at` | TIMESTAMPTZ | 제재 처리 일시 |
| `created_at` | TIMESTAMPTZ | DEFAULT CURRENT_TIMESTAMP |

---

## 3. Policies (정책 메인 데이터)
| 컬럼명 | 타입 | 제약 조건 / 설명 |
| :--- | :--- | :--- |
| `id` | BIGSERIAL | Primary Key |
| `title` | VARCHAR(255) | 정책명 |
| `category` | VARCHAR(50) | 지원금, 취업/창업 등 |
| `target_age_min` | INTEGER | 나이 하한 (제한 없으면 NULL) |
| `target_age_max` | INTEGER | 나이 상한 (제한 없으면 NULL) |
| `income_criteria` | VARCHAR(100) | 중위소득 기준 |
| `region` | VARCHAR(100) | 지역 제한 (전국이면 NULL 또는 '전국') |
| `support_amount` | TEXT | 지원 금액 요약 |
| `apply_start` | DATE | 신청 시작일 |
| `apply_end` | DATE | 신청 마감일 |
| `apply_url` | TEXT | 원본 신청 링크 |
| `source_site` | VARCHAR(50) | 출처 사이트 (예: 복지로, 정부24) |
| `summary` | VARCHAR(200) | AI 최대 200자 요약 |
| `status` | policy_status | PENDING, APPROVED, REJECTED, EXPIRED |
| `source_url_hash` | VARCHAR(255) | UNIQUE, 원본 URL 해시 (중복 크롤링 방지) |
| `reviewed_by` | BIGINT | REFERENCES admins(id) ON DELETE SET NULL |
| `reviewed_at` | TIMESTAMPTZ | 검수 일시 |
| `created_at` | TIMESTAMPTZ | DEFAULT CURRENT_TIMESTAMP |

---

## 4. Normalization Tables (매칭 최적화용)
**[user_interested_categories]**
| 컬럼명 | 타입 | 제약 조건 |
| :--- | :--- | :--- |
| `user_id` | BIGINT | REFERENCES users(id) ON DELETE CASCADE |
| `category` | VARCHAR(50) | |
| *PK* | | `(user_id, category)` |

**[policy_target_jobs]**
| 컬럼명 | 타입 | 제약 조건 |
| :--- | :--- | :--- |
| `policy_id` | BIGINT | REFERENCES policies(id) ON DELETE CASCADE |
| `job_status` | VARCHAR(50) | |
| *PK* | | `(policy_id, job_status)` |

---

## 5. Community & Reviews (커뮤니티)
**[policy_reviews]**
| 컬럼명 | 타입 | 제약 조건 |
| :--- | :--- | :--- |
| `id` | BIGSERIAL | Primary Key |
| `policy_id` | BIGINT | REFERENCES policies(id) ON DELETE CASCADE |
| `approval_tips` | TEXT | |
| `status` | review_status | PENDING, APPROVED, REJECTED |
| `reviewed_by` | BIGINT | REFERENCES admins(id) ON DELETE SET NULL |
| `reviewed_at` | TIMESTAMPTZ| |

**[policy_qna]**
| 컬럼명 | 타입 | 제약 조건 |
| :--- | :--- | :--- |
| `id` | BIGSERIAL | Primary Key |
| `policy_id` | BIGINT | REFERENCES policies(id) ON DELETE CASCADE |
| `user_id` | BIGINT | REFERENCES users(id) ON DELETE SET NULL |
| `is_anonymous` | BOOLEAN | DEFAULT TRUE |
| `anonymous_nickname`| VARCHAR(255)| 사용자 ID 기반 가상 닉네임 (예: 익명_A1B2) |
| `content` | TEXT | |
| `likes_count` | INTEGER | DEFAULT 0 |
| `is_deleted` | BOOLEAN | DEFAULT FALSE |
| `deleted_by` | BIGINT | REFERENCES admins(id) ON DELETE SET NULL |
| `deleted_at` | TIMESTAMPTZ | |
| `created_at` | TIMESTAMPTZ | DEFAULT CURRENT_TIMESTAMP |

**[qna_likes]** (어뷰징 방지용 다대다 매핑)
| 컬럼명 | 타입 | 제약 조건 |
| :--- | :--- | :--- |
| `qna_id` | BIGINT | REFERENCES policy_qna(id) ON DELETE CASCADE |
| `user_id` | BIGINT | REFERENCES users(id) ON DELETE CASCADE |
| *PK* | | `(qna_id, user_id)` |

**[qna_comments]**
| 컬럼명 | 타입 | 제약 조건 |
| :--- | :--- | :--- |
| `id` | BIGSERIAL | Primary Key |
| `qna_id` | BIGINT | REFERENCES policy_qna(id) ON DELETE CASCADE |
| `user_id` | BIGINT | REFERENCES users(id) ON DELETE SET NULL |
| `content` | TEXT | |
| `is_deleted` | BOOLEAN | DEFAULT FALSE |
| `deleted_by` | BIGINT | REFERENCES admins(id) ON DELETE SET NULL |
| `deleted_at` | TIMESTAMPTZ | |
| `created_at` | TIMESTAMPTZ | DEFAULT CURRENT_TIMESTAMP |

---

## 6. User_Matches & Notification_Logs
**[user_matches]**
(PK: `id`, `user_id`, `policy_id`, `match_score`, `created_at`, UNIQUE(`user_id`, `policy_id`))

**[notification_logs]**
(PK: `id`, `match_id` (FK), `noti_type`, `sent_at`, UNIQUE(`match_id`, `noti_type`))