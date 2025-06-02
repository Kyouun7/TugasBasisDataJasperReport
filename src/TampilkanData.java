import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class TampilkanData extends JFrame {
    JTable table;
    DefaultTableModel model;

    public TampilkanData() {
        setTitle("Data Mahasiswa");
        setSize(600, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        model = new DefaultTableModel(new String[]{"Nama", "Total Kredit", "Nama Departemen"}, 0);
        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JButton btnCetak = new JButton("Cetak Laporan PDF");
        btnCetak.addActionListener(e -> {
            try {
                LaporanMahasiswa.main(new String[]{});
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Gagal mencetak laporan!");
            }
        });

        JPanel panelBawah = new JPanel();
        panelBawah.add(btnCetak);
        add(panelBawah, BorderLayout.SOUTH);

        ambilDataDariDatabase();
    }

    private void ambilDataDariDatabase() {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=University;integratedSecurity=true;encrypt=false";

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT name, tot_cred, dept_name FROM student");

            while (rs.next()) {
                String nama = rs.getString("name");
                int tot_cred = rs.getInt("tot_cred");
                String dept_name = rs.getString("dept_name");
                model.addRow(new Object[]{nama, tot_cred, dept_name});
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal mengambil data!");
        }
    }
}
