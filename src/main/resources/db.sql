CREATE TABLE public.summary
(
  id BIGINT PRIMARY KEY NOT NULL,
  context_of_text text NOT NULL,
  summary_of_text text NOT NULL,
  word_chain text,
  filename character varying(255) DEFAULT NULL::character varying,
  class_of_text character varying(255) DEFAULT NULL::character varying,
  class_of_summary character varying(255) DEFAULT NULL::character varying
);

