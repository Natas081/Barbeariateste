CREATE DATABASE IF NOT EXISTS barbearia;
USE barbearia;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS item_atendimento;
DROP TABLE IF EXISTS atendimento;
DROP TABLE IF EXISTS agendamento;
DROP TABLE IF EXISTS servico;
DROP TABLE IF EXISTS dependente_telefone;
DROP TABLE IF EXISTS dependente;
DROP TABLE IF EXISTS cliente_telefone;
DROP TABLE IF EXISTS gerente;
DROP TABLE IF EXISTS barbeiro;
DROP TABLE IF EXISTS profissional_telefone;
DROP TABLE IF EXISTS profissional;
DROP TABLE IF EXISTS cliente;

CREATE TABLE cliente (
                         cpf VARCHAR(11) PRIMARY KEY,
                         nome VARCHAR(100) NOT NULL,
                         rua VARCHAR(100) NOT NULL,
                         numero VARCHAR(10) NOT NULL,

                         CONSTRAINT chk_cliente_cpf
                             CHECK (cpf REGEXP '^[0-9]{11}$')
    );

CREATE TABLE cliente_telefone (
                                  cpf_cliente VARCHAR(11) NOT NULL,
                                  telefone VARCHAR(15) NOT NULL,

                                  CONSTRAINT pk_cliente_telefone
                                      PRIMARY KEY (cpf_cliente, telefone),

                                  CONSTRAINT fk_cliente_telefone_cliente
                                      FOREIGN KEY (cpf_cliente)
                                          REFERENCES cliente(cpf)
                                          ON UPDATE CASCADE
                                          ON DELETE CASCADE
);

CREATE TABLE dependente (
                            cpf_dependente VARCHAR(11) NOT NULL,
                            cpf_cliente VARCHAR(11) NOT NULL,
                            nome VARCHAR(100) NOT NULL,
                            rua VARCHAR(100) NOT NULL,
                            numero VARCHAR(10) NOT NULL,

                            CONSTRAINT pk_dependente
                                PRIMARY KEY (cpf_dependente, cpf_cliente),

                            CONSTRAINT uq_dependente_cpf
                                UNIQUE (cpf_dependente),

                            CONSTRAINT chk_dependente_cpf
                                CHECK (cpf_dependente REGEXP '^[0-9]{11}$'),

    CONSTRAINT fk_dependente_cliente
        FOREIGN KEY (cpf_cliente)
        REFERENCES cliente(cpf)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE dependente_telefone (
                                     cpf_dependente VARCHAR(11) NOT NULL,
                                     cpf_cliente VARCHAR(11) NOT NULL,
                                     telefone VARCHAR(15) NOT NULL,

                                     CONSTRAINT pk_dependente_telefone
                                         PRIMARY KEY (cpf_dependente, cpf_cliente, telefone),

                                     CONSTRAINT fk_dependente_telefone_dependente
                                         FOREIGN KEY (cpf_dependente, cpf_cliente)
                                             REFERENCES dependente(cpf_dependente, cpf_cliente)
                                             ON UPDATE CASCADE
                                             ON DELETE CASCADE
);

CREATE TABLE profissional (
                              cpf VARCHAR(11) PRIMARY KEY,
                              nome VARCHAR(100) NOT NULL,
                              rua VARCHAR(100) NOT NULL,
                              numero VARCHAR(10) NOT NULL,

                              CONSTRAINT chk_profissional_cpf
                                  CHECK (cpf REGEXP '^[0-9]{11}$')
    );

CREATE TABLE profissional_telefone (
                                       cpf_profissional VARCHAR(11) NOT NULL,
                                       telefone VARCHAR(15) NOT NULL,

                                       CONSTRAINT pk_profissional_telefone
                                           PRIMARY KEY (cpf_profissional, telefone),

                                       CONSTRAINT fk_profissional_telefone_profissional
                                           FOREIGN KEY (cpf_profissional)
                                               REFERENCES profissional(cpf)
                                               ON UPDATE CASCADE
                                               ON DELETE CASCADE
);

CREATE TABLE barbeiro (
                          cpf_profissional VARCHAR(11) PRIMARY KEY,
                          especialidade VARCHAR(50) NOT NULL DEFAULT 'Corte masculino',
                          horario_trabalho VARCHAR(50) NOT NULL DEFAULT '08:00-18:00',
                          cpf_supervisor VARCHAR(11),

                          CONSTRAINT fk_barbeiro_profissional
                              FOREIGN KEY (cpf_profissional)
                                  REFERENCES profissional(cpf)
                                  ON UPDATE CASCADE
                                  ON DELETE CASCADE,

                          CONSTRAINT fk_barbeiro_supervisor
                              FOREIGN KEY (cpf_supervisor)
                                  REFERENCES barbeiro(cpf_profissional)
                                  ON UPDATE CASCADE
                                  ON DELETE SET NULL
);

CREATE TABLE gerente (
                         cpf_profissional VARCHAR(11) PRIMARY KEY,
                         bonificacao DECIMAL(10,2) NOT NULL DEFAULT 0.00,

                         CONSTRAINT chk_gerente_bonificacao
                             CHECK (bonificacao >= 0),

                         CONSTRAINT fk_gerente_profissional
                             FOREIGN KEY (cpf_profissional)
                                 REFERENCES profissional(cpf)
                                 ON UPDATE CASCADE
                                 ON DELETE CASCADE
);

CREATE TABLE servico (
                         id_servico INT AUTO_INCREMENT PRIMARY KEY,
                         nome VARCHAR(100) NOT NULL UNIQUE,
                         descricao VARCHAR(255),
                         duracao INTEGER NOT NULL,
                         preco DECIMAL(10,2) NOT NULL,

                         CONSTRAINT chk_servico_duracao
                             CHECK (duracao > 0),

                         CONSTRAINT chk_servico_preco
                             CHECK (preco >= 0)
);

CREATE TABLE agendamento (
                             id_agendamento INT AUTO_INCREMENT PRIMARY KEY,
                             status VARCHAR(20) NOT NULL DEFAULT 'Agendado',
                             cpf_cliente VARCHAR(11) NOT NULL,
                             cpf_barbeiro VARCHAR(11) NOT NULL,

                             CONSTRAINT chk_agendamento_status
                                 CHECK (status IN ('Agendado', 'Confirmado', 'Concluido', 'Cancelado')),

                             CONSTRAINT fk_agendamento_cliente
                                 FOREIGN KEY (cpf_cliente)
                                     REFERENCES cliente(cpf)
                                     ON UPDATE CASCADE
                                     ON DELETE RESTRICT,

                             CONSTRAINT fk_agendamento_barbeiro
                                 FOREIGN KEY (cpf_barbeiro)
                                     REFERENCES barbeiro(cpf_profissional)
                                     ON UPDATE CASCADE
                                     ON DELETE RESTRICT
);


CREATE TABLE atendimento (
                             id_atendimento INT AUTO_INCREMENT PRIMARY KEY,
                             horario_inicio TIMESTAMP NOT NULL,
                             horario_final TIMESTAMP,
                             id_agendamento INTEGER UNIQUE,

                             CONSTRAINT chk_atendimento_horario
                                 CHECK (horario_final IS NULL OR horario_final > horario_inicio),

                             CONSTRAINT fk_atendimento_agendamento
                                 FOREIGN KEY (id_agendamento)
                                     REFERENCES agendamento(id_agendamento)
                                     ON UPDATE CASCADE
                                     ON DELETE SET NULL
);

CREATE TABLE item_atendimento (
                                  id_atendimento INTEGER NOT NULL,
                                  id_servico INTEGER NOT NULL,
                                  preco_praticado DECIMAL(10,2) NOT NULL,

                                  CONSTRAINT pk_item_atendimento
                                      PRIMARY KEY (id_atendimento, id_servico),

                                  CONSTRAINT chk_preco_praticado
                                      CHECK (preco_praticado >= 0),

                                  CONSTRAINT fk_item_atendimento_atendimento
                                      FOREIGN KEY (id_atendimento)
                                          REFERENCES atendimento(id_atendimento)
                                          ON UPDATE CASCADE
                                          ON DELETE CASCADE,

                                  CONSTRAINT fk_item_atendimento_servico
                                      FOREIGN KEY (id_servico)
                                          REFERENCES servico(id_servico)
                                          ON UPDATE CASCADE
                                          ON DELETE RESTRICT
);


INSERT INTO cliente (cpf, nome, rua, numero) VALUES
                                                 ('10000000001', 'Cliente 1', 'Rua Cliente 1', '101'),
                                                 ('10000000002', 'Cliente 2', 'Rua Cliente 2', '102'),
                                                 ('10000000003', 'Cliente 3', 'Rua Cliente 3', '103'),
                                                 ('10000000004', 'Cliente 4', 'Rua Cliente 4', '104'),
                                                 ('10000000005', 'Cliente 5', 'Rua Cliente 5', '105'),
                                                 ('10000000006', 'Cliente 6', 'Rua Cliente 6', '106'),
                                                 ('10000000007', 'Cliente 7', 'Rua Cliente 7', '107'),
                                                 ('10000000008', 'Cliente 8', 'Rua Cliente 8', '108'),
                                                 ('10000000009', 'Cliente 9', 'Rua Cliente 9', '109'),
                                                 ('10000000010', 'Cliente 10', 'Rua Cliente 10', '110'),
                                                 ('10000000011', 'Cliente 11', 'Rua Cliente 11', '111'),
                                                 ('10000000012', 'Cliente 12', 'Rua Cliente 12', '112'),
                                                 ('10000000013', 'Cliente 13', 'Rua Cliente 13', '113'),
                                                 ('10000000014', 'Cliente 14', 'Rua Cliente 14', '114'),
                                                 ('10000000015', 'Cliente 15', 'Rua Cliente 15', '115'),
                                                 ('10000000016', 'Cliente 16', 'Rua Cliente 16', '116'),
                                                 ('10000000017', 'Cliente 17', 'Rua Cliente 17', '117'),
                                                 ('10000000018', 'Cliente 18', 'Rua Cliente 18', '118'),
                                                 ('10000000019', 'Cliente 19', 'Rua Cliente 19', '119'),
                                                 ('10000000020', 'Cliente 20', 'Rua Cliente 20', '120'),
                                                 ('10000000021', 'Cliente 21', 'Rua Cliente 21', '121'),
                                                 ('10000000022', 'Cliente 22', 'Rua Cliente 22', '122'),
                                                 ('10000000023', 'Cliente 23', 'Rua Cliente 23', '123'),
                                                 ('10000000024', 'Cliente 24', 'Rua Cliente 24', '124'),
                                                 ('10000000025', 'Cliente 25', 'Rua Cliente 25', '125'),
                                                 ('10000000026', 'Cliente 26', 'Rua Cliente 26', '126'),
                                                 ('10000000027', 'Cliente 27', 'Rua Cliente 27', '127'),
                                                 ('10000000028', 'Cliente 28', 'Rua Cliente 28', '128'),
                                                 ('10000000029', 'Cliente 29', 'Rua Cliente 29', '129'),
                                                 ('10000000030', 'Cliente 30', 'Rua Cliente 30', '130');

INSERT INTO cliente_telefone (cpf_cliente, telefone) VALUES
                                                         ('10000000001', '81980000001'),
                                                         ('10000000002', '81980000002'),
                                                         ('10000000003', '81980000003'),
                                                         ('10000000004', '81980000004'),
                                                         ('10000000005', '81980000005'),
                                                         ('10000000006', '81980000006'),
                                                         ('10000000007', '81980000007'),
                                                         ('10000000008', '81980000008'),
                                                         ('10000000009', '81980000009'),
                                                         ('10000000010', '81980000010'),
                                                         ('10000000011', '81980000011'),
                                                         ('10000000012', '81980000012'),
                                                         ('10000000013', '81980000013'),
                                                         ('10000000014', '81980000014'),
                                                         ('10000000015', '81980000015'),
                                                         ('10000000016', '81980000016'),
                                                         ('10000000017', '81980000017'),
                                                         ('10000000018', '81980000018'),
                                                         ('10000000019', '81980000019'),
                                                         ('10000000020', '81980000020'),
                                                         ('10000000021', '81980000021'),
                                                         ('10000000022', '81980000022'),
                                                         ('10000000023', '81980000023'),
                                                         ('10000000024', '81980000024'),
                                                         ('10000000025', '81980000025'),
                                                         ('10000000026', '81980000026'),
                                                         ('10000000027', '81980000027'),
                                                         ('10000000028', '81980000028'),
                                                         ('10000000029', '81980000029'),
                                                         ('10000000030', '81980000030');

INSERT INTO dependente (cpf_dependente, cpf_cliente, nome, rua, numero) VALUES
                                                                            ('20000000001', '10000000001', 'Dependente 1', 'Rua Dependente 1', '201'),
                                                                            ('20000000002', '10000000002', 'Dependente 2', 'Rua Dependente 2', '202'),
                                                                            ('20000000003', '10000000003', 'Dependente 3', 'Rua Dependente 3', '203'),
                                                                            ('20000000004', '10000000004', 'Dependente 4', 'Rua Dependente 4', '204'),
                                                                            ('20000000005', '10000000005', 'Dependente 5', 'Rua Dependente 5', '205'),
                                                                            ('20000000006', '10000000006', 'Dependente 6', 'Rua Dependente 6', '206'),
                                                                            ('20000000007', '10000000007', 'Dependente 7', 'Rua Dependente 7', '207'),
                                                                            ('20000000008', '10000000008', 'Dependente 8', 'Rua Dependente 8', '208'),
                                                                            ('20000000009', '10000000009', 'Dependente 9', 'Rua Dependente 9', '209'),
                                                                            ('20000000010', '10000000010', 'Dependente 10', 'Rua Dependente 10', '210'),
                                                                            ('20000000011', '10000000011', 'Dependente 11', 'Rua Dependente 11', '211'),
                                                                            ('20000000012', '10000000012', 'Dependente 12', 'Rua Dependente 12', '212'),
                                                                            ('20000000013', '10000000013', 'Dependente 13', 'Rua Dependente 13', '213'),
                                                                            ('20000000014', '10000000014', 'Dependente 14', 'Rua Dependente 14', '214'),
                                                                            ('20000000015', '10000000015', 'Dependente 15', 'Rua Dependente 15', '215'),
                                                                            ('20000000016', '10000000016', 'Dependente 16', 'Rua Dependente 16', '216'),
                                                                            ('20000000017', '10000000017', 'Dependente 17', 'Rua Dependente 17', '217'),
                                                                            ('20000000018', '10000000018', 'Dependente 18', 'Rua Dependente 18', '218'),
                                                                            ('20000000019', '10000000019', 'Dependente 19', 'Rua Dependente 19', '219'),
                                                                            ('20000000020', '10000000020', 'Dependente 20', 'Rua Dependente 20', '220'),
                                                                            ('20000000021', '10000000021', 'Dependente 21', 'Rua Dependente 21', '221'),
                                                                            ('20000000022', '10000000022', 'Dependente 22', 'Rua Dependente 22', '222'),
                                                                            ('20000000023', '10000000023', 'Dependente 23', 'Rua Dependente 23', '223'),
                                                                            ('20000000024', '10000000024', 'Dependente 24', 'Rua Dependente 24', '224'),
                                                                            ('20000000025', '10000000025', 'Dependente 25', 'Rua Dependente 25', '225'),
                                                                            ('20000000026', '10000000026', 'Dependente 26', 'Rua Dependente 26', '226'),
                                                                            ('20000000027', '10000000027', 'Dependente 27', 'Rua Dependente 27', '227'),
                                                                            ('20000000028', '10000000028', 'Dependente 28', 'Rua Dependente 28', '228'),
                                                                            ('20000000029', '10000000029', 'Dependente 29', 'Rua Dependente 29', '229'),
                                                                            ('20000000030', '10000000030', 'Dependente 30', 'Rua Dependente 30', '230');

INSERT INTO dependente_telefone (cpf_dependente, cpf_cliente, telefone) VALUES
                                                                            ('20000000001', '10000000001', '81970000001'),
                                                                            ('20000000002', '10000000002', '81970000002'),
                                                                            ('20000000003', '10000000003', '81970000003'),
                                                                            ('20000000004', '10000000004', '81970000004'),
                                                                            ('20000000005', '10000000005', '81970000005'),
                                                                            ('20000000006', '10000000006', '81970000006'),
                                                                            ('20000000007', '10000000007', '81970000007'),
                                                                            ('20000000008', '10000000008', '81970000008'),
                                                                            ('20000000009', '10000000009', '81970000009'),
                                                                            ('20000000010', '10000000010', '81970000010'),
                                                                            ('20000000011', '10000000011', '81970000011'),
                                                                            ('20000000012', '10000000012', '81970000012'),
                                                                            ('20000000013', '10000000013', '81970000013'),
                                                                            ('20000000014', '10000000014', '81970000014'),
                                                                            ('20000000015', '10000000015', '81970000015'),
                                                                            ('20000000016', '10000000016', '81970000016'),
                                                                            ('20000000017', '10000000017', '81970000017'),
                                                                            ('20000000018', '10000000018', '81970000018'),
                                                                            ('20000000019', '10000000019', '81970000019'),
                                                                            ('20000000020', '10000000020', '81970000020'),
                                                                            ('20000000021', '10000000021', '81970000021'),
                                                                            ('20000000022', '10000000022', '81970000022'),
                                                                            ('20000000023', '10000000023', '81970000023'),
                                                                            ('20000000024', '10000000024', '81970000024'),
                                                                            ('20000000025', '10000000025', '81970000025'),
                                                                            ('20000000026', '10000000026', '81970000026'),
                                                                            ('20000000027', '10000000027', '81970000027'),
                                                                            ('20000000028', '10000000028', '81970000028'),
                                                                            ('20000000029', '10000000029', '81970000029'),
                                                                            ('20000000030', '10000000030', '81970000030');


INSERT INTO profissional (cpf, nome, rua, numero) VALUES
                                                      ('30000000001', 'Profissional 1', 'Rua Profissional 1', '301'),
                                                      ('30000000002', 'Profissional 2', 'Rua Profissional 2', '302'),
                                                      ('30000000003', 'Profissional 3', 'Rua Profissional 3', '303'),
                                                      ('30000000004', 'Profissional 4', 'Rua Profissional 4', '304'),
                                                      ('30000000005', 'Profissional 5', 'Rua Profissional 5', '305'),
                                                      ('30000000006', 'Profissional 6', 'Rua Profissional 6', '306'),
                                                      ('30000000007', 'Profissional 7', 'Rua Profissional 7', '307'),
                                                      ('30000000008', 'Profissional 8', 'Rua Profissional 8', '308'),
                                                      ('30000000009', 'Profissional 9', 'Rua Profissional 9', '309'),
                                                      ('30000000010', 'Profissional 10', 'Rua Profissional 10', '310'),
                                                      ('30000000011', 'Profissional 11', 'Rua Profissional 11', '311'),
                                                      ('30000000012', 'Profissional 12', 'Rua Profissional 12', '312'),
                                                      ('30000000013', 'Profissional 13', 'Rua Profissional 13', '313'),
                                                      ('30000000014', 'Profissional 14', 'Rua Profissional 14', '314'),
                                                      ('30000000015', 'Profissional 15', 'Rua Profissional 15', '315'),
                                                      ('30000000016', 'Profissional 16', 'Rua Profissional 16', '316'),
                                                      ('30000000017', 'Profissional 17', 'Rua Profissional 17', '317'),
                                                      ('30000000018', 'Profissional 18', 'Rua Profissional 18', '318'),
                                                      ('30000000019', 'Profissional 19', 'Rua Profissional 19', '319'),
                                                      ('30000000020', 'Profissional 20', 'Rua Profissional 20', '320'),
                                                      ('30000000021', 'Profissional 21', 'Rua Profissional 21', '321'),
                                                      ('30000000022', 'Profissional 22', 'Rua Profissional 22', '322'),
                                                      ('30000000023', 'Profissional 23', 'Rua Profissional 23', '323'),
                                                      ('30000000024', 'Profissional 24', 'Rua Profissional 24', '324'),
                                                      ('30000000025', 'Profissional 25', 'Rua Profissional 25', '325'),
                                                      ('30000000026', 'Profissional 26', 'Rua Profissional 26', '326'),
                                                      ('30000000027', 'Profissional 27', 'Rua Profissional 27', '327'),
                                                      ('30000000028', 'Profissional 28', 'Rua Profissional 28', '328'),
                                                      ('30000000029', 'Profissional 29', 'Rua Profissional 29', '329'),
                                                      ('30000000030', 'Profissional 30', 'Rua Profissional 30', '330'),
                                                      ('30000000031', 'Profissional 31', 'Rua Profissional 31', '331'),
                                                      ('30000000032', 'Profissional 32', 'Rua Profissional 32', '332'),
                                                      ('30000000033', 'Profissional 33', 'Rua Profissional 33', '333'),
                                                      ('30000000034', 'Profissional 34', 'Rua Profissional 34', '334'),
                                                      ('30000000035', 'Profissional 35', 'Rua Profissional 35', '335'),
                                                      ('30000000036', 'Profissional 36', 'Rua Profissional 36', '336'),
                                                      ('30000000037', 'Profissional 37', 'Rua Profissional 37', '337'),
                                                      ('30000000038', 'Profissional 38', 'Rua Profissional 38', '338'),
                                                      ('30000000039', 'Profissional 39', 'Rua Profissional 39', '339'),
                                                      ('30000000040', 'Profissional 40', 'Rua Profissional 40', '340'),
                                                      ('30000000041', 'Profissional 41', 'Rua Profissional 41', '341'),
                                                      ('30000000042', 'Profissional 42', 'Rua Profissional 42', '342'),
                                                      ('30000000043', 'Profissional 43', 'Rua Profissional 43', '343'),
                                                      ('30000000044', 'Profissional 44', 'Rua Profissional 44', '344'),
                                                      ('30000000045', 'Profissional 45', 'Rua Profissional 45', '345'),
                                                      ('30000000046', 'Profissional 46', 'Rua Profissional 46', '346'),
                                                      ('30000000047', 'Profissional 47', 'Rua Profissional 47', '347'),
                                                      ('30000000048', 'Profissional 48', 'Rua Profissional 48', '348'),
                                                      ('30000000049', 'Profissional 49', 'Rua Profissional 49', '349'),
                                                      ('30000000050', 'Profissional 50', 'Rua Profissional 50', '350'),
                                                      ('30000000051', 'Profissional 51', 'Rua Profissional 51', '351'),
                                                      ('30000000052', 'Profissional 52', 'Rua Profissional 52', '352'),
                                                      ('30000000053', 'Profissional 53', 'Rua Profissional 53', '353'),
                                                      ('30000000054', 'Profissional 54', 'Rua Profissional 54', '354'),
                                                      ('30000000055', 'Profissional 55', 'Rua Profissional 55', '355'),
                                                      ('30000000056', 'Profissional 56', 'Rua Profissional 56', '356'),
                                                      ('30000000057', 'Profissional 57', 'Rua Profissional 57', '357'),
                                                      ('30000000058', 'Profissional 58', 'Rua Profissional 58', '358'),
                                                      ('30000000059', 'Profissional 59', 'Rua Profissional 59', '359'),
                                                      ('30000000060', 'Profissional 60', 'Rua Profissional 60', '360');


INSERT INTO profissional_telefone (cpf_profissional, telefone) VALUES
                                                                   ('30000000001', '81960000001'),
                                                                   ('30000000002', '81960000002'),
                                                                   ('30000000003', '81960000003'),
                                                                   ('30000000004', '81960000004'),
                                                                   ('30000000005', '81960000005'),
                                                                   ('30000000006', '81960000006'),
                                                                   ('30000000007', '81960000007'),
                                                                   ('30000000008', '81960000008'),
                                                                   ('30000000009', '81960000009'),
                                                                   ('30000000010', '81960000010'),
                                                                   ('30000000011', '81960000011'),
                                                                   ('30000000012', '81960000012'),
                                                                   ('30000000013', '81960000013'),
                                                                   ('30000000014', '81960000014'),
                                                                   ('30000000015', '81960000015'),
                                                                   ('30000000016', '81960000016'),
                                                                   ('30000000017', '81960000017'),
                                                                   ('30000000018', '81960000018'),
                                                                   ('30000000019', '81960000019'),
                                                                   ('30000000020', '81960000020'),
                                                                   ('30000000021', '81960000021'),
                                                                   ('30000000022', '81960000022'),
                                                                   ('30000000023', '81960000023'),
                                                                   ('30000000024', '81960000024'),
                                                                   ('30000000025', '81960000025'),
                                                                   ('30000000026', '81960000026'),
                                                                   ('30000000027', '81960000027'),
                                                                   ('30000000028', '81960000028'),
                                                                   ('30000000029', '81960000029'),
                                                                   ('30000000030', '81960000030'),
                                                                   ('30000000031', '81960000031'),
                                                                   ('30000000032', '81960000032'),
                                                                   ('30000000033', '81960000033'),
                                                                   ('30000000034', '81960000034'),
                                                                   ('30000000035', '81960000035'),
                                                                   ('30000000036', '81960000036'),
                                                                   ('30000000037', '81960000037'),
                                                                   ('30000000038', '81960000038'),
                                                                   ('30000000039', '81960000039'),
                                                                   ('30000000040', '81960000040'),
                                                                   ('30000000041', '81960000041'),
                                                                   ('30000000042', '81960000042'),
                                                                   ('30000000043', '81960000043'),
                                                                   ('30000000044', '81960000044'),
                                                                   ('30000000045', '81960000045'),
                                                                   ('30000000046', '81960000046'),
                                                                   ('30000000047', '81960000047'),
                                                                   ('30000000048', '81960000048'),
                                                                   ('30000000049', '81960000049'),
                                                                   ('30000000050', '81960000050'),
                                                                   ('30000000051', '81960000051'),
                                                                   ('30000000052', '81960000052'),
                                                                   ('30000000053', '81960000053'),
                                                                   ('30000000054', '81960000054'),
                                                                   ('30000000055', '81960000055'),
                                                                   ('30000000056', '81960000056'),
                                                                   ('30000000057', '81960000057'),
                                                                   ('30000000058', '81960000058'),
                                                                   ('30000000059', '81960000059'),
                                                                   ('30000000060', '81960000060');


INSERT INTO barbeiro (cpf_profissional, especialidade, horario_trabalho, cpf_supervisor) VALUES
                                                                                             ('30000000001', 'Corte masculino', '09:00-18:00', NULL),
                                                                                             ('30000000002', 'Barba', '08:00-17:00', '30000000001'),
                                                                                             ('30000000003', 'Degrade', '09:00-18:00', '30000000001'),
                                                                                             ('30000000004', 'Coloracao', '08:00-17:00', '30000000001'),
                                                                                             ('30000000005', 'Corte e barba', '09:00-18:00', '30000000001'),
                                                                                             ('30000000006', 'Corte masculino', '08:00-17:00', '30000000001'),
                                                                                             ('30000000007', 'Barba', '09:00-18:00', '30000000001'),
                                                                                             ('30000000008', 'Degrade', '08:00-17:00', '30000000001'),
                                                                                             ('30000000009', 'Coloracao', '09:00-18:00', '30000000001'),
                                                                                             ('30000000010', 'Corte e barba', '08:00-17:00', '30000000001'),
                                                                                             ('30000000011', 'Corte masculino', '09:00-18:00', '30000000001'),
                                                                                             ('30000000012', 'Barba', '08:00-17:00', '30000000001'),
                                                                                             ('30000000013', 'Degrade', '09:00-18:00', '30000000001'),
                                                                                             ('30000000014', 'Coloracao', '08:00-17:00', '30000000001'),
                                                                                             ('30000000015', 'Corte e barba', '09:00-18:00', '30000000001'),
                                                                                             ('30000000016', 'Corte masculino', '08:00-17:00', '30000000001'),
                                                                                             ('30000000017', 'Barba', '09:00-18:00', '30000000001'),
                                                                                             ('30000000018', 'Degrade', '08:00-17:00', '30000000001'),
                                                                                             ('30000000019', 'Coloracao', '09:00-18:00', '30000000001'),
                                                                                             ('30000000020', 'Corte e barba', '08:00-17:00', '30000000001'),
                                                                                             ('30000000021', 'Corte masculino', '09:00-18:00', '30000000001'),
                                                                                             ('30000000022', 'Barba', '08:00-17:00', '30000000001'),
                                                                                             ('30000000023', 'Degrade', '09:00-18:00', '30000000001'),
                                                                                             ('30000000024', 'Coloracao', '08:00-17:00', '30000000001'),
                                                                                             ('30000000025', 'Corte e barba', '09:00-18:00', '30000000001'),
                                                                                             ('30000000026', 'Corte masculino', '08:00-17:00', '30000000001'),
                                                                                             ('30000000027', 'Barba', '09:00-18:00', '30000000001'),
                                                                                             ('30000000028', 'Degrade', '08:00-17:00', '30000000001'),
                                                                                             ('30000000029', 'Coloracao', '09:00-18:00', '30000000001'),
                                                                                             ('30000000030', 'Corte e barba', '08:00-17:00', '30000000001');

-- 30 GERENTES
INSERT INTO gerente (cpf_profissional, bonificacao) VALUES
                                                        ('30000000031', 500.00),
                                                        ('30000000032', 525.00),
                                                        ('30000000033', 550.00),
                                                        ('30000000034', 575.00),
                                                        ('30000000035', 600.00),
                                                        ('30000000036', 625.00),
                                                        ('30000000037', 650.00),
                                                        ('30000000038', 675.00),
                                                        ('30000000039', 700.00),
                                                        ('30000000040', 725.00),
                                                        ('30000000041', 750.00),
                                                        ('30000000042', 775.00),
                                                        ('30000000043', 800.00),
                                                        ('30000000044', 825.00),
                                                        ('30000000045', 850.00),
                                                        ('30000000046', 875.00),
                                                        ('30000000047', 900.00),
                                                        ('30000000048', 925.00),
                                                        ('30000000049', 950.00),
                                                        ('30000000050', 975.00),
                                                        ('30000000051', 1000.00),
                                                        ('30000000052', 1025.00),
                                                        ('30000000053', 1050.00),
                                                        ('30000000054', 1075.00),
                                                        ('30000000055', 1100.00),
                                                        ('30000000056', 1125.00),
                                                        ('30000000057', 1150.00),
                                                        ('30000000058', 1175.00),
                                                        ('30000000059', 1200.00),
                                                        ('30000000060', 1225.00);


INSERT INTO servico (nome, descricao, duracao, preco) VALUES
                                                          ('Corte Tradicional', 'Servico de corte tradicional', 15, 24.00),
                                                          ('Corte Degrade', 'Servico de corte degrade', 25, 28.00),
                                                          ('Corte Social', 'Servico de corte social', 35, 32.00),
                                                          ('Corte Infantil', 'Servico de corte infantil', 45, 36.00),
                                                          ('Corte Navalhado', 'Servico de corte navalhado', 55, 40.00),
                                                          ('Barba Tradicional', 'Servico de barba tradicional', 65, 44.00),
                                                          ('Barba Modelada', 'Servico de barba modelada', 15, 48.00),
                                                          ('Barboterapia', 'Servico de barboterapia', 25, 52.00),
                                                          ('Corte e Barba', 'Servico de corte e barba', 35, 56.00),
                                                          ('Pezinho', 'Servico de pezinho', 45, 60.00),
                                                          ('Sobrancelha', 'Servico de sobrancelha', 55, 64.00),
                                                          ('Pigmentacao de Barba', 'Servico de pigmentacao de barba', 65, 68.00),
                                                          ('Pigmentacao Capilar', 'Servico de pigmentacao capilar', 15, 72.00),
                                                          ('Platinado', 'Servico de platinado', 25, 76.00),
                                                          ('Luzes', 'Servico de luzes', 35, 80.00),
                                                          ('Hidratacao Capilar', 'Servico de hidratacao capilar', 45, 84.00),
                                                          ('Reconstrucao Capilar', 'Servico de reconstrucao capilar', 55, 88.00),
                                                          ('Relaxamento', 'Servico de relaxamento', 65, 92.00),
                                                          ('Alisamento', 'Servico de alisamento', 15, 96.00),
                                                          ('Coloracao', 'Servico de coloracao', 25, 100.00),
                                                          ('Camuflagem de Fios Brancos', 'Servico de camuflagem de fios brancos', 35, 104.00),
                                                          ('Limpeza Facial', 'Servico de limpeza facial', 45, 108.00),
                                                          ('Mascara Facial', 'Servico de mascara facial', 55, 112.00),
                                                          ('Esfoliacao Facial', 'Servico de esfoliacao facial', 65, 116.00),
                                                          ('Depilacao Nariz', 'Servico de depilacao nariz', 15, 120.00),
                                                          ('Depilacao Orelha', 'Servico de depilacao orelha', 25, 124.00),
                                                          ('Massagem Capilar', 'Servico de massagem capilar', 35, 128.00),
                                                          ('Lavagem Capilar', 'Servico de lavagem capilar', 45, 132.00),
                                                          ('Finalizacao', 'Servico de finalizacao', 55, 136.00),
                                                          ('Pacote Premium', 'Servico de pacote premium', 65, 140.00);


INSERT INTO agendamento (status, cpf_cliente, cpf_barbeiro) VALUES
                                                                ('Agendado', '10000000001', '30000000001'),
                                                                ('Confirmado', '10000000002', '30000000001'),
                                                                ('Concluido', '10000000003', '30000000001'),
                                                                ('Cancelado', '10000000004', '30000000001'),
                                                                ('Agendado', '10000000005', '30000000001'),
                                                                ('Confirmado', '10000000006', '30000000002'),
                                                                ('Concluido', '10000000007', '30000000002'),
                                                                ('Cancelado', '10000000008', '30000000002'),
                                                                ('Agendado', '10000000009', '30000000002'),
                                                                ('Confirmado', '10000000010', '30000000003'),
                                                                ('Concluido', '10000000011', '30000000003'),
                                                                ('Cancelado', '10000000012', '30000000003'),
                                                                ('Agendado', '10000000013', '30000000003'),
                                                                ('Confirmado', '10000000014', '30000000004'),
                                                                ('Concluido', '10000000015', '30000000004'),
                                                                ('Cancelado', '10000000016', '30000000004'),
                                                                ('Agendado', '10000000017', '30000000005'),
                                                                ('Confirmado', '10000000018', '30000000005'),
                                                                ('Concluido', '10000000019', '30000000005'),
                                                                ('Cancelado', '10000000020', '30000000006'),
                                                                ('Agendado', '10000000021', '30000000006'),
                                                                ('Confirmado', '10000000022', '30000000007'),
                                                                ('Concluido', '10000000023', '30000000007'),
                                                                ('Cancelado', '10000000024', '30000000008'),
                                                                ('Agendado', '10000000025', '30000000008'),
                                                                ('Confirmado', '10000000026', '30000000009'),
                                                                ('Concluido', '10000000027', '30000000010'),
                                                                ('Cancelado', '10000000028', '30000000011'),
                                                                ('Agendado', '10000000029', '30000000012'),
                                                                ('Confirmado', '10000000030', '30000000013');


INSERT INTO atendimento (horario_inicio, horario_final, id_agendamento) VALUES
                                                                            ('2026-09-01 08:00:00', '2026-09-01 08:50:00', 1),
                                                                            ('2026-09-02 09:00:00', '2026-09-02 10:00:00', 2),
                                                                            ('2026-09-03 10:00:00', '2026-09-03 10:40:00', 3),
                                                                            ('2026-09-04 11:00:00', '2026-09-04 11:50:00', 4),
                                                                            ('2026-09-05 12:00:00', '2026-09-05 13:00:00', 5),
                                                                            ('2026-09-06 13:00:00', '2026-09-06 13:40:00', 6),
                                                                            ('2026-09-07 14:00:00', '2026-09-07 14:50:00', 7),
                                                                            ('2026-09-08 15:00:00', '2026-09-08 16:00:00', 8),
                                                                            ('2026-09-09 08:00:00', '2026-09-09 08:40:00', 9),
                                                                            ('2026-09-10 09:00:00', '2026-09-10 09:50:00', 10),
                                                                            ('2026-09-11 10:00:00', '2026-09-11 11:00:00', 11),
                                                                            ('2026-09-12 11:00:00', '2026-09-12 11:40:00', 12),
                                                                            ('2026-09-13 12:00:00', '2026-09-13 12:50:00', 13),
                                                                            ('2026-09-14 13:00:00', '2026-09-14 14:00:00', 14),
                                                                            ('2026-09-15 14:00:00', '2026-09-15 14:40:00', 15),
                                                                            ('2026-09-16 15:00:00', '2026-09-16 15:50:00', 16),
                                                                            ('2026-09-17 08:00:00', '2026-09-17 09:00:00', 17),
                                                                            ('2026-09-18 09:00:00', '2026-09-18 09:40:00', 18),
                                                                            ('2026-09-19 10:00:00', '2026-09-19 10:50:00', 19),
                                                                            ('2026-09-20 11:00:00', '2026-09-20 12:00:00', 20),
                                                                            ('2026-09-21 12:00:00', '2026-09-21 12:40:00', 21),
                                                                            ('2026-09-22 13:00:00', '2026-09-22 13:50:00', 22),
                                                                            ('2026-09-23 14:00:00', '2026-09-23 15:00:00', 23),
                                                                            ('2026-09-24 15:00:00', '2026-09-24 15:40:00', 24),
                                                                            ('2026-09-25 08:00:00', '2026-09-25 08:50:00', 25),
                                                                            ('2026-09-26 09:00:00', '2026-09-26 10:00:00', 26),
                                                                            ('2026-09-27 10:00:00', '2026-09-27 10:40:00', 27),
                                                                            ('2026-09-28 11:00:00', '2026-09-28 11:50:00', 28),
                                                                            ('2026-09-29 12:00:00', '2026-09-29 13:00:00', 29),
                                                                            ('2026-09-30 13:00:00', '2026-09-30 13:40:00', 30);


INSERT INTO item_atendimento (id_atendimento, id_servico, preco_praticado) VALUES
                                                                               (1, 1, 24.00),
                                                                               (2, 1, 24.00),
                                                                               (3, 1, 24.00),
                                                                               (4, 1, 24.00),
                                                                               (5, 1, 24.00),
                                                                               (6, 1, 24.00),
                                                                               (7, 1, 24.00),
                                                                               (8, 1, 24.00),
                                                                               (9, 1, 24.00),
                                                                               (10, 2, 28.00),
                                                                               (11, 2, 28.00),
                                                                               (12, 2, 28.00),
                                                                               (13, 2, 28.00),
                                                                               (14, 2, 28.00),
                                                                               (15, 2, 28.00),
                                                                               (16, 2, 28.00),
                                                                               (17, 9, 56.00),
                                                                               (18, 9, 56.00),
                                                                               (19, 9, 56.00),
                                                                               (20, 9, 56.00),
                                                                               (21, 9, 56.00),
                                                                               (22, 9, 56.00),
                                                                               (23, 6, 44.00),
                                                                               (24, 6, 44.00),
                                                                               (25, 6, 44.00),
                                                                               (26, 6, 44.00),
                                                                               (27, 11, 64.00),
                                                                               (28, 11, 64.00),
                                                                               (29, 3, 32.00),
                                                                               (30, 4, 36.00);

SET FOREIGN_KEY_CHECKS = 1;
