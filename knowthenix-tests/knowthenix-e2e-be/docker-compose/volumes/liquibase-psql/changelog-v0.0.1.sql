--liquibase formatted sql

--changeset dpopkov:1 labels:v0.0.1
CREATE TYPE "tr_syntax_type" AS ENUM ('plain_text', 'markdown', 'html');
CREATE TYPE "tr_type_type" AS ENUM ('question', 'answer', 'article', 'tutorial');
CREATE TYPE "tr_state_type" AS ENUM ('new', 'edited', 'to_verify', 'verified');
CREATE TYPE "tr_visibility_type" AS ENUM ('public', 'owner', 'registered');

CREATE TABLE "translations" (
	"id" text primary key constraint trs_id_length_ctr check (length("id") < 64),
	"original_id" text constraint trs_original_id_length_ctr check (length("original_id") < 64),
	"language" text constraint trs_language_length_ctr check (length("language") < 128) not null,
	"content" text constraint trs_content_length_ctr check (length(content) < 4096) not null,
	"syntax" tr_syntax_type not null,
	"tr_type" tr_type_type not null,
	"state" tr_state_type not null,
    "aggregate_id" text constraint trs_aggregate_id_length_ctr check (length("aggregate_id") < 64),
	"owner_id" text not null constraint trs_owner_id_length_ctr check (length("owner_id") < 64),
	"visibility" tr_visibility_type not null,
	"lock" text not null constraint trs_lock_length_ctr check (length(id) < 64)
);

CREATE INDEX trs_owner_id_idx on "translations" using hash ("owner_id");

CREATE INDEX trs_tr_type_idx on "translations" using hash ("tr_type");

CREATE INDEX trs_visibility_idx on "translations" using hash ("visibility");
