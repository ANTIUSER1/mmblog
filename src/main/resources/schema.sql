
CREATE TABLE if NOT EXISTS pract.blog.messages (
	id bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
	title varchar NULL,
	picture_url varchar NULL,
	"content"  text NULL,
	tags _varchar NULL,
	likes_count int8 NULL,
	CONSTRAINT messages_pk PRIMARY KEY (id)
);


CREATE TABLE   IF NOT EXISTS  pract.blog.comments    (
	id int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
	content varchar NULL,
	message_key int8 NULL,
	CONSTRAINT comments_pk PRIMARY KEY (id),
	CONSTRAINT comments_message_fk FOREIGN KEY (message_key)
	     REFERENCES pract.blog.messages(id) ON DELETE CASCADE ON UPDATE CASCADE
);

