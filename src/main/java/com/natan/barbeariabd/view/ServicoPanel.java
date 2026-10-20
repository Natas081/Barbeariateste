package com.natan.barbeariabd.view;

import com.natan.barbeariabd.dao.ServicoDAO;
import com.natan.barbeariabd.model.Servico;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class ServicoPanel extends JPanel {

    private final ServicoDAO dao =
            new ServicoDAO();

    private final JTextField id =
            new JTextField();

    private final JTextField nome =
            new JTextField();

    private final JTextField descricao =
            new JTextField();

    private final JTextField duracao =
            new JTextField();

    private final JTextField preco =
            new JTextField();

    private final JTable tabela =
            new JTable();

    public ServicoPanel() {

        setLayout(
                new BorderLayout(10, 10)
        );

        setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );

        id.setEditable(false);

        JPanel formulario = new JPanel(new GridLayout(1, 5, 14, 0));
        formulario.add(campo("Código", id));
        formulario.add(campo("Nome do serviço", nome));
        formulario.add(campo("Descrição", descricao));
        formulario.add(campo("Duração (min)", duracao));
        formulario.add(campo("Preço (R$)", preco));

        JButton inserir =
                new JButton("Inserir");

        JButton atualizar =
                new JButton("Atualizar");

        JButton excluir =
                new JButton("Excluir");

        JButton limpar =
                new JButton("Limpar");

        JPanel botoes =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        botoes.add(inserir);
        botoes.add(atualizar);
        botoes.add(excluir);
        botoes.add(limpar);

        JPanel topo =
                new JPanel(
                        new BorderLayout(
                                0,
                                10
                        )
                );

        topo.add(
                formulario,
                BorderLayout.CENTER
        );

        topo.add(
                botoes,
                BorderLayout.SOUTH
        );

        tabela.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        add(
                topo,
                BorderLayout.NORTH
        );

        add(
                new JScrollPane(tabela),
                BorderLayout.CENTER
        );

        inserir.addActionListener(
                e -> inserir()
        );

        atualizar.addActionListener(
                e -> atualizar()
        );

        excluir.addActionListener(
                e -> excluir()
        );

        limpar.addActionListener(
                e -> limpar()
        );

        tabela
                .getSelectionModel()
                .addListSelectionListener(
                        e -> {

                            if (
                                    !e.getValueIsAdjusting()
                            ) {

                                selecionar();
                            }
                        }
                );

        carregar();
    }

    private JPanel campo(String label, JTextField input) {
        JPanel field = new JPanel(new BorderLayout(0, 8));
        field.add(new JLabel(label), BorderLayout.NORTH);
        field.add(input, BorderLayout.CENTER);
        return field;
    }

    private void carregar() {

        try {

            List<Servico> servicos =
                    dao.listar();

            DefaultTableModel model =
                    new DefaultTableModel(
                            new Object[]{
                                    "ID",
                                    "Nome",
                                    "Descrição",
                                    "Duração",
                                    "Preço"
                            },
                            0
                    ) {

                        @Override
                        public boolean isCellEditable(
                                int row,
                                int column
                        ) {
                            return false;
                        }
                    };

            for (Servico servico : servicos) {

                model.addRow(
                        new Object[]{
                                servico.getIdServico(),
                                servico.getNome(),
                                servico.getDescricao(),
                                servico.getDuracao(),
                                servico.getPreco()
                        }
                );
            }

            tabela.setModel(model);

        } catch (Exception e) {

            erro(e);
        }
    }

    private Servico ler(
            boolean exigirId
    ) {

        if (
                nome.getText().isBlank()
                        || descricao.getText().isBlank()
                        || duracao.getText().isBlank()
                        || preco.getText().isBlank()
        ) {

            throw new IllegalArgumentException(
                    "Preencha todos os campos."
            );
        }

        Integer codigo = null;

        if (exigirId) {

            codigo =
                    Integer.valueOf(
                            id.getText()
                    );
        }

        return new Servico(
                codigo,
                nome.getText().trim(),
                descricao.getText().trim(),
                Integer.parseInt(
                        duracao.getText().trim()
                ),
                new BigDecimal(
                        preco
                                .getText()
                                .trim()
                                .replace(
                                        ",",
                                        "."
                                )
                )
        );
    }

    private void inserir() {

        try {

            dao.inserir(
                    ler(false)
            );

            carregar();
            limpar();

        } catch (Exception e) {

            erro(e);
        }
    }

    private void atualizar() {

        try {

            dao.atualizar(
                    ler(true)
            );

            carregar();
            limpar();

        } catch (Exception e) {

            erro(e);
        }
    }

    private void excluir() {

        try {

            if (
                    id.getText().isBlank()
            ) {
                return;
            }

            dao.excluir(
                    Integer.parseInt(
                            id.getText()
                    )
            );

            carregar();
            limpar();

        } catch (Exception e) {

            erro(e);
        }
    }

    private void selecionar() {

        int linha =
                tabela.getSelectedRow();

        if (linha < 0) {
            return;
        }

        id.setText(
                tabela.getValueAt(
                        linha,
                        0
                ).toString()
        );

        nome.setText(
                tabela.getValueAt(
                        linha,
                        1
                ).toString()
        );

        descricao.setText(
                String.valueOf(
                        tabela.getValueAt(
                                linha,
                                2
                        )
                )
        );

        duracao.setText(
                tabela.getValueAt(
                        linha,
                        3
                ).toString()
        );

        preco.setText(
                tabela.getValueAt(
                        linha,
                        4
                ).toString()
        );
    }

    private void limpar() {

        id.setText("");
        nome.setText("");
        descricao.setText("");
        duracao.setText("");
        preco.setText("");

        tabela.clearSelection();
    }

    private void erro(Exception e) {

        JOptionPane.showMessageDialog(
                this,
                e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE
        );
    }
}