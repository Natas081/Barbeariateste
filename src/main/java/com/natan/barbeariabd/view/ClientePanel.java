package com.natan.barbeariabd.view;

import com.natan.barbeariabd.dao.ClienteDAO;
import com.natan.barbeariabd.model.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ClientePanel extends JPanel {

    private final ClienteDAO dao =
            new ClienteDAO();

    private final JTextField cpf =
            new JTextField();

    private final JTextField nome =
            new JTextField();

    private final JTextField rua =
            new JTextField();

    private final JTextField numero =
            new JTextField();

    private final JTable tabela =
            new JTable();

    public ClientePanel() {

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

        JPanel formulario = new JPanel(new GridLayout(1, 4, 14, 0));
        formulario.add(campo("CPF", cpf));
        formulario.add(campo("Nome completo", nome));
        formulario.add(campo("Rua", rua));
        formulario.add(campo("Número", numero));

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

            List<Cliente> clientes =
                    dao.listar();

            DefaultTableModel model =
                    new DefaultTableModel(
                            new Object[]{
                                    "CPF",
                                    "Nome",
                                    "Rua",
                                    "Número"
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

            for (Cliente cliente : clientes) {

                model.addRow(
                        new Object[]{
                                cliente.getCpf(),
                                cliente.getNome(),
                                cliente.getRua(),
                                cliente.getNumero()
                        }
                );
            }

            tabela.setModel(model);

        } catch (Exception e) {

            erro(e);
        }
    }

    private Cliente ler() {

        if (
                cpf.getText().isBlank()
                        || nome.getText().isBlank()
                        || rua.getText().isBlank()
                        || numero.getText().isBlank()
        ) {

            throw new IllegalArgumentException(
                    "Preencha todos os campos."
            );
        }

        return new Cliente(
                cpf.getText().trim(),
                nome.getText().trim(),
                rua.getText().trim(),
                numero.getText().trim()
        );
    }

    private void inserir() {

        try {

            dao.inserir(
                    ler()
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
                    ler()
            );

            carregar();
            limpar();

        } catch (Exception e) {

            erro(e);
        }
    }

    private void excluir() {

        try {

            if (cpf.getText().isBlank()) {
                return;
            }

            dao.excluir(
                    cpf.getText().trim()
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

        cpf.setText(
                tabela
                        .getValueAt(
                                linha,
                                0
                        )
                        .toString()
        );

        nome.setText(
                tabela
                        .getValueAt(
                                linha,
                                1
                        )
                        .toString()
        );

        rua.setText(
                tabela
                        .getValueAt(
                                linha,
                                2
                        )
                        .toString()
        );

        numero.setText(
                tabela
                        .getValueAt(
                                linha,
                                3
                        )
                        .toString()
        );

        cpf.setEditable(false);
    }

    private void limpar() {

        cpf.setText("");
        nome.setText("");
        rua.setText("");
        numero.setText("");

        cpf.setEditable(true);

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