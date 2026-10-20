SELECT
    c.nome AS cliente,
    a.id_agendamento,
    a.status,
    p.nome AS barbeiro
FROM cliente c
         INNER JOIN agendamento a
                    ON a.cpf_cliente = c.cpf
         INNER JOIN barbeiro b
                    ON b.cpf_profissional = a.cpf_barbeiro
         INNER JOIN profissional p
                    ON p.cpf = b.cpf_profissional
ORDER BY a.id_agendamento;


SELECT
    p.nome AS barbeiro,
    COUNT(a.id_agendamento) AS total_agendamentos
FROM barbeiro b
         INNER JOIN profissional p
                    ON p.cpf = b.cpf_profissional
         LEFT JOIN agendamento a
                   ON a.cpf_barbeiro = b.cpf_profissional
GROUP BY p.cpf, p.nome
ORDER BY total_agendamentos DESC;


SELECT
    at.id_atendimento,
    c.nome AS cliente,
    COALESCE(SUM(ia.preco_praticado), 0) AS valor_total
FROM atendimento at
         LEFT JOIN agendamento ag
                   ON ag.id_agendamento = at.id_agendamento
         LEFT JOIN cliente c
                   ON c.cpf = ag.cpf_cliente
         LEFT JOIN item_atendimento ia
                   ON ia.id_atendimento = at.id_atendimento
GROUP BY at.id_atendimento, c.nome
ORDER BY at.id_atendimento;


SELECT
    s.nome AS servico,
    COUNT(ia.id_servico) AS quantidade_realizada,
    COALESCE(ROUND(AVG(ia.preco_praticado), 2), 0) AS preco_medio,
    COALESCE(ROUND(SUM(ia.preco_praticado), 2), 0) AS faturamento_total
FROM servico s
         LEFT JOIN item_atendimento ia
                   ON ia.id_servico = s.id_servico
GROUP BY s.id_servico, s.nome
ORDER BY quantidade_realizada DESC, faturamento_total DESC, s.nome;
