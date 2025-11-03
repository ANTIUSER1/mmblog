
CREATE TABLE    messages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
	title  VARCHAR(50)  NULL ,
	picture_url  VARCHAR(50)   NULL,
	content   VARCHAR(50) NOT NULL,
	likes_count INT   NULL ,
	comments_count INT   NULL,
    tags VARCHAR(50) NULL,
	CONSTRAINT messages_pk PRIMARY KEY (id)
);

INSERT INTO messages(title, content, likes_count, comments_count )
VALUES ('Иван', 'Иванов' , 222, 7),
       ('Пётр', 'Петров', 55 , 54 ),
        ('TT-2', 'CC-5', 47 , 65),
        ('TT-1', 'CC-1',  2,22),
        ('TT-2','CC-2', 222,44),
        ('TT-2','CC-3', 222,55),
                                              --  ('TT-2','CC-4','PU-4',222,11),
       ('Мария', 'Сидорова' , 87, 454);
/*
INSERT INTO messages
                 (title,content,  likes_count , comments_count )
     VALUES  ('TT-2'  ,'CC-5', 222, 33);
                   -- ('TT-1','CC-1','PU-1',2,22),
                  --('TT-2','CC-2','PU-2',222,44),
               --   ('TT-2','CC-3','PU-3',222,55),
                --  ('TT-2','CC-4','PU-4',222,11),
*/

CREATE TABLE     comments    (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content varchar NULL,
	message_key INT NULL,
	CONSTRAINT comments_pk PRIMARY KEY (id),
	CONSTRAINT comments_message_fk FOREIGN KEY (message_key)
	     REFERENCES messages(id) ON DELETE CASCADE ON UPDATE CASCADE
);



